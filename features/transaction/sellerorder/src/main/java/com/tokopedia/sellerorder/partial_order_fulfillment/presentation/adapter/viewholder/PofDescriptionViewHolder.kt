package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofDescriptionBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofDescriptionUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper

class PofDescriptionViewHolder(
    view: View
) : AbstractViewHolder<PofDescriptionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_description
    }

    private val binding = ItemPofDescriptionBinding.bind(view)

    override fun bind(element: PofDescriptionUiModel) {
        setupText(element.text)
    }

    override fun bind(element: PofDescriptionUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupText(text: StringRes) {
        binding.tvPofDescription.text = HtmlLinkHelper(
            binding.root.context, text.getString(binding.root.context)
        ).apply {
            urlList.forEach { url ->
                url.onClick = { RouteManager.route(binding.root.context, url.linkUrl) }
            }
        }.spannedString ?: String.EMPTY
        binding.tvPofDescription.movementMethod = LinkMovementMethod.getInstance()
    }
}
