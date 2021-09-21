package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
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

    private val playWidgetUIMutableLiveData: MutableLiveData<PlayWidgetUiModel?> = MutableLiveData(PlayWidgetUiModel.Placeholder)
    private val _reminderLoginEvent = SingleLiveEvent<Boolean>()
    private val _reminderObservable = MutableLiveData<Result<PlayWidgetReminderType>>()

    private var dataPresent: Boolean = false
    private var reminderData: Pair<String, PlayWidgetReminderType>? = null

    fun getPlayWidgetUILiveData(): LiveData<PlayWidgetUiModel?> = playWidgetUIMutableLiveData

    val reminderLoginEvent: LiveData<Boolean>
        get() = _reminderLoginEvent

    val reminderObservable: LiveData<Result<PlayWidgetReminderType>>
        get() = _reminderObservable

    @Inject
    lateinit var playWidgetTools: PlayWidgetTools

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

    fun hitPlayWidgetService() {
        launchCatchError(block = {
            components.data?.firstOrNull()?.playWidgetPlayID?.let { widgetID ->
                playWidgetUIMutableLiveData.value = processPlayWidget(widgetID)
            }
        }, onError = {
            playWidgetUIMutableLiveData.value = null
        })
    }


    private suspend fun processPlayWidget(widgetID: String): PlayWidgetUiModel {

        val response = playWidgetTools.getWidgetFromNetwork(widgetType = PlayWidgetUseCase.WidgetType.DiscoveryPage(widgetID))
        val uiModel = playWidgetTools.mapWidgetToModel(response)
        return uiModel
    }

    fun updatePlayWidgetTotalView(channelId: String, totalView: String) {
        if (channelId.isNotEmpty() && totalView.isNotEmpty()) {
            val currentValue = playWidgetUIMutableLiveData.value
            currentValue?.let {
                playWidgetUIMutableLiveData.value = playWidgetTools.updateTotalView(it, channelId, totalView)
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

            when (val success = playWidgetTools.mapWidgetToggleReminder(response)) {
                success -> {
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


    private fun updateWidget(onUpdate: (oldVal: PlayWidgetUiModel) -> PlayWidgetUiModel) {
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