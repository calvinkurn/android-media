package com.tokopedia.top_ads_on_boarding.view.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant
import com.tokopedia.top_ads_on_boarding.data.mapper.OnboardingMapper
import com.tokopedia.top_ads_on_boarding.databinding.TopadsAutoPsOnboardingFragmentLayoutBinding
import com.tokopedia.top_ads_on_boarding.di.TopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.view.adapter.OnboardingFaqListAdapter
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SOURCE_AUTOPS_ONBOARDING
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SOURCE_PACKAGE
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class AutoPsOnboardingFragment : BaseDaggerFragment() {

    private var binding: TopadsAutoPsOnboardingFragmentLayoutBinding? = null
    private var mapper: OnboardingMapper? = null
    private val faqListAdapter by lazy {
        OnboardingFaqListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopadsAutoPsOnboardingFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapper = OnboardingMapper()
        init()
    }

    private fun init() {
        val faqList = mapper?.getFaqList()
        binding?.rvFaq?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.rvFaq?.adapter = faqListAdapter
        faqListAdapter.submitList(faqList)

        binding?.image?.urlSrc = TopAdsOnBoardingConstant.ONBOARDING_IMG_URL

        binding?.submit?.setOnClickListener {
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
                    .apply {
                        putExtra(
                            TopAdsCommonConstant.PARAM_FEATURE,
                            TopAdsCommonConstant.ONBOARDING_PARAM
                        )
                    }
            intent.putExtra(SOURCE_PACKAGE,SOURCE_AUTOPS_ONBOARDING)
            startActivity(intent)
        }

        binding?.articleLink?.movementMethod = LinkMovementMethod.getInstance()
        binding?.articleLink?.text = getClickableString()
    }

    private fun getClickableString(): SpannableString {
        val text = getString(R.string.topads_autops_onboarding_article_link)
        val ss = SpannableString(text)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                RouteManager.route(
                    context,
                    ApplinkConstInternalGlobal.WEBVIEW,
                    TopAdsOnBoardingConstant.ONBOARDING_ARTICLE_LINK
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                context?.let {
                    ds.color = ContextCompat.getColor(
                        it,
                        unifyprinciplesR.color.Unify_GN500
                    )
                }
                ds.isFakeBoldText = true
            }
        }

        ss.setSpan(cs, text.length - 12, text.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    companion object {
        fun newInstance(): AutoPsOnboardingFragment {
            return AutoPsOnboardingFragment()
        }
    }

    override fun getScreenName(): String {
        return AutoPsOnboardingFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsOnBoardingComponent::class.java).inject(this)
    }
}
