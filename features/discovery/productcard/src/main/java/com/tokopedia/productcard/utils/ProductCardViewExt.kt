package com.tokopedia.productcard.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.common.MediaDataSource
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.transform.CenterCrop
import com.tokopedia.media.loader.utils.MediaException
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
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

internal fun ImageView.glideClear() {
    this.clearImage()
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
        this.loadImage(url) {
            cacheStrategy = MediaCacheStrategy.RESOURCE
            error = R.drawable.placeholder_grey
        }
    }
}
internal fun ImageView.loadImage(url: String?, state: ((Boolean) -> Unit)) {
    if (url != null && url.isNotEmpty()) {
        this.loadImage(url) {
            cacheStrategy = MediaCacheStrategy.RESOURCE
            error = R.drawable.placeholder_grey
            loaderListener = object : LoaderStateListener {
                override fun successLoad(resource: Bitmap?, dataSource: MediaDataSource?) {
                    state.invoke(true)
                }

                override fun failedLoad(error: MediaException?) {
                    state.invoke(false)
                }
            }
        }
    }
}
internal fun ImageView.loadImageWithOutPlaceholder(url: String?, state: ((Boolean) -> Unit)) {
    if (url != null && url.isNotEmpty()) {
        this.loadImage(url) {
            cacheStrategy = MediaCacheStrategy.RESOURCE
            error = R.drawable.placeholder_grey
            loaderListener = object : LoaderStateListener {
                override fun successLoad(resource: Bitmap?, dataSource: MediaDataSource?) {
                    state.invoke(true)
                }

                override fun failedLoad(error: MediaException?) {
                    state.invoke(false)
                }
            }
        }
    }
}

internal fun ImageView.loadImageRounded(url: String?) {
    if (url != null && url.isNotEmpty()) {
        this.loadImage(url) {
            cacheStrategy = MediaCacheStrategy.RESOURCE
            error = R.drawable.placeholder_grey
            transforms = listOf(CenterCrop())
            roundedRadius = getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_6).toFloat()
        }
    }
}

internal fun ImageView.loadIcon(url: String?) {
    if (url != null && url.isNotEmpty()) {
        this.loadImage(url) {
            cacheStrategy = MediaCacheStrategy.RESOURCE
        }
    }
}

internal fun Label.initLabelGroup(labelGroup: ProductCardModel.LabelGroup?) {
    if (labelGroup == null) hide()
    else showLabel(labelGroup)
}

private fun Label.showLabel(labelGroup: ProductCardModel.LabelGroup) {
    shouldShowWithAction(labelGroup.title.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(labelGroup.title)
        it.determineLabelType(labelGroup.type)
    }
}

private fun Label.determineLabelType(labelGroupType: String) {
    val unifyLabelType = labelGroupType.toUnifyLabelType()

    if (unifyLabelType != -1) setLabelType(unifyLabelType)
    else setCustomLabelType(labelGroupType)
}

private fun String?.toUnifyLabelType(): Int {
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
        else -> -1
    }
}

private fun Label.setCustomLabelType(labelGroupType: String) {
    try {
        trySetCustomLabelType(labelGroupType)
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    }
}

private fun Label.trySetCustomLabelType(labelGroupType: String) {
    unlockFeature = true

    val colorRes = labelGroupType.toUnifyLabelColor()
    val colorHexInt = ContextCompat.getColor(context, colorRes)
    val colorHexString = "#${Integer.toHexString(colorHexInt)}"
    setLabelType(colorHexString)
}

@ColorRes
private fun String?.toUnifyLabelColor(): Int {
    return when(this) {
        TRANSPARENT_BLACK -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        else -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
    }
}

internal fun Typography.initLabelGroup(labelGroup: ProductCardModel.LabelGroup?) {
    if (labelGroup == null) hide()
    else showTypography(labelGroup)
}

private fun Typography.showTypography(labelGroup: ProductCardModel.LabelGroup) {
    shouldShowWithAction(labelGroup.title.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(labelGroup.title)
        it.setTextColor(safeParseColor(labelGroup.type.toUnifyTextColor()))
    }
}

private fun String?.toUnifyTextColor(): String {
    return when(this) {
        TEXT_DARK_ORANGE -> COLOR_TEXT_DARK_ORANGE
        TEXT_DARK_RED -> COLOR_TEXT_DARK_RED
        TEXT_DARK_GREY -> COLOR_TEXT_DARK_GREY
        TEXT_LIGHT_GREY -> COLOR_TEXT_LIGHT_GREY
        else -> this ?: ""
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

internal fun View.expandTouchArea(left: Int, top: Int, right: Int, bottom: Int) {
    val parent = parent

    if (parent is View) {
        val hitRect = Rect()
        getHitRect(hitRect)

        hitRect.left -= left
        hitRect.top -= top
        hitRect.right += right
        hitRect.bottom += bottom

        parent.touchDelegate = TouchDelegate(hitRect, this)
    }
}