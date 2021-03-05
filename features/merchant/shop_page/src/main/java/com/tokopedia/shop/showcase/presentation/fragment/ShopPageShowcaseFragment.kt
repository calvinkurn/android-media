package com.tokopedia.shop.showcase.presentation.fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.showcase.di.component.DaggerShopPageShowcaseComponent
import com.tokopedia.shop.showcase.di.component.ShopPageShowcaseComponent

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseFragment : BaseDaggerFragment(), HasComponent<ShopPageShowcaseComponent> {

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun getComponent(): ShopPageShowcaseComponent? {
        return activity?.run {
            DaggerShopPageShowcaseComponent
                    .builder()
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

}