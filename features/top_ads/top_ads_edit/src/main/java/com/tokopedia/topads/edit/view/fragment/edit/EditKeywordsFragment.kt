package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.KeySharedModel
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.CURRENT_KEY_TYPE
import com.tokopedia.topads.edit.utils.Constants.FROM_EDIT
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.ITEM_POSITION
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_NAME
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_EXACT
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.MAX_BID
import com.tokopedia.topads.edit.utils.Constants.MIN_BID
import com.tokopedia.topads.edit.utils.Constants.MIN_SUGGESTION
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_EDIT
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_KEYWORD_ALL
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID
import com.tokopedia.topads.edit.utils.Constants.REQUEST_OK
import com.tokopedia.topads.edit.utils.Constants.SELECTED_DATA
import com.tokopedia.topads.edit.utils.Constants.SUGGESTION_BID
import com.tokopedia.topads.edit.view.activity.SelectKeywordActivity
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject

/**
 * Created by Pika on 12/4/20.
 */


private const val CLICK_SETUP_KEY = "click - setup keyword"
private const val CLICK_TAMBAH_KATA_KUNCI = "click - tambah kata kunci"

class EditKeywordsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: EditKeywordListAdapter
    private lateinit var callBack: ButtonAction
    private var minSuggestKeyword = "0"
    private var maxSuggestKeyword = "0"
    private var deletedKeywords: ArrayList<KeySharedModel>? = arrayListOf()
    private var addedKeywords: ArrayList<KeySharedModel>? = arrayListOf()
    private var editedKeywords: ArrayList<KeySharedModel>? = arrayListOf()
    private var existingKeyword: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var recommendedKeywords: ArrayList<KeywordDataItem>? = arrayListOf()
    private var initialBudget: MutableList<String> = mutableListOf()
    private var isnewlyAddded: MutableList<Boolean> = mutableListOf()
    private var originalKeyList: MutableList<String> = arrayListOf()
    private var selectedData: ArrayList<KeywordDataItem>? = arrayListOf()
    private var cursor = ""
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var keywordTitle:LinearLayout
    private lateinit var budgetInput:TextFieldUnify
    private var productId: MutableList<String> = mutableListOf()

    private var userID: String = ""
    private lateinit var addKeyword: TextView
    private lateinit var ticker: Ticker
    private var minBid = "0"
    private var maxBid = "0"
    private var suggestBidPerClick = "0"
    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(EditFormDefaultViewModel::class.java)
    }

    private val viewModelKeyword by lazy {
        viewModelProvider.get(KeywordAdsViewModel::class.java)
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SharedViewModel::class.java)
    }
    private var groupId = 0
    private var productIds = ""

    override fun getScreenName(): String {
        return EditKeywordsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditKeywordListAdapter(EditKeywordListAdapterTypeFactoryImpl(this::onAddKeyword, ::onActionClicked, ::onActionClicked, ::onActionClicked))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_create_fragment_budget_list), container, false)
        recyclerView = view.findViewById(R.id.bid_list)
        addKeyword = view.findViewById(R.id.addKeyword)
        ticker = view.findViewById(R.id.ticker)
        keywordTitle = view.findViewById(R.id.keyword_title)
        budgetInput = view.findViewById(R.id.budget)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (cursor != "") {
                    fetchNextPage()
                }
            }
        }
    }

    private fun fetchNextPage() {
        viewModel.getAdKeyword(groupId, cursor, this::onSuccessKeyword)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel.getBudget().observe(viewLifecycleOwner,Observer {
            budgetInput.textFieldInput.setText(it.toString())
        })
        sharedViewModel.getProuductIds().observe(viewLifecycleOwner, Observer {
            productId = it
            getLatestBid()
        })
        val suggestions = java.util.ArrayList<DataSuggestions>()
        val dummyId: MutableList<Long> = mutableListOf()
        suggestions.add(DataSuggestions("group", dummyId))
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion)
        viewModelKeyword.getSuggestionKeyword(productIds, 0, ::onSuccessRecommended)
    }

    private fun getLatestBid() {
        val dummyId: MutableList<Long> = mutableListOf()
        productId.forEach {
            dummyId.add(it.toLong())
        }
        val suggestionsDefault = java.util.ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions(Constants.PRODUCT, dummyId))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onBidSuccessSuggestion)
    }

    private fun onBidSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            suggestBidPerClick = it.suggestionBid
            minBid = it.minBid
            maxBid = it.maxBid
        }
        checkForbidValidity(getCurrentBid())
    }

    private fun getCurrentBid(): Int {
        return budgetInput.textFieldInput.text.toString().removeCommaRawString().toInt()
    }

    private fun checkForbidValidity(result: Int) {
        when {
            minBid == "0" || maxBid == "0" -> {
                return
            }
            result < minBid.toDouble() -> {
                setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                actionEnable(false)
            }
            result > maxBid.toDouble() -> {
                actionEnable(false)
                setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
            }
            result % (Constants.MULTIPLY_CONST.toInt()) != 0 -> {
                actionEnable(false)
                setMessageErrorField(getString(R.string.topads_common_50_multiply_error), Constants.MULTIPLY_CONST, true)
            }
            else -> {
                actionEnable(true)
                setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
            }
        }
    }

    private fun actionEnable(isEnable:Boolean){
        callBack.buttonDisable(isEnable)
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        budgetInput.setError(bool)
        budgetInput.setMessage(String.format(error, bid))
    }

    private fun onSuccessRecommended(keywords: List<KeywordData>) {
        keywords.forEach {
            recommendedKeywords?.addAll(0, it.keywordData)
        }
        if (existingKeyword?.isEmpty() == false)
            checkForCommonData()
    }

    private fun checkForCommonData() {
        val listItem: MutableList<KeySharedModel> = mutableListOf()
        var commonToRecommendation: Boolean
        existingKeyword?.forEach { selected ->
            commonToRecommendation = false
            recommendedKeywords?.forEach { recommend ->
                if (selected.tag == recommend.keyword) {
                    commonToRecommendation = true
                    listItem.add(mapToModel(selected, recommend))
                }
            }
            if(!commonToRecommendation){
                listItem.add(mapToModelManual(selected))
            }
        }
        listItem.forEach {
            adapter.items.add(EditKeywordItemViewModel(it))
        }
        adapter.notifyDataSetChanged()
    }

    private fun mapToModelManual(selected: GetKeywordResponse.KeywordsItem): KeySharedModel {
        return KeySharedModel(
                selected.tag,
                "baru",
                "baru",
                selected.priceBid,
                "400",
                selected.source,
                "specific",
                selected.type,
                selected.status,
                selected.keywordId,
                selected.priceBid
        )

    }

    private fun mapToModel(selected: GetKeywordResponse.KeywordsItem, recommend: KeywordDataItem): KeySharedModel {
        return KeySharedModel(
                selected.tag,
                recommend.totalSearch,
                recommend.competition,
                selected.priceBid,
                "400",
                selected.source,
                recommend.keywordType,
                selected.type,
                selected.status,
                selected.keywordId,
                recommend.bidSuggest
        )
    }

    private fun onActionClicked(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_SETUP_KEY, groupId.toString(), userID)
