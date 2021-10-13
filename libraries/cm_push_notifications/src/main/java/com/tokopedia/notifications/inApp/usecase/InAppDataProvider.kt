package com.tokopedia.notifications.inApp.usecase

import android.app.Application
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class InAppDataProvider private constructor(private val application: Application,
                        private val repositoryManager: RepositoryManager) : CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val getInAppListUseCase : GetInAppListUseCase by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
        GetInAppListUseCase(application, repositoryManager)
    }

    fun getInAppData(screenName : String, isActivity : Boolean,
                     inAppFetchListener: InAppFetchListener){
        launchCatchError(block = {
            inAppFetchListener.onInAPPListFetchCompleted(
                    getInAppListUseCase.getInAPPListByScreenName(screenName, isActivity)
            )
        },onError ={
            inAppFetchListener.onInAPPListFetchCompleted(null)
            //todo Timber Logging by lalit
        })
    }


    companion object{
        @Volatile private var INSTANCE: InAppDataProvider? = null

        fun getInstance(application: Application,
                        repositoryManager: RepositoryManager): InAppDataProvider =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: InAppDataProvider(application, repositoryManager).also { INSTANCE = it }
                }
    }

}


interface InAppFetchListener {
    fun onInAPPListFetchCompleted(inAppList : List<CMInApp>?)
}