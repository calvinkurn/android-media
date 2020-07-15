package com.tokopedia.flight.cancellationV2.presentation.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentModel
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperModel
import com.tokopedia.flight.cancellationV2.di.FlightCancellationComponent
import com.tokopedia.flight.cancellationV2.presentation.adapter.FlightCancellationAdapterTypeFactory
import com.tokopedia.flight.cancellationV2.presentation.adapter.viewholder.FlightCancellationViewHolder
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.cancellationV2.presentation.viewmodel.FlightCancellationPassengerViewModel
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import kotlinx.android.synthetic.main.fragment_flight_cancellation.*
import java.util.*
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
    private lateinit var flightCancellationJourneyList: List<FlightCancellationJourney>
    private var flightCancellationWrapperModel: FlightCancellationWrapperModel = FlightCancellationWrapperModel()
    private lateinit var passengerRelationMap: Map<String, FlightCancellationPassengerModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            if (it.containsKey(EXTRA_FIRST_CHECK)) isFirstRelationCheck = it.getBoolean(EXTRA_FIRST_CHECK)
        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightCancellationPassengerViewModel = viewModelProvider.get(FlightCancellationPassengerViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_flight_cancellation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initVariable()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(EXTRA_FIRST_CHECK, isFirstRelationCheck)
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

    override fun initInjector() = getComponent(FlightCancellationComponent::class.java).inject(this)

    override fun loadData(page: Int) {
        adapter.clearAllElements()
        showLoading()
        flightCancellationPassengerViewModel.getCancellablePassenger(invoiceId)
    }

    override fun onPassengerChecked(passengerModel: FlightCancellationPassengerModel, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPassengerUnchecked(passengerModel: FlightCancellationPassengerModel, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isChecked(passengerModel: FlightCancellationPassengerModel): Boolean {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun onRetryClicked() {
        showLoading()
        flightCancellationPassengerViewModel.getCancellablePassenger(invoiceId)
    }

    private fun initVariable() {
        invoiceId = arguments?.getString(EXTRA_INVOICE_ID) ?: ""
        flightCancellationJourneyList = arguments?.getParcelableArrayList(EXTRA_CANCEL_JOURNEY)
                ?: arrayListOf()
        flightCancellationWrapperModel.invoice = invoiceId
        flightCancellationWrapperModel.cancellationReasonAndAttachment = FlightCancellationReasonAndAttachmentModel()
        passengerRelationMap = hashMapOf()
    }

    private fun showFullLoading() {
        btn_container.visibility = View.GONE
    }

    private fun hideFullLoading() {
        btn_container.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        const val EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY"
        const val EXTRA_FIRST_CHECK = "EXTRA_FIRST_CHECK"

        const val REQUEST_REFUND_CANCELLATION = 1
        const val REQUEST_REASON_AND_PROOF_CANCELLATION = 2

        fun createInstance(invoiceId: String,
                           flightCancellationJourney: List<FlightCancellationJourney>)
                : FlightCancellationPassengerFragment =
                FlightCancellationPassengerFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                        putParcelableArrayList(EXTRA_CANCEL_JOURNEY, flightCancellationJourney as ArrayList<out Parcelable>)
                    }
                }
    }
}