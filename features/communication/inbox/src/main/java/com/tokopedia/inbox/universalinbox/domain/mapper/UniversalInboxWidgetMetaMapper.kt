package com.tokopedia.inbox.universalinbox.domain.mapper

import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxMenuAndWidgetMetaResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class UniversalInboxWidgetMetaMapper @Inject constructor() {

    fun mapWidgetMetaToUiModel(
        widgetMeta: UniversalInboxMenuAndWidgetMetaResponse?,
        allCounter: UniversalInboxAllCounterResponse,
        driverCounter: Result<Pair<Int, Int>>?
    ): UniversalInboxWidgetMetaUiModel {
        val result = UniversalInboxWidgetMetaUiModel()
        if (widgetMeta != null) {
            widgetMeta.widgetMenu.forEach {
                it.mapToUiModel(allCounter, driverCounter)?.let { uiModel ->
                    result.widgetList.add(uiModel)
                }
            }
        } else {
            result.isError = true
        }
        return result
    }

    private fun UniversalInboxWidgetDataResponse.mapToUiModel(
        allCounter: UniversalInboxAllCounterResponse,
        driverCounter: Result<Pair<Int, Int>>? = null
    ): UniversalInboxWidgetUiModel? {
        return when (this.type) {
            UniversalInboxValueUtil.CHATBOT_TYPE -> {
                this.mapToWidget(allCounter.othersUnread.helpUnread)
            }
            UniversalInboxValueUtil.GOJEK_TYPE -> {
                this.mapToDriverWidget(driverCounter)
            }
            else -> this.mapToWidget() // Default
        }
    }

    private fun UniversalInboxWidgetDataResponse.mapToWidget(
        counter: Int = Int.ZERO
    ): UniversalInboxWidgetUiModel? {
        // If not dynamic (not controlled from BE) and no notification, do not show the widget
        return if (!this.isDynamic && counter < Int.ONE) {
            null
        } else {
            UniversalInboxWidgetUiModel(
                icon = this.icon.toIntOrZero(),
                title = this.title,
                subtext = this.subtext,
                applink = this.applink,
                counter = counter,
                type = this.type
            )
        }
    }

    private fun UniversalInboxWidgetDataResponse.mapToDriverWidget(
        driverCounter: Result<Pair<Int, Int>>?
    ): UniversalInboxWidgetUiModel? {
        return if (driverCounter != null) {
            when (driverCounter) {
                is Success -> {
                    if (!this.isDynamic && driverCounter.data.first < Int.ONE) {
                        null
                    } else {
                        // Show when only active channel is not zero or dynamic from BE
                        UniversalInboxWidgetUiModel(
                            icon = this.icon.toIntOrZero(),
                            title = this.title,
                            subtext = this.subtext.replace(
                                oldValue = UniversalInboxValueUtil.GOJEK_REPLACE_TEXT,
                                newValue = "${driverCounter.data.first}",
                                ignoreCase = true
                            ),
                            applink = this.applink,
                            counter = driverCounter.data.second,
                            type = this.type
                        )
                    }
                }
                is Fail -> {
                    // Return widget error with data
                    UniversalInboxWidgetUiModel(
                        icon = this.icon.toIntOrZero(),
                        title = this.title,
                        subtext = this.subtext,
                        applink = this.applink,
                        counter = Int.ZERO,
                        type = this.type,
                        isError = true
                    )
                }
            }
        } else {
            null // Return null if driver counter is null
        }
    }
}
