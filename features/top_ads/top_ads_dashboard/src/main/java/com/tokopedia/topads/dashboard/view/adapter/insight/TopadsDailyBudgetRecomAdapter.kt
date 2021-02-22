package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BUDGET_MULTIPLE_FACTOR
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.RECOMMENDATION_DAILY_MAX_BUDGET
import com.tokopedia.topads.dashboard.data.model.DataBudget
import com.tokopedia.utils.text.currency.NumberTextWatcher
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

    private fun calculatePotentialClick(holder: ViewHolder): Double {
        val budgetModel = items[holder.adapterPosition]
        var totalPotentialClick = 0.0
        totalPotentialClick += if (budgetModel.setCurrentBid
                >= budgetModel.priceDaily) {
            ((budgetModel.setCurrentBid
                    - budgetModel.priceDaily) / budgetModel.avgBid.toDouble())
        } else {
            ((budgetModel.suggestedPriceDaily - budgetModel.priceDaily) / budgetModel.avgBid.toDouble())
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
            holder.view.potentialClick.text = String.format(holder.view.context.getString(R.string.topads_dash_potential_click_text), calculatePotentialClick(holder).thousandFormatted())
            holder.view.buttonSubmitEdit.setOnClickListener {
                holder.view.buttonSubmitEdit.isLoading = true
                onBudgetClicked(holder.adapterPosition)
            }
            holder.view.editBudget?.textFieldInput?.addTextChangedListener(object : NumberTextWatcher(holder.view.editBudget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    items[holder.adapterPosition].setCurrentBid = number
                    holder.view.potentialClick.text = String.format(holder.view.context.getString(R.string.topads_dash_potential_click_text), calculatePotentialClick(holder).thousandFormatted())
                    when {
                        number < items[holder.adapterPosition].suggestedPriceDaily && number > items[holder.adapterPosition].priceDaily -> {
                            holder.view.buttonSubmitEdit.isEnabled = true
                            holder.view.editBudget?.setError(false)
                            holder.view.editBudget?.setMessage(String.format(holder.view.context.getString(R.string.topads_dash_budget_recom_error), suggestedPriceDaily.toInt()))
                        }
                        number < items[holder.adapterPosition].priceDaily -> {
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_min_budget_error))
                            holder.view.buttonSubmitEdit.isEnabled = false
                        }
                        number > RECOMMENDATION_DAILY_MAX_BUDGET.toDouble() -> {
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_max_budget_error))
                            holder.view.buttonSubmitEdit.isEnabled = false
                        }
                        number.toInt() % BUDGET_MULTIPLE_FACTOR != 0 -> {
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(String.format(holder.view.context.getString(com.tokopedia.topads.common.R.string.topads_common_error_multiple_50), BUDGET_MULTIPLE_FACTOR))
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
