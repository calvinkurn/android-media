package com.tokopedia.expresscheckout.view.profile.viewholder

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileActionListener
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.item_profile_profile_list_page.view.*

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class ProfileViewHolder(val view: View, val listener: CheckoutProfileActionListener) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_profile_profile_list_page
    }

    fun bind(element: ProfileViewModel?) {
        if (element != null) {
            if (element.isSelected) {
                itemView.img_selected.visibility = View.VISIBLE
            } else {
                itemView.img_selected.visibility = View.GONE
            }

            if (element.isMainTemplate) {
                itemView.tv_main_template.visibility = View.VISIBLE
            } else {
                itemView.tv_main_template.visibility = View.GONE
            }

            itemView.tv_template_title.text = "Template ${element.templateTitle}"
            itemView.tv_profile_address.text = getHtmlFormat("<b>${element.addressTitle}</b> ${element.addressDetail}")
            ImageHandler.loadImageRounded2(itemView.context, itemView.img_payment_method, element.paymentImageUrl)
            itemView.tv_payment_detail.text = element.paymentDetail
            itemView.tv_shipping_detail.text = getHtmlFormat("Durasi <b>${element.durationDetail}</b>")
            itemView.setOnClickListener { listener.onItemSelected(element) }
        }
    }

    private fun getHtmlFormat(text: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(text)
        }
    }

}