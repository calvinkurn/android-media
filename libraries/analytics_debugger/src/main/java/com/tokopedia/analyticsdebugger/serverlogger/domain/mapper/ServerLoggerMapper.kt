package com.tokopedia.analyticsdebugger.serverlogger.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerUiModel
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
    ): List<ServerLoggerUiModel> {
        val decrypt = loggerRepository?.decrypt
        val loggerUiModelList = mutableListOf<ServerLoggerUiModel>()
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

            val loggerUiModel = ServerLoggerUiModel(
                serverChannel = serverChannelList,
                tag = tag,
                previewMessage = formatDataExcerpt(message),
                message = message,
                dateTime = getDateFormat(it.timeStamp),
                priority = it.serverChannel
            )

            if (keyword.isNotBlank()) {
                if (keyword in message || keyword in tag || keyword in serverChannelList) {
                    loggerUiModelList.add(loggerUiModel)
                }
            } else {
                loggerUiModelList.add(loggerUiModel)
            }
        }
        return loggerUiModelList
    }

    fun mapToPriorityList(priorityList: List<String>, chipsSelected: String): ServerLoggerPriorityUiModel {
        return ServerLoggerPriorityUiModel(
            priorityList.map {
                ItemPriorityUiModel(
                    priorityName = it,
                    isSelected = it == chipsSelected
                )
            }
        )
    }

    private fun getDateFormat(timeStamp: Long): String {
        val datePattern = "yyyy-MM-dd HH:mm:ss.SSS"
        val dateFormat = SimpleDateFormat(datePattern, DateFormatUtils.DEFAULT_LOCALE)
        return dateFormat.format(Date(timeStamp))
    }

}