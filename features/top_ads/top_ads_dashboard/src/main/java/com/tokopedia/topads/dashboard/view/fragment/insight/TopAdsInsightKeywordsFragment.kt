package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.TopAdsInsightRecommKeywordsView
import kotlinx.android.synthetic.main.topads_insight_fragment_keyword.*

class TopAdsInsightKeywordsFragment : BaseDaggerFragment() {

    private val keywordCostLayout by lazy { getAccordionSubView(TopAdsInsightConstants.RECOMM_KEYWORD) }
    private val newKeywordLayout by lazy { getAccordionSubView(TopAdsInsightConstants.NEW_KEYWORD) }
    private val negativeKeywordLayout by lazy { getAccordionSubView(TopAdsInsightConstants.NEGATIVE_KEYWORD) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAccordion()
    }

    private fun setAccordion() {
        accordionUnify.addGroup(
            newAccordion(R.string.topads_insight_recomm_keyword_cost, 2, keywordCostLayout)
        )

        accordionUnify.addGroup(
            newAccordion(R.string.topads_insight_new_recomm_keyword, 1, newKeywordLayout)
        )

        accordionUnify.addGroup(
            newAccordion(R.string.topads_insight_negative_recomm_keyword, 4, negativeKeywordLayout)
        )
    }

    private fun getAccordionSubView(type: Int): TopAdsInsightRecommKeywordsView {
        val instance = TopAdsInsightRecommKeywordsView.createInstance(requireContext(), type)
        return instance
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
        return inflater.inflate(R.layout.topads_insight_fragment_keyword, container, false)
    }

    override fun getScreenName(): String {
        return TopAdsInsightKeywordsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    companion object {
        fun createInstance(): TopAdsInsightKeywordsFragment {
            return TopAdsInsightKeywordsFragment()
        }
    }
    //todo view model
}