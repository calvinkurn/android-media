package com.tokopedia.pms.bankaccount.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.bankaccount.data.model.BankListModel
import com.tokopedia.pms.databinding.ItemBankListBinding

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankListViewHolder(
    view: View,
    val clickAction: (BankListModel) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemBankListBinding.bind(view)

    fun bind(element: BankListModel) {
        binding.bankName.text = element.bankName
        itemView.setOnClickListener { clickAction(element) }
    }

    companion object {
        var Layout = R.layout.item_bank_list
    }
}
