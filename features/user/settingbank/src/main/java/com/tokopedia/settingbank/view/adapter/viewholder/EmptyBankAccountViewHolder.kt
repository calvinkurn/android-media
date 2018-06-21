package com.tokopedia.settingbank.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.view.listener.EmptyBankAccountListener

/**
 * @author by nisie on 6/21/18.
 */

class EmptyBankAccountViewHolder(val v: View, val listener: EmptyBankAccountListener) :
        AbstractViewHolder<EmptyModel>(v) {

    val emptyIcon: ImageView = itemView.findViewById(R.id.empty_image)
    val addAccountButton: TextView = itemView.findViewById(R.id.add_account_button_empty)


    companion object {
        val LAYOUT = R.layout.empty_bank_account
    }

    override fun bind(element: EmptyModel?) {

        ImageHandler.LoadImage(emptyIcon, "https://ecs7.tokopedia.net/img/gold-active-large.png")
        addAccountButton.setOnClickListener({ listener.addNewAccount() })
    }

}