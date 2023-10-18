package com.tokopedia.people.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
object UserProfileInjector {

    private var customComponent: UserProfileComponent? = null

    fun get(context: Context): UserProfileComponent = synchronized(this) {
        return customComponent ?: DaggerUserProfileComponent.builder()
            .baseAppComponent(
                (context.applicationContext as BaseMainApplication).baseAppComponent,
            )
            .userProfileModule(UserProfileModule(context))
            .build()
    }

    fun set(component: UserProfileComponent) = synchronized(this) {
        customComponent = component
    }
}
