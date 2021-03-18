package com.tokopedia.internal_review.common

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

/**
 * Created By @ilhamsuaib on 05/02/21
 */

class SellerAppReviewRemoteConfig constructor(
        private val remoteConfig: FirebaseRemoteConfigImpl
) {

    fun isSellerReviewEnabled(): Boolean {
        return remoteConfig.getBoolean(Const.RemoteConfigKey.SELLERAPP_IN_APP_REVIEW, false)
    }
}