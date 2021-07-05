package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.CURRENTLIST
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_ADDED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_DELETED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORD_ALL
import com.tokopedia.topads.edit.utils.Constants.REQUEST_OK
import com.tokopedia.topads.edit.utils.Constants.RESTORED_DATA
import com.tokopedia.topads.edit.utils.Constants.SELECTED_KEYWORD
import com.tokopedia.topads.edit.view.activity.SelectNegKeywordActivity
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_edit_negative_keyword_layout.*
import javax.inject.Inject

/**
 * Created by Pika on 12/4/20.
 */

class EditNegativeKeywordsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var deletedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var addedKeywords: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var originalKeyList: MutableList<String> = arrayListOf()
    private lateinit var adapter: EditNegKeywordListAdapter
    private var restoreData: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private var cursor = ""
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var groupId = 0
    private val sharedViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
    }
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(EditFormDefaultViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditNegKeywordListAdapter(EditNegKeywordListAdapterTypeFactoryImpl(this::onDeleteNegKeyword, this::onAddKeyword, ::onStatusChange))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_edit_negative_keyword_layout), container, false)
        recyclerView = view.findViewById(R.id.keyword_list)
        setAdapter()
        return view
    }

    private fun fetchNextPage() {
        viewModel.getAdKeyword(groupId, cursor, this::onSuccessKeyword)
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

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>, cursor: String) {
        this.cursor = cursor
        data.forEach { result ->
            if ((result.type == Constants.KEYWORD_TYPE_NEGATIVE_PHRASE || result.type == Constants.KEYWORD_TYPE_NEGATIVE_EXACT)) {
                adapter.items.add(EditNegKeywordItemViewModel(result))
                originalKeyList.add(result.tag)
            }
        }
        if (adapter.items.isEmpty()) {
            adapter.items.add(EditNegKeywordEmptyViewModel())
            setVisibilityOperation(View.GONE)
        } else {
            setVisibilityOperation(View.VISIBLE)
        }
        updateItemCount()
        adapter.notifyDataSetChanged()
        recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun onDeleteNegKeyword(position: Int) {
        showNegConfirmationDialog(position)
    }

    private fun onStatusChange(position: Int) {
        if (isExistsOriginal(position)) {
            deletedKeywords?.add((adapter.items[position] as EditNegKeywordItemViewModel).data)
            addedKeywords?.add((adapter.items[position] as EditNegKeywordItemViewModel).data)
        }
    }

    private fun showNegConfirmationDialog(position: Int) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.topads_edit_delete_neg_keyword_conf_dialog_title))
            dialog.setDescription(MethodChecker.fromHtml(String.format(getString(R.string.topads_edit_delete_neg_keyword_conf_dialog_desc),
                    (adapter.items[position] as EditNegKeywordItemViewModel).data.tag)))
            dialog.setPrimaryCTAText(getString(R.string.topads_edit_batal))
            dialog.setSecondaryCTAText(getString(R.string.topads_edit_ya))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                deleteNegKeyword(position)
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    private fun deleteNegKeyword(position: Int) {
        restoreData?.forEach {
            if (it.tag == (adapter.items[position] as EditNegKeywordItemViewModel).data.tag) {
                it.isChecked = false
            }
        }
        if (isExistsOriginal(position)) {
            deletedKeywords?.add((adapter.items[position] as EditNegKeywordItemViewModel).data)
        } else {
            var ind = 0
            addedKeywords?.forEachIndexed { index, it ->
                if (it.tag == (adapter.items[position] as EditNegKeywordItemViewModel).data.tag) {
                    ind = index
                }
            }
            addedKeywords?.removeAt(ind)
        }
        adapter.items.removeAt(position)
        if (adapter.items.isEmpty()) {
            adapter.items.add(EditNegKeywordEmptyViewModel())
            setVisibilityOperation(View.GONE)
        }
        adapter.notifyDataSetChanged()
        updateItemCount()
        view?.let {
            Toaster.build(it, getString(com.tokopedia.topads.common.R.string.topads_neg_keyword_common_del_toaster),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL).show()
        }
    }

    private fun onAddKeyword() {
        val intent = Intent(context, SelectNegKeywordActivity::class.java)
        intent.putParcelableArrayListExtra(RESTORED_DATA, restoreData)
        intent.putStringArrayListExtra(CURRENTLIST, getCurrentItems())
        startActivityForResult(intent, 1)
    }

    private fun getCurrentItems(): ArrayList<String>? {
        val list: ArrayList<String> = arrayListOf()
        adapter.items.forEach {
            if (it is EditNegKeywordItemViewModel)
                list.add(it.data.tag)
        }
        return list
    }

    private fun updateItemCount() {
        keyword_count.text = String.format(getString(R.string.topads_edit_kata_kunci_negative_count), adapter.items.size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getGroupId().observe(viewLifecycleOwner, Observer {
            groupId = it
            adapter.items.clear()
            viewModel.getAdKeyword(groupId, cursor, this::onSuccessKeyword)
        })

        add_image.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_plus_add_keyword))
        add_keyword.setOnClickListener {
            onAddKeyword()
        }
    }

    override fun getScreenName(): String {
        return EditNegativeKeywordsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    private fun setVisibilityOperation(visibility: Int) {
        keyword_count.visibility = visibility
        add_keyword.visibility = visibility
        add_image.visibility = visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OK) {
            if (resultCode == Activity.RESULT_OK) {
                val selected = data?.getParcelableArrayListExtra<GetKeywordResponse.KeywordsItem>(SELECTED_KEYWORD)
                restoreData = data?.getParcelableArrayListExtra(RESTORED_DATA)
                if (selected?.size != 0)
                    addKeywords(selected)
            }
        }
    }

    private fun addKeywords(data: ArrayList<GetKeywordResponse.KeywordsItem>?) {
        if (adapter.items.isNotEmpty() && adapter.items[0] is EditNegKeywordEmptyViewModel) {
            adapter.items.clear()
        }
        data?.forEach {
            if (adapter.items.find { item -> (item as EditNegKeywordItemViewModel).data.tag == it.tag } == null) {
                adapter.items.add(EditNegKeywordItemViewModel(it))
                addedKeywords?.add(it)
            }
        }
        setVisibilityOperation(View.VISIBLE)
        updateItemCount()
        adapter.notifyDataSetChanged()
    }

    private fun isExistsOriginal(position: Int): Boolean {
        return (originalKeyList.find { it -> (adapter.items[position] as EditNegKeywordItemViewModel).data.tag == it } != null)
    }

    fun sendData(): Bundle {
        val bundle = Bundle()
        val list: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
        list.addAll(adapter.getCurrentItems())
        bundle.putParcelableArrayList(NEGATIVE_KEYWORDS_ADDED, addedKeywords)
        bundle.putParcelableArrayList(NEGATIVE_KEYWORDS_DELETED, deletedKeywords)
        bundle.putParcelableArrayList(NEGATIVE_KEYWORD_ALL, list)
        return bundle
    }

}