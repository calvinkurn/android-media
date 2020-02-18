package com.tokopedia.flight.filter.presentation.bottomsheets

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterAirlineAdapterTypeFactory
import com.tokopedia.flight.search.presentation.fragment.OnFlightFilterListener
import com.tokopedia.flight.search.presentation.model.resultstatistics.AirlineStat
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_flight_filter_airline.view.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 17/02/2020
 */
class FlightFilterAirlineBottomSheet : BottomSheetUnify(),
        BaseCheckableViewHolder.CheckableInteractionListener,
        BaseListCheckableAdapter.OnCheckableAdapterListener<AirlineStat> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter: BaseListCheckableAdapter<AirlineStat, BaseListCheckableTypeFactory<AirlineStat>>
    lateinit var listener: OnFlightFilterListener

    lateinit var mChildView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        initAdapter()
        initRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (::listener.isInitialized && listener.flightSearchStatisticModel != null) {
            renderList(listener.flightSearchStatisticModel.airlineStatList)
        }
    }

    override fun isChecked(position: Int): Boolean = adapter.isChecked(position)

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        adapter.updateListByCheck(isChecked, position)
    }

    override fun onItemChecked(airlineStat: AirlineStat, isChecked: Boolean) {
//        val flightFilterModel = listener.flightFilterModel
//        val airlineStatList = adapter.checkedDataList
//        val airlineList = Observable.from(airlineStatList).map { airlineStat -> airlineStat.airlineDB.id }.toList().toBlocking().first()
//        flightFilterModel.airlineList = airlineList
//        listener.onFilterModelChanged(flightFilterModel)
    }

    private fun initBottomSheet() {
        try {
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            customPeekHeight = displayMetrics.heightPixels
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.airline))
        setAction(getString(R.string.reset)) {
            // TODO: Add Function for reset button
        }

        mChildView = View.inflate(requireContext(), R.layout.fragment_flight_filter_airline, null)
        setChild(mChildView)
    }

    private fun initAdapter() {
        val typeFactory = FlightFilterAirlineAdapterTypeFactory(this)
        adapter = BaseListCheckableAdapter<AirlineStat, BaseListCheckableTypeFactory<AirlineStat>>(typeFactory, this)
    }

    private fun initRecyclerView() {
        with(mChildView) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            recyclerView.adapter = adapter
        }
    }

    private fun renderList(list: List<AirlineStat>) {
        adapter.addElement(list)

        val flightFilterModel = listener.flightFilterModel
        val checkedPositionList = HashSet<Int>()
        if (flightFilterModel != null) {
            val airlineList = flightFilterModel.airlineList
            if (airlineList != null) {
                var i = 0
                val sizei = airlineList.size
                while (i < sizei) {
                    val selectedAirline = airlineList[i]
                    var j = 0
                    val sizej = list.size
                    while (j < sizej) {
                        val airlineStat = list[j]
                        if (airlineStat.airlineDB.id == selectedAirline) {
                            checkedPositionList.add(j)
                            break
                        }
                        j++
                    }
                    i++
                }
            }
        }

        adapter.setCheckedPositionList(checkedPositionList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG_FILTER_AIRLINE = "TagFilterAirlineBottomSheet"

        fun getInstance(): FlightFilterAirlineBottomSheet = FlightFilterAirlineBottomSheet()
    }

}