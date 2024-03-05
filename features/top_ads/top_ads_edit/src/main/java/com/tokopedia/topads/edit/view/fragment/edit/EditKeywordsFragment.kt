package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_TYPE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.DEFAULT_NEW_KEYWORD_VALUE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.EXACT_POSITIVE
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_GROUP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_BROWSE
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_SEARCH
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.KeySharedModel
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.view.sheet.TopAdsEditKeywordBidSheet
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.BID_TYPE
import com.tokopedia.topads.edit.utils.Constants.FROM_EDIT
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.IS_AUTO_BID
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
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID_LIST
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
import com.tokopedia.topads.edit.view.sheet.ChooseKeyBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import javax.inject.Inject
import com.tokopedia.topads.common.R as topadscommonR

/**
 * Created by Pika on 12/4/20.
 */

private const val CLICK_TAMBAH_KATA_KUNCI = "click - tambah kata kunci"
private const val CLICK_DAILY_BUDGET_BOX = "click - box biaya iklan pencarian"
private const val CLICK_DAILY_BUDGET_REKOMENDASI_BOX =
    "click - box biaya iklan manual di rekomendasi"
private const val CLICK_EDIT_KEYWORD_TYPE = "click - button edit luas pencarian"
private const val CLICK_EDIT_KEYWORD_BID = "click - edit kata kunci"
private const val CLICK_EDIT_KEYWORD_DELETE = "click - delete icon kata kunci"

class EditKeywordsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var adapter: EditKeywordListAdapter? = null
    private lateinit var callBack: ButtonAction
    private var minSuggestKeyword = Int.ZERO.toString()
    private var maxSuggestKeyword = Int.ZERO.toString()
    private var deletedKeywords: ArrayList<KeySharedModel>? = arrayListOf()
    private var addedKeywords: ArrayList<KeySharedModel>? = arrayListOf()
    private var editedKeywords: ArrayList<KeySharedModel>? = arrayListOf()
    private var existingKeyword: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var recommendedKeywords: ArrayList<KeywordDataItem>? = arrayListOf()
    private var initialBudget: MutableList<String> = mutableListOf()
    private var isnewlyAddded: MutableList<Boolean> = mutableListOf()
    private var originalKeyList: MutableList<String> = arrayListOf()
    private var selectedData: ArrayList<KeywordDataItem>? = arrayListOf()
    private var bidTypeData: ArrayList<TopAdsBidSettingsModel>? = arrayListOf()
    private var cursor = ""
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedKeyword: Typography
    private var receivedRecom = false
    private var receivedKeywords = false
    private var productId: MutableList<String> = mutableListOf()

    private var userID: String = String.EMPTY
    private lateinit var addKeyword: Typography
    private var minBid = Int.ZERO.toString()
    private var maxBid = Int.ZERO.toString()
    private var suggestBidPerClick = Int.ZERO.toString()
    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider[EditFormDefaultViewModel::class.java]
    }

    private val viewModelKeyword by lazy {
        viewModelProvider[KeywordAdsViewModel::class.java]
    }

    private var groupId = Int.ZERO
    private var productIds = String.EMPTY

    override fun getScreenName(): String {
        return EditKeywordsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditKeywordListAdapter(
            EditKeywordListAdapterTypeFactoryImpl(
                this::onAddKeyword,
                ::onDeleteItem,
                this::onEditBudget,
                ::onEditType
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            context?.resources?.getLayout(topadscommonR.layout.topads_create_fragment_budget_list),
            container,
            false
        )
        recyclerView = view.findViewById(topadscommonR.id.bid_list)
        addKeyword = view.findViewById(topadscommonR.id.addKeyword)
        selectedKeyword = view.findViewById(topadscommonR.id.selectedKeyword)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (cursor != String.EMPTY) {
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
        arguments?.getStringArrayList(PRODUCT_ID_LIST).let {
            productIds = it?.joinToString(",") ?: ""
            productId = it?.toMutableList() ?: mutableListOf()
            if (productIds.isNotEmpty() && recommendedKeywords?.isEmpty() == true)
                viewModelKeyword.getSuggestionKeyword(productIds, Int.ZERO, ::onSuccessRecommended)
            if (productIds.isNotEmpty()) {
                getLatestBid()
            }
            getBidForKeywords()

        }

        arguments?.getString(IS_AUTO_BID).let {
            if (it?.isEmpty() == true && productIds.isNotEmpty()) {
                getLatestBid()
                viewModelKeyword.getSuggestionKeyword(productIds, Int.ZERO, ::onSuccessRecommended)
            }
        }

    }

    private fun getBidForKeywords() {
        val suggestions = java.util.ArrayList<DataSuggestions>()
        val dummyId: MutableList<String> = mutableListOf()
        suggestions.add(DataSuggestions(GROUP, dummyId))
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion)
    }

    private fun getLatestBid() {
        val suggestionsDefault = java.util.ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions(String.EMPTY, listOf(groupId.toString())))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onBidSuccessSuggestion)
    }

    fun getSuggestedBidSettings(): List<GroupEditInput.Group.TopadsSuggestionBidSetting> {
        return listOf(
            GroupEditInput.Group.TopadsSuggestionBidSetting(
                PRODUCT_SEARCH,
                suggestBidPerClick.toFloat()
            ),
            GroupEditInput.Group.TopadsSuggestionBidSetting(
                PRODUCT_BROWSE,
                suggestBidPerClick.toFloat()
            )
        )
    }

    private fun onBidSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            suggestBidPerClick = it.suggestionBid
            minBid = it.minBid
            maxBid = it.maxBid
        }
    }

    private fun onEditBudget(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(CLICK_EDIT_KEYWORD_BID, "")
        val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(pos))
        sheet.show(childFragmentManager, String.EMPTY)
        sheet.onSaved = { bid, position ->
            (adapter?.items?.getOrNull(position) as? EditKeywordItemViewModel)?.let {
                it.data.priceBid = bid
                adapter?.notifyItemChanged(position)
            }
        }
    }

    private fun onEditType(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(CLICK_EDIT_KEYWORD_TYPE, "")
        val sheet = ChooseKeyBottomSheet.newInstance()
        val item = (adapter?.items?.getOrNull(pos) as? EditKeywordItemViewModel)

        val type = item?.data?.typeInt
        if (type != null) {
            sheet.show(childFragmentManager, type)
        }

        sheet.onSelect = { typeKey ->
            val typeInt = if (typeKey == BROAD_TYPE)
                BROAD_POSITIVE
            else
                EXACT_POSITIVE


            item?.let {
                item.data.typeInt = typeInt
                if (item.data.typeInt != type) {
                    actionStatusChange(pos)
                }
            }

            adapter?.notifyItemChanged(pos)
        }
    }

    private fun onSuccessRecommended(keywords: List<KeywordData>) {
        receivedRecom = true
        recommendedKeywords?.clear()
        keywords.forEach {
            recommendedKeywords?.addAll(Int.ZERO, it.keywordData)
        }
        if (existingKeyword?.isEmpty() == false)
            checkForCommonData()
    }

    private fun checkForCommonData() {
        if (!receivedKeywords || !receivedRecom)
            return
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
            if (!commonToRecommendation) {
                listItem.add(mapToModelManual(selected))
            }
        }
        adapter?.items?.clear()
        listItem.forEach {
            adapter?.items?.add(EditKeywordItemViewModel(it))
        }
        setCount()
        adapter?.notifyItemRangeChanged(Int.ZERO, listItem.size)
    }

    private fun onDeleteItem(position: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
            CLICK_EDIT_KEYWORD_DELETE,
            ""
        )
        showConfirmationDialog(position)
    }

    private fun updateString() {
        if (adapter?.items?.count() == 0)
            setEmptyView()
    }

    private fun setCount() {
        selectedKeyword.text =
            String.format(
                getString(topadscommonR.string.topads_common_selected_keyword),
                adapter?.items?.count()
            )
    }

    private fun mapToModelManual(selected: GetKeywordResponse.KeywordsItem): KeySharedModel {
        return KeySharedModel(
            selected.tag,
            "-1",
            getString(topadscommonR.string.topads_common_keyword_competition_unknown),
            selected.priceBid,
            minSuggestKeyword,
            selected.source,
            selected.type,
            selected.keywordId,
        )
    }

    private fun mapToModel(
        selected: GetKeywordResponse.KeywordsItem,
        recommend: KeywordDataItem
    ): KeySharedModel {
        return KeySharedModel(
            selected.tag,
            recommend.totalSearch,
            recommend.competition,
            selected.priceBid,
            minSuggestKeyword,
            selected.source,
            selected.type,
            selected.keywordId,
        )
    }

    private fun prepareBundle(pos: Int): Bundle {
        val bundle = Bundle()
        if ((adapter?.items?.getOrNull(pos) as EditKeywordItemViewModel).data.priceBid == Int.ZERO.toString())
            (adapter?.items?.getOrNull(pos) as EditKeywordItemViewModel).data.priceBid =
                minSuggestKeyword
        bundle.putString(MAX_BID, maxSuggestKeyword)
        bundle.putString(MIN_BID, minSuggestKeyword)
        bundle.putString(
            SUGGESTION_BID,
            (adapter?.items?.getOrNull(pos) as EditKeywordItemViewModel).data.priceBid
        )
        bundle.putInt(ITEM_POSITION, pos)
        bundle.putString(
            KEYWORD_NAME,
            (adapter?.items?.getOrNull(pos) as EditKeywordItemViewModel).data.name
        )
        bundle.putInt(FROM_EDIT, Int.ONE)
        bundle.putString(GROUPID, groupId.toString())
        return bundle
    }

    private fun actionStatusChange(position: Int) {
        if (isExistsOriginal(position)) {
            deletedKeywords?.add((adapter?.items?.getOrNull(position) as EditKeywordItemViewModel).data)
            addedKeywords?.add((adapter?.items?.getOrNull(position) as EditKeywordItemViewModel).data)
        }
    }

    private fun showConfirmationDialog(position: Int) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(
                String.format(
                    getString(R.string.topads_edit_delete_keyword_conf_dialog_title),
                    (adapter?.items?.getOrNull(position) as EditKeywordItemViewModel).data.name
                )
            )
            dialog.setDescription(
                MethodChecker.fromHtml(
                    String.format(
                        getString(R.string.topads_edit_delete_keyword_conf_dialog_desc),
                        (adapter?.items?.getOrNull(position) as EditKeywordItemViewModel).data.name
                    )
                )
            )
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
        adapter?.setBid(minSuggestKeyword)
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
        var pos = Int.ZERO
        if (selectedData?.isNotEmpty() == true) {
            selectedData?.forEachIndexed { index, select ->
                if (select.keyword == (adapter?.items?.getOrNull(position) as EditKeywordItemViewModel).data.name) {
                    pos = index
                }
            }
            selectedData?.removeAt(pos)
        }
        if (adapter?.items?.getOrNull(position) is EditKeywordItemViewModel) {
            if (isExistsOriginal(position)) {
                deletedKeywords?.add((adapter?.items?.getOrNull(position) as EditKeywordItemViewModel).data)
            } else {
                if (addedKeywords?.isNotEmpty() == true) {
                    var index = Int.ZERO
                    addedKeywords?.forEachIndexed { it, key ->
                        if (key.name == (adapter?.items?.getOrNull(position) as EditKeywordItemViewModel).data.name) {
                            index = it
                        }
                    }
                    addedKeywords?.removeAt(index)
                }
            }
        }
        if (position < (adapter?.itemCount ?: Int.ZERO))
            adapter?.items?.removeAt(position)
        if (position < isnewlyAddded.size)
            isnewlyAddded.removeAt(position)
        if (position < initialBudget.size)
            initialBudget.removeAt(position)
        if (adapter?.items?.isEmpty() == true) {
            setEmptyView()
        }
        if (position >= Int.ZERO)
            adapter?.notifyItemRemoved(position)
        setCount()
        adapter?.getBidData(initialBudget, isnewlyAddded)
        updateString()
        view?.let {
            Toaster.build(
                it,
                getString(topadscommonR.string.topads_keyword_common_del_toaster),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL
            ).show()
        }
    }

    private fun isExistsOriginal(position: Int): Boolean {
        return (originalKeyList.find { (adapter?.items?.getOrNull(position) as? EditKeywordItemViewModel)?.data?.name == it } != null)
    }

    private fun isExistsOriginal(name: String): Boolean {
        return (originalKeyList.find { name == it } != null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userID = UserSession(view.context).userId
        registerObservers()
        addKeyword.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(
                CLICK_TAMBAH_KATA_KUNCI,
                ""
            )
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                CLICK_TAMBAH_KATA_KUNCI,
                ""
            )
            onAddKeyword()
        }
    }

    private fun registerObservers() {
        arguments?.getString(PARAM_GROUP_Id).let {
            groupId = it.toIntOrZero()
            viewModel.getAdKeyword(groupId, cursor, this::onSuccessKeyword)
        }
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>, cursor: String) {
        this.cursor = cursor
        receivedKeywords = true
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
            adapter?.getBidData(initialBudget, isnewlyAddded)
        }

    }

    private fun setEmptyView() {
        adapter?.clearList()
        adapter?.items?.add(EditKeywordEmptyViewModel())
        setVisibilityOperation(View.GONE)
        val position = adapter?.items?.indexOfFirst { it is EditKeywordEmptyViewModel }
        position?.let {
            if (position >= Int.ZERO) adapter?.notifyItemChanged(it)
        }
    }

    private fun setVisibilityOperation(visibility: Int) {
        addKeyword.visibility = visibility
        selectedKeyword.visibility = visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OK) {
            if (resultCode == Activity.RESULT_OK) {
                selectedData = data?.getParcelableArrayListExtra(SELECTED_DATA)
                if (selectedData?.size != Int.ZERO) {
                    updateKeywords(selectedData)
                    setCount()
                }
            }
        }
    }

    private fun updateKeywords(selectedKeywords: ArrayList<KeywordDataItem>?) {
        if (adapter?.items?.isNotEmpty() == true && adapter?.items?.firstOrNull() is EditKeywordEmptyViewModel) {
            adapter?.clearList()
        }
        selectedKeywords?.forEach {
            if (adapter?.items?.find { item -> it.keyword == (item as EditKeywordItemViewModel).data.name } == null) {
                if (it.bidSuggest == Int.ZERO.toString())
                    it.bidSuggest = DEFAULT_NEW_KEYWORD_VALUE
                adapter?.items?.add(EditKeywordItemViewModel(mapToSharedModel(it)))
                initialBudget.add(it.bidSuggest)
                isnewlyAddded.add(true)
                if (!isExistsOriginal(it.keyword)) {
                    addedKeywords?.add(mapToSharedModel(it))
                }
            }
        }
        adapter?.getBidData(initialBudget, isnewlyAddded)
        setVisibilityOperation(View.VISIBLE)
    }

    private fun mapToSharedModel(it: KeywordDataItem): KeySharedModel {
        return KeySharedModel(
            it.keyword,
            it.totalSearch,
            it.competition,
            it.bidSuggest,
            minSuggestKeyword,
            it.source,
            if (it.keywordType == TopAdsCommonConstant.SPECIFIC_TYPE) EXACT_POSITIVE else BROAD_POSITIVE,
            Int.ZERO.toString(),
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

        adapter?.let { adapter ->
            list.addAll(adapter.getCurrentItems())
            if (adapter.items.isNotEmpty() && adapter.items.getOrNull(Int.ZERO) !is EditKeywordEmptyViewModel) {
                adapter.items.forEachIndexed { index, item ->
                    if (index < adapter.data.size &&
                        (item as EditKeywordItemViewModel).data.priceBid != (adapter.data.getOrNull(
                            index
                        ))
                    ) {
                        if (isExistsOriginal(item.data.name))
                            editedKeywords?.add(item.data)
                    }
                }
            }
        }
        bidTypeData?.clear()
        bundle.putParcelableArrayList(BID_TYPE, bidTypeData)
        bundle.putParcelableArrayList(POSITIVE_CREATE, addedKeywords)
        bundle.putParcelableArrayList(POSITIVE_DELETE, deletedKeywords)
        bundle.putParcelableArrayList(POSITIVE_EDIT, editedKeywords)
        bundle.putParcelableArrayList(POSITIVE_KEYWORD_ALL, list)
        return bundle
    }

    interface ButtonAction {
        fun buttonDisable(enable: Boolean)
    }

    companion object {

        fun newInstance(bundle: Bundle?): EditKeywordsFragment {
            val fragment = EditKeywordsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
