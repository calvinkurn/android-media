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
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent

class HomeRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent>{

    companion object{
        private const val PRODUCT_ID = "PRODUCT_ID"

        @JvmStatic
        fun createIntent(context: Context) = Intent(context, HomeRecommendationActivity::class.java)

        @JvmStatic
        fun createIntent(context: Context, bundle: Bundle) = Intent(context, HomeRecommendationActivity::class.java).apply {
            putExtras(bundle)
        }

        @JvmStatic
        fun createIntent(context: Context, productId: String) = Intent(context, HomeRecommendationActivity::class.java).apply {
            putExtra(PRODUCT_ID, productId)
        }
    }

    override fun getNewFragment(): Fragment {
        return RecommendationFragment.newInstance()
    }

    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

}