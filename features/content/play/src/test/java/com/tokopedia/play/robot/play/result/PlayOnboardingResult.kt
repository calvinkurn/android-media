package com.tokopedia.play.robot.play.result

import com.tokopedia.play.helper.NoValueException
import com.tokopedia.play.robot.DualResult
import com.tokopedia.play.robot.errorHasResult
import com.tokopedia.play.robot.errorNoResult
import com.tokopedia.play_common.util.event.Event
import org.assertj.core.api.Assertions


/**
 * Created by mzennis on 17/02/21.
 */
class PlayOnboardingResult(
        result: () -> Event<Unit>
) {
    private val mResult = try {
        DualResult.HasValue(result())
    } catch (e: Throwable) {
        DualResult.NoValue(e)
    }

    fun isEqualTo(expected: Event<Unit>): PlayOnboardingResult {
        require(mResult is DualResult.HasValue) { errorNoResult }

        Assertions
                .assertThat(mResult.result)
                .isEqualToComparingFieldByField(expected)
        return this
    }

    fun hasNoValue(): PlayOnboardingResult {
        require(mResult is DualResult.NoValue) { errorHasResult }

        Assertions
                .assertThat(mResult.reason)
                .isInstanceOf(NoValueException::class.java)

        return this
    }
}