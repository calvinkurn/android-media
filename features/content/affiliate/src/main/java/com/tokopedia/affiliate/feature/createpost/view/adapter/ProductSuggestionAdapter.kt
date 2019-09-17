package com.tokopedia.affiliate.feature.createpost.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.ProductSuggestionItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_af_product_suggestion.view.*

/**
 * @author by milhamj on 2019-09-17.
 */
class ProductSuggestionAdapter :
        RecyclerView.Adapter<ProductSuggestionAdapter.SuggestionViewHolder>() {

    private val list: MutableList<ProductSuggestionItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        return SuggestionViewHolder(parent.inflateLayout(R.layout.item_af_product_suggestion))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val element = list[position]
        with(holder.itemView)  {
            image.loadImage(element.imageUrl)
            title.text = element.title
            price.text = element.price

            if (element.type == ProductSuggestionItem.TYPE_AFFILIATE) {
                bymeLogo.visible()
            } else {
                bymeLogo.gone()
            }
        }
    }

    fun addAll(list: List<ProductSuggestionItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class SuggestionViewHolder(v: View): RecyclerView.ViewHolder(v)
}