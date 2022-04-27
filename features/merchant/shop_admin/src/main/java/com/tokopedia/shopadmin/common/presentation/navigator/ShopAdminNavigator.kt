package com.tokopedia.shopadmin.common.presentation.navigator

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants

internal fun Activity.goToPlayStoreOrSellerApp() {
    try {
        val intent = packageManager?.getLaunchIntentForPackage(SellerMigrationConstants.PACKAGE_SELLER_APP)
        if (intent != null) {
            intent.putExtra(SellerMigrationConstants.SELLER_MIGRATION_KEY_AUTO_LOGIN, true)
            startActivity(intent)
        } else {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(SellerMigrationConstants.APPLINK_PLAYSTORE + SellerMigrationConstants.PACKAGE_SELLER_APP)
                )
            )
        }
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(SellerMigrationConstants.URL_PLAYSTORE + SellerMigrationConstants.PACKAGE_SELLER_APP)
            )
        )
    }
}