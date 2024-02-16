package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.util.setupCardDarkMode
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailProductCardItemBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailNonProductBundleCardViewHolder(
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool,
    itemView: View?
) : AbstractViewHolder<NonProductBundleUiModel>(itemView),
    BmgmAddOnViewHolder.Listener {

    companion object {
        val RES_LAYOUT = R.layout.detail_product_card_item
    }

    private val binding by viewBinding<DetailProductCardItemBinding>()
    private var productDetailViewHolder: PartialSomDetailNonProductBundleDetailViewHolder? = null
    private var addOnSummaryViewHolder: BmgmAddOnSummaryViewHolder? = null
    private var partialBmgmAddonSummaryBinding: PartialBmgmAddOnSummaryBinding? = null

    override fun bind(element: NonProductBundleUiModel) {
        productDetailViewHolder = getProductDetailViewHolder()
        productDetailViewHolder?.bind(element.product)
        setupDividerAddonSummary(element.addOnSummaryUiModel)
        setupAddonSection(element.addOnSummaryUiModel)
        setupContainerBackground()
    }

    private fun setupContainerBackground() {
        binding?.detailProductCardItem?.setupCardDarkMode()
    }

    private fun setupDividerAddonSummary(addOnSummaryUiModel: AddOnSummaryUiModel?) {
        binding?.dividerAddOn?.showWithCondition(addOnSummaryUiModel != null)
    }

    private fun getProductDetailViewHolder(): PartialSomDetailNonProductBundleDetailViewHolder {
        return productDetailViewHolder ?: PartialSomDetailNonProductBundleDetailViewHolder(
            binding?.layoutProductDetail, actionListener
        )
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
        actionListener?.onCopyAddOnDescription(label, description)
    }

    override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
        actionListener?.onAddOnsBmgmExpand(isExpand, addOnsIdentifier)
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
        actionListener?.onAddOnsInfoLinkClicked(infoLink, type)
    }

    override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {}

    private fun setupAddonSection(addOnSummaryUiModel: AddOnSummaryUiModel?) {
        val addonsViewStub: View = itemView.findViewById(R.id.layoutProductAddOn)
        if (addOnSummaryUiModel?.addonItemList?.isNotEmpty() == true) {
            binding?.containerLayoutProductAddOn?.show()
            if (addonsViewStub is ViewStub) addonsViewStub.inflate() else addonsViewStub.show()
            setupAddonsBinding()
            addOnSummaryViewHolder =
                partialBmgmAddonSummaryBinding?.let {
                    BmgmAddOnSummaryViewHolder(
                        this,
                        it,
                        recyclerViewSharedPool
                    )
                }
            addOnSummaryViewHolder?.bind(addOnSummaryUiModel)
        } else {
            addonsViewStub.hide()
            binding?.containerLayoutProductAddOn?.hide()
        }
    }

    private fun setupAddonsBinding() {
        if (partialBmgmAddonSummaryBinding == null) {
            partialBmgmAddonSummaryBinding =
                PartialBmgmAddOnSummaryBinding.bind(this.itemView.findViewById(R.id.layoutProductAddOn))
        }
    }
}
