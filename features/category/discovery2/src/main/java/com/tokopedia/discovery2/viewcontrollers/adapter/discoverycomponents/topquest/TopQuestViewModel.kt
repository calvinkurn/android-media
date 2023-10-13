package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.HideSectionUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.quest_widget.constants.QuestUrls
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class TopQuestViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {
    @JvmField
    @Inject
    var hideSectionUseCase: HideSectionUseCase? = null

    private val _hideSection = SingleLiveEvent<String>()
    val hideSectionLD: LiveData<String> = _hideSection

    var shouldUpdate: Boolean = false
    var loggedInUpdate: Boolean = false

    private val _shouldHideWidget: MutableLiveData<Boolean> = MutableLiveData()
    val shouldHideWidget: LiveData<Boolean> = _shouldHideWidget

    private val _updateQuestData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val updateQuestData: LiveData<Boolean> = _updateQuestData

    private val _navigateData: SingleLiveEvent<String> = SingleLiveEvent()
    val navigateData: LiveData<String> = _navigateData

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

    private fun hideIfPresentInSection() {
        val response = hideSectionUseCase?.checkForHideSectionHandling(components)
        if (response?.shouldHideSection == true) {
            if (response.sectionId.isNotEmpty()) {
                _hideSection.value = response.sectionId
            }
            syncData.value = true
        }
    }

    fun handleWidgetStatus(status: LiveDataResult.STATUS) {
        when (status) {
            LiveDataResult.STATUS.ERROR, LiveDataResult.STATUS.EMPTY_DATA -> {
                _shouldHideWidget.value = true
                hideIfPresentInSection()
            }
            else -> {
                _shouldHideWidget.value = false
            }
        }
    }
}
