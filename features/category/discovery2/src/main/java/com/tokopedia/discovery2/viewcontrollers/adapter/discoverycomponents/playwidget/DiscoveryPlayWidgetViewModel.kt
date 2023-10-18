package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.HideSectionUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiscoveryPlayWidgetViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val playWidgetUIMutableLiveData: MutableLiveData<PlayWidgetState?> = MutableLiveData(PlayWidgetState(isLoading = true))
    private val _reminderLoginEvent = SingleLiveEvent<Boolean>()
    private val _reminderObservable = MutableLiveData<Result<PlayWidgetReminderType>>()
    private val _hideSection = SingleLiveEvent<String>()

    private var dataPresent: Boolean = false
    private var reminderData: Pair<String, PlayWidgetReminderType>? = null

    fun getPlayWidgetUILiveData(): LiveData<PlayWidgetState?> = playWidgetUIMutableLiveData

    val reminderLoginEvent: LiveData<Boolean>
        get() = _reminderLoginEvent

    val reminderObservable: LiveData<Result<PlayWidgetReminderType>>
        get() = _reminderObservable

    val hideSectionLD: LiveData<String> = _hideSection

    @Inject
    lateinit var playWidgetTools: PlayWidgetTools

    @Inject
    lateinit var hideSectionUseCase: HideSectionUseCase

    fun isPlayWidgetToolsInitialized(): Boolean {
        return this::playWidgetTools.isInitialized
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getPlayWidgetData() {
        if (!dataPresent) {
            dataPresent = true
            hitPlayWidgetService()
        }
    }

    private fun hideIfPresentInSection() {
        val response = hideSectionUseCase.checkForHideSectionHandling(components)
        if(response.shouldHideSection){
            if(response.sectionId.isNotEmpty())
                _hideSection.value = response.sectionId
            syncData.value = true
        }
    }

    fun hitPlayWidgetService() {
        launchCatchError(block = {
            components.data?.firstOrNull()?.playWidgetPlayID?.let {  widgetID ->
                playWidgetUIMutableLiveData.value = processPlayWidget(widgetID)
            } ?: run {
                hideIfPresentInSection()
            }
        }, onError = {
            playWidgetUIMutableLiveData.value = null
            hideIfPresentInSection()
        })
    }


    private suspend fun processPlayWidget(widgetID: String): PlayWidgetState {
        val response = playWidgetTools.getWidgetFromNetwork(widgetType = PlayWidgetUseCase.WidgetType.DiscoveryPage(widgetID))
        return playWidgetTools.mapWidgetToModel(response)
    }

    fun updatePlayWidgetTotalView(channelId: String, totalView: String) {
        if (channelId.isNotEmpty() && totalView.isNotEmpty()) {
            val currentValue = playWidgetUIMutableLiveData.value
            currentValue?.let {
                playWidgetUIMutableLiveData.value = playWidgetTools.updateTotalView(it, channelId, totalView)
            }
        }
    }

    fun updatePlayWidgetReminder(channelId: String, isReminder: Boolean) {
        if (channelId.isNotEmpty()) {
            updateWidget {
                val reminderType = if(isReminder) PlayWidgetReminderType.Reminded else PlayWidgetReminderType.NotReminded
                playWidgetTools.updateActionReminder(it, channelId, reminderType)
            }
        }
    }

    fun shouldUpdatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        if (UserSession(application).isLoggedIn)
            updatePlayWidgetToggleReminder(channelId, reminderType)
        else {
            reminderData = Pair(channelId, reminderType)
            _reminderLoginEvent.setValue(true)
        }
    }

    private fun updatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        reminderData = null
        updateWidget {
            playWidgetTools.updateActionReminder(it, channelId, reminderType)
        }

        launchCatchError(block = {
            val response = playWidgetTools.updateToggleReminder(
                channelId,
                reminderType
            )

            when (playWidgetTools.mapWidgetToggleReminder(response)) {
                true -> {
                    _reminderObservable.postValue(Success(reminderType))
                }
                else -> {
                    updateWidget {
                        playWidgetTools.updateActionReminder(it, channelId, reminderType.switch())
                    }
                    _reminderObservable.postValue(Fail(Throwable()))
                }
            }
        }) { throwable ->
            updateWidget {
                playWidgetTools.updateActionReminder(it, channelId, reminderType.switch())
            }
            _reminderObservable.postValue(Fail(throwable))
        }
    }


    private fun updateWidget(onUpdate: (oldVal: PlayWidgetState) -> PlayWidgetState) {
        playWidgetUIMutableLiveData.value?.let { currentValue ->
            playWidgetUIMutableLiveData.postValue(onUpdate(currentValue))
        }
    }

    override fun loggedInCallback() {
        if (UserSession(application).isLoggedIn)
            reminderData?.run {
                updatePlayWidgetToggleReminder(first, second)
            }
    }
}
