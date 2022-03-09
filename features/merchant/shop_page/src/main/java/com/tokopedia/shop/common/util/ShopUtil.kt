package com.tokopedia.shop.common.util

import android.content.Context
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_KEY
import com.tokopedia.remoteconfig.RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.common.constant.IGNORED_FILTER_KONDISI
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENAWARAN
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENGIRIMAN
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_PER_PAGE_NON_TABLET
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_PER_PAGE_TABLET
import com.tokopedia.shop.common.constant.ShopPageConstant.VALUE_INT_ONE
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet

object ShopUtil {
    fun getProductPerPage(context: Context?): Int{
        return context?.let{
            if(DeviceScreenInfo.isTablet(context)){
                DEFAULT_PER_PAGE_TABLET
            } else {
                DEFAULT_PER_PAGE_NON_TABLET
            }
        }?: DEFAULT_PER_PAGE_NON_TABLET
    }

    fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    fun isMyShop(shopId: String, userSessionShopId: String)  = shopId == userSessionShopId

    fun isFilterNotIgnored(title: String): Boolean {
        return when (title.toLowerCase()) {
            IGNORED_FILTER_PENGIRIMAN -> false
            IGNORED_FILTER_PENAWARAN -> false
            IGNORED_FILTER_KONDISI -> false
            else -> true
        }
    }

    fun getShopPageWidgetUserAddressLocalData(context: Context?): LocalCacheModel? {
        return context?.let{
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    fun getShopFollowButtonAbTestVariant(): String? {
        return RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                AB_TEST_SHOP_FOLLOW_BUTTON_KEY,
                AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD
        )
    }

    fun isUsingNewShareBottomSheet(context: Context): Boolean {
        return UniversalShareBottomSheet.isCustomSharingEnabled(
                context,
                ShopPageConstant.ENABLE_SHOP_PAGE_UNIVERSAL_BOTTOM_SHEET
        )
    }

    fun joinStringWithDelimiter(vararg listString: String, delimiter: String): String {
        val filteredListString = listString.filter {
            it.isNotEmpty()
        }
        return TextUtils.join(delimiter, filteredListString)
    }

    fun <E> MutableList<E>.setElement(index: Int, element: E){
        if(index in 0 until size){
            set(index, element)
        }
    }

    fun getColorHexString(context: Context, idColor: Int): String {
        return try {
            val colorHexInt = ContextCompat.getColor(context, idColor)
            val startIndex = 2
            val colorToHexString = Integer.toHexString(colorHexInt).uppercase().substring(startIndex)
            return "#$colorToHexString"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getShopGridViewTypeString(gridType: ShopProductViewGridType) : String {
        return when (gridType) {
            ShopProductViewGridType.LIST -> ShopPageTrackingConstant.LIST_VIEW_TYPE
            ShopProductViewGridType.SMALL_GRID -> ShopPageTrackingConstant.GRID_VIEW_TYPE
            ShopProductViewGridType.BIG_GRID -> ShopPageTrackingConstant.BIG_GRID_VIEW_TYPE
        }
    }

    fun getActualPositionFromIndex(indexPosition: Int): Int{
        return indexPosition + VALUE_INT_ONE
    }
}