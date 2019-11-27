package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.pdp.data.UmrahFAQContent
import kotlinx.android.synthetic.main.item_umrah_pdp_faq.view.*

/**
 * @author by M on 8/11/19
 */
class UmrahPdpFaqAdapter : RecyclerView.Adapter<UmrahPdpFaqAdapter.UmrahPdpFaqViewHolder>() {
    lateinit var onItemListener: OnClickListener
    var faqs = emptyList<UmrahFAQContent>()
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpFaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_faq,parent,false)
        return UmrahPdpFaqViewHolder(view)
    }

    override fun getItemCount(): Int = faqs.size

    override fun onBindViewHolder(holder: UmrahPdpFaqViewHolder, position: Int) {
        holder.bind(faqs[position])
    }

    inner class UmrahPdpFaqViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(umrahFAQContent: UmrahFAQContent) {
            with(itemView){
                item_tg_umrah_pdp_faq_question.text = umrahFAQContent.header
                item_tg_umrah_pdp_faq_answer.text = umrahFAQContent.snippet
                item_tg_umrah_pdp_faq_read_more.setOnClickListener {
                    onItemListener.onClick(umrahFAQContent.link)
                }
            }
        }
    }

    interface OnClickListener{
        fun onClick(link: String)
    }
}