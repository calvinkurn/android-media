package com.tokopedia.play.robot.play.result

import com.tokopedia.play.robot.DualResult
import com.tokopedia.play.robot.errorNoResult
import com.tokopedia.play.view.uimodel.recom.PlayLikeInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeParamInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeStatusInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayLikeInfoResult(
        private val result: PlayLikeInfoUiModel
) {

    val likeParam = LikeParamResult(result.param)
    val likeStatus = LikeStatusResult { if (result is PlayLikeInfoUiModel.Complete) result.status else null }

    fun isEqualTo(expected: PlayLikeStatusInfoUiModel): PlayLikeInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    class LikeStatusResult(
            result: () -> PlayLikeStatusInfoUiModel?
    ) {

        private val mResult = try {
            val theResult = result()
            requireNotNull(theResult)
            DualResult.HasValue(theResult)
        } catch (e: Throwable) {
            DualResult.NoValue(e)
        }

        fun isEqualTo(expected: PlayLikeStatusInfoUiModel): LikeStatusResult {
            require(mResult is DualResult.HasValue) { errorNoResult }

            Assertions
                    .assertThat(mResult.result)
                    .isEqualTo(expected)

            return this
        }

        fun isAvailable(): LikeStatusResult {
            Assertions
                    .assertThat(mResult)
                    .isInstanceOf(DualResult.HasValue::class.java)

            return this
        }

        fun isUnavailable(): LikeStatusResult {
            Assertions
                    .assertThat(mResult)
                    .isInstanceOf(DualResult.NoValue::class.java)

            return this
        }
    }

    class LikeParamResult(
            private val result: PlayLikeParamInfoUiModel
    ) {

        fun isEqualTo(expected: PlayLikeParamInfoUiModel): LikeParamResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }
}