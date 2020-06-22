package com.tokopedia.seller_migration_common

import android.content.Context
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants


fun isSellerMigrationEnabled(context: Context?): Boolean {
    return if (context == null) {
        false
    } else {
        (!GlobalConfig.isSellerApp() && FirebaseRemoteConfigImpl(context).getBoolean(SellerMigrationConstants.APP_ENABLE_SELLER_MIGRATION, true))
    }
}

fun getSellerMigrationDate(context: Context?): String {
    return if(context == null) {
        ""
    } else {
        FirebaseRemoteConfigImpl(context).getString(SellerMigrationConstants.APP_DATE_SELLER_MIGRATION, "")
    }
}