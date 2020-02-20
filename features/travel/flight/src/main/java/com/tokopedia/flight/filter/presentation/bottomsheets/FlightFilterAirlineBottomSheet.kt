package com.tokopedia.flight.filter.presentation.bottomsheets

import android.os.Bundle
import android.view.View
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

/**
 * @author by furqan on 17/02/2020
 */
class FlightFilterAirlineBottomSheet : BottomSheetUnify(),
        BaseCheckableViewHolder.CheckableInteractionListener,
        BaseListCheckableAdapter.OnCheckableAdapterListener<AirlineStat> {

    lateinit var listener: OnFlightFilterListener

    private lateinit var mChildView: View
    private lateinit var adapter: BaseListCheckableAdapter<AirlineStat, BaseListCheckableTypeFactory<AirlineStat>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        initAdapter()
        initView()
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
        // do nothing
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.airline))
        setAction(getString(R.string.flight_reset_label)) {
            resetAirlineFilter()
        }

        mChildView = View.inflate(requireContext(), R.layout.fragment_flight_filter_airline, null)
        setChild(mChildView)
    }

    private fun initAdapter() {
        val typeFactory = FlightFilterAirlineAdapterTypeFactory(this)
        adapter = BaseListCheckableAdapter<AirlineStat, BaseListCheckableTypeFactory<AirlineStat>>(typeFactory, this)
    }

    private fun initView() {
        with(mChildView) {
            rvFlightFilterAirline.setHasFixedSize(true)
            rvFlightFilterAirline.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvFlightFilterAirline.adapter = adapter

            btnFlightFilterAirlineSave.setOnClickListener {
                saveAirlineFilter()
            }
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

    private fun saveAirlineFilter() {
        val filterModel = listener.flightFilterModel
        val checkedAirlineList = adapter.checkedDataList

        filterModel.airlineList = checkedAirlineList.map {
            it.airlineDB.id
        }.toList()
        listener.onFilterModelChanged(filterModel)

        if (isAdded) {
            dismiss()
        }
    }

    private fun resetAirlineFilter() {
        val filterModel = listener.flightFilterModel
        filterModel.airlineList = arrayListOf()
        adapter.resetCheckedItemSet()
        adapter.notifyDataSetChanged()
        listener.onFilterModelChanged(filterModel)
    }

    companion object {
        const val TAG_FILTER_AIRLINE = "TagFilterAirlineBottomSheet"

        fun getInstance(): FlightFilterAirlineBottomSheet = FlightFilterAirlineBottomSheet()
    }

}