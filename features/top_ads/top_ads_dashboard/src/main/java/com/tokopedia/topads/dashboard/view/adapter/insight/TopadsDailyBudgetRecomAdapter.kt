package com.tokopedia.topads.dashboard.view.adapter.insight

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.DataBudget
import kotlinx.android.synthetic.main.topads_dash_recon_daily_budget_item.view.*

class TopadsDailyBudgetRecomAdapter(private val onBudgetClicked: ((pos: Int) -> Unit)) : RecyclerView.Adapter<TopadsDailyBudgetRecomAdapter.ViewHolder>() {
    var items: MutableList<DataBudget> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_dash_recon_daily_budget_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun calculatePotentialClick(holder: ViewHolder): Int {
        val budgetModel = items[holder.adapterPosition]
        var totalPotentialClick = 0
        totalPotentialClick += if (budgetModel.setCurrentBid
                >= budgetModel.priceDaily) {
            (budgetModel.setCurrentBid
                    - budgetModel.priceDaily) / budgetModel.avgBid
        } else {
            (budgetModel.suggestedPriceDaily - budgetModel.priceDaily) / budgetModel.avgBid
        }
        return totalPotentialClick
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[holder.adapterPosition]) {
            holder.view.buttonSubmitEdit.isLoading = false
            holder.view.groupName.text = groupName
            val dailyBudget = convertToCurrency(priceDaily.toLong()) + holder.view.context.getString(com.tokopedia.topads.common.R.string.topads_common_hari_)
            holder.view.daily_budget_value.text = dailyBudget
            val recommendationBid = "Rp" + convertToCurrency(suggestedPriceDaily.toLong()) + holder.view.context.getString(com.tokopedia.topads.common.R.string.topads_common_hari_)
            holder.view.recom_budget.text = recommendationBid
            holder.view.editBudget?.textFieldInput?.setText(convertToCurrency(suggestedPriceDaily.toLong()))
            setCurrentBid = suggestedPriceDaily
            holder.view.potentialClick.text = String.format(holder.view.context.getString(R.string.topads_dash_potential_click_text), calculatePotentialClick(holder))
            holder.view.buttonSubmitEdit.setOnClickListener {
                holder.view.buttonSubmitEdit.isLoading = true
                onBudgetClicked(holder.adapterPosition)
            }
            holder.view.editBudget?.textFieldInput?.addTextChangedListener(object : NumberTextWatcher(holder.view.editBudget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    setCurrentBid = number.toInt()
                    holder.view.potentialClick.text = calculatePotentialClick(holder).toString()
                    when {
                        number < suggestedPriceDaily.toDouble() && number > priceDaily -> {
                            holder.view.buttonSubmitEdit.isEnabled = true
                            holder.view.editBudget?.setMessage(Html.fromHtml(String.format(holder.view.context.getString(R.string.topads_dash_budget_recom_error), suggestedPriceDaily)))
                        }
                        number < priceDaily -> {
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_budget_recom_min_bid_error))
                            holder.view.buttonSubmitEdit.isEnabled = false
                        }
                        else -> {
                            holder.view.editBudget?.setError(false)
                            holder.view.editBudget?.setMessage("")
                            holder.view.buttonSubmitEdit.isEnabled = true
                        }
                    }
                }
            })
        }
    }
}
