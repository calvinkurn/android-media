package com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee

import android.view.View
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeIconBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeIconUiModel
import com.tokopedia.utils.resources.isDarkMode

class WidgetTransparencyFeeIcon(
    view: View,
    private val actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : BaseWidgetTransparencyFeeAttribute<TransparencyFeeIconUiModel> {

    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_icon
    }

    private val binding = ItemDetailTransparencyFeeIconBinding.bind(view)

    override fun bind(data: TransparencyFeeIconUiModel) {
        setupLabel(data)
    }

    private fun setupLabel(element: TransparencyFeeIconUiModel) {
        val iconUrl = if (binding.root.context.isDarkMode()) {
            element.darkIconUrl
        } else {
            element.iconUrl
        }
        binding.detailIncomeIcon.run {
            loadImage(iconUrl)
            if (element.transparencyFeeInfo.hasTooltip()) {
                setOnClickListener {
                    actionListener.onTransparencyInfoIconClicked(
                        element.transparencyFeeInfo.title,
                        element.transparencyFeeInfo.desc
                    )
                }
            }
        }
    }

}
