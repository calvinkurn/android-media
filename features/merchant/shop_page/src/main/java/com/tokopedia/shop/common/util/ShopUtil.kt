package com.tokopedia.shop.common.util

import android.content.Context
import com.tokopedia.config.GlobalConfig
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.shop.common.constant.IGNORED_FILTER_KONDISI
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENAWARAN
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENGIRIMAN
import com.tokopedia.shop.common.constant.ShopPageConstant.AB_TEST_NEW_SHOP_HEADER_KEY
import com.tokopedia.shop.common.constant.ShopPageConstant.AB_TEST_NEW_SHOP_HEADER_NEW_VALUE
import com.tokopedia.shop.common.constant.ShopPageConstant.AB_TEST_ROLLOUT_NEW_SHOP_ETALASE
import com.tokopedia.shop.common.constant.ShopPageConstant.REMOTE_CONFIG_ENABLE_NEW_SHOP_PAGE_HEADER
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import timber.log.Timber

object ShopUtil {
    fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    fun isMyShop(shopId: String, userSessionShopId: String)  = shopId == userSessionShopId

    fun logTimberWarning(errorTag: String, errorData: Map<String, String>) {
        ServerLogger.log(Priority.P2, errorTag, errorData)
    }

    fun isFilterNotIgnored(title: String): Boolean {
        return when (title.toLowerCase()) {
            IGNORED_FILTER_PENGIRIMAN -> false
            IGNORED_FILTER_PENAWARAN -> false
            IGNORED_FILTER_KONDISI -> false
            else -> true
        }
    }

    fun isUsingNewNavigation(): Boolean {
        val navType = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                AbTestPlatform.NAVIGATION_EXP_TOP_NAV,
                AbTestPlatform.NAVIGATION_VARIANT_OLD
        )
        return (navType == AbTestPlatform.NAVIGATION_VARIANT_REVAMP && !GlobalConfig.isSellerApp())
    }

    fun getShopPageWidgetUserAddressLocalData(context: Context?): LocalCacheModel? {
        return context?.let{
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    fun isUsingNewShopPageHeader(context: Context?): Boolean {
        val abTestShopHeaderType = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                AB_TEST_NEW_SHOP_HEADER_KEY,
                AB_TEST_NEW_SHOP_HEADER_NEW_VALUE
        )
        val remoteConfigEnableNewShopHeaderValue = FirebaseRemoteConfigImpl(context).getBoolean(
                REMOTE_CONFIG_ENABLE_NEW_SHOP_PAGE_HEADER,
                true
        )
        return abTestShopHeaderType.equals(AB_TEST_NEW_SHOP_HEADER_NEW_VALUE, true) && remoteConfigEnableNewShopHeaderValue
    }

    fun isShouldCheckShopType(): Boolean {
        val shopEtalaseRevampKey = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                AB_TEST_ROLLOUT_NEW_SHOP_ETALASE,
                ""
        )
        return shopEtalaseRevampKey.equals(AB_TEST_ROLLOUT_NEW_SHOP_ETALASE, true)
    }

    fun isNotRegularMerchant(shopPageHeaderDataModel: ShopPageHeaderDataModel?): Boolean {
        return shopPageHeaderDataModel?.let { shop ->
            shop.isGoldMerchant || shop.isOfficial
        } ?: false
    }
}