package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.ItemPofDescriptionBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UrlLinkManager

class PofDescriptionViewHolder(
    view: View,
    private val listener: PofAdapterTypeFactory.Listener
) : AbstractViewHolder<PofDescriptionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_description
    }

    private val binding = ItemPofDescriptionBinding.bind(view)

    override fun bind(element: PofDescriptionUiModel) {
        setupText(element)
    }

    override fun bind(element: PofDescriptionUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupText(element: PofDescriptionUiModel) {
        binding.tvPofDescription.text = HtmlLinkHelper(
            binding.root.context, element.text.getString(binding.root.context)
        ).apply {
            urlList.forEach { url -> attachUrlClickListener(url, element.onClickEventData) }
        }.spannedString ?: String.EMPTY
        binding.tvPofDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun attachUrlClickListener(url: UrlLinkManager, onClickEventData: UiEvent) {
        url.onClick = {
            val appLink = formatUrlToAppLink(url.linkUrl)
            val intent = RouteManager.getIntentNoFallback(binding.root.context, appLink)
            if (intent != null) {
                listener.onEvent(onClickEventData)
                binding.root.context.startActivity(intent)
            }
        }
    }

    private fun formatUrlToAppLink(url: String): String {
        return if (url.startsWith(SomConsts.PREFIX_HTTP)) {
            "${ApplinkConst.WEBVIEW}?url=$url"
        } else {
            url
        }
    }
}
