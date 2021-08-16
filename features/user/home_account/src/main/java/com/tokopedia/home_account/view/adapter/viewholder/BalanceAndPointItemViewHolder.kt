package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountItemBalanceAndPointBinding
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener
import com.tokopedia.utils.view.binding.viewBinding

class BalanceAndPointItemViewHolder(
    private val balanceAndPointListener: BalanceAndPointListener,
    private val view: View
) : BaseViewHolder(view) {

    private val binding: HomeAccountItemBalanceAndPointBinding? by viewBinding()

    fun bind(item: BalanceAndPointUiModel?) {
        setImage(item?.urlImage)
        setTitleText(item?.type ?: 0, item?.title)
        setSubtitleText(item?.id, item?.type ?: 0, item?.subtitle)
        setClickLitener(item?.type ?: 0, balanceAndPointListener)
    }

    private fun setImage(url: String?) {
        url?.let { binding?.homeAccountItemBalanceAndPointIcon?.setImageUrl(it) }
    }

    private fun setTitleText(type: Int, text: String?) {
        val title = when (type) {
            DEFAULT_TYPE -> {
                text
            }
            NOT_LINKED_TYPE -> {
                setTitleToGreen()
                "Aktivasi"
            }
            ZERO_BALANCE_TYPE -> {
                setTitleToGreen()
                "Tambah Points"
            }
            FAILED_TO_LOAD_TYPE -> {
                setTitleToGreen()
                "Muat Ulang"
            }
            else -> {
                text
            }
        }
        binding?.homeAccountItemBalanceAndPointTitle?.text = title
    }

    private fun setTitleToGreen() {
        binding?.homeAccountItemBalanceAndPointTitle?.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.Unify_G500
            )
        )
    }

    private fun setSubtitleText(id: String?, type: Int, text: String?) {
        val subtitle = if (type == FAILED_TO_LOAD_TYPE) {
            when (id) {
                OVO_BALANCE_ID -> {
                    "OVO"
                }
                GOPAY_BALANCE_ID -> {
                    "GoPay"
                }
                TOKOPOINT_BALANCE_ID -> {
                    "Tokopoint"
                }
                SALDO_BALANCE_ID -> {
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

    private fun setClickLitener(type: Int, listener: BalanceAndPointListener) {
        binding?.container?.setOnClickListener {
            listener.onClickBalanceAndPoint(type)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_account_item_balance_and_point

        const val OVO_BALANCE_ID = "ovo"
        const val GOPAY_BALANCE_ID = "gopay"
        const val TOKOPOINT_BALANCE_ID = "tokopoint"
        const val SALDO_BALANCE_ID = "saldo"

        const val DEFAULT_TYPE = 0
        const val NOT_LINKED_TYPE = 1
        const val ZERO_BALANCE_TYPE = 2
        const val FAILED_TO_LOAD_TYPE = 3
    }
}