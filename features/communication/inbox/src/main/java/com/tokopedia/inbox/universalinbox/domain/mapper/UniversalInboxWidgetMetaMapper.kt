package com.tokopedia.inbox.universalinbox.domain.mapper

import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.inbox.universalinbox.util.Result as Result

class UniversalInboxWidgetMetaMapper @Inject constructor(
    private val abTestPlatform: UniversalInboxAbPlatform
) {

    private val driver = "driver"

    fun mapWidgetMetaToUiModel(
        widgetMetaResponse: List<UniversalInboxWidgetDataResponse>,
        counterResponse: UniversalInboxAllCounterResponse,
        driverCounter: Result<List<ConversationsChannel>>
    ): UniversalInboxWidgetMetaUiModel {
        val result = UniversalInboxWidgetMetaUiModel()
        widgetMetaResponse.forEach {
            it.mapToUiModel(counterResponse, driverCounter)?.let { uiModel ->
                result.widgetList.add(uiModel)
            }
        }
        return result
    }

    private fun UniversalInboxWidgetDataResponse.mapToUiModel(
        counterResponse: UniversalInboxAllCounterResponse,
        driverCounter: Result<List<ConversationsChannel>>
    ): UniversalInboxWidgetUiModel? {
        return when (this.type) {
            UniversalInboxValueUtil.CHATBOT_TYPE -> {
                this.mapToWidget(counterResponse.othersUnread.helpUnread)
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
        driverCounter: Result<List<ConversationsChannel>>
    ): UniversalInboxWidgetUiModel? {
        return when (driverCounter) {
            is Result.Success -> {
                val counterData = onSuccessGetDriverChannelList(driverCounter.data)
                if (!this.isDynamic && counterData.first < Int.ONE) {
                    null
                } else {
                    // Show when only active channel is not zero or dynamic from BE
                    UniversalInboxWidgetUiModel(
                        icon = this.icon.toIntOrZero(),
                        title = this.title,
                        subtext = this.subtext.replace(
                            oldValue = UniversalInboxValueUtil.GOJEK_REPLACE_TEXT,
                            newValue = "${counterData.first}",
                            ignoreCase = true
                        ),
                        applink = this.applink,
                        counter = counterData.second,
                        type = this.type
                    )
                }
            }
            is Result.Error -> {
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
            is Result.Loading -> {
                null
            }
        }
    }

    private fun onSuccessGetDriverChannelList(
        channelList: List<ConversationsChannel>
    ): Pair<Int, Int> {
        return try {
            var activeChannel = 0
            var unreadTotal = 0
            channelList.forEach { channel ->
                val driverMember = channel.members.find {
                    it.ownerType == driver
                }
                if ((channel.expiresAt > System.currentTimeMillis() || channel.expiresAt == 0L) && // Expires At should not be 0, If 0 then SDK initialize the chat and the data is still being fetch
                    driverMember != null
                ) {
                    val serviceType = channel.metadata?.orderInfo?.serviceType ?: 0
                    if (isChatTokofood(serviceType) || isTokoChatLogisticEnabled()) {
                        activeChannel++
                        unreadTotal += channel.unreadCount
                    }
                }
            }
            if (unreadTotal >= 0) {
                Pair(activeChannel, unreadTotal)
            } else {
                throw IllegalArgumentException()
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            Pair(0, 0) // Default
        }
    }

    private fun isTokoChatLogisticEnabled(): Boolean {
        return abTestPlatform.getString(
            ROLLENCE_LOGISTIC_CHAT,
            ""
        ) == ROLLENCE_LOGISTIC_CHAT
    }

    private fun isChatTokofood(serviceType: Int): Boolean {
        return serviceType == TOKOFOOD_SERVICE_TYPE
    }

    companion object {
        const val ROLLENCE_LOGISTIC_CHAT = "gosend_chat_an"
        private const val GOSEND_INSTANT_SERVICE_TYPE = 14
        private const val GOSEND_SAMEDAY_SERVICE_TYPE = 23
        private const val TOKOFOOD_SERVICE_TYPE = 5
    }
}
