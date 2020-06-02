package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantItemView
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantSpannableUtil.createSpannableString
import com.tokopedia.unifyprinciples.Typography

class PowerMerchantItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val context by lazy { itemView.context }
    
    private val textTitle by lazy { itemView.findViewById<Typography>(R.id.textTitle) }
    private val textDescription by lazy { itemView.findViewById<Typography>(R.id.textDescription) }
    private val imageIcon by lazy { itemView.findViewById<ImageView>(R.id.imageIcon) }

    fun bind(item: PowerMerchantItemView) {
        showTitle(item)
        showDescription(item)
        ImageHandler.loadImageWithId(imageIcon, item.icon)
    }

    private fun showTitle(item: PowerMerchantItemView) {
        textTitle.text = context.getString(item.title)
    }

    private fun showDescription(item: PowerMerchantItemView) {
        val description = context.getString(item.description)
        val clickableText = item.clickableText?.let { context.getString(it) }

        textDescription.text = if (clickableText != null) {
            val clickableUrl = item.clickableUrl
            val clickableTextColor = ContextCompat.getColor(context, R.color.pm_main_color)
            createSpannableString(description, clickableText, clickableTextColor) {
                clickableUrl?.let { url -> goToWebViewPage(url) }
            }
        } else {
            description
        }

        textDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun goToWebViewPage(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }
}