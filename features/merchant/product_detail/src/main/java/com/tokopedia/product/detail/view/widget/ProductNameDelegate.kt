package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.product.detail.common.data.model.pdplayout.LabelIcons
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.databinding.ItemDynamicProductContentBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.text.style.ImageSpanCenterVertical

/**
 * Created by yovi.putra on 02/02/24"
 * Project name: android-tokopedia-core
 **/

internal class ProductNameDelegate(
    private val typography: Typography?
) {
    companion object {
        private val KVI_ICON_HEIGHT = 16.toPx()
    }

    private val context
        get() = typography?.context

    private val glideApp by lazyThreadSafetyNone {
        GlideApp
            .with(context ?: return@lazyThreadSafetyNone null)
            .asDrawable()
    }

    private val iconPlaceholder by lazyThreadSafetyNone { IconLoaded(null, false) }

    // avoid blinking the label icons when scrolling up and down
    private var previousTitle: String = ""
    private var previousLabelIcons: List<LabelIcons> = emptyList()

    fun setTitle(title: String, labelIcons: List<LabelIcons> = emptyList()) {
        if (!hasChanged(title = title, labelIcons = labelIcons)) return

        if (labelIcons.isEmpty()) {
            setProductNameText(title = title)
        } else {
            setProductNameWithIcon(labelIcons = labelIcons, title = title)
        }
    }

    private fun hasChanged(title: String, labelIcons: List<LabelIcons> = emptyList()): Boolean {
        val hasChanged = previousTitle != title || previousLabelIcons != labelIcons

        if (hasChanged) {
            previousTitle = title
            previousLabelIcons = labelIcons
        }

        return hasChanged
    }

    private fun setProductNameText(title: String) {
        withContext {
            val text = title.parseAsHtmlLink(it, false)
            setText(text = text)
        }
    }

    private fun setProductNameWithIcon(labelIcons: List<LabelIcons>, title: String) = withContext {
        val stringBuilder = SpannableStringBuilder().apply {
            append(title.parseAsHtmlLink(it, false))
        }

        setText(text = stringBuilder)
        stringBuilder.processLabelIcons(labelIcons = labelIcons)
    }

    private fun SpannableStringBuilder.processLabelIcons(
        labelIcons: List<LabelIcons>
    ) {
        val initialMapIcons: MutableMap<String, IconLoaded> = labelIcons
            .associate { it.iconURL to iconPlaceholder }
            .toMutableMap()

        prepareLoadIcons(mapIcons = initialMapIcons)
    }

    private fun SpannableStringBuilder.prepareLoadIcons(
        mapIcons: MutableMap<String, IconLoaded>
    ) {
        mapIcons.keys.forEach { url ->
            loadIcon(url = url) {
                val iconLoaded = mapIcons[url] ?: iconPlaceholder
                mapIcons[url] = iconLoaded.copy(this, true)
                renderLabelIcons(mapIcons = mapIcons)
            }
        }
    }

    private fun SpannableStringBuilder.renderLabelIcons(mapIcons: MutableMap<String, IconLoaded>) {
        // Exit if all icons have not finished load
        if (!allIconsLoaded(mapIcons)) return

        mapIcons.getIcons().forEach { resource ->
            resource.resizeLabelIconSpec()
            setLabelIconSpan(drawable = resource)
            setText(text = this)
        }
    }

    private fun MutableMap<String, IconLoaded>.getIcons() = values
        // reversed this, because each logo put from index 0 always
        .reversed()
        .asSequence()
        .map { it.first }
        .filterNotNull()

    private fun allIconsLoaded(mapIcons: MutableMap<String, IconLoaded>): Boolean {
        // ensure all icons has loaded
        return mapIcons.values.all { it.second }
    }

    private fun Drawable.resizeLabelIconSpec() {
        val ratio = intrinsicWidth.toFloat() / intrinsicHeight.toFloat()
        val right = KVI_ICON_HEIGHT * ratio
        setBounds(Int.ZERO, Int.ZERO, right.toInt(), KVI_ICON_HEIGHT)
    }

    private fun SpannableStringBuilder.setLabelIconSpan(drawable: Drawable) {
        val imageSpan = ImageSpanCenterVertical(drawable)

        insert(Int.ZERO, "  ")
        setSpan(imageSpan, Int.ZERO, Int.ONE, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private inline fun loadIcon(url: String, crossinline onLoaded: Drawable?.() -> Unit) {
        glideApp
            ?.load(url)
            ?.into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    onLoaded(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    onLoaded(null)
                }
            })
    }

    private inline fun withContext(block: (Context) -> Unit) {
        val mContext = context ?: return
        block(mContext)
    }

    private fun setText(text: SpannableStringBuilder) {
        typography?.text = text
    }

    private fun setText(text: CharSequence?) {
        typography?.text = text
    }
}

/**
 * simplify by using typealias to store drawable & status after icon loaded
 */
private typealias IconLoaded = Pair<Drawable?, Boolean>

internal fun ItemDynamicProductContentBinding.productNameDelegate() =
    lazyThreadSafetyNone {
        ProductNameDelegate(typography = productName)
    }
