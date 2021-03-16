package com.tokopedia.sellerreview.common

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 05/02/21
 */

@SellerHomeScope
class SellerAppReviewRemoteConfig @Inject constructor(
        private val remoteConfig: FirebaseRemoteConfigImpl
) {

    fun isSellerReviewEnabled(): Boolean {
        return remoteConfig.getBoolean(Const.RemoteConfigKey.SELLERAPP_IN_APP_REVIEW, false)
    }
}