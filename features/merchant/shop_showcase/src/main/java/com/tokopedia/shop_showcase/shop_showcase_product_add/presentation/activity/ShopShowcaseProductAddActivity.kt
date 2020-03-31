package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddFragment

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShopShowcaseProductAddActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return ShopShowcaseProductAddFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    companion object {
        val ACTIVITY_LAYOUT = R.layout.activity_shop_showcase_product_add
    }
}