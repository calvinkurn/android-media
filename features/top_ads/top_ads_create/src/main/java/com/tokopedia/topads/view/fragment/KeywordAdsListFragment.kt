package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.sheet.TipSheetKeywordList
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.KeywordData
import com.tokopedia.topads.data.response.KeywordDataItem
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.KeywordSearchActivity
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordSelectedAdapter
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_PILIH_KEYWORD = "click - lanjutkan pilih kata kunci rekomendasi"
private const val CLICK_TIPS_KEYWORD = "click - info tips kata kunci"
private const val CLICK_CHECKBOX = "click - ceklist rekomendasi kata kunci"
private const val EVENT_LIST_CHECKBOX = "kata kunci pilihan yang di ceklist"
private const val SELCTED = "select"
private const val UNSELECT = "unselect"
private const val GROUPID = "0"
private const val EVENT_CLICK_LAJUKTAN = "kata kunci pilihan dari rekomendasi"
private const val CLICK_ON_SEARCH = "click - tambah kata kunci manual"
private const val EVENT_CLICK_ON_SEARCH = "kata kunci yang ditambahkan manual"


class KeywordAdsListFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter
    private lateinit var keywordSelectedAdapter: KeywordSelectedAdapter
    private var STAGE = 0
    private var selectedKeyFromSearch: ArrayList<SearchData>? = arrayListOf()
    private var userID: String = ""

    companion object {

        private const val COACH_MARK_TAG = "keyword"
        const val PRODUCT_IDS_SELECTED = "product_ids"
        const val SEARCH_QUERY = "search"
        const val SELECTED_KEYWORDS = "selected_key"
        const val REQUEST_CODE_SEARCH = 47
        fun createInstance(): Fragment {

            val fragment = KeywordAdsListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(KeywordAdsViewModel::class.java)
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        keywordListAdapter = KeywordListAdapter(KeywordListAdapterTypeFactoryImpl(this::onKeywordSelected))
        keywordSelectedAdapter = KeywordSelectedAdapter(::onItemUnchecked)

    }

    private fun onItemUnchecked(pos: Int) {
        if (!keywordSelectedAdapter.items[pos].fromSearch) {
            keywordListAdapter.items.add(KeywordItemViewModel(
                    KeywordDataItem(keywordSelectedAdapter.items[pos].bidSuggest, keywordSelectedAdapter.items[pos].totalSearch,
                            keywordSelectedAdapter.items[pos].keyword, keywordSelectedAdapter.items[pos].competition,
                            keywordSelectedAdapter.items[pos].source)))

        } else {
            removeSearchedItem(pos)
        }
        keywordSelectedAdapter.items.removeAt(pos)
        keywordSelectedAdapter.notifyItemRemoved(pos)
        if (keywordSelectedAdapter.items.isEmpty()) {
            setStepLayout(View.GONE)
            STAGE = 0
            setBtnText()
        }
        sortList()
        showSelectMessage()
    }

    private fun removeSearchedItem(pos: Int) {
        var id = -1
        selectedKeyFromSearch?.forEachIndexed { index, it ->
            if (it.keyword == keywordSelectedAdapter.items[pos].keyword) {
                id = index
            }
        }
        if (id != -1 && selectedKeyFromSearch?.size ?: 0 > id)
            selectedKeyFromSearch?.removeAt(id)
    }

    private fun startShowCase() {
        val coachMark = CoachMarkBuilder().build()
        if (!coachMark.hasShown(activity, COACH_MARK_TAG)) {
            val coachItems = ArrayList<CoachMarkItem>()
            coachItems.add(CoachMarkItem(searchBar, getString(R.string.coach_mark_title_1), getString(R.string.coach_mark_desc_1)))
            coachItems.add(CoachMarkItem(txtRecommended, getString(R.string.coach_mark_title_2), getString(R.string.coach_mark_desc_2)))
            coachMark.show(activity, COACH_MARK_TAG, coachItems)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        STAGE = stepperModel?.STAGE ?: 0
        val list = stepperModel?.selectedProductIds!!
        val productId = list.joinToString(",")
        keywordListAdapter.items.clear()
        viewModel.getSuggestionKeyword(productId, 0, this::onSuccessSuggestion, this::onEmptySuggestion)
    }

    private fun restorePrevState() {
        STAGE = 1
        setStepLayout(View.VISIBLE)
        setBtnText()
        keywordSelectedAdapter.items.clear()
        keywordSelectedAdapter.items.addAll(stepperModel?.selectedKeywordStage?.asIterable()!!)
        keywordSelectedAdapter.notifyDataSetChanged()
        removeFromRecommended()
    }

    private fun setBtnText() {
        if (STAGE == 0) {
            btn_next?.text = resources.getString(R.string.topads_common_keyword_list_step)
            txtRecommendation?.text = resources.getString(R.string.topads_common_recommended_list)
        } else {
            btn_next?.text = resources.getString(R.string.lanjutkan)
            txtRecommendation?.text = resources.getString(R.string.topads_common_label_top_ads_keyword)
        }
    }

    private fun removeFromRecommended() {
        val iterator = keywordListAdapter.items.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            if (stepperModel?.selectedKeywordStage?.find { item ->
                        (key as KeywordItemViewModel).data.keyword == item.keyword
                    } != null) {
                iterator.remove()
            }
        }
        keywordListAdapter.notifyDataSetChanged()
    }

    private fun onKeywordSelected(pos: Int) {
        if (pos != -1 && keywordListAdapter.items[pos] is KeywordItemViewModel) {
            val eventLabel = if ((keywordListAdapter.items[pos] as KeywordItemViewModel).isChecked) {
                "$SELCTED - $GROUPID - $EVENT_LIST_CHECKBOX"
            } else {
                "$UNSELECT - $GROUPID - $EVENT_LIST_CHECKBOX"
            }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_CHECKBOX, eventLabel, userID)

            if (STAGE == 1) {
                keywordSelectedAdapter.items.add((keywordListAdapter.items[pos] as KeywordItemViewModel).data)
                keywordListAdapter.items.removeAt(pos)
                keywordListAdapter.notifyItemRemoved(pos)
                sortListSelected()
            }
            showSelectMessage()

        }
    }

    private fun showSelectMessage() {
        val count = if (STAGE == 0)
            getTotalChosenKeywords().size
        else
            keywordSelectedAdapter.itemCount
        selected_info.text = String.format(getString(R.string.format_selected_keyword), count)
        btn_next.isEnabled = count < 50
    }

    private fun onSuccessSuggestion(keywords: List<KeywordData>) {
        startLoading(false)
        startShowCase()
        keywords.forEach { key ->
            key.keywordData.forEach { index ->
                keywordListAdapter.items.add(KeywordItemViewModel(index))
            }
        }
        tip_btn.visibility = View.VISIBLE
        headlineList.visibility = View.VISIBLE
        keywordListAdapter.notifyDataSetChanged()
        if (stepperModel?.STAGE == 1) {
            restorePrevState()
        }
        showSelectMessage()
    }

    private fun onEmptySuggestion() {
        STAGE = 1
        setBtnText()
        tip_btn.visibility = View.GONE
        headlineList.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        showSelectMessage()
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperModel?.selectedKeywords = getSelectedKeyword()
        stepperModel?.selectedSuggestBid = getSelectedBid()
        stepperModel?.STAGE = 1
        stepperModel?.selectedKeywordStage = keywordSelectedAdapter.items
        val eventLabel = "$GROUPID - $EVENT_CLICK_LAJUKTAN"
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_PILIH_KEYWORD, eventLabel, userID)
        stepperListener?.goToNextPage(stepperModel)

    }

    private fun getSelectedKeyword(): MutableList<String> {
        val list = mutableListOf<String>()
        keywordSelectedAdapter.items.forEach {
            list.add(it.keyword)
        }
        return list
    }

    private fun getSelectedBid(): MutableList<Int> {
        val list = mutableListOf<Int>()
        keywordSelectedAdapter.items.forEach {
            list.add(it.bidSuggest)
        }
        return list
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.topads_common_keyword_list_step))
    }

    override fun getScreenName(): String {
        return KeywordAdsListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_layout_keyword_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userID = UserSession(view.context).userId
        startLoading(true)
        setStepLayout(View.GONE)
        btn_next.setOnClickListener {
            if (btn_next.text == resources.getString(R.string.topads_common_keyword_list_step)) {
                gotoNextStage()
            } else
                gotoNextPage()
        }
        tip_btn.setOnClickListener {
            TipSheetKeywordList().show(fragmentManager!!, KeywordAdsListFragment::class.java.name)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_KEYWORD, GROUPID, userID)
        }
        setAdapters()
        val searchBar = view.findViewById<SearchBarUnify>(R.id.searchBar)
        searchBar?.searchBarTextField?.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                val eventLabel = "$GROUPID - $EVENT_CLICK_ON_SEARCH"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_ON_SEARCH, eventLabel, userID)
            }
        }
        Utils.setSearchListener(searchBar, context, view, ::fetchData)
    }

    private fun startLoading(isLoading: Boolean) {
        if (isLoading) {
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
            headlineList.visibility = View.VISIBLE
        }
    }

    private fun setAdapters() {
        keyword_list?.adapter = keywordListAdapter
        keyword_list?.layoutManager = LinearLayoutManager(context)
        selectedKeyList?.isNestedScrollingEnabled = false
        selectedKeyList?.adapter = keywordSelectedAdapter
        selectedKeyList?.layoutManager = LinearLayoutManager(context)
    }

    private fun gotoNextStage() {
        setStepLayout(View.VISIBLE)
        keywordSelectedAdapter.items.clear()
        keywordSelectedAdapter.items.addAll(getTotalChosenKeywords())
        sortListSelected()
        keywordListAdapter.items.removeAll(keywordListAdapter.getSelectedItems())
        keywordListAdapter.notifyDataSetChanged()
        STAGE = 1
        setBtnText()
        showSelectMessage()
    }

    private fun sortListSelected() {
        keywordSelectedAdapter.items.sortWith(Comparator
        { lhs, rhs -> lhs?.totalSearch?.toInt()!!.compareTo(rhs?.totalSearch?.toInt()!!) })
        keywordSelectedAdapter.items.reverse()
        keywordSelectedAdapter.notifyDataSetChanged()
    }

    private fun sortList() {
        keywordListAdapter.items.sortWith(Comparator
        { lhs, rhs ->
            (lhs as KeywordItemViewModel).data.totalSearch.toInt()
                    .compareTo((rhs as KeywordItemViewModel).data.totalSearch.toInt())
        })
        keywordListAdapter.items.reverse()
        keywordListAdapter.notifyDataSetChanged()
    }

    private fun setStepLayout(visible: Int) {
        txtSelected?.visibility = visible
        separate_select?.visibility = visible
        txtRecommended?.visibility = visible
    }

    private fun getProductIds(): String {
        val ids: MutableList<String> = mutableListOf()
        stepperModel?.selectedProductIds?.forEach {
            ids.add(it.toString())
        }
        return ids.joinToString(",")
    }

    private fun fetchData() {
        if (searchBar.searchBarTextField.text.toString().isNotEmpty()) {
            val intent = Intent(context, KeywordSearchActivity::class.java).apply {
                putExtra(PRODUCT_IDS_SELECTED, getProductIds())
                putExtra(SEARCH_QUERY, searchBar.searchBarTextField.text.toString())
            }
            startActivityForResult(intent, REQUEST_CODE_SEARCH)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SEARCH) {
            selectedKeyFromSearch?.clear()
            if (resultCode == Activity.RESULT_OK) {
                val dataFromSearch: ArrayList<SearchData>? = data?.getParcelableArrayListExtra(SELECTED_KEYWORDS)
                for (item in dataFromSearch!!) {
                    selectedKeyFromSearch?.add(item)
                }
                if (STAGE == 0)
                    gotoNextStage()
                else {
                    addSearchItems()
                }
            }
        }
    }

    private fun addSearchItems() {
        val list: MutableList<KeywordDataItem> = mapSearchDataToModel()
        if (list.isNotEmpty()) {
            keywordSelectedAdapter.items.addAll(list)
            sortListSelected()
        }
    }

    private fun getTotalChosenKeywords(): MutableList<KeywordDataItem> {
        val list: MutableList<KeywordDataItem> = mutableListOf()
        if (STAGE == 0) {
            keywordListAdapter.getSelectedItems().forEach {
                list.add(it.data)
            }
        } else {
            list.addAll(keywordSelectedAdapter.items)
        }
        list.addAll(mapSearchDataToModel())
        val distinctList = list.distinctBy { it.keyword }
        return distinctList.toMutableList()
    }

    private fun mapSearchDataToModel(): MutableList<KeywordDataItem> {
        val list: MutableList<KeywordDataItem> = mutableListOf()
        for (item in selectedKeyFromSearch!!) {
            if (keywordSelectedAdapter.items.find { selected -> selected.keyword == item.keyword } == null) {
                list.add(KeywordDataItem(item.bidSuggest, item.totalSearch.toString(), item.keyword
                        ?: "", item.competition ?: "", item.source ?: "", true, true))
            }
        }
        return list
    }

}