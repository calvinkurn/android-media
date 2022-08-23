package com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.ItemShopDiscountProductDetailListGlobalErrorLayoutBinding
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailListGlobalErrorUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
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
        setGlobalError()
    }

    private fun setGlobalError() {
        viewBinding?.layoutGlobalError?.apply {
            errorSecondaryAction.hide()
            (errorAction as? UnifyButton)?.apply {
                buttonVariant = UnifyButton.Variant.GHOST
                text = getString(R.string.product_detail_error_button_text)
            }
            errorTitle.text = getString(R.string.product_detail_error_title)
            errorDescription.apply {
                layoutParams.apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                setMargin(0, 0, 0, 16.toPx())
                text = getString(R.string.product_detail_error_description)
            }
            errorIllustration.setImageDrawable(
                MethodChecker.getDrawable(
                    context,
                    com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
                )
            )
            setActionClickListener {
                listener.onGlobalErrorActionClickRetry()
            }
        }
    }

}