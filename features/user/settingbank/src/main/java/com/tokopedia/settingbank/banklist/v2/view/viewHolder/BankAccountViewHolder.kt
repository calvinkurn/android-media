package com.tokopedia.settingbank.banklist.v2.view.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankAccountClickListener

class BankAccountViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(bankAccount: BankAccount, listener: BankAccountClickListener?) {
        view.findViewById<TextView>(R.id.tvBankName).text = bankAccount.bankName ?: ""
        view.findViewById<TextView>(R.id.tvBankAccountNumber).text = bankAccount.accNumber?: ""
        view.findViewById<TextView>(R.id.tvBankAccountHolderName).text =bankAccount.accName?.let { "a.n $it" } ?: ""
        bankAccount.bankImageUrl?.let {
            ImageLoader.LoadImage(view.findViewById(R.id.ivBankImage), bankAccount.bankImageUrl)
        }
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank_account
    }

}


/**
 *
bank state:

1. Added (Verified)-
1. Primary
2. Non Primary + Hapus(Delete this)

2. Added but Verification Pending

3.Added but Disabled:
1. Required Additional data
2. Required Addition data + Isi Data Button

4. Account submitted for verification (Your account is in the process of verification within 2x 24 hours)

 * */