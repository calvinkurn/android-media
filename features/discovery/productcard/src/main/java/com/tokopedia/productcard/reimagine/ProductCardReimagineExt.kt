package com.tokopedia.productcard.reimagine

import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.productcard.utils.toOverlayUnifyLabelType
import com.tokopedia.productcard.utils.toUnifyTextColor
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifycomponents.R as unifyComponentsR

private val LABEL_COLOR_MAP by lazyThreadSafetyNone { mapOf(
    Label.HIGHLIGHT_LIGHT_GREEN to
        (unifyComponentsR.color.Unify_GN50
            to unifyComponentsR.color.Unify_GN500),
) }

internal fun Typography.initLabelGroupText(labelGroup: ProductCardModel.LabelGroup?) {
    if (labelGroup == null) hide()
    else showTypography(labelGroup)
}

private fun Typography.showTypography(labelGroup: ProductCardModel.LabelGroup) {
    shouldShowWithAction(labelGroup.hasTitle()) {
        it.text = MethodChecker.fromHtml(labelGroup.title)
        it.setTextColor(labelGroup.type.toUnifyTextColor(context))
    }
}

internal fun Typography.initLabelGroupLabel(labelGroup: ProductCardModel.LabelGroup?) {
    if (labelGroup == null) hide()
    else showLabel(labelGroup)
}

private fun Typography.showLabel(labelGroup: ProductCardModel.LabelGroup) {
    shouldShowWithAction(labelGroup.hasTitle()) {
        it.text = MethodChecker.fromHtml(labelGroup.title)

        try {
            val unifyLabelType = labelGroup.type.toOverlayUnifyLabelType()
            val labelColorPair = LABEL_COLOR_MAP[unifyLabelType] ?: (0 to 0)

            it.background = labelBackground(labelColorPair.first)
            it.setTextColor(ContextCompat.getColor(context, labelColorPair.second))
        } catch (_: Throwable) { }
    }
}

private fun Typography.labelBackground(color: Int) =
    background.apply {
        colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            ContextCompat.getColor(context, color),
            BlendModeCompat.SRC_ATOP,
        )
    }

internal fun <T: View?> View.lazyView(@IdRes id: Int): Lazy<T?> =
    lazyThreadSafetyNone { findViewById(id) }

internal fun <T: View?> View.showView(@IdRes id: Int, isShow: Boolean, initializer: () -> T) {
    if (isShow)
        (findViewById<T>(id) ?: initializer())?.show()
    else
        findViewById<T>(id)?.hide()
}

internal fun ConstraintLayout?.applyConstraintSet(configure: ConstraintSet.() -> Unit) {
    val constraintLayout = this ?: return
    val constraintSet = ConstraintSet()

    constraintSet.clone(constraintLayout)
    constraintSet.configure()
    constraintSet.applyTo(constraintLayout)
}
