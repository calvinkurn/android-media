package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.BID_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEW_KEYWORD
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.TopAdsInsightShopKeywordRecommView
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import kotlinx.android.synthetic.main.fragment_topads_insight_shop_keyword.*
import kotlin.IllegalStateException

class TopAdsInsightShopKeywordFragment : BaseDaggerFragment() {

    private val itemsCountMap = mutableMapOf<Int, Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromArgument()
        accordionUnify.onItemClick = ::accordionUnifyItemClick
    }

    private fun getDataFromArgument() {
        val data = arguments?.getParcelable<RecommendedKeywordData>(BUNDLE_NEW_KEYWORD)
        if (data?.recommendedKeywordDetails != null)
            addAccordion(NEW_KEYWORD, data)
    }

    private fun accordionUnifyItemClick(position: Int, isExpanded: Boolean) {
        (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(isExpanded)
        val type = getTypeOfPosi(position)
        if (isExpanded) onKeywordSelected(type, itemsCountMap[type] ?: 0)
    }

    private fun addAccordion(type: Int, recommendedKeywordData: RecommendedKeywordData) {
        val instance = TopAdsInsightShopKeywordRecommView.createInstance(
            requireContext(),
            type,
            recommendedKeywordData,
            ::onKeywordSelected
        )

        accordionUnify.addGroup(
            AccordionDataUnify(
                title = getAccordionTitle(type, recommendedKeywordData.recommendedKeywordCount),
                expandableView = instance,
                isExpanded = false
            )
        )
        itemsCountMap[type] = instance.selectedItemCount
    }

    private fun onKeywordSelected(type: Int, count: Int) {
        itemsCountMap[type] = count
        (activity as? TopAdsDashboardActivity)?.updateMultiActionButton(type, count)
    }

    private fun getTypeOfPosi(position: Int): Int {
        return when(position) {
            0 -> NEW_KEYWORD
            1 -> NEGATIVE_KEYWORD
            2 -> BID_KEYWORD
            else -> throw IllegalStateException("not a posi")
        }
    }

    private fun getAccordionTitle(type: Int, count: Int): String {
        return String.format(
            resources.getString(
                when (type) {
                    BID_KEYWORD -> R.string.topads_insight_title_bid_keyword
                    NEW_KEYWORD -> R.string.topads_insight_title_new_keyword
                    NEGATIVE_KEYWORD -> R.string.topads_insight_title_negative_keyword
                    else -> throw Exception("Wrong type")
                }
            ), count
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initInjector()
        return inflater.inflate(layout, container, false)
    }

    override fun getScreenName(): String {
        return TopAdsInsightShopKeywordFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    companion object {
        private val layout = R.layout.fragment_topads_insight_shop_keyword
        private const val BUNDLE_NEW_KEYWORD = "new_keyword"

        fun createInstance(data: RecommendedKeywordData?): TopAdsInsightShopKeywordFragment {
            val instance = TopAdsInsightShopKeywordFragment()
            val bundle = Bundle().also {
                it.putParcelable(BUNDLE_NEW_KEYWORD, data)
            }
            instance.arguments = bundle
            return instance
        }
    }
}