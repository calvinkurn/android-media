package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.databinding.HomeComponentBestSellerChipBinding
import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.BestSellerChipListener
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ChipsUnify

internal class BestSellerChipViewHolder(
    itemView: View,
    private val listener: BestSellerChipListener,
): RecyclerView.ViewHolder(itemView) {

    private var binding: HomeComponentBestSellerChipBinding? by viewBinding()

    fun bind(element: BestSellerChipProductDataModel) {
        binding?.homeComponentBestSellerChip?.run {
            chipText = element.title
            chipType = getChipType(element.chip)
            addOnImpressionListener(element.chip.impressHolder) {
                listener.onChipImpressed(element.chip)
            }
            setOnClickListener {
                listener.onChipClicked(element.chip)
            }
        }
    }

    private fun getChipType(element: BestSellerChipDataModel) =
        if (element.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_best_seller_chip
    }
}
