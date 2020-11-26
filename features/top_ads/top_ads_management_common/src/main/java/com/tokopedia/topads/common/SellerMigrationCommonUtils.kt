package com.tokopedia.topads.common

import android.os.Bundle
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import java.util.*

fun getSellerMigrationRedirectionApplinks(extras: Bundle?): ArrayList<String> {
    return extras?.run {
        ArrayList(getStringArrayList(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA).orEmpty())
    } ?: arrayListOf()
}

fun getSellerMigrationFeatureName(extras: Bundle?): String {
    return extras?.run {
        getString(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).orEmpty()
    }.orEmpty()
}

fun isFromPdpSellerMigration(extras: Bundle?): Boolean {
    return extras?.run {
        !getString(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).isNullOrEmpty()
    } ?: false
}

fun getPdpAppLink(extras: Bundle?): String {
    return extras?.run {
        getStringArrayList(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)?.firstOrNull().orEmpty()
    }.orEmpty()
}