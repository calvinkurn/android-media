package com.tokopedia.topads.edit.view.fragment.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.Utils
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.view.adapter.neg_keyword.NegKeywordListAdapter
import kotlinx.android.synthetic.main.topads_edit_select_negative_keyword_list_layout.*

/**
 * Created by Pika on 13/4/20.
 */
class NegKeywordAdsListFragment : BaseDaggerFragment() {


    private lateinit var adapter: NegKeywordListAdapter
    private var currentList: ArrayList<String> = arrayListOf()

    companion object {
        private const val SELECTED_KEYWORD = "selectKeywords"
        private const val RESTORED_DATA = "restoreData"
        private const val CURRENTLIST = "currentKeywords"

        fun createInstance(extras: Bundle?): NegKeywordAdsListFragment {
            val fragment = NegKeywordAdsListFragment()
            fragment.arguments = extras
            return fragment

        }
    }

    override fun getScreenName(): String {
        return NegKeywordAdsListFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_select_negative_keyword_list_layout), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = NegKeywordListAdapter(this::onCheckedItem)
    }

    private fun getSelectedList(): ArrayList<GetKeywordResponse.KeywordsItem> {
        val list: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
        list.addAll(adapter.getSelectedList())
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_btn.isEnabled = false
        val data = arguments?.getParcelableArrayList<GetKeywordResponse.KeywordsItem>(RESTORED_DATA)
        currentList = arguments?.getStringArrayList(CURRENTLIST)!!
        formatRestoredData(data)

        add_btn.setOnClickListener {
            if (keywordValidation(editText.text.toString().trim())) {
                adapter.addKeyword(editText.text.toString().trim())
                onCheckedItem()
            }
        }

        btn_next.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra(SELECTED_KEYWORD, getSelectedList())
            intent.putParcelableArrayListExtra(RESTORED_DATA, getData())
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }

        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(context)
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = validateKeyword(s)
                if (s.toString().trim().isEmpty()) {
                    add_btn.isEnabled = false
                } else if (!text.isNullOrBlank()) {
                    setValues(false)
                    error_text.text = text
                } else {
                    setValues(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //  add_btn.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (keywordValidation(editText.text.toString().trim())) {
                    adapter.addKeyword(editText.text.toString().trim())
                    onCheckedItem()
                }
                Utils.dismissKeyboard(context, v)

            }
            true
        }
    }

    private fun formatRestoredData(list: ArrayList<GetKeywordResponse.KeywordsItem>?) {
        val list1: MutableList<GetKeywordResponse.KeywordsItem> = mutableListOf()
        list?.forEach {
            list1.add(it)
        }
        adapter.items.addAll(list1)
        adapter.notifyDataSetChanged()
        onCheckedItem()
    }

    private fun onCheckedItem() {
        val count = adapter.getSelectedList().size
        selected_info.text = String.format(getString(R.string.format_selected_keyword), count)
    }

    private fun setValues(flag: Boolean) {
        if (flag) {
            add_btn.isEnabled = true
            editText.imeOptions = EditorInfo.IME_ACTION_NEXT
            error_text.visibility = View.INVISIBLE

        } else {
            add_btn.isEnabled = false
            editText.imeOptions = EditorInfo.IME_ACTION_NONE
            error_text.visibility = View.VISIBLE
        }
    }

    private fun makeToast(s: String) {
        SnackbarManager.make(activity, s,
                Snackbar.LENGTH_LONG)
                .show()
    }

    private fun validateKeyword(text: CharSequence?): CharSequence? {
        return if (!text.isNullOrBlank() && text.split(" ").size > 5) {
            getString(R.string.error_max_length_keyword)
        } else if (!text.isNullOrBlank() && !text.matches("^[A-Za-z0-9 ]*$".toRegex())) {
            getString(R.string.error_keyword)
        } else if (text!!.length > 50) {
            getString(R.string.error_max_length)
        } else {
            null
        }
    }

    private fun getData(): ArrayList<GetKeywordResponse.KeywordsItem>? {
        val list: ArrayList<GetKeywordResponse.KeywordsItem> = arrayListOf()
        adapter.items.forEach {
            list.add(it)
        }
        return list
    }

    private fun keywordValidation(key: String): Boolean {
        if (key.isNotEmpty()) {
            adapter.items.forEach {
                if (it.tag == key) {
                    makeToast(getString(R.string.keyword_already_exists))
                    return false
                }
            }
            currentList.forEach {
                if (it == key) {
                    makeToast(getString(R.string.keyword_already_exists))
                    return false
                }
            }
        }
        return true
    }
}