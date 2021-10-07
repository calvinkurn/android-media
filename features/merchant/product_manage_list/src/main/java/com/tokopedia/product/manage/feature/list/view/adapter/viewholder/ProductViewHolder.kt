package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageFitCenter
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.quickedit.common.interfaces.ProductCampaignInfoListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_manage_product_list.view.*

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

    private val campaignCountText: Typography? = itemView.findViewById(R.id.tv_manage_product_item_campaign_count)

    override fun bind(product: ProductUiModel) {
        setTitleAndPrice(product)
        showProductStock(product)

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
        itemView.textTitle.text = product.title
        val prices = mutableListOf(product.minPrice?.priceFormatted, product.maxPrice?.priceFormatted).distinct()
        itemView.textPrice.text = prices.joinToString(" - ")
    }

    private fun showProductStock(product: ProductUiModel) {
        product.stock?.run {
            itemView.textStockCount.text = if (this <= MAX_SHOWING_STOCK) {
                getNumberFormatted()
            } else {
                "${MAX_SHOWING_STOCK.getNumberFormatted()}+"
            }
            itemView.textStockCount.show()
            itemView.textStock.show()
        }
    }

    private fun showProductLabel(product: ProductUiModel) {
        itemView.labelBanned.showWithCondition(product.isViolation())
        itemView.labelInactive.showWithCondition(product.isInactive())
        itemView.labelActive.showWithCondition(product.isActive())
    }

    private fun showVariantLabel(product: ProductUiModel) {
        itemView.labelVariant.showWithCondition(product.isVariant())
    }

    private fun showProductButton(product: ProductUiModel) {
        if(product.multiSelectActive) {
            itemView.btnContactCS.hide()
            itemView.btnEditPrice.hide()
            itemView.btnEditStock.hide()
            itemView.btnMoreOptions.hide()
        } else {
            itemView.btnContactCS.showWithCondition(product.isViolation())
            itemView.btnEditPrice.showWithCondition(product.isNotViolation())
            itemView.btnEditStock.showWithCondition(product.isNotViolation())
            itemView.btnMoreOptions.showWithCondition(product.isNotViolation())
        }

        itemView.btnEditPrice.isEnabled = product.hasEditPriceAccess()
    }

    private fun setupButtonStyle(product: ProductUiModel) {
        itemView.btnEditPrice.apply {
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
        itemView.imageStockInformation
            .showWithCondition(product.isEmpty() && product.isNotViolation())
    }

    private fun showProductImage(product: ProductUiModel) {
        loadImageFitCenter(itemView.context, itemView.imageProduct, product.imageUrl)
    }

    private fun setOnClickListeners(product: ProductUiModel) {
        setOnItemClickListener(product)
        setQuickEditBtnListeners(product)

        itemView.checkBoxSelect.setOnClickListener { onClickCheckBox() }
        itemView.btnMoreOptions.setOnClickListener { listener.onClickMoreOptionsButton(product) }
        itemView.imageStockInformation.setOnClickListener { listener.onClickStockInformation() }
        itemView.btnContactCS.setOnClickListener { listener.onClickContactCsButton(product)}
    }

    private fun setQuickEditBtnListeners(product: ProductUiModel) {
        if(product.isVariant()) {
            itemView.btnEditPrice.setOnClickListener { listener.onClickEditVariantPriceButton(product) }
            itemView.btnEditStock.setOnClickListener { listener.onClickEditVariantStockButton(product) }
        } else {
            itemView.btnEditPrice.setOnClickListener { listener.onClickEditPriceButton(product) }
            itemView.btnEditStock.setOnClickListener { listener.onClickEditStockButton(product) }
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
        with(itemView) {
            val clickAction =
                    if (isEnabled) {
                        getComponentOnClickAction(product)
                    } else {
                        null
                    }
            imageProduct?.setOnClickListener(clickAction)
            textTitle?.setOnClickListener(clickAction)
        }
    }

    private fun getComponentOnClickAction(product: ProductUiModel) =
            View.OnClickListener {
                onClickProductItem(product)
            }

    private fun showProductCheckBox(product: ProductUiModel) {
        itemView.checkBoxSelect.isChecked = product.isChecked
        itemView.checkBoxSelect.showWithCondition(product.multiSelectActive)
    }

    private fun showProductTopAdsIcon(product: ProductUiModel) {
        itemView.imageTopAds.showWithCondition(product.hasTopAds())
    }

    private fun showCampaignCountText(product: ProductUiModel) {
        val shouldShowCampaignCount = !product.campaignTypeList.isNullOrEmpty() && product.isCampaign
        campaignCountText?.run {
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
        itemView.checkBoxSelect.apply { isChecked = !isChecked }
    }

    private fun onClickCheckBox() {
        val isChecked = itemView.checkBoxSelect.isChecked
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