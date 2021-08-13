package com.tokopedia.settingbank.view.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.view.adapter.BankAccountClickListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerType

class BankAccountViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val DISABLE_IMAGE_ALPHA = 0.38F
    private val ENABLE_IMAGE_ALPHA = 1F

    private val ivBankImage : ImageUnify = itemView.findViewById(R.id.ivBankImage)
    private val tvBankName : TextView = itemView.findViewById(R.id.tvBankName)
    private val tvBankAccountNumber : TextView = itemView.findViewById(R.id.tvBankAccountNumber)
    private val tvBankAccountHolderName : TextView = itemView.findViewById(R.id.tvBankAccountHolderName)
    private val btnIsiData : View = itemView.findViewById(R.id.btnIsiData)
    private val tvAccountPending : View = itemView.findViewById(R.id.tvAccountPending)
    private val btnHapus : View = itemView.findViewById(R.id.btnHapus)


    private val ticker: Ticker = view.findViewById(R.id.tickerBankAccount)

    var listener: BankAccountClickListener? = null

    fun bind(bankAccount: BankAccount, listener: BankAccountClickListener?) {
        view.tag = bankAccount
        this.listener = listener
        tvBankName.text = bankAccount.bankName ?: ""
        tvBankAccountNumber.text = bankAccount.accNumber ?: ""
        tvBankAccountHolderName.text = bankAccount.accName?.let { "a.n $it" } ?: ""
        ivBankImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
        bankAccount.bankImageUrl?.let {
            ivBankImage.loadImage(bankAccount.bankImageUrl)
        }
        ticker.gone()
        setBankStatus(
                status = bankAccount.statusFraud,
                copyWriting = bankAccount.copyWriting
        )

        addClickListener()
    }

    private fun addClickListener() {
        btnIsiData.setOnClickListener { listener?.onClickDataContent(view.tag as BankAccount) }
        btnHapus.setOnClickListener { listener?.deleteBankAccount(view.tag as BankAccount) }
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
        tvBankName.isEnabled = false
        tvBankAccountNumber.isEnabled = false
        tvBankAccountHolderName.isEnabled = false
        ivBankImage.alpha = DISABLE_IMAGE_ALPHA
    }

    private fun enableBankItem() {
        tvBankName.isEnabled = true
        tvBankAccountNumber.isEnabled = true
        tvBankAccountHolderName.isEnabled = true
        ivBankImage.alpha = ENABLE_IMAGE_ALPHA
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
            true -> btnIsiData.visible()
            else -> btnIsiData.gone()
        }
    }

    private fun hideShowPendingAccountTag(isShow: Boolean) {
        when (isShow) {
            true -> tvAccountPending.visible()
            else -> tvAccountPending.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank_account
    }
}
