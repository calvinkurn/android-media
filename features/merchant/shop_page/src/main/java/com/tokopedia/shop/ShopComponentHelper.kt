package com.tokopedia.shop

import android.app.Application
import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shop.common.di.component.DaggerShopComponent
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.di.module.ShopModule

/**
 * Created by nakama on 11/12/17.
 */

class ShopComponentHelper {

    fun getComponent(
        application: Application,
        activityContext: Context
    ): ShopComponent = DaggerShopComponent.builder().baseAppComponent(
        (application as BaseMainApplication).baseAppComponent
    )
        .shopModule(ShopModule(activityContext)).build()
}
