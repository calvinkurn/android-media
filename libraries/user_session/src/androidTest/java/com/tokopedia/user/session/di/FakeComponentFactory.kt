package com.tokopedia.user.session.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.di.ComponentFactory
import com.tokopedia.user.session.di.DaggerUserSessionComponent
import com.tokopedia.user.session.di.SessionModule
import com.tokopedia.user.session.di.UserSessionComponent
import io.mockk.every
import io.mockk.spyk

class FakeComponentFactory: ComponentFactory() {

    val spykedPreference: DataStorePreference = spyk(DataStorePreference(ApplicationProvider.getApplicationContext())) {
        every { isDataStoreEnabled() } returns true
    }

    override fun createUserSessionComponent(context: Context): UserSessionComponent {
        return DaggerUserSessionComponent.builder().sessionModule(
            object : SessionModule() {
                override fun provideAbPlatform(context: Context): DataStorePreference {
                    return spykedPreference
                }
            }
        ).context(context).build()
    }

}
