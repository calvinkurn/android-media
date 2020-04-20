package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.param.NegKeyword
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.view.activity.SelectNegKeywordActivity
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordViewModel
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.topads_edit_negative_keyword_layout.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 12/4/20.
 */

class EditNegativeKeywordsFragment : BaseDaggerFragment() {

    private val RESTORED_DATA = "restoreData"
    private val SELECTED_KEYWORD = "selectKeywords"
    private val NEGATIVE_KEYWORDS = "negative_keywords"
    private val POSITIVE_KEYWORDS = "positive_keywords"


    private lateinit var adapter: EditNegKeywordListAdapter
    var items: MutableList<EditNegKeywordViewModel> = mutableListOf()
    var restoreData:ArrayList<NegKeyword>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditNegKeywordListAdapter(EditNegKeywordListAdapterTypeFactoryImpl(this::onDeleteNegKeyword, this::onAddKeyword))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_negative_keyword_layout), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter.items.add(EditNegKeywordEmptyViewModel())

    }

    private fun onDeleteNegKeyword(position: Int) {
        showNegConfirmationDialog(position)
    }

    private fun showNegConfirmationDialog(position: Int) {
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.delete_neg_keyword_conf_dialog_title))
        dialog.setDescription(getString(R.string.delete_neg_keyword_conf_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.batal))
        dialog.setSecondaryCTAText(getString(R.string.ya))
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
            if(it.name == (adapter.items[position] as EditNegKeywordItemViewModel).data.name){
                it.isChecked = false
            }

        }
        adapter.items.removeAt(position)
        if (adapter.items.size == 0) {
            adapter.items.add(EditNegKeywordEmptyViewModel())
        }
        adapter.notifyDataSetChanged()
        updateItemCount()

    }

    private fun onAddKeyword() {
        val intent = Intent(context, SelectNegKeywordActivity::class.java)
        intent.putParcelableArrayListExtra(RESTORED_DATA,restoreData)
        startActivityForResult(intent, 1)

    }

    private fun updateItemCount() {
        keyword_count.text = String.format(getString(R.string.keyword_count), adapter.items.size)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_keyword.setOnClickListener {
            onAddKeyword()
        }
        if (adapter.items.size == 0) {
            setVisibilityOperation(false)
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
                val data1 = data?.getParcelableArrayListExtra<NegKeyword>(SELECTED_KEYWORD)
                restoreData = data?.getParcelableArrayListExtra<NegKeyword>(RESTORED_DATA)
                addKeywords(data1)
            }
        }
    }

    private fun addKeywords(data: ArrayList<NegKeyword>?) {
        adapter.items.clear()
        setVisibilityOperation(true)
        data?.forEach {
         //   if (adapter.items.find { it -> (it as EditNegKeywordItemViewModel).data.name == it.data.name } == null)
                adapter.items.add(EditNegKeywordItemViewModel(it))
        }
        updateItemCount()
        adapter.notifyDataSetChanged()

    }

    fun sendData(): Bundle {
        var bundle = Bundle()
        var list: ArrayList<NegKeyword> = arrayListOf()
        list.addAll(adapter.getCurrentItems())
        bundle.putParcelableArrayList(NEGATIVE_KEYWORDS, list)
        return bundle
    }

}