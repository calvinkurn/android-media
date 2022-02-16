package com.tokopedia.affiliate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDatePickerAdapterFactory
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeAdapter
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeDiffcallback
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeTypeFactory
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerInterface
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import java.text.SimpleDateFormat
import java.util.*

class AffiliateBottomDatePicker: BottomSheetUnify() , AffiliateDatePickerInterface {
    private var contentView: View? = null

    private var rangeSelected = TODAY
    private var rangeChangeInterface: AffiliateDatePickerRangeChangeInterface? = null
    private var identifier = IDENTIFIER_HOME


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {
        const val TODAY = "Hari ini"
        const val YESTERDAY = "Kemarin"
        const val SEVEN_DAYS = "7 Hari Terakhir"
        const val THIRTY_DAYS = "30 Hari Terakhir"
        const val IDENTIFIER_HOME = "home_fragment"
        const val IDENTIFIER_WITHDRAWAL = "income_fragment"
        const val IDENTIFIER = "indentifier"
        fun newInstance(selected: String,onRangeChangeInterface: AffiliateDatePickerRangeChangeInterface,identify: String = IDENTIFIER_WITHDRAWAL): AffiliateBottomDatePicker {
            return AffiliateBottomDatePicker().apply {
                arguments = Bundle().apply {
                    putString(IDENTIFIER,identify)
                }
                rangeSelected = selected
                rangeChangeInterface = onRangeChangeInterface
            }
        }
    }
    private var dateRV: RecyclerView? = null
    private var tickerCv: CardView? = null
    private fun init() {
        setBundleData()
        rangeChangeInterface = (parentFragment as? AffiliateDatePickerRangeChangeInterface)
        showCloseIcon = true
        showKnob = false
        contentView = View.inflate(context,
            R.layout.affiliate_date_range_picker_bottom_sheet, null)
        setTitle(getString(R.string.affiliate_date_picker_header))
        dateRV = contentView?.findViewById(R.id.date_picker_rv)
        tickerCv = contentView?.findViewById(R.id.affiliate_filter_announcement_ticker_cv)
        setTicker()
        setData()
        initClickListener(contentView)
        setChild(contentView)
    }

    private fun setBundleData() {
        arguments?.let {
            identifier = it.getString(IDENTIFIER, IDENTIFIER_WITHDRAWAL)
        }
    }

    private fun setTicker() {
        if(identifier == IDENTIFIER_HOME){
            when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                in 0..9 -> tickerCv?.show()
                else -> tickerCv?.hide()
            }
        }

    }

    private fun initClickListener(contentView: View?) {
        contentView?.findViewById<UnifyButton>(R.id.cnf_btn)?.setOnClickListener {
            itemList.forEach { visitable ->
                if((visitable as AffiliateDateRangePickerModel).dateRange.isSelected){
                    rangeChangeInterface?.rangeChanged((visitable as AffiliateDateRangePickerModel).dateRange)
                    dismiss()
                }
            }
        }
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
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(TODAY,rangeSelected == TODAY,"0",DateUtils().getMessage(TODAY,context),identifier == IDENTIFIER_HOME)))
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(YESTERDAY, rangeSelected == YESTERDAY,"1",DateUtils().getMessage(YESTERDAY,context),identifier == IDENTIFIER_HOME)))
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(SEVEN_DAYS,rangeSelected == SEVEN_DAYS,"7",DateUtils().getMessage(SEVEN_DAYS,context),identifier == IDENTIFIER_HOME)))
        itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(THIRTY_DAYS,rangeSelected == THIRTY_DAYS,"30",DateUtils().getMessage(THIRTY_DAYS,context),identifier == IDENTIFIER_HOME)))
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