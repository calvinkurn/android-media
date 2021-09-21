package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.viewholder.TypoCorrectionViewHolder

class TypoCorrectionAdapter : RecyclerView.Adapter<TypoCorrectionViewHolder>() {

    private var typoKeywords: List<Pair<String, String>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypoCorrectionViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_typo_correction, parent, false)
        return TypoCorrectionViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return typoKeywords.size
    }

    override fun onBindViewHolder(holder: TypoCorrectionViewHolder, position: Int) {
        holder.bindData(typoKeywords[position])
    }

    fun setTypoKeywords(typoKeywords: List<Pair<String, String>>) {
        this.typoKeywords = typoKeywords
        notifyDataSetChanged()
    }
}