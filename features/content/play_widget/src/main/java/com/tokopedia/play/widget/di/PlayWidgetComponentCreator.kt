package com.tokopedia.play.widget.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by kenny.hadisaputra on 02/06/22
 */
internal object PlayWidgetComponentCreator {

    private var INSTANCE: PlayWidgetComponent? = null

    fun getOrCreate(context: Context): PlayWidgetComponent = synchronized(this) {
        if (INSTANCE == null) {
            INSTANCE = DaggerPlayWidgetComponent.builder()
                .baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
                ).build()
        }
        return INSTANCE!!
    }
}