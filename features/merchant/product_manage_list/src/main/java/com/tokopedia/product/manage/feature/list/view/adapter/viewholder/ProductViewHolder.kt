package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageFitCenter
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.quickedit.common.interfaces.ProductCampaignInfoListener
import com.tokopedia.product.manage.databinding.ItemManageProductListBinding
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class ProductViewHolder(
    view: View,
    private val listener: ProductViewHolderView,
    private val campaignListener: ProductCampaignInfoListener
): AbstractViewHolder<ProductUiModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list
        const val MAX_SHOWING_STOCK = 999_999
    }

    private val binding by viewBinding<ItemManageProductListBinding>()

    override fun bind(product: ProductUiModel) {
        setTitleAndPrice(product)
        showProductStock(product)

        showProductTicker(product)
        showProductLabel(product)
        showVariantLabel(product)

        setupButtonStyle(product)
        showProductButton(product)

        showProductImage(product)
        showStockHintImage(product)
        showProductCheckBox(product)
        showProductTopAdsIcon(product)
        showCampaignCountText(product)

        setOnClickListeners(product)
    }

    private fun setTitleAndPrice(product: ProductUiModel) {
        binding?.textTitle?.text = product.title
        val prices = mutableListOf(product.minPrice?.priceFormatted, product.maxPrice?.priceFormatted).distinct()
        binding?.textPrice?.text = prices.joinToString(" - ")
    }

    private fun showProductTicker(product: ProductUiModel) {
        binding?.tickerProductManageViolation?.showWithCondition(product.isPending())
    }

    private fun showProductStock(product: ProductUiModel) {
        product.stock?.run {
            binding?.textStockCount?.text = if (this <= MAX_SHOWING_STOCK) {
                getNumberFormatted()
            } else {
                "${MAX_SHOWING_STOCK.getNumberFormatted()}+"
            }
            binding?.textStockCount?.show()
            binding?.textStock?.show()
        }
    }

    private fun showProductLabel(product: ProductUiModel) {
        binding?.labelBanned?.showWithCondition(product.isViolation())
        binding?.labelInactive?.showWithCondition(product.isInactive())
        binding?.labelActive?.showWithCondition(product.isActive())
    }

    private fun showVariantLabel(product: ProductUiModel) {
        binding?.labelVariant?.showWithCondition(product.isVariant())
    }

    private fun showProductButton(product: ProductUiModel) {
        if(product.multiSelectActive) {
            binding?.btnContactCS?.hide()
            binding?.btnEditPrice?.hide()
            binding?.btnEditStock?.hide()
            binding?.btnMoreOptions?.hide()
        } else {
            binding?.btnContactCS?.run {
                showWithCondition(product.isViolation() || product.isPending())
                when {
                    product.isViolation() -> {
                        text = getString(R.string.product_manage_contact_cs)
                    }
                    product.isPending() -> {
                        text = getString(R.string.product_manage_violation_button_text)
                    }
                }
            }
            binding?.btnEditPrice?.showWithCondition(product.isNotViolation())
            binding?.btnEditStock?.showWithCondition(product.isNotViolation())
            binding?.btnMoreOptions?.showWithCondition(product.isNotViolation())
        }

        binding?.btnEditPrice?.isEnabled = product.hasEditPriceAccess()
    }

    private fun setupButtonStyle(product: ProductUiModel) {
        binding?.btnEditPrice?.run {
            if (product.hasEditPriceAccess()) {
                buttonType = UnifyButton.Type.ALTERNATE
                buttonVariant = UnifyButton.Variant.GHOST
            } else {
                buttonType = UnifyButton.Type.MAIN
                buttonVariant = UnifyButton.Variant.FILLED
            }
        }
    }

    private fun showStockHintImage(product: ProductUiModel) {
        binding?.imageStockInformation
            ?.showWithCondition(product.isEmpty() && product.isNotViolation())
    }

    private fun showProductImage(product: ProductUiModel) {
        binding?.imageProduct?.let {
            loadImageFitCenter(itemView.context, it, product.imageUrl)
        }
    }

    private fun setOnClickListeners(product: ProductUiModel) {
        setOnItemClickListener(product)
        setQuickEditBtnListeners(product)

        binding?.checkBoxSelect?.setOnClickListener { onClickCheckBox() }
        binding?.btnMoreOptions?.setOnClickListener { listener.onClickMoreOptionsButton(product) }
        binding?.imageStockInformation?.setOnClickListener { listener.onClickStockInformation() }
        binding?.btnContactCS?.setOnClickListener { listener.onClickContactCsButton(product)}
    }

    private fun setQuickEditBtnListeners(product: ProductUiModel) {
        if(product.isVariant()) {
            binding?.btnEditPrice?.setOnClickListener { listener.onClickEditVariantPriceButton(product) }
            binding?.btnEditStock?.setOnClickListener { listener.onClickEditVariantStockButton(product) }
        } else {
            binding?.btnEditPrice?.setOnClickListener { listener.onClickEditPriceButton(product) }
            binding?.btnEditStock?.setOnClickListener { listener.onClickEditStockButton(product) }
        }
    }

    private fun setOnItemClickListener(product: ProductUiModel) {
        if(product.hasEditProductAccess()) {
            itemView.setOnClickListener {
                if (product.multiSelectActive) {
                    toggleCheckBox()
                    onClickCheckBox()
                }
            }
            setComponentsOnItemClickListener(product, !product.multiSelectActive)
        } else {
            itemView.setOnClickListener(null)
            setComponentsOnItemClickListener(product, false)
        }
    }

    private fun setComponentsOnItemClickListener(product: ProductUiModel, isEnabled: Boolean) {
        binding?.run {
            val clickAction =
                    if (isEnabled) {
                        getComponentOnClickAction(product)
                    } else {
                        null
                    }
            imageProduct.setOnClickListener(clickAction)
            textTitle.setOnClickListener(clickAction)
        }
    }

    private fun getComponentOnClickAction(product: ProductUiModel) =
            View.OnClickListener {
                onClickProductItem(product)
            }

    private fun showProductCheckBox(product: ProductUiModel) {
        binding?.checkBoxSelect?.isChecked = product.isChecked
        binding?.checkBoxSelect?.showWithCondition(product.multiSelectActive)
    }

    private fun showProductTopAdsIcon(product: ProductUiModel) {
        binding?.imageTopAds?.showWithCondition(product.hasTopAds())
    }

    private fun showCampaignCountText(product: ProductUiModel) {
        val shouldShowCampaignCount = !product.campaignTypeList.isNullOrEmpty() && product.isCampaign
        binding?.tvManageProductItemCampaignCount?.run {
            showWithCondition(shouldShowCampaignCount)
            if (shouldShowCampaignCount) {
                text = String.format(getString(com.tokopedia.product.manage.common.R.string.product_manage_campaign_count), product.getCampaignTypeCount())
                setOnClickListener {
                    product.campaignTypeList?.let {
                        campaignListener.onClickCampaignInfo(it)
                    }
                }
            }
        }
    }

    private fun toggleCheckBox() {
        binding?.checkBoxSelect?.run {
            isChecked = !isChecked
        }
    }

    private fun onClickCheckBox() {
        val isChecked = binding?.checkBoxSelect?.isChecked == true
        listener.onClickProductCheckBox(isChecked, adapterPosition)
    }

    private fun onClickProductItem(product: ProductUiModel) {
        if(product.isNotViolation()) {
            listener.onClickProductItem(product)
        }
    }

    interface ProductViewHolderView {
        fun onClickStockInformation()
        fun onClickMoreOptionsButton(product: ProductUiModel)
        fun onClickProductItem(product: ProductUiModel)
        fun onClickProductCheckBox(isChecked: Boolean, position: Int)
        fun onClickEditPriceButton(product: ProductUiModel)
        fun onClickEditStockButton(product: ProductUiModel)
        fun onClickEditVariantPriceButton(product: ProductUiModel)
        fun onClickEditVariantStockButton(product: ProductUiModel)
        fun onClickContactCsButton(product: ProductUiModel)
    }
}