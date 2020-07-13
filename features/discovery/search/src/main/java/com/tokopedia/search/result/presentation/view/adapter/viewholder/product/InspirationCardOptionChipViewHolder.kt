package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCardOptionViewModel
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.search.utils.createColorSampleDrawable
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.search_result_product_inspiration_card_option_chip_layout.view.*

class InspirationCardOptionChipViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.search_result_product_inspiration_card_option_chip_layout
        const val TYPE_ANNOTATION = "annotation"
        const val TYPE_CATEGORY = "category"
        const val TYPE_GUIDED = "guided"
    }

    internal fun bind(option: InspirationCardOptionViewModel) {
        setChipType(option)
        if (option.img.isNotEmpty()) bindIcon(option.img)
        else bindIconColor(option)
        bindOptionTitle(option)
        bindListener(option)
    }

    private fun setChipType(option: InspirationCardOptionViewModel) {
        when(option.inspirationCardType) {
            TYPE_ANNOTATION, TYPE_CATEGORY -> itemView.inspirationCardOptionChip?.chipType = ChipsUnify.TYPE_NORMAL
            TYPE_GUIDED -> itemView.inspirationCardOptionChip?.chipType = ChipsUnify.TYPE_ALTERNATE
            else -> itemView.inspirationCardOptionChip?.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun bindIcon(imageUrl: String) {
        itemView.inspirationCardOptionChip?.chip_image_icon?.type = ImageUnify.TYPE_RECT
        itemView.inspirationCardOptionChip?.chip_image_icon?.setImageUrl(imageUrl)
        itemView.inspirationCardOptionChip?.chip_image_icon?.visibility = View.VISIBLE
    }

    private fun bindIconColor(option: InspirationCardOptionViewModel) {
        itemView.inspirationCardOptionChip?.shouldShowWithAction(option.color.isNotEmpty()) {
            val gradientDrawable = createColorSampleDrawable(itemView.context, option.color)
            itemView.inspirationCardOptionChip?.chip_image_icon?.setImageDrawable(gradientDrawable)
            itemView.inspirationCardOptionChip?.chip_image_icon?.visibility = View.VISIBLE
        }
    }

    private fun bindOptionTitle(option: InspirationCardOptionViewModel) {
        itemView.inspirationCardOptionChip?.shouldShowWithAction(option.text.isNotEmpty()) {
            itemView.inspirationCardOptionChip?.chip_text?.text = MethodChecker.fromHtml(option.text)
        }
    }

    private fun bindListener(option: InspirationCardOptionViewModel) {
        itemView.inspirationCardOptionChip?.setOnClickListener {
            //Tracker
            inspirationCardListener.onInspirationCardOptionClicked(option)
        }
    }

}