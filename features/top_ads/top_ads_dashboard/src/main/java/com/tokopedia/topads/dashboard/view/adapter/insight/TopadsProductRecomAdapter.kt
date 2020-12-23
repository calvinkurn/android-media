package com.tokopedia.topads.dashboard.view.adapter.insight

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import kotlinx.android.synthetic.main.topads_dash_recon_product_item.view.*

class TopadsProductRecomAdapter(var itemSelected: () -> Unit) : RecyclerView.Adapter<TopadsProductRecomAdapter.ViewHolder>() {
    var items: MutableList<ProductRecommendation> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_dash_recon_product_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getSelectedIds(): MutableList<String> {
        val selectedItems: MutableList<String> = mutableListOf()
        items.forEach {
            if (it.isChecked) {
                selectedItems.add(it.productId)
            }
        }
        return selectedItems
    }

     fun setAllChecked(checked: Boolean) {
        items.forEach {
            it.isChecked = checked
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[holder.adapterPosition]) {
            holder.view.productName.text = productName
            holder.view.img_folder.loadImage(imgUrl)
            val totalSearch = Html.fromHtml(String.format(holder.view.context.getString(R.string.topads_dash_total_search_pattern), "$searchPercentage%", searchCount.thousandFormatted()))
            holder.view.totalSearch.text = totalSearch
            val recommendationBid = "Rp" + convertToCurrency(recomBid.toLong()) + holder.view.context.getString(com.tokopedia.topads.common.R.string.topads_common_klik_)
            holder.view.recommendedBid.text = recommendationBid
            holder.view.editBudget.textFieldInput.setText(convertToCurrency(recomBid.toLong()))
            holder.view.cb_product_recom.isChecked = isChecked
            holder.view.setOnClickListener {
                holder.view.cb_product_recom.isChecked = !holder.view.cb_product_recom.isChecked
                isChecked = holder.view.cb_product_recom.isChecked
                itemSelected.invoke()
            }
            holder.view.editBudget?.textFieldInput?.addTextChangedListener(object : NumberTextWatcher(holder.view.editBudget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    setCurrentBid = number.toInt()
                    when {
                        number < recomBid.toDouble() && number > minBid -> {
                            holder.view.editBudget?.setError(false)
                            holder.view.editBudget?.setMessage(Html.fromHtml(String.format(holder.view.context.getString(R.string.topads_dash_budget_recom_error), recomBid)))
                        }
                        number < minBid -> {
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_min_budget_error))
                        }
                        else -> {
                            holder.view.editBudget?.setError(false)
                            holder.view.editBudget?.setMessage("")
                        }
                    }
                }
            })
        }
    }

}
