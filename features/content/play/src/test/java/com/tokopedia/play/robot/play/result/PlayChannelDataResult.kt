package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.storage.PlayChannelData
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 11/02/21
 */
class PlayChannelDataResult(
        private val result: PlayChannelData
) {

    fun isEqualTo(expected: PlayChannelData): PlayChannelDataResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    fun isNotEqualTo(expected: PlayChannelData): PlayChannelDataResult {
        Assertions
                .assertThat(result)
                .isNotEqualTo(expected)

        return this
    }
}