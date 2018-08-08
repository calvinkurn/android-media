package com.tokopedia.settingbank.banklist.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.data.SettingBankUrl
import com.tokopedia.settingbank.banklist.view.listener.EmptyBankAccountListener

/**
 * @author by nisie on 6/21/18.
 */

class EmptyBankAccountViewHolder(val v: View, val listener: EmptyBankAccountListener) :
        AbstractViewHolder<EmptyModel>(v) {

    private val emptyIcon: ImageView = itemView.findViewById(R.id.empty_image)
    private val addAccountButton: TextView = itemView.findViewById(R.id.add_account_button_empty)


    companion object {
        val LAYOUT = R.layout.empty_bank_account
    }

    override fun bind(element: EmptyModel?) {
        ImageHandler.LoadImage(emptyIcon, SettingBankUrl.Companion.IMAGE_EMPTY_BANK_LIST)
        addAccountButton.setOnClickListener({ listener.addNewAccount() })
    }

}