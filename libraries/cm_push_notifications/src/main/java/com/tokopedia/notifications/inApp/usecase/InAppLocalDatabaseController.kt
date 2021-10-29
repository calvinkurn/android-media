package com.tokopedia.notifications.inApp.usecase

import android.app.Application
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.HashMap
import kotlin.coroutines.CoroutineContext

class InAppLocalDatabaseController private constructor(private val application: Application,
                                                       private val repositoryManager: RepositoryManager) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val getInAppListUseCase: GetInAppListUseCase
            by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                GetInAppListUseCase(repositoryManager)
            }
    private val deleteExpireInAppUseCase: DeleteExpireInAppUseCase
            by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                DeleteExpireInAppUseCase(application, repositoryManager)
            }
    private val saveNewInAppUseCase: SaveNewInAppUseCase
            by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                SaveNewInAppUseCase(application, repositoryManager)
            }

    fun getInAppData(screenName: String, isActivity: Boolean,
                     inAppFetchListener: InAppFetchListener) {
        launchCatchError(block = {
            inAppFetchListener.onInAppListFetchCompleted(
                    getInAppListUseCase.getInAppListByScreenName(screenName, isActivity)
            )
        }, onError = {
            inAppFetchListener.onInAppListFetchCompleted(null)
            logThrowable(it, FETCH_ERROR_STR)
        })
    }

    fun clearExpiredInApp() {
        launchCatchError(block = {
            deleteExpireInAppUseCase.deleteAllCompletedAndExpireInAPP()
        }, onError = {
            logThrowable(it, CLEAR_ERROR_STR)
        })
    }

    private fun logThrowable(throwable: Throwable, dataMessage: String){
        val messageMap: MutableMap<String, String> = HashMap()
        messageMap[SERVER_KEY_TYPE] = SERVER_LOG_EXCEPTION
        messageMap[SERVER_KEY_ERROR] = Log.getStackTraceString(throwable).substring(0,
                Log.getStackTraceString(throwable).length.coerceAtMost(CMConstant.TimberTags.MAX_LIMIT)
        )
        messageMap[SERVER_KEY_DATA] = dataMessage
        ServerLogger.log(Priority.P2, SERVER_LOG_STR, messageMap)
    }

    fun saveNewInApp(cmInApp: CMInApp, newInAppSavedListener: NewInAppSavedListener) {
        launchCatchError(block = {
            val isSaved = saveNewInAppUseCase.saveInApp(cmInApp)
            if (isSaved) {
                newInAppSavedListener.onNewInAppSaved()
            }
        }, onError = {
            //todo Timber Logging by lalit
        })
    }

    companion object {
        @Volatile
        private var INSTANCE: InAppLocalDatabaseController? = null

        private const val SERVER_LOG_STR = "CM_VALIDATION"
        private const val FETCH_ERROR_STR = "CM INApp Fetch V2 Error"
        private const val CLEAR_ERROR_STR = "CM InApp Clear V2 Error"
        private const val SERVER_KEY_TYPE = "type"
        private const val SERVER_KEY_ERROR = "err"
        private const val SERVER_KEY_DATA = "data"
        private const val SERVER_LOG_EXCEPTION = "exception"

        fun getInstance(application: Application,
                        repositoryManager: RepositoryManager): InAppLocalDatabaseController =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: InAppLocalDatabaseController(application, repositoryManager).also { INSTANCE = it }
                }
    }

}


interface InAppFetchListener {
    fun onInAppListFetchCompleted(inAppList: List<CMInApp>?)
}

interface NewInAppSavedListener {
    fun onNewInAppSaved()
}