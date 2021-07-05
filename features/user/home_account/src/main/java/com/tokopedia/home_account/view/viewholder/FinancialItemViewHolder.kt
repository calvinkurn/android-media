package com.tokopedia.home_account.view.viewholder

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import kotlinx.android.synthetic.main.home_account_item_financial.view.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Yoris Prayogo on 09/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class FinancialItemViewHolder(itemView: View, val listener: HomeAccountUserListener) : BaseViewHolder(itemView) {

    fun bind(financial: CommonDataView) {
        with(itemView) {
            changeColor(account_user_item_financial_title, financial.titleColor, financial.isTitleBold)
            changeColor(account_user_item_financial_body, financial.bodyColor, financial.isBodyBold)

            account_user_item_financial_body?.text = financial.body
            account_user_item_financial_title?.text = financial.title

            if (financial.icon != 0) {
                account_user_item_financial_icon?.setImageResource(financial.icon)
            } else if (financial.urlIcon.isNotEmpty()) {
                ImageHandler.loadImageFit2(account_user_item_financial_icon.context, account_user_item_financial_icon, financial.urlIcon)
            }
            itemView.setOnClickListener {
                listener.onSettingItemClicked(financial)
            }
        }
    }

    private fun changeColor(viewTextView: TextView?, colorHex: String, isBold: Boolean) {
        val colorPattern: Pattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})")
        if (colorHex.isNotEmpty()) {
            val m: Matcher = colorPattern.matcher(colorHex)
            if (m.matches()) {
                viewTextView?.setTextColor(Color.parseColor(colorHex))
            }
        }
        if (isBold) {
            viewTextView?.setTypeface(viewTextView.typeface, Typeface.BOLD)
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_financial

        const val TYPE_OVO_TOKOPOINTS = 4
        const val TYPE_SALDO = 5
    }

}