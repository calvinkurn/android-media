package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetAdapter
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetAdapterFactory
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetDiffcallback
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateBottomDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficAttributionModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithrawalInfoAttributionModel
import com.tokopedia.affiliate.viewmodel.AffiliateRecyclerViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AffiliateRecylerBottomSheet: BottomSheetUnify() {
    private var contentView: View? = null
    private var type: String =""
    private var titleSheet: String = ""
    private var subText: String = ""
    private var filterType: String = ""
    private var tickerDesc: String = ""
    private var listItem :Any? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {
        private const val TYPE = "type"
        private const val TITLE = "title"
        private const val SUB_TEXT = "subText"
        private const val FILTER_TYPE = "filterType"
        private const val TICKER_INFO = "tickerInfo"
        const val TYPE_WITHDRAWAL = "withdrawalType"
        const val TYPE_HOME = "homeType"
        fun newInstance(type: String, title: String?, subText: String?, list: Any?, filterType: String = "",tickerInfo: String? = ""
        ): AffiliateRecylerBottomSheet {
            return AffiliateRecylerBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(TYPE,type)
                    putString(TITLE,title)
                    putString(SUB_TEXT,subText)
                    putString(FILTER_TYPE,filterType)
                    putString(TICKER_INFO,tickerInfo)
                }
                listItem = list
            }
        }
    }
    private var dataRv: RecyclerView? = null
    private fun init() {
        initBundle()
        initViewModel()
        showCloseIcon = true
        showKnob = false
        clearContentPadding = true
        contentView = View.inflate(context,
            R.layout.affiliate_traffic_attribution_bottom_sheet_attribute, null)
        setTitle(titleSheet)
        contentView?.findViewById<Typography>(R.id.traffic_attribution_text)?.apply {
            text = subText
        }
        dataRv = contentView?.findViewById(R.id.traffic_rv)
        initDivider()
        initList(listItem)
        setData()
        setChild(contentView)
    }

    private fun initDivider() {
        if((listItem as? List<*>)?.isNotEmpty() == true && type == TYPE_HOME ) {
            contentView?.findViewById<DividerUnify>(R.id.divider_2)?.show()
            setTicker()
        }else{
            contentView?.findViewById<DividerUnify>(R.id.divider_2)?.gone()
        }
    }

    private fun setTicker() {
        if(tickerDesc.isNotEmpty()){
            contentView?.findViewById<CardUnify2>(R.id.affiliate_metric_announcement_ticker_cv)?.isVisible = true
            contentView?.findViewById<Typography>(R.id.affiliate_metric_announcement_ticker)?.apply {
                text = tickerDesc
            }
        }
    }

    private fun initList(listItem: Any?) {
        if(type == TYPE_HOME){
            var metricList = (listItem as? List<AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics.Tooltip.SubMetrics?>)
            metricList =  metricList?.sortedBy { subMetrics -> subMetrics?.order  }
            metricList?.forEachIndexed { index, subMetrics ->
                if(index == metricList.lastIndex) subMetrics?.isLastItem = true
                viewModel?.itemList?.add(AffiliateTrafficAttributionModel(subMetrics))
            }
        }
        else if(type == TYPE_WITHDRAWAL){
            (listItem as? List<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail.Tooltip>?)?.forEach {
                if(it.tooltipType == "divider")
                    viewModel?.itemList?.add(AffiliateBottomDividerItemModel())
                else
                    viewModel?.itemList?.add(AffiliateWithrawalInfoAttributionModel(it))
            }
        }

    }

    private var viewModel:AffiliateRecyclerViewModel? = null
    private fun initViewModel() {
        viewModel = ViewModelProvider(this,viewModelProvider).get(AffiliateRecyclerViewModel::class.java)
    }

    private fun initBundle() {
        arguments?.let { bundle ->
            type = bundle.getString(TYPE,"")
            titleSheet = bundle.getString(TITLE,"")
            subText = bundle.getString(SUB_TEXT,"")
            filterType = bundle.getString(FILTER_TYPE,"")
            tickerDesc = bundle.getString(TICKER_INFO,"")
        }
    }

    private val adapter by lazy {
        val asyncDifferConfig = AsyncDifferConfig.Builder(AffiliateBottomSheetDiffcallback())
            .build()
        AffiliateBottomSheetAdapter(asyncDifferConfig, AffiliateBottomSheetAdapterFactory())
    }

    private fun setData() {
       dataRv?.layoutManager = LinearLayoutManager(context)
       dataRv?.adapter = adapter
       adapter.submitList(viewModel?.itemList as List<Visitable<*>>?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private fun initInject() {
        getComponent().injectRecyclerBottomSheet(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

}
