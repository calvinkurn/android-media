package com.tokopedia.seller_migration_common

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants


fun isSellerMigrationEnabled(context: Context?): Boolean {
    return if (context == null) {
        false
    } else {
        FirebaseRemoteConfigImpl(context).getBoolean(SellerMigrationConstants.APP_ENABLE_SELLER_MIGRATION, true)
    }
}