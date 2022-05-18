package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest

import android.app.Application
import androidx.lifecycle.LiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.quest_widget.constants.QuestUrls
import com.tokopedia.utils.lifecycle.SingleLiveEvent

class TopQuestViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {
    var shouldUpdate:Boolean = false
    var loggedInUpdate:Boolean = false
    private val _updateQuestData:SingleLiveEvent<Boolean> = SingleLiveEvent()
    val updateQuestData:LiveData<Boolean> = _updateQuestData

    private val _navigateData:SingleLiveEvent<String> = SingleLiveEvent()
    val navigateData:LiveData<String> = _navigateData

    override fun onResume() {
        super.onResume()
        if (shouldUpdate) {
            shouldUpdate = false
            _updateQuestData.value = true
        }
        if (loggedInUpdate) {
            loggedInUpdate = false
            shouldUpdate = true
        }
    }

    override fun loggedInCallback() {
        super.loggedInCallback()
        _navigateData.value = QuestUrls.QUEST_URL
        loggedInUpdate = true
    }
}