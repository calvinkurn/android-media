package com.tokopedia.topads.dashboard.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.credit.history.view.fragment.TopAdsCreditHistoryFragment
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsKeywordInsightsActivity
import com.tokopedia.topads.dashboard.view.fragment.*
import com.tokopedia.topads.dashboard.view.fragment.insight.*
import com.tokopedia.topads.debit.autotopup.view.fragment.TopAdsAutoTopUpFragment

import dagger.Component

/**
 * Created by hadi.putra on 23/04/18.
 */

@TopAdsDashboardScope
@Component(modules = [TopAdsDashboardModule::class, TopAdsDashboardShopModule::class,
    TopAdsDashboardNetworkModule::class, ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface TopAdsDashboardComponent {

    fun inject(topAdsProductIklanFragment: TopAdsProductIklanFragment)
    fun inject(topAdsAddCreditFragment: TopAdsAddCreditFragment)
    fun inject(topAdsCreditHistoryFragment: TopAdsCreditHistoryFragment)
    fun inject(topAdsAutoTopUpFragment: TopAdsAutoTopUpFragment)
    fun inject(berandaTabFragment: BerandaTabFragment)
    fun inject(topAdsDashGroupFragment: TopAdsDashGroupFragment)
    fun inject(topAdsDashWithoutGroupFragment: TopAdsDashWithoutGroupFragment)
    fun inject(productTabFragment: ProductTabFragment)
    fun inject(keywordTabFragment: KeywordTabFragment)
    fun inject(negKeywordTabFragment: NegKeywordTabFragment)
    fun inject(topAdsRecommendationFragment: TopAdsRecommendationFragment)
    fun inject(topadsKeywordInsightBase: TopadsInsightBaseKeywordFragment)
    fun inject(topAdsInsightMiniBidFragment: TopAdsInsightMiniBidFragment)
    fun inject(topAdsInsightMiniKeyFragment: TopAdsInsightMiniKeyFragment)
    fun inject(topAdsInsightBaseBidFragment: TopAdsInsightBaseBidFragment)
    fun inject(topAdsInsightBaseProductFragment: TopAdsInsightBaseProductFragment)
    fun inject(topAdsInsightMiniProductFragment: TopAdsInsightMiniProductFragment)
    fun inject(topAdsInsightKeyBidFragment: TopAdsInsightKeyBidFragment)
    fun inject(topAdsInsightKeyNegFragment: TopAdsInsightKeyNegFragment)
    fun inject(topAdsInsightKeyPosFragment: TopAdsInsightKeyPosFragment)
    fun inject(topAdsDashboardActivity: TopAdsDashboardActivity)
    fun inject(topAdsGroupDetailViewActivity: TopAdsGroupDetailViewActivity)
    fun inject(topAdsOpenKeywordInsightsActivity: TopAdsKeywordInsightsActivity)
}
