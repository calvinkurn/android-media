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
import com.tokopedia.topads.common.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.data.response.ResponseBidInfo
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.view.activity.SelectKeywordActivity
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.topads_edit_keword_layout.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Pika on 12/4/20.
 */
class EditKeywordsFragment : BaseDaggerFragment() {

    private lateinit var sharedViewModel: SharedViewModel

    @Inject
    lateinit var viewmodel: EditFormDefaultViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: EditKeywordListAdapter
    private var bidMap = mutableMapOf<String, Int>()
    private lateinit var callBack: ButtonAction
    private var minSuggestKeyword = 0
    private var maxSuggestKeyword = 0
    private val MAX_BID = "max"
    private val MIN_BID = "min"
    private var deletedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var addedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var editedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var initialBudget: MutableList<Int> = mutableListOf()
    private var error: MutableList<Boolean> = mutableListOf()
    private var originalKeyList: MutableList<String> = arrayListOf()
    private var restoreData: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>? = arrayListOf()
    private var selectedData: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>? = arrayListOf()
    private var manualData: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>? = arrayListOf()


    private var groupId = 0
    private var productIds = ""

    companion object {
        private const val FAVOURED_DATA = "favouredData"
        private const val SELECTED_DATA = "selectedData"
        private const val PRODUCT_ID = "product"
        private const val GROUP_ID = "groupId"
        private const val MIN_SUGGESTION = "minSuggestedBid"
        private const val POSITIVE_CREATE = "createdPositiveKeyword"
        private const val POSITIVE_DELETE = "deletedPositiveKeyword"
        private const val POSITIVE_EDIT = "editedPositiveKeyword"
        private const val MANUAL_DATA = "manualData"
        private const val POSITIVE_PHRASE = 11
        private const val POSITIVE_SPECIFIC = 21

    }

    override fun getScreenName(): String {
        return EditKeywordsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditKeywordListAdapter(EditKeywordListAdapterTypeFactoryImpl(this::onDeleteKeyword, this::onAddKeyword, this::getMinMax, this::actionEnable))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewmodel = ViewModelProviders.of(this, viewModelFactory).get(EditFormDefaultViewModel::class.java)
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_keword_layout), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val suggestions = java.util.ArrayList<DataSuggestions>()
        val dummyId: MutableList<Int> = mutableListOf()
        suggestions.add(DataSuggestions("group", dummyId))
        viewmodel.getBidInfo(suggestions, this::onSuccessSuggestion, this::onErrorSuggestion)
        viewmodel.getAdKeyword(groupId, this::onSuccessKeyword, this::onErrorKeyword, this::onEmptyKeyword)
    }

    private fun getMinMax(): MutableMap<String, Int> {
        bidMap[MIN_BID] = minSuggestKeyword
        bidMap[MAX_BID] = maxSuggestKeyword
        return bidMap
    }

    private fun actionEnable() {
        val enable = !adapter.isError()
        callBack.buttonDisable(enable)

    }

    private fun onErrorSuggestion(e: Throwable) {
        Timber.d(e)
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
        intent.putParcelableArrayListExtra(FAVOURED_DATA, restoreData)
        intent.putParcelableArrayListExtra(SELECTED_DATA, selectedData)
        intent.putParcelableArrayListExtra(MANUAL_DATA, manualData)
        startActivityForResult(intent, 1)
    }

    private fun showConfirmationDialog(position: Int) {
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.delete_keyword_conf_dialog_title))
        dialog.setDescription(getString(R.string.delete_keyword_conf_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.batal))
        dialog.setSecondaryCTAText(getString(R.string.ya))
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
        if (selectedData!!.isNotEmpty()) {
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
        if (adapter.items.size == 0) {
            setEmptyView(true)
        }
        adapter.notifyDataSetChanged()
        updateItemCount()
    }

    private fun isExistsOriginal(position: Int): Boolean {
        return (originalKeyList.find { it -> (adapter.items[position] as EditKeywordItemViewModel).data.tag == it } != null)

    }

    private fun isExistsOriginal(name: String): Boolean {
        return (originalKeyList.find { it -> name == it } != null)

    }

    private fun updateItemCount() {
        keyword_count.text = String.format(getString(R.string.keyword_count), adapter.items.size)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.productId.observe(requireActivity(), Observer {
            productIds = it.toString()
        })
        sharedViewModel.groupId.observe(requireActivity(), Observer {
            groupId = it
        })
        add_keyword.setOnClickListener {
            onAddKeyword()
        }
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(context)
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>) {
        val negKeyword: MutableList<GetKeywordResponse.KeywordsItem> = mutableListOf()
        data.forEach { result ->
            if ((result.type == POSITIVE_SPECIFIC || result.type == POSITIVE_PHRASE) && result.status != -1) {
                adapter.items.add(EditKeywordItemViewModel(result))
                initialBudget.add(result.priceBid)
                error.add(false)
                originalKeyList.add(result.tag)
            } else if (result.status != -1) {
                negKeyword.add(result)
            }
        }
        sharedViewModel.setNegKeywords(negKeyword)
        if (adapter.items.size == 0) {
            setEmptyView(true)
        } else {
            setVisibilityOperation(true)
            adapter.notifyDataSetChanged()
        }
        updateItemCount()
        adapter.getBidData(initialBudget, error)
    }

    private fun setEmptyView(value: Boolean) {
        adapter.items.clear()
        adapter.items.add(EditKeywordEmptyViewModel())
        setVisibilityOperation(false)
        adapter.notifyDataSetChanged()
    }

    private fun onErrorKeyword(error: String) {}

    private fun onEmptyKeyword() {
        setEmptyView(true)

    }

    private fun setVisibilityOperation(flag: Boolean) {
        if (flag) {
            keyword_count.visibility = View.VISIBLE
            add_keyword.visibility = View.VISIBLE
            add_image.visibility = View.VISIBLE
        } else {
            keyword_count.visibility = View.GONE
            add_keyword.visibility = View.GONE
            add_image.visibility = View.GONE

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                restoreData = data?.getParcelableArrayListExtra(FAVOURED_DATA)
                selectedData = data?.getParcelableArrayListExtra(SELECTED_DATA)
                manualData = data?.getParcelableArrayListExtra(MANUAL_DATA)
                updateKeywords(selectedData)
            }
        }
    }

    private fun updateKeywords(selectedKeywords: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>?) {
        if (adapter.items[0] is EditKeywordEmptyViewModel) {
            adapter.items.clear()
        }
        selectedKeywords?.forEach {
            if (adapter.items.find { item -> it.keyword == (item as EditKeywordItemViewModel).data.tag } == null) {
                adapter.items.add(EditKeywordItemViewModel(GetKeywordResponse.KeywordsItem(POSITIVE_PHRASE, 1, "0", it.bidSuggest, false, it.keyword)))
                initialBudget.add(it.bidSuggest)
                error.add(false)
                if (!isExistsOriginal(it.keyword)) {
                    addedKeywords?.add(GetKeywordResponse.KeywordsItem(POSITIVE_PHRASE, 1, "0", it.bidSuggest, false, it.keyword))
                }
            }
        }
        adapter.getBidData(initialBudget, error)
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

        if (adapter.items[0] !is EditKeywordEmptyViewModel) {
            adapter.items.forEachIndexed { index, item ->
                if ((item as EditKeywordItemViewModel).data.priceBid != adapter.data[index]) {
                    (adapter.items[index] as EditKeywordItemViewModel).data.priceBid = adapter.data[index]
                    editedKeywords?.add((adapter.items[index] as EditKeywordItemViewModel).data)
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