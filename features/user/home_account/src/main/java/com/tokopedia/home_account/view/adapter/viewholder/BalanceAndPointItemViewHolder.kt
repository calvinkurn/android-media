package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountItemBalanceAndPointBinding
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.utils.view.binding.viewBinding
import java.util.regex.Pattern

class BalanceAndPointItemViewHolder(
    private val balanceAndPointListener: BalanceAndPointListener,
    private val view: View
) : BaseViewHolder(view) {

    private val binding: HomeAccountItemBalanceAndPointBinding? by viewBinding()

    fun bind(item: BalanceAndPointUiModel?) {
        setImage(item?.urlImage)
        setTitleText(item?.isFailed.orFalse(), item?.title)
        setSubtitleText(item)
        item?.let {
            setClickLitener(
                it,
                balanceAndPointListener
            )
        }
    }

    fun getSubTitle(): String {
        return binding?.homeAccountItemBalanceAndPointSubtitle?.text.toString()
    }

    private fun setImage(url: String?) {
        url?.let { binding?.homeAccountItemBalanceAndPointIcon?.setImageUrl(it) }
    }

    private fun setTitleText(isFailed: Boolean, text: String?) {
        val title = if (isFailed) {
            getString(R.string.home_account_balance_and_point_retry)
        } else {
            text
        }

        binding?.homeAccountItemBalanceAndPointTitle?.text = title

        if (!checkAnyNumbers(text ?: "")) {
            setTitleToGreen()
        }
    }

    private fun checkAnyNumbers(text: String): Boolean {
        val pattern = Pattern.compile(NUMBER_ONLY_REGEX)
        val matcher = pattern.matcher(text)
        return matcher.find()
    }

    private fun setTitleToGreen() {
        binding?.homeAccountItemBalanceAndPointTitle?.setTextColor(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_G500
            )
        )
    }

    private fun setSubtitleText(item: BalanceAndPointUiModel?) {
        binding?.homeAccountItemBalanceAndPointSubtitle?.text = if (item?.isFailed == true) {
            item.title
        } else {
            item?.subtitle
        }
        binding?.homeAccountItemBalanceAndPointSubtitle?.setTextColor(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700
            )
        )
    }

    private fun setClickLitener(
        balanceAndPointUiModel: BalanceAndPointUiModel,
        listener: BalanceAndPointListener
    ) {
        binding?.container?.setOnClickListener {
            listener.onClickBalanceAndPoint(balanceAndPointUiModel)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_account_item_balance_and_point

        private const val NUMBER_ONLY_REGEX = "\\d"
    }
}