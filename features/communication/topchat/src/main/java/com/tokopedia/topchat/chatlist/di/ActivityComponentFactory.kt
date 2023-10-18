package com.tokopedia.topchat.chatlist.di

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication

open class ActivityComponentFactory {

    open fun createChatListComponent(
        application: Application,
        context: Context
    ): ChatListComponent {
        return DaggerChatListComponent.builder()
            .baseComponent((application as BaseMainApplication).baseAppComponent)
            .context(context)
            .build()
    }

    companion object {
        private var sInstance: ActivityComponentFactory? = null

        @VisibleForTesting
        var instance: ActivityComponentFactory
            get() {
                if (sInstance == null) sInstance = ActivityComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
