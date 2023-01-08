package com.tokopedia.applink.digitaldeals

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

object DeeplinkMapperDeals {

    private const val DEALS = "deals"
    private const val DEALS_PRODUCT_DETAIL = "deals/i"
    private const val DEALS_BRAND_DETAIL = "deals/b"
    private const val SECOND_PATH = 2

    fun getRegisteredNavigationFromHttpDeals(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val path = uri.pathSegments.joinToString("/")
        return when {
            path == DEALS -> {
                ApplinkConstInternalDeals.DEALS_HOMEPAGE
            }
            path.startsWith(DEALS_PRODUCT_DETAIL) -> {
                "${ApplinkConstInternalDeals.DEALS_PRODUCT_DETAIL_PAGE}?${uri.lastPathSegment}"
            }
            path.startsWith(DEALS_BRAND_DETAIL) -> {
                "${ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE}?${uri.lastPathSegment}"
            }
            else -> ""
        }
    }

    fun getRegisteredNavigationDeals(context: Context, deeplink: String) : String {
        val uri = Uri.parse(deeplink)
        return when {
            // tokopedia://deals
            uri.pathSegments.size == Int.ZERO ->
                ApplinkConstInternalDeals.DEALS_HOMEPAGE

            //tokopedia://deals/order
            uri.pathSegments.size == Int.ONE && uri.pathSegments[Int.ZERO] == "order"->
                ""
            // tokopedia://deals/{id}
            uri.pathSegments.size == Int.ONE ->
                "${ApplinkConstInternalDeals.DEALS_PRODUCT_DETAIL_PAGE}?${uri.lastPathSegment}"

            //tokopedia://deals/brand/{slug}
            uri.pathSegments.size == SECOND_PATH && uri.pathSegments[Int.ZERO] == "brand" ->
                    "${ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE}?${uri.pathSegments[1]}"

            //tokopedia://deals/i/{slug}
            uri.pathSegments.size == SECOND_PATH && uri.pathSegments[Int.ZERO] == "i" ->
                "${ApplinkConstInternalDeals.DEALS_PRODUCT_DETAIL_PAGE}?${uri.lastPathSegment}"

            //tokopedia://deals/allbrands/{isVoucher}
            uri.pathSegments.size == SECOND_PATH && uri.pathSegments[Int.ZERO] == "allbrands"->
                    "${ApplinkConstInternalDeals.DEALS_BRAND_PAGE}?${uri.query}"

            //tokopedia://deals/category/page
            uri.pathSegments.size == SECOND_PATH ->
                "${ApplinkConstInternalDeals.DEALS_CATEGORY_PAGE}?${uri.query}"
            else -> ""
        }

    }
}
