package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeyword
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.TopAdsInsightRecommKeywordsView
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import kotlinx.android.synthetic.main.topads_insight_fragment_keyword.*

class TopAdsInsightKeywordsFragment : BaseDaggerFragment() {

    private val recommendedKeyword by lazy {
        Gson().fromJson(
            getResp(),
            RecommendedKeyword::class.java
        )
    }
    private val itemsCount = arrayOf(0, 0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAccordion()
        accordionUnify.onItemClick = { position, isExpanded ->
            (activity as? TopAdsDashboardActivity)?.bottomLayout()?.visibility =
                if (isExpanded) View.VISIBLE else View.GONE
            if (isExpanded) onItemSelected(position, itemsCount[position])
        }
    }

    private fun setAccordion() {
        accordionUnify.addGroup(
            newAccordion(
                R.string.topads_insight_recomm_keyword_cost,
                recommendedKeyword.keywordCount,
                getAccordionSubView(TopAdsInsightConstants.BID_KEYWORD)
            )
        )

        accordionUnify.addGroup(
            newAccordion(
                R.string.topads_insight_new_recomm_keyword,
                recommendedKeyword.keywordCount,
                getAccordionSubView(TopAdsInsightConstants.NEW_KEYWORD)
            )
        )

        accordionUnify.addGroup(
            newAccordion(
                R.string.topads_insight_negative_recomm_keyword,
                recommendedKeyword.keywordCount,
                getAccordionSubView(TopAdsInsightConstants.NEGATIVE_KEYWORD)
            )
        )
    }

    private fun getAccordionSubView(type: Int): TopAdsInsightRecommKeywordsView {
        val instance = TopAdsInsightRecommKeywordsView.createInstance(
            requireContext(),
            type,
            recommendedKeyword
        )
        instance.itemSelectedListener = { t, c ->
            onItemSelected(t, c)
        }
        return instance
    }

    fun onItemSelected(type: Int, count: Int) {
        itemsCount[type] = count
        (activity as? TopAdsDashboardActivity)?.multiActionButton()?.apply {
            text = when (type) {
                TopAdsInsightConstants.BID_KEYWORD -> {
                    String.format(resources.getString(R.string.apply_fee), count)
                }
                TopAdsInsightConstants.NEW_KEYWORD -> {
                    String.format(resources.getString(R.string.add_keyword), count)
                }
                TopAdsInsightConstants.NEGATIVE_KEYWORD -> {
                    String.format(resources.getString(R.string.add_neg_keywords), count)
                }
                else -> ""
            }
            isEnabled = count > 0
        }
    }

    private fun newAccordion(stringId: Int, value: Int, view: View): AccordionDataUnify {
        return AccordionDataUnify(
            String.format(resources.getString(stringId), value),
            "",
            null,
            null,
            view,
            false
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
        return TopAdsInsightKeywordsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    companion object {
        private val layout = R.layout.topads_insight_fragment_keyword
        fun createInstance(): TopAdsInsightKeywordsFragment {
            return TopAdsInsightKeywordsFragment()
        }
    }

    //todo view model
    fun getResp() = "{   \n" +
            "  shop_id: 123,   \n" +
            "  recommended_keyword_count: 3,    \n" +
            "  group_count: 2,    \n" +
            "  total_impression_count: 369,   \n" +
            "  recommended_keyword_details: [\n" +
            "    {         \n" +
            "      keyword_tag: \"sam ga\",         \n" +
            "      group_id: 122,         \n" +
            "      group_name: \"sdsfs\",         \n" +
            "      total_hits: 432423,         \n" +
            "      recommended_bid: 2342.32,         \n" +
            "      min_bid: 22224324,         \n" +
            "      max_bid: 34242423,\n" +
            "      impression_count: 122  \n" +
            "    },\n" +
            "    {         \n" +
            "      keyword_tag: \"go to\",         \n" +
            "      group_id: 123,         \n" +
            "      group_name: \"temp group\",         \n" +
            "      total_hits: 4322,         \n" +
            "      recommended_bid: 2342.32,         \n" +
            "      min_bid: 22224324,         \n" +
            "      max_bid: 34242423,\n" +
            "      impression_count: 123  \n" +
            "    },\n" +
            "    {         \n" +
            "      keyword_tag: \"go me\",         \n" +
            "      group_id: 123,         \n" +
            "      group_name: \"temp group\",         \n" +
            "      total_hits: 4324,         \n" +
            "      recommended_bid: 2342.32,         \n" +
            "      min_bid: 22224324,         \n" +
            "      max_bid: 34242423,\n" +
            "      impression_count: 124  \n" +
            "    }\n" +
            "  ]\n" +
            "}"
}