package com.tokopedia.tkpd.comparison

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

@Composable
fun UnifyButtonCompose(modifier: Modifier = Modifier, text: String) {
    AndroidView(modifier = modifier, factory = {
        UnifyButton(it).apply {
            this.text = text
            // This approach only works if we set non-default value
            this.buttonSize = UnifyButton.Size.MEDIUM
//            this.buttonVariant = UnifyButton.Variant.FILLED
//            this.buttonType = UnifyButton.Type.MAIN
        }
    }, update = {

    })
}

@Composable
fun TextUnify(
    text: String,
    modifier: Modifier = Modifier,
    type: TextUnifyType,
    weight: TextUnifyWeight = TextUnifyWeight.Regular,
    update: Typography.(Context) -> Unit
) {
    AndroidView(
        factory = { context ->
            Typography(context).apply {
                setWeight(weight = weight.value)
                setType(type = type.value)
                this.text = text
            }
        },
        modifier = modifier,
        update = {
            update.invoke(it, it.context)
        }
    )
}

sealed class TextUnifyWeight(val value: Int) {
    object Regular: TextUnifyWeight(value = 1)
    object Bold: TextUnifyWeight(value = 2)
}

sealed class TextUnifyType(val value: Int) {
    object Heading1: TextUnifyType(value = 1)
    object Heading2: TextUnifyType(value = 2)
    object Heading3: TextUnifyType(value = 3)
    object Heading4: TextUnifyType(value = 4)
    object Heading5: TextUnifyType(value = 5)
    object Heading6: TextUnifyType(value = 6)
    object Body1: TextUnifyType(value = 7)
    object Body2: TextUnifyType(value = 8)
    object Body3: TextUnifyType(value = 9)
    object Small: TextUnifyType(value = 10)
    object Display1: TextUnifyType(value = 14)
    object Display2: TextUnifyType(value = 15)
    object Display3: TextUnifyType(value = 16)
    object Paragraph1: TextUnifyType(value = 17)
    object Paragraph2: TextUnifyType(value = 18)
    object Paragraph3: TextUnifyType(value = 19)
    object Display3Uppercase: TextUnifyType(value = 20)
}
