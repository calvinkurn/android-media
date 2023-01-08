package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.tokopedia.user.session.di.FakeComponentFactory
import com.tokopedia.encryption.security.AeadEncryptorImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import com.tokopedia.user.session.datastore.workmanager.WorkOps.MIGRATED
import com.tokopedia.user.session.datastore.workmanager.WorkOps.NO_OPS
import com.tokopedia.user.session.datastore.workmanager.WorkOps.OPERATION_KEY
import com.tokopedia.user.session.di.ComponentFactory
import com.tokopedia.utils.SampleUserModel
import com.tokopedia.utils.getSampleUser
import com.tokopedia.utils.setSample
import io.mockk.every
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
    lateinit var spykedPref: DataStorePreference

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        val factory = FakeComponentFactory()
        ComponentFactory.instance = factory
        spykedPref = factory.spykedPreference
        every { spykedPref.isDataStoreEnabled() } returns false
    }

    @Test
    fun basic_migration_test() {
        runBlocking {
            val sample = SampleUserModel(
                true, "fakeId", "Foo Name", "fooToken", "barToken"
            )
            with(UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())) {
                setSample(sample)
            }
            every { spykedPref.isDataStoreEnabled() } returns true

            val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
            val result = worker.doWork()

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))
            assertThat(dataStore.getSampleUser(), equalTo(sample))
        }
    }

    @Test
    fun when_run_twice_Then_sync_is_passed() {
        val sample = SampleUserModel(
            true, "fakeId", "Foo Name", "fooToken", "barToken"
        )
        with(UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())) {
            setSample(sample)
        }
        every { spykedPref.isDataStoreEnabled() } returns true

        val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
        runBlocking {
            val result = worker.doWork()

            val secondResult = worker.doWork()

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))
            assertThat(secondResult, `is`(Result.success(workDataOf(OPERATION_KEY to NO_OPS))))
            assertThat(dataStore.getSampleUser(), equalTo(sample))
        }
    }

    @Test
    fun when_usersession_is_set_after_migration_Then_datastore_is_updated_and_next_migration_is_noop() {
        val sample = SampleUserModel(
            true, "fakeId", "Foo Name", "fooToken", "barToken", true
        )
        val userSession =
            UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead()).apply {
                setSample(sample)
            }
        every { spykedPref.isDataStoreEnabled() } returns true

        val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
        runBlocking {
            val result = worker.doWork()
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))

            val newName = "New Name"
            userSession.name = newName
            val dataStore = UserSessionDataStoreClient.getInstance(context)
            assertThat(dataStore.getName().first(), equalTo(newName))

            val secondResult = worker.doWork()
            assertThat(secondResult, `is`(Result.success(workDataOf(OPERATION_KEY to NO_OPS))))
        }
    }

}
