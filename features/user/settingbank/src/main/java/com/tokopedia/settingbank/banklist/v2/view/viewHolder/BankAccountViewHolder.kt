package com.tokopedia.settingbank.banklist.v2.view.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankAccountClickListener
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerType

class BankAccountViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val PRIMARY_ACCOUNT = 1
    private val DISABLE_IMAGE_ALPHA = 0.38F
    private val ENABLE_IMAGE_ALPHA = 1F


    private val ticker: Ticker = view.findViewById(R.id.tickerBankAccount)

    var listener: BankAccountClickListener? = null

    fun bind(bankAccount: BankAccount, listener: BankAccountClickListener?) {
        view.tag = bankAccount
        this.listener = listener
        view.findViewById<TextView>(R.id.tvBankName).text = bankAccount.bankName ?: ""
        view.findViewById<TextView>(R.id.tvBankAccountNumber).text = bankAccount.accNumber ?: ""
        view.findViewById<TextView>(R.id.tvBankAccountHolderName).text = bankAccount.accName?.let { "a.n $it" } ?: ""
        bankAccount.bankImageUrl?.let {
            ImageLoader.LoadImage(view.findViewById(R.id.ivBankImage), bankAccount.bankImageUrl)
        }
        ticker.gone()
        setBankStatus(
                isPrimary = (bankAccount.fsp == PRIMARY_ACCOUNT),
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
        var tickerType = Ticker.TYPE_INFORMATION
        when (status) {
            in 0..1, 4 -> {
                enableBankItem()
                setAccountViewStatus(isPrimary = isPrimary, showMakePrimaryButton = !isPrimary,
                        showIsiDataButton = false, showDeleteButton = !isPrimary,
                        showPendingAccountButton = false)
            }
            2 -> {
                disableBankItem()
                tickerType = Ticker.TYPE_ERROR
                setAccountViewStatus(isPrimary = false, showMakePrimaryButton = false,
                        showIsiDataButton = true, showDeleteButton = true,
                        showPendingAccountButton = false)
            }
            3 -> {
                disableBankItem()
                tickerType = Ticker.TYPE_ERROR
                setAccountViewStatus(isPrimary = false, showMakePrimaryButton = false,
                        showIsiDataButton = false, showDeleteButton = true,
                        showPendingAccountButton = false)

            }
            5 -> {
                disableBankItem()
                tickerType = Ticker.TYPE_INFORMATION
                setAccountViewStatus(isPrimary = false, showMakePrimaryButton = false,
                        showIsiDataButton = false, showDeleteButton = true,
                        showPendingAccountButton = false)
            }
            6 -> {
                enableBankItem()
                setAccountViewStatus(isPrimary = false, showMakePrimaryButton = false,
                        showIsiDataButton = false, showDeleteButton = true,
                        showPendingAccountButton = true)
            }
        }

        setCopyWriting(copyWriting, tickerType)
    }

    private fun disableBankItem() {
        view.findViewById<TextView>(R.id.tvBankName).isEnabled = false
        view.findViewById<TextView>(R.id.tvBankAccountNumber).isEnabled = false
        view.findViewById<TextView>(R.id.tvBankAccountHolderName).isEnabled = false
        view.findViewById<ImageView>(R.id.ivBankImage).alpha = DISABLE_IMAGE_ALPHA
    }

    private fun enableBankItem() {
        view.findViewById<TextView>(R.id.tvBankName).isEnabled = true
        view.findViewById<TextView>(R.id.tvBankAccountNumber).isEnabled = true
        view.findViewById<TextView>(R.id.tvBankAccountHolderName).isEnabled = true
        view.findViewById<ImageView>(R.id.ivBankImage).alpha = ENABLE_IMAGE_ALPHA
    }

    private fun setCopyWriting(copyWriting: String?, @TickerType tickerType: Int) {
        copyWriting?.let {
            if (it.isEmpty()) {
                ticker.gone()
            } else {
                ticker.tickerType = tickerType
                ticker.visible()
                ticker.setTextDescription(copyWriting)
            }
        } ?: run {
            ticker.gone()
        }
    }

    private fun setAccountViewStatus(isPrimary: Boolean,
                                     showMakePrimaryButton: Boolean,
                                     showIsiDataButton: Boolean,
                                     showDeleteButton: Boolean,
                                     showPendingAccountButton: Boolean) {
        hideShowPrimaryTag(isPrimary)
        hideShowMakePrimaryButton(showMakePrimaryButton)
        hideShowIsiDataButton(showIsiDataButton)
        hideShowDeleteButton(showDeleteButton)
        hideShowPendingAccountTag(showPendingAccountButton)
    }

    private fun hideShowDeleteButton(isShow: Boolean) {
        when (isShow) {
            true -> view.findViewById<TextView>(R.id.btnHapus).visible()
            else -> view.findViewById<TextView>(R.id.btnHapus).gone()
        }
    }

    private fun hideShowPrimaryTag(isShow: Boolean) {
        when (isShow) {
            true -> view.findViewById<TextView>(R.id.tvAccountPrimaryTag).visible()
            else -> view.findViewById<TextView>(R.id.tvAccountPrimaryTag).gone()
        }
    }

    private fun hideShowMakePrimaryButton(isShow: Boolean) {
        when (isShow) {
            true -> view.findViewById<TextView>(R.id.btnMakePrimary).visible()
            else -> view.findViewById<TextView>(R.id.btnMakePrimary).gone()
        }
    }

    private fun hideShowIsiDataButton(isShow: Boolean) {
        when (isShow) {
            true -> view.findViewById<TextView>(R.id.btnIsiData).visible()
            else -> view.findViewById<TextView>(R.id.btnIsiData).gone()
        }
    }

    private fun hideShowPendingAccountTag(isShow: Boolean) {
        when (isShow) {
            true -> view.findViewById<TextView>(R.id.tvAccountPending).visible()
            else -> view.findViewById<TextView>(R.id.tvAccountPending).gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank_account
    }
}
