package com.tokopedia.productcard.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.productcard.R
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

internal val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

internal val View?.isNotNullAndVisible
    get() = this != null && this.isVisible

internal val View?.isNullOrNotVisible
    get() = this == null || !this.isVisible

internal fun View.doIfVisible(action: (View) -> Unit) {
    if(this.isVisible) {
        action(this)
    }
}

internal fun View.getDimensionPixelSize(@DimenRes id: Int): Int {
    return this.context.resources.getDimensionPixelSize(id)
}

internal fun ConstraintLayout?.applyConstraintSet(configureConstraintSet: (ConstraintSet) -> Unit) {
    this?.let {
        val constraintSet = ConstraintSet()

        constraintSet.clone(it)
        configureConstraintSet(constraintSet)
        constraintSet.applyTo(it)
    }
}

internal fun TextView?.setTextWithBlankSpaceConfig(textValue: String, blankSpaceConfigValue: Boolean) {
    this?.configureVisibilityWithBlankSpaceConfig(textValue.isNotEmpty(), blankSpaceConfigValue) {
        it.text = MethodChecker.fromHtml(textValue)
    }
}

internal fun <T: View> T?.configureVisibilityWithBlankSpaceConfig(isVisible: Boolean, blankSpaceConfigValue: Boolean, action: (T) -> Unit) {
    if (this == null) return

    visibility = if (isVisible) {
        action(this)
        View.VISIBLE
    } else {
        getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue)
    }
}

internal fun getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue: Boolean): Int {
    return if (blankSpaceConfigValue) {
        View.INVISIBLE
    }
    else {
        View.GONE
    }
}

internal fun <T: View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
    if (this == null) return

    if (shouldShow) {
        this.visibility = View.VISIBLE
        action(this)
    } else {
        this.visibility = View.GONE
    }
}

internal fun ImageView.loadImage(url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_loading_toped_new)
                .error(R.drawable.ic_loading_toped_new)
                .into(this)
    }
}

internal fun ImageView.loadImageRounded(url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_loading_toped_new)
                .error(R.drawable.error_drawable)
                .into(this)
    }
}

internal fun ImageView.loadIcon(url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(this)
    }
}

internal fun String?.toUnifyLabelType(): Int {
    return when (this) {
        LIGHT_GREY -> Label.GENERAL_LIGHT_GREY
        LIGHT_BLUE -> Label.GENERAL_LIGHT_BLUE
        LIGHT_GREEN -> Label.GENERAL_LIGHT_GREEN
        LIGHT_RED -> Label.GENERAL_LIGHT_RED
        LIGHT_ORANGE -> Label.GENERAL_LIGHT_ORANGE
        DARK_GREY -> Label.GENERAL_DARK_GREY
        DARK_BLUE -> Label.GENERAL_DARK_BLUE
        DARK_GREEN -> Label.GENERAL_DARK_GREEN
        DARK_RED -> Label.GENERAL_DARK_RED
        DARK_ORANGE -> Label.GENERAL_DARK_ORANGE
        else -> Label.GENERAL_LIGHT_GREY
    }
}

internal fun Label.initLabelGroup(labelGroup: ProductCardModel.LabelGroup?) {
    if (labelGroup == null) hide()
    else showLabel(labelGroup)
}

private fun Label.showLabel(labelGroup: ProductCardModel.LabelGroup) {
    shouldShowWithAction(labelGroup.title.isNotEmpty()) {
        it.text = labelGroup.title
        it.setLabelType(labelGroup.type.toUnifyLabelType())
    }
}

internal fun String?.toUnifyTypographyType(): Int {
    return when(this) {
        HEADING_1 -> Typography.HEADING_1
        HEADING_2 -> Typography.HEADING_2
        HEADING_3 -> Typography.HEADING_3
        HEADING_4 -> Typography.HEADING_4
        HEADING_5 -> Typography.HEADING_5
        HEADING_6 -> Typography.HEADING_6
        BODY_1 -> Typography.BODY_1
        BODY_2 -> Typography.BODY_2
        BODY_3 -> Typography.BODY_3
        SMALL -> Typography.SMALL
        else -> Typography.SMALL
    }
}

internal fun String?.toUnifyTypographyWeight(): Int {
    return when(this) {
        BOLD -> Typography.BOLD
        REGULAR -> Typography.REGULAR
        else -> Typography.REGULAR
    }
}

internal fun Typography.initTextGroup(textGroup: ProductCardModel.TextGroup?) {
    if (textGroup == null) hide()
    else showTypography(textGroup)
}

private fun Typography.showTypography(textGroup: ProductCardModel.TextGroup) {
    shouldShowWithAction(textGroup.title.isNotEmpty()) {
        it.text = textGroup.title
        it.setType(textGroup.type.toUnifyTypographyType())
        it.setWeight(textGroup.weight.toUnifyTypographyWeight())
        it.setTextColor(safeParseColor(textGroup.color))
    }
}

private fun safeParseColor(color: String): Int {
    return try {
        Color.parseColor(color)
    }
    catch (throwable: Throwable) {
        throwable.printStackTrace()
        0
    }
}