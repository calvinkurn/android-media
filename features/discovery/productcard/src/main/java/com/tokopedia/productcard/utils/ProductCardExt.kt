package com.tokopedia.productcard.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
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
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageTopRightCrop
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*
import kotlinx.android.synthetic.main.product_card_grid_layout.view.progressBarStock
import kotlinx.android.synthetic.main.product_card_grid_layout.view.textViewStockLabel
import kotlinx.android.synthetic.main.product_card_list_layout.view.*

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
            setErrorDrawable(R.drawable.product_card_placeholder_grey)
        }
    }
}
internal fun ImageView.loadImage(url: String?, state: ((Boolean) -> Unit)) {
    if (url != null && url.isNotEmpty()) {
        this.loadImage(url) {
            setErrorDrawable(R.drawable.product_card_placeholder_grey)
            listener({ _, _ ->
                state.invoke(true)
            }, {
                state.invoke(false)
            })
        }
    }
}
internal fun ImageView.loadImageWithOutPlaceholder(url: String?, state: ((Boolean) -> Unit)) {
    if (url != null && url.isNotEmpty()) {
        this.loadImage(url) {
            setErrorDrawable(R.drawable.product_card_placeholder_grey)
            setPlaceHolder(-1)
            listener({ _, _ ->
                state.invoke(true)
            }, {
                state.invoke(false)
            })
        }
    }
}

internal fun ImageView.loadImageRounded(url: String?) {
    if (url != null && url.isNotEmpty()) {
        this.loadImage(url) {
            setErrorDrawable(R.drawable.product_card_placeholder_grey)
            centerCrop()
            setRoundedRadius(getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_6).toFloat())
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
        it.setTextColor(labelGroup.type.toUnifyTextColor(context))
    }
}

private fun String?.toUnifyTextColor(context: Context): Int {
    return try{
        when(this) {
            TEXT_DARK_ORANGE -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y400)
            TEXT_DARK_RED -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500)
            TEXT_DARK_GREY -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            TEXT_LIGHT_GREY -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
            TEXT_GREEN -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            else -> Color.parseColor(this)
        }
    } catch (throwable: Throwable){
        throwable.printStackTrace()
        ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
    }
}

internal fun safeParseColor(color: String, defaultColor: Int): Int {
    return try {
        Color.parseColor(color)
    }
    catch (throwable: Throwable) {
        throwable.printStackTrace()
        defaultColor
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

internal fun renderLabelCampaign(
        labelCampaignBackground: ImageView?,
        textViewLabelCampaign: Typography?,
        productCardModel: ProductCardModel
) {
    if (productCardModel.isShowLabelCampaign()) {
        val labelCampaign = productCardModel.getLabelCampaign() ?: return

        labelCampaignBackground?.show()
        labelCampaignBackground?.loadImageTopRightCrop(labelCampaign.imageUrl)

        textViewLabelCampaign?.show()
        textViewLabelCampaign?.text = MethodChecker.fromHtml(labelCampaign.title)
    }
    else {
        labelCampaignBackground?.hide()
        textViewLabelCampaign?.hide()
    }
}

internal fun renderLabelBestSeller(
        labelBestSeller: Typography?,
        productCardModel: ProductCardModel
) {
    labelBestSeller ?: return

    if (productCardModel.isShowLabelBestSeller()) {
        labelBestSeller.initLabelBestSeller(productCardModel.getLabelBestSeller())
    }
    else {
        labelBestSeller.initLabelBestSeller(null)
    }
}

private fun Typography.initLabelBestSeller(labelBestSellerModel: ProductCardModel.LabelGroup?) {
    if (labelBestSellerModel == null) hide()
    else showLabelBestSeller(labelBestSellerModel)
}

private fun Typography.showLabelBestSeller(labelBestSellerModel: ProductCardModel.LabelGroup) {
    show()

    val defaultColor = "#E1AA1D"
    background.overrideColor(labelBestSellerModel.type, defaultColor)
    text = labelBestSellerModel.title
}

internal fun Drawable.overrideColor(hexColor: String, defaultColor: String) {
    when (this) {
        is GradientDrawable -> setColor(safeParseColor(hexColor, Color.parseColor(defaultColor)))
        is ShapeDrawable -> paint.color = safeParseColor(hexColor, Color.parseColor(defaultColor))
        is ColorDrawable -> color = safeParseColor(hexColor, Color.parseColor(defaultColor))
    }
}

internal fun renderStockBar(progressBarStock: ProgressBarUnify?, textViewStock: Typography?, productCardModel: ProductCardModel) {
    renderStockPercentage(progressBarStock, productCardModel)
    renderStockLabel(textViewStock, productCardModel)
}

private fun renderStockPercentage(progressBarStock: ProgressBarUnify?, productCardModel: ProductCardModel) {
    progressBarStock?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        progressBarStock.setProgressIcon(icon = null)
        if (productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
            it.setProgressIcon(
                    icon = ContextCompat.getDrawable(it.context, R.drawable.product_card_ic_fire_filled),
                    width = it.context.resources.getDimension(FIRE_WIDTH).toInt(),
                    height = it.context.resources.getDimension(FIRE_HEIGHT).toInt())
        }
        it.progressBarColorType = ProgressBarUnify.COLOR_RED
        it.setValue(productCardModel.stockBarPercentage, false)
    }
}

private fun renderStockLabel(textViewStockLabel: Typography?, productCardModel: ProductCardModel) {
    textViewStockLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        it.text = productCardModel.stockBarLabel

        val color = when {
            productCardModel.stockBarLabelColor.isNotEmpty() ->
                safeParseColor(
                        productCardModel.stockBarLabelColor,
                        ContextCompat.getColor(it.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                )
            else ->
                MethodChecker.getColor(it.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
        }

        it.setTextColor(color)
    }
}