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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.di.HeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.EditTopAdsHeadlineKeywordActivity
import com.tokopedia.top_ads_headline.view.viewmodel.HeadlineEditKeywordViewModel
import com.tokopedia.top_ads_headline.view.viewmodel.SharedEditHeadlineViewModel
import com.tokopedia.topads.common.constant.Constants.KEYWORD_TYPE_EXACT
import com.tokopedia.topads.common.constant.Constants.KEYWORD_TYPE_NEGATIVE_EXACT
import com.tokopedia.topads.common.constant.Constants.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.topads.common.constant.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.topads.common.constant.Constants.TITLE_1
import com.tokopedia.topads.common.constant.Constants.TITLE_2
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
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.CURRENTLIST
import com.tokopedia.topads.edit.utils.Constants.RESTORED_DATA
import com.tokopedia.topads.edit.view.activity.SelectNegKeywordActivity
import com.tokopedia.unifycomponents.UnifyButton
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
    private var tipsSortListSheet: TipsListSheet? = null
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
    private var restoreNegativeKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var selectedKeywordsList: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
    private var deletedKeywords: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
    private var addedKeywords: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
    private var editedKeywords: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()

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
        setUpSearchTypeTipsSheet()
        add_keyword.setOnClickListener {
            if (keywordType == KEYWORD_POSITIVE) {
                openPositiveAdKeywordActivity()
            } else {
                openNegativeAdKeywordActivity()
            }
        }
    }

    private fun openNegativeAdKeywordActivity() {
        val intent = Intent(context, SelectNegKeywordActivity::class.java)
        intent.putParcelableArrayListExtra(RESTORED_DATA, restoreNegativeKeywords)
        intent.putStringArrayListExtra(CURRENTLIST, getCurrentItems())
        startActivityForResult(intent, NEGATIVE_KEYWORD_REQUEST_CODE)
    }

    private fun openPositiveAdKeywordActivity() {
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
            stepperModel?.selectedKeywords = getSelectedKeywords(selectedKeywordsList)
        })
        sharedEditHeadlineViewModel?.getBidInfoData()?.observe(viewLifecycleOwner, Observer {
            stepperModel?.minBid = it.minBid
            stepperModel?.maxBid = it.maxBid
            stepperModel?.dailyBudget = it.minDailyBudget.toFloat()
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
        viewModel.getAdKeyword(userSession.shopId, groupId, cursor, this::onSuccessKeyword)
    }

    private fun setUpKeywords() {
        val keywordUiModels: ArrayList<KeywordUiModel> = getKeywordUiModels(selectedKeywordsList)
        adapter.setKeywordItems(keywordUiModels)
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>, cursor: String) {
        selectedKeywordsList = data as ArrayList<GetKeywordResponse.KeywordsItem>
        stepperModel?.selectedKeywords = getSelectedKeywords(data)
        this.cursor = cursor
    }

    private fun getKeywordUiModels(data: List<GetKeywordResponse.KeywordsItem>): java.util.ArrayList<KeywordUiModel> {
        val keywordUiModels = ArrayList<KeywordUiModel>()
        data.forEach { result ->
            if (result.status != -1) {
                val keywordSubType: String = getKeywordSubType(result.type)
                if (keywordType == KEYWORD_POSITIVE && (result.type == KEYWORD_TYPE_PHRASE || result.type == KEYWORD_TYPE_EXACT)) {
                    keywordUiModels.add(HeadlineEditAdKeywordModel(result.tag, keywordSubType,
                            advertisingCost = Utils.convertToCurrency(result.priceBid.toLong()), priceBid = result.priceBid,
                            maximumBid = stepperModel?.maxBid
                                    ?: 0, minimumBid = stepperModel?.minBid ?: 0))
                } else if (keywordType == KEYWORD_NEGATIVE &&
                        (result.type == KEYWORD_TYPE_NEGATIVE_PHRASE || result.type == KEYWORD_TYPE_NEGATIVE_EXACT)) {
                    keywordUiModels.add(HeadlineEditAdKeywordModel(result.tag, keywordSubType,
                            advertisingCost = Utils.convertToCurrency(result.priceBid.toLong()), priceBid = result.priceBid, isNegativeKeyword = true,
                            maximumBid = stepperModel?.maxBid
                                    ?: 0, minimumBid = stepperModel?.minBid ?: 0))
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

    private fun getKeywordSubType(type: Int): String {
        return if (type == KEYWORD_TYPE_PHRASE || type == KEYWORD_TYPE_NEGATIVE_PHRASE) {
            TITLE_1
        } else {
            TITLE_2
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
        dialog.setPrimaryCTAText(getString(com.tokopedia.topads.edit.R.string.topads_edit_batal))
        dialog.setSecondaryCTAText(getString(com.tokopedia.topads.edit.R.string.topads_edit_ya))
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
        var deletedItem: GetKeywordResponse.KeywordsItem? = null
        selectedKeywordsList.forEach {
            if (it.tag == keywordModel.keywordName) {
                deletedKeywords.add(it)
                deletedItem = it
            }
        }
        selectedKeywordsList.remove(deletedItem)
        adapter.removeItem(keywordModel)
        adapter.notifyDataSetChanged()
    }

    override fun onSearchTypeClick(keywordModel: HeadlineEditAdKeywordModel) {
        tipsSortListSheet?.setOnUiSortItemClickListener(sortItemClick = object : TipsUiSortViewHolder.OnUiSortItemClick {
            override fun onItemClick(sortModel: TipsUiSortModel) {
                tipsSortListSheet?.getTipsList()?.forEach { model ->
                    if (model is TipsUiSortModel && model == sortModel) {
                        model.isChecked = true
                        changeSearchStatus(keywordModel, getString(model.headerText))
                    } else {
                        (model as? TipsUiSortModel)?.isChecked = false
                    }
                }
                tipsSortListSheet?.dismissAllowingStateLoss()
            }
        })
        tipsSortListSheet?.show(childFragmentManager, "")
    }

    override fun onEditPriceBid(isEnabled: Boolean, keywordModel: HeadlineEditAdKeywordModel) {
        activity?.findViewById<UnifyButton>(R.id.btn_submit)?.isEnabled = isEnabled
        if (isEnabled) {
            selectedKeywordsList.forEach {
                if (it.tag == keywordModel.keywordName) {
                    it.priceBid = keywordModel.priceBid
                    val editedKeyword = editedKeywords.find { editedKeyword -> editedKeyword.tag == keywordModel.keywordName }
                    if (editedKeyword != null) {
                        editedKeyword.priceBid = keywordModel.priceBid
                    } else {
                        editedKeywords.add(it)
                    }
                }
            }
        }
    }

    private fun setUpSearchTypeTipsSheet() {
        val tipsList: ArrayList<TipsUiModel> = ArrayList()
        tipsList.apply {
            add(TipsUiSortModel(R.string.topads_headline_broad_sort_type_header, R.string.topads_headline_broad_sort_type_subheader, true))
            add(TipsUiSortModel(R.string.topads_headline_specific_sort_type_header, R.string.topads_headline_specific_sort_type_subheader))
        }
        tipsSortListSheet = context?.let { it1 ->
            TipsListSheet.newInstance(it1, tipsList = tipsList)
        }
        tipsSortListSheet?.setTitle(getString(R.string.topads_headline_sort_type_title))
        tipsSortListSheet?.showHeader = true
        tipsSortListSheet?.showKnob = false
    }

    private fun changeSearchStatus(keywordModel: HeadlineEditAdKeywordModel, searchType: String) {
        if (searchType.equals(TITLE_1, false)) {
            selectedKeywordsList.forEach {
                if (it.tag == keywordModel.keywordName) {
                    if (keywordType == KEYWORD_POSITIVE) {
                        it.type = KEYWORD_TYPE_PHRASE
                    } else {
                        it.type = KEYWORD_TYPE_NEGATIVE_PHRASE
                    }
                    editedKeywords.add(it)
                }
            }
        } else {
            selectedKeywordsList.forEach {
                if (it.tag == keywordModel.keywordName) {
                    if (keywordType == KEYWORD_POSITIVE) {
                        it.type = KEYWORD_TYPE_EXACT
                    } else {
                        it.type = KEYWORD_TYPE_NEGATIVE_EXACT
                    }
                }
                editedKeywords.add(it)
            }
        }
        keywordModel.searchType = searchType
        val position = adapter.getItems().indexOf(keywordModel)
        if (position != -1) {
            adapter.notifyItemChanged(position)
        }
    }

    override fun onCtaBtnClick() {
        openNegativeAdKeywordActivity()
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
        data?.getParcelableArrayListExtra<KeywordDataItem>(ADDED_KEYWORDS)?.let {
            it.forEach { data ->
                addedKeywords.add(GetKeywordResponse.KeywordsItem(tag = data.keyword, priceBid = data.bidSuggest))
            }
        }
        data?.getParcelableArrayListExtra<KeywordDataItem>(EDITED_KEYWORDS)?.let { it ->
            it.forEach { data ->
                selectedKeywordsList.forEach { selectedKeyword ->
                    if (selectedKeyword.tag == data.keyword) {
                        selectedKeyword.priceBid = data.bidSuggest
                        editedKeywords.add(selectedKeyword)
                    }
                }
            }
        }
        data?.getStringArrayExtra(DELETED_KEYWORDS)?.let {
            it.forEach { keyword ->
                selectedKeywordsList.forEach { selectedKeyword ->
                    if (keyword == selectedKeyword.tag) {
                        deletedKeywords.add(selectedKeyword)
                    }
                }
            }
        }
        data?.getParcelableExtra<HeadlineAdStepperModel>(BaseStepperActivity.STEPPER_MODEL_EXTRA)?.let {
            stepperModel = it
            val tempSelectedKeywordList = ArrayList<GetKeywordResponse.KeywordsItem>()
            it.selectedKeywords.forEach { data ->
                for(selectedKeyword in selectedKeywordsList){
                    if(data.keyword == selectedKeyword.tag){
                        selectedKeyword.priceBid = data.bidSuggest
                        tempSelectedKeywordList.add(selectedKeyword)
                        break
                    }
                }
                addedKeywords.forEach { addedKeyword ->
                    if (addedKeyword.tag == data.keyword) {
                        tempSelectedKeywordList.add(addedKeyword)
                    }
                }
            }
            selectedKeywordsList.removeAll(tempSelectedKeywordList)
            selectedKeywordsList.forEach { selectedKeyword ->
                if(!deletedKeywords.contains(selectedKeyword)){
                    deletedKeywords.add(selectedKeyword)
                }
            }
            selectedKeywordsList.clear()
            selectedKeywordsList.addAll(tempSelectedKeywordList)
            addKeywords(selectedKeywordsList)
        }
    }

    private fun getDataForNegativeKeywordActivity(data: Intent?) {
        val selected = data?.getParcelableArrayListExtra<GetKeywordResponse.KeywordsItem>(Constants.SELECTED_KEYWORD)
        restoreNegativeKeywords = data?.getParcelableArrayListExtra(RESTORED_DATA)
        if (selected?.size != 0) {
            selected?.let { addKeywords(it) }
            selected?.forEach {
                if (!selectedKeywordsList.contains(it)) {
                    addedKeywords.add(it)
                }
            }
        }
        selectedKeywordsList = selected as ArrayList<GetKeywordResponse.KeywordsItem>
    }

    private fun addKeywords(list: java.util.ArrayList<GetKeywordResponse.KeywordsItem>) {
        adapter.clearAllItems()
        val keywordUiModels = getKeywordUiModels(list)
        adapter.setKeywordItems(keywordUiModels)
    }
}