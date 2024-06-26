package com.tokopedia.flight.cancellation_navigation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationResponseEntity
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationViewHolder
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationPassengerFragment
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationReasonAndAttachmentModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationPassengerViewModel
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationReasonFragment.Companion.EXTRA_CANCELLATION_MODEL
import com.tokopedia.flight.databinding.FragmentFlightCancellationBinding
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by furqan on 10/07/2020
 */
class FlightCancellationPassengerFragment : BaseListFragment<FlightCancellationModel, FlightCancellationAdapterTypeFactory>(),
        FlightCancellationViewHolder.FlightCancellationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightCancellationPassengerViewModel: FlightCancellationPassengerViewModel

    private var isFirstRelationCheck: Boolean = false
    private var invoiceId: String = ""
    private lateinit var flightCancellationJourneyList: List<FlightCancellationResponseEntity>
    private var flightCancellationWrapperModel: FlightCancellationWrapperModel = FlightCancellationWrapperModel()
    private lateinit var passengerRelationMap: Map<String, FlightCancellationPassengerModel>

    private var binding by autoClearedNullable<FragmentFlightCancellationBinding>()

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()

        savedInstanceState?.let {
            if (it.containsKey(EXTRA_FIRST_CHECK)) isFirstRelationCheck = it.getBoolean(EXTRA_FIRST_CHECK)
        }

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            flightCancellationPassengerViewModel = viewModelProvider.get(FlightCancellationPassengerViewModel::class.java)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFlightCancellationBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initVariable()

        super.onViewCreated(view, savedInstanceState)

        binding?.buttonSubmit?.setOnClickListener {
            onNextButtonClicked()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightCancellationPassengerViewModel.cancellationPassengerList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    if (it.data.isNotEmpty()) {
                        renderCancellationList(it.data)
                        setupNextButton()
                    } else {
                        activity?.let { mActivity ->
                            val intent = Intent()
                            intent.putExtra(FlightCancellationPassengerFragment.EXTRA_IS_CANCEL_ERROR, true)
                            mActivity.setResult(Activity.RESULT_CANCELED, intent)
                            mActivity.finish()
                        }
                    }
                }
                is Fail -> {
                    showGetListError(it.throwable)
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(EXTRA_FIRST_CHECK, isFirstRelationCheck)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_REASON_AND_PROOF_CANCELLATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    closeCancellationPage()
                }
            }
        }
    }

    override fun getAdapterTypeFactory(): FlightCancellationAdapterTypeFactory =
            FlightCancellationAdapterTypeFactory(this)

    override fun createAdapterInstance(): BaseListAdapter<FlightCancellationModel, FlightCancellationAdapterTypeFactory> {
        val adapter = super.createAdapterInstance()
        val errorNetworkModel = adapter.errorNetworkModel
        errorNetworkModel.iconDrawableRes = R.drawable.ic_flight_empty_state
        errorNetworkModel.onRetryListener = this
        adapter.errorNetworkModel = errorNetworkModel
        return adapter
    }

    override fun onItemClicked(t: FlightCancellationModel?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() =
            getComponent(FlightCancellationComponent::class.java)
                    .inject(this)

    override fun loadData(page: Int) {
        if (flightCancellationPassengerViewModel.selectedCancellationPassengerList.size == 0) {
            adapter.clearAllElements()
            showLoading()
            flightCancellationPassengerViewModel.getCancellablePassenger(invoiceId, flightCancellationJourneyList)
        }
    }

    override fun onPassengerChecked(passengerModel: FlightCancellationPassengerModel, position: Int) {
        if (flightCancellationPassengerViewModel.checkPassenger(passengerModel, position) && isFirstRelationCheck) {
            showAutoCheckDialog()
        }
        setupNextButton()
    }

    override fun onPassengerUnchecked(passengerModel: FlightCancellationPassengerModel, position: Int) {
        flightCancellationPassengerViewModel.uncheckPassenger(passengerModel, position)
        setupNextButton()
    }

    override fun isChecked(passengerModel: FlightCancellationPassengerModel): Boolean =
            flightCancellationPassengerViewModel.isPassengerChecked(passengerModel)

    override fun onRetryClicked() {
        showLoading()
        flightCancellationPassengerViewModel.getCancellablePassenger(invoiceId, flightCancellationJourneyList)
    }

    private fun initVariable() {
        invoiceId = arguments?.getString(EXTRA_INVOICE_ID) ?: ""
        flightCancellationJourneyList = arguments?.getParcelableArrayList(EXTRA_CANCEL_JOURNEY)
                ?: arrayListOf()
        flightCancellationWrapperModel.invoiceId = invoiceId
        flightCancellationWrapperModel.cancellationReasonAndAttachmentModel = FlightCancellationReasonAndAttachmentModel()
        passengerRelationMap = hashMapOf()
    }

    private fun renderCancellationList(cancellationModelList: List<FlightCancellationModel>) {
        hideLoading()
        hideFullLoading()
        if (adapter.dataSize != cancellationModelList.size) {
            renderList(cancellationModelList)
        }

        if (cancellationModelList.isNotEmpty()) {
            binding?.btnContainer?.visibility = View.VISIBLE
        } else {
            binding?.btnContainer?.visibility = View.GONE
        }
    }

    private fun showAutoCheckDialog() {
        isFirstRelationCheck = false
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(R.string.flight_cancellation_auto_check_dialog_title)
        dialog.setDescription(getString(R.string.flight_cancellation_auto_check_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.flight_cancellation_auto_check_dialog_button))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun hideFullLoading() {
        binding?.btnContainer?.visibility = View.VISIBLE
    }

    private fun setupNextButton() {
        if (flightCancellationPassengerViewModel.canGoNext()) {
            enableNextButton()
        } else {
            disableNextButton()
        }
    }

    private fun enableNextButton() {
        binding?.buttonSubmit?.isEnabled = true
    }

    private fun disableNextButton() {
        binding?.buttonSubmit?.isEnabled = false
    }

    private fun onNextButtonClicked() {
        var canGoNext = false

        for (cancellation in flightCancellationPassengerViewModel.selectedCancellationPassengerList) {
            if (cancellation.passengerModelList.size > 0) {
                canGoNext = true
            }
        }

        if (canGoNext) {
            flightCancellationPassengerViewModel.trackOnNext()
            navigateToReasonPage()
        } else {
            showShouldChooseAtLeastOnePassengerError()
        }
    }

    private fun navigateToReasonPage() {
        flightCancellationWrapperModel.cancellationList = flightCancellationPassengerViewModel.selectedCancellationPassengerList
        view?.let {
            it.findNavController().navigate(R.id.action_flightCancellationPassenger_to_CancellationReason,
                    Bundle().apply {
                        putParcelable(EXTRA_CANCELLATION_MODEL, flightCancellationWrapperModel)
                    })
        }
    }

    private fun showShouldChooseAtLeastOnePassengerError() {
        Toaster.build(requireView(), getString(R.string.flight_cancellation_should_choose_at_least_one_passenger_error),
                Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun closeCancellationPage() {
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    companion object {
        const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        const val EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY"
        const val EXTRA_FIRST_CHECK = "EXTRA_FIRST_CHECK"

        const val EXTRA_IS_CANCEL_ERROR = "EXTRA_IS_CANCEL_ERROR"

        const val REQUEST_REASON_AND_PROOF_CANCELLATION = 1111
    }
}