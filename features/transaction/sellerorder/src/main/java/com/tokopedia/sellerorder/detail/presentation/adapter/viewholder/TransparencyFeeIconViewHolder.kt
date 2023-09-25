package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeIconBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeIconUiModel
import com.tokopedia.utils.resources.isDarkMode

class TransparencyFeeIconViewHolder(
    view: View?,
    private val actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeIconUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_icon
    }

    private val binding = ItemDetailTransparencyFeeIconBinding.bind(itemView)

    override fun bind(element: TransparencyFeeIconUiModel) {
        setupLabel(element)
    }

    private fun setupLabel(element: TransparencyFeeIconUiModel) {
        val iconUrl = if (itemView.context.isDarkMode()) element.darkIconUrl else element.iconUrl
        binding.detailIncomeIcon.run {
            loadImage(iconUrl)
            setOnClickListener {
                actionListener.onTransparencyInfoIconClicked(
                    element.transparencyFeeInfo.title,
                    element.transparencyFeeInfo.desc
                )
            }
        }
    }

}
