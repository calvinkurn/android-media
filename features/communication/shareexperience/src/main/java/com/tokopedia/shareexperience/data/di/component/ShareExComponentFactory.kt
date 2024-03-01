package com.tokopedia.shareexperience.data.di.component

import android.app.Application

interface ShareExComponentFactory {

    fun createShareExComponent(application: Application): ShareExComponent
}
