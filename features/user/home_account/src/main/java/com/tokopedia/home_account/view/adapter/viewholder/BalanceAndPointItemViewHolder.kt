package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountItemBalanceAndPointBinding
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.utils.view.binding.viewBinding

class BalanceAndPointItemViewHolder(
    private val balanceAndPointListener: BalanceAndPointListener,
    private val view: View
) : BaseViewHolder(view) {

    private val binding: HomeAccountItemBalanceAndPointBinding? by viewBinding()

    fun bind(item: BalanceAndPointUiModel?) {
        setImage(item?.urlImage)
        setTitleText(item?.isFailed.orFalse(), item?.title)
        setSubtitleText(item?.id, item?.isFailed.orFalse(), item?.subtitle)
        setClickLitener(
            item?.id,
            item?.applink,
            item?.weblink,
            item?.isFailed.orFalse(),
            balanceAndPointListener
        )
    }

    private fun setImage(url: String?) {
        url?.let { binding?.homeAccountItemBalanceAndPointIcon?.setImageUrl(it) }
    }

    private fun setTitleText(isFailed: Boolean, text: String?) {
        val title = if (isFailed) {
            "Muat Ulang"
        } else {
            text
        }

        binding?.homeAccountItemBalanceAndPointTitle?.text = title

        when (text) {
            "Aktifkan", "Tambah Points", "Muat Ulang" -> {
                setTitleToGreen()
            }
        }
    }

    private fun setTitleToGreen() {
        binding?.homeAccountItemBalanceAndPointTitle?.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.Unify_G500
            )
        )
    }

    private fun setSubtitleText(id: String?, isFailed: Boolean, text: String?) {
        val subtitle = if (isFailed) {
            when (id) {
                AccountConstants.WALLET.OVO -> {
                    "OVO"
                }
                AccountConstants.WALLET.GOPAY -> {
                    "GoPay"
                }
                AccountConstants.WALLET.TOKOPOINT -> {
                    "Tokopoint"
                }
                AccountConstants.WALLET.SALDO -> {
                    "Saldo Tokopedia"
                }
                else -> {
                    text
                }
            }
        } else {
            text
        }
        binding?.homeAccountItemBalanceAndPointSubtitle?.text = subtitle
    }

    private fun setClickLitener(
        id: String?,
        applink: String?,
        weblink: String?,
        isFailed: Boolean,
        listener: BalanceAndPointListener
    ) {
        binding?.container?.setOnClickListener {
            id?.let { id -> listener.onClickBalanceAndPoint(id, applink, weblink, isFailed) }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_account_item_balance_and_point
    }
}