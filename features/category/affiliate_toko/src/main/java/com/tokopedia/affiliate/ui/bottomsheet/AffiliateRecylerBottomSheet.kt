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
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetAdapterFactory
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetAdapter
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetDiffcallback
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficAttributionModel
import com.tokopedia.affiliate.viewmodel.AffiliateRecyclerViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AffiliateRecylerBottomSheet: BottomSheetUnify() {
    private var contentView: View? = null
    private var type: String =""
    private var titleSheet: String = ""
    private var subText: String = ""
    private var metricsList :List<AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics.Tooltip.SubMetrics?>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {
        private const val TYPE = "type"
        private const val TITLE = "title"
        private const val SUB_TEXT = "subText"
        fun newInstance(type: String, title: String?, subText: String?, metrics: List<AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics.Tooltip.SubMetrics?>?
        ): AffiliateRecylerBottomSheet {
            return AffiliateRecylerBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(TYPE,type)
                    putString(TITLE,title)
                    putString(SUB_TEXT,subText)
                }
                metricsList = metrics
            }
        }
    }
    private var dataRv: RecyclerView? = null
    private fun init() {
        initBundle()
        initViewModel()
        showCloseIcon = true
        showKnob = false
        contentView = View.inflate(context,
            R.layout.affiliate_traffic_attribution_bottom_sheet_attribute, null)
        setTitle(titleSheet)
        contentView?.findViewById<Typography>(R.id.traffic_attribution_text)?.apply {
            text = subText
        }
        dataRv = contentView?.findViewById(R.id.traffic_rv)
        initMetricData(metricsList)
        setData()
        setChild(contentView)
    }

    private fun initMetricData(metricsList: List<AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics.Tooltip.SubMetrics?>?) {
        metricsList?.forEach {
            viewModel?.itemList?.add(AffiliateTrafficAttributionModel(it))
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