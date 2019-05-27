package com.tokopedia.report.view.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.InputFilter
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.text.style.WebViewURLSpan
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.report.R
import com.tokopedia.report.data.constant.GeneralConstant
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.webview.BaseSimpleWebViewActivity
import kotlinx.android.synthetic.main.item_header_form.view.*
import kotlinx.android.synthetic.main.item_link_form.view.*
import kotlinx.android.synthetic.main.item_photo_form.view.*
import kotlinx.android.synthetic.main.item_submit_form.view.*
import kotlinx.android.synthetic.main.item_textarea_form.view.*

class ReportFormAdapter(private val item: ProductReportReason) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Pair<String, Any>>()

    init {
        items.addAll(item.additionalInfo.map { it.type to it })
        items.addAll(item.additionalFields.asSequence().filterNot { it.type == "popup" || it.type == "text"}
                .map { it.type to it }.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_HEADER -> HeaderViewHolder(parent.inflateLayout(R.layout.item_header_form))
            TYPE_SUBMIT -> SubmitViewHolder(parent.inflateLayout(R.layout.item_submit_form))
            TYPE_LINK -> LinkViewHolder(parent.inflateLayout(R.layout.item_link_form))
            TYPE_TEXTAREA -> TextAreaViewHolder(parent.inflateLayout(R.layout.item_textarea_form))
            TYPE_PHOTO -> UploadPhotoViewHolder(parent.inflateLayout(R.layout.item_photo_form))
            else -> HeaderViewHolder(parent.inflateLayout(R.layout.item_header_form))
        }
    }

    override fun getItemCount(): Int = items.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0){
            (holder as HeaderViewHolder).bind(item.value, item.detail)
        } else if (position < itemCount - 1){
            val (_, field) = items[position - 1]
            if (holder is LinkViewHolder && field is ProductReportReason.AdditionalInfo){
                holder.bindLink(field.label, field.value)
            } else if (holder is TextAreaViewHolder && field is ProductReportReason.AdditionalField){
                holder.bind(field)
            } else if (holder is UploadPhotoViewHolder && field is ProductReportReason.AdditionalField){
                holder.bind(field)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> TYPE_HEADER
            itemCount - 1 -> TYPE_SUBMIT
            else -> when(items[position -1].first){
                "link" -> TYPE_LINK
                "textarea" -> TYPE_TEXTAREA
                "file" -> TYPE_PHOTO
                else -> super.getItemViewType(position)
            }
        }
    }

    inner class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(detail: String, descr: String){
            with(itemView){
                title.text = detail
                if (descr.isBlank()) description.gone()
                else {
                    description.text = descr
                    description.visible()
                }
            }
        }
    }

    inner class SubmitViewHolder(view: View): RecyclerView.ViewHolder(view){
        init {
            with(itemView){
                footer.movementMethod = LinkMovementMethod.getInstance()
                val spannable = MethodChecker.fromHtml(context.getString(R.string.product_report_see_all_types)) as Spannable
                spannable.getSpans(0, spannable.length, URLSpan::class.java).forEach {
                    val start = spannable.getSpanStart(it)
                    val end = spannable.getSpanEnd(it)
                    spannable.removeSpan(it)
                    val urlSpan = WebViewURLSpan( it.url).apply {
                        listener = object : WebViewURLSpan.OnClickListener {
                            override fun onClick(url: String) {
                                itemView.context.startActivity(BaseSimpleWebViewActivity.getStartIntent(
                                        itemView.context, GeneralConstant.URL_REPORT_TYPE
                                ))
                            }

                            override fun showUnderline() = false

                        }
                    }
                    spannable.setSpan(urlSpan, start, end, 0)
                    spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, 0)
                }
                footer.text = spannable
                if (item.additionalFields.isEmpty()){
                    btn_lapor.gone()
                } else {
                    btn_lapor.visible()
                }
            }
        }
    }

    inner class LinkViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bindLink(text: String, url: String){
            with(itemView){
                link.text = text
                link.setOnClickListener {
                    context.startActivity(BaseSimpleWebViewActivity.getStartIntent(context, url))
                }
            }
        }
    }

    inner class TextAreaViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(field: ProductReportReason.AdditionalField){
            with(itemView){
                textInputLayoutReport.setHint(field.value)
                textInputLayoutReport.counterMaxLength = field.max
                edit_text_report.filters = arrayOf(InputFilter.LengthFilter(field.max))
                edit_text_report.hint = context.getString(R.string.product_hint_product_report,
                        field.min.toString())
            }
        }
    }

    inner class UploadPhotoViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(field: ProductReportReason.AdditionalField){
            with(itemView){
                title_upload.text = context.getString(R.string.product_report_upload_title,
                        field.value, field.min, field.max)
                description_upload.text = field.detail
            }
        }
    }

    companion object{
        private const val TYPE_HEADER = 1
        private const val TYPE_LINK = 2
        private const val TYPE_TEXTAREA = 3
        private const val TYPE_PHOTO = 4
        private const val TYPE_SUBMIT = 5
    }
}