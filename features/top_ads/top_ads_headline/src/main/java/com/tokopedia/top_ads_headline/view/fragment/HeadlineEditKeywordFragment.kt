package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.Constants.ACTION_CREATE
import com.tokopedia.top_ads_headline.Constants.ACTION_DELETE
import com.tokopedia.top_ads_headline.Constants.ACTION_EDIT
import com.tokopedia.top_ads_headline.Constants.ACTIVE_STATUS
import com.tokopedia.top_ads_headline.Constants.CURRENT_LIST
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_EXACT
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_NEGATIVE_EXACT
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.top_ads_headline.Constants.NEGATIVE_PHRASE
import com.tokopedia.top_ads_headline.Constants.NEGATIVE_SPECIFIC
import com.tokopedia.top_ads_headline.Constants.POSITIVE_PHRASE
import com.tokopedia.top_ads_headline.Constants.POSITIVE_SPECIFIC
import com.tokopedia.top_ads_headline.Constants.RESTORED_DATA
import com.tokopedia.top_ads_headline.Constants.SELECTED_KEYWORD
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.di.HeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.EditTopAdsHeadlineKeywordActivity
import com.tokopedia.top_ads_headline.view.activity.SaveButtonState
import com.tokopedia.top_ads_headline.view.viewmodel.HeadlineEditKeywordViewModel
import com.tokopedia.top_ads_headline.view.viewmodel.SharedEditHeadlineViewModel
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.util.SpaceItemDecoration
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditEmptyAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditAdKeywordModel
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditEmptyAdKeywordModel
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.KeywordUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewholder.TipsUiSortViewHolder
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiSortModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_headline_edit_keyword.*
import javax.inject.Inject

const val KEYWORD_POSITIVE = "keywordPositive"
const val KEYWORD_NEGATIVE = "keywordNegative"
const val KEYWORD_TYPE = "keywordType"
const val POSITIVE_KEYWORD_REQUEST_CODE = 101
const val NEGATIVE_KEYWORD_REQUEST_CODE = 102
const val ADDED_KEYWORDS = "addedKeywords"
const val EDITED_KEYWORDS = "editedKeywords"
const val DELETED_KEYWORDS = "deletedKeywords"

class HeadlineEditKeywordFragment : BaseDaggerFragment(), HeadlineEditAdKeywordViewHolder.OnHeadlineAdEditItemClick,
        HeadlineEditEmptyAdKeywordViewHolder.OnHeadlineEmptyKeywordButtonClick {
    private var keywordType: String = ""
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private var cursor = ""

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HeadlineEditKeywordViewModel
    private var sharedEditHeadlineViewModel: SharedEditHeadlineViewModel? = null
    private lateinit var adapter: KeywordListAdapter
    private var groupId = 0
    private var stepperModel: HeadlineAdStepperModel? = null
    private var restoreNegativeKeywords: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()

    private var negativeAddedKeywords: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
    private var selectedKeywordsList: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()

    private var saveButtonState: SaveButtonState? = activity as? SaveButtonState

    companion object {
        fun getInstance(keywordType: String, groupId: Int): HeadlineEditKeywordFragment {
            return HeadlineEditKeywordFragment().apply {
                arguments = Bundle().apply {
                    putString(KEYWORD_TYPE, keywordType)
                    putInt(GROUP_ID, groupId)
                }
            }
        }
    }

    override fun getScreenName(): String {
        return HeadlineEditKeywordFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(HeadlineAdsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            keywordType = getString(KEYWORD_TYPE) ?: ""
            groupId = getInt(GROUP_ID)
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(HeadlineEditKeywordViewModel::class.java)
        activity?.let {
            sharedEditHeadlineViewModel = ViewModelProvider(it, viewModelFactory).get(SharedEditHeadlineViewModel::class.java)
        }
        adapter = KeywordListAdapter(this, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.fragment_headline_edit_keyword), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        fetchNextPage()
        setAdapter()
        add_keyword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.topads_plus_add_keyword, 0, 0, 0)
        add_keyword.setOnClickListener {
            onCtaBtnClick()
        }
    }

    private fun openNegativeAdKeywordActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_NEGATIVE_KEYWORD_EDIT)?.apply {
            putParcelableArrayListExtra(RESTORED_DATA, restoreNegativeKeywords)
            putStringArrayListExtra(CURRENT_LIST, getCurrentItems())
        }
        startActivityForResult(intent, NEGATIVE_KEYWORD_REQUEST_CODE)
    }

    private fun openPositiveAdKeywordActivity() {
        stepperModel?.selectedKeywords = getSelectedKeywords(selectedKeywordsList)
        val intent = Intent(context, EditTopAdsHeadlineKeywordActivity::class.java).apply {
            putExtra(BaseStepperActivity.STEPPER_MODEL_EXTRA, stepperModel)
        }
        startActivityForResult(intent, POSITIVE_KEYWORD_REQUEST_CODE)
    }

    private fun getCurrentItems(): ArrayList<String>? {
        val list: ArrayList<String> = arrayListOf()
        adapter.getItems().forEach {
            if (it is HeadlineEditAdKeywordModel) {
                list.add(it.keywordName)
            }
        }
        return list
    }

    private fun setUpObservers() {
        sharedEditHeadlineViewModel?.getEditHeadlineAdLiveData()?.observe(viewLifecycleOwner, Observer {
            stepperModel = it
            setUpKeywords()
        })
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.parent.parent.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
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
        viewModel.getAdKeyword(userSession.shopId, groupId, cursor, this::onSuccessKeyword, keywordType = keywordType)
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>, cursor: String) {
        selectedKeywordsList = data as ArrayList<GetKeywordResponse.KeywordsItem>
        if (keywordType == KEYWORD_NEGATIVE) {
            getRestoredNegativeKeywords()
        }
        this.cursor = cursor
        if (stepperModel != null) {
            setUpKeywords()
        }
    }

    private fun getRestoredNegativeKeywords() {
        selectedKeywordsList.filter { it.type == KEYWORD_TYPE_NEGATIVE_PHRASE || it.type == KEYWORD_TYPE_NEGATIVE_EXACT }.let {
            restoreNegativeKeywords.clear()
            it.forEach { item ->
                item.isChecked = true
                restoreNegativeKeywords.add(item)
            }
        }
    }

    private fun getKeywordUiModels(data: List<GetKeywordResponse.KeywordsItem>): java.util.ArrayList<KeywordUiModel> {
        val keywordUiModels = ArrayList<KeywordUiModel>()
        data.forEach { result ->
            if (result.status != -1) {
                val keywordSubType: String = getKeywordSearchTitle(result.type)
                if (keywordType == KEYWORD_POSITIVE && (result.type == KEYWORD_TYPE_PHRASE || result.type == KEYWORD_TYPE_EXACT)) {
                    keywordUiModels.add(HeadlineEditAdKeywordModel(result.tag, keywordSubType,
                            advertisingCost = Utils.convertToCurrency(result.priceBid.toLong()), priceBid = result.priceBid,
                            maximumBid = stepperModel?.maxBid
                                    ?: "0", minimumBid = stepperModel?.minBid ?: "0"))
                } else if (keywordType == KEYWORD_NEGATIVE &&
                        (result.type == KEYWORD_TYPE_NEGATIVE_PHRASE || result.type == KEYWORD_TYPE_NEGATIVE_EXACT)) {
                    keywordUiModels.add(HeadlineEditAdKeywordModel(result.tag, keywordSubType,
                            advertisingCost = Utils.convertToCurrency(result.priceBid.toLong()), priceBid = result.priceBid, isNegativeKeyword = true,
                            maximumBid = stepperModel?.maxBid
                                    ?: "0", minimumBid = stepperModel?.minBid ?: "0"))
                }
            }
        }
        showEmptyView(keywordUiModels.isEmpty())
        val keywordCounter = if (keywordType == KEYWORD_POSITIVE) {
            "${keywordUiModels.size} ${getString(R.string.topads_headline_edit_kata_kunci)}"
        } else {
            "${keywordUiModels.size} ${getString(R.string.topads_headline_edit_kata_kunci_neg)}"
        }
        if (keywordUiModels.isEmpty()) {
            if (keywordType == KEYWORD_POSITIVE) {
                keywordUiModels.add(HeadlineEditEmptyAdKeywordModel(R.string.topads_headline_edit_keyword_empty_kata_kunci_header,
                        R.string.topads_headline_edit_keyword_empty_kata_kunci_subheader, R.string.topads_headline_edit_keyword_empty_kata_kunci_cta))
            } else {
                keywordUiModels.add(HeadlineEditEmptyAdKeywordModel(R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_header,
                        R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_subheader, R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_cta))
            }
        }
        keyword_counter.text = keywordCounter
        return keywordUiModels
    }

    private fun getSelectedKeywords(data: List<GetKeywordResponse.KeywordsItem>): MutableList<KeywordDataItem> {
        return MutableList(data.size) {
            KeywordDataItem(keyword = data[it].tag, bidSuggest = data[it].priceBid)
        }
    }

    private fun getKeywordSearchTitle(type: Int): String {
        return if (type == KEYWORD_TYPE_PHRASE || type == KEYWORD_TYPE_NEGATIVE_PHRASE) {
            getString(R.string.topads_headline_broad_sort_type_header)
        } else {
            getString(R.string.topads_headline_specific_sort_type_header)
        }
    }

    private fun showEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            keyword_counter.hide()
            add_keyword.hide()
        } else {
            keyword_counter.show()
            add_keyword.show()
            if (keywordType == KEYWORD_POSITIVE) {
                add_keyword.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_cta)
            } else {
                add_keyword.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_cta)
            }
        }
    }

    override fun onDeleteItemClick(keywordModel: HeadlineEditAdKeywordModel) {
        showConfirmationDialog(keywordModel)
    }

    private fun showConfirmationDialog(keywordModel: HeadlineEditAdKeywordModel) {
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        if (keywordType == KEYWORD_POSITIVE) {
            dialog.setTitle(getString(R.string.topads_headline_edit_keyword_delete_kata_kunci_title))
            dialog.setDescription(getString(R.string.topads_headline_edit_keyword_delete_kata_kunci_description, keywordModel.keywordName))
        } else {
            dialog.setTitle(getString(R.string.topads_headline_edit_keyword_delete_kata_kunci_negatif_title))
            dialog.setDescription(getString(R.string.topads_headline_edit_keyword_delete_kata_kunci_negatif_description, keywordModel.keywordName))
        }
        dialog.setPrimaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_batal))
        dialog.setSecondaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_ya))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            deleteKeyword(keywordModel)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteKeyword(keywordModel: HeadlineEditAdKeywordModel) {
        val deletedItem: GetKeywordResponse.KeywordsItem? = selectedKeywordsList.find { keyword -> keyword.tag == keywordModel.keywordName }
        selectedKeywordsList.remove(deletedItem)
        if (keywordType == KEYWORD_POSITIVE) {
            val removedItem = stepperModel?.selectedKeywords?.find { it.keyword == keywordModel.keywordName }
            val removedManualItem = stepperModel?.manualSelectedKeywords?.find { it.keyword == keywordModel.keywordName }
            stepperModel?.selectedKeywords?.remove(removedItem)
            stepperModel?.manualSelectedKeywords?.remove(removedManualItem)
        } else {
            restoreNegativeKeywords.remove(deletedItem)
        }
        adapter.removeItem(keywordModel)
        if (adapter.itemCount != 0) {
            updateCounter(adapter.itemCount)
            adapter.notifyDataSetChanged()
        } else {
            setUpKeywords()
        }
    }

    private fun updateCounter(itemCount: Int) {
        val keywordCount = keyword_counter.text
        val spaceIndex = keywordCount.indexOf(' ')
        if (spaceIndex != -1) {
            keyword_counter.text = keywordCount.replaceRange(0 until spaceIndex, itemCount.toString())
        }
    }

    override fun onSearchTypeClick(keywordModel: HeadlineEditAdKeywordModel) {
        val tipsList: ArrayList<TipsUiModel> = ArrayList()
        tipsList.apply {
            add(TipsUiSortModel(R.string.topads_headline_broad_sort_type_header, R.string.topads_headline_broad_sort_type_subheader, keywordModel.searchType.equals(
                    getString(R.string.topads_headline_broad_sort_type_header), true
            )))
            add(TipsUiSortModel(R.string.topads_headline_specific_sort_type_header, R.string.topads_headline_specific_sort_type_subheader, keywordModel.searchType.equals(
                    getString(R.string.topads_headline_specific_sort_type_header), true
            )))
        }
        val tipsSortListSheet = context?.let { it1 ->
            TipsListSheet.newInstance(it1, tipsList = tipsList, itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        tipsSortListSheet?.setTitle(getString(R.string.topads_headline_sort_type_title))
        tipsSortListSheet?.showHeader = true
        tipsSortListSheet?.showKnob = false

        tipsSortListSheet?.setOnUiSortItemClickListener(sortItemClick = object : TipsUiSortViewHolder.OnUiSortItemClick {
            override fun onItemClick(sortModel: TipsUiSortModel) {
                tipsSortListSheet.getTipsList().forEach { model ->
                    if (model is TipsUiSortModel && model == sortModel) {
                        model.isChecked = true
                        changeSearchStatus(keywordModel, getString(model.headerText))
                    } else {
                        (model as? TipsUiSortModel)?.isChecked = false
                    }
                }
                tipsSortListSheet.dismissAllowingStateLoss()
            }
        })
        tipsSortListSheet?.show(childFragmentManager, "")
    }

    override fun onEditPriceBid(isEnabled: Boolean, keywordModel: HeadlineEditAdKeywordModel) {
        saveButtonState?.setButtonState(isEnabled)
        if (isEnabled) {
            selectedKeywordsList.find { keyword -> keyword.tag == keywordModel.keywordName && keyword.priceBid != keywordModel.priceBid }?.let {
                it.priceBid = keywordModel.priceBid
            }
        }
    }

    private fun changeSearchStatus(keywordModel: HeadlineEditAdKeywordModel, searchType: String) {
        if (searchType.equals(getString(R.string.topads_headline_broad_sort_type_header), false)) {
            selectedKeywordsList.find { keyword -> keyword.tag == keywordModel.keywordName }?.type = if (keywordType == KEYWORD_POSITIVE) {
                KEYWORD_TYPE_PHRASE
            } else {
                KEYWORD_TYPE_NEGATIVE_PHRASE
            }
        } else {
            selectedKeywordsList.find { keyword -> keyword.tag == keywordModel.keywordName }?.type = if (keywordType == KEYWORD_POSITIVE) {
                KEYWORD_TYPE_EXACT
            } else {
                KEYWORD_TYPE_NEGATIVE_EXACT
            }
        }
        keywordModel.searchType = searchType
        val position = adapter.getItems().indexOf(keywordModel)
        if (position != -1) {
            adapter.notifyItemChanged(position)
        }
    }

    override fun onCtaBtnClick() {
        if (keywordType == KEYWORD_POSITIVE) {
            openPositiveAdKeywordActivity()
        } else {
            openNegativeAdKeywordActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NEGATIVE_KEYWORD_REQUEST_CODE) {
                getDataForNegativeKeywordActivity(data)
            } else if (requestCode == POSITIVE_KEYWORD_REQUEST_CODE) {
                getDataForPositiveKeywordActivity(data)
            }
        }
    }

    private fun getDataForPositiveKeywordActivity(data: Intent?) {
        data?.getStringArrayListExtra(DELETED_KEYWORDS)?.let { list ->
            val deletedItems = ArrayList<GetKeywordResponse.KeywordsItem>()
            list.forEach { keyword ->
                selectedKeywordsList.find { item -> item.tag == keyword }?.let {
                    deletedItems.add(it)
                }
            }
            selectedKeywordsList.removeAll(deletedItems)
        }
        data?.getParcelableArrayListExtra<KeywordDataItem>(EDITED_KEYWORDS)?.let { it ->
            it.forEach { data ->
                selectedKeywordsList.find { keyword -> keyword.tag == data.keyword }?.let {
                    it.priceBid = data.bidSuggest
                }
            }
        }
        data?.getParcelableArrayListExtra<KeywordDataItem>(ADDED_KEYWORDS)?.let {
            it.forEach { data ->
                selectedKeywordsList.add(GetKeywordResponse.KeywordsItem(tag = data.keyword, priceBid = data.bidSuggest))
            }
        }
        data?.getParcelableExtra<HeadlineAdStepperModel>(BaseStepperActivity.STEPPER_MODEL_EXTRA)?.let {
            stepperModel = it
        }
        setUpKeywords(selectedKeywordsList)
    }

    private fun getDataForNegativeKeywordActivity(data: Intent?) {
        val selected = data?.getParcelableArrayListExtra<GetKeywordResponse.KeywordsItem>(SELECTED_KEYWORD)
        data?.getParcelableArrayListExtra<GetKeywordResponse.KeywordsItem>(RESTORED_DATA)?.let {
            restoreNegativeKeywords = it
        }
        if (selected?.size != 0) {
            selected?.let { setUpKeywords(it) }
            selected?.forEach {
                if (!selectedKeywordsList.contains(it)) {
                    negativeAddedKeywords.add(it)
                }
            }
        }
        selectedKeywordsList = selected as ArrayList<GetKeywordResponse.KeywordsItem>
    }

    private fun setUpKeywords(list: ArrayList<GetKeywordResponse.KeywordsItem> = selectedKeywordsList) {
        adapter.clearAllItems()
        val keywordUiModels = getKeywordUiModels(list)
        adapter.setKeywordItems(keywordUiModels)
    }

    fun getKeywordOperations(): List<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation> {
        val list = ArrayList<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation>()
        val commonItems = ArrayList<GetKeywordResponse.KeywordsItem>()
        val tempList = ArrayList<GetKeywordResponse.KeywordsItem>()
        tempList.addAll(selectedKeywordsList)
        viewModel.getSelectedKeywords().forEach {
            val item = selectedKeywordsList.find { keywordsItem -> keywordsItem.tag == it.tag }?.let { keywordItem ->
                if (it.type != keywordItem.type) {
                    val tempKeywordDataItem = keywordItem.copy(type = if (keywordItem.type == KEYWORD_TYPE_PHRASE) {
                        KEYWORD_TYPE_EXACT
                    } else {
                        KEYWORD_TYPE_PHRASE
                    })
                    list.add(getKeywordOperation(tempKeywordDataItem, ACTION_DELETE))
                    list.add(getKeywordOperation(keywordItem, ACTION_CREATE))
                } else if (it.priceBid != keywordItem.priceBid) {
                    list.add(getKeywordOperation(keywordItem, ACTION_EDIT))
                }
                commonItems.add(keywordItem)
            }
            if (item == null) {
                list.add(getKeywordOperation(it, ACTION_DELETE))
            }
        }
        tempList.removeAll(commonItems)
        tempList.forEach {
            list.add(getKeywordOperation(it, ACTION_CREATE))
        }
        return list
    }

    private fun getKeywordOperation(it: GetKeywordResponse.KeywordsItem, action: String): TopAdsManageHeadlineInput.Operation.Group.KeywordOperation {
        return TopAdsManageHeadlineInput.Operation.Group.KeywordOperation(
                action = action,
                keyword = TopAdsManageHeadlineInput.Operation.Group.KeywordOperation.Keyword().apply {
                    id = it.keywordId
                    priceBid = it.priceBid.toLong()
                    tag = it.tag
                    status = ACTIVE_STATUS
                    type = getKeywordSearchType(it.type)
                })
    }

    private fun getKeywordSearchType(type: Int): String {
        return when (type) {
            KEYWORD_TYPE_PHRASE -> {
                POSITIVE_PHRASE
            }
            KEYWORD_TYPE_EXACT -> {
                POSITIVE_SPECIFIC
            }
            KEYWORD_TYPE_NEGATIVE_PHRASE -> {
                NEGATIVE_PHRASE
            }
            KEYWORD_TYPE_NEGATIVE_EXACT -> {
                NEGATIVE_SPECIFIC
            }
            else -> {
                ""
            }
        }
    }
}