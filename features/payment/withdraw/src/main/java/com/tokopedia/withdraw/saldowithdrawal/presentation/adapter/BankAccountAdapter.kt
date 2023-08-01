package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.withdraw.saldowithdrawal.analytics.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.CheckEligible
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder.BankAccountViewHolder
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder.BankSettingButtonViewHolder

class BankAccountAdapter(private val withdrawAnalytics: WithdrawAnalytics,
                         private val listener: BankAdapterListener,
                         private val isRpLogoVisible: Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var needToShowRPCoachMark: Boolean = false

    private val bankAccountList: ArrayList<BankAccount> = arrayListOf()
    private var checkEligible: CheckEligible? = null

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

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (needToShowRPCoachMark && holder is BankAccountViewHolder
                && holder.isPowerMerchantVisible()) {
            needToShowRPCoachMark = false
            val coachMarchView = holder.getPowerMerchantImageView()
            listener.showCoachMarkOnRPIcon(coachMarchView)
        }

        val position = holder.absoluteAdapterPosition
        val isGopay = bankAccountList[position].isGopay()
        if (holder is BankAccountViewHolder && isGopay) {
            listener.showCoachMarkOnGopayBank(holder.itemView, bankAccountList[position])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bankAccount = bankAccountList[position]
        if (holder is BankAccountViewHolder)
            holder.bindData(bankAccount, ::onBankAccountSelected, listener, isRpLogoVisible)
        else
            (holder as BankSettingButtonViewHolder).bindData(
                    bankAccountList.size - 1,
                    checkEligible, openAddBankAccount = ::openAddBankAccount,
                    openBankAccountSetting = ::openBankAccountSetting)
    }

    fun updateBankList(newBankList: ArrayList<BankAccount>, checkEligible: CheckEligible,
                       needToShowRPCoachMark: Boolean) {
        this.needToShowRPCoachMark = needToShowRPCoachMark;
        this.checkEligible = checkEligible
        bankAccountList.clear()
        bankAccountList.addAll(updateDefaultBankList(newBankList))
    }

    private fun updateDefaultBankList(newBankList: ArrayList<BankAccount>): ArrayList<BankAccount> {
        newBankList.forEach {
            if (it.defaultBankAccount) {
                it.isChecked = true
                currentSelectedBankAccount = it
                return@forEach
            }
        }
        return newBankList
    }

    private fun onBankAccountSelected(newSelectedBankAccount: BankAccount) {
        if (::currentSelectedBankAccount.isInitialized) {
            if (currentSelectedBankAccount == newSelectedBankAccount || (newSelectedBankAccount.isGopay() && !newSelectedBankAccount.isGopayEligible())) {
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
        withdrawAnalytics.onClickManageAccount()
    }

    fun getSelectedBankAccount(): BankAccount? {
        return if (!::currentSelectedBankAccount.isInitialized || !bankAccountList.contains(currentSelectedBankAccount))
            null
        else
            currentSelectedBankAccount
    }

    interface BankAdapterListener {
        fun onButtonClicked(applink: String)
        fun onBankAccountChanged()

        fun openAddBankAccount()

        fun openBankAccountSetting()

        fun showCoachMarkOnRPIcon(iconView: View)

        fun showCoachMarkOnGopayBank(view: View, bankAccount: BankAccount)

        fun showPremiumAccountDialog(bankAccount: BankAccount)

        fun onDisabledBankClick(bankAccount: BankAccount)
    }

}
