package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeyword
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.TopAdsInsightRecommKeywordsView
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightKeywordViewModel
import kotlinx.android.synthetic.main.topads_insight_fragment_keyword.*

class TopAdsInsightKeywordsFragment : BaseDaggerFragment() {

    private lateinit var viewModel: TopAdsInsightKeywordViewModel
    private val itemsCount = arrayOf(0, 0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        accordionUnify.onItemClick = ::accordionUnifyItemClick

        viewModel.getKeywords("480396", arrayOf())

        val it = TopAdsInsightKeywordViewModel.getInsightKeywordRecommendation().suggestion?.recommendedKeyword!!
        addAccordion(TopAdsInsightConstants.BID_KEYWORD, it)
        addAccordion(TopAdsInsightConstants.NEW_KEYWORD, it)
        addAccordion(TopAdsInsightConstants.NEGATIVE_KEYWORD, it)
    }

    private fun observeLiveData() {
        viewModel.recommendedKeyword.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it.shopID, Toast.LENGTH_SHORT).show()
        })
    }

    private fun accordionUnifyItemClick(position: Int, isExpanded: Boolean) {
        (activity as? TopAdsDashboardActivity)?.bottomLayout()?.let {
            it.visibility =
                if (isExpanded) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
        if (isExpanded) onKeywordSelected(position, itemsCount[position])
    }

    private fun addAccordion(type: Int, recommendedKeyword: RecommendedKeyword) {
        val instance = TopAdsInsightRecommKeywordsView.createInstance(
            requireContext(),
            type,
            recommendedKeyword,
            ::onKeywordSelected
        )

        accordionUnify.addGroup(
            AccordionDataUnify(
                title = getAccordionTitle(type, recommendedKeyword.recommendedKeywordCount),
                expandableView = instance,
                isExpanded = false
            )
        )
    }

    private fun onKeywordSelected(type: Int, count: Int) {
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

    private fun getAccordionTitle(type: Int, count: Int): String {
        return String.format(
            resources.getString(
                when (type) {
                    TopAdsInsightConstants.BID_KEYWORD -> R.string.topads_insight_title_bid_keyword
                    TopAdsInsightConstants.NEW_KEYWORD -> R.string.topads_insight_title_new_keyword
                    TopAdsInsightConstants.NEGATIVE_KEYWORD -> R.string.topads_insight_title_negative_keyword
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
}