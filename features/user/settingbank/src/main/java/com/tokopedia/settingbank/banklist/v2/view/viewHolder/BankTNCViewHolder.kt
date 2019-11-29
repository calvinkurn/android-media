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
        val ticker = view.findViewById<Ticker>(R.id.tickerCatatan)
        templateData?.let {
            ticker.tickerTitle = "Catatan:"
            ticker.setHtmlDescription(templateData.template)
            ticker.findViewById<TextView>(R.id.ticker_description).text = Html.fromHtml(templateData.template)
            ticker.findViewById<TextView>(R.id.ticker_description).setTextColor(ticker.context.resources.getColor(R.color.grey_796))
        } ?: ticker.gone()
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank_tnc
    }

}

