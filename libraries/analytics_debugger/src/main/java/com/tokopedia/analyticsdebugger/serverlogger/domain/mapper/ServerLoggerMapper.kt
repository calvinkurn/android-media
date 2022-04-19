package com.tokopedia.analyticsdebugger.serverlogger.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.utils.ServerLoggerConstants
import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.repository.LoggerRepository
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.LoggerReporting
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ServerLoggerMapper @Inject constructor(
    private val loggerRepository: LoggerRepository?
) {

    fun mapToLoggerListUiModel(
        loggerLocal: List<Logger>,
        keyword: String
    ): List<ItemServerLoggerUiModel> {
        val decrypt = loggerRepository?.decrypt
        val loggerUiModelList = mutableListOf<ItemServerLoggerUiModel>()
        loggerLocal.forEach {
            val message = decrypt?.invoke(it.message).orEmpty()
            val obj = JSONObject(message)
            val tag = obj.getString(Constants.TAG_LOG).orEmpty()
            val serverChannelList = mutableListOf<String>()
            val tagMapsValue = StringBuilder(it.serverChannel)
                .append(LoggerReporting.DELIMITER_TAG_MAPS)
                .append(tag)
                .toString()

            val loggerReporting = LoggerReporting.getInstance()
            loggerReporting.tagMapsScalyr[tagMapsValue]?.let {
                serverChannelList.add(ServerLoggerConstants.SCALYR)
            }
            loggerReporting.tagMapsNewRelic[tagMapsValue]?.let {
                serverChannelList.add(ServerLoggerConstants.NEW_RELIC)
            }
            loggerReporting.tagMapsEmbrace[tagMapsValue]?.let {
                serverChannelList.add(ServerLoggerConstants.EMBRACE)
            }

            val loggerUiModel = ItemServerLoggerUiModel(
                serverChannel = serverChannelList,
                tag = tag,
                previewMessage = formatDataExcerpt(message),
                message = message,
                dateTime = getDateFormat(it.timeStamp),
                priority = it.serverChannel
            )

            if (keyword.isNotBlank()) {
                if (message.contains(keyword, true) ||
                    tag.contains(keyword, true) ||
                    serverChannelList.contains(keyword)
                ) {
                    loggerUiModelList.add(loggerUiModel)
                }
            } else {
                loggerUiModelList.add(loggerUiModel)
            }
        }
        return loggerUiModelList
    }

    fun mapToPriorityList(
        chipsSelected: String
    ): ServerLoggerPriorityUiModel {
        return ServerLoggerPriorityUiModel(
            getPriorityList().map {
                ItemPriorityUiModel(
                    priorityName = it,
                    isSelected = it == chipsSelected
                )
            }
        )
    }

    private fun getPriorityList(): List<String> {
        return listOf(LoggerReporting.P1, LoggerReporting.P2, LoggerReporting.SF)
    }

    private fun getDateFormat(timeStamp: Long): String {
        val datePattern = "yyyy-MM-dd HH:mm:ss.SSS"
        val dateFormat = SimpleDateFormat(datePattern, DateFormatUtils.DEFAULT_LOCALE)
        return dateFormat.format(Date(timeStamp))
    }

}