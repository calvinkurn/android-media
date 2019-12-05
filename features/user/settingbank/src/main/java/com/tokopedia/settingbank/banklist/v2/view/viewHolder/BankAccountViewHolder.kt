package com.tokopedia.settingbank.banklist.v2.view.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankAccountClickListener
import com.tokopedia.unifycomponents.ticker.Ticker

class BankAccountViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val ticker: Ticker = view.findViewById(R.id.tickerBankAccount)

    var listener: BankAccountClickListener? = null

    fun bind(bankAccount: BankAccount, listener: BankAccountClickListener?) {
        view.tag = bankAccount
        this.listener = listener
        view.findViewById<TextView>(R.id.tvBankName).text = bankAccount.bankName ?: ""
        view.findViewById<TextView>(R.id.tvBankAccountNumber).text = bankAccount.accNumber ?: ""
        view.findViewById<TextView>(R.id.tvBankAccountHolderName).text = bankAccount.accName?.let { "a.n $it" }
                ?: ""
        bankAccount.bankImageUrl?.let {
            ImageLoader.LoadImage(view.findViewById(R.id.ivBankImage), bankAccount.bankImageUrl)
        }
        ticker.gone()
        setBankStatus(
                isPrimary = (bankAccount.fsp == 1),
                status = bankAccount.statusFraud,
                copyWriting = bankAccount.copyWriting
        )
        addClickListener()
    }

    private fun addClickListener() {
        view.findViewById<View>(R.id.btnIsiData).setOnClickListener { listener?.onClickDataContent(view.tag as BankAccount) }
        view.findViewById<View>(R.id.btnMakePrimary).setOnClickListener { listener?.onMakeAccountPrimary(view.tag as BankAccount) }
        view.findViewById<View>(R.id.btnHapus).setOnClickListener { listener?.deleteBankAccount(view.tag as BankAccount) }
    }

    private fun setBankStatus(isPrimary: Boolean, status: Int, copyWriting: String?) {
        if (isPrimary) {
            hideShowPrimaryTag(isHide = false)
            hideShowIsiData(isHide = true)
            hideShowDeleteButton(isHide = true)
            hideShowMakePrimary(isHide = true)
            hideShowPendingAccountTag(isHide = true)
        }
        when (status) {
            in 0..1 -> {
                hideShowPrimaryTag(isHide = true)
                hideShowMakePrimary(isHide = false)
                hideShowIsiData(isHide = true)
                hideShowDeleteButton(isHide = true)
                hideShowPendingAccountTag(isHide = true)

            }
            2 -> {
                copyWriting?.let {
                    ticker.tickerType = Ticker.TYPE_ERROR
                    ticker.visible()
                    ticker.setTextDescription(copyWriting)
                } ?: run {
                    ticker.gone()

                }
                hideShowPrimaryTag(isHide = true)
                hideShowMakePrimary(isHide = true)
                hideShowIsiData(isHide = false)
                hideShowDeleteButton(isHide = false)
                hideShowPendingAccountTag(isHide = true)
            }
            3 -> {
                //todo disable all text data an ticker
                copyWriting?.let {
                    ticker.tickerType = Ticker.TYPE_ERROR
                    ticker.visible()
                    ticker.setTextDescription(copyWriting)
                } ?: run {
                    ticker.gone()
                }
                hideShowPrimaryTag(isHide = true)
                hideShowMakePrimary(isHide = true)
                hideShowIsiData(isHide = true)
                hideShowDeleteButton(isHide = false)
                hideShowPendingAccountTag(isHide = true)

            }
            in 4..5 -> {
                //todo disable all text data an ticker
                copyWriting?.let {
                    ticker.tickerType = Ticker.TYPE_ERROR
                    ticker.visible()
                    ticker.setTextDescription(copyWriting)
                } ?: run {
                    ticker.gone()
                }
                hideShowPrimaryTag(isHide = true)
                hideShowMakePrimary(isHide = true)
                hideShowIsiData(isHide = true)
                hideShowDeleteButton(isHide = false)
                hideShowPendingAccountTag(isHide = true)
            }
            6 -> {
                hideShowPendingAccountTag(isHide = false)
                hideShowPrimaryTag(isHide = true)
                hideShowMakePrimary(isHide = true)
                hideShowIsiData(isHide = true)
                hideShowDeleteButton(isHide = false)
            }
        }
    }

    private fun hideShowDeleteButton(isHide: Boolean) {
        when (isHide) {
            true -> view.findViewById<TextView>(R.id.btnHapus).gone()
            else -> view.findViewById<TextView>(R.id.btnHapus).visible()
        }
    }

    private fun hideShowPrimaryTag(isHide: Boolean) {
        when (isHide) {
            true -> view.findViewById<TextView>(R.id.tvAccountPrimaryTag).gone()
            else -> view.findViewById<TextView>(R.id.tvAccountPrimaryTag).visible()
        }
    }

    private fun hideShowMakePrimary(isHide: Boolean) {
        when (isHide) {
            true -> view.findViewById<TextView>(R.id.btnMakePrimary).gone()
            else -> view.findViewById<TextView>(R.id.btnMakePrimary).visible()
        }
    }

    private fun hideShowIsiData(isHide: Boolean) {
        when (isHide) {
            true -> view.findViewById<TextView>(R.id.btnIsiData).gone()
            else -> view.findViewById<TextView>(R.id.btnIsiData).visible()
        }
    }

    private fun hideShowPendingAccountTag(isHide: Boolean) {
        when (isHide) {
            true -> view.findViewById<TextView>(R.id.tvAccountPending).gone()
            else -> view.findViewById<TextView>(R.id.tvAccountPending).visible()
        }
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank_account
    }
}
