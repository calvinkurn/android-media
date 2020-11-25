package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import kotlinx.android.synthetic.main.home_account_item_financial.view.*

/**
 * Created by Yoris Prayogo on 09/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class FinancialItemViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    fun bind(financial: CommonDataView) {
        with(itemView) {
            account_user_item_financial_body?.text = financial.body
            account_user_item_financial_title?.text = financial.title

            if(financial.icon != 0) {
                account_user_item_financial_icon?.setImageResource(financial.icon)
            }
            else if(financial.urlIcon.isNotEmpty()){
                ImageHandler.loadImageFit2(account_user_item_financial_icon.context, account_user_item_financial_icon, financial.urlIcon)
            }
            itemView.setOnClickListener {
                listener.onSettingItemClicked(financial)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_financial
    }

}