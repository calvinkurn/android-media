package com.tokopedia.di

import android.content.Context
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.di.ComponentFactory
import com.tokopedia.user.session.di.DaggerUserSessionComponent
import com.tokopedia.user.session.di.SessionModule
import com.tokopedia.user.session.di.UserSessionComponent
import io.mockk.every
import io.mockk.mockk

class FakeComponentFactory: ComponentFactory() {

    override fun createUserSessionComponent(context: Context): UserSessionComponent {
        return DaggerUserSessionComponent.builder().sessionModule(
            object : SessionModule() {
                override fun provideAbPlatform(context: Context): DataStorePreference {
                    return mockk {
                        every { isDataStoreEnabled() } returns true
                    }
                }
            }
        ).context(context).build()
    }

}