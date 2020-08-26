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
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.view.sheet.TopAdsEditKeywordBidSheet
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.data.response.ResponseBidInfo
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.CURRENT_KEY_TYPE
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.ITEM_POSITION
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_EXISTS
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_NAME
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_EXACT
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.MAX_BID
import com.tokopedia.topads.edit.utils.Constants.MIN_BID
import com.tokopedia.topads.edit.utils.Constants.MIN_SUGGESTION
import com.tokopedia.topads.edit.utils.Constants.ORIGINAL_LIST
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_EDIT
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
import kotlinx.android.synthetic.main.topads_edit_keword_layout.*
import javax.inject.Inject

/**
 * Created by Pika on 12/4/20.
 */
class EditKeywordsFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: EditKeywordListAdapter
    private lateinit var callBack: ButtonAction
    private var minSuggestKeyword = 0
    private var maxSuggestKeyword = 0
    private var deletedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var addedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var editedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var initialBudget: MutableList<Int> = mutableListOf()
    private var isnewlyAddded:MutableList<Boolean> = mutableListOf()
    private var originalKeyList: MutableList<String> = arrayListOf()
    private var selectedData: ArrayList<KeywordDataItem>? = arrayListOf()
    private var selectedDataOriginal: ArrayList<KeywordDataItem>? = arrayListOf()

    private var cursor = ""
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

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
        adapter = EditKeywordListAdapter(EditKeywordListAdapterTypeFactoryImpl(this::onAddKeyword, ::onActionClicked))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(resources.getLayout(R.layout.topads_edit_keword_layout), container, false)
         recyclerView = view.findViewById(R.id.keyword_list)
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
                if (cursor!="") {
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
        val suggestions = java.util.ArrayList<DataSuggestions>()
        val dummyId: MutableList<Int> = mutableListOf()
        suggestions.add(DataSuggestions("group", dummyId))
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion)
    }


    private fun onActionClicked(pos: Int) {
        val bundle = Bundle()
        bundle.putInt(MAX_BID, maxSuggestKeyword)
        bundle.putInt(MIN_BID, minSuggestKeyword)
        bundle.putInt(SUGGESTION_BID, (adapter.items[pos] as EditKeywordItemViewModel).data.priceBid)
        bundle.putInt(ITEM_POSITION, pos)
        bundle.putInt(CURRENT_KEY_TYPE, (adapter.items[pos] as EditKeywordItemViewModel).data.type)
        bundle.putString(KEYWORD_NAME, (adapter.items[pos] as EditKeywordItemViewModel).data.tag)
        val sheet = TopAdsEditKeywordBidSheet.createInstance(bundle)
        sheet.show(fragmentManager!!, "")
        sheet.onSaved = { bid, type, position ->
            if ((adapter.items[pos] as EditKeywordItemViewModel).data.type != type) {
                actionStatusChange(pos)
            }
            (adapter.items[pos] as EditKeywordItemViewModel).data.type = type
            (adapter.items[pos] as EditKeywordItemViewModel).data.priceBid = bid.toInt()
            adapter.notifyItemChanged(position)

        }
        sheet.onDelete = { position ->
            showConfirmationDialog(position)
        }
    }

    private fun actionStatusChange(position: Int) {
        if (isExistsOriginal(position)) {
            deletedKeywords?.add((adapter.items[position] as EditKeywordItemViewModel).data)
            addedKeywords?.add((adapter.items[position] as EditKeywordItemViewModel).data)
        } else {
            addedKeywords?.forEach {
                if (it.tag == (adapter.items[position] as EditKeywordItemViewModel).data.tag)
                    it.type = (adapter.items[position] as EditKeywordItemViewModel).data.type
            }
        }
    }

    private fun showConfirmationDialog(position: Int) {
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.topads_edit_delete_keyword_conf_dialog_title))
        dialog.setDescription(String.format(getString(R.string.topads_edit_delete_keyword_conf_dialog_desc), (adapter.items[position] as EditKeywordItemViewModel).data.tag))
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


    private fun onSuccessSuggestion(data: List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) {
        minSuggestKeyword = data[0].minBid
        maxSuggestKeyword = data[0].maxBid
    }

    private fun onAddKeyword() {
        val intent = Intent(context, SelectKeywordActivity::class.java)
        intent.putExtra(MIN_SUGGESTION, minSuggestKeyword)
        intent.putExtra(PRODUCT_ID, productIds)
        intent.putExtra(GROUP_ID, groupId)
        intent.putStringArrayListExtra(ORIGINAL_LIST, ArrayList(originalKeyList))
        intent.putParcelableArrayListExtra(SELECTED_DATA, selectedData)
        startActivityForResult(intent, 1)
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
        isnewlyAddded.removeAt(position)
        initialBudget.removeAt(position)
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
            viewModel.getAdKeyword(groupId, cursor, this::onSuccessKeyword)
        })

        add_keyword.setOnClickListener {
            onAddKeyword()
        }
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>,cursor:String) {
        this.cursor = cursor
        if (data.isEmpty()) {
            setEmptyView()
        } else {
            val negKeyword: MutableList<GetKeywordResponse.KeywordsItem> = mutableListOf()
            data.forEach { result ->
                if ((result.type == KEYWORD_TYPE_EXACT || result.type == KEYWORD_TYPE_PHRASE) && result.status != -1) {
                    adapter.items.add(EditKeywordItemViewModel(result))
                    isnewlyAddded.add(false)
                    initialBudget.add(result.priceBid)
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
            recyclerviewScrollListener.updateStateAfterGetData()
            adapter.getBidData(initialBudget,isnewlyAddded)
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
                selectedData = data?.getParcelableArrayListExtra(SELECTED_DATA)
                selectedDataOriginal = selectedData
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
            if (adapter.items.find { item -> it.keyword == (item as EditKeywordItemViewModel).data.tag } == null) {
                adapter.items.add(EditKeywordItemViewModel(GetKeywordResponse.KeywordsItem(KEYWORD_TYPE_PHRASE, KEYWORD_EXISTS, "0", it.bidSuggest, false, it.keyword, it.source)))
                initialBudget.add(it.bidSuggest)
                isnewlyAddded.add(true)
                if (!isExistsOriginal(it.keyword)) {
                    addedKeywords?.add(GetKeywordResponse.KeywordsItem(KEYWORD_TYPE_PHRASE, KEYWORD_EXISTS, "0", it.bidSuggest, false, it.keyword, it.source))
                }
            }
        }
        adapter.getBidData(initialBudget, isnewlyAddded)
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