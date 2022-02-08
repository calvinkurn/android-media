package com.tokopedia.shop_widget.mvc_locked_to_product.util

import android.content.Context
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductConstant.VALUE_INT_ONE

object MvcLockedToProductUtil {

    fun getWidgetUserAddressLocalData(context: Context?): LocalCacheModel {
        return context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        } ?: LocalCacheModel()
    }

    fun getActualPositionFromIndex(indexPosition: Int): Int{
        return indexPosition + VALUE_INT_ONE
    }

    fun isSellerView(shopId: String, userSessionShopId: String): Boolean{
        return shopId == userSessionShopId
    }

    fun isMvcPhase2(): Boolean{
        val experimentName = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
            "asd",
            "asd"
        )
        return true
    }
}