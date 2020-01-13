package com.tokopedia.salam.umrah.checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutMandatoryDocument
import kotlinx.android.synthetic.main.item_umrah_checkout_bottom_sheet_mandatory_document.view.*

/**
 * @author by firman on 21/11/19
 */

class UmrahCheckoutMandatoryDocumentAdapter : RecyclerView.Adapter<UmrahCheckoutMandatoryDocumentAdapter.UmrahCheckoutMandatoryDocumentAdapterViewHolder>() {

    private var listCategories = emptyList<UmrahCheckoutMandatoryDocument>()

    inner class UmrahCheckoutMandatoryDocumentAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(umrahTermCondition: UmrahCheckoutMandatoryDocument) {
            with(itemView) {
                tg_umrah_checkout_title_mandatory_document.text = umrahTermCondition.title
                tg_umrah_checkout_desc_mandatory_document.text = umrahTermCondition.desc
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahCheckoutMandatoryDocumentAdapterViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahCheckoutMandatoryDocumentAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_checkout_bottom_sheet_mandatory_document, parent, false)
        return UmrahCheckoutMandatoryDocumentAdapterViewHolder(itemView)
    }

    fun setList(list: List<UmrahCheckoutMandatoryDocument>) {
        listCategories = list
        notifyDataSetChanged()
    }

}