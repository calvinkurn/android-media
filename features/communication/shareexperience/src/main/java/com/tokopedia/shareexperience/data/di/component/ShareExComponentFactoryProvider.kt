package com.tokopedia.shareexperience.data.di.component

import androidx.annotation.VisibleForTesting

object ShareExComponentFactoryProvider {

    private var sInstance: ShareExComponentFactory? = null

    @VisibleForTesting
    var instance: ShareExComponentFactory
        get() {
            if (sInstance == null) sInstance = ShareExComponentFactoryImpl()
            return sInstance!!
        }
        set(instance) {
            sInstance = instance
        }
}
