package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.*
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
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
    }

    internal fun bind(optionData: InspirationCardOptionDataView) {
        setChipType(optionData)
        if (optionData.img.isNotEmpty()) bindIcon(optionData.img)
        else bindIconColor(optionData)
        bindOptionTitle(optionData)
        bindListener(optionData)
    }

    private fun setChipType(optionData: InspirationCardOptionDataView) {
        when(optionData.inspirationCardType) {
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

    private fun bindIconColor(optionData: InspirationCardOptionDataView) {
        itemView.inspirationCardOptionChip?.shouldShowWithAction(optionData.hexColor.isNotEmpty()) {
            val gradientDrawable = createColorSampleDrawable(itemView.context, optionData.hexColor)
            itemView.inspirationCardOptionChip?.chip_image_icon?.setImageDrawable(gradientDrawable)
            itemView.inspirationCardOptionChip?.chip_image_icon?.visibility = View.VISIBLE
        }
    }

    private fun bindOptionTitle(optionData: InspirationCardOptionDataView) {
        itemView.inspirationCardOptionChip?.shouldShowWithAction(optionData.text.isNotEmpty()) {
            itemView.inspirationCardOptionChip?.chip_text?.text = MethodChecker.fromHtml(optionData.text)
        }
    }

    private fun bindListener(optionData: InspirationCardOptionDataView) {
        itemView.inspirationCardOptionChip?.setOnClickListener {
            //Tracker
            inspirationCardListener.onInspirationCardOptionClicked(optionData)
        }
    }

}