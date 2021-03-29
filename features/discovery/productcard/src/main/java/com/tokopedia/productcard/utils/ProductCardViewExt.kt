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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
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

internal fun ImageView.glideClear(context: Context) {
    if (context.isValidGlideContext()) {
        Glide.with(context).clear(this)
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
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.product_card_placeholder_grey)
                .error(R.drawable.product_card_placeholder_grey)
                .into(this)
    }
}
internal fun ImageView.loadImage(url: String?, state: ((Boolean) -> Unit)) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.product_card_placeholder_grey)
                .error(R.drawable.product_card_placeholder_grey)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        state.invoke(false)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        state.invoke(true)
                        return false
                    }
                })
                .into(this)
    }
}
internal fun ImageView.loadImageWithOutPlaceholder(url: String?, state: ((Boolean) -> Unit)) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.product_card_placeholder_grey)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        state.invoke(false)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        state.invoke(true)
                        return false
                    }
                })
                .into(this)
    }
}

internal fun ImageView.loadImageRounded(url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(context)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(getDimensionPixelSize(R.dimen.dp_6)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.product_card_placeholder_grey)
                .error(R.drawable.product_card_placeholder_grey)
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

internal fun ImageView.loadImageTopRightCrop(url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(context)
                .load(url)
                .transform(TopRightCrop())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.product_card_placeholder_grey)
                .error(R.drawable.product_card_placeholder_grey)
                .into(this)
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
            else -> Color.parseColor(this)
        }
    } catch (throwable: Throwable){
        throwable.printStackTrace()
        ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
    }
}

internal fun safeParseColor(color: String): Int {
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

    background.overrideColor(labelBestSellerModel.type)
    text = labelBestSellerModel.title
}

internal fun Drawable.overrideColor(hexColor: String) {
    when (this) {
        is GradientDrawable -> setColor(safeParseColor(hexColor))
        is ShapeDrawable -> paint.color = safeParseColor(hexColor)
        is ColorDrawable -> color = safeParseColor(hexColor)
    }
}

internal fun renderStockBar(progressBarStock: ProgressBarUnify?, textViewStock: Typography?, productCardModel: ProductCardModel) {
    renderStockPercentage(progressBarStock, productCardModel)
    renderStockLabel(textViewStock, productCardModel)
}

private fun renderStockPercentage(progressBarStock: ProgressBarUnify?, productCardModel: ProductCardModel) {
    progressBarStock?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        if (productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
            it.setProgressIcon(
                    icon = ContextCompat.getDrawable(it.context, R.drawable.ic_fire_filled),
                    width = it.context.resources.getDimension(FIRE_WIDTH).toInt(),
                    height = it.context.resources.getDimension(FIRE_HEIGHT).toInt())
        }
        it.progressBarColorType = ProgressBarUnify.COLOR_RED
        it.setValue(productCardModel.stockBarPercentage, true)
    }
}

private fun renderStockLabel(textViewStockLabel: Typography?, productCardModel: ProductCardModel) {
    textViewStockLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        it.text = productCardModel.stockBarLabel

        val color = when {
            productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true) ->
                MethodChecker.getColor(it.context, com.tokopedia.unifyprinciples.R.color.Unify_R600)
            productCardModel.stockBarLabelColor.isNotEmpty() ->
                safeParseColor(productCardModel.stockBarLabelColor)
            else ->
                MethodChecker.getColor(it.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
        }

        it.setTextColor(color)
    }
}