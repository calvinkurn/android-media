package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.CreateHeadlineAdsStepperModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.adapter.TopAdsHeadlineKeyAdapter
import com.tokopedia.top_ads_headline.view.adapter.TopAdsHeadlineKeySelectedAdapter
import com.tokopedia.top_ads_headline.view.viewmodel.TopAdsHeadlineKeyViewModel
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.topads_headline_keyword_list_fragment.*
import javax.inject.Inject

/**
 * Created by Pika on 5/11/20.
 */

class TopAdsHeadlineKeyFragment : BaseHeadlineStepperFragment<CreateHeadlineAdsStepperModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var minSuggestedBid = 0

    companion object {
        fun createInstance() = TopAdsHeadlineKeyFragment()
    }

    private lateinit var keywordListAdapter: TopAdsHeadlineKeyAdapter
    private lateinit var keywordSelectedAdapter: TopAdsHeadlineKeySelectedAdapter
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TopAdsHeadlineKeyViewModel::class.java)
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateHeadlineAdsStepperModel()
    }

    override fun gotoNextPage() {
        stepperModel?.minBid = minSuggestedBid
        stepperModel?.selectedKeywords = getSelectedKeywords()
        stepperListener?.goToNextPage(stepperModel)
    }

    private fun getSelectedKeywords(): MutableList<KeywordDataItem> {
        var list: MutableList<KeywordDataItem> = mutableListOf()
        list = keywordSelectedAdapter.items.map {
            it
        }.toMutableList()
        list.addAll(keywordListAdapter.getSelectItems().map {
            it
        }.toMutableList())
        return list
    }

    override fun populateView() {
        setAdapter()
        setToolTip()
        addBtn?.setOnClickListener {
            addManualKeywords()
        }
        editText?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val error = Utils.validateKeyword(context, s)
                if (error == null && s?.trim()?.isNotEmpty() == true) {
                    addBtn?.isEnabled = true
                    editText?.setError(true)

                } else {
                    addBtn.isEnabled = false
                    editText?.setError(true)
                    editText?.setMessage(error.toString())
                }
            }
        })
    }

    private fun setToolTip() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.topads_headline_keyword_bottomsheet_title1)
            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(this.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }
        tipBtn?.addItem(tooltipView)
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_ad_detail_fragment_label))
        }
    }

    override fun getScreenName(): String {
        return TopAdsHeadlineKeyFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_headline_keyword_list_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keywordSelectedAdapter = TopAdsHeadlineKeySelectedAdapter(::onItemUnselect)
        keywordListAdapter = TopAdsHeadlineKeyAdapter(::onItemChecked)
        viewModel.getSuggestionKeyword(stepperModel?.selectedProductIds?.joinToString(""), 0, ::onSuccessSuggestionKeywords, ::onEmptySuggestion)
    }

    private fun setAdapter() {
        rvKeywordList.adapter = keywordListAdapter
        rvKeywordList.layoutManager = LinearLayoutManager(context)
        rvSelectedKeywordList.adapter = keywordSelectedAdapter
        rvSelectedKeywordList.layoutManager = LinearLayoutManager(context)
    }

    private fun addManualKeywords() {
        if (keywordSelectedAdapter.items.find { it.keyword == editText.textFieldInput.text.toString() } == null) {
            if (keywordListAdapter.items.find { it.keyword == editText.textFieldInput.text.toString() } == null) {
                selectedTitle.visibility = View.VISIBLE
                val item = KeywordDataItem()
                item.keyword = editText.textFieldInput.text.toString()
                item.totalSearch = "-1"
                item.fromSearch = true
                //todo
                item.bidSuggest = 500
                keywordSelectedAdapter.items.add(item)
                keywordSelectedAdapter.notifyItemInserted(keywordListAdapter.itemCount - 1)
                setCount()
            }
        }
    }

    private fun onEmptySuggestion() {
        recomTitle.visibility = View.GONE
        rvKeywordList.visibility = View.GONE
    }

    private fun onSuccessSuggestionKeywords(list: List<KeywordData>) {
        minSuggestedBid = list.firstOrNull()?.minBid ?: 0
        keywordListAdapter.setList(list)
    }

    private fun onItemChecked(pos: Int) {
        setCount()
    }

    private fun onItemUnselect(pos: Int) {
        removeFromList(pos)
    }

    private fun removeFromList(pos: Int) {
        keywordSelectedAdapter.items.removeAt(pos)
        keywordSelectedAdapter.notifyItemRemoved(pos)
    }

    private fun setCount() {
        selectedTitle.visibility = if (keywordSelectedAdapter.itemCount > 0) View.VISIBLE else View.GONE
        selectKeyInfo.text = String.format(getString(R.string.format_selected_keyword), keywordSelectedAdapter.itemCount)
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }
}