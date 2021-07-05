package com.tokopedia.play.robot.parent.result

import com.tokopedia.play_common.model.result.PageResult
import com.tokopedia.play_common.model.result.PageResultState
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayParentChannelIdResult(
        private val result: PageResult<List<String>>
) {

    fun isNotEmpty(): PlayParentChannelIdResult {
        Assertions
                .assertThat(result.currentValue)
                .isNotEmpty

        return this
    }

    fun isEmpty(): PlayParentChannelIdResult {
        Assertions
                .assertThat(result.currentValue)
                .isEmpty()

        return this
    }

    fun isSuccess(): PlayParentChannelIdResult {
        Assertions
                .assertThat(result.state)
                .isInstanceOf(PageResultState.Success::class.java)

        return this
    }

    fun isFailure(): PlayParentChannelIdResult {
        Assertions
                .assertThat(result.state)
                .isInstanceOf(PageResultState.Fail::class.java)

        return this
    }
}