package com.tokopedia.topads.edit.view.fragment.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.Constants.KEYWORD_CHARACTER_COUNT
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.sheet.TipSheetKeywordList
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID
import com.tokopedia.topads.edit.utils.Constants.SELECTED_DATA
import com.tokopedia.topads.edit.view.activity.KeywordSearchActivity
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordSelectedAdapter
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

private const val EVENT_LIST_CHECKBOX = "kata kunci pilihan yang di ceklist"
private const val SELCTED = "select"
private const val CLICK_CHECKBOX = "click - ceklist rekomendasi kata kunci"
private const val UNSELECT = "unselect"
private const val CLICK_TIPS_KEYWORD = "click - info tips kata kunci"
private const val EVENT_CLICK_ON_SEARCH = "kata kunci yang ditambahkan manual"
private const val CLICK_ON_SEARCH = "click - tambah kata kunci manual"
private const val EVENT_CLICK_LAJUKTAN = "kata kunci pilihan dari rekomendasi"
private const val CLICK_PILIH_KEYWORD = "click - lanjutkan pilih kata kunci rekomendasi"
private const val CLICK_SEARCH_FIELD = "click - search box kata kunci"

class KeywordAdsListFragment : BaseDaggerFragment() {

    private var loading: LoaderUnify? = null
    private var searchBar: SearchBarUnify? = null
    private var txtSelected: Typography? = null
    private var headlineList: ConstraintLayout? = null
    private var txtRecommendation: Typography? = null
    private var selectedKeyList: RecyclerView? = null
    private var separateSelect: DividerUnify? = null
    private var txtRecommended: Typography? = null
    private var keywordList: RecyclerView? = null
    private var tipBtn: FloatingButtonUnify? = null
    private var btnNext: UnifyButton? = null
    private var selectedInfo: Typography? = null
    private var emptyLayout: CardView? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter
    private lateinit var keywordSelectedAdapter: KeywordSelectedAdapter
    private var STAGE = 0
    private var selectedKeyFromSearch: ArrayList<SearchData>? = arrayListOf()
    private var selected: ArrayList<KeywordDataItem>? = arrayListOf()
    var groupId = 0
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null

    companion object {
        const val PRODUCT_IDS_SELECTED = "product_ids"
        const val SEARCH_QUERY = "search"
        const val SELECTED_KEYWORDS = "selected_key"
        const val REQUEST_CODE_SEARCH = 47
        private var userID: String = ""
        fun createInstance(extras: Bundle?): Fragment {
            val fragment = KeywordAdsListFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            viewModel =
                ViewModelProviders.of(it, viewModelFactory).get(KeywordAdsViewModel::class.java)

            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        keywordListAdapter =
            KeywordListAdapter.createInstance(KeywordListAdapterTypeFactoryImpl(this::onKeywordSelected))
        keywordSelectedAdapter = KeywordSelectedAdapter(::onItemUnchecked)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val productIds = arguments?.getString(PRODUCT_ID) ?: ""
        groupId = arguments?.getInt(GROUP_ID) ?: 0
        if (productIds.isNotEmpty()) {
            viewModel.getSuggestionKeyword(productIds, groupId, this::onSuccessSuggestion)
        } else {
            setEmptyView()
            setEmptyLayout(true)
            selectedInfo?.text = String.format(getString(com.tokopedia.topads.common.R.string.format_selected_keyword), 0)
        }
    }

    private fun onKeywordSelected(pos: Int) {
        if (pos != -1 && keywordListAdapter.items[pos] is KeywordItemViewModel) {
            val eventLabel =
                if ((keywordListAdapter.items[pos] as KeywordItemViewModel).isChecked) {
                    "$SELCTED - $groupId - $EVENT_LIST_CHECKBOX"
                } else {
                    "$UNSELECT - $groupId - $EVENT_LIST_CHECKBOX"
                }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_CHECKBOX,
                eventLabel,
                userID)

            if (STAGE == 1) {
                keywordSelectedAdapter.items.add((keywordListAdapter.items[pos] as KeywordItemViewModel).data)
                sortListSelected()
                keywordListAdapter.items.removeAt(pos)
                keywordListAdapter.notifyItemRemoved(pos)
            }
        }
        showSelectMessage()
    }

    private fun sortListSelected() {
        keywordSelectedAdapter.items.sortWith { lhs, rhs ->
            (lhs?.totalSearch?.toLongOrZero() ?: 0).compareTo(rhs?.totalSearch?.toLongOrZero() ?: 0)
        }
        keywordSelectedAdapter.items.reverse()
        keywordSelectedAdapter.notifyDataSetChanged()
    }

    private fun sortList() {
        keywordListAdapter.items.sortWith { lhs, rhs ->
            ((lhs as? KeywordItemViewModel)?.data?.totalSearch?.toLongOrZero() ?: 0)
                .compareTo((rhs as? KeywordItemViewModel)?.data?.totalSearch?.toLongOrZero() ?: 0)
        }
        keywordListAdapter.items.reverse()
        keywordListAdapter.notifyDataSetChanged()
    }


    private fun onItemUnchecked(pos: Int) {
        if (!keywordSelectedAdapter.items[pos].fromSearch) {
            keywordListAdapter.items.add(KeywordItemViewModel(
                KeywordDataItem(keywordSelectedAdapter.items[pos].bidSuggest,
                    keywordSelectedAdapter.items[pos].totalSearch,
                    keywordSelectedAdapter.items[pos].keyword,
                    keywordSelectedAdapter.items[pos].source,
                    keywordSelectedAdapter.items[pos].competition)))

        } else {
            removeSearchedItem(pos)
        }
        keywordSelectedAdapter.items.removeAt(pos)
        keywordSelectedAdapter.notifyItemRemoved(pos)
        if (keywordSelectedAdapter.items.isEmpty()) {
            STAGE = 0
            setBtnText()
            setStepLayout(View.GONE)
        }
        sortList()
        showSelectMessage()
    }

    private fun removeSearchedItem(pos: Int) {
        val iterator = selectedKeyFromSearch?.iterator()
        while (iterator?.hasNext() == true) {
            val key = iterator.next()
            if (key.keyword == keywordSelectedAdapter.items[pos].keyword) {
                iterator.remove()
            }
        }
    }

    private fun setBtnText() {
        if (STAGE == 0) {
            btnNext?.text = resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_list_step)
            txtRecommendation?.text = resources.getString(com.tokopedia.topads.common.R.string.topads_common_recommended_list)
        } else {
            btnNext?.text = resources.getString(com.tokopedia.topads.common.R.string.lanjutkan)
            txtRecommendation?.text =
                resources.getString(com.tokopedia.topads.common.R.string.topads_common_label_top_ads_keyword)
        }
    }

    private fun setEmptyLayout(empty: Boolean) {
        if (empty) {
            tipBtn?.visibility = View.GONE
            headlineList?.visibility = View.GONE
            emptyLayout?.visibility = View.VISIBLE

        } else {
            tipBtn?.visibility = View.VISIBLE
            headlineList?.visibility = View.VISIBLE
            emptyLayout?.visibility = View.GONE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SEARCH) {
            selectedKeyFromSearch?.clear()
            if (resultCode == Activity.RESULT_OK) {
                val dataFromSearch: ArrayList<SearchData>? =
                    data?.getParcelableArrayListExtra(SELECTED_KEYWORDS)
                dataFromSearch?.forEach { item ->
                    selectedKeyFromSearch?.add(item)
                }
                if (selectedKeyFromSearch?.isNotEmpty() != false)
                    setEmptyLayout(false)
                checkifExist()
                if (STAGE == 0)
                    gotoNextStage()
                else {
                    addSearchItems()
                }
            }
        }
    }

    private fun checkifExist() {
        selectedKeyFromSearch?.forEach lit@{
            if (keywordSelectedAdapter.items.find { item -> item.keyword == it.keyword } != null) {
                view?.run {
                    Toaster.make(this,
                        getString(R.string.topads_edit_keyword_duplicate_toast),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_NORMAL,
                        getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)
                    ) {
                    }
                }
                return@lit
            }
        }
    }


    private fun addSearchItems() {
        val list: MutableList<KeywordDataItem> = mapSearchDataToModel()
        if (list.isNotEmpty()) {
            keywordSelectedAdapter.items.addAll(list)
            sortListSelected()
        }
        emptyLayout?.visibility = View.GONE
        showSelectMessage()
    }

    private fun showSelectMessage() {
        val count = if (STAGE == 0)
            getTotalChosenKeywords().size
        else
            keywordSelectedAdapter.itemCount
        selectedInfo?.text = String.format(getString(com.tokopedia.topads.common.R.string.format_selected_keyword), count)
        btnNext?.isEnabled = count <= KEYWORD_CHARACTER_COUNT
    }

    private fun onSuccessSuggestion(keywords: List<KeywordData>) {
        startLoading(false)
        keywords.forEach { key ->
            key.keywordData.forEach {
                keywordListAdapter.items.add(KeywordItemViewModel(it))
            }
        }
        tipBtn?.visibility = View.VISIBLE
        headlineList?.visibility = View.VISIBLE
        keywordListAdapter.notifyDataSetChanged()
        if (keywords.isEmpty()) {
            setEmptyView()
        }
        if (selected?.isNotEmpty() != false) {
            restoreStage()
        } else {
            setStepLayout(View.GONE)
        }
        showSelectMessage()
    }

    private fun setEmptyView() {
        startLoading(false)
        STAGE = 1
        if (selected?.isEmpty() != false && selectedKeyFromSearch?.isEmpty() != false) {
            setEmptyLayout(true)
        }
        setBtnText()
    }

    override fun getScreenName(): String {
        return KeywordAdsListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.topads_edit_select_layout_keyword_list, container, false)
        loading = view.findViewById(R.id.loading)
        searchBar = view.findViewById(R.id.searchBar)
        txtSelected = view.findViewById(R.id.txtSelected)
        headlineList = view.findViewById(R.id.headlineList)
        txtRecommendation = view.findViewById(R.id.txtRecommendation)
        selectedKeyList = view.findViewById(R.id.selectedKeyList)
        separateSelect = view.findViewById(R.id.separate_select)
        txtRecommended = view.findViewById(R.id.txtRecommended)
        keywordList = view.findViewById(R.id.keyword_list)
        tipBtn = view.findViewById(R.id.tip_btn)
        btnNext = view.findViewById(R.id.btn_next)
        selectedInfo = view.findViewById(R.id.selected_info)
        emptyLayout = view.findViewById(R.id.emptyLayout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userID = UserSession(view.context).userId
        startLoading(true)
        setStepLayout(View.GONE)
        selected = arguments?.getParcelableArrayList(SELECTED_DATA)
        btnNext?.setOnClickListener {
            if (btnNext?.text == resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_list_step)) {
                gotoNextStage()
                STAGE = 1
            } else {
                val eventLabel = "$groupId - $EVENT_CLICK_LAJUKTAN"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_PILIH_KEYWORD,
                    eventLabel,
                    userID)

                val returnIntent = Intent()
                returnIntent.putParcelableArrayListExtra(SELECTED_DATA,
                    ArrayList(keywordSelectedAdapter.items))
                activity?.setResult(Activity.RESULT_OK, returnIntent)
                activity?.finish()
            }
        }
        val tooltipView =
            layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null)
                .apply {
                    tvToolTipText = this.findViewById(com.tokopedia.topads.common.R.id.tooltip_text)
                    tvToolTipText?.text =
                        getString(com.tokopedia.topads.common.R.string.topads_common_tip_memilih_kata_kunci)

                    imgTooltipIcon = this.findViewById(com.tokopedia.topads.common.R.id.tooltip_icon)
                    imgTooltipIcon?.setImageDrawable(AppCompatResources.getDrawable(this.context,
                        com.tokopedia.topads.common.R.drawable.topads_ic_tips))
                }

        tipBtn?.addItem(tooltipView)
        tipBtn?.setOnClickListener {
            TipSheetKeywordList().show(childFragmentManager,
                KeywordAdsListFragment::class.java.name)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_TIPS_KEYWORD,
                groupId.toString(),
                userID)

        }
        val searchBar = view.findViewById<SearchBarUnify>(R.id.searchBar)
        searchBar?.searchBarTextField?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val eventLabel = "$groupId - $EVENT_CLICK_ON_SEARCH"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_ON_SEARCH,
                    eventLabel,
                    userID)
            }
        }
        setSearchAction()
        selectedKeyList?.adapter = keywordSelectedAdapter
        selectedKeyList?.isNestedScrollingEnabled = false
        keywordList?.adapter = keywordListAdapter
        keywordList?.layoutManager = LinearLayoutManager(context)
        selectedKeyList?.layoutManager = LinearLayoutManager(context)

    }

    private fun setSearchAction() {
        view?.let {
            val searchBar = it.findViewById<SearchBarUnify>(R.id.searchBar)
            searchBar?.searchBarTextField?.setOnClickListener {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_SEARCH_FIELD,
                    "")
            }
            Utils.setSearchListener(searchBar, context, it, ::fetchData)
        }
    }

    private fun startLoading(isLoading: Boolean) {
        if (isLoading) {
            loading?.visibility = View.VISIBLE
        } else {
            loading?.visibility = View.GONE
            headlineList?.visibility = View.VISIBLE
        }
    }

    private fun restoreStage() {
        setStepLayout(View.VISIBLE)
        STAGE = 1
        setBtnText()
        keywordSelectedAdapter.items.clear()
        selected?.forEach { item ->
            keywordSelectedAdapter.items.add(item)
        }
        keywordSelectedAdapter.notifyDataSetChanged()
        removeFromRecommended()
        showSelectMessage()
    }

    private fun removeFromRecommended() {
        val iterator = keywordListAdapter.items.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            if (selected?.find { item ->
                    (key as KeywordItemViewModel).data.keyword == item.keyword
                } != null) {
                iterator.remove()
            }
        }
        keywordListAdapter.notifyDataSetChanged()
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
        selectedKeyFromSearch?.forEach { item ->
            if (keywordSelectedAdapter.items.find { selected -> selected.keyword == item.keyword } == null) {
                list.add(KeywordDataItem(item.bidSuggest, item.totalSearch.toString(), item.keyword
                    ?: "", item.source ?: "", item.competition ?: "", true, fromSearch = true))
            }
        }
        return list
    }

    private fun setStepLayout(visible: Int) {
        txtSelected?.visibility = visible
        separateSelect?.visibility = visible
        txtRecommended?.visibility = visible
    }

    private fun fetchData() {
        if (searchBar?.searchBarTextField?.text.toString().isNotEmpty()) {
            val intent = Intent(context, KeywordSearchActivity::class.java).apply {
                putExtra(PRODUCT_IDS_SELECTED, arguments?.getString(PRODUCT_ID) ?: "")
                putExtra(SEARCH_QUERY, searchBar?.searchBarTextField?.text.toString())
                putExtra(GROUP_ID, groupId.toString())
            }
            startActivityForResult(intent, REQUEST_CODE_SEARCH)
        }
    }

}
