package com.tokopedia.pdpsimulation.creditcard.presentation.tnc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder.CreditCardPdpInfoBulletViewHolder

class CreditCardPdpBulletInfoAdapter(var notesList: ArrayList<String>) : RecyclerView.Adapter<CreditCardPdpInfoBulletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardPdpInfoBulletViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CreditCardPdpInfoBulletViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CreditCardPdpInfoBulletViewHolder, position: Int) {
        holder.bindData(notesList[position])
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}