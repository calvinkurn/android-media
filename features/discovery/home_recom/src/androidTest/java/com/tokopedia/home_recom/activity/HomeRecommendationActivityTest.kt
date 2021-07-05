package com.tokopedia.home_recom.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_recom.di.DaggerHomeRecommendationComponent
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.test.R
import com.tokopedia.home_recom.view.fragment.RecommendationFragment

/**
 * Created by Lukas on 26/07/20.
 */

class HomeRecommendationActivityTest : AppCompatActivity(), HasComponent<HomeRecommendationComponent> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_recom_test)

        val homeRecommendation: Fragment = RecommendationFragment.newInstance("596843822", "ref=cart", "", "")

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container_home, homeRecommendation)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun getComponent(): HomeRecommendationComponent {
        return DaggerHomeRecommendationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
    }
}