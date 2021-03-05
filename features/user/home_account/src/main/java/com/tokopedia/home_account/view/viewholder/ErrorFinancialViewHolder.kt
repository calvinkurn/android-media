package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.unifycomponents.LocalLoad

/**
 * Created by Ade Fulki on 24/02/21.
 */

class ErrorFinancialViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    private val localLoad: LocalLoad = itemView.findViewById(R.id.home_account_financial_local_load)

    fun bind() {
        localLoad.refreshBtn?.setOnClickListener {
            localLoad.progressState = !localLoad.progressState
            listener.onFinancialErrorClicked(ERROR_TYPE)
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_financial_error
        const val ERROR_TYPE = 11
    }

}