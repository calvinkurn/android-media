package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.viewholder.NameRecommendationViewHolder
import com.tokopedia.product.addedit.detail.presentation.viewholder.TitleCorrectionViewHolder

class TitleCorrectionAdapter : RecyclerView.Adapter<TitleCorrectionViewHolder>() {

    private var titleSubstrings: List<String> = listOf()
    private var errorKeywords: List<String> = listOf()
    private var warningKeywords: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleCorrectionViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_title_correction, parent, false)
        return TitleCorrectionViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return titleSubstrings.size
    }

    override fun onBindViewHolder(holder: TitleCorrectionViewHolder, position: Int) {
        val substring = titleSubstrings[position]
        val mode = when {
            errorKeywords.contains(substring) -> {
                TitleCorrectionViewHolder.Mode.ERROR
            }
            warningKeywords.contains(substring) -> {
                TitleCorrectionViewHolder.Mode.WARNING
            }
            else -> {
                TitleCorrectionViewHolder.Mode.NORMAL
            }
        }

        holder.bindData(substring, mode)
    }

    fun setTitle(title: String) {
        this.titleSubstrings = title.split( " ")
        notifyDataSetChanged()
    }

    fun setErrorKeywords(keywords: List<String>) {
        errorKeywords = keywords
    }

    fun setWarningKeywords(keywords: List<String>) {
        warningKeywords = keywords
    }
}