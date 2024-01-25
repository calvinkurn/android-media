package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemGwpMiniCartEditorProductBinding
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.GwpMiniCartEditorAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.campaign.utils.extension.doOnTextChanged
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class GwpMiniCartEditorProductViewHolder(
    itemView: View,
    private val listener: GwpMiniCartEditorAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.ProductUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_gwp_mini_cart_editor_product
        private const val ADJUST_QTY_DEBOUNCE = 600L
    }

    private val binding by lazy {
        ItemGwpMiniCartEditorProductBinding.bind(itemView)
    }
    private var adjustQtyJob: Job? = null
    private val coroutineScope by lazy {
        CoroutineScope(Dispatchers.Default)
    }

    override fun bind(element: BmgmMiniCartVisitable.ProductUiModel) {
        showProduct(element)
        setupQuantityEditor(element)
        setupDeleteButton(element)
        showDivider(element.ui.showDivider)
    }

    private fun showDivider(showDivider: Boolean) {
        if (showDivider) {
            binding.dividerBottom.visible()
        } else {
            binding.dividerBottom.invisible()
        }
    }

    private fun showProduct(element: BmgmMiniCartVisitable.ProductUiModel) {
        with(binding) {
            imgGwpProduct.loadImage(element.productImage)
            tvGwpProductName.text = element.productName
            tvGwpProductPrice.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(element.finalPrice, false)
        }
    }

    private fun setupQuantityEditor(element: BmgmMiniCartVisitable.ProductUiModel) {
        with(binding) {
            gwpQtyEditor.editText.doOnTextChanged {
                adjustQuantity(element, it)
            }
            gwpQtyEditor.minValue = element.minQuantity
            gwpQtyEditor.maxValue = element.maxQuantity
            val currentValue = gwpQtyEditor.getValue()
            if (currentValue != element.quantity) {
                gwpQtyEditor.setValue(element.quantity)
            }
        }
    }

    private fun setupDeleteButton(element: BmgmMiniCartVisitable.ProductUiModel) {
        with(binding.btnGwpQtyDelete) {
            setOnClickListener {
                deleteCart(element)
            }
            visible()
        }
    }

    private fun adjustQuantity(element: BmgmMiniCartVisitable.ProductUiModel, qty: CharSequence?) {
        adjustQtyJob?.cancel()
        adjustQtyJob = coroutineScope.launch {
            delay(ADJUST_QTY_DEBOUNCE)

            val newQty = qty.toString().replace(".", "").toIntOrZero()
            val prevQty = element.quantity
            if (newQty <= Int.ZERO) {
                withContext(Dispatchers.Main) {
                    setOnInvalidMinQty()
                }
                return@launch
            } else {
                withContext(Dispatchers.Main) {
                    binding.gwpQtyEditor.errorMessage.gone()
                }
            }
            if (newQty != prevQty) {
                listener.onCartItemQuantityChanged(element, newQty)
            }
        }
    }

    private fun setOnInvalidMinQty() {
        binding.gwpQtyEditor.errorMessage.visible()
        binding.gwpQtyEditor.errorMessage.text = itemView.context.getString(R.string.qty_error_empty_message)
    }

    private fun deleteCart(element: BmgmMiniCartVisitable.ProductUiModel) {
        adjustQtyJob?.cancel()
        adjustQtyJob = coroutineScope.launch {
            delay(ADJUST_QTY_DEBOUNCE)

            withContext(Dispatchers.Main) {
                listener.onCartItemQuantityDeleted(element)
            }
        }
    }
}