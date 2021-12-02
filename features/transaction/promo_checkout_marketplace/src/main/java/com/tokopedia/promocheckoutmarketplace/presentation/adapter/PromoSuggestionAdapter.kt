package com.tokopedia.promocheckoutmarketplace.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoSuggestionBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutSuggestionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoSuggestionItemUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoSuggestionViewHolder

class PromoSuggestionAdapter(val actionListener: PromoCheckoutSuggestionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = ArrayList<PromoSuggestionItemUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding = PromoCheckoutMarketplaceModuleItemPromoSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return PromoSuggestionViewHolder(viewBinding, actionListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PromoSuggestionViewHolder).bindData(data[position])
    }

}