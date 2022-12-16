package com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.adapter.viewholder

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductSseSubmissionErrorBinding
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.seller_tokopedia_flash_sale.R

class ProductSseSubmissionErrorItemViewHolder(private val binding: StfsItemProductSseSubmissionErrorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: FlashSaleProductSubmissionProgress.CampaignProductError) {
        binding.apply {
            textProductName.text = uiModel.productName
            textProductSku.text = getString(R.string.stfs_bottomsheet_error_product_submission_sse_item_sku, uiModel.sku)
            textErrorMessage.text = uiModel.message
            imgProduct.loadImage(uiModel.productPicture)
        }
    }

    private fun getString(@StringRes stringRes: Int, vararg value: Any): String {
        return itemView.context.getString(stringRes, *value)
    }
}
