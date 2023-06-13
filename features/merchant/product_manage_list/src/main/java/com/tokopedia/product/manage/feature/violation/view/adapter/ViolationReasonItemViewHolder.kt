package com.tokopedia.product.manage.feature.violation.view.adapter

import android.content.Context
import android.text.TextPaint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemProductManageViolationBinding
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class ViolationReasonItemViewHolder(itemView: View,
                                    private val listener: Listener): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_violation

        private const val ROBOTO_TYPOGRAPHY_FONT = "RobotoRegular.ttf"
    }

    private val binding by viewBinding<ItemProductManageViolationBinding>()

    fun bind(detail: String) {
        binding?.tvProductManageViolationItemNumber?.text = (adapterPosition + 1).toString()
        setDetailHtmlSpan(detail)
    }

    private fun setDetailHtmlSpan(detail: String) {
        itemView.context?.let { context ->
            val textColorInt = MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_G500
            )
            binding?.tvProductManageViolationItemDetail?.run {
                setClickableUrlHtml(
                    htmlText = detail,
                    applyCustomStyling = {
                        isUnderlineText = false
                        color = textColorInt
                        applyTypographyFont(context)
                    },
                    onUrlClicked = { link, _ ->
                        listener.onLinkClicked(link)
                    }
                )
            }
        }
    }

    private fun TextPaint.applyTypographyFont(context: Context) {
        try {
            typeface = com.tokopedia.unifyprinciples.getTypeface(context, ROBOTO_TYPOGRAPHY_FONT)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    interface Listener {
        fun onLinkClicked(link: String)
    }

}
