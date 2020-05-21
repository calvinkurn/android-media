package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.withdraw.saldowithdrawal.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder.BankAccountViewHolder
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder.BankSettingButtonViewHolder

class BankAccountAdapter(private val withdrawAnalytics: WithdrawAnalytics,
                         private val listener: BankAdapterListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val bankAccountList: ArrayList<BankAccount> = arrayListOf()

    /**
     * it is used to put extra item in List for setting
     * */
    private val bankSetting = BankAccount()

    private lateinit var currentSelectedBankAccount: BankAccount

    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        return if (viewType == BankAccountViewHolder.LAYOUT_ID)
            BankAccountViewHolder.getViewHolder(inflater, parent)
        else
            BankSettingButtonViewHolder.getViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = bankAccountList.size

    override fun getItemViewType(position: Int): Int {
        return if (bankAccountList[position] == bankSetting)
            BankSettingButtonViewHolder.LAYOUT_ID
        else
            BankAccountViewHolder.LAYOUT_ID
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bankAccount = bankAccountList[position]
        if (holder is BankAccountViewHolder)
            holder.bindData(bankAccount, ::onBankAccountSelected)
        else
            (holder as BankSettingButtonViewHolder).bindData(
                    bankAccountList.size - 1,
                    openAddBankAccount = ::openAddBankAccount,
                    openBankAccountSetting = ::openBankAccountSetting)
    }

    fun updateBankList(newBankList: ArrayList<BankAccount>) {
        bankAccountList.clear()
        bankAccountList.addAll(updateDefaultBankList(newBankList))
        bankAccountList.add(bankSetting)
    }

    private fun updateDefaultBankList(newBankList: ArrayList<BankAccount>): ArrayList<BankAccount> {
        newBankList.forEach {
            if (it.isDefaultBank == DEFAULT_BANK_STATUS) {
                it.isChecked = true
                currentSelectedBankAccount = it
                return@forEach
            }
        }
        return newBankList
    }

    private fun onBankAccountSelected(newSelectedBankAccount: BankAccount) {
        if (::currentSelectedBankAccount.isInitialized) {
            if (currentSelectedBankAccount == newSelectedBankAccount) {
                return
            } else {
                currentSelectedBankAccount.isChecked = false
            }
        }
        newSelectedBankAccount.isChecked = true
        currentSelectedBankAccount = newSelectedBankAccount
        notifyDataSetChanged()
        listener.onBankAccountChanged()
        withdrawAnalytics.eventClickAccountBank()
    }

    private fun openAddBankAccount() {
        listener.openAddBankAccount()
        withdrawAnalytics.eventClickAddAccount()
    }

    private fun openBankAccountSetting() {
        listener.openBankAccountSetting()
    }

    fun getSelectedBankAccount(): BankAccount? {
        return if (!::currentSelectedBankAccount.isInitialized)
            null
        else
            currentSelectedBankAccount
    }

    interface BankAdapterListener {
        fun onBankAccountChanged()

        fun openAddBankAccount()

        fun openBankAccountSetting()
    }

    companion object {
        private const val DEFAULT_BANK_STATUS = 1
    }
}
