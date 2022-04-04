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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.CURRENTLIST
import com.tokopedia.topads.edit.utils.Constants.RESTORED_DATA
import com.tokopedia.topads.edit.utils.Constants.SELECTED_KEYWORD
import com.tokopedia.topads.edit.view.adapter.neg_keyword.NegKeywordListAdapter
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 13/4/20.
 */
class NegKeywordAdsListFragment : BaseDaggerFragment() {

    private var editText: TextFieldUnify? = null
    private var addBtn: UnifyButton? = null
    private var keywordList: RecyclerView? = null
    private var btnNext: UnifyButton? = null
    private var selectedInfo: Typography? = null

    private lateinit var adapter: NegKeywordListAdapter
    private var currentList: ArrayList<String> = arrayListOf()

    companion object {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view =
            inflater.inflate(resources.getLayout(R.layout.topads_edit_select_negative_keyword_list_layout),
                container, false)
        editText = view.findViewById(R.id.editText)
        addBtn = view.findViewById(R.id.add_btn)
        keywordList = view.findViewById(R.id.keyword_list)
        btnNext = view.findViewById(R.id.btn_next)
        selectedInfo = view.findViewById(R.id.selected_info)
        return view
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
        addBtn?.isEnabled = false
        val data = arguments?.getParcelableArrayList<GetKeywordResponse.KeywordsItem>(RESTORED_DATA)
        currentList = arguments?.getStringArrayList(CURRENTLIST)!!
        formatRestoredData(data)

        addBtn?.setOnClickListener {
            if (keywordValidation(editText?.textFieldInput?.text.toString().trim())) {
                adapter.addKeyword(editText?.textFieldInput?.text.toString().trim())
                onCheckedItem()
            }
            addBtn?.isEnabled = false
            editText?.textFieldInput?.text?.clear()
        }

        btnNext?.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra(SELECTED_KEYWORD, getSelectedList())
            intent.putParcelableArrayListExtra(RESTORED_DATA, getData())
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }

        keywordList?.adapter = adapter
        keywordList?.layoutManager = LinearLayoutManager(context)
        editText?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = validateKeyword(s)
                if (s.toString().trim().isEmpty()) {
                    addBtn?.isEnabled = false
                } else if (!text.isNullOrBlank()) {
                    setValues(false, text)
                } else {
                    setValues(true, "")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        editText?.textFieldInput?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (keywordValidation(editText?.textFieldInput?.text.toString().trim())) {
                    adapter.addKeyword(editText?.textFieldInput?.text.toString().trim())
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
        selectedInfo?.text = String.format(getString(R.string.format_selected_keyword), count)
    }

    private fun setValues(flag: Boolean, text: CharSequence) {
        addBtn?.isEnabled = flag
        editText?.textFieldInput?.imeOptions =
            if (flag) EditorInfo.IME_ACTION_NEXT else EditorInfo.IME_ACTION_NONE
        if (flag) {
            editText?.setError(false)
            editText?.setMessage(text)
        } else {
            editText?.setError(true)
            editText?.setMessage(text)
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
        if (key.isEmpty()) {
            return false
        } else {
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