package com.tokopedia.settingbank.view.viewHolder

import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography


class BankTNCViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind() {
        val context = view.context
        val title = view.findViewById<Typography>(R.id.tvBankNoteTitle)
        val description = view.findViewById<Typography>(R.id.tvBankNoteDescription)

        title.text = context?.getString(R.string.sbank_catatan_title)
        description.text = context?.getString(R.string.sbank_catatan_description)
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank_tnc
    }

    private fun fromHtml(text: String?): Spanned {
        var text = text
        if (text == null) {
            text = ""
        }
        val result: Spanned
        result = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
        return result
    }


}

