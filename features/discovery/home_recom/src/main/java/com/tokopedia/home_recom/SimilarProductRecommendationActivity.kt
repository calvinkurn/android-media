package com.tokopedia.home_recom

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.view.fragment.SimilarProductRecommendationFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero

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

    private fun isNumber(text: String): Boolean{
        return (text.toIntOrNull() != null)
    }

    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
}