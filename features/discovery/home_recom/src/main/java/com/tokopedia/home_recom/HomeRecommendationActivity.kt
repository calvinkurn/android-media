package com.tokopedia.home_recom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.view.fragment.RecommendationFragment

class HomeRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent>{

    companion object{
        const val PRODUCT_ID = "PRODUCT_ID"
        private const val DEEP_LINK_URI = "deep_link_uri"

        @JvmStatic
        fun newInstance(context: Context) = Intent(context, HomeRecommendationActivity::class.java)

        @JvmStatic
        fun newInstance(context: Context, bundle: Bundle) = Intent(context, HomeRecommendationActivity::class.java).apply {
            putExtras(bundle)
        }

        @JvmStatic
        fun newInstance(context: Context, productId: String) = Intent(context, HomeRecommendationActivity::class.java).apply {
            putExtra(PRODUCT_ID, productId)
        }
    }

    object DeeplinkIntents{

        @JvmStatic
        @DeepLink(ApplinkConst.RECOMMENDATION_PAGE)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
            return RouteManager.getIntent(context,
                    ApplinkConstInternalMarketplace.HOME_RECOMMENDATION_WITH_ID,
                    uri.lastPathSegment) ?: Intent()
        }
    }

    override fun getNewFragment(): Fragment {
        return RecommendationFragment.newInstance(intent.getStringExtra(PRODUCT_ID))
    }

    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                RecommendationPageTracking.eventUserClickBack()
                RouteManager.route(this, ApplinkConst.HOME)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}