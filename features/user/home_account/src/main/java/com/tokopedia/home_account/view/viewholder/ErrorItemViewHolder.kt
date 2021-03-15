package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.unifycomponents.LocalLoad

/**
 * Created by Yoris Prayogo on 08/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ErrorItemViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    private val localLoad: LocalLoad = itemView.findViewById(R.id.home_account_local_load)

    fun bind() {
        localLoad.refreshBtn?.setOnClickListener {
            localLoad.progressState = !localLoad.progressState
            listener.onMemberErrorClicked()
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_error
    }
}