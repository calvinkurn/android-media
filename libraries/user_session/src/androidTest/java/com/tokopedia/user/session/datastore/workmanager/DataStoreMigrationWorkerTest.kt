package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.tokopedia.di.FakeComponentFactory
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import com.tokopedia.user.session.di.ComponentFactory
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreMigrationWorkerTest {

    lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        ComponentFactory.instance = FakeComponentFactory()
    }

    @Test
    fun workmanager_Test() {
        with(UserSession(context)) {
            setIsLogin(true)
            userId = "thisIsFakeId"
            setToken("randomToken", "Bearer")
            setRefreshToken("refreshToken")
            name = "Fake Name"
        }

        val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
        runBlocking {
            val result = worker.doWork()

            assertThat(result, `is`(ListenableWorker.Result.success()))

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            assertThat(dataStore.getName(), equalTo("Fake Name"))
        }
    }
}