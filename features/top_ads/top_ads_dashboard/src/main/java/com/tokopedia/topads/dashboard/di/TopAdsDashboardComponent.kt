package com.tokopedia.topads.dashboard.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.credit.history.view.fragment.TopAdsCreditHistoryFragment
import com.tokopedia.topads.dashboard.recommendation.views.fragments.GroupDetailFragment
import com.tokopedia.topads.dashboard.recommendation.views.fragments.PotentialProductFragment
import com.tokopedia.topads.dashboard.recommendation.views.fragments.RecommendationFragment
import com.tokopedia.topads.dashboard.recommendation.views.fragments.SaranTabsFragment
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsKeywordInsightsActivity
import com.tokopedia.topads.dashboard.view.fragment.*
import com.tokopedia.topads.dashboard.view.fragment.insight.*
import com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet.TopAdsRecomGroupBottomSheet
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.fragment.TopAdsEditAutoTopUpFragment
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseCreditBottomSheet
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseNominalBottomSheet
import com.tokopedia.topads.headline.view.activity.TopAdsHeadlineAdDetailViewActivity
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineBaseFragment
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineKeyFragment
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineShopFragment
import dagger.Component

/**
 * Created by hadi.putra on 23/04/18.
 */

@TopAdsDashboardScope
@Component(modules = [TopAdsDashboardModule::class, TopAdsDashboardShopModule::class,
    TopAdsDashboardNetworkModule::class, ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface TopAdsDashboardComponent {

    fun inject(topAdsInsightShopKeywordRecommendationFragment: TopAdsInsightShopKeywordRecommendationFragment)
    fun inject(topAdsProductIklanFragment: TopAdsProductIklanFragment)
    fun inject(topAdsCreditHistoryFragment: TopAdsCreditHistoryFragment)
    fun inject(topAdsEditAutoTopUpFragment: TopAdsEditAutoTopUpFragment)
    fun inject(fragment: TopAdsDashboardBerandaFragment)
    fun inject(topAdsDashGroupFragment: TopAdsDashGroupFragment)
    fun inject(topAdsDashWithoutGroupFragment: TopAdsDashWithoutGroupFragment)
    fun inject(productTabFragment: ProductTabFragment)
    fun inject(keywordTabFragment: KeywordTabFragment)
    fun inject(negKeywordTabFragment: NegKeywordTabFragment)
    fun inject(topAdsHeadlineKeyFragment: TopAdsHeadlineKeyFragment)
    fun inject(topAdsHeadlineBaseFragment: TopAdsHeadlineBaseFragment)
    fun inject(topAdsHeadlineShopFragment: TopAdsHeadlineShopFragment)
    fun inject(topAdsBaseTabFragment: TopAdsBaseTabFragment)
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
    fun inject(topAdsHeadlineGroupDetailViewActivity: TopAdsHeadlineAdDetailViewActivity)
    fun inject(topAdsAddCreditActivity: TopAdsAddCreditActivity)
    fun inject(topAdsChooseNominalBottomSheet: TopAdsChooseNominalBottomSheet)
    fun inject(topAdsChooseCreditBottomSheet: TopAdsChooseCreditBottomSheet)
    fun inject(topAdsRecomGroupBottomSheet: TopAdsRecomGroupBottomSheet)
    fun inject(topAdsDashDeletedGroupFragment: TopAdsDashDeletedGroupFragment)
    fun inject(recommendationFragment: RecommendationFragment)
    fun inject(saranTabsFragment: SaranTabsFragment)
    fun inject(groupDetailFragment: GroupDetailFragment)
    fun inject(potentialProductFragment: PotentialProductFragment)
}
