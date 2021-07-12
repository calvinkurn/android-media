package com.tokopedia.internal_review.common

import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

/**
 * Created By @ilhamsuaib on 05/02/21
 */

class ReviewRemoteConfig constructor(
        private val remoteConfig: FirebaseRemoteConfigImpl
) {

    fun isReviewEnabled(): Boolean {
        if (GlobalConfig.isSellerApp()) {
            return remoteConfig.getBoolean(Const.RemoteConfigKey.SELLERAPP_IN_APP_REVIEW, false)
        } else {
            return remoteConfig.getBoolean(Const.RemoteConfigKey.CUSTOMERAPP_INTERNAL_REVIEW, false)
        }
    }
}