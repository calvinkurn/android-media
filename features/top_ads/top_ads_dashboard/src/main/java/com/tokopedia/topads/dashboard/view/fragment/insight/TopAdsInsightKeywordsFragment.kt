package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.TopadsHeadlineKeyword
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.TopAdsInsightRecommKeywordsView
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightKeywordViewModel
import kotlinx.android.synthetic.main.topads_insight_fragment_keyword.*

class TopAdsInsightKeywordsFragment : BaseDaggerFragment() {

    private val dummyResp by lazy { Gson().fromJson(getResp(), TopadsHeadlineKeyword::class.java) }
    private lateinit var viewModel: TopAdsInsightKeywordViewModel
    private val itemsCount = arrayOf(0, 0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRecommendation(0)
        observeLiveData()
        setAccordion()
        accordionUnify.onItemClick = { position, isExpanded ->

            (activity as? TopAdsDashboardActivity)?.bottomLayout()?.let {
                it.visibility =
                    if (isExpanded) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
            if (isExpanded) onItemSelected(position, itemsCount[position])
        }
    }

    //method to be executed when ad type is changed
    fun loadRecommendation(type: Int) {
        viewModel.getKeywords("480396", arrayOf())
    }

    private fun observeLiveData() {
        viewModel.recommendedKeyword.observe(viewLifecycleOwner, {
            //Toast.makeText(requireContext(), it.shopId, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setAccordion() {
        accordionUnify.addGroup(
            newAccordion(
                R.string.topads_insight_recomm_keyword_cost,
                1,
                getAccordionSubView(TopAdsInsightConstants.BID_KEYWORD)
            )
        )

        accordionUnify.addGroup(
            newAccordion(
                R.string.topads_insight_new_recomm_keyword,
                1,
                getAccordionSubView(TopAdsInsightConstants.NEW_KEYWORD)
            )
        )

        accordionUnify.addGroup(
            newAccordion(
                R.string.topads_insight_negative_recomm_keyword,
                1,
                getAccordionSubView(TopAdsInsightConstants.NEGATIVE_KEYWORD)
            )
        )
    }

    private fun getAccordionSubView(type: Int): TopAdsInsightRecommKeywordsView {
        val instance = TopAdsInsightRecommKeywordsView.createInstance(
            requireContext(),
            type,
            TopadsHeadlineKeyword()
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
        val view = inflater.inflate(layout, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(TopAdsInsightKeywordViewModel::class.java)
        return view
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
    fun getResp() = "{\n" +
            "    \"topadsHeadlineKeywordSuggestion\": {\n" +
            "      \"data\": {\n" +
            "        \"shopID\": \"479085\",\n" +
            "        \"recommendedKeywordCount\": 1,\n" +
            "        \"groupCount\": 1,\n" +
            "        \"totalImpressionCount\": \"243\",\n" +
            "        \"recommendedKeywordDetails\": [\n" +
            "          {\n" +
            "            \"keywordTag\": \"svj\",\n" +
            "            \"groupID\": \"9254\",\n" +
            "            \"groupName\": \"testing el\",\n" +
            "            \"totalHits\": \"222\",\n" +
            "            \"recommendedBid\": 12000,\n" +
            "            \"minBid\": 12000,\n" +
            "            \"maxBid\": 500000,\n" +
            "            \"impressionCount\": \"243\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"errors\": []\n" +
            "    }\n" +
            "}"
}