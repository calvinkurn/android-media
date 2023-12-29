package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.tokopedia.encryption.security.AeadEncryptorImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionMap
import com.tokopedia.user.session.datastore.DataStoreMigrationHelper
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import com.tokopedia.user.session.datastore.workmanager.WorkOps.MIGRATED
import com.tokopedia.user.session.datastore.workmanager.WorkOps.NO_OPS
import com.tokopedia.user.session.datastore.workmanager.WorkOps.OPERATION_KEY
import com.tokopedia.user.session.di.ComponentFactory
import com.tokopedia.user.session.di.FakeComponentFactory
import com.tokopedia.utils.UserSessionModel
import com.tokopedia.utils.getUserModel
import com.tokopedia.utils.setModel
import io.mockk.every
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.empty
import org.junit.Assert.assertEquals
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
            val sample = UserSessionModel()
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            userSession.setModel(sample)
            every { spykedPref.isDataStoreEnabled() } returns true

            val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
            val result = worker.doWork()

            val dataStore = UserSessionDataStoreClient.getInstance(context)

            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))
            assertThat(dataStore.getUserModel(), equalTo(userSession.getUserModel()))
            assertThat(DataStoreMigrationHelper.checkDataSync(dataStore, userSession), `is`(empty()))
        }
    }

    @Test
    fun whenShopIdIsSetFromUserSession_DataStoreShopIdShouldBeSet() {
        runBlocking {
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            every { spykedPref.isDataStoreEnabled() } returns true
            val dataStore = UserSessionDataStoreClient.getInstance(context)

            assertEquals(userSession.shopId, dataStore.getShopId().first())

            userSession.shopId = "923081"
            UserSessionMap.map.clear()
            assertEquals(userSession.shopId, dataStore.getShopId().first())

            userSession.shopId = ""
            UserSessionMap.map.clear()
            assertEquals(userSession.shopId, dataStore.getShopId().first())

            userSession.shopId = "0"
            UserSessionMap.map.clear()
            assertEquals(userSession.shopId, dataStore.getShopId().first())

            userSession.shopId = "-1"
            UserSessionMap.map.clear()
            assertEquals(userSession.shopId, dataStore.getShopId().first())

            userSession.shopId = null
            UserSessionMap.map.clear()
            assertEquals(userSession.shopId, dataStore.getShopId().first())
        }
    }

    /**
     * solve issue difference on token type after user migration on datastore
     */
    @Test
    fun whenTokenTypeIsSetFromUserSession_DataStoreShopIdShouldBeSet() {
        runBlocking {
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            every { spykedPref.isDataStoreEnabled() } returns true
            val dataStore = UserSessionDataStoreClient.getInstance(context)

            assertEquals(userSession.tokenType, dataStore.getTokenType().first())

            userSession.setToken("abc", "xyz")
            UserSessionMap.map.clear()
            assertEquals(userSession.tokenType, dataStore.getTokenType().first())

            userSession.setToken("", "")
            UserSessionMap.map.clear()
            assertEquals(userSession.tokenType, dataStore.getTokenType().first())

            userSession.setToken("0", "0")
            UserSessionMap.map.clear()
            assertEquals(userSession.tokenType, dataStore.getTokenType().first())

            userSession.setToken("-1", "-1")
            UserSessionMap.map.clear()
            assertEquals(userSession.tokenType, dataStore.getTokenType().first())

            userSession.setToken(null, null)
            UserSessionMap.map.clear()
            assertEquals(userSession.tokenType, dataStore.getTokenType().first())
        }
    }

    @Test
    fun whenIsMultiLocationShopIsSetFromUserSession_DataStoreShouldBeSet() {
        runBlocking {
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            every { spykedPref.isDataStoreEnabled() } returns true
            val dataStore = UserSessionDataStoreClient.getInstance(context)

            assertEquals(userSession.tokenType, dataStore.getTokenType().first())

            userSession.setIsMultiLocationShop(true)
            UserSessionMap.map.clear()
            assertEquals(userSession.isMultiLocationShop, dataStore.isMultiLocationShop().first())

            userSession.setIsMultiLocationShop(false)
            UserSessionMap.map.clear()
            assertEquals(userSession.isMultiLocationShop, dataStore.isMultiLocationShop().first())
        }
    }

    @Test
    fun whenLoginMethodIsSetFromUserSession_DataStoreLoginMethodShouldBeSet() {
        runBlocking {
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            every { spykedPref.isDataStoreEnabled() } returns true
            val dataStore = UserSessionDataStoreClient.getInstance(context)

            assertEquals(userSession.loginMethod, dataStore.getLoginMethod().first())

            userSession.loginMethod = UserSession.LOGIN_METHOD_GOOGLE
            UserSessionMap.map.clear()
            assertEquals(userSession.loginMethod, dataStore.getLoginMethod().first())

            userSession.loginMethod = ""
            UserSessionMap.map.clear()
            assertEquals(userSession.loginMethod, dataStore.getLoginMethod().first())
        }
    }

    @Test
    fun when_logout_session_after_migration_data_remains_synced() {
        runBlocking {
            val sample = UserSessionModel()
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            userSession.setModel(sample)
            every { spykedPref.isDataStoreEnabled() } returns true

            val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
            val result = worker.doWork()
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            userSession.logoutSession()
            dataStore.logoutSession()

            assertThat(DataStoreMigrationHelper.checkDataSync(dataStore, userSession), `is`(empty()))
            assertThat(dataStore.getUserModel(), equalTo(userSession.getUserModel()))
        }
    }

    @Test
    fun when_cleared_token_after_migration_data_remains_synced() {
        runBlocking {
            val sample = UserSessionModel()
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            userSession.setModel(sample)
            every { spykedPref.isDataStoreEnabled() } returns true

            val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
            val result = worker.doWork()
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            userSession.clearToken()
            dataStore.clearToken()

            assertThat(DataStoreMigrationHelper.checkDataSync(dataStore, userSession), `is`(empty()))
            assertThat(dataStore.getUserModel(), equalTo(userSession.getUserModel()))
        }
    }

    @Test
    fun when_run_twice_Then_sync_is_passed() {
        val sample = UserSessionModel()
        val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
        userSession.setModel(sample)
        every { spykedPref.isDataStoreEnabled() } returns true

        val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
        runBlocking {
            val result = worker.doWork()

            val secondResult = worker.doWork()

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))
            assertThat(secondResult, `is`(Result.success(workDataOf(OPERATION_KEY to NO_OPS))))
            assertThat(dataStore.getUserModel(), equalTo(userSession.getUserModel()))
        }
    }

    @Test
    fun when_usersession_is_set_after_migration_Then_datastore_is_updated_and_next_migration_is_noop() {
        val sample = UserSessionModel(isShopOwner = true)
        val userSession =
            UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead()).apply {
                setModel(sample)
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
            assertThat(userSession.name, equalTo(newName))

            val secondResult = worker.doWork()
            assertThat(secondResult, `is`(Result.success(workDataOf(OPERATION_KEY to NO_OPS))))
        }
    }

    @Test
    fun when_cleared_data_logout_migration_data_remains_synced() {
        runBlocking {
            val sample = UserSessionModel()
            val userSession = UserSession(context, spykedPref, AeadEncryptorImpl(context).getAead())
            userSession.setModel(sample)
            every { spykedPref.isDataStoreEnabled() } returns true

            val worker = TestListenableWorkerBuilder<DataStoreMigrationWorker>(context).build()
            val result = worker.doWork()
            assertThat(result, `is`(Result.success(workDataOf(OPERATION_KEY to MIGRATED))))

            val dataStore = UserSessionDataStoreClient.getInstance(context)
            userSession.logoutUserSession()
            dataStore.logoutSession()

            assertThat(DataStoreMigrationHelper.checkDataSync(dataStore, userSession), `is`(empty()))
            assertThat(dataStore.getUserModel(), equalTo(userSession.getUserModel()))
        }
    }

}
