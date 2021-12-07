package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.Constants.ACTION_EDIT
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput2
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.BID_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEW_KEYWORD
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.TopAdsInsightShopKeywordRecommendationView
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_topads_insight_shop_keyword.*
import javax.inject.Inject

class TopAdsInsightShopKeywordRecommendationFragment : BaseDaggerFragment() {

    private var expandedPosi = 0
    @Inject
    lateinit var viewModel: TopAdsInsightViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeViewModel()
        getDataFromArgument()
        accordionUnify.onItemClick = ::accordionUnifyItemClick
    }

    private fun observeViewModel() {
        viewModel.applyKeyword.observe(viewLifecycleOwner, {
            showToast()
            (parentFragment as TopAdsRecommendationFragment).loadShopData()
            (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(false)
        })
    }

    private fun getDataFromArgument() {
        val data = arguments?.getParcelable<RecommendedKeywordData>(BUNDLE_NEW_KEYWORD)
        if (data == null || data.recommendedKeywordCount == 0) {
            empty_view.show()
        } else {
            addAccordion(data)
        }
    }

    private fun accordionUnifyItemClick(position: Int, isExpanded: Boolean) {
        expandedPosi = position
        (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(isExpanded)
        if (isExpanded) onKeywordSelected(
            getExpandedView(position).type,
            getExpandedView(position).selectedItemsCount()
        )
    }

    private fun addAccordion(recommendedKeywordData: RecommendedKeywordData) {
        val instance = TopAdsInsightShopKeywordRecommendationView.createInstance(
            requireContext(),
            NEW_KEYWORD,
            recommendedKeywordData,
            ::onKeywordSelected
        )

        accordionUnify.addGroup(
            AccordionDataUnify(
                title = getAccordionTitle(
                    NEW_KEYWORD,
                    recommendedKeywordData.recommendedKeywordCount
                ),
                expandableView = instance,
                isExpanded = false
            )
        )
    }

    private fun onKeywordSelected(type: Int, count: Int) {
        (activity as? TopAdsDashboardActivity)?.updateMultiActionButton(type, count)
    }

    fun applyKeywords() {
        val groupMap = getExpandedView(expandedPosi).getKeywords() ?: return
        for ((key, value) in groupMap) {
            val input = TopAdsManageHeadlineInput2(
                source = "android.insight_center_headline_keyword_recom",
                operation = TopAdsManageHeadlineInput2.Operation(
                    action = ACTION_EDIT,
                    group = TopAdsManageHeadlineInput2.Operation.Group(
                        id = key.first.toString(),
                        name = key.second,
                        shopID = userSession.shopId,
                        keywordOperations = value,
                        status = "published"
                    )
                )
            )
            viewModel.applyRecommendedKeywords(input)
        }
    }

    private fun getExpandedView(posi: Int) =
        accordionUnify.accordionData[posi].expandableView as TopAdsInsightShopKeywordRecommendationView

    private fun initView() {
        empty_view.findViewById<Typography>(R.id.text_title).text =
            resources.getString(R.string.empty_view_title)
        empty_view.findViewById<Typography>(R.id.text_desc).text =
            resources.getString(R.string.empty_view_desc)
        empty_view.findViewById<ImageUnify>(R.id.image_empty)
            .setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
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

    private fun showToast() {
        Toaster.build(
            requireView(),
            String.format(
                resources.getString(R.string.toast_message),
                getExpandedView(expandedPosi).selectedItemsCount()
            ),
            actionText = resources.getString(R.string.topads_headline_oke_button)
        ).show()
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