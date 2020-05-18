package com.tokopedia.product.addedit.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.viewholder.NameRecommendationViewHolder

class NameRecommendationAdapter(private val clickListener: ProductNameItemClickListener)
    : RecyclerView.Adapter<NameRecommendationViewHolder>() {

    interface ProductNameItemClickListener {
        fun onNameItemClicked(productName: String)
    }

    private var nameInput: String = ""
    private var nameRecommendations: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameRecommendationViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.name_recommendation_item, parent, false)
        return NameRecommendationViewHolder(rootView, clickListener)
    }

    override fun getItemCount(): Int {
        return nameRecommendations.size
    }

    override fun onBindViewHolder(holder: NameRecommendationViewHolder, position: Int) {
        val productNameRecommendation = nameRecommendations[position]
        holder.bindData(nameInput, productNameRecommendation)
    }

    fun setProductNameRecommendations(nameRecommendations: List<String>) {
        this.nameRecommendations = nameRecommendations
        notifyDataSetChanged()
    }

    fun setProductNameInput(productNameInput: String) {
        this.nameInput = productNameInput
    }
}