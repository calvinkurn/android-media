package com.tokopedia.play.robot.play.result

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
    val likeStatus = LikeStatusResult(if (result is PlayLikeInfoUiModel.Complete) result.status else null)

    fun isEqualTo(expected: PlayLikeStatusInfoUiModel): PlayLikeInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    class LikeStatusResult(private val result: PlayLikeStatusInfoUiModel?) {

        fun isEqualTo(expected: PlayLikeStatusInfoUiModel): LikeStatusResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }

        fun isAvailable(): LikeStatusResult {
            Assertions
                    .assertThat(result)
                    .isNotNull

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