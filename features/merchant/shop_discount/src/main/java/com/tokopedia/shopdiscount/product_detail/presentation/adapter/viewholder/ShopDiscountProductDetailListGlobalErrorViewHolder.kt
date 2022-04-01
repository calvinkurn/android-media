package com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.ItemShopDiscountProductDetailListGlobalErrorLayoutBinding
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailListGlobalErrorUiModel
import com.tokopedia.utils.view.binding.viewBinding

open class ShopDiscountProductDetailListGlobalErrorViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ShopDiscountProductDetailListGlobalErrorUiModel>(itemView) {
    private val viewBinding: ItemShopDiscountProductDetailListGlobalErrorLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_discount_product_detail_list_global_error_layout
    }

    interface Listener {
        fun onGlobalErrorActionClickRetry()
    }

    override fun bind(uiModel: ShopDiscountProductDetailListGlobalErrorUiModel) {
        setGlobalError(uiModel)
    }

    private fun setGlobalError(uiModel: ShopDiscountProductDetailListGlobalErrorUiModel) {
        viewBinding?.layoutGlobalError?.apply {
            errorSecondaryAction.hide()
            val message = ErrorHandler.getErrorMessage(context, uiModel.throwable)
            errorTitle.text = message
            setActionClickListener {
                listener.onGlobalErrorActionClickRetry()
            }
        }
    }

}