package com.tokopedia.top_ads_on_boarding.view.activity

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ONBOARDING_ARTICLE_LINK
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ONBOARDING_IMG_URL
import com.tokopedia.top_ads_on_boarding.data.mapper.OnboardingMapper
import com.tokopedia.top_ads_on_boarding.databinding.TopadsAutoPsOnboardingActivityLayoutBinding
import com.tokopedia.top_ads_on_boarding.di.DaggerTopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.di.TopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.view.adapter.OnboardingFaqListAdapter
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.ONBOARDING_PARAM
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.PARAM_FEATURE
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.top_ads_on_boarding.R

class AutoPsOnboardingActivity : BaseActivity(), HasComponent<TopAdsOnBoardingComponent> {

    private var binding: TopadsAutoPsOnboardingActivityLayoutBinding? = null
    private var mapper: OnboardingMapper? = null
    private val faqListAdapter by lazy {
        OnboardingFaqListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TopadsAutoPsOnboardingActivityLayoutBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
        mapper = OnboardingMapper()
        init()
    }

    private fun init() {
        val faqList = mapper?.getFaqList()
        binding?.rvFaq?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.rvFaq?.adapter = faqListAdapter
        faqListAdapter.submitList(faqList)

        binding?.image?.urlSrc = ONBOARDING_IMG_URL

        binding?.submit?.setOnClickListener{
            val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE).apply {
                putExtra(PARAM_FEATURE,ONBOARDING_PARAM)
            }
            startActivity(intent)
        }

        binding?.header?.title = this.title
        binding?.header?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.articleLink?.movementMethod = LinkMovementMethod.getInstance()
        binding?.articleLink?.text = getClickableString()
    }

    private fun getClickableString() : SpannableString {
        val text = getString(R.string.topads_autops_onboarding_article_link)
        val ss = SpannableString(text)
        val cs = object : ClickableSpan(){
            override fun onClick(p0: View) {
                RouteManager.route(
                    this@AutoPsOnboardingActivity,
                    ApplinkConstInternalGlobal.WEBVIEW,
                    ONBOARDING_ARTICLE_LINK
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(
                    this@AutoPsOnboardingActivity,
                    unifyprinciplesR.color.Unify_GN500
                )
                ds.isFakeBoldText = true
            }
        }

        ss.setSpan(cs, text.length-12, text.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getComponent(): TopAdsOnBoardingComponent =
        DaggerTopAdsOnBoardingComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
}
