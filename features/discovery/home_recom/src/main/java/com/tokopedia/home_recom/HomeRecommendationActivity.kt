package com.tokopedia.home_recom

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.analytics.SimilarProductRecommendationTracking
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.view.fragment.RecommendationFragment
import com.tokopedia.home_recom.view.fragment.SimilarProductRecommendationFragment

/**
 * Created by lukas on 21/05/2019
 *
 * A activity class for default activity when opening recommendation page from deeplink
 */
@SuppressLint("GoogleAppIndexingApiWarning")
class HomeRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent>{
    companion object{
        private const val PRODUCT_ID = "PRODUCT_ID"

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

    override fun getParentViewResourceID(): Int = com.tokopedia.home_recom.R.id.recom_container

    override fun getLayoutRes(): Int = com.tokopedia.home_recom.R.layout.recommendation_activity

    override fun getToolbarResourceID(): Int = com.tokopedia.home_recom.R.id.recom_toolbar

    /**
     * [getNewFragment] is override from [BaseSimpleActivity]
     * @return default fragment it will shown at activity
     */
    override fun getNewFragment(): Fragment {
        return when{
            intent.data != null -> {
                if(isSimilarProduct(intent?.data?.toString() ?: "")) SimilarProductRecommendationFragment.newInstance(
                        getSimilarRecomPageProductId(),
                        getRef(),
                        getSource(),
                        getInternalRef(),
                        FragmentInflater.ACTIVITY
                )
                else RecommendationFragment
                        .newInstance(
                                getRecomPageProductId(),
                                getSource(),
                                getRef(),
                                getInternalRef(),
                                FragmentInflater.ACTIVITY)
            }
            else -> {
                RouteManager.route(this, ApplinkConst.HOME)
                RecommendationFragment.newInstance()
            }
        }
    }

    private fun getRecomPageProductId() = intent.data?.lastPathSegment ?: ""

    private fun getSource() = intent.data?.query ?: ""

    private fun getRef() = intent.data?.getQueryParameter("ref") ?: "null"

    private fun getInternalRef() = intent.data?.getQueryParameter("search_ref") ?: ""

    private fun getSimilarRecomPageProductId() =
            if (isNumber(intent.data?.pathSegments?.get(1) ?: "")) intent.data?.pathSegments?.get(1)
                    ?: ""
            else ""

    /**
     * Function [isSimilarProduct]
     * This function will checking this is from deeplink similar or
     * deeplink home recom landing page
     * @param url is string variable for checking this is number or not
     * @return boolean
     */
    private fun isSimilarProduct(url: String): Boolean{
        return url.contains("/d/") || url.contains("/d?") || url.contains("/d")
    }

    /**
     * Function [isNumber]
     * This function will checking input is number or not
     * @param text is string variable for checking this is number or not
     * @return boolean
     */
    private fun isNumber(text: String): Boolean{
        return (text.toIntOrNull() != null)
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
                onBackPressed()
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
        if(!isSimilarProduct(intent?.data?.toString() ?: "")) {
            if(intent?.data?.pathSegments?.isEmpty() == false && isNumber(intent.data?.pathSegments?.get(0)
                            ?: "")){
                RecommendationPageTracking.eventUserClickBackWithProductId()
            }else{
                RecommendationPageTracking.eventUserClickBack()
            }
        } else {
            SimilarProductRecommendationTracking.eventClickBackButton()
        }
        if(isTaskRoot) RouteManager.route(this, ApplinkConst.HOME)
        this.finish()
    }
}
