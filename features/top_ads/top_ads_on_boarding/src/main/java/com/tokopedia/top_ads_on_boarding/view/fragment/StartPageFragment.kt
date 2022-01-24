package com.tokopedia.top_ads_on_boarding.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.EMPTY_TEXT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TAG_STEPPER_FRAGMENT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TopAdsOnBoardingLink.LEARN_TOKOPEDIA_URL
import com.tokopedia.top_ads_on_boarding.view.activity.TopAdsOnBoardingActivity
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class StartPageFragment : TkpdBaseV4Fragment() {
    private var firstAdButton: UnifyButton? = null
    private var learnTopsAdsButton: UnifyButton? = null
    private var startPageDescription: Typography? = null
    override fun getScreenName(): String {
        return EMPTY_TEXT
    }

    companion object {
        fun newInstance(): StartPageFragment {
            return StartPageFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.topads_start_page_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setDescriptionText()
        firstAdButton?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.onBoardingFragmentContainer,
                AdsObjectiveFragment.newInstance(),
                TAG_STEPPER_FRAGMENT
            )?.addToBackStack("2")?.commit()
        }
        learnTopsAdsButton?.setOnClickListener {
            (activity as? TopAdsOnBoardingActivity)?.goToWebView(LEARN_TOKOPEDIA_URL)
        }

    }

    private fun setDescriptionText() {
        startPageDescription?.text =
            MethodChecker.fromHtml(context?.getString(R.string.topads_start_page_description))
    }

    private fun initViews(view: View) {
        firstAdButton = view.findViewById(R.id.firstAdsButton)
        learnTopsAdsButton = view.findViewById(R.id.LearnTopAdsButton)
        startPageDescription = view.findViewById(R.id.startPageDescription)
    }
}
