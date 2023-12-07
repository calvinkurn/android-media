package com.tokopedia.creation.common.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created By : Jonathan Darwin on November 16, 2023
 */
object ContentCreationInjector {

    fun get(context: Context): ContentCreationComponent {
        return DaggerContentCreationComponent.factory().create(
            baseAppComponent = (context.applicationContext as BaseMainApplication).baseAppComponent,
            context = context
        )
    }
}
