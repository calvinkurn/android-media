package com.tokopedia.report.view.adapter

import android.graphics.Typeface
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.InputFilter
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.text.style.WebViewURLSpan
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.report.R
import com.tokopedia.report.data.constant.GeneralConstant
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.view.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.item_header_form.view.*
import kotlinx.android.synthetic.main.item_link_form.view.*
import kotlinx.android.synthetic.main.item_photo_form.view.*
import kotlinx.android.synthetic.main.item_submit_form.view.*
import kotlinx.android.synthetic.main.item_textarea_form.view.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class ReportFormAdapter(private val item: ProductReportReason,
                        private val tracking: MerchantReportTracking,
                        private val inputDetailListener:((String, String, Int, Int) -> Unit),
                        private val addPhotoListener: ((String, Int) -> Unit),
                        private val submitForm: (()-> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var submitEnabled = false

    val trackingReasonLabel: String
        get() = item.value.toLowerCase(Locale.getDefault())

    private val items = mutableListOf<Pair<String, Any>>()
    val inputs = mutableMapOf<String, Any>()

    init {
        items.addAll(item.additionalInfo.map { it.type to it })
        items.addAll(item.additionalFields.asSequence()
                .filterNot { it.type == GeneralConstant.TYPE_POPUP || it.type == GeneralConstant.TYPE_TEXT}
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
        if (position == 0) {
            (holder as HeaderViewHolder).bind(item.value, item.detail)
        } else if (getItemViewType(position) == TYPE_SUBMIT){
            (holder as SubmitViewHolder).validateButtonSubmit()
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
                GeneralConstant.TYPE_LINK -> TYPE_LINK
                GeneralConstant.TYPE_TEXTAREA -> TYPE_TEXTAREA
                GeneralConstant.TYPE_FILE -> TYPE_PHOTO
                else -> super.getItemViewType(position)
            }
        }
    }

    fun updatePhotoForType(type: String, photoUri: List<String>) {
        val imgUriList: MutableList<String> = inputs[type] as? MutableList<String> ?: mutableListOf()
        imgUriList.addAll(photoUri)
        inputs[type] = imgUriList
        notifyDataSetChanged()
    }

    fun updateTextInput(key: String, input: String?, isButtonNeedEnabled: Boolean) {
        inputs[key] = input ?: ""
        submitEnabled = isButtonNeedEnabled
        notifyDataSetChanged()
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
                                tracking.eventReportLearnMore(item.value.toLowerCase(Locale.getDefault()))
                                RouteManager.route(itemView.context, "${ApplinkConst.WEBVIEW}?url=${GeneralConstant.URL_REPORT_TYPE}")
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
                btn_lapor.setOnClickListener {
                    tracking.eventReportClickDetail(item.value.toLowerCase(Locale.getDefault()))
                    submitForm.invoke()
                }
            }
        }

        fun validateButtonSubmit() {
            itemView.btn_lapor.isEnabled =  submitEnabled
        }
    }

    inner class LinkViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bindLink(text: String, url: String){
            with(itemView){
                link.text = text
                link.setOnClickListener {
                    tracking.eventReportClickLink(text, trackingReasonLabel)
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$url")
                }
            }
        }
    }

    inner class TextAreaViewHolder(view: View): RecyclerView.ViewHolder(view), ValidateViewHolder{
        private var minChar = -1
        private var maxChar = -1

        override fun validate(): Boolean {
            return if (minChar == -1 && maxChar == -1) true
            else {
                val input = itemView.edit_text_report.text.toString()
                with(itemView.textInputLayoutReport){
                    if (input.length in minChar..maxChar){
                        error = null
                        isErrorEnabled = false
                    } else {
                        error = context.getString(R.string.product_hint_product_report, minChar.toString())
                    }
                }
                input.isNotBlank() && input.length in minChar..maxChar
            }
        }

        fun bind(field: ProductReportReason.AdditionalField){
            with(itemView){
                minChar = field.min
                maxChar = field.max

                val input = inputs[field.key]?.toString() ?: ""
                textInputLayoutReport.hint = field.value
                textInputLayoutReport.counterMaxLength = field.max
                textInputLayoutReport.helperText = context.getString(R.string.product_helper_product_report,
                        field.min.toString())
                textInputLayoutReport.setHelperTextColor(ContextCompat.getColorStateList(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                edit_text_report.filters = arrayOf(InputFilter.LengthFilter(field.max))
                edit_text_report.setText(input)
                if (inputs[field.key] != null)
                    validate()

                edit_text_report.setOnClickListener {
                    inputDetailListener.invoke(field.key, edit_text_report.text.toString(), field.min, field.max)
                }
            }
        }
    }

    inner class UploadPhotoViewHolder(view: View): RecyclerView.ViewHolder(view), ValidateViewHolder{

        private var minChar = -1
        private var maxChar = -1
        private val photoAdapter: UploadPhotoAdapter by lazy { UploadPhotoAdapter("",
                addPhotoListener, this::afterRemovePhoto) }

        init {
            with(itemView.rv_uploaded_foto){
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(SpaceItemDecoration(context.resources.getDimensionPixelSize(R.dimen.dp_6),
                        LinearLayoutManager.HORIZONTAL))
            }
        }

        override fun validate(): Boolean {
            if (minChar == -1 && maxChar == -1) return true
            return photoAdapter.itemCount - 1 >= minChar
        }

        fun bind(field: ProductReportReason.AdditionalField){
            with(itemView){
                minChar = field.min
                maxChar = field.max

                photoAdapter.type = field.key
                photoAdapter.updateMax(field.max)
                val photoUris: List<String> = inputs[field.key] as? List<String> ?: listOf()
                photoAdapter.updatePhoto(photoUris)
                title_upload.text = context.getString(R.string.product_report_upload_title,
                        field.value, photoUris.size, field.max)
                description_upload.text = field.detail
                rv_uploaded_foto.adapter = photoAdapter
            }
        }

        private fun afterRemovePhoto(key: String, newList: List<String>){
            val photoUris: MutableList<String> = inputs[key] as? MutableList<String> ?: mutableListOf()
            photoUris.clear()
            photoUris.addAll(newList)
            notifyItemChanged(adapterPosition)
        }
    }

    interface ValidateViewHolder{
        fun validate(): Boolean
    }

    companion object{
        private const val TYPE_HEADER = 1
        private const val TYPE_LINK = 2
        private const val TYPE_TEXTAREA = 3
        private const val TYPE_PHOTO = 4
        private const val TYPE_SUBMIT = 5
    }
}