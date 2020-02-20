package com.tokopedia.flight.filter.presentation.bottomsheets

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.di.DaggerFlightFilterComponent
import com.tokopedia.flight.filter.di.FlightFilterComponent
import com.tokopedia.flight.filter.presentation.OnFlightFilterListener
import com.tokopedia.flight.filter.presentation.viewmodel.FlightFilterViewModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_flight_filter.view.*
import javax.inject.Inject

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterBottomSheet : BottomSheetUnify(), OnFlightFilterListener {

    var listener: FlightFilterBottomSheetListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightFilterViewModel: FlightFilterViewModel

    private lateinit var flightFilterComponent: FlightFilterComponent
    private lateinit var mChildView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightFilterViewModel = viewModelProvider.get(FlightFilterViewModel::class.java)
            if (arguments?.containsKey(ARG_FILTER_MODEL) == true) {
                flightFilterViewModel.init(
                        arguments?.getParcelable(ARG_FILTER_MODEL) as FlightFilterModel,
                        arguments?.getBoolean(ARG_IS_RETURN) ?: false)
            }
        }

        initBottomSheet()
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightFilterViewModel.statisticModel.observe(this, Observer {
            // TODO: Render Filter Layout
        })
    }

    override fun getFlightSearchStaticticModel(): FlightSearchStatisticModel? = flightFilterViewModel.statisticModel.value

    override fun getFlightFilterModel(): FlightFilterModel? = flightFilterViewModel.filterModel.value

    private fun initInjector() {
        if (!::flightFilterComponent.isInitialized) {
            flightFilterComponent = DaggerFlightFilterComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(requireActivity().application as Application))
                    .build()
        }
        flightFilterComponent.inject(this)
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.flight_filter_label))
        setAction(getString(R.string.flight_reset_label)) {
            flightFilterViewModel.resetFilter()
        }

        mChildView = View.inflate(requireContext(), R.layout.fragment_flight_filter, null)
        setChild(mChildView)
    }

    private fun initView() {
        with(mChildView) {
            rvFlightFilter.setHasFixedSize(true)
            rvFlightFilter.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            btnFlightFilterSave.setOnClickListener {
                listener?.onSaveFilter(flightFilterViewModel.filterModel.value)
            }
        }
    }

    companion object {
        const val TAG_FILTER = "TagFilterBottomSheet"

        private const val ARG_FILTER_MODEL = "ARG_FILTER_MODEL"
        private const val ARG_IS_RETURN = "ARG_IS_RETURN"

        fun getInstance(filterModel: FlightFilterModel, isReturn: Boolean = false): FlightFilterBottomSheet =
                FlightFilterBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putParcelable(ARG_FILTER_MODEL, filterModel)
                        putBoolean(ARG_IS_RETURN, isReturn)
                    }
                }
    }

    interface FlightFilterBottomSheetListener {
        fun onSaveFilter(flightFilterModel: FlightFilterModel?)
    }

}