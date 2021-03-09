package com.tokopedia.shop.showcase.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.R
import com.tokopedia.shop.showcase.di.component.DaggerShopPageShowcaseComponent
import com.tokopedia.shop.showcase.di.component.ShopPageShowcaseComponent

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseFragment : BaseDaggerFragment(), HasComponent<ShopPageShowcaseComponent> {

    companion object {

        @JvmStatic
        fun createInstance(): ShopPageShowcaseFragment = ShopPageShowcaseFragment()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_page_showcase, container, false)
    }

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