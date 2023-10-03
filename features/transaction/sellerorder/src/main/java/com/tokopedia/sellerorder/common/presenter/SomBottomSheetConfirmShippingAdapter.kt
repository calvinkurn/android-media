package com.tokopedia.sellerorder.common.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerorder.common.presenter.model.ConfirmShippingNotes
import com.tokopedia.sellerorder.databinding.BottomsheetConfirmshippingItemBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by irpan on 30/08/23.
 */
class SomBottomSheetConfirmShippingAdapter : RecyclerView.Adapter<SomBottomSheetConfirmShippingAdapter.ViewHolder>() {
    private var list: MutableList<ConfirmShippingNotes> = mutableListOf()

    companion object {
        private const val ADDITIONAL_INDEX_POSITION_CLICKABLE_START = 2
        private const val ADDITIONAL_INDEX_POSITION_CLICKABLE_END = 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListInfo(listNotes: List<ConfirmShippingNotes>) {
        if (listNotes.isEmpty()) return
        list.clear()
        list = listNotes.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(BottomsheetConfirmshippingItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.apply {
            tvInfoNumber.text = "${position.plus(1)}"

            val notes = list.getOrNull(position)
            val spannableString = notes?.let { getNotes(holder.itemView.context, it) }

            tvInfo.apply {
                movementMethod = LinkMovementMethod.getInstance()
                isClickable = true
                setText(spannableString, TextView.BufferType.SPANNABLE)
            }
        }
    }

    private fun getNotes(context: Context, notes: ConfirmShippingNotes): SpannableString {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                gotoWebView(context, notes.url)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_GN500)
            }
        }
        var spannableString = SpannableString(notes.noteText)

        if (notes.url.isNotEmpty()) {
            val text = "${notes.noteText} ${notes.urlText}"
            spannableString = SpannableString(text)
            val startSpan = notes.noteText.lastIndex + ADDITIONAL_INDEX_POSITION_CLICKABLE_START
            val endSpan = text.lastIndex + ADDITIONAL_INDEX_POSITION_CLICKABLE_END
            val boldSpan = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(boldSpan, startSpan, endSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(clickableSpan, startSpan, endSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    fun gotoWebView(context: Context, url: String) {
        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$url")
    }

    inner class ViewHolder(binding: BottomsheetConfirmshippingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding by viewBinding<BottomsheetConfirmshippingItemBinding>()
    }
}
