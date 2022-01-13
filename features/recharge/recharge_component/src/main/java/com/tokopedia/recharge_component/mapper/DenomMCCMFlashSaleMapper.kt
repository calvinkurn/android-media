package com.tokopedia.recharge_component.mapper

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import java.text.SimpleDateFormat
import java.util.*

object DenomMCCMFlashSaleMapper {

    private const val CHANNEL_ID = "1"
    private const val EXPIRED_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZ"
    private const val SECOND_IN_MILIS = 1000

    private const val DUMMY_END_TIME = "1642098639"

    fun getChannelFlashSale(titleFlashSale: String, subtitleFlashSale:String, textColor: String): ChannelModel {

        val currentDate = Calendar.getInstance().time
        val currentTimeInSeconds = currentDate.time / SECOND_IN_MILIS

        val parser = SimpleDateFormat(EXPIRED_DATE_PATTERN)
        val expiredTime = if (DUMMY_END_TIME.isNotEmpty())
            parser.format(Date(DUMMY_END_TIME.toLong() * SECOND_IN_MILIS))
        else ""

        return ChannelModel(
            id = CHANNEL_ID,
            groupId = CHANNEL_ID,
            channelHeader = ChannelHeader(
                name = titleFlashSale,
                subtitle = subtitleFlashSale,
                textColor = textColor,
                expiredTime = expiredTime,
                serverTimeUnix = currentTimeInSeconds
            ),
            channelConfig = ChannelConfig(
                serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                    currentTimeInSeconds
                )
            )
        )
    }

    fun getChannelMCCM(titleMCCM: String, textColor: String): ChannelModel {
        return ChannelModel(
            id = CHANNEL_ID,
            groupId = CHANNEL_ID,
            channelHeader = ChannelHeader(
                name = titleMCCM,
                textColor = textColor
            )
        )
    }
}