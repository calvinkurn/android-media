package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 11/02/21
 */
class PlayBottomInsetsResult(
        private val result: Map<BottomInsetsType, BottomInsetsState>
) {

    val keyboard = BottomInsetResult(result[BottomInsetsType.Keyboard] ?: error("Keyboard should be supported"))
    val productBottomSheet = BottomInsetResult(result[BottomInsetsType.ProductSheet] ?: error("Product Bottom Sheet should be supported"))
    val variantBottomSheet = BottomInsetResult(result[BottomInsetsType.VariantSheet] ?: error("Variant Bottom Sheet should be supported"))

    class BottomInsetResult(
            private val result: BottomInsetsState
    ) {

        fun isShown() {
            Assertions
                    .assertThat(result)
                    .isInstanceOf(BottomInsetsState.Shown::class.java)
        }

        fun isHidden() {
            Assertions
                    .assertThat(result)
                    .isInstanceOf(BottomInsetsState.Hidden::class.java)
        }
    }
}