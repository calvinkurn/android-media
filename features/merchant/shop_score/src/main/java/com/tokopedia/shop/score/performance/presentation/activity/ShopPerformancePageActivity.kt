package com.tokopedia.shop.score.performance.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.score.performance.di.ShopPerformanceComponentBuilder
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment

class ShopPerformancePageActivity : BaseSimpleActivity(), HasComponent<ShopPerformanceComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        return ShopPerformancePageFragment.newInstance(bundle)
    }

    override fun getComponent(): ShopPerformanceComponent {
        return DaggerShopPerformanceComponent()
                .builder()
                .shopPerformanceComponent(ShopPerformanceComponentBuilder.getComponent(application))
                .build()
    }
}