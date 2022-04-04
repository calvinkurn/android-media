package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDatePickerAdapterFactory
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeAdapter
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeDiffcallback
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerInterface
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerViewModel
import com.tokopedia.affiliate.viewmodel.AffiliateDatePickerBottomSheetViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import java.util.*
import javax.inject.Inject

class AffiliateBottomDatePicker: BottomSheetUnify() , AffiliateDatePickerInterface {
    private var contentView: View? = null

    private var rangeSelected = TODAY
    private var rangeChangeInterface: AffiliateDatePickerRangeChangeInterface? = null
    private var identifier = IDENTIFIER_HOME

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliateDatePickerBottomSheetViewModel: AffiliateDatePickerBottomSheetViewModel

    private fun initInject() {
        getComponent().injectDateFilterBottomSheet(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
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
        affiliateDatePickerBottomSheetViewModel = ViewModelProvider(this, viewModelProvider).get(AffiliateDatePickerBottomSheetViewModel::class.java)
        setBundleData()
        initObserver()
        rangeChangeInterface = (parentFragment as? AffiliateDatePickerRangeChangeInterface)
        showCloseIcon = true
        showKnob = false
        contentView = View.inflate(context,
            R.layout.affiliate_date_range_picker_bottom_sheet, null)
        setTitle(getString(R.string.affiliate_date_picker_header))
        dateRV = contentView?.findViewById(R.id.date_picker_rv)
        tickerCv = contentView?.findViewById(R.id.affiliate_filter_announcement_ticker_cv)
        setData()
        initClickListener(contentView)
        setChild(contentView)
    }

    private fun initObserver() {
        affiliateDatePickerBottomSheetViewModel.getAffiliateFilterItems().observe(this,{list ->
            list?.let {
                adapter.submitList(list as List<Visitable<*>>?)
            }
        })
        affiliateDatePickerBottomSheetViewModel.getShimmerVisibility().observe(this,{shimmer ->
            if(shimmer!=null && shimmer){
                val itemList: ArrayList<Visitable<AffiliateDateRangeTypeFactory>> = ArrayList()
                repeat(4){ itemList.add(AffiliateShimmerViewModel()) }
                adapter.submitList(itemList as List<Visitable<*>>?)
            }
        })
        affiliateDatePickerBottomSheetViewModel.getTickerInfo().observe(this,{info ->
            if(info?.isNotEmpty() == true && identifier == IDENTIFIER_HOME){
                tickerCv?.show()
                contentView?.findViewById<Ticker>(R.id.affiliate_filter_announcement_ticker)?.setTextDescription(info)
            }
        })
        affiliateDatePickerBottomSheetViewModel.getError().observe(this,{isError ->
            isError?.let { error ->
                if(error)dismiss()
            }
        })
    }

    private fun setBundleData() {
        arguments?.let {
            identifier = it.getString(IDENTIFIER, IDENTIFIER_WITHDRAWAL)
        }
        affiliateDatePickerBottomSheetViewModel.identifier = identifier
        affiliateDatePickerBottomSheetViewModel.rangeSelected = rangeSelected
    }

    private fun initClickListener(contentView: View?) {
        contentView?.findViewById<UnifyButton>(R.id.cnf_btn)?.setOnClickListener {
            affiliateDatePickerBottomSheetViewModel.getItemList()?.forEach { visitable ->
                if((visitable as AffiliateDateRangePickerModel).dateRange.isSelected){
                    rangeChangeInterface?.rangeChanged(visitable.dateRange)
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
       affiliateDatePickerBottomSheetViewModel.getAffiliateFilterData()
    }

    override fun onDateRangeClicked(position: Int) {
        updateItem(position)
        adapter.notifyDataSetChanged()
    }

    private fun updateItem(position: Int) {
        affiliateDatePickerBottomSheetViewModel.updateItem(position)
    }
}