package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.adapter.TopAdsHeadlineKeyAdapter
import com.tokopedia.top_ads_headline.view.adapter.TopAdsHeadlineKeySelectedAdapter
import com.tokopedia.top_ads_headline.view.viewmodel.TopAdsHeadlineKeyViewModel
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.topads_headline_keyword_list_fragment.*
import javax.inject.Inject

/**
 * Created by Pika on 5/11/20.
 */

private const val KEY_LIMIT = 50
const val SEARCH_NOT_AVAILABLE = "-1"

class TopAdsHeadlineKeyFragment : BaseHeadlineStepperFragment<HeadlineAdStepperModel>() {

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
    private var deletedKeywords: ArrayList<String> = arrayListOf()
    private var addedKeywords: ArrayList<KeywordDataItem> = arrayListOf()
    private var editedKeywords: ArrayList<KeywordDataItem> = arrayListOf()


    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TopAdsHeadlineKeyViewModel::class.java)
    }

    override fun gotoNextPage() {
        stepperModel?.minBid = minSuggestedBid
        stepperModel?.manualSelectedKeywords = getManualAddedKeywords()
        stepperModel?.selectedKeywords = getSelectedKeywords()
        stepperModel?.keywordOperations = getKeyWordOperations()
        stepperModel?.stateRestoreKeyword = true
        if (stepperListener != null) {
            stepperListener?.goToNextPage(stepperModel)
        } else {
            val intent = Intent()
            intent.putExtra(BaseStepperActivity.STEPPER_MODEL_EXTRA, stepperModel)
            intent.putParcelableArrayListExtra(ADDED_KEYWORDS, addedKeywords)
            intent.putParcelableArrayListExtra(EDITED_KEYWORDS, editedKeywords)
            intent.putStringArrayListExtra(DELETED_KEYWORDS, deletedKeywords)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun getManualAddedKeywords(): MutableList<KeywordDataItem> {
        return keywordSelectedAdapter.items.map {
            it
        }.toMutableList()
    }

    private fun getKeyWordOperations(): List<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation> {
        return ArrayList<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation>().apply {
            stepperModel?.selectedKeywords?.forEach {
                add(TopAdsManageHeadlineInput.Operation.Group.KeywordOperation(
                        action = ParamObject.ACTION_CREATE,
                        keyword = TopAdsManageHeadlineInput.Operation.Group.KeywordOperation.Keyword(
                                type = ParamObject.HEADLINE_KEYWORD_TYPE,
                                status = ParamObject.ACTIVE_STATUS,
                                priceBid = it.bidSuggest,
                                tag = it.keyword)
                ))
            }
        }
    }

    private fun getSelectedKeywords(): MutableList<KeywordDataItem> {
        val list: MutableList<KeywordDataItem> = keywordSelectedAdapter.items.map {
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
        setInitialSetup()
        addBtn?.setOnClickListener {
            addManualKeywords()
        }
        btnNext?.setOnClickListener {
            gotoNextPage()
        }
        editText?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editText.textFiedlLabelText.text = getString(R.string.topads_headline_enter_keyword_hint)
                if (s?.trim()?.isNotEmpty() == true) {
                    val errMax = checkMaxSelectedValue()
                    val error = Utils.validateKeyword(context, s)
                    if (error == null && errMax == null) {
                        addBtn?.isEnabled = true
                        editText?.setError(false)
                        editText?.setMessage("")
                    } else {
                        addBtn.isEnabled = false
                        editText?.setError(true)
                        if (error != null)
                            editText?.setMessage(error.toString())
                        else
                            editText?.setMessage(errMax.toString())
                    }
                } else {
                    addBtn?.isEnabled = false
                }
            }
        })
        btnNext.setOnClickListener {
            gotoNextPage()
        }
    }

    private fun setInitialSetup() {
        stepperModel?.manualSelectedKeywords?.let {
            keywordSelectedAdapter.items.addAll(it)
            keywordSelectedAdapter.notifyDataSetChanged()
        }
    }

    private fun checkMaxSelectedValue(): String? {
        val count = keywordSelectedAdapter.itemCount
        return if (count >= KEY_LIMIT) {
            getString(R.string.error_max_selected_keyword)
        } else
            null
    }

    private fun setToolTip() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.topads_headline_keyword_bottomsheet_title1)
            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(this.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }
        tipBtn?.addItem(tooltipView)
        tipBtn.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiHeaderModel(R.string.topads_headline_keyword_bottomsheet_title1))
                add(TipsUiRowModel(R.string.topads_headline_keyword_bottomsheet_desc1, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_keyword_bottomsheet_desc2, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_keyword_bottomsheet_desc3, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_keyword_bottomsheet_desc4, R.drawable.topads_create_ic_checklist))
                add(TipsUiHeaderModel(R.string.topads_headline_keyword_bottomsheet_title2))
                add(TipsUiRowModel(R.string.topads_headline_keyword_bottomsheet_desc5, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_keyword_bottomsheet_desc6, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_keyword_bottomsheet_desc7, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.show(childFragmentManager, "")
        }
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_keywrod_title))
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
        keywordSelectedAdapter = TopAdsHeadlineKeySelectedAdapter(::onItemUnselect, ::onKeywordBidChange)
        keywordListAdapter = TopAdsHeadlineKeyAdapter(::onItemChecked, ::onKeywordBidChange, stepperModel?.selectedKeywords)
        getLatestBid()
        viewModel.getSuggestionKeyword(stepperModel?.selectedProductIds?.joinToString(","), 0, ::onSuccessSuggestionKeywords, ::onEmptySuggestion)
    }

    private fun onKeywordBidChange(enable: Boolean, keywordData: KeywordDataItem) {
        btnNext?.isEnabled = enable
        if (enable) {
            if (!editedKeywords.contains(keywordData)) {
                editedKeywords.add(keywordData)
            }
        }
    }

    private fun getLatestBid() {
        val selectedProductIds: MutableList<Int>? = stepperModel?.selectedProductIds
                ?: mutableListOf()
        val suggestions = DataSuggestions(ParamObject.TYPE_HEADLINE_KEYWORD, ids = selectedProductIds)
        viewModel.getBidInfo(listOf(suggestions), this::onSuccessSuggestion, this::onEmptySuggestion)
    }

    private fun onSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        keywordSelectedAdapter.setDefaultValues(data.firstOrNull()?.maxBid,
                data.firstOrNull()?.minBid, data.firstOrNull()?.suggestionBid)
        keywordListAdapter.setMax(data.firstOrNull()?.maxBid ?: 0)
        stepperModel?.maxBid = data.firstOrNull()?.maxBid ?: 0
        minSuggestedBid = data.firstOrNull()?.minBid ?: 0
    }

    private fun setAdapter() {
        rvKeywordList.adapter = keywordListAdapter
        rvKeywordList.layoutManager = LinearLayoutManager(context)
        rvSelectedKeywordList.adapter = keywordSelectedAdapter
        rvSelectedKeywordList.layoutManager = LinearLayoutManager(context)
    }

    private fun addManualKeywords() {
        if (keywordSelectedAdapter.items.find { it.keyword.trim() == editText.textFieldInput.text.toString().trim() } == null) {
            if (keywordListAdapter.items.find { it.keyword.trim() == editText.textFieldInput.text.toString().trim() } == null) {
                selectedTitle.visibility = View.VISIBLE
                val item = KeywordDataItem()
                item.keyword = editText.textFieldInput.text.toString()
                item.totalSearch = SEARCH_NOT_AVAILABLE
                item.fromSearch = true
                item.bidSuggest = minSuggestedBid
                keywordSelectedAdapter.items.add(item)
                keywordSelectedAdapter.notifyItemInserted(keywordSelectedAdapter.itemCount - 1)
                if(!addedKeywords.contains(item)){
                    addedKeywords.add(item)
                }
                setCount()
            }
        }
    }

    private fun onEmptySuggestion() {
        recomTitle.visibility = View.GONE
        rvKeywordList.visibility = View.GONE
    }

    private fun onSuccessSuggestionKeywords(list: List<KeywordData>) {
        keywordListAdapter.setList(list, list.firstOrNull()?.minBid
                ?: 0, stepperModel?.selectedKeywords, stepperModel?.stateRestoreKeyword)
        setCount()
    }

    private fun onItemChecked(keywordData: KeywordDataItem) {
        if(keywordData.onChecked){
            if(!addedKeywords.contains(keywordData)){
                addedKeywords.add(keywordData)
            }
            deletedKeywords.remove(keywordData.keyword)
        }else{
            if(!deletedKeywords.contains(keywordData.keyword)){
                deletedKeywords.add(keywordData.keyword)
            }
            addedKeywords.remove(keywordData)
        }
        setCount()
    }

    private fun onItemUnselect(pos: Int, keywordData: KeywordDataItem) {
        if(!deletedKeywords.contains(keywordData.keyword)){
            deletedKeywords.add(keywordData.keyword)
        }
        removeFromList(pos)
        setCount()
    }

    private fun removeFromList(pos: Int) {
        keywordSelectedAdapter.items.removeAt(pos)
        keywordSelectedAdapter.notifyItemRemoved(pos)
    }

    private fun setCount() {
        selectedTitle.text = String.format(getString(R.string.topads_common_selected_list_count), keywordSelectedAdapter.itemCount)
        selectedTitle.visibility = if (keywordSelectedAdapter.itemCount > 0) View.VISIBLE else View.GONE
        selectKeyInfo.text = String.format(getString(R.string.format_selected_keyword), getSelectedKeywords().size)
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }
}