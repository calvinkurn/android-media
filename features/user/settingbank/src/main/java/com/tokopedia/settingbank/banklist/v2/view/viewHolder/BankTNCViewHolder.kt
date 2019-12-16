package com.tokopedia.settingbank.banklist.v2.view.viewHolder

import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.domain.TemplateData
import com.tokopedia.unifycomponents.ticker.Ticker


class BankTNCViewHolder (val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(templateData: TemplateData?) {
        val context = view.context
        val ticker = view.findViewById<Ticker>(R.id.tickerCatatan)
        templateData?.let {
            ticker.tickerTitle = context?.getString(R.string.sbank_catatan)
            ticker.setHtmlDescription(templateData.template)
            ticker.findViewById<TextView>(R.id.ticker_description).text = Html.fromHtml(templateData.template)
            ticker.findViewById<TextView>(R.id.ticker_description).setTextColor(context.resources.getColor(com.tokopedia.design.R.color.grey_796))
        } ?: ticker.gone()
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank_tnc
    }

}