//        val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(pos))
//        sheet.show(childFragmentManager, "")
//        sheet.onSaved = { bid, type, position ->
//            if (ifNewKeyword((adapter.items[position] as EditKeywordItemViewModel).data.tag)) {
//                addedKeywords?.forEach {
//                    if (it.tag == (adapter.items[position] as EditKeywordItemViewModel).data.tag) {
//                        it.priceBid = bid
//                        it.type = type
//                    }
//                }
//            }
//            if ((adapter.items[position] as EditKeywordItemViewModel).data.type != type) {
//                actionStatusChange(position)
//            }
//            (adapter.items[position] as EditKeywordItemViewModel).data.type = type
//            (adapter.items[position] as EditKeywordItemViewModel).data.priceBid = bid
//            adapter.notifyItemChanged(position)
//
//        }
//        sheet.onDelete = { position ->
//            showConfirmationDialog(position)
//        }
    }

    private fun prepareBundle(pos: Int): Bundle {
        val bundle = Bundle()
        if ((adapter.items[pos] as EditKeywordItemViewModel).data.priceBid == "0")
            (adapter.items[pos] as EditKeywordItemViewModel).data.priceBid = minSuggestKeyword
        bundle.putString(MAX_BID, maxSuggestKeyword)
        bundle.putString(MIN_BID, minSuggestKeyword)
        bundle.putString(SUGGESTION_BID, (adapter.items[pos] as EditKeywordItemViewModel).data.priceBid)
        bundle.putInt(ITEM_POSITION, pos)
        bundle.putInt(CURRENT_KEY_TYPE, (adapter.items[pos] as EditKeywordItemViewModel).data.typeInt)
        bundle.putString(KEYWORD_NAME, (adapter.items[pos] as EditKeywordItemViewModel).data.name)
        bundle.putInt(FROM_EDIT, 1)
        bundle.putString(GROUPID, groupId.toString())
        return bundle
    }

    private fun ifNewKeyword(tag: String): Boolean {
        return addedKeywords?.find { item -> item.name == tag } != null
    }

    private fun actionStatusChange(position: Int) {
//        if (isExistsOriginal(position)) {
//            deletedKeywords?.add((adapter.items[position] as EditKeywordItemViewModel).data)
//            addedKeywords?.add((adapter.items[position] as EditKeywordItemViewModel).data)
//        }
    }

    private fun showConfirmationDialog(position: Int) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.topads_edit_delete_keyword_conf_dialog_title))
            dialog.setDescription(MethodChecker.fromHtml(String.format(getString(R.string.topads_edit_delete_keyword_conf_dialog_desc),
                    (adapter.items[position] as EditKeywordItemViewModel).data.name)))
            dialog.setPrimaryCTAText(getString(R.string.topads_edit_batal))
            dialog.setSecondaryCTAText(getString(R.string.topads_edit_ya))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                deleteKeyword(position)
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    private fun onSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            minSuggestKeyword = it.minBid
            maxSuggestKeyword = it.maxBid
        }
        adapter.setBid(minSuggestKeyword)
    }

    private fun onAddKeyword() {
        val intent = Intent(context, SelectKeywordActivity::class.java)
        intent.putExtra(MIN_SUGGESTION, minSuggestKeyword)
        intent.putExtra(PRODUCT_ID, productIds)
        intent.putExtra(GROUP_ID, groupId)
        intent.putParcelableArrayListExtra(SELECTED_DATA, selectedData)
        startActivityForResult(intent, 1)
    }


    private fun deleteKeyword(position: Int) {
        var pos = 0
        if (selectedData?.isNotEmpty()!!) {
            selectedData?.forEachIndexed { index, select ->
                if (select.keyword == (adapter.items[position] as EditKeywordItemViewModel).data.name) {
                    pos = index
                }
            }
            selectedData?.removeAt(pos)
        }
        if (adapter.items[position] is EditKeywordItemViewModel) {
            if (isExistsOriginal(position)) {
                deletedKeywords?.add((adapter.items[position] as EditKeywordItemViewModel).data)
            } else {
                if (addedKeywords!!.isNotEmpty()) {
                    var index = 0
                    addedKeywords?.forEachIndexed { it, key ->
                        if (key.name == (adapter.items[position] as EditKeywordItemViewModel).data.name) {
                            index = it
                        }
                    }
                    addedKeywords?.removeAt(index)
                }
            }
        }
        adapter.items.removeAt(position)
        isnewlyAddded.removeAt(position)
        initialBudget.removeAt(position)
        if (adapter.items.isEmpty()) {
            setEmptyView()
        }
        adapter.notifyDataSetChanged()
    }

    private fun isExistsOriginal(position: Int): Boolean {
        return (originalKeyList.find { (adapter.items[position] as EditKeywordItemViewModel).data.name == it } != null)
    }

    private fun isExistsOriginal(name: String): Boolean {
        return (originalKeyList.find { name == it } != null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userID = UserSession(view.context).userId
        sharedViewModel.getProuductIds().observe(viewLifecycleOwner, Observer {
            productIds = it.joinToString(",")
        })
        sharedViewModel.getGroupId().observe(viewLifecycleOwner, Observer {
            groupId = it
            viewModel.getAdKeyword(groupId, cursor, this::onSuccessKeyword)
        })
        addKeyword.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_TAMBAH_KATA_KUNCI, "")
            onAddKeyword()
        }
        budgetInput.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budgetInput.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                sharedViewModel.setBudget(result)
                checkForbidValidity(result)
            }
        })
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>, cursor: String) {
        this.cursor = cursor
        if (data.isEmpty()) {
            setEmptyView()
        } else {
            data.forEach { result ->
                if ((result.type == KEYWORD_TYPE_EXACT || result.type == KEYWORD_TYPE_PHRASE)) {
                    isnewlyAddded.add(false)
                    initialBudget.add(result.priceBid)
                    originalKeyList.add(result.tag)
                    existingKeyword?.add(result)
                }
            }
            if (existingKeyword?.isEmpty() == true) {
                setEmptyView()
            } else {
                checkForCommonData()
                setVisibilityOperation(View.VISIBLE)
            }
            recyclerviewScrollListener.updateStateAfterGetData()
            adapter.getBidData(initialBudget, isnewlyAddded)
        }

    }

    private fun setEmptyView() {
        adapter.items.clear()
        adapter.items.add(EditKeywordEmptyViewModel())
        setVisibilityOperation(View.GONE)
        adapter.notifyDataSetChanged()
    }

    private fun setVisibilityOperation(visibility: Int) {
        recyclerView.visibility = visibility
        addKeyword.visibility = visibility
        ticker.visibility = visibility
        keywordTitle.visibility = visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OK) {
            if (resultCode == Activity.RESULT_OK) {
                selectedData = data?.getParcelableArrayListExtra(SELECTED_DATA)
                if (selectedData?.size != 0)
                    updateKeywords(selectedData)
            }
        }
    }

    private fun updateKeywords(selectedKeywords: ArrayList<KeywordDataItem>?) {
        if (adapter.items.isNotEmpty() && adapter.items[0] is EditKeywordEmptyViewModel) {
            adapter.items.clear()
        }
        selectedKeywords?.forEach {
            if (adapter.items.find { item -> it.keyword == (item as EditKeywordItemViewModel).data.name } == null) {
                if (it.bidSuggest == "0")
                    it.bidSuggest = minSuggestKeyword
                adapter.items.add(EditKeywordItemViewModel(maptoSharedModel(it)))
//                adapter.items.add(EditKeywordItemViewModel(GetKeywordResponse.KeywordsItem(KEYWORD_TYPE_PHRASE, KEYWORD_EXISTS,
//                        "0", it.bidSuggest, false, it.keyword, it.source)))
                initialBudget.add(it.bidSuggest)
                isnewlyAddded.add(true)
                if (!isExistsOriginal(it.keyword)) {
                    addedKeywords?.add(maptoSharedModel(it))
                }
            }
        }
        adapter.getBidData(initialBudget, isnewlyAddded)
        setVisibilityOperation(View.VISIBLE)
    }

    private fun maptoSharedModel(it: KeywordDataItem): KeySharedModel {
        return KeySharedModel(
                it.keyword,
                it.totalSearch,
                it.competition,
                it.bidSuggest,
                "400",
                it.source,
                it.keywordType,
                11,
                1,
                "0",
                it.bidSuggest

        )

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is ButtonAction) {
            callBack = parentFragment as ButtonAction
        } else {
            throw RuntimeException("The parent fragment must implement ButtonAction")
        }

    }

    fun sendData(): Bundle {
        val bundle = Bundle()
        val list: ArrayList<KeySharedModel> = arrayListOf()
        list.addAll(adapter.getCurrentItems())

        if (adapter.items.isNotEmpty() && adapter.items[0] !is EditKeywordEmptyViewModel) {
            adapter.items.forEachIndexed { index, item ->
                if ((item as EditKeywordItemViewModel).data.priceBid != adapter.data[index]) {
                    if (isExistsOriginal(item.data.name))
                        editedKeywords?.add(item.data)
                }
            }
        }
        bundle.putParcelableArrayList(POSITIVE_CREATE, addedKeywords)
        bundle.putParcelableArrayList(POSITIVE_DELETE, deletedKeywords)
        bundle.putParcelableArrayList(POSITIVE_EDIT, editedKeywords)
        bundle.putParcelableArrayList(POSITIVE_KEYWORD_ALL, list)
        return bundle
    }

    interface ButtonAction {
        fun buttonDisable(enable: Boolean)
    }

}