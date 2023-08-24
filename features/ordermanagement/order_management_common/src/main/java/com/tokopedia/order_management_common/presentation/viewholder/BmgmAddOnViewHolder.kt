package com.tokopedia.order_management_common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.databinding.ItemBmgmDetailAddOnBinding
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.widget.BmgmAddOnDescriptionWidget
import com.tokopedia.order_management_common.util.stripLastDot
import com.tokopedia.utils.view.binding.viewBinding

class BmgmAddOnViewHolder(
    itemView: View?,
    private val listener: Listener
) : AbstractViewHolder<AddOnSummaryUiModel.AddonItemUiModel>(itemView),
    BmgmAddOnDescriptionWidget.Listener {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_detail_add_on
        const val MAX_RECYCLED_VIEWS = 10
    }

    private val binding by viewBinding<ItemBmgmDetailAddOnBinding>()
    private var element: AddOnSummaryUiModel.AddonItemUiModel? = null

    override fun bind(element: AddOnSummaryUiModel.AddonItemUiModel) {
        this.element = element
        setupProductAddOn(element)
    }

    override fun onDescriptionSeeLessClicked() {
        element?.run {
            descriptionExpanded = false
            binding?.layoutAddOnDescription?.collapse()
        }
    }

    override fun onDescriptionSeeMoreClicked() {
        element?.run {
            descriptionExpanded = true
            binding?.layoutAddOnDescription?.expand()
        }
    }

    override fun onCopyDescriptionClicked(label: String, description: CharSequence) {
        listener.onCopyAddOnDescriptionClicked(label, description)
    }

    private fun setupProductAddOn(addOnUiModel: AddOnSummaryUiModel.AddonItemUiModel) {
        binding?.run {
            setupAddOnImage(addOnUiModel.addOnsThumbnailUrl)
            setupAddOnName(
                addOnUiModel.type,
                addOnUiModel.addOnsName,
                addOnUiModel.noteCopyable
            )
            setupAddOnPrice(addOnUiModel.quantity, addOnUiModel.priceText)
            setupAddOnDescriptions(
                addOnUiModel
            )
        }
    }

    private fun ItemBmgmDetailAddOnBinding.setupAddOnImage(thumbnailUrl: String) {
        ivAddOn.loadImage(thumbnailUrl)
    }

    private fun ItemBmgmDetailAddOnBinding.setupAddOnName(
        type: String,
        name: String,
        providedByBranchShop: Boolean
    ) {
        tvAddOnName.text = composeAddOnName(type, name, providedByBranchShop)
    }

    private fun ItemBmgmDetailAddOnBinding.setupAddOnPrice(quantity: Int, priceText: String) {
        tvAddOnPrice.text = StringBuilder("$quantity x $priceText")
    }

    private fun ItemBmgmDetailAddOnBinding.setupAddOnDescriptions(
        addonItemUiModel: AddOnSummaryUiModel.AddonItemUiModel
    ) {
        layoutAddOnDescription.run {
            if (addonItemUiModel.message.isEmpty()) {
                hide()
            } else {
                setIsCopyable(copyable = addonItemUiModel.noteCopyable)
                setReceiverName(addonItemUiModel.toStr)
                setSenderName(addonItemUiModel.fromStr)
                setDescription(
                    addonItemUiModel.message.stripLastDot(),
                    addonItemUiModel.descriptionExpanded
                )
                listener = this@BmgmAddOnViewHolder
                show()
            }
        }
    }

    private fun composeAddOnName(
        type: String,
        name: String,
        hasShop: Boolean
    ): CharSequence {
        return if (hasShop) "$type - $name" else name
    }

    interface Listener {
        fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence)
    }
}
