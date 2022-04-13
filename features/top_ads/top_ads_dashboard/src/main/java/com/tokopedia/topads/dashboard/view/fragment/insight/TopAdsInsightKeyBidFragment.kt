package com.tokopedia.topads.dashboard.view.fragment.insight

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INSIGHT_DATA_HEADER
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEY_INSIGHT
import com.tokopedia.topads.dashboard.data.model.insightkey.Header
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightBidKeyAdapter
import com.tokopedia.topads.dashboard.view.sheet.InsightKeyBottomSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 21/7/20.
 */
class TopAdsInsightKeyBidFragment : BaseDaggerFragment() {

    private var insightTitle: Typography? = null
    private var toolTip: ImageUnify? = null
    private var insightDesc: Typography? = null
    private var btnTambah: UnifyButton? = null
    private var insightListPosKey: RecyclerView? = null

    private lateinit var adapter: TopAdsInsightBidKeyAdapter
    private var itemCountCallBack: OnKeywordBidAdded? = null
    private var key: String? = null
    private var data: Header? = null
    private var dataInsight: HashMap<String, KeywordInsightDataMain>? = null

    override fun getScreenName(): String {
        return TopAdsInsightKeyBidFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    companion object {
        const val COUNT = "$" + "count"
        const val VALUE = "$" + "value"
        fun createInstance(bundle: Bundle): TopAdsInsightKeyBidFragment {
            val fragment = TopAdsInsightKeyBidFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.topads_dash_fragment_pos_key_insight, container, false)
        insightTitle = view.findViewById(R.id.insight_title)
        toolTip = view.findViewById(R.id.toolTip)
        insightDesc = view.findViewById(R.id.insight_desc)
        btnTambah = view.findViewById(R.id.btnTambah)
        insightListPosKey = view.findViewById(R.id.insightListPosKey)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TopAdsInsightBidKeyAdapter(this::butttonClicked)
        setView()
        toolTip?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dash_info_tooltip))
        toolTip?.setOnClickListener {
            val sheet = InsightKeyBottomSheet.createInstance(2)
            sheet.show(fragmentManager!!, "")
        }
        insightListPosKey?.adapter = adapter
        insightListPosKey?.layoutManager = LinearLayoutManager(context)
    }

    private fun butttonClicked(data: MutationData) {
        itemCountCallBack?.onButtonClickedBid(listOf(data), key ?: "", 1, false)
    }

    private fun setView() {
        getfromArguments()
        var totalPotential = 0.0
        val mutationList: MutableList<MutationData> = mutableListOf()
        dataInsight?.get(key)?.bid?.forEach {
            adapter.items.add(it)
            mutationList.add(it.mutationData)
            totalPotential += (it.data?.get(TopAdsDashboardConstant.INDEX_4)?.value) as Double
        }
        setHeader(totalPotential)
        btnTambah?.setOnClickListener {
            itemCountCallBack?.onButtonClickedBid(mutationList, key
                ?: "", dataInsight?.get(key)?.bid?.size ?: 0, true)
        }
        adapter.notifyDataSetChanged()
    }

    private fun setHeader(totalPotential: Double) {
        insightTitle?.text = data?.bid?.box?.title
        val text = data?.keyword?.box?.desc
        val withValue = text?.replace(COUNT, dataInsight?.get(key)?.bid?.size.toString())
            ?.replace(VALUE, "+" + Utils.convertToCurrencyString(totalPotential.toLong()))
        insightDesc?.text = Html.fromHtml(withValue)
        btnTambah?.text = data?.bid?.box?.button?.title?.replace(COUNT,
            dataInsight?.get(key)?.bid?.size.toString())
        if (dataInsight?.get(key)?.bid?.size == 0)
            btnTambah?.isEnabled = false
    }

    private fun getfromArguments() {
        key = arguments?.getString(KEY_INSIGHT)
        data = arguments?.getParcelable(INSIGHT_DATA_HEADER)
        dataInsight =
            arguments?.getSerializable(DATA_INSIGHT) as HashMap<String, KeywordInsightDataMain>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnKeywordBidAdded) {
            itemCountCallBack = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        itemCountCallBack = null
    }

    interface OnKeywordBidAdded {
        fun onButtonClickedBid(
            data: List<MutationData>,
            groupId: String,
            countToAdd: Int,
            forAllButton: Boolean,
        )
    }

}