package com.tokopedia.shop.open.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainSuggestionResult
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.listener.InputShopInterface
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampInputShopFragment

class ShopOpenRevampShopsSuggestionAdapter(
        val listener: InputShopInterface
) : RecyclerView.Adapter<ShopOpenRevampShopsSuggestionAdapter.ShopOpenRevampShopsSuggestionViewHolder>() {

    private var shopDomainNameSuggestions: MutableList<String> = mutableListOf()
    var selectedPosition = ShopOpenRevampInputShopFragment.DEFAULT_SELECTED_POSITION

    fun updateDataShopSuggestions(shopDomains: ValidateShopDomainSuggestionResult) {
        shopDomainNameSuggestions = shopDomains.shopDomains.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopOpenRevampShopsSuggestionViewHolder {
        return ShopOpenRevampShopsSuggestionViewHolder(parent.inflateLayout(R.layout.shop_open_revamp_item_shop_suggestion))
    }

    override fun getItemCount(): Int {
        return shopDomainNameSuggestions.size
    }

    override fun onBindViewHolder(holder: ShopOpenRevampShopsSuggestionViewHolder, position: Int) {
        holder.bindData(shopDomainNameSuggestions[position], position)
    }

    inner class ShopOpenRevampShopsSuggestionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var chipTextView: TextView
        var chipContainer: LinearLayout
        val context: Context

        init {
            context = itemView.context
            chipTextView = itemView.findViewById(R.id.chip_textview)
            chipContainer = itemView.findViewById(R.id.chip_container_domain_suggestion)
        }

        fun bindData(shopDomainItem: String, position: Int ) {
            chipTextView.text = shopDomainItem
            if (selectedPosition == position) {
                chipContainer.background = ContextCompat.getDrawable(context, R.drawable.unify_chip_selected_small)
                chipTextView.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
            } else {
                chipContainer.background = ContextCompat.getDrawable(context, R.drawable.unify_chip_normal_small)
                chipTextView.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200))
            }
            chipTextView.setOnClickListener {
                with(itemView.context){
                    listener.onClickedSuggestion(shopDomainItem, position)
                }
            }
        }
    }

}
