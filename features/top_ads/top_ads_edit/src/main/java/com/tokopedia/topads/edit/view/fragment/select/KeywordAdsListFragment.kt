package com.tokopedia.topads.edit.view.fragment.select

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
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.view.sheet.TipSheetKeywordList
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.COUNT
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.ORIGINAL_LIST
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID
import com.tokopedia.topads.edit.utils.Constants.SELECTED_DATA
import com.tokopedia.topads.edit.view.activity.KeywordSearchActivity
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordSelectedAdapter
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModel
import kotlinx.android.synthetic.main.topads_edit_select_layout_keyword_list.*
import java.util.*
import javax.inject.Inject
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class KeywordAdsListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter
    private lateinit var keywordSelectedAdapter: KeywordSelectedAdapter
    private var STAGE = 0
    private var selectedKeyFromSearch: ArrayList<SearchData>? = arrayListOf()

    private val keywordList = HashSet<String>()
    var productId = ""
    private var originalList: ArrayList<String> = arrayListOf()
    private var selected: ArrayList<KeywordDataItem>? = arrayListOf()


    companion object {
        const val PRODUCT_IDS_SELECTED = "product_ids"
        const val SEARCH_QUERY = "search"
        const val SELECTED_KEYWORDS = "selected_key"
        const val REQUEST_CODE_SEARCH = 47
        fun createInstance(extras: Bundle?): Fragment {
            val fragment = KeywordAdsListFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(KeywordAdsViewModel::class.java)

            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        keywordListAdapter = KeywordListAdapter.createInstance(KeywordListAdapterTypeFactoryImpl(this::onKeywordSelected))
        keywordSelectedAdapter = KeywordSelectedAdapter(::onItemUnchecked)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val productIds = arguments?.getString(PRODUCT_ID) ?: ""
        val groupId = arguments?.getInt(GROUP_ID)
        originalList = arguments?.getStringArrayList(ORIGINAL_LIST)!!
        viewModel.getSuggestionKeyword(productIds, groupId, this::onSuccessSuggestion)
    }

    private fun onKeywordSelected(pos: Int) {
        if (pos != -1 && keywordListAdapter.items[pos] is KeywordItemViewModel && STAGE == 1) {
            keywordSelectedAdapter.items.add((keywordListAdapter.items[pos] as KeywordItemViewModel).data)
            sortListSelected()
            keywordListAdapter.items.removeAt(pos)
            keywordListAdapter.notifyItemRemoved(pos)
        }
        showSelectMessage()
    }

    private fun sortListSelected() {
        keywordSelectedAdapter.items.sortWith(Comparator { lhs, rhs -> lhs?.totalSearch?.toInt()!!.compareTo(rhs?.totalSearch?.toInt()!!) })
        keywordSelectedAdapter.items.reverse()
        keywordSelectedAdapter.notifyDataSetChanged()
    }

    private fun sortList() {
        keywordListAdapter.items.sortWith(Comparator
        { lhs, rhs -> (lhs as KeywordItemViewModel).data.totalSearch.toInt().compareTo((rhs as KeywordItemViewModel).data.totalSearch.toInt()) })
        keywordListAdapter.items.reverse()
        keywordListAdapter.notifyDataSetChanged()
    }


    private fun onItemUnchecked(pos: Int) {
        keywordListAdapter.items.add(KeywordItemViewModel(KeywordDataItem(keywordSelectedAdapter.items[pos].bidSuggest, keywordSelectedAdapter.items[pos].totalSearch, keywordSelectedAdapter.items[pos].keyword, keywordSelectedAdapter.items[pos].competition, keywordSelectedAdapter.items[pos].source)))
        keywordSelectedAdapter.items.removeAt(pos)
        keywordSelectedAdapter.notifyItemRemoved(pos)
        sortList()
        showSelectMessage()
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

    private fun showSelectMessage() {
        val count = if (STAGE == 0)
            getTotalChosenKeywords().size
        else
            keywordSelectedAdapter.itemCount
        selected_info.text = String.format(getString(R.string.format_selected_keyword), count)
        btn_next.isEnabled = count <= COUNT
    }

    private fun onSuccessSuggestion(keywords: List<KeywordData>) {
        keywords.forEach { key ->
            key.keywordData.forEach {
                keywordListAdapter.items.add(KeywordItemViewModel(it))
                keywordList.add(KeywordItemViewModel(it).data.keyword)
            }
        }
        keywordListAdapter.notifyDataSetChanged()
        showSelectMessage()
    }

    override fun getScreenName(): String {
        return KeywordAdsListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_edit_select_layout_keyword_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selected = arguments?.getParcelableArrayList(SELECTED_DATA)
        if (selected?.isNotEmpty()!!) {
            restoreStage()
        } else {
            setStepLayout(View.GONE)
        }
        btn_next.setOnClickListener {
            if (btn_next.text == resources.getString(R.string.topads_common_keyword_list_step)) {
                gotoNextStage()
                STAGE = 1
            } else {
                val returnIntent = Intent()
                returnIntent.putParcelableArrayListExtra(SELECTED_DATA, ArrayList(keywordSelectedAdapter.items))
                activity?.setResult(Activity.RESULT_OK, returnIntent)
                activity?.finish()
            }
        }
        tip_btn.setOnClickListener {
            TipSheetKeywordList().show(fragmentManager!!, KeywordAdsListFragment::class.java.name)
        }
        Constants.setSearchListener(context, view, ::fetchData)
        selectedKeyList?.adapter = keywordSelectedAdapter
        selectedKeyList?.isNestedScrollingEnabled = false
        keyword_list?.adapter = keywordListAdapter
        keyword_list?.layoutManager = LinearLayoutManager(context)
        selectedKeyList?.layoutManager = LinearLayoutManager(context)

    }

    private fun restoreStage() {
        setStepLayout(View.VISIBLE)
        btn_next?.text = resources.getString(R.string.lanjutkan)
        keywordSelectedAdapter.items.clear()
        selected?.forEach { item ->
            if ((originalList.find { item.keyword == it } == null)) {
                keywordSelectedAdapter.items.add(item)
            }
        }
        keywordSelectedAdapter.notifyDataSetChanged()
        removeFromRecommended()
        showSelectMessage()
    }

    private fun removeFromRecommended() {
        val ids: MutableList<Int> = mutableListOf()
        keywordListAdapter.items.forEachIndexed { index, key ->
            if (selected?.find { item -> (key as KeywordItemViewModel).data.keyword == item.keyword } != null) {
                ids.add(index)
            }
        }
        ids.forEach {
            keywordListAdapter.items.removeAt(it)
        }

        keywordListAdapter.notifyDataSetChanged()
    }

    private fun gotoNextStage() {
        setStepLayout(View.VISIBLE)
        btn_next?.text = resources.getString(R.string.lanjutkan)
        keywordSelectedAdapter.items.clear()
        keywordSelectedAdapter.items.addAll(getTotalChosenKeywords())
        sortListSelected()
        keywordListAdapter.items.removeAll(keywordListAdapter.getSelectedItems())
        keywordListAdapter.notifyDataSetChanged()
        STAGE = 1
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
        for (item in selectedKeyFromSearch!!) {
            if (keywordSelectedAdapter.items.find { selected -> selected.keyword == item.keyword } == null) {
                list.add(KeywordDataItem(item.bidSuggest, item.totalSearch.toString(), item.keyword
                        ?: "", item.competition ?: "", item.source ?: ""))
            }
        }
        return list
    }

    private fun setStepLayout(visible: Int) {
        txtSelected?.visibility = visible
        separate_select?.visibility = visible
        txtRecommended?.visibility = visible
    }

    private fun fetchData() {
        val intent = Intent(context, KeywordSearchActivity::class.java).apply {
            putExtra(PRODUCT_IDS_SELECTED, arguments?.getString(PRODUCT_ID) ?: "")
            putExtra(SEARCH_QUERY, searchBar.searchBarTextField.text.toString())
        }
        startActivityForResult(intent, REQUEST_CODE_SEARCH)
    }

    private fun makeToast(s: String) {
        SnackbarManager.make(activity, s,
                Snackbar.LENGTH_LONG)
                .show()
    }

}