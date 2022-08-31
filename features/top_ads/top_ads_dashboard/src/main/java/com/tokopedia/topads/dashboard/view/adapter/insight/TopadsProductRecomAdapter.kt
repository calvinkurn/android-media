package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.model.InsightProductRecommendationModel
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BUDGET_MULTIPLE_FACTOR
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.NumberTextWatcher

const val VIEW_RECOMMENDED_PRODUK = "view - rekomendasi produk"
const val VIEW_DAILY_RECOMMENDATION_PRODUKS = "view - rekomendasi anggaran - grup iklan"

class TopadsProductRecomAdapter(
    private val userSession: UserSessionInterface,
    private val itemSelected: () -> Unit, private val enableButton: (enable: Boolean) -> Unit,
) : RecyclerView.Adapter<TopadsProductRecomAdapter.ViewHolder>() {

    var items: MutableList<ProductRecommendation> = mutableListOf()
    private var maxBid = "0"
    private val insightRecommendationModel = mutableListOf<InsightProductRecommendationModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_dash_recon_product_item, parent, false)
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
            holder.productName?.text = productName
            holder.imageFolder?.loadImage(imgUrl)
            val totalSearch = String.format(
                holder.view.resources.getString(com.tokopedia.topads.dashboard.R.string.topads_dash_max_times_month),
                searchCount.toIntOrZero().thousandFormatted()
            )
            holder.totalSearch?.text = totalSearch
            val recommendationBid =
                "Rp" + convertToCurrency(recomBid.toLong()) + holder.view.context.getString(com.tokopedia.topads.common.R.string.topads_common_klik_)
            holder.recommendedBid?.text = recommendationBid
            holder.editBudget?.textFieldInput?.setText(convertToCurrency(recomBid.toLong()))
            holder.cbProductRecom?.isChecked = isChecked
            setCurrentBid = recomBid
            holder.view.setOnClickListener {
                holder.cbProductRecom?.isChecked = holder.cbProductRecom?.isChecked == false
                isChecked = holder.cbProductRecom?.isChecked == true
                itemSelected.invoke()
            }
            holder.view.addOnImpressionListener(impressHolder) {
                insightRecommendationModel.clear()
                val insightProductRecommendationModel = InsightProductRecommendationModel().apply {
                    id = productId
                    name = productName
                    searchNumber = searchCount.toIntOrZero()
                    searchPercent = searchPercentage
                    recommendedBid = recomBid
                }
                insightRecommendationModel.add(insightProductRecommendationModel)
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightSightProductEcommerceViewEvent(
                    VIEW_RECOMMENDED_PRODUK, "",
                    insightRecommendationModel, holder.adapterPosition, userSession.userId
                )
            }
            holder.editBudget?.textFieldInput?.addTextChangedListener(object :
                NumberTextWatcher(holder.editBudget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    items[holder.adapterPosition].setCurrentBid = number.toInt().toString()
                    when {
                        number < items[holder.adapterPosition].recomBid.toDouble() && number > minBid.toFloat() -> {
                            enableButton.invoke(true)
                            holder.editBudget?.setError(false)
                            holder.editBudget?.setMessage(
                                String.format(
                                    holder.view.context.getString(
                                        R.string.topads_dash_budget_recom_error
                                    ), recomBid
                                )
                            )
                        }
                        number < items[holder.adapterPosition].minBid.toFloat() -> {
                            enableButton.invoke(false)
                            holder.editBudget?.setError(true)
                            holder.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_min_budget_error))
                        }
                        number >= maxBid.toFloat() -> {
                            enableButton.invoke(false)
                            holder.editBudget?.setError(true)
                            holder.editBudget?.setMessage(holder.view.context.getString(R.string.topads_dash_product_recomm_max_budget_error))
                        }
                        number.toInt() % BUDGET_MULTIPLE_FACTOR != 0 -> {
                            enableButton.invoke(false)
                            holder.editBudget?.setError(true)
                            holder.editBudget?.setMessage(
                                String.format(
                                    holder.view.context.getString(
                                        com.tokopedia.topads.common.R.string.topads_common_error_multiple_50
                                    ), BUDGET_MULTIPLE_FACTOR
                                )
                            )
                        }
                        else -> {
                            enableButton.invoke(true)
                            holder.editBudget?.setError(false)
                            holder.editBudget?.setMessage("")
                        }
                    }
                }
            })
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cbProductRecom: CheckboxUnify? = view.findViewById(R.id.cb_product_recom)
        val imageFolder: ImageUnify? = view.findViewById(R.id.img_folder)
        val productName: Typography? = view.findViewById(R.id.productName)
        val totalSearch: Typography? = view.findViewById(R.id.totalSearch)
        val divider: DividerUnify? = view.findViewById(R.id.divider)
        val recommendedBid: Typography? = view.findViewById(R.id.recommendedBid)
        val editBudget: TextFieldUnify? = view.findViewById(R.id.editBudget)
    }
}
