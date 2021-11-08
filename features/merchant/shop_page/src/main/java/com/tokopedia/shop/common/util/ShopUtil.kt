package com.tokopedia.shop.common.util

import android.content.Context
import android.text.TextUtils
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.AB_TEST_SHOP_NEW_HOME_TAB
import com.tokopedia.remoteconfig.RollenceKey.AB_TEST_SHOP_REVIEW
import com.tokopedia.remoteconfig.RollenceKey.NEW_REVIEW_SHOP
import com.tokopedia.remoteconfig.RollenceKey.OLD_REVIEW_SHOP
import com.tokopedia.remoteconfig.RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_KEY
import com.tokopedia.remoteconfig.RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD
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

    fun isUsingNewShopReviewPage(): Boolean {
        val shopReviewAbTestKey = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                AB_TEST_SHOP_REVIEW,
                OLD_REVIEW_SHOP
        )
        return shopReviewAbTestKey.equals(NEW_REVIEW_SHOP, true)
    }

    fun isUsingNewShopHomeTab(): Boolean {
        val newShopHomeTabAbTestKey = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                AB_TEST_SHOP_NEW_HOME_TAB,
                ""
        ).orEmpty()
        return newShopHomeTabAbTestKey.isNotEmpty()
    }

    fun <E> MutableList<E>.setElement(index: Int, element: E){
        if(index in 0 until size){
            set(index, element)
        }
    }
}