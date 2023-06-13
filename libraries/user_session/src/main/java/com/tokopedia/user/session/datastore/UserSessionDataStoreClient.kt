package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.tokopedia.encryption.security.AeadEncryptorImpl
import kotlinx.coroutines.*

object UserSessionDataStoreClient {

    private var userSessionDataStore: UserSessionDataStore? = null
    private var scope: CoroutineScope? = null
    private const val DATA_STORE_FILE_NAME = "user_session.pb"
    private const val shortDelay = 100L

    @JvmStatic
    fun getInstance(context: Context): UserSessionDataStore {
        if (userSessionDataStore != null) {
            return userSessionDataStore!!
        }
        userSessionDataStore = initialize(context)
        return userSessionDataStore!!
    }

    /**
     * This function is temporarily develop to solve Google Tink's General Security Exception
     * Supposed to be removed once the issue is completely resolved
     * */
    suspend fun reCreate(context: Context, deleteFile: Boolean = true) {
        scope?.cancel()
        // This delay is required, otherwise we have IlleagalStateException's multiple datastore active
        delay(shortDelay)
        if (deleteFile) context.dataStoreFile(DATA_STORE_FILE_NAME).delete()
        userSessionDataStore = initialize(context)
    }

    private fun initialize(context: Context): UserSessionDataStore {
        val aead = AeadEncryptorImpl(context)
        with(CoroutineScope(Dispatchers.IO + SupervisorJob())) {
            scope = this
            val store = DataStoreFactory.create(
                UserSessionSerializer(aead),
                produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) },
                scope = this
            )
            return UserSessionDataStoreImpl(store)
        }
    }
}
