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
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.annotation.RegEx
/**
 * Created by lukas on 21/05/2019
 *
 * A activity class for default activity when opening recommendation page from deeplink
 */
class HomeRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent>{
    companion object{
        const val PRODUCT_ID = "PRODUCT_ID"
        private const val REF = "REF"

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

//    object DeeplinkIntents{
//        /**
//         * [getDefaultCallingIntent] for handling deeplink without product id with pattern "tokopedia://rekomendasi/?ref={ref}"
//         * @param context the default context
//         * @param extras the default extras
//         */
//        @JvmStatic
//        @DeepLink(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE)
//        fun getDefaultCallingIntent(context: Context, extras: Bundle): Intent{
//            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
//            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.HOME_RECOMMENDATION, "", "")
//        }
//
//        /**
//         * [getDefaultCallingIntent] for handling deeplink with product id with pattern "tokopedia://rekomendasi/{product_id}/?ref={ref}"
//         * @param context the default context
//         * @param extras the default extras
//         */
//        @JvmStatic
//        @DeepLink(ApplinkConst.RECOMMENDATION_PAGE)
//        fun getCallingIntent(context: Context, extras: Bundle): Intent {
//            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
//            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.HOME_RECOMMENDATION, uri.lastPathSegment, "") ?: Intent()
//        }
//
//        /**
//         * [getDefaultCallingIntent] for handling deeplink without product id with pattern "tokopedia://rekomendasi/?ref={ref}"
//         * @param context the default context
//         * @param extras the default extras
//         */
//        @JvmStatic
//        @DeepLink(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE_WITH_REF)
//        fun getDefaultCallingIntentWithRef(context: Context, extras: Bundle): Intent{
//            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
//            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.HOME_RECOMMENDATION, "", uri.getQueryParameter(REF) ?: "")
//        }
//
//        /**
//         * [getDefaultCallingIntent] for handling deeplink with product id with pattern "tokopedia://rekomendasi/{product_id}/?ref={ref}"
//         * @param context the default context
//         * @param extras the default extras
//         */
//        @JvmStatic
//        @DeepLink(ApplinkConst.RECOMMENDATION_PAGE_WITH_REF)
//        fun getCallingIntentWithRef(context: Context, extras: Bundle): Intent {
//            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
//            return RouteManager.getIntent(context,
//                    ApplinkConstInternalMarketplace.HOME_RECOMMENDATION,
//                    uri.lastPathSegment,
//                    uri.getQueryParameter(REF) ?: "") ?: Intent()
//        }
//    }

    /**
     * [getNewFragment] is override from [BaseSimpleActivity]
     * @return default fragment it will shown at activity
     */
    override fun getNewFragment(): Fragment {
        return if(intent.hasExtra(PRODUCT_ID) && intent.hasExtra(REF)) {
            RecommendationFragment.newInstance(intent.getStringExtra(PRODUCT_ID), intent.getStringExtra(REF))
        } else if(intent.data != null && intent.data?.scheme == ApplinkConst.APPLINK_CUSTOMER_SCHEME){
            RecommendationFragment.newInstance(intent.data?.lastPathSegment ?: "", intent.data?.getQueryParameter("ref") ?: "")
        } else {
            RecommendationFragment.newInstance()
        }
    }

    /**
     * [onPause] is override from [BaseSimpleActivity]
     * this void override with added extra sendAllTracking
     */
    override fun onPause() {
        super.onPause()
        TrackingQueue(this).sendAll()
    }

    /**
     * [getComponent] is override from [BaseSimpleActivity]
     * this function will handle dependency injection with return dagger component
     * for a whole fragment it will show at this activity
     */
    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()


    /**
     * [onOptionsItemSelected] is override from [BaseSimpleActivity]
     * this function will handle options item selected
     */
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

    /**
     * [onBackPressed] is override from [BaseSimpleActivity]
     * this function will handle user press back with back button at device
     * and send tracking also routing to home
     */
    override fun onBackPressed() {
        RecommendationPageTracking.eventUserClickBack()
        RouteManager.route(this, ApplinkConst.HOME)
        this.finish()
    }
}