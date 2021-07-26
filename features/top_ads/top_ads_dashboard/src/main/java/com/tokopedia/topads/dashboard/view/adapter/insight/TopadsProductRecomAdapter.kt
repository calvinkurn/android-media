package com.tokopedia.topads.dashboard.view.adapter.insight

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.model.InsightProductRecommendationModel
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BUDGET_MULTIPLE_FACTOR
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_dash_recon_product_item.view.*

const val VIEW_RECOMMENDED_PRODUK = "view - rekomendasi produk"
const val VIEW_DAILY_RECOMMENDATION_PRODUKS = "view - rekomendasi anggaran - grup iklan"
class TopadsProductRecomAdapter(private val userSession: UserSessionInterface, var itemSelected: () -> Unit, var enableButton: (enable: Boolean) -> Unit) : RecyclerView.Adapter<TopadsProductRecomAdapter.ViewHolder>() {
    var items: MutableList<ProductRecommendation> = mutableListOf()
    private var maxBid = "0"
    private var insightRecommendationModel = mutableListOf<InsightProductRecommendationModel>()

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

    fun setMaxValue(bid: String) {
        maxBid = bid
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
            setCurrentBid = recomBid.toInt()
            holder.view.setOnClickListener {
                holder.view.cb_product_recom.isChecked = !holder.view.cb_product_recom.isChecked
                isChecked = holder.view.cb_product_recom.isChecked
                itemSelected.invoke()
            }
            holder.view.addOnImpressionListener(impressHolder) {
                insightRecommendationModel.clear()
                    var insightProductRecommendationModel = InsightProductRecommendationModel().apply {
                        id = productId
                        name = productName
                        searchNumber = searchCount
                        searchPercent = searchPercentage
                        recommendedBid = recomBid
                    }
                    insightRecommendationModel.add(insightProductRecommendationModel)
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightSightProductEcommerceViewEvent(VIEW_RECOMMENDED_PRODUK, "", insightRecommendationModel, holder.adapterPosition, userSession.userId)
            }
            holder.view.editBudget?.textFieldInput?.addTextChangedListener(object : NumberTextWatcher(holder.view.editBudget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    items[holder.adapterPosition].setCurrentBid = number.toInt()
                    when {
                        number < items[holder.adapterPosition].recomBid.toDouble() && number > minBid.toInt() -> {
                            enableButton.invoke(true)
                            holder.view.editBudget?.setError(false)
                            holder.view.editBudget?.setMessage(String.format(holder.view.context.getString(R.string.topads_dash_budget_recom_error), recomBid))
                        }
                        number < items[holder.adapterPosition].minBid.toFloat() -> {
                            enableButton.invoke(false)
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_min_budget_error))
                        }
                        number >= maxBid.toFloat() -> {
                            enableButton.invoke(false)
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_max_budget_error))
                        }
                        number.toInt() % BUDGET_MULTIPLE_FACTOR != 0 -> {
                            enableButton.invoke(false)
                            holder.view.editBudget?.setError(true)
                            holder.view.editBudget?.setMessage(String.format(holder.view.context.getString(com.tokopedia.topads.common.R.string.topads_common_error_multiple_50), BUDGET_MULTIPLE_FACTOR))
                        }
                        else -> {
                            enableButton.invoke(true)
                            holder.view.editBudget?.setError(false)
                            holder.view.editBudget?.setMessage("")
                        }
                    }
                }
            })
        }
    }

}
