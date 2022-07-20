package com.tokopedia.search.result.product.inspirationwidget.card

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_ANNOTATION
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_CATEGORY
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_GUIDED
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInspirationCardOptionChipLayoutBinding
import com.tokopedia.search.utils.createColorSampleDrawable
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class InspirationCardOptionChipViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.search_result_product_inspiration_card_option_chip_layout
    }

    private var binding: SearchResultProductInspirationCardOptionChipLayoutBinding? by viewBinding()

    internal fun bind(optionData: InspirationCardOptionDataView) {
        setChipType(optionData)
        if (optionData.img.isNotEmpty()) bindIcon(optionData.img)
        else bindIconColor(optionData)
        bindOptionTitle(optionData)
        bindListener(optionData)
    }

    private fun setChipType(optionData: InspirationCardOptionDataView) {
        when(optionData.inspirationCardType) {
            TYPE_ANNOTATION, TYPE_CATEGORY -> binding?.inspirationCardOptionChip?.chipType = ChipsUnify.TYPE_NORMAL
            TYPE_GUIDED -> binding?.inspirationCardOptionChip?.chipType = ChipsUnify.TYPE_ALTERNATE
            else -> binding?.inspirationCardOptionChip?.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun bindIcon(imageUrl: String) {
        binding?.inspirationCardOptionChip?.chip_image_icon?.let {
            it.type = ImageUnify.TYPE_RECT
            it.setImageUrl(imageUrl)
            it.visibility = View.VISIBLE
        }
    }

    private fun bindIconColor(optionData: InspirationCardOptionDataView) {
        binding?.inspirationCardOptionChip?.let {
            it.shouldShowWithAction(optionData.hexColor.isNotEmpty()) {
                val gradientDrawable = createColorSampleDrawable(itemView.context, optionData.hexColor)
                it.chip_image_icon.setImageDrawable(gradientDrawable)
                it.chip_image_icon.visibility = View.VISIBLE
            }
        }
    }

    private fun bindOptionTitle(optionData: InspirationCardOptionDataView) {
        binding?.inspirationCardOptionChip?.let {
            it.shouldShowWithAction(optionData.text.isNotEmpty()) {
                it.chip_text.text = MethodChecker.fromHtml(optionData.text)
            }
        }
    }

    private fun bindListener(optionData: InspirationCardOptionDataView) {
        binding?.inspirationCardOptionChip?.setOnClickListener {
            //Tracker
            inspirationCardListener.onInspirationCardOptionClicked(optionData)
        }
    }

}