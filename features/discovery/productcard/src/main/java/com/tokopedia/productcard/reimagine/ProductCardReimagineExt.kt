package com.tokopedia.productcard.reimagine

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.utils.LABEL_BLACK
import com.tokopedia.productcard.utils.LABEL_WHITE
import com.tokopedia.productcard.utils.LIGHT_GREEN
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifycomponents.R as unifycomponentsR

private val LABEL_COLOR_MAP by lazyThreadSafetyNone { mapOf(
    LIGHT_GREEN to
        (unifycomponentsR.color.Unify_GN100
            to unifycomponentsR.color.Unify_GN500),

    LABEL_BLACK to
        (unifycomponentsR.color.Unify_Static_Black_68
            to unifycomponentsR.color.Unify_Static_White),

    LABEL_WHITE to
        (unifycomponentsR.color.Unify_NN50_96
            to unifycomponentsR.color.Unify_NN950),
) }

internal fun Typography.initLabelGroupLabel(labelGroup: ProductCardModel.LabelGroup?) {
    if (labelGroup == null) hide()
    else showLabel(labelGroup)
}

private fun Typography.showLabel(labelGroup: ProductCardModel.LabelGroup) {
    shouldShowWithAction(labelGroup.hasTitle()) {
        it.text = MethodChecker.fromHtml(labelGroup.title)

        try {
            val unifyLabelType = labelGroup.type
            val labelColorPair = LABEL_COLOR_MAP[unifyLabelType] ?: (0 to 0)

            it.setLabelBackground(labelColorPair.first)
            it.setTextColor(ContextCompat.getColor(context, labelColorPair.second))
        } catch (_: Throwable) { }
    }
}

private fun Typography.setLabelBackground(@ColorRes color: Int) =
    background.run {
        mutate()

        val colorInt = ContextCompat.getColor(context, color)
        (this as? GradientDrawable)?.setColor(colorInt)

        alpha = colorInt.alpha
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
