package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayChannelInfoResult(
        private val result: PlayChannelInfoUiModel
) {

    val channelType = ChannelTypeResult(result.channelType)

    fun isEqualTo(expected: PlayChannelInfoUiModel): PlayChannelInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    class ChannelTypeResult(
            private val result: PlayChannelType
    ) {

        fun isEqualTo(expected: PlayChannelType): ChannelTypeResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }
}