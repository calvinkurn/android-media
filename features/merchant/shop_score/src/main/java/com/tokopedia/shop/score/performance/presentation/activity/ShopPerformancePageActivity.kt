package com.tokopedia.shop.score.performance.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.score.performance.di.component.DaggerShopPerformanceComponent
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment

class ShopPerformancePageActivity : BaseSimpleActivity(), HasComponent<ShopPerformanceComponent> {

    override fun getNewFragment(): Fragment = ShopPerformancePageFragment.newInstance()

    override fun getComponent(): ShopPerformanceComponent {
        return DaggerShopPerformanceComponent
                .builder()
                .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
                .build()
    }
}