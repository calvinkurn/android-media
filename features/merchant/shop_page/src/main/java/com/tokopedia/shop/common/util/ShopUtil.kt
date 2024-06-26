package com.tokopedia.shop.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.common.constant.IGNORED_FILTER_KONDISI
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENAWARAN
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENGIRIMAN
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_PER_FOR_SHARE_PURPOSE
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_PER_PAGE_NON_TABLET
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_PER_PAGE_TABLET
import com.tokopedia.shop.common.constant.ShopPageConstant.VALUE_INT_ONE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.DATA_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.FUNCTION_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.LIVE_DATA_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.REASON_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.SHOP_ID_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.SHOP_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.TYPE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.USER_ID_KEY
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ShopUtil {
    var isFoldableAndHorizontalScreen: Boolean = false
    var isFoldable: Boolean = true

    fun getProductPerPage(context: Context?, isShare: Boolean = false): Int {
        return context?.let {
            if (DeviceScreenInfo.isTablet(context)) {
                DEFAULT_PER_PAGE_TABLET
            } else {
                DEFAULT_PER_PAGE_NON_TABLET
            }
        } ?: DEFAULT_PER_PAGE_NON_TABLET
    }

    fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    fun isMyShop(shopId: String, userSessionShopId: String) = shopId == userSessionShopId

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
        return context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }


    fun <E> MutableList<E>.setElement(index: Int, element: E) {
        if (index in 0 until size) {
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

    fun getShopGridViewTypeString(gridType: ShopProductViewGridType): String {
        return when (gridType) {
            ShopProductViewGridType.LIST -> ShopPageTrackingConstant.LIST_VIEW_TYPE
            ShopProductViewGridType.SMALL_GRID -> ShopPageTrackingConstant.GRID_VIEW_TYPE
            ShopProductViewGridType.BIG_GRID -> ShopPageTrackingConstant.BIG_GRID_VIEW_TYPE
        }
    }

    fun getActualPositionFromIndex(indexPosition: Int): Int {
        return indexPosition + VALUE_INT_ONE
    }

    fun loadImageWithEmptyTarget(
        context: Context,
        url: String,
        properties: Properties.() -> Unit,
        mediaTarget: MediaBitmapEmptyTarget<Bitmap>
    ) {
        com.tokopedia.media.loader.loadImageWithEmptyTarget(context, url, properties, mediaTarget)
    }

    fun String.isUrlPng(): Boolean {
        return endsWith(".png")
    }

    fun String.isUrlJson(): Boolean {
        return endsWith(".json")
    }

    fun parseColorFromHexString(colorHex: String): Int {
        return try {
            Color.parseColor(colorHex)
        } catch (e: Exception) {
            // TODO need to add default color from unify, but need to pass context on param
            FirebaseCrashlytics.getInstance().recordException(e)
            Int.ZERO
        }
    }
    
    fun parseColorOrFallback(hexColor: String, fallbackColor: Int): Int {
        return try {
            Color.parseColor(hexColor)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            fallbackColor
        }
    }

    fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }

    fun formatPrice(displayedPrice: String): String {
        return if (!TextUtils.isEmpty(displayedPrice)) {
            displayedPrice.replace("\\D".toRegex(), "")
        } else {
            ""
        }
    }

    fun isEnableShopPageReImagined(context: Context?): Boolean {
        return FirebaseRemoteConfigImpl(context).getBoolean(
            RemoteConfigKey.ENABLE_SHOP_PAGE_REIMAGINED,
            true
        )
    }

    fun isFulfillmentByGroupLabel(labelGroupList: List<LabelGroup>): Boolean? {
        return if (labelGroupList.isEmpty()) {
            null
        } else {
            labelGroupList.any { it.position == ShopPageTrackingConstant.LABEL_GROUP_POSITION_FULFILLMENT }
        }
    }
}
