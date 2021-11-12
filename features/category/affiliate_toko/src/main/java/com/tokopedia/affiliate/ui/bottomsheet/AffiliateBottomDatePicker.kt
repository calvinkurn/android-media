package com.tokopedia.affiliate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDatePickerAdapterFactory
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeAdapter
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeDiffcallback
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeTypeFactory
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerInterface
import com.tokopedia.affiliate.model.AffiliateDatePickerData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.ArrayList

class AffiliateBottomDatePicker: BottomSheetUnify() , AffiliateDatePickerInterface {
    private var contentView: View? = null

    private var rangeSelected = TODAY



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {
        const val TODAY = "Hari ini"
        const val YESTERDAY = "Kemarin"
        const val SEVEN_DAYS = "7 Hari Terakhir"
        const val THIRTY_DAYS = "30 Hari Terakhir"
        fun newInstance(selected: String): AffiliateBottomDatePicker {
            return AffiliateBottomDatePicker().apply {
                rangeSelected = selected
            }
        }
    }
    private var dateRV:RecyclerView?=null
    private fun init() {
        showCloseIcon = true
        showKnob = false
        contentView = View.inflate(context,
            R.layout.affiliate_date_range_picker_bottom_sheet, null)
        setTitle("Pilih tanggal")
        dateRV = contentView?.findViewById(R.id.date_picker_rv)
        setData()
        setChild(contentView)
    }
    private val adapter by lazy {
        val asyncDifferConfig = AsyncDifferConfig.Builder(AffiliateDateRangeDiffcallback())
            .build()
        AffiliateDateRangeAdapter(asyncDifferConfig, AffiliateDatePickerAdapterFactory(this))
    }

    private fun setData() {
       dateRV?.layoutManager = LinearLayoutManager(context)
       dateRV?.adapter = adapter
       getData()
       adapter.submitList(itemList as List<Visitable<*>>?)
    }
    private val itemList: ArrayList<Visitable<AffiliateDateRangeTypeFactory>> = ArrayList()
    private fun getData() {
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(TODAY,rangeSelected == TODAY)))
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(YESTERDAY, rangeSelected == YESTERDAY)))
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(SEVEN_DAYS,rangeSelected == SEVEN_DAYS)))
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(THIRTY_DAYS,rangeSelected == THIRTY_DAYS)))
    }

    override fun onDateRangeClicked(position: Int) {
        updateItem(position)
        adapter.notifyDataSetChanged()
    }

    private fun updateItem(position: Int) {
        itemList.forEachIndexed { index, visitable ->
            (visitable as AffiliateDateRangePickerModel).dateRange.isSelected = index == position
        }
    }
}