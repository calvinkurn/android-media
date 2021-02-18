package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiscoveryPlayWidgetViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private var playWidgetUIMutableLiveData: MutableLiveData<PlayWidgetUiModel?> = MutableLiveData(PlayWidgetUiModel.Placeholder)
    private var dataPresent: Boolean = false

    fun getPlayWidgetUILiveData(): LiveData<PlayWidgetUiModel?> = playWidgetUIMutableLiveData

    @Inject
    lateinit var playWidgetTools: PlayWidgetTools

    fun isPlayWidgetToolsInitialized(): Boolean {
        return this::playWidgetTools.isInitialized
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getPlayWidgetData() {
        if (!dataPresent)
            launchCatchError(block = {
                Log.e(DiscoveryPlayWidgetViewHolder.TestTag, "getPlayWidgetData - Block")
                components.data?.firstOrNull()?.playWidgetPlayID?.let { widgetID ->
                    playWidgetUIMutableLiveData.value = processPlayWidget(widgetID)
                }
                dataPresent = true
            }, onError = {
                dataPresent = true
//                TODO::ERROR HANDLING
                Log.e(DiscoveryPlayWidgetViewHolder.TestTag, "getPlayWidgetData - onError")
                Log.e(DiscoveryPlayWidgetViewHolder.TestTag, "Error - ${it}")
                playWidgetUIMutableLiveData.value = null
            })
    }


    private suspend fun processPlayWidget(widgetID: String): PlayWidgetUiModel {

        val response = playWidgetTools.getWidgetFromNetwork(widgetType = PlayWidgetUseCase.WidgetType.DiscoveryPage(widgetID))
        val uiModel = playWidgetTools.mapWidgetToModel(response)
        if(widgetID == "6")
            throw RuntimeException()
        return uiModel
    }

    fun updatePlayWidgetTotalView(channelId: String, totalView: String) {
        Log.e(DiscoveryPlayWidgetViewHolder.TestTag, "updatePlayWidgetTotalView - channelId $channelId totalView $totalView ")
        if (channelId.isNotEmpty() && totalView.isNotEmpty()) {
            val currentValue = playWidgetUIMutableLiveData.value
            currentValue?.let {
                playWidgetUIMutableLiveData.value = playWidgetTools.updateTotalView(it, channelId, totalView)
            }
        }
    }

}