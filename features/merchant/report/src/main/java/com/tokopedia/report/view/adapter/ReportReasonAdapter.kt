package com.tokopedia.report.view.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
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
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.webview.BaseSimpleWebViewActivity
import kotlinx.android.synthetic.main.item_footer.view.title as titleFooter
import kotlinx.android.synthetic.main.item_header.view.title as titleHeader
import kotlinx.android.synthetic.main.item_report_type.view.*

class ReportReasonAdapter(private val listener: OnReasonClick,
                          private val tracking: MerchantReportTracking): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val filteredId = mutableListOf<Int>()

    private var title: String = ""
    private var baseParent: ProductReportReason? = null

    private val reasons = mutableListOf<ProductReportReason>()
    private val filteredReasons = mutableListOf<ProductReportReason>()

    fun changeList(_reasons: List<ProductReportReason>){
        filteredId.clear()
        reasons.clear()
        reasons.addAll(_reasons)
        updateFilteredReasons()
        notifyDataSetChanged()
    }

    private fun updateFilteredReasons(){
        filteredReasons.clear()
        val id = filteredId.lastOrNull() ?: -1
        if (id <= 0){
            title = ""
            filteredReasons.addAll(reasons)
        } else {
            val reason = reasons.firstOrNull { it.categoryId == id }
            title = reason?.value ?: ""
            filteredReasons.addAll(reason?.children ?: listOf())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            HEADER_TYPE -> HeaderViewHolder(parent.inflateLayout(R.layout.item_header))
            FOOTER_TYPE -> FooterViewHolder(parent.inflateLayout(R.layout.item_footer))
            else -> ItemViewHolder(parent.inflateLayout(R.layout.item_report_type))
        }
    }

    override fun getItemCount(): Int = filteredReasons.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0){
            (holder as HeaderViewHolder).setTitle(title)
        } else if (position < itemCount - 1){
            (holder as ItemViewHolder).bind(filteredReasons[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> HEADER_TYPE
            itemCount-1 -> FOOTER_TYPE
            else -> ITEM_TYPE
        }
    }

    fun back() {
        filteredId.removeAt(filteredId.size - 1)
        if (filteredId.isEmpty())
            baseParent = null
        updateFilteredReasons()
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun setTitle(title: String){
            itemView.titleHeader.text = if (title.isBlank()) itemView.context
                    .getString(R.string.product_report_header) else title
        }
    }

    inner class FooterViewHolder(view: View): RecyclerView.ViewHolder(view){
        init {
            with(itemView){
                titleFooter.movementMethod = LinkMovementMethod.getInstance()
                val spannable = MethodChecker.fromHtml(context.getString(R.string.product_report_see_all_types)) as Spannable
                spannable.getSpans(0, spannable.length, URLSpan::class.java).forEach {
                    val start = spannable.getSpanStart(it)
                    val end = spannable.getSpanEnd(it)
                    spannable.removeSpan(it)
                    val urlSpan = WebViewURLSpan( it.url).apply {
                        listener = object : WebViewURLSpan.OnClickListener {
                            override fun onClick(url: String) {
                                tracking.eventReportLearnMore()
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
                titleFooter.text = spannable
            }
        }
    }

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(reason: ProductReportReason){
            with(itemView){
                title.text = reason.value
                if (reason.detail.isBlank() || filteredId.isEmpty()) subtitle.gone()
                else {
                    subtitle.text = reason.detail
                    subtitle.visible()
                }
            }

            itemView.setOnClickListener {
                if (reason.children.isNotEmpty()) {
                    tracking.eventReportReason(reason.strLabel)
                    if (filteredId.isEmpty()){
                        baseParent = reason
                    }
                    filteredId.add(reason.categoryId)
                    updateFilteredReasons()
                    notifyDataSetChanged()
                    listener.scrollToTop()
                } else {
                    val fieldReason = if (baseParent != null && filteredId.isNotEmpty()){
                        reason.copy(additionalInfo = baseParent!!.additionalInfo,
                                additionalFields = baseParent!!.additionalFields).apply {
                            parentLabel = baseParent!!.strLabel
                        }
                    } else reason

                    listener.gotoForm(fieldReason)
                }
            }
        }
    }

    interface OnReasonClick{
        fun scrollToTop()
        fun gotoForm(reason: ProductReportReason)
    }

    companion object{
        private const val HEADER_TYPE = 1
        private const val ITEM_TYPE = 2
        private const val FOOTER_TYPE = 3
    }
}