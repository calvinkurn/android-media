package com.tokopedia.shop.note.view.adapter.viewholder

import android.text.Spanned
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.note.data.ShopNoteBottomSheetUiModel
import com.tokopedia.shop.common.util.CustomAnimation
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography


class ShopNoteBottomSheetViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_note_bottom_sheet
        const val ANIMATION_DURATION = 250L
    }

    private var tpTitle: Typography? = null
    private var tpDescription: Typography? = null
    private var ivChevron: ImageView? = null
    private var divider: DividerUnify? = null
    private var mHeight = 0

    private fun setupUi(title: String, description: Spanned, isTheLastPosition: Boolean) {
        itemView.apply {
            initialization(title, description, isTheLastPosition)
            setUnknownHeight()
        }.setOnClickListener {
            if (tpDescription?.isShown == true) {
                hideDescription(isTheLastPosition)
            } else {
                showDescription(isTheLastPosition)
            }
        }
    }

    private fun hideDescription(isTheLastPosition: Boolean) {
        slideDownAnimation()
        setIconColor(com.tokopedia.iconunify.R.drawable.iconunify_chevron_down)
        if (isTheLastPosition) {
            divider?.gone()
        }
    }

    private fun showDescription(isTheLastPosition: Boolean) {
        slideUpAnimation()
        setIconColor(com.tokopedia.iconunify.R.drawable.iconunify_chevron_up)
        if (isTheLastPosition) {
            divider?.show()
        }
    }

    private fun initialization(title: String, description: Spanned, isTheLastPosition: Boolean) {
        itemView.apply {
            tpTitle = findViewById(R.id.tp_title)
            tpDescription = findViewById(R.id.tp_description)
            ivChevron = findViewById(R.id.iv_chevron)
            divider = findViewById(R.id.divider)

            tpTitle?.text = title
            tpDescription?.text = description

            setIconColor(com.tokopedia.iconunify.R.drawable.iconunify_chevron_down)
            if (isTheLastPosition && tpDescription?.isShown != true) {
                divider?.gone()
            }
        }
    }

    private fun setUnknownHeight() {
        tpDescription?.post {
            tpDescription?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            mHeight = tpDescription?.measuredHeight ?: 0
        }
    }

    private fun slideDownAnimation() {
        val animation = CustomAnimation(tpDescription as View, ANIMATION_DURATION, CustomAnimation.COLLAPSE)
        mHeight = animation.height
        tpDescription?.startAnimation(animation)
    }

    private fun slideUpAnimation() {
        val animation = CustomAnimation(tpDescription as View, ANIMATION_DURATION, CustomAnimation.EXPAND)
        animation.height = mHeight
        tpDescription?.startAnimation(animation)
    }

    private fun setIconColor(resourceId: Int) {
        AppCompatResources.getDrawable(itemView.context, resourceId)?.let {
            val backgroundDrawable = DrawableCompat.wrap(it).mutate()
            DrawableCompat.setTint(backgroundDrawable, ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700))
            ivChevron?.loadImage(backgroundDrawable)
            ivChevron?.layoutParams?.width = 36.toPx()
            ivChevron?.layoutParams?.height = 21.toPx()
        }
    }

    fun bind(model: ShopNoteBottomSheetUiModel) {
        setupUi(
                title = model.title,
                description = MethodChecker.fromHtml(model.description),
                isTheLastPosition = model.isTheLastPosition
        )
    }
}