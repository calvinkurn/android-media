package com.tokopedia.browse.categoryNavigation

import android.content.Context
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryConfigUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_CATEGORY_BROWSE_ENABLE_AB
import rx.Subscriber

object CategoryNavigationConfig {

    var isNewCategoryEnabled: Boolean = false

    fun updateCategoryConfig(context: Context) {

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        if (!remoteConfig.getBoolean(APP_CATEGORY_BROWSE_ENABLE_AB, true)) {
            isNewCategoryEnabled = true
            return
        }

        GetCategoryConfigUseCase(context).execute(object : Subscriber<Boolean>() {
            override fun onNext(isRevampBelanja: Boolean) {
                isNewCategoryEnabled = isRevampBelanja
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                isNewCategoryEnabled = true
            }
        })


    }
}

