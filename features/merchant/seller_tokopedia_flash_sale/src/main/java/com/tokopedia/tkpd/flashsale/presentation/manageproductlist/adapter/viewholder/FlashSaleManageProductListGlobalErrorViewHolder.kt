package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.LayoutGlobalErrorCampaignCommonBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListGlobalErrorItem

class FlashSaleManageProductListGlobalErrorViewHolder(
    private val binding: LayoutGlobalErrorCampaignCommonBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {


    interface Listener {
        fun onGlobalErrorActionClickRetry()
    }

    private val layoutGlobalError by lazy {
        binding.layoutGlobalError
    }

    fun bind(model: FlashSaleManageProductListGlobalErrorItem) {
        setGlobalError(model)
    }

    private fun setGlobalError(uiModel: FlashSaleManageProductListGlobalErrorItem) {
        layoutGlobalError.apply {
            errorSecondaryAction.hide()
            val message = ErrorHandler.getErrorMessage(context, uiModel.throwable)
            errorTitle.text = message
            setActionClickListener {
                listener.onGlobalErrorActionClickRetry()
            }
        }
    }

}