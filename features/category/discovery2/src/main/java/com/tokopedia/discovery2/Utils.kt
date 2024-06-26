package com.tokopedia.discovery2

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tkpd.atcvariant.BuildConfig
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.byteio.RefreshType
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.MoveAction
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.QUERY_PARENT
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.common.domain.data.*
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.floor

const val LABEL_PRODUCT_STATUS = "status"
const val LABEL_PRICE = "price"
const val TIME_DISPLAY_FORMAT = "%1$02d"
const val DEFAULT_TIME_DATA: Long = 0
const val CONSTANT_10_e = 1e1
const val CONSTANT_0 = 0
const val CONSTANT_10 = 10
const val CONSTANT_11 = 11
const val CONSTANT_19 = 19

class Utils {
    companion object {
        const val TIME_ZONE = "Asia/Jakarta"
        const val TIMER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
        private const val TIMER_SPRINT_SALE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val DEFAULT_BANNER_WIDTH = 800
        const val DEFAULT_BANNER_HEIGHT = 150
        const val DEFAULT_BANNER_WEIGHT = 1.0f
        const val BANNER_SUBSCRIPTION_DEFAULT_POSITION = -1
        const val BANNER_SUBSCRIPTION_DEFAULT_STATUS = -1
        const val BANNER_SUBSCRIPTION_REMINDED_STATUS = 1
        const val BANNER_SUBSCRIPTION_UNREMINDED_STATUS = 0
        const val SEARCH_DEEPLINK = "tokopedia://search-autocomplete"
        private const val SERIBU = 1000
        private const val SEJUTA = 1000000
        private const val SEMILIAR = 1000000000
        private const val VIEW_LIMIT = 1.0
        private const val SERIBU_TEXT = "rb orang"
        private const val SEJUTA_TEXT = "jt orang"
        private const val SEMILIAR_TEXT = "M orang"
        var preSelectedTab = -1
        const val IDENTIFIER = "identifier"
        private const val COMPONENT_ID = "component_id"
        const val DEVICE = "device"
        const val DEVICE_VALUE = "Android"
        const val FILTERS = "filters"
        const val REFRESH_TYPE = "refresh_type"
        const val SESSION_ID = "current_session_id"
        const val VERSION = "version"
        const val SRE_IDENTIFIER = "l_name"
        const val SRE_VALUE = "sre"
        const val SECTION_ID = "section_id"
        private const val COUNT_ONLY = "count_only"
        private const val RPC_USER_ID = "rpc_UserID"
        const val RPC_PAGE_NUMBER = "rpc_page_number"
        const val RPC_PAGE_SIZE = "rpc_page_size"
        const val RPC_NEXT_PAGE = "rpc_next_page"
        const val RPC_FILTER_KEY = "rpc_"
        const val DARK_MODE = "dark_mode"
        private const val DEFAULT_ENCODING = "UTF-8"

        private val setOfKeysToNotSendToShare = mutableSetOf(
            DiscoveryActivity.AFFILIATE_UNIQUE_ID,
            DiscoveryActivity.CHANNEL,
            QUERY_PARENT,
            DiscoveryActivity.SOURCE
        )

        fun extractDimension(url: String?, dimension: String = "height"): Int? {
            val uri = Uri.parse(url)

            try {
                return uri?.getQueryParameter(dimension)?.toInt()
            } catch (e: Exception) {
                // Added temp fix for disco to handled in invlaid url from backend

                try {
                    val newUrl =
                        uri?.getQueryParameter(dimension)?.replace("[^-?0-9]+".toRegex(), " ")
                            ?.replace("?", "")
                    val parts = newUrl?.trim()?.split(" ")
                    return parts?.get(0)?.toInt()
                } catch (e: Exception) {
                }
            }

            return 1
        }

        fun shareData(context: Context?, shareTxt: String?, productUri: String?) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + productUri)
            context?.startActivity(Intent.createChooser(share, shareTxt))
        }

        fun getCountView(countView: Double, notifyMeText: String = ""): String = when {
            countView >= SERIBU && countView < SEJUTA -> {
                getDisplayValue(getDecimalFormatted(countView / SERIBU), SERIBU_TEXT, notifyMeText)
            }

            countView >= SEJUTA && countView < SEMILIAR -> {
                getDisplayValue(getDecimalFormatted(countView / SEJUTA), SEJUTA_TEXT, notifyMeText)
            }

            countView >= SEMILIAR -> {
                getDisplayValue(
                    getDecimalFormatted(countView / SEMILIAR),
                    SEMILIAR_TEXT,
                    notifyMeText
                )
            }

            else -> ""
        }

        private fun getDecimalFormatted(currentViewCount: Double) =
            floor(currentViewCount * CONSTANT_10_e) / CONSTANT_10_e

        private fun getDisplayValue(
            convertedValue: Double,
            text: String,
            notifyMeText: String
        ): String {
            return if (convertedValue > VIEW_LIMIT) {
                "${convertedValue.toString().replace('.', ',')} $text $notifyMeText"
            } else {
                "${convertedValue.toInt()} $text $notifyMeText"
            }
        }

        fun getQueryMap(
            componentId: String,
            pageIdentifier: String,
            selectedFilterMapParameter: Map<String, String?>? = null,
            userId: String? = "0",
            addCountFilters: Boolean = false
        ): Map<String, Any> {
            val component = getComponent(componentId, pageIdentifier)
            val filtersMasterMapParam = mutableMapOf<String, String?>()
            discoComponentQuery?.let {
                filtersMasterMapParam.putAll(it)
            }
            filtersMasterMapParam.remove(QUERY_PARENT)

            component?.let {
                filtersMasterMapParam.putAll(addAddressQueryMapWithWareHouse(it.userAddressData))
            }
            if (addCountFilters && selectedFilterMapParameter != null) {
                val filtersMap = selectedFilterMapParameter as MutableMap<String, String?>
                filtersMap.let {
                    it[COUNT_ONLY] = "true"
                    it[RPC_PAGE_SIZE] = "10"
                    it[RPC_PAGE_NUMBER] = "1"
                    it[RPC_USER_ID] = if (userId.isNullOrEmpty()) "0" else userId
                }
                filtersMasterMapParam.putAll(filtersMap)
            }
            val queryString = StringBuilder()
            filtersMasterMapParam.forEach { (key, value) ->
                if (!value.isNullOrEmpty()) {
                    if (queryString.isNotEmpty()) {
                        queryString.append('&')
                    }
                    queryString.append(key).append('=').append(value)
                }
            }

            return getComponentsGQLParams(componentId, pageIdentifier, queryString.toString())
        }

        fun getComponentsGQLParams(
            componentId: String,
            pageIdentifier: String,
            queryString: String,
            sessionId: String = String.EMPTY,
            refreshType: RefreshType = RefreshType.UNKNOWN
        ): MutableMap<String, Any> {
            val queryParameterMap = mutableMapOf<String, Any>()
            queryParameterMap[IDENTIFIER] = pageIdentifier
            queryParameterMap[DEVICE] = DEVICE_VALUE
            queryParameterMap[COMPONENT_ID] = componentId
            if (queryString.isNotEmpty()) queryParameterMap[FILTERS] = queryString
            queryParameterMap[SESSION_ID] = sessionId
            queryParameterMap[REFRESH_TYPE] = refreshType.value.toString()
            return queryParameterMap
        }

        fun getQueryString(filtersMasterMapParam: MutableMap<String, Any>): String {
            val queryString = StringBuilder()
            filtersMasterMapParam.forEach { (key, value) ->
                if (queryString.isNotEmpty()) {
                    queryString.append('&')
                }
                queryString.append(key).append('=').append(value)
            }
            return queryString.toString()
        }

        fun addAddressQueryMap(userAddressData: LocalCacheModel?): MutableMap<String, String> {
            val addressQueryParameterMap = mutableMapOf<String, String>()
            userAddressData?.let {
                if (it.address_id.isNotEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_ADDRESS_ID] =
                        it.address_id
                }
                if (it.city_id.isNotEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_CITY_ID] =
                        it.city_id
                }
                if (it.district_id.isNotEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_DISTRICT_ID] =
                        it.district_id
                }
                if (it.lat.isNotEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_LAT] =
                        it.lat
                }
                if (it.long.isNotEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_LONG] =
                        it.long
                }
                if (it.postal_code.isNotEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_POST_CODE] =
                        it.postal_code
                }
            }
            return addressQueryParameterMap
        }

        fun addAddressQueryMapWithWareHouse(userAddressData: LocalCacheModel?): MutableMap<String, String> {
            val addressQueryParameterMap = addAddressQueryMap(userAddressData)
            userAddressData?.let {
                if (it.warehouse_id.isNotEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_WAREHOUSE_ID] =
                        userAddressData.warehouse_id
                }
                if (!it.warehouses.isNullOrEmpty()) {
                    addressQueryParameterMap[Constant.ChooseAddressQueryParams.RPC_USER_WAREHOUSE_IDS] =
                        setUserWarehouseIds(userAddressData.warehouses)
                }
            }
            return addressQueryParameterMap
        }

        private fun setUserWarehouseIds(warehouses: List<LocalWarehouseModel>): String {
            val userWarehouseIds = mutableListOf<String>()
            warehouses.forEach { warehouseModel ->
                userWarehouseIds.add(warehouseModel.warehouse_id.toString() + "#" + warehouseModel.service_type)
            }
            return userWarehouseIds.joinToString(separator = ",")
        }

        fun addQueryParamMap(queryParameterMap: MutableMap<String, String?>): String {
            return queryParameterMap[QUERY_PARENT] ?: ""
        }

        fun isFutureSale(
            saleStartDate: String,
            timerFormat: String = TIMER_SPRINT_SALE_DATE_FORMAT
        ): Boolean {
            if (saleStartDate.isEmpty()) return false
            val currentSystemTime = Calendar.getInstance().time
            val parsedDate = parseData(saleStartDate, timerFormat)
            return if (parsedDate != null) {
                currentSystemTime.time < parsedDate.time
            } else {
                false
            }
        }


        /**
         * queryValue: activeTab=3&key=abc
         * key: activeTab
         * value: 4
         * Output: activeTab=4&key=abc
         */
        fun upsertQueryParam(
            queryValue: String,
            key: String,
            value: String
        ): String {
            return if (queryValue.isEmpty()) {
                "$key=$value"
            } else {
                try {
                    val map = UriUtil.stringQueryParamsToMap(queryValue)
                    map.remove(key)
                    map[key] = value
                    UriUtil.buildQueryParamToString(map)
                } catch (e: Exception) {
                    "$key=$value"
                }
            }
        }

        fun isFutureSaleOngoing(
            saleStartDate: String,
            saleEndDate: String,
            timerFormat: String = TIMER_SPRINT_SALE_DATE_FORMAT
        ): Boolean {
            if (saleStartDate.isEmpty() || saleEndDate.isEmpty()) return false
            val currentSystemTime = Calendar.getInstance().time
            val parsedSaleStartDate = parseData(saleStartDate, timerFormat)
            val parsedSaleEndDate = parseData(saleEndDate, timerFormat)
            return if (parsedSaleStartDate != null && parsedSaleEndDate != null) {
                (parsedSaleStartDate.time <= currentSystemTime.time) && (currentSystemTime.time < parsedSaleEndDate.time)
            } else {
                false
            }
        }

        fun isSaleOver(
            saleEndDate: String,
            timerFormat: String = TIMER_SPRINT_SALE_DATE_FORMAT
        ): Boolean {
            if (saleEndDate.isEmpty()) return true
            val currentSystemTime = Calendar.getInstance().time
            val parsedDate = parseData(saleEndDate, timerFormat)
            return if (parsedDate != null) {
                currentSystemTime.time >= parsedDate.time
            } else {
                false
            }
        }

        fun parseData(date: String?, timerFormat: String = TIMER_SPRINT_SALE_DATE_FORMAT): Date? {
            return date?.let {
                try {
                    SimpleDateFormat(timerFormat, Locale.getDefault())
                        .parse(date)
                } catch (parseException: ParseException) {
                    null
                }
            }
        }

        fun parseFlashSaleDate(saleTime: String?): String {
            if (!saleTime.isNullOrEmpty() && saleTime.length >= CONSTANT_19) {
                val date = saleTime.substring(CONSTANT_0, CONSTANT_10)
                val time = saleTime.substring(CONSTANT_11, CONSTANT_19)
                return "${date}T$time"
            }
            return ""
        }

        fun parsedColor(context: Context, fontColor: String, defaultColor: Int): Int {
            return try {
                Color.parseColor(fontColor)
            } catch (exception: Exception) {
                MethodChecker.getColor(context, defaultColor)
            }
        }

        @SuppressLint("ResourceType")
        fun getValidHexCode(context: Context, color: String?): String {
            if (color.isNullOrEmpty()) {
                return context.resources.getString(com.tokopedia.unifyprinciples.R.color.Unify_Background)
            }
            val regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3}|[A-Fa-f0-9]{8})$"
            val pattern: Pattern = Pattern.compile(regex)
            return if (pattern.matcher(color).matches()) {
                color
            } else {
                context.resources.getString(com.tokopedia.unifyprinciples.R.color.Unify_Background)
            }
        }

        fun setTimerBoxDynamicBackground(view: View, color: Int) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    view.background.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
                } else {
                    view.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                }
            } catch (exception: Exception) {
                view.setBackgroundColor(MethodChecker.getColor(view.context, color))
            }
        }

        fun getElapsedTime(endDate: String): Long {
            if (endDate.isNotEmpty()) {
                try {
                    TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE))
                    val currentSystemTime = Calendar.getInstance().time
                    SimpleDateFormat(TIMER_DATE_FORMAT, Locale.getDefault()).parse(endDate)?.let {
                        return it.time - currentSystemTime.time
                    }
                } catch (e: Exception) {
                    return DEFAULT_TIME_DATA
                }
            }
            return DEFAULT_TIME_DATA
        }

        fun getShareUrlQueryParamAppended(
            url: String?,
            queryParameterMap: Map<String, String?>?
        ): String {
            var isAllKeyNullOrEmpty = true
            val queryString = StringBuilder()
            val queryParent = queryParameterMap?.get(QUERY_PARENT)
            val queryParams = queryParent?.processQueryParent()
            queryParams?.let {
                isAllKeyNullOrEmpty = false
                queryString.appendParams(it.first, it.second)
            }
            queryParameterMap?.forEach { (key, value) ->
                if (!value.isNullOrEmpty() && !setOfKeysToNotSendToShare.contains(key)) {
                    isAllKeyNullOrEmpty = false
                    queryString.appendParams(key, value)
                }
            }

            if (url.isNullOrEmpty()) return ""

            return if (isAllKeyNullOrEmpty) {
                url
            } else {
                "$url?$queryString"
            }
        }

        private fun String?.processQueryParent(): Pair<String, String>? {
            if (!this.isNullOrEmpty()) {
                val queryParams = this.split('=', '&')
                var queryIndex = 0
                while (queryIndex < queryParams.size - 1) {
                    val paramKey = queryParams[queryIndex]
                    val paramValue = queryParams[queryIndex + 1]

                    if (paramKey == "q") {
                        return (Pair(paramKey, paramValue))
                    }
                    queryIndex = queryIndex + 2
                }
            }
            return null
        }

        private fun StringBuilder.appendParams(key: String, value: String) {
            if (this.isNotEmpty()) {
                this.append('&')
            }
            this.append(key).append('=').append(value)
        }

        fun getDisplayMetric(context: Context?): DisplayMetrics {
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics
        }

        fun nextPageAvailable(component: ComponentsItem, productPerPage: Int): Boolean {
            return component.nextPageKey?.isNotEmpty()
                ?: (
                    component.getComponentsItem()?.size.isMoreThanZero() &&
                        component.getComponentsItem()?.size?.rem(productPerPage) == 0
                    )
        }

        fun getUserId(context: Context?): String {
            return context?.let { UserSession(it).userId } ?: ""
        }

        fun extractFromHtml(couponName: String?): String? {
            return try {
                if (couponName?.isNotEmpty() == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(couponName, Html.FROM_HTML_MODE_LEGACY).toString()
                    } else {
                        Html.fromHtml(couponName).toString()
                    }
                } else {
                    couponName
                }
            } catch (e: Exception) {
                couponName
            }
        }

        fun corners(
            view: View,
            leftOffset: Int,
            topOffset: Int,
            rightOffset: Int,
            bottomOffset: Int,
            radius: Float
        ) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.outlineProvider = object : ViewOutlineProvider() {
                        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun getOutline(view: View?, outline: Outline?) {
                            outline?.setRoundRect(
                                leftOffset,
                                topOffset,
                                (view?.width).toZeroIfNull() + rightOffset,
                                (view?.height).toZeroIfNull() + bottomOffset,
                                radius
                            )
                        }
                    }
                    view.clipToOutline = true
                }
            } catch (e: Exception) {
            }
        }

        fun updateProductAddedInCart(
            products: List<ComponentsItem>,
            map: Map<MiniCartItemKey, MiniCartItem>?
        ) {
            if (map == null) return
            products.forEach { componentsItem ->
                componentsItem.data?.firstOrNull()?.let { dataItem ->
                    if (dataItem.hasATC && !dataItem.parentProductId.isNullOrEmpty() && map.containsKey(
                            MiniCartItemKey(
                                dataItem.parentProductId ?: "",
                                type = MiniCartItemType.PARENT
                            )
                        )
                    ) {
                        map.getMiniCartItemParentProduct(
                            dataItem.parentProductId ?: ""
                        )?.totalQuantity?.let { quantity ->
                            dataItem.quantity = quantity
                        }
                    } else if (dataItem.hasATC && !dataItem.productId.isNullOrEmpty() && map.containsKey(
                            MiniCartItemKey(dataItem.productId ?: "")
                        )
                    ) {
                        map.getMiniCartItemProduct(
                            dataItem.productId ?: ""
                        )?.quantity?.let { quantity ->
                            dataItem.quantity = quantity
                        }
                    }
                }
            }
        }

        fun getParentPosition(componentsItem: ComponentsItem): Int {
            var parentComponentPosition = componentsItem.parentComponentPosition
            if (componentsItem.parentComponentId.isNotEmpty()) {
                getComponent(
                    componentsItem.parentComponentId,
                    componentsItem.pageEndPoint
                )?.let { parentComp ->
                    parentComponentPosition = parentComp.position
                }
            }
            return parentComponentPosition
        }

        fun logException(t: Throwable) {
            if (!BuildConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().recordException(Exception(t))
            } else {
                t.printStackTrace()
            }
        }

        fun String.toEncodedString(): String {
            return try {
                URLEncoder.encode(this, DEFAULT_ENCODING)
            } catch (exception: Exception) {
                this
            }
        }

        fun String.toDecodedString(): String {
            return try {
                URLDecoder.decode(this, DEFAULT_ENCODING)
            } catch (exception: Exception) {
                this
            }
        }

        fun ComponentsItem.areFiltersApplied(): Boolean {
            return (selectedSort != null && selectedFilters != null) &&
                (
                    selectedSort?.isNotEmpty() == true ||
                        selectedFilters?.isNotEmpty() == true
                    )
        }

        fun generateRandomUUID(): String {
            return UUID.randomUUID().toString()
        }

        fun getTargetComponentOfFilter(components: ComponentsItem): ComponentsItem? {
            var compId = components.properties?.targetId ?: ""
            if (components.properties?.dynamic == true) {
                getComponent(
                    components.parentComponentId,
                    components.pageEndPoint
                )?.let parent@{ parentItem ->
                    parentItem.getComponentsItem()?.forEach {
                        if (!it.dynamicOriginalId.isNullOrEmpty()) {
                            if (it.dynamicOriginalId == compId) {
                                compId = it.id
                                return@parent
                            }
                        }
                    }
                }
            }
            return getComponent(compId, components.pageEndPoint)
        }

        fun isRPCFilterApplicableForTab(
            valueOfRpcFilter: String,
            components: ComponentsItem
        ): String {
            if (valueOfRpcFilter.contains("_")) {
                val splitValues = valueOfRpcFilter.split("_")
                if (splitValues.size < 2) return ""
                val tabPosition = splitValues[0].toIntOrNull()?.minus(1)
                val actualValue = splitValues[1]
                if (tabPosition == null || actualValue.isEmpty()) return ""
                if (tabPosition == components.tabPosition) {
                    return actualValue
                }
                return ""
            } else {
                return valueOfRpcFilter
            }
        }

        fun dpToPx(dp: Int): Float {
            return (dp * Resources.getSystem().displayMetrics.density)
        }

        fun setParameterMapUtil(
            queryParameterMap: String?,
            queryParameterMapWithRpc: MutableMap<String, String>,
            queryParameterMapWithoutRpc: MutableMap<String, String>
        ) {
            val queryMap =
                URLParser(ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY + "?" + queryParameterMap).paramKeyValueMapDecoded
            for ((key, value) in queryMap) {
                if (!value.isNullOrEmpty()) {
                    if (key.startsWith(RPC_FILTER_KEY)) {
                        val keyWithoutPrefix = key.removePrefix(RPC_FILTER_KEY)
                        queryParameterMapWithRpc[keyWithoutPrefix] = value
                    } else {
                        queryParameterMapWithoutRpc[key] = value
                    }
                }
            }
        }

        fun routingBasedOnMoveAction(moveAction: MoveAction, fragment: Fragment) {
            when (moveAction.type) {
                Constant.REDIRECTION -> {
                    if (!moveAction.value.isNullOrEmpty()) {
                        RouteManager.route(fragment.activity, moveAction.value)
                    }
                }

                Constant.NAVIGATION -> {
                    if (!moveAction.value.isNullOrEmpty()) {
                        (fragment as? DiscoveryFragment)?.redirectToOtherTab(moveAction.value)
                    }
                }
            }
        }

        fun setTabSelectedBasedOnDataItem(componentItem: ComponentsItem, isSelected: Boolean) {
            componentItem.apply {
                if (!data.isNullOrEmpty()) {
                    data?.get(0)?.let { tabData ->
                        tabData.isSelected = isSelected
                    }
                }
            }
        }

        fun navIcons(
            navIconBuilderFlag: IconBuilderFlag,
            onClick: () -> Unit,
            handleGlobalNavCartClick: () -> Unit,
            handleGlobalMenuCartClick: () -> Unit
        ): MutableMap<Int, IconBuilder> {
            val navIconMap = mutableMapOf<Int, IconBuilder>()
            navIconMap[1] = IconBuilder(navIconBuilderFlag).addIcon(
                iconId = IconList.ID_SHARE,
                disableRouteManager = true,
                onClick = onClick,
                disableDefaultGtmTracker = true
            ).addIcon(
                iconId = IconList.ID_CART,
                onClick = handleGlobalNavCartClick,
                disableDefaultGtmTracker = true
            )

            navIconMap[2] = IconBuilder(navIconBuilderFlag).addIcon(
                iconId = IconList.ID_SHARE,
                disableRouteManager = true,
                onClick = onClick,
                disableDefaultGtmTracker = true
            ).addIcon(
                iconId = IconList.ID_NAV_GLOBAL,
                onClick = handleGlobalMenuCartClick,
                disableDefaultGtmTracker = true
            )

            navIconMap[3] = IconBuilder(navIconBuilderFlag).addIcon(
                iconId = IconList.ID_SHARE,
                disableRouteManager = true,
                onClick = onClick,
                disableDefaultGtmTracker = true
            ).addIcon(
                iconId = IconList.ID_CART,
                onClick = handleGlobalNavCartClick,
                disableDefaultGtmTracker = true
            )
                .addIcon(
                    iconId = IconList.ID_NAV_GLOBAL,
                    onClick = handleGlobalMenuCartClick,
                    disableDefaultGtmTracker = true
                )

            navIconMap[12] = IconBuilder(navIconBuilderFlag).addIcon(
                iconId = IconList.ID_SHARE,
                disableRouteManager = true,
                onClick = onClick,
                disableDefaultGtmTracker = true
            )

            navIconMap[13] = IconBuilder(navIconBuilderFlag).addIcon(
                iconId = IconList.ID_SHARE,
                disableRouteManager = true,
                onClick = onClick,
                disableDefaultGtmTracker = true
            ).addIcon(
                iconId = IconList.ID_CART,
                onClick = handleGlobalNavCartClick,
                disableDefaultGtmTracker = true
            )

            navIconMap[23] = IconBuilder(navIconBuilderFlag).addIcon(
                iconId = IconList.ID_SHARE,
                disableRouteManager = true,
                onClick = onClick,
                disableDefaultGtmTracker = true
            ).addIcon(
                iconId = IconList.ID_NAV_GLOBAL,
                onClick = handleGlobalMenuCartClick,
                disableDefaultGtmTracker = true
            )

            navIconMap[123] = IconBuilder(navIconBuilderFlag).addIcon(
                iconId = IconList.ID_SHARE,
                disableRouteManager = true,
                onClick = onClick,
                disableDefaultGtmTracker = true
            )
            return navIconMap
        }

        internal fun Typography.verticalScrollAnimation(
            duration: Long,
            isReverse: Boolean,
            isFromHtml: Boolean
        ): ValueAnimator {
            val totalHeight = if (isFromHtml) {
                lineCount * lineHeight + (lineCount * lineHeight * 0.058).toInt()
            } else {
                lineCount * lineHeight
            }
            val animator = if (isReverse) {
                ValueAnimator.ofInt(totalHeight - lineHeight, 0)
            } else {
                ValueAnimator.ofInt(0, totalHeight - lineHeight)
            }
            val newDuration = duration / 5
            animator.startDelay = newDuration * 3
            animator.duration = newDuration * 2
            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                scrollTo(0, animatedValue)
            }
            return animator
        }

        internal fun ShapeableImageView.verticalScrollAnimation(
            duration: Long,
            isReverse: Boolean
        ): ValueAnimator {
            val animator = if (isReverse) {
                ValueAnimator.ofInt(height, 0)
            } else {
                ValueAnimator.ofInt(0, height)
            }
            val newDuration = duration / 5
            animator.startDelay = newDuration * 3
            animator.duration = newDuration * 2
            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                scrollTo(0, animatedValue)
            }
            return animator
        }

        internal fun View.flipImage(
            imageView: ShapeableImageView,
            imageTier: String
        ) {
            val animOut: Animation =
                AnimationUtils.loadAnimation(this.context, R.anim.bmsm_slide_up_out)
            val animIn: Animation =
                AnimationUtils.loadAnimation(this.context, R.anim.bmsm_slide_up_in)
            animOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    this@flipImage.invisible()
                    imageView.loadImage(imageTier)
                    animIn.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            this@flipImage.visible()
                        }

                        override fun onAnimationRepeat(animation: Animation?) {}
                        override fun onAnimationEnd(animation: Animation?) {}
                    })
                    this@flipImage.startAnimation(animIn)
                }
            })
            this.startAnimation(animOut)
        }

        fun Properties?.isOldProductCardType(): Boolean {
            val cardType = this?.getCardType() ?: Properties.CardType.OLD_VERSION
            return cardType == Properties.CardType.OLD_VERSION
        }

        fun Properties?.isReimagineProductCardInBackground(): Boolean {
            return this?.getCardType() == Properties.CardType.NEW_VERSION_WITH_BACKGROUND
        }
    }
}
