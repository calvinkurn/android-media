package com.tokopedia.home_recom

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.view.fragment.SimilarProductRecommendationFragment

/**
 * Created by Lukas on 26/08/19
 */
class SimilarProductRecommendationActivity : BaseSimpleActivity(), HasComponent<HomeRecommendationComponent> {
    override fun getNewFragment(): Fragment? {
        return SimilarProductRecommendationFragment()
    }

    override fun getComponent(): HomeRecommendationComponent = DaggerHomeRecommendationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
}