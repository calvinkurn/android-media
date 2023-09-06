package com.tokopedia.sellerorder.common.presenter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.model.ConfirmShippingNotes
import com.tokopedia.sellerorder.databinding.BottomsheetConfirmshippingItemBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by irpan on 30/08/23.
 */
class SomBottomSheetConfirmShippingAdapter(
) : RecyclerView.Adapter<SomBottomSheetConfirmShippingAdapter.ViewHolder>() {
    private var list: List<ConfirmShippingNotes> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateListInfo(list: List<ConfirmShippingNotes>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_confirmshipping_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notes = list[position]
        var spannableString = SpannableString(list[position].noteText)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                RouteManager.route(holder.itemView.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, notes.url))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    holder.itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            }
        }

        holder.binding?.apply {

            tvInfoNumber.text = "${position.plus(1)}"

            if (notes.url.isNotEmpty()) {

                val text = "${notes.noteText} ${notes.urlText}"
                spannableString = SpannableString(text)
                val startSpan = notes.noteText.lastIndex + 2
                val endSpan = text.lastIndex + 1
                val boldSpan = StyleSpan(Typeface.BOLD)
                spannableString.setSpan(boldSpan, startSpan, endSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(clickableSpan, startSpan, endSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tvInfo.apply {
                    movementMethod = LinkMovementMethod.getInstance()
                    isClickable = true
                    setText(spannableString, TextView.BufferType.SPANNABLE)
                }
            } else {
                tvInfo.text = spannableString

            }

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding by viewBinding<BottomsheetConfirmshippingItemBinding>()
    }
}
