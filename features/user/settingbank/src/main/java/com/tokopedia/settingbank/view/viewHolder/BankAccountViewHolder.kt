package com.tokopedia.settingbank.view.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.view.adapter.BankAccountClickListener
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerType

class BankAccountViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

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
                status = bankAccount.statusFraud,
                copyWriting = bankAccount.copyWriting
        )

        addClickListener()
    }

    private fun addClickListener() {
        view.findViewById<View>(R.id.btnIsiData).setOnClickListener { listener?.onClickDataContent(view.tag as BankAccount) }
        view.findViewById<View>(R.id.btnHapus).setOnClickListener { listener?.deleteBankAccount(view.tag as BankAccount) }
    }

    private fun setBankStatus(status: Int, copyWriting: String?) {
        var tickerType = Ticker.TYPE_INFORMATION
        when (status) {
            in 0..1, 4 -> {
                enableBankItem()
                setAccountViewStatus(showIsiDataButton = false, showPendingAccountButton = false)
            }
            2 -> {
                disableBankItem()
                tickerType = Ticker.TYPE_ERROR
                setAccountViewStatus(showIsiDataButton = true, showPendingAccountButton = false)
            }
            3 -> {
                disableBankItem()
                tickerType = Ticker.TYPE_ERROR
                setAccountViewStatus(showIsiDataButton = false,showPendingAccountButton = false)

            }
            5 -> {
                disableBankItem()
                tickerType = Ticker.TYPE_INFORMATION
                setAccountViewStatus(showIsiDataButton = false,showPendingAccountButton = false)
            }
            6 -> {
                enableBankItem()
                setAccountViewStatus(showIsiDataButton = false, showPendingAccountButton = true)
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

    private fun setAccountViewStatus(showIsiDataButton: Boolean,
                                     showPendingAccountButton: Boolean) {
        hideShowIsiDataButton(showIsiDataButton)
        hideShowPendingAccountTag(showPendingAccountButton)
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
