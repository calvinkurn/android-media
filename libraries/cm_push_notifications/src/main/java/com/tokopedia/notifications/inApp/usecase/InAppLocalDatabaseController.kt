package com.tokopedia.notifications.inApp.usecase

import android.app.Application
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val saveInAppUseCase: SaveInAppUseCase
            by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                SaveInAppUseCase(application, repositoryManager)
            }

    fun getInAppData(screenName: String, isActivity: Boolean,
                     inAppFetchListener: InAppFetchListener) {
        launchCatchError(block = {
            inAppFetchListener.onInAPPListFetchCompleted(
                    getInAppListUseCase.getInAPPListByScreenName(screenName, isActivity)
            )
        }, onError = {
            inAppFetchListener.onInAPPListFetchCompleted(null)
            //todo Timber Logging by lalit
        })
    }

    fun clearExpiredInApp() {
        launchCatchError(block = {
            deleteExpireInAppUseCase.deleteAllCompletedAndExpireInAPP()
        }, onError = {
            //todo Timber Logging by lalit
        })
    }

    fun saveInApp(cmInApp: CMInApp, inAppSavedListener: InAppSavedListener) {
        launchCatchError(block = {
            val isSaved = saveInAppUseCase.saveInApp(cmInApp)
            if (isSaved) {
                inAppSavedListener.onInAppSaved(cmInApp.isAmplification)
            }
        }, onError = {
            //todo Timber Logging by lalit
        })
    }

    companion object {
        @Volatile
        private var INSTANCE: InAppLocalDatabaseController? = null

        fun getInstance(application: Application,
                        repositoryManager: RepositoryManager): InAppLocalDatabaseController =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: InAppLocalDatabaseController(application, repositoryManager).also { INSTANCE = it }
                }
    }

}


interface InAppFetchListener {
    fun onInAPPListFetchCompleted(inAppList: List<CMInApp>?)
}

interface InAppSavedListener {
    fun onInAppSaved(isAmplification: Boolean)
}