package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.text.SpannableString
import android.text.Spanned
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoDescriptionViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_description.view.*

class CampaignInfoDescriptionViewHolder(view: View): AbstractViewHolder<CampaignInfoDescriptionViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_description
        private const val MAX_CHARACTER = 150
        private const val HTML_MORE_DESCRIPTION = "<font color='#42b549'>selengkapnya</font>"
    }

    override fun bind(element: CampaignInfoDescriptionViewModel) {
        with(itemView.description_content){
            text = getFormatedMessage(element.description)
            setOnClickListener {
                if (text.endsWith("selengkapnya")){
                    text = element.description
                }
            }
        }
    }

    private fun getFormatedMessage(message: String): Spanned {
        if (message.length > MAX_CHARACTER) {
            val subDescription = message.substring(0, MAX_CHARACTER)
            return MethodChecker
                    .fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                            + HTML_MORE_DESCRIPTION)
        } else {
            return SpannableString(message)
        }
    }
}