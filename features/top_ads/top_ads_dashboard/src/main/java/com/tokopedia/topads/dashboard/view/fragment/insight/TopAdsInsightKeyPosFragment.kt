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
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INDEX_4
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INSIGHT_DATA_HEADER
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEY_INSIGHT
import com.tokopedia.topads.dashboard.data.model.insightkey.Header
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.topads.dashboard.data.utils.Utils.convertToCurrencyString
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightPosKeyAdapter
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyBidFragment.Companion.COUNT
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyBidFragment.Companion.VALUE
import com.tokopedia.topads.dashboard.view.sheet.InsightKeyBottomSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 21/7/20.
 */
class TopAdsInsightKeyPosFragment : BaseDaggerFragment() {

    private var insightTitle: Typography? = null
    private var toolTip: ImageUnify? = null
    private var insightDesc: Typography? = null
    private var btnTambah: UnifyButton? = null
    private var insightListPosKey: RecyclerView? = null

    private lateinit var adapter: TopAdsInsightPosKeyAdapter
    private var itemCountCallBack: SetCount? = null
    private var key: String? = null
    private var data: Header? = null
    private var dataInsight: HashMap<String, KeywordInsightDataMain>? = null

    override fun getScreenName(): String {
        return TopAdsInsightKeyPosFragment::class.java.name
    }

    companion object {
        fun createInstance(bundle: Bundle): TopAdsInsightKeyPosFragment {
            val fragment = TopAdsInsightKeyPosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TopAdsInsightPosKeyAdapter(::butttonClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        toolTip?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dash_info_tooltip))
        toolTip?.setOnClickListener {
            val sheet = InsightKeyBottomSheet.createInstance(0)
            sheet.show(requireFragmentManager(), "")
        }
        insightListPosKey?.adapter = adapter
        insightListPosKey?.layoutManager = LinearLayoutManager(context)
    }

    private fun butttonClicked(position: Int) {
        itemCountCallBack?.onButtonClicked(listOf(adapter.items[position].mutationData), key
            ?: "", 1, false)
    }

    private fun setView() {
        getfromArguments()
        var totalPotential = 0.0
        val mutationList: MutableList<MutationData> = mutableListOf()
        dataInsight?.get(key)?.keyword?.forEach {
            adapter.items.add(it)
            mutationList.add(it.mutationData)
            totalPotential += (it.data?.get(INDEX_4)?.value) as Double
        }
        setTabCount()
        setHeader(totalPotential)
        btnTambah?.setOnClickListener {
            itemCountCallBack?.onButtonClicked(mutationList, key
                ?: "", dataInsight?.get(key)?.keyword?.size ?: 0, true)
        }
        adapter.notifyDataSetChanged()
    }

    private fun setTabCount() {
        itemCountCallBack?.setCount(dataInsight?.get(key)?.keyword?.size
            ?: 0, dataInsight?.get(key)?.negative?.size ?: 0,
            dataInsight?.get(key)?.bid?.size ?: 0)
    }

    private fun getfromArguments() {
        key = arguments?.getString(KEY_INSIGHT)
        data = arguments?.getParcelable(INSIGHT_DATA_HEADER)
        dataInsight =
            arguments?.getSerializable(DATA_INSIGHT) as HashMap<String, KeywordInsightDataMain>
    }

    private fun setHeader(totalPotential: Double) {
        insightTitle?.text = data?.keyword?.box?.title
        val text = data?.keyword?.box?.desc
        val withValue = text?.replace(COUNT, dataInsight?.get(key)?.keyword?.size.toString())
            ?.replace(VALUE, "+" + convertToCurrencyString(totalPotential.toLong()))
        insightDesc?.text = Html.fromHtml(withValue)
        btnTambah?.text = data?.keyword?.box?.button?.title?.replace(COUNT,
            dataInsight?.get(key)?.keyword?.size.toString())
        if (dataInsight?.get(key)?.keyword?.size == 0)
            btnTambah?.isEnabled = false
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SetCount) {
            itemCountCallBack = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        itemCountCallBack = null
    }

    interface SetCount {
        fun setCount(sizePos: Int, sizeNeg: Int, sizeBid: Int)
        fun onButtonClicked(
            mutationData: List<MutationData>,
            groupId: String,
            countToAdd: Int,
            forAllButton: Boolean,
        )
    }
}