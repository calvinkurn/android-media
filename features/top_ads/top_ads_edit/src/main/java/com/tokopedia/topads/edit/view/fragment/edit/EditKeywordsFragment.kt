package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.data.response.ResponseBidInfo
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.FAVOURED_DATA
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_EXISTS
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_EXACT
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.MANUAL_DATA
import com.tokopedia.topads.edit.utils.Constants.MAX
import com.tokopedia.topads.edit.utils.Constants.MIN
import com.tokopedia.topads.edit.utils.Constants.MIN_SUGGESTION
import com.tokopedia.topads.edit.utils.Constants.ORIGINAL_LIST
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_EDIT
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID
import com.tokopedia.topads.edit.utils.Constants.REQUEST_OK
import com.tokopedia.topads.edit.utils.Constants.SELECTED_DATA
import com.tokopedia.topads.edit.view.activity.SelectKeywordActivity
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.topads_edit_keword_layout.*
import javax.inject.Inject

/**
 * Created by Pika on 12/4/20.
 */
class EditKeywordsFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: EditKeywordListAdapter
    private var bidMap = mutableMapOf<String, Int>()
    private lateinit var callBack: ButtonAction
    private var minSuggestKeyword = 0
    private var maxSuggestKeyword = 0
    private var deletedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var addedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var editedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var initialBudget: MutableList<Int> = mutableListOf()
    private var error: MutableList<Boolean> = mutableListOf()
    private var originalKeyList: MutableList<String> = arrayListOf()
    private var restoreData: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>? = arrayListOf()
    private var selectedData: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>? = arrayListOf()
    private var manualData: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>? = arrayListOf()

    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(EditFormDefaultViewModel::class.java)
    }
    private val sharedViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
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
        adapter = EditKeywordListAdapter(EditKeywordListAdapterTypeFactoryImpl(this::onDeleteKeyword, this::onAddKeyword, this::getMinMax, this::actionEnable,this::actionStatusChange))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_keword_layout), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val suggestions = java.util.ArrayList<DataSuggestions>()
        val dummyId: MutableList<Int> = mutableListOf()
        suggestions.add(DataSuggestions("group", dummyId))
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion)
    }

    private fun getMinMax(): MutableMap<String, Int> {
        bidMap[MIN] = minSuggestKeyword
        bidMap[MAX] = maxSuggestKeyword
        return bidMap
    }

    private fun actionEnable() {
        val enable = !adapter.isError()
        callBack.buttonDisable(enable)
    }

    private fun actionStatusChange(position:Int) {
        if(isExistsOriginal(position)) {
            deletedKeywords?.add((adapter.items[position] as EditKeywordItemViewModel).data)
            addedKeywords?.add((adapter.items[position] as EditKeywordItemViewModel).data)
        }
        else{
            addedKeywords?.forEach {
                if(it.tag == (adapter.items[position] as EditKeywordItemViewModel).data.tag)
                    it.type = (adapter.items[position] as EditKeywordItemViewModel).data.type
            }
        }
    }

    private fun onSuccessSuggestion(data: List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) {
        minSuggestKeyword = data[0].minBid
        maxSuggestKeyword = data[0].maxBid
    }

    private fun onDeleteKeyword(position: Int) {
        showConfirmationDialog(position)
    }

    private fun onAddKeyword() {
        val intent = Intent(context, SelectKeywordActivity::class.java)
        intent.putExtra(MIN_SUGGESTION, minSuggestKeyword)
        intent.putExtra(PRODUCT_ID, productIds)
        intent.putExtra(GROUP_ID, groupId)
        intent.putStringArrayListExtra(ORIGINAL_LIST, getCurrentList())
        intent.putParcelableArrayListExtra(FAVOURED_DATA, restoreData)
        intent.putParcelableArrayListExtra(SELECTED_DATA, selectedData)
        intent.putParcelableArrayListExtra(MANUAL_DATA, manualData)
        startActivityForResult(intent, 1)
    }

    private fun getCurrentList(): ArrayList<String>? {
        val list: ArrayList<String> = arrayListOf()
        adapter.items.forEach {
            if (it is EditKeywordItemViewModel) {
                list.add(it.data.tag)
            }
        }
        return list
    }

    private fun showConfirmationDialog(position: Int) {
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.topads_edit_delete_keyword_conf_dialog_title))
        dialog.setDescription(String.format(getString(R.string.topads_edit_delete_keyword_conf_dialog_desc),(adapter.items[position] as EditKeywordItemViewModel).data.tag))
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

    private fun deleteKeyword(position: Int) {
        var pos = 0
        if (selectedData?.isNotEmpty()!!) {
            selectedData?.forEachIndexed { index, select ->
                if (select.keyword == (adapter.items[position] as EditKeywordItemViewModel).data.tag) {
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
                        if (key.tag == (adapter.items[position] as EditKeywordItemViewModel).data.tag) {
                            index = it
                        }
                    }
                    addedKeywords?.removeAt(index)
                }
            }
        }
        adapter.items.removeAt(position)
        initialBudget.removeAt(position)
        error.removeAt(position)
        if (adapter.items.isEmpty()) {
            setEmptyView()
        }
        adapter.notifyDataSetChanged()
        updateItemCount()
    }

    private fun isExistsOriginal(position: Int): Boolean {
        return (originalKeyList.find { (adapter.items[position] as EditKeywordItemViewModel).data.tag == it } != null)
    }

    private fun isExistsOriginal(name: String): Boolean {
        return (originalKeyList.find { name == it } != null)
    }

    private fun updateItemCount() {
        keyword_count.text = String.format(getString(R.string.keyword_count), adapter.items.size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getProuductIds().observe(viewLifecycleOwner, Observer {
            productIds = it.joinToString(",")
        })
        sharedViewModel.getGroupId().observe(viewLifecycleOwner, Observer {
            groupId = it
            viewModel.getAdKeyword(groupId, this::onSuccessKeyword)
        })

        add_keyword.setOnClickListener {
            onAddKeyword()
        }
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(context)
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>) {
        if (data.isEmpty()) {
            setEmptyView()
        } else {
            val negKeyword: MutableList<GetKeywordResponse.KeywordsItem> = mutableListOf()
            data.forEach { result ->
                if ((result.type == KEYWORD_TYPE_EXACT || result.type == KEYWORD_TYPE_PHRASE) && result.status != -1) {
                    adapter.items.add(EditKeywordItemViewModel(result))
                    initialBudget.add(result.priceBid)
                    error.add(false)
                    originalKeyList.add(result.tag)
                } else if (result.status != -1) {
                    negKeyword.add(result)
                }
            }
            sharedViewModel.setNegKeywords(negKeyword)
            if (adapter.items.isEmpty()) {
                setEmptyView()
            } else {
                setVisibilityOperation(View.VISIBLE)
                adapter.notifyDataSetChanged()
            }
            updateItemCount()
            adapter.getBidData(initialBudget, error)
        }
    }

    private fun setEmptyView() {
        adapter.items.clear()
        adapter.items.add(EditKeywordEmptyViewModel())
        setVisibilityOperation(View.GONE)
        adapter.notifyDataSetChanged()
    }

    private fun setVisibilityOperation(visibilty: Int) {
        keyword_count.visibility = visibilty
        add_keyword.visibility = visibilty
        add_image.visibility = visibilty
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OK) {
            if (resultCode == Activity.RESULT_OK) {
                restoreData = data?.getParcelableArrayListExtra(FAVOURED_DATA)
                selectedData = data?.getParcelableArrayListExtra(SELECTED_DATA)
                manualData = data?.getParcelableArrayListExtra(MANUAL_DATA)
                if (selectedData?.size != 0)
                    updateKeywords(selectedData)
            }
        }
    }

    private fun updateKeywords(selectedKeywords: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>?) {
        if (adapter.items.isNotEmpty() && adapter.items[0] is EditKeywordEmptyViewModel) {
            adapter.items.clear()
        }
        selectedKeywords?.forEach {
            if (adapter.items.find { item -> it.keyword == (item as EditKeywordItemViewModel).data.tag } == null) {
                adapter.items.add(EditKeywordItemViewModel(GetKeywordResponse.KeywordsItem(KEYWORD_TYPE_PHRASE, KEYWORD_EXISTS, "0", it.bidSuggest, false, it.keyword, it.source)))
                initialBudget.add(it.bidSuggest)
                error.add(false)
                if (!isExistsOriginal(it.keyword)) {
                    addedKeywords?.add(GetKeywordResponse.KeywordsItem(KEYWORD_TYPE_PHRASE, KEYWORD_EXISTS, "0", it.bidSuggest, false, it.keyword, it.source))
                }
            }
        }
        adapter.getBidData(initialBudget, error)
        setVisibilityOperation(View.VISIBLE)
        updateItemCount()
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
        val list: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
        list.addAll(adapter.getCurrentItems())

        if (adapter.items.isNotEmpty() && adapter.items[0] !is EditKeywordEmptyViewModel) {
            adapter.items.forEachIndexed { index, item ->
                if ((item as EditKeywordItemViewModel).data.priceBid != adapter.data[index]) {
                    item.data.priceBid = adapter.data[index]
                    addedKeywords?.forEach {
                        if (it.tag == item.data.tag)
                            it.priceBid = adapter.data[index]
                    }
                    if (isExistsOriginal(item.data.tag))
                        editedKeywords?.add(item.data)
                }
            }
        }
        bundle.putParcelableArrayList(POSITIVE_CREATE, addedKeywords)
        bundle.putParcelableArrayList(POSITIVE_DELETE, deletedKeywords)
        bundle.putParcelableArrayList(POSITIVE_EDIT, editedKeywords)
        return bundle
    }

    interface ButtonAction {
        fun buttonDisable(enable: Boolean)
    }

}