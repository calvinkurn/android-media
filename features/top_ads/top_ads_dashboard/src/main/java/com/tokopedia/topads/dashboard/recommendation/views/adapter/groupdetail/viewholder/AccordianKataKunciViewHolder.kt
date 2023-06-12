package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_CREATE_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewPositiveKeywordsRecom
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel

class AccordianKataKunciViewHolder(
    private val itemView: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianKataKunciUiModel>(itemView) {

    inner class KataKunciItemsAdapter :
        RecyclerView.Adapter<KataKunciItemsAdapter.KataKunciItemsViewHolder>() {
        var kataKunciItemList: List<NewPositiveKeywordsRecom> = mutableListOf()

        inner class KataKunciItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val checkbox : com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.checkbox)
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val searchesCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.searches_count_value)
            private val potentialCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)
            private val keywordStateType : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            private val keywordCost : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.keyword_cost)
            fun bind(element: NewPositiveKeywordsRecom) {
                checkbox.setOnCheckedChangeListener(null)
                checkbox.isChecked = element.isSelected
                title.text = element.keywordTag
                searchesCount.text = String.format("%s/bulan",element.totalSearch)
                potentialCount.text = String.format("+%s kali/hari",element.predictedImpression)
                keywordCost.editText.setText(element.suggestionBid.toString())
                if(element.keywordType == KEYWORD_TYPE_POSITIVE_PHRASE){
                    keywordStateType.text = getString(R.string.wide)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN100))
                } else {
                    keywordStateType.text = getString(R.string.specific)
                    keywordStateType.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_BN100))
                }
                checkbox.setOnCheckedChangeListener { btn, isChecked ->
                    element.isSelected = isChecked
                    if(isChecked){
                        addTopadsManagePromoGroupProductInput(element)
                    } else {
                        topadsManagePromoGroupProductInput?.keywordOperation = topadsManagePromoGroupProductInput?.keywordOperation?.filter { it?.keyword?.tag != element.keywordTag }
                    }
                    onInsightAction.invoke(hasErrors)
                }

                keywordCost.editText.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(text: Editable?) {
                        val bid = text.toString().toIntOrZero()
                        if(bid < 400){
                            keywordCost.isInputError = true
                            keywordCost.editText.error = "Min. biaya Rp400."
                        } else if(bid > 10000){
                            keywordCost.isInputError = true
                            keywordCost.editText.error = "Maks. biaya Rp10.000."
                        } else if(bid % 50 != 0){
                            keywordCost.isInputError = true
                            keywordCost.editText.error = "Harus kelipatan Rp50."
                        } else {
                            element.priceBid = text.toString().toIntOrZero()
                        }
                    }

                })
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KataKunciItemsViewHolder {
            return KataKunciItemsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.topads_insights_accordian_kata_kunci_item_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: KataKunciItemsViewHolder, position: Int) {
            holder.bind(kataKunciItemList[position])
        }

        override fun getItemCount(): Int {
            return kataKunciItemList.count()
        }

        fun updateList(list : List<NewPositiveKeywordsRecom>){
            kataKunciItemList = list
            notifyDataSetChanged()
        }
    }

    private val adapter by lazy { KataKunciItemsAdapter() }
    private val kataKunciRv: RecyclerView = itemView.findViewById(R.id.kataKunciRv)
    private val selectAllCheckbox: com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.selectAllCheckbox)
    private var topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput? = null
    private var hasErrors: Boolean = false

    override fun bind(element: AccordianKataKunciUiModel?) {
        topadsManagePromoGroupProductInput = element?.input
        kataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        kataKunciRv.adapter = adapter
        element?.newPositiveKeywordsRecom?.let {
            adapter.updateList(it)
        }
        kataKunciRv.addItemDecoration(
            DividerItemDecoration(
                itemView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        selectAllCheckbox.setOnCheckedChangeListener { btn, isChecked ->
            if(isChecked) {
                val list = mutableListOf<KeywordEditInput>()
                element?.newPositiveKeywordsRecom?.forEach {
                    list.add(
                        KeywordEditInput(
                            ACTION_CREATE_PARAM,
                            keyword = KeywordEditInput.Keyword(
                                type = it.keywordType,
                                status = it.keywordStatus,
                                tag = it.keywordTag,
                                suggestionPriceBid = it.suggestionBid.toDouble(),
                                price_bid = if(it.priceBid.isZero()) it.suggestionBid.toDouble() else it.priceBid.toDouble(),
                                source = it.keywordSource
                            )
                        )
                    )
                }
                topadsManagePromoGroupProductInput?.keywordOperation = list
            } else {
                topadsManagePromoGroupProductInput?.keywordOperation = listOf()
            }
            element?.newPositiveKeywordsRecom?.forEach { it.isSelected = isChecked }
            element?.newPositiveKeywordsRecom?.let {
                adapter.updateList(it)
            }
            onInsightAction.invoke(hasErrors)
        }
    }

    fun addTopadsManagePromoGroupProductInput(element: NewPositiveKeywordsRecom){
        val list = topadsManagePromoGroupProductInput?.keywordOperation?.toMutableList()
        list?.add(KeywordEditInput(
            ACTION_CREATE_PARAM,
            keyword = KeywordEditInput.Keyword(
                type = element.keywordType,
                status = element.keywordStatus,
                tag = element.keywordTag,
                price_bid = if(element.priceBid.isZero()) element.suggestionBid.toDouble() else element.priceBid.toDouble(),
                suggestionPriceBid = element.suggestionBid.toDouble(),
                source = element.keywordSource
            )
        ))
        topadsManagePromoGroupProductInput?.keywordOperation = list
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_kata_kunci_layout
    }
}
