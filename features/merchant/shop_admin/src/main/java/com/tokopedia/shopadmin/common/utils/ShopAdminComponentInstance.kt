package com.tokopedia.shopadmin.common.utils

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shopadmin.common.di.DaggerShopAdminComponent
import com.tokopedia.shopadmin.common.di.ShopAdminComponent

object ShopAdminComponentInstance {
    private lateinit var shopAdminComponent: ShopAdminComponent

    fun getShopAdminComponent(application: Application): ShopAdminComponent {
        if (!::shopAdminComponent.isInitialized) {
            shopAdminComponent = DaggerShopAdminComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
        }

        return shopAdminComponent
    }
}