package com.tokopedia.home_recom

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_recom.analytics.SimilarProductRecommendationTracking
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.view.fragment.SimilarProductRecommendationFragment
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by Lukas on 26/08/19
 */
class SimilarProductRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent> {
    companion object{
        private const val EXTRA_REF = "REF"
        private const val EXTRA_PRODUCT_ID = "PRODUCT_ID"
    }

    override fun getNewFragment(): Fragment? {
        return when {
            intent.hasExtra(EXTRA_REF) -> SimilarProductRecommendationFragment.newInstance(intent.getStringExtra(EXTRA_PRODUCT_ID) ?: "", intent.getStringExtra(EXTRA_REF) ?: "")
            intent.data != null -> SimilarProductRecommendationFragment.newInstance(if(isNumber(intent.data?.pathSegments?.get(0) ?: "")) intent.data?.pathSegments?.get(0) ?: ""
                    else "",intent.data?.getQueryParameter("ref") ?: "")
            else -> SimilarProductRecommendationFragment.newInstance("")
        }
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
     * [onBackPressed] is override from [BaseSimpleActivity]
     * this function will handle user press back with back button at device
     * and send tracking also routing to home
     */
    override fun onBackPressed() {
        SimilarProductRecommendationTracking.eventClickBackButton()
        this.finish()
    }
}