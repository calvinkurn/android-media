package com.tokopedia.creation.common.upload.di.uploader

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created By : Jonathan Darwin on October 12, 2023
 */
object CreationUploaderComponentProvider {

    private var creationUploaderComponent: CreationUploaderComponent? = null

    fun get(context: Context): CreationUploaderComponent = synchronized(this) {
        if (creationUploaderComponent == null) {
            creationUploaderComponent = DaggerCreationUploaderComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
        }

        return creationUploaderComponent!!
    }
}
