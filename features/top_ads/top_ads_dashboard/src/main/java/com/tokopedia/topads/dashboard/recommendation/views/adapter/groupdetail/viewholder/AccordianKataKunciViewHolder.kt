package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.topads.common.data.response.KeywordEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.CONST_2
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_CREATE_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_MULTIPLIER
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_STATUS_ACTIVE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_STATUS_DELETED
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_STATUS_INACTIVE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_NEGATIVE_BROAD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_NEGATIVE_EXACT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_BROAD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_EXACT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_PHRASE
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData.NewPositiveKeywordsRecom
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel
import com.tokopedia.unifycomponents.TextFieldUnify2

class AccordianKataKunciViewHolder(
    private val itemView: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianKataKunciUiModel>(itemView) {
    private var kataKunciItemList: List<NewPositiveKeywordsRecom> = mutableListOf()

    inner class KataKunciItemsAdapter :
        RecyclerView.Adapter<KataKunciItemsAdapter.KataKunciItemsViewHolder>() {

        inner class KataKunciItemsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val checkbox : com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.checkbox)
            private val title : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.title)
            private val searchesCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.searches_count_value)
            private val potentialCount : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.show_potential_value)
            private val keywordStateType : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.keyword_state_type)
            private val keywordCost : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.keyword_cost)
            fun bind(element: NewPositiveKeywordsRecom) {
                setTextChangeListener(element)
                bindValues(element)
                setSelected(element)
                setClickListener(element)
            }

            private fun setTextChangeListener(element: NewPositiveKeywordsRecom) {
                keywordCost.editText.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(text: Editable?) {
                        val bid = text.toString().toIntOrZero()
                        element.currentBid = bid
                        val errorMsg = validateKataKunciBid(bid)
                        if(errorMsg.isEmpty()){
                            keywordCost.isInputError = false
                            if(bid == element.suggestionBid) {
                                keywordCost.setMessage(getString(R.string.biaya_optimal))
                            } else {
                                keywordCost.setMessage(getClickableString(element,keywordCost))
                            }

                            if(checkbox.isChecked){
                                topadsManagePromoGroupProductInput?.keywordOperation?.firstOrNull { it?.keyword?.tag == element.keywordTag }?.keyword?.price_bid = bid.toDouble()
                            }
                        } else {
                            keywordCost.isInputError = true
                            keywordCost.setMessage(errorMsg)
                        }
                        onInsightAction.invoke(validateAllSelectedItems())
                    }
                })
            }

            private fun bindValues(element: NewPositiveKeywordsRecom) {
                title.text = element.keywordTag
                searchesCount.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_times_per_month_template),
                        element.totalSearch
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                potentialCount.text = HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.topads_dashboard_times_per_day_value),
                        element.predictedImpression
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                if(keywordCost.editText.text.isNullOrEmpty())
                    keywordCost.editText.setText(element.suggestionBid.toString())
                if (element.keywordType == KEYWORD_TYPE_POSITIVE_PHRASE) {
                    keywordStateType.text = getString(R.string.wide)
                    keywordStateType.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN100
                        )
                    )
                } else {
                    keywordStateType.text = getString(R.string.specific)
                    keywordStateType.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_BN100
                        )
                    )
                }
            }

            private fun setClickListener(element: NewPositiveKeywordsRecom){
                checkbox.setOnClickListener {
                    if (checkbox.isChecked)
                        addItemIntoInput(element, keywordCost.editText.text.toString().toIntOrZero())
                    else
                        removeItemFromInput(element)
                    element.isSelected = checkbox.isChecked
                    onInsightAction.invoke(validateAllSelectedItems())
                    setSelectAllButtonIndeterminateState()
                }
            }

            private fun setSelected(element: NewPositiveKeywordsRecom) {
                checkbox.isChecked = element.isSelected
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
    }

    private val adapter by lazy { KataKunciItemsAdapter() }
    private val kataKunciRv: RecyclerView = itemView.findViewById(R.id.kataKunciRv)
    private val selectAllCheckbox: com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.selectAllCheckbox)
    private var topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput? = null
    private var maxBid: Int? = Int.ZERO
    private var minBid: Int? = Int.ZERO

    override fun bind(element: AccordianKataKunciUiModel?) {
        updateKeys(element)
        setDefaultInputModel(element)
        setViews(element)
        setClickListener(element)
    }

    private fun updateKeys(element: AccordianKataKunciUiModel?) {
        topadsManagePromoGroupProductInput = element?.input
        maxBid = element?.maxBid
        minBid = element?.minBid
    }

    private fun setDefaultInputModel(element: AccordianKataKunciUiModel?) {
        topadsManagePromoGroupProductInput?.groupInput = null
        addAllItemsIntoInput(element)
        element?.newPositiveKeywordsRecom?.forEach {
            it.isSelected = true
        }
        onInsightAction.invoke(validateAllSelectedItems())
    }

    private fun setViews(element: AccordianKataKunciUiModel?) {
        kataKunciRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        kataKunciRv.adapter = adapter
        element?.newPositiveKeywordsRecom?.let {
            kataKunciItemList = it
            adapter.notifyDataSetChanged()
        }
        kataKunciRv.addItemDecoration(
            RecommendationInsightItemDecoration(
                itemView.context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setClickListener(element: AccordianKataKunciUiModel?) {
        selectAllCheckbox.setOnClickListener {
            selectAllCheckbox.setIndeterminate(false)
            if (selectAllCheckbox.isChecked)
                addAllItemsIntoInput(element)
            else
                topadsManagePromoGroupProductInput?.keywordOperation = listOf()
            element?.newPositiveKeywordsRecom?.forEach {
                it.isSelected = selectAllCheckbox.isChecked
            }
            element?.newPositiveKeywordsRecom?.let {
                kataKunciItemList = it
                adapter.notifyDataSetChanged()
            }
            onInsightAction.invoke(validateAllSelectedItems())
        }
    }

    private fun addItemIntoInput(element: NewPositiveKeywordsRecom, priceBid: Int){
        val list = topadsManagePromoGroupProductInput?.keywordOperation?.toMutableList()
        list?.add(KeywordEditInput(
            ACTION_CREATE_PARAM,
            keyword = KeywordEditInput.Keyword(
                type = getKeywordType(element.keywordType),
                status = getKeywordStatus(element.keywordStatus),
                tag = element.keywordTag,
                price_bid = priceBid.toDouble(),
                suggestionPriceBid = element.suggestionBid.toDouble(),
                source = element.keywordSource
            )
        ))
        topadsManagePromoGroupProductInput?.keywordOperation = list
    }

    private fun removeItemFromInput(element: NewPositiveKeywordsRecom) {
        topadsManagePromoGroupProductInput?.keywordOperation = topadsManagePromoGroupProductInput?.keywordOperation?.filter { it?.keyword?.tag != element.keywordTag }
    }

    private fun addAllItemsIntoInput(element: AccordianKataKunciUiModel?) {
        val list = mutableListOf<KeywordEditInput>()
        element?.newPositiveKeywordsRecom?.forEach {
            list.add(
                KeywordEditInput(
                    ACTION_CREATE_PARAM,
                    keyword = KeywordEditInput.Keyword(
                        type = getKeywordType(it.keywordType),
                        status = getKeywordStatus(it.keywordStatus),
                        tag = it.keywordTag,
                        suggestionPriceBid = it.suggestionBid.toDouble(),
                        price_bid = it.suggestionBid.toDouble(),
                        source = it.keywordSource
                    )
                )
            )
        }
        topadsManagePromoGroupProductInput?.keywordOperation = list
    }

    private fun setSelectAllButtonIndeterminateState(){
        if(topadsManagePromoGroupProductInput?.keywordOperation?.count() == kataKunciItemList.count())
            selectAllCheckbox.setIndeterminate(false)
        else
            selectAllCheckbox.setIndeterminate(true)

        selectAllCheckbox.isChecked = topadsManagePromoGroupProductInput?.keywordOperation?.count().toZeroIfNull() > Int.ZERO
    }

    private fun getKeywordType(type: String): String {
        return when(type){
            KEYWORD_TYPE_POSITIVE_EXACT,
            KEYWORD_TYPE_POSITIVE_PHRASE,
            KEYWORD_TYPE_POSITIVE_BROAD,
            KEYWORD_TYPE_NEGATIVE_PHRASE,
            KEYWORD_TYPE_NEGATIVE_EXACT,
            KEYWORD_TYPE_NEGATIVE_BROAD -> type
            else -> {
                if (type.split(' ').firstOrNull()?.length.toZeroIfNull() >= CONST_2)
                    KEYWORD_TYPE_POSITIVE_EXACT
                else
                    KEYWORD_TYPE_POSITIVE_PHRASE
            }
        }
    }

    private fun getKeywordStatus(status : String): String{
        return if(status == KEYWORD_STATUS_ACTIVE || status == KEYWORD_STATUS_INACTIVE || status == KEYWORD_STATUS_DELETED)
                status
            else
                KEYWORD_STATUS_ACTIVE
    }

    private fun validateKataKunciBid(bid: Int): String{
        return if(bid < minBid.toZeroIfNull())
            String.format(getString(R.string.topads_insight_min_bid_error_msg_format), minBid.toZeroIfNull())
        else if(bid > maxBid.toZeroIfNull())
            String.format(getString(R.string.topads_insight_max_bid_error_msg_format), maxBid.toZeroIfNull())
        else if(bid % INSIGHT_MULTIPLIER != Int.ZERO)
            getString(R.string.error_bid_not_multiple_50)
        else String.EMPTY
    }

    private fun validateAllSelectedItems(): Boolean{
        var hasErrors = false
        kataKunciItemList.forEach {
            if(it.isSelected && !validateKataKunciBid(it.currentBid).isEmpty()){
                hasErrors = true
            }
        }
        return hasErrors
    }

    private fun getClickableString(
        element: NewPositiveKeywordsRecom,
        keywordCost: TextFieldUnify2
    ): SpannableString {
        val msg = String.format(
            getString(R.string.topads_insight_recommended_bid_apply),
            element.suggestionBid
        )
        val ss = SpannableString(msg)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                keywordCost.editText.setText(element.suggestionBid.toString())
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(
                    keywordCost.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                ds.isFakeBoldText = true
            }
        }
        ss.setSpan(cs, msg.length - 8, msg.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_kata_kunci_layout
    }
}
