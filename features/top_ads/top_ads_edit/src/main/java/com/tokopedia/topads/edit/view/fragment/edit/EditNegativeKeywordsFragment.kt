package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.CURRENTLIST
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_ADDED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_DELETED
import com.tokopedia.topads.edit.utils.Constants.REQUEST_OK
import com.tokopedia.topads.edit.utils.Constants.RESTORED_DATA
import com.tokopedia.topads.edit.utils.Constants.SELECTED_KEYWORD
import com.tokopedia.topads.edit.view.activity.SelectNegKeywordActivity
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel
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
    private val sharedViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditNegKeywordListAdapter(EditNegKeywordListAdapterTypeFactoryImpl(this::onDeleteNegKeyword, this::onAddKeyword, ::onStatusChange))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_negative_keyword_layout), container, false)
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
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.topads_edit_delete_neg_keyword_conf_dialog_title))
        dialog.setDescription(Html.fromHtml(String.format(getString(R.string.topads_edit_delete_neg_keyword_conf_dialog_desc),
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

        sharedViewModel.getnegKeywords().observe(viewLifecycleOwner, Observer {
            adapter.items.clear()
            it.forEach { item ->
                adapter.items.add(EditNegKeywordItemViewModel(item))
                originalKeyList.add(item.tag)

            }
            if (adapter.items.isEmpty()) {
                adapter.items.add(EditNegKeywordEmptyViewModel())
                setVisibilityOperation(View.GONE)
            } else {
                setVisibilityOperation(View.VISIBLE)
            }
            updateItemCount()
            adapter.notifyDataSetChanged()
        })
        if (adapter.items.isNotEmpty()) {
            setVisibilityOperation(View.VISIBLE)
        } else {
            adapter.items.add(EditNegKeywordEmptyViewModel())
            setVisibilityOperation(View.GONE)

        }
        add_keyword.setOnClickListener {
            onAddKeyword()
        }
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(context)
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
        bundle.putParcelableArrayList(NEGATIVE_KEYWORDS_ADDED, addedKeywords)
        bundle.putParcelableArrayList(NEGATIVE_KEYWORDS_DELETED, deletedKeywords)
        return bundle
    }

}