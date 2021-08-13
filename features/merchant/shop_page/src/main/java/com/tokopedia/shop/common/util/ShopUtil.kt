package com.tokopedia.shop.common.util

import android.content.Context
import android.text.TextUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.RollenceKey.AB_TEST_ROLLOUT_NEW_SHOP_ETALASE
import com.tokopedia.shop.common.constant.IGNORED_FILTER_KONDISI
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENAWARAN
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENGIRIMAN
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.DATA_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.FUNCTION_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.LIVE_DATA_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.REASON_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.SHOP_ID_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.SHOP_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.TYPE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.USER_ID_KEY
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ShopUtil {
    fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    fun isMyShop(shopId: String, userSessionShopId: String)  = shopId == userSessionShopId

    fun logTimberWarning(priority: Priority, tag: String, extraMessage: Map<String, String>) {
        ServerLogger.log(priority, tag, extraMessage)
    }

    fun logShopPageP2BuyerFlowAlerting(
            tag: String,
            functionName: String,
            liveDataName: String = "",
            userId: String = "",
            shopId: String,
            shopName: String = "",
            errorMessage: String,
            stackTrace: String,
            errType: String
    ) {
        val extraParam = mapOf(
                TYPE to errType,
                FUNCTION_NAME_KEY to functionName,
                LIVE_DATA_NAME_KEY to liveDataName,
                SHOP_ID_KEY to shopId,
                USER_ID_KEY to userId,
                SHOP_NAME_KEY to shopName,
                REASON_KEY to errorMessage,
                DATA_KEY to stackTrace
        )
        logTimberWarning(Priority.P2, tag, extraParam)
    }

    fun isExceptionIgnored(throwable: Throwable) = throwable is UnknownHostException || throwable is SocketTimeoutException

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
                RollenceKey.NAVIGATION_EXP_TOP_NAV,
                RollenceKey.NAVIGATION_VARIANT_OLD
        )
        return (navType == RollenceKey.NAVIGATION_VARIANT_REVAMP && !GlobalConfig.isSellerApp())
    }

    fun getShopPageWidgetUserAddressLocalData(context: Context?): LocalCacheModel? {
        return context?.let{
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
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
}