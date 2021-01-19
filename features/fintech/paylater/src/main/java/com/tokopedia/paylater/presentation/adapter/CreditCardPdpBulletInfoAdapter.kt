package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.presentation.viewholder.CreditCardPdpInfoBulletViewHolder
import com.tokopedia.paylater.presentation.viewholder.CreditCardPdpInfoViewHolder

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