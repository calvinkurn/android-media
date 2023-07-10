package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.withdraw.R

class AddBankButtonViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bindData(openAddBankAccount: () -> Unit) {

    }

    companion object {
        val LAYOUT_ID = R.layout.swd_item_add_bank
    }
}
