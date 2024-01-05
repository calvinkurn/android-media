package com.tokopedia.top_ads_on_boarding.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.top_ads_on_boarding.data.mapper.OnboardingMapper
import com.tokopedia.top_ads_on_boarding.databinding.TopadsAutoPsOnboardingActivityLayoutBinding
import com.tokopedia.top_ads_on_boarding.di.DaggerTopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.di.TopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.view.adapter.OnboardingFaqListAdapter

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

        binding?.submit?.setOnClickListener{
            startActivity(RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE))
        }
    }

    override fun getComponent(): TopAdsOnBoardingComponent =
        DaggerTopAdsOnBoardingComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
}
