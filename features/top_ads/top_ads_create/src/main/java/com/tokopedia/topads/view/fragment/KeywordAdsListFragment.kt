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
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.UNKNOWN_SEARCH
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.view.adapter.keyword.KeywordSearchAdapter
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import com.tokopedia.topads.view.sheet.KeyTipsSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list.*
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list_empty_tip.view.*
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
private const val EVENT_CLICK_LAJUKTAN = "kata kunci pilihan dari rekomendasi"
private const val CLICK_ON_SEARCH = "click - tambah kata kunci manual"
private const val EVENT_CLICK_ON_SEARCH = "kata kunci yang ditambahkan manual"


class KeywordAdsListFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter
    private lateinit var keywordSearchAdapter: KeywordSearchAdapter
    private var userID: String = ""
    private var shopID = ""
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null
    private var stepModel: CreateManualAdsStepperModel? = null

    companion object {

        fun createInstance(extras: Bundle?): Fragment {
            val fragment = KeywordAdsListFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProvider(this, viewModelFactory).get(KeywordAdsViewModel::class.java)
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        keywordListAdapter = KeywordListAdapter(::onKeywordSelected)
        keywordSearchAdapter = KeywordSearchAdapter(::onCheckedItem)
        getModel()
    }

    private fun onCheckedItem(pos: Int, isChecked: Boolean) {
        if (isChecked)
            addToMainList(pos)
        else
            removeFromMainList(pos)
        showSelectMessage()

    }

    private fun removeFromMainList(pos: Int) {
        keywordListAdapter.items.remove(keywordSearchAdapter.items[pos])
        keywordListAdapter.notifyDataSetChanged()
    }

    private fun addToMainList(pos: Int) {
        keywordListAdapter.items.add(0, keywordSearchAdapter.items[pos])
        keywordListAdapter.notifyItemChanged(0)
    }

    private fun getModel() {
        stepModel = arguments?.getParcelable("model")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        keywordListAdapter.items.clear()
        viewModel.getSuggestionKeyword(getProductIds(), 0, this::onSuccessSuggestion, this::onEmptySuggestion)
    }

    private fun removeFromRecommended() {
        val iterator = keywordListAdapter.items.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            if (stepModel?.selectedKeywordStage?.find { item ->
                        key.keyword == item.keyword
                    } != null) {
                iterator.remove()
            }
        }
        keywordListAdapter.notifyDataSetChanged()
    }

    private fun onKeywordSelected(pos: Int) {
        if (pos != -1) {
            val eventLabel = if (keywordListAdapter.items[pos].onChecked) {
                "$SELCTED - $shopID - $EVENT_LIST_CHECKBOX"
            } else {
                "$UNSELECT - $shopID - $EVENT_LIST_CHECKBOX"
            }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_CHECKBOX, eventLabel, userID)
            showSelectMessage()
        }
    }

    private fun showSelectMessage() {
        val count = keywordListAdapter.getSelectedItems().count()
        selected_info?.text = MethodChecker.fromHtml(String.format(getString(R.string.topads_common_kata_kunci_lihat), count))
        btn_next?.isEnabled = count < 50
    }

    private fun onSuccessSuggestion(keywords: List<KeywordData>) {
        startLoading(false)
        keywords.forEach { key ->
            key.keywordData.forEach {
                keywordListAdapter.items.add(it)
            }
        }
        getAlreadySelected()
        showSelectMessage()
    }

    private fun getAlreadySelected() {
        removeFromRecommended()
        stepModel?.selectedKeywordStage?.let {
            keywordListAdapter.items.addAll(0, it)
        }
        keywordListAdapter.setSelectedItem(stepModel?.selectedKeywordStage)
        sortFinalList()
    }

    private fun onEmptySuggestion() {
        startLoading(false)
        setEmptyView()
        showSelectMessage()
    }

    private fun gotoNextPage() {
        stepModel?.selectedKeywordStage = keywordListAdapter.getSelectedItems().toMutableList()
        val eventLabel = "$shopID - $EVENT_CLICK_LAJUKTAN"
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_PILIH_KEYWORD, eventLabel, userID)
        val intent = Intent()
        intent.putExtra("model", stepModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
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
        shopID = UserSession(view.context).shopId
        setAdapters()
        startLoading(true)
        setTipLayout()
        setEmptyLayout()
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        selected_info?.setOnClickListener {
            goToFinalStage()
        }
        setSearchAction()
    }

    private fun setSearchAction() {
        view?.let {
            val searchBar = it.findViewById<SearchBarUnify>(R.id.searchBar)
            searchBar?.searchBarTextField?.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    val eventLabel = "$shopID - $EVENT_CLICK_ON_SEARCH"
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_ON_SEARCH, eventLabel, userID)
                }
            }
            Utils.setSearchListener(searchBar, context, it, ::fetchData)
        }
    }

    private fun setEmptyLayout() {
        emptyLayout.ic_tip.setImageDrawable(view?.context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_bulp_fill))
        emptyLayout.imageView2.setImageDrawable(view?.context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
        emptyLayout.imageView3.setImageDrawable(view?.context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
        emptyLayout.imageView4.setImageDrawable(view?.context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
    }

    private fun setTipLayout() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(com.tokopedia.topads.common.R.string.topads_empty_tip_memilih_kata_kunci_title)
            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view?.context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }
        tip_btn?.addItem(tooltipView)
        tip_btn.setOnClickListener {
            KeyTipsSheet().show(childFragmentManager, KeywordAdsListFragment::class.java.name)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_KEYWORD, shopID, userID)
        }
    }

    private fun goToFinalStage() {
        keyword_list?.visible()
        searchList?.gone()
        sortFinalList()
    }

    private fun sortFinalList() {
        keywordListAdapter.items.sortWith(Comparator
        { lhs, rhs ->
            (lhs.onChecked.compareTo(rhs.onChecked))
        })
        keywordListAdapter.items.reverse()
        keywordListAdapter.notifyDataSetChanged()
    }

    private fun checkIfNeedsManualAddition(listKeywords: MutableList<String>) {
        if (listKeywords.find { key -> searchBar?.searchBarTextField?.text.toString() == key } == null) {
            manualAd?.visible()
            manualAdTxt?.visible()
            dividerManual?.visible()
            manualAdTxt.text = MethodChecker.fromHtml(String.format(getString(R.string.topads_common_new_manual_key), searchBar?.searchBarTextField?.text.toString()))
            manualAd?.setOnClickListener {
                addManualKeyword()
                searchBar?.searchBarTextField?.text?.clear()
            }
        }
    }

    private fun addManualKeyword() {
        headlineList.visible()
        manualAd.gone()
        manualAdTxt?.gone()
        dividerManual?.gone()
        val item = KeywordDataItem()
        item.keyword = searchBar.searchBarTextField.text.toString()
        item.onChecked = true
        item.totalSearch = UNKNOWN_SEARCH
        keywordSearchAdapter.items.add(0, item)
        keywordSearchAdapter.notifyItemInserted(0)
        addToMainList(0)
        showSelectMessage()
        setEmpty(false)
    }

    private fun startLoading(isLoading: Boolean, search: Boolean = false) {
        if (isLoading) {
            loading.visible()
            if (search) {
                searchLoading?.visible()
            }
        } else {
            loading.gone()
            searchLoading?.gone()
            headlineList.visibility = View.VISIBLE
        }
    }

    private fun setAdapters() {
        keyword_list?.adapter = keywordListAdapter
        keyword_list?.layoutManager = LinearLayoutManager(context)
        searchList?.adapter = keywordSearchAdapter
        searchList?.layoutManager = LinearLayoutManager(context)
    }

    private fun getProductIds(): String {
        val ids: MutableList<String> = mutableListOf()
        stepModel?.selectedProductIds?.forEach {
            ids.add(it)
        }
        return ids.joinToString(",")
    }

    private fun fetchData() {
        setSearchLayout()
        if (searchBar.searchBarTextField.text.toString().isNotEmpty()) {
            viewModel.searchKeyword(searchBar.searchBarTextField.text.toString(), getProductIds(), ::showSearchResult)
        }
    }

    private fun setSearchLayout() {
        startLoading(true, search = true)
        keyword_list?.gone()
        searchList?.visible()
    }

    private fun showSearchResult(data: List<SearchData>) {
        keywordSearchAdapter.items.clear()
        startLoading(false)
        val listKeywords: MutableList<String> = mutableListOf()
        if (searchBar.searchBarTextField.text.toString().isNotEmpty() && data.isNotEmpty()) {
            val list: MutableList<KeywordDataItem> = mutableListOf()
            data.forEach { item ->
                listKeywords.add(item.keyword ?: "")
                list.add(KeywordDataItem(item.bidSuggest, item.totalSearch.toString(), item.keyword
                        ?: "", item.source ?: "", item.competition
                        ?: "", item.onChecked, fromSearch = true))
            }
            keywordSearchAdapter.setSearchList(list)
        } else {
            setEmpty(data.isEmpty())
        }
        checkIfNeedsManualAddition(listKeywords)
    }

    private fun setEmpty(setEmpty: Boolean) {
        if (setEmpty) {
            emptyImage?.visible()
            title_empty?.visible()
            desc_empty?.visible()
            headlineList?.gone()
        } else {
            emptyImage?.gone()
            title_empty?.gone()
            desc_empty?.gone()
            headlineList?.visible()
            emptyLayout?.gone()
        }
    }


    private fun setEmptyView() {
        headlineList?.gone()
        emptyLayout?.visible()
    }
}