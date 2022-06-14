package com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.ItemShopDiscountManageDiscountGlobalErrorLayoutBinding
import com.tokopedia.shopdiscount.databinding.ItemShopDiscountProductDetailListGlobalErrorLayoutBinding
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountManageDiscountGlobalErrorUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailListGlobalErrorUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopDiscountManageDiscountGlobalErrorViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ShopDiscountManageDiscountGlobalErrorUiModel>(itemView) {
    private val viewBinding: ItemShopDiscountManageDiscountGlobalErrorLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_discount_manage_discount_global_error_layout
    }

    interface Listener {
        fun onGlobalErrorActionClickRetry()
    }

    override fun bind(uiModel: ShopDiscountManageDiscountGlobalErrorUiModel) {
        setGlobalError(uiModel)
    }

    private fun setGlobalError(uiModel: ShopDiscountManageDiscountGlobalErrorUiModel) {
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