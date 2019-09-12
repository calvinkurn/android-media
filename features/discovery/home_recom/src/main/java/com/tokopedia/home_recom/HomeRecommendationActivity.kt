package com.tokopedia.home_recom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MenuItem
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.view.fragment.RecommendationFragment

class HomeRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent>{
    private lateinit var productId: String
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
        @DeepLink(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE)
        fun getDefaultCallingIntent(context: Context, extras: Bundle): Intent{
            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.HOME_RECOMMENDATION, "")
        }

        @JvmStatic
        @DeepLink(ApplinkConst.RECOMMENDATION_PAGE)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
            return RouteManager.getIntent(context,
                    ApplinkConstInternalMarketplace.HOME_RECOMMENDATION,
                    uri.lastPathSegment) ?: Intent()
        }
    }

    override fun getNewFragment(): Fragment {
        return if(intent.data != null && intent.data.pathSegments.size > 1 ){
            RecommendationFragment.newInstance(intent.data.lastPathSegment ?: "")
        } else if (intent.hasExtra(PRODUCT_ID)) {
            RecommendationFragment.newInstance(intent.getStringExtra(PRODUCT_ID))
        } else {
            RecommendationFragment.newInstance()
        }
    }

    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                RecommendationPageTracking.eventUserClickBack()
                RouteManager.route(this, ApplinkConst.HOME)
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}