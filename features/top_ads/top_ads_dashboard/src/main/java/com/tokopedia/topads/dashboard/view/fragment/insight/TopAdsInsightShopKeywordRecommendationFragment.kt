package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.domain.model.createheadline.TopAdsManageHeadlineInput2
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.BID_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEW_KEYWORD
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.TopAdsInsightShopKeywordRecommendationView
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.viewmodel.TopAdsInsightViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsInsightShopKeywordRecommendationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TopAdsInsightViewModel::class.java]
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var emptyView: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        getDataFromArgument()
        view.findViewById<AccordionUnify>(R.id.accordionUnify)?.onItemClick =
            ::accordionUnifyItemClick
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
            view?.findViewById<ConstraintLayout>(R.id.empty_view)?.show()
        } else {
            addAccordion(data)
        }
    }

    private fun accordionUnifyItemClick(position: Int, isExpanded: Boolean) {
        expandedPosi = if (isExpanded) position else NOT_EXPANDED
        (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(isExpanded)
        if (isExpanded) getExpandedView(position)?.let {
            onKeywordSelected(
                it.type,
                it.selectedItemsCount()
            )
        }
    }

    private fun addAccordion(recommendedKeywordData: RecommendedKeywordData) {
        val instance = TopAdsInsightShopKeywordRecommendationView.createInstance(
            requireContext(),
            NEW_KEYWORD,
            recommendedKeywordData,
            ::onKeywordSelected
        )

        view?.findViewById<AccordionUnify>(R.id.accordionUnify)?.addGroup(
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
        val groupMap = getExpandedView(expandedPosi)?.getKeywords() ?: return
        for ((key, value) in groupMap) {
            val input = TopAdsManageHeadlineInput2(
                source = "android.insight_center_headline_keyword_recom",
                operation = TopAdsManageHeadlineInput2.Operation(
                    action = "edit",
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
        view?.findViewById<AccordionUnify>(R.id.accordionUnify)?.accordionData?.getOrNull(posi)?.expandableView as? TopAdsInsightShopKeywordRecommendationView

    private fun initView(view: View) {
        emptyView = view.findViewById(R.id.empty_view)
        try {
            view.findViewById<Typography>(R.id.text_title).text =
                resources.getString(R.string.empty_view_title)
            view.findViewById<Typography>(R.id.text_desc).text =
                resources.getString(R.string.topads_dash_empty_daily_budget_desc)
            view.findViewById<ImageUnify>(R.id.image_empty)
                .setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        } catch (e: Exception) {
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

    private fun showToast() {
        Toaster.build(
            requireView(),
            String.format(
                resources.getString(R.string.apply_keyword_toast_message),
                getExpandedView(expandedPosi)?.selectedItemsCount()
            ),
            actionText = resources.getString(R.string.topads_insight_oke_button)
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initInjector()
        val view = inflater.inflate(layout, container, false)
        initView(view)
        return view
    }

    override fun getScreenName(): String {
        return TopAdsInsightShopKeywordRecommendationFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        expandedPosi = NOT_EXPANDED
    }

    companion object {
        const val NOT_EXPANDED = -1
        var expandedPosi = NOT_EXPANDED
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
