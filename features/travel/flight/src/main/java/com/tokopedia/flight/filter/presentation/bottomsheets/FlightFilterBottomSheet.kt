package com.tokopedia.flight.filter.presentation.bottomsheets

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.flight.filter.presentation.OnFlightFilterListener
import com.tokopedia.flight.filter.presentation.viewmodel.FlightFilterViewModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterBottomSheet : BottomSheetUnify(), OnFlightFilterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightFilterViewModel: FlightFilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightFilterViewModel = viewModelProvider.get(FlightFilterViewModel::class.java)
            if (arguments?.containsKey(ARG_FILTER_MODEL) == true) {
                flightFilterViewModel.init(arguments?.getParcelable(ARG_FILTER_MODEL) as FlightFilterModel)
            }
        }
    }

    override fun getFlightSearchStaticticModel(): FlightSearchStatisticModel? = flightFilterViewModel.statisticModel.value

    override fun getFlightFilterModel(): FlightFilterModel? = flightFilterViewModel.filterModel.value

    private fun resetFilter() {
        flightFilterViewModel.resetFilter()
    }

    companion object {
        const val TAG_FILTER = "TagFilterBottomSheet"

        private const val ARG_FILTER_MODEL = "ARG_FILTER_MODEL"

        fun getInstance(filterModel: FlightFilterModel): FlightFilterBottomSheet =
                FlightFilterBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putParcelable(ARG_FILTER_MODEL, filterModel)
                    }
                }
    }

}