package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.Utils.stripLastDot
import com.tokopedia.sellerorder.databinding.ItemAddOnBinding
import com.tokopedia.sellerorder.detail.data.model.AddOnSummary
import com.tokopedia.sellerorder.detail.presentation.model.AddOnUiModel
import com.tokopedia.sellerorder.detail.presentation.widget.AddOnDescriptionWidget
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailAddOnViewHolder(
    itemView: View?,
    private val listener: Listener
) : AbstractViewHolder<AddOnUiModel>(itemView),
    AddOnDescriptionWidget.Listener {

    companion object {
        val RES_LAYOUT = R.layout.item_add_on
        const val MAX_RECYCLED_VIEWS = 10
    }

    private val binding by viewBinding<ItemAddOnBinding>()
    private var element: AddOnUiModel? = null

    override fun bind(element: AddOnUiModel) {
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

    private fun setupProductAddOn(addOnUiModel: AddOnUiModel) {
        binding?.run {
            setupAddOnImage(addOnUiModel.addOn.imageUrl)
            setupAddOnName(addOnUiModel.addOn.type, addOnUiModel.addOn.name, addOnUiModel.providedByBranchShop)
            setupAddOnPrice(addOnUiModel.addOn.quantity, addOnUiModel.addOn.priceStr)
            setupAddOnDescriptions(addOnUiModel.addOn.metadata, addOnUiModel.descriptionExpanded, addOnUiModel.providedByBranchShop)
        }
    }

    private fun ItemAddOnBinding.setupAddOnImage(thumbnailUrl: String) {
        ivAddOn.loadImage(thumbnailUrl)
    }

    @SuppressLint("SetTextI18n")
    private fun ItemAddOnBinding.setupAddOnName(
        type: String,
        name: String,
        providedByBranchShop: Boolean
    ) {
        tvAddOnName.text = composeAddOnName(type, name, providedByBranchShop)
    }

    @SuppressLint("SetTextI18n")
    private fun ItemAddOnBinding.setupAddOnPrice(quantity: Int, priceText: String) {
        tvAddOnPrice.text = "$quantity x $priceText"
    }

    private fun ItemAddOnBinding.setupAddOnDescriptions(
        metadata: AddOnSummary.Addon.Metadata?,
        descriptionExpanded: Boolean,
        providedByBranchShop: Boolean
    ) {
        layoutAddOnDescription.run {
            if (metadata?.addOnNote == null || metadata.addOnNote.isEmpty()) {
                gone()
            } else {
                setIsCopyable(copyable = !providedByBranchShop)
                setReceiverName(metadata.addOnNote.to)
                setSenderName(metadata.addOnNote.from)
                setDescription(metadata.addOnNote.notes.stripLastDot(), descriptionExpanded)
                listener = this@SomDetailAddOnViewHolder
                show()
            }
        }
    }

    private fun composeAddOnName(
        type: String,
        name: String,
        providedByBranchShop: Boolean
    ): CharSequence {
        return if (providedByBranchShop) type else "$type - $name"
    }

    interface Listener {
        fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence)
    }
}