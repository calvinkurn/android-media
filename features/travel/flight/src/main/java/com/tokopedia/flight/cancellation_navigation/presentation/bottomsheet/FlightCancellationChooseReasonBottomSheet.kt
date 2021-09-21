package com.tokopedia.flight.cancellation_navigation.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationChooseReasonAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationChooseReasonViewHolder
import com.tokopedia.flight.cancellation_navigation.presentation.viewmodel.FlightCancellationBottomSheetChooseReasonViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by furqan on 27/05/2021
 */
class FlightCancellationChooseReasonBottomSheet : BottomSheetUnify(),
        FlightCancellationChooseReasonViewHolder.Listener {

    private lateinit var flightCancellationComponent: FlightCancellationComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var chooseReasonViewModel: FlightCancellationBottomSheetChooseReasonViewModel

    var listener: FlightChooseReasonListener? = null

    private lateinit var mChildView: View
    private lateinit var adapter: BaseListAdapter<FlightCancellationPassengerEntity.Reason, FlightCancellationChooseReasonAdapterTypeFactory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            chooseReasonViewModel = viewModelProvider.get(FlightCancellationBottomSheetChooseReasonViewModel::class.java)
            chooseReasonViewModel.selectedReason = arguments?.getParcelable(EXTRA_SELECTED_REASON)
            chooseReasonViewModel.loadReasonList()
        }

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chooseReasonViewModel.reasonList.observe(viewLifecycleOwner, Observer {
            renderList(it)
        })
    }

    override fun isItemChecked(reason: FlightCancellationPassengerEntity.Reason): Boolean =
            chooseReasonViewModel.isReasonChecked(reason)

    private fun initBottomSheet() {
        showCloseIcon = true
        isFullpage = true
        setTitle(getString(R.string.flight_cancellation_reason_title))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_choose_reason, null)
        setChild(mChildView)
    }

    private fun setupRecyclerView() {
        adapter = BaseListAdapter(FlightCancellationChooseReasonAdapterTypeFactory(this))
        adapter.setOnAdapterInteractionListener {
            chooseReasonViewModel.selectedReason = it
            listener?.onReasonChoosed(it)
            this@FlightCancellationChooseReasonBottomSheet.dismiss()
        }

        view?.let {
            val recyclerView = it.findViewById<RecyclerView>(R.id.rv_flight_choose_reason)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }
    }

    private fun renderList(data: List<FlightCancellationPassengerEntity.Reason>) {
        adapter.clearAllElements()
        adapter.addElement(data)
    }

    private fun initInjector() {
        activity?.let {
            if (!::flightCancellationComponent.isInitialized) {
                flightCancellationComponent = DaggerFlightCancellationComponent.builder()
                        .flightComponent(FlightComponentInstance.getFlightComponent(it.application))
                        .build()
            }
            flightCancellationComponent.inject(this)
        }
    }

    interface FlightChooseReasonListener {
        fun onReasonChoosed(selectedReason: FlightCancellationPassengerEntity.Reason)
    }

    companion object {
        const val EXTRA_SELECTED_REASON = "EXTRA_SELECTED_REASON"
        const val TAG_CANCELLATION_CHOOSE_REASON = "TAG_FLIGHT_CANCELLATION_CHOOSE_REASON"

        fun getInstance(selectedReason: FlightCancellationPassengerEntity.Reason?): FlightCancellationChooseReasonBottomSheet =
                FlightCancellationChooseReasonBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_SELECTED_REASON, selectedReason)
                    }
                }
    }

}