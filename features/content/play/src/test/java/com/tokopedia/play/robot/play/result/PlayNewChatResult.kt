package com.tokopedia.play.robot.play.result

import com.tokopedia.play.helper.NoValueException
import com.tokopedia.play.robot.DualResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.Event
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 15/02/21
 */
class PlayNewChatResult(
        result: () -> Event<PlayChatUiModel>
) {

    private val mResult = try {
        DualResult.HasValue(result())
    } catch (e: Throwable) {
        DualResult.NoValue(e)
    }

    private val errorNoResult = "Result has no value"
    private val errorHasResult = "Result has value"

    fun isEqualTo(expected: PlayChatUiModel): PlayNewChatResult {
        require(mResult is DualResult.HasValue) { errorNoResult }

        Assertions
                .assertThat(mResult.result.peekContent())
                .isEqualTo(expected)

        return this
    }

    fun isMessageEqualTo(expected: String): PlayNewChatResult {
        require(mResult is DualResult.HasValue) { errorNoResult }

        Assertions
                .assertThat(mResult.result.peekContent().message)
                .isEqualTo(expected)

        return this
    }

    fun hasNoValue(): PlayNewChatResult {
        require(mResult is DualResult.NoValue) { errorHasResult }

        Assertions
                .assertThat(mResult.reason)
                .isInstanceOf(NoValueException::class.java)

        return this
    }
}