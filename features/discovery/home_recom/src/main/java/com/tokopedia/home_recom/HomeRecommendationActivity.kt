package com.tokopedia.home_recom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.view.recommendation.RecommendationFragment

class HomeRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent>{

    private var isFromDeeplink = false

    companion object{
        @JvmStatic
        fun getStartIntent(context: Context) = Intent(context, HomeRecommendationActivity::class.java)
    }

    object DeeplinkIntents{
        @DeepLink(ApplinkConst.HOME_RECOMMENDATION_PAGE)
        @JvmStatic
        fun getStartIntent(context: Context, extras: Bundle): Intent{
            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.HOME_RECOMMENDATION, uri.lastPathSegment) ?: Intent()
        }
    }

    override fun getNewFragment(): Fragment {
        return RecommendationFragment.newInstance()
    }

    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

}