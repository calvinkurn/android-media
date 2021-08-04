package com.tokopedia.flight.cancellation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationChooseReasonAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationChooseReasonViewHolder
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationChooseReasonViewModel
import javax.inject.Inject

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationChooseReasonFragment : BaseListFragment<FlightCancellationPassengerEntity.Reason, FlightCancellationChooseReasonAdapterTypeFactory>(),
        FlightCancellationChooseReasonViewHolder.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightCancellationChooseReasonViewModel: FlightCancellationChooseReasonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightCancellationChooseReasonViewModel = viewModelProvider.get(FlightCancellationChooseReasonViewModel::class.java)
            flightCancellationChooseReasonViewModel.selectedReason = arguments?.getParcelable(EXTRA_SELECTED_REASON)
            flightCancellationChooseReasonViewModel.loadReasonList()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightCancellationChooseReasonViewModel.reasonList.observe(viewLifecycleOwner, Observer {
            renderList(it)
        })
    }

    override fun getAdapterTypeFactory(): FlightCancellationChooseReasonAdapterTypeFactory =
            FlightCancellationChooseReasonAdapterTypeFactory(this)

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightCancellationComponent::class.java).inject(this)
    }

    override fun onItemClicked(reason: FlightCancellationPassengerEntity.Reason) {
        flightCancellationChooseReasonViewModel.selectedReason = reason
        adapter.notifyDataSetChanged()

        onReasonSelected()
    }

    override fun loadData(page: Int) {}

    override fun isItemChecked(reason: FlightCancellationPassengerEntity.Reason): Boolean =
            flightCancellationChooseReasonViewModel.isReasonChecked(reason)

    private fun onReasonSelected() {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_REASON, flightCancellationChooseReasonViewModel.selectedReason)
        requireActivity().setResult(Activity.RESULT_OK, intent)
        requireActivity().finish()
    }

    companion object {
        const val EXTRA_SELECTED_REASON = "EXTRA_SELECTED_REASON"

        fun createInstance(selectedReason: FlightCancellationPassengerEntity.Reason?): FlightCancellationChooseReasonFragment =
                FlightCancellationChooseReasonFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_SELECTED_REASON, selectedReason)
                    }
                }
    }

}