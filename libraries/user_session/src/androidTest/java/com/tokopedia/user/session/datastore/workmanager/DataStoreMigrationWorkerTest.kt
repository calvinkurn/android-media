package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.tokopedia.di.FakeComponentFactory
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker.Companion.OPERATION_KEY
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker.Companion.WorkOps.MIGRATED
import com.tokopedia.user.session.di.ComponentFactory
import com.tokopedia.utils.SampleUserModel
import com.tokopedia.utils.getSampleUser
import com.tokopedia.utils.setSample
import kotlinx.coroutines.flow.first
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
        val sample = SampleUserModel(
            true, "fakeId", "Foo Name", "fooToken", "barToken"
        )

        with(UserSession(context)) {
            setSample(sample)
        }

        val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
        runBlocking {
            val result = worker.doWork()

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))
            assertThat(dataStore.getSampleUser(), equalTo(sample))
        }
    }
}