package com.tokopedia.home_recom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
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

    override fun getNewFragment(): Fragment {
        return RecommendationFragment.newInstance(intent.getStringExtra(PRODUCT_ID))
    }

    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

}