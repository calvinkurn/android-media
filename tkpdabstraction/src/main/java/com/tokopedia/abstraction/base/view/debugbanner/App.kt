package com.tokopedia.abstraction.base.view.debugbanner

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        DebugBanner.init(application = this)
    }
}
