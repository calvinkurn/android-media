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
import com.tokopedia.topads.dashboard.view.TopAdsInsightShopKeywordRecommendationView
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightViewModel
import kotlinx.android.synthetic.main.fragment_topads_insight_shop_keyword.*
import javax.inject.Inject
import kotlin.IllegalStateException

class TopAdsInsightShopKeywordRecommendationFragment : BaseDaggerFragment() {

    private lateinit var newKeywordRecommView : TopAdsInsightShopKeywordRecommendationView
    private val itemsCountMap = mutableMapOf<Int, Int>()
    private val typeToPosiMap = mutableMapOf<Int, Int>()

    @Inject
    lateinit var viewModel: TopAdsInsightViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        getDataFromArgument()
        accordionUnify.onItemClick = ::accordionUnifyItemClick
    }

    private fun observeViewModel() {
        viewModel.applyKeyword.observe(viewLifecycleOwner, {
        })
    }

    private fun getDataFromArgument() {
        val data = arguments?.getParcelable<RecommendedKeywordData>(BUNDLE_NEW_KEYWORD)
        if (data?.recommendedKeywordDetails != null && data.recommendedKeywordCount != 0)
            addNewKeywordAccordion(data)
    }

    private fun accordionUnifyItemClick(position: Int, isExpanded: Boolean) {
        (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(isExpanded)
        val type = getTypeOfPosi(position)
        if (isExpanded) onKeywordSelected(type, itemsCountMap[type] ?: 0)
    }

    private fun addNewKeywordAccordion(recommendedKeywordData: RecommendedKeywordData) {
        newKeywordRecommView = TopAdsInsightShopKeywordRecommendationView.createInstance(
            requireContext(),
            NEW_KEYWORD,
            recommendedKeywordData,
            ::onKeywordSelected
        )

        accordionUnify.addGroup(
            AccordionDataUnify(
                title = getAccordionTitle(NEW_KEYWORD, recommendedKeywordData.recommendedKeywordCount),
                expandableView = newKeywordRecommView,
                isExpanded = false
            )
        )
        itemsCountMap[NEW_KEYWORD] = recommendedKeywordData.recommendedKeywordCount
        typeToPosiMap[accordionUnify.accordionData.size - 1] = NEW_KEYWORD
    }

    private fun onKeywordSelected(type: Int, count: Int) {
        itemsCountMap[type] = count
        (activity as? TopAdsDashboardActivity)?.updateMultiActionButton(type, count)
    }

    private fun getTypeOfPosi(position: Int): Int {
        return typeToPosiMap[position] ?: throw IllegalStateException("not a position")
    }

    private fun getAccordionTitle(type: Int, count: Int): String {
        return String.format(resources.getString(
            when (type) {
                BID_KEYWORD -> R.string.topads_insight_title_bid_keyword
                NEW_KEYWORD -> R.string.topads_insight_title_new_keyword
                NEGATIVE_KEYWORD -> R.string.topads_insight_title_negative_keyword
                else -> throw Exception("Wrong type")
            }
        ), count)
    }

    fun applyKeywords() {
        viewModel.applyRecommendedKeywords()
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
        return TopAdsInsightShopKeywordRecommendationFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    companion object {
        private val layout = R.layout.fragment_topads_insight_shop_keyword
        private const val BUNDLE_NEW_KEYWORD = "new_keyword"

        fun createInstance(data: RecommendedKeywordData?): TopAdsInsightShopKeywordRecommendationFragment {
            val instance = TopAdsInsightShopKeywordRecommendationFragment()
            val bundle = Bundle().also {
                it.putParcelable(BUNDLE_NEW_KEYWORD, data)
            }
            instance.arguments = bundle
            return instance
        }
    }
}