package com.tokopedia.createpost.view.adapter

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.createpost.SuggestionItemHandler
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.viewmodel.ProductSuggestionItem
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_af_product_suggestion.view.*

/**
 * @author by milhamj on 2019-09-17.
 */

class ProductSuggestionAdapter(
        private var onSuggestionItemClicked: SuggestionItemHandler,
        private var onSuggestionItemFirstView: SuggestionItemHandler
) : RecyclerView.Adapter<ProductSuggestionAdapter.SuggestionViewHolder>() {

    private val list: MutableList<ProductSuggestionItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        return SuggestionViewHolder(
                parent.inflateLayout(R.layout.item_af_product_suggestion),
                onSuggestionItemClicked,
                onSuggestionItemFirstView
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val element = list[position]
        holder.bind(element)
    }

    fun addAll(list: List<ProductSuggestionItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean = this.list.isEmpty()

    class SuggestionViewHolder(
            v: View,
            private var onSuggestionItemClicked: SuggestionItemHandler,
            private var onSuggestionItemFirstView: SuggestionItemHandler)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: ProductSuggestionItem) {
            with(itemView) {
                setOnClickListener { onSuggestionItemClicked(element) }

                image.loadImage(element.imageUrl)
                title.text = element.title
                price.text = element.price

                if (element.type == ProductSuggestionItem.TYPE_AFFILIATE) {
                    setAffiliateStyle(itemView)
                } else {
                    setDefaultStyle(itemView)
                }
                addOnImpressionListener(element.impressHolder) {
                    onSuggestionItemFirstView(element)
                }

            }

        }

        private fun setAffiliateStyle(itemView: View) {
            with(itemView) {
                bymeLogo.visible()
                title.text = context.getString(R.string.af_get_commission)
                title.setWeight(Typography.REGULAR)
                title.setTextColor(MethodChecker.getColor(context, R.color.clr_ae31353b))
                price.setTextColor(MethodChecker.getColor(context, R.color.af_commission_blue))
            }
        }

        private fun setDefaultStyle(itemView: View) {
            with(itemView) {
                bymeLogo.gone()
                title.setWeight(Typography.BOLD)
                title.setTextColor(MethodChecker.getColor(context, R.color.clr_f531353b))
                price.setTextColor(MethodChecker.getColor(context, R.color.Yellow_Y500))
            }

        }
    }
}