package com.tokopedia.notifications.inApp.ruleEngine.rulesinterpreter

import com.tokopedia.notifications.inApp.ruleEngine.RulesUtil
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import rx.Subscriber

class InAppDataProviderSubscriber(
        private val elapsedTimeObj: ElapsedTime?,
        private val dataProvider: DataProvider?) : Subscriber<List<CMInApp>>() {

    private var inAppList: MutableList<CMInApp>? = null

    override fun onNext(inAppDataList: List<CMInApp>?) {

        if (inAppDataList != null) {
            inAppList = inAppDataList.toMutableList()
            inAppList?.let {
                val iter = it.iterator()
                while (iter.hasNext()) {
                    val inAppData = iter.next()
                    if (!(checkIfActiveInTimeFrame(inAppData, System.currentTimeMillis()) &&
                                    checkIfFrequencyIsValid(inAppData, System.currentTimeMillis()))) {
                        iter.remove()
                        if (performDeletion(inAppData)) {
                            RepositoryManager.getInstance()
                                    .storageProvider
                                    .deleteRecord(inAppData.id)
                                    .subscribe()
                        }
                    }
                }
            }

        }
        dataProvider?.notificationsDataResult(inAppList,0,"")
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        dataProvider?.notificationsDataResult(null,0,"")
    }

    private fun checkIfActiveInTimeFrame(inAppData: CMInApp, currentTime: Long): Boolean {
        return if (checkIfTimeNotAssigned(inAppData)) {
            true
        } else {
            //check how to get current time stamp
            checkForTimeIntervalValidity(inAppData, currentTime)
        }
    }

    private fun checkForTimeIntervalValidity(inAppData: CMInApp, currentTime: Long): Boolean {
        return RulesUtil.isValidTimeFrame(inAppData.startTime,
                inAppData.endTime, currentTime, elapsedTimeObj)
    }


    private fun checkIfTimeNotAssigned(inAppData: CMInApp): Boolean {
        return inAppData.startTime == 0L && inAppData.endTime == 0L
    }

    private fun checkIfFrequencyIsValid(inAppData: CMInApp, currentTime: Long): Boolean {
        return if (inAppData.freq == RulesUtil.Constants.DEFAULT_FREQ && !inAppData.isShown) {
            true
        } else if (inAppData.freq > 0 && !inAppData.isShown) {
            true
        } else {
            checkIfInvaildFreq(inAppData, currentTime)
        }
    }

    private fun checkIfInvaildFreq(inAppData: CMInApp, currentTime: Long): Boolean {
        return if (inAppData.freq < RulesUtil.Constants.DEFAULT_FREQ) {
            checkIfLessThanDefaultFreq(inAppData, currentTime)
        } else {
            false
        }
    }

    private fun checkIfLessThanDefaultFreq(inAppData: CMInApp, currentTime: Long): Boolean {
        return if (checkIfTimeNotAssigned(inAppData)) {
            false
        } else if (inAppData.startTime > 0 && inAppData.endTime > 0) {
            checkForTimeIntervalValidity(inAppData, currentTime)
        } else {
            false
        }
    }

    private fun performDeletion(inAppData: CMInApp): Boolean {
        val perstOn = inAppData.isPersistentToggle
        return if (!perstOn && checkIfActiveInTimeFrame(inAppData, System.currentTimeMillis())) false else inAppData.endTime < System.currentTimeMillis() ||
                !inAppData.isShown && (inAppData.freq == 0 || inAppData.freq < RulesUtil.Constants.DEFAULT_FREQ)
    }

}