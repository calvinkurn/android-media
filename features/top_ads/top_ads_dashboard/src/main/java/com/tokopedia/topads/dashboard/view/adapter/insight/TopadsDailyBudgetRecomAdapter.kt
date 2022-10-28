package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.model.InsightDailyBudgetModel
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BUDGET_MULTIPLE_FACTOR
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.RECOMMENDATION_DAILY_MAX_BUDGET
import com.tokopedia.topads.dashboard.data.model.DataBudget
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.unifyprinciples.Typography

class TopadsDailyBudgetRecomAdapter(
    private val userSession: UserSessionInterface,
    private val onBudgetClicked: ((pos: Int) -> Unit),
) : RecyclerView.Adapter<TopadsDailyBudgetRecomAdapter.ViewHolder>() {
    var items: MutableList<DataBudget> = mutableListOf()
    private var dailyRecommendationModel = mutableListOf<InsightDailyBudgetModel>()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val groupName: Typography = view.findViewById(R.id.groupName)
        val dailyBudgetValue: Typography = view.findViewById(R.id.daily_budget_value)
        val recomBudget: Typography = view.findViewById(R.id.recom_budget)
        val potentialClick: Typography = view.findViewById(R.id.potentialClick)
        val editBudget: TextFieldUnify = view.findViewById(R.id.editBudget)
        val buttonSubmitEdit: UnifyButton = view.findViewById(R.id.buttonSubmitEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_dash_recon_daily_budget_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun calculatePotentialClick(holder: ViewHolder): Double {
        val budgetModel = items[holder.adapterPosition]
        var totalPotentialClick = 0.0
        totalPotentialClick += if (budgetModel.setCurrentBid
            >= budgetModel.priceDaily
        ) {
            ((budgetModel.setCurrentBid
                    - budgetModel.priceDaily) / budgetModel.avgBid.toDouble())
        } else {
            ((budgetModel.suggestedPriceDaily - budgetModel.priceDaily) / budgetModel.avgBid.toDouble())
        }
        return totalPotentialClick
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[holder.adapterPosition]) {
            holder.buttonSubmitEdit.isLoading = false
            holder.groupName.text = groupName
            val dailyBudget =
                convertToCurrency(priceDaily.toLong()) + holder.view.context.getString(com.tokopedia.topads.common.R.string.topads_common_hari_)
            holder.dailyBudgetValue.text = dailyBudget
            val recommendationBid =
                "Rp" + convertToCurrency(suggestedPriceDaily.toLong()) + holder.view.context.getString(
                    com.tokopedia.topads.common.R.string.topads_common_hari_)
            holder.recomBudget.text = recommendationBid
            holder.editBudget?.textFieldInput?.setText(convertToCurrency(suggestedPriceDaily.toLong()))
            setCurrentBid = suggestedPriceDaily
            holder.potentialClick.text =
                String.format(holder.view.context.getString(R.string.topads_dash_potential_click_text),
                    calculatePotentialClick(holder).thousandFormatted())
            holder.buttonSubmitEdit.setOnClickListener {
                holder.buttonSubmitEdit.isLoading = true
                onBudgetClicked(holder.adapterPosition)
            }
            setPotensiKlik = calculatePotentialClick(holder).toLong()
            holder.view.addOnImpressionListener(impressHolder) {
                dailyRecommendationModel.clear()
                val dailyBudgetModel = InsightDailyBudgetModel().apply {
                    id = groupId
                    name = groupName
                    dailySuggestedPrice = suggestedPriceDaily
                    potentialClick = calculatePotentialClick(holder).toLong()
                }
                dailyRecommendationModel.add(dailyBudgetModel)
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightSightDailyProductEcommerceViewEvent(
                    VIEW_DAILY_RECOMMENDATION_PRODUKS,
                    "",
                    dailyRecommendationModel,
                    holder.adapterPosition,
                    userSession.userId)
            }
            holder.editBudget?.textFieldInput?.addTextChangedListener(object :
                NumberTextWatcher(holder.editBudget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    items[holder.adapterPosition].setCurrentBid = number
                    holder.potentialClick.text =
                        String.format(holder.view.context.getString(R.string.topads_dash_potential_click_text),
                            calculatePotentialClick(holder).thousandFormatted())
                    setPotensiKlik = calculatePotentialClick(holder).toLong()
                    when {
                        number < items[holder.adapterPosition].suggestedPriceDaily && number > items[holder.adapterPosition].priceDaily -> {
                            holder.buttonSubmitEdit.isEnabled = true
                            holder.editBudget?.setError(false)
                            holder.editBudget?.setMessage(String.format(holder.view.context.getString(
                                R.string.topads_dash_budget_recom_error),
                                suggestedPriceDaily.toInt()))
                        }
                        number < items[holder.adapterPosition].priceDaily -> {
                            holder.editBudget?.setError(true)
                            holder.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_min_budget_error))
                            holder.buttonSubmitEdit.isEnabled = false
                        }
                        number > RECOMMENDATION_DAILY_MAX_BUDGET.toDouble() -> {
                            holder.editBudget?.setError(true)
                            holder.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_max_budget_error))
                            holder.buttonSubmitEdit.isEnabled = false
                        }
                        number.toInt() % BUDGET_MULTIPLE_FACTOR != 0 -> {
                            holder.editBudget?.setError(true)
                            holder.editBudget?.setMessage(String.format(holder.view.context.getString(
                                com.tokopedia.topads.common.R.string.topads_common_error_multiple_50),
                                BUDGET_MULTIPLE_FACTOR))
                            holder.buttonSubmitEdit.isEnabled = false
                        }
                        else -> {
                            holder.editBudget?.setError(false)
                            holder.editBudget?.setMessage("")
                            holder.buttonSubmitEdit.isEnabled = true
                        }
                    }
                }
            })
        }
    }
}
