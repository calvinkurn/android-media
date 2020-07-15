package com.tokopedia.topads.edit.view.fragment.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.COUNT
import com.tokopedia.topads.edit.utils.Constants.FAVOURED_DATA
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.MANUAL_DATA
import com.tokopedia.topads.edit.utils.Constants.MIN_SUGGESTION
import com.tokopedia.topads.edit.utils.Constants.NOT_KNOWN
import com.tokopedia.topads.edit.utils.Constants.ORIGINAL_LIST
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID
import com.tokopedia.topads.edit.utils.Constants.REGEX
import com.tokopedia.topads.edit.utils.Constants.SELECTED_DATA
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModel
import kotlinx.android.synthetic.main.topads_edit_select_layout_keyword_list.*
import javax.inject.Inject

class KeywordAdsListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter
    private val keywordList = HashSet<String>()
    var productId = ""
    var minSuggestedBid = 0
    private var originalList: ArrayList<String> = arrayListOf()
    private var selected: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>? = arrayListOf()
    private var favoured: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>? = arrayListOf()
    private var manual: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>? = arrayListOf()


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
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(KeywordAdsViewModel::class.java)

            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        keywordListAdapter = KeywordListAdapter.createInstance(KeywordListAdapterTypeFactoryImpl(this::onKeywordSelected))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val productIds = arguments?.getString(PRODUCT_ID) ?: ""
        val groupId = arguments?.getInt(GROUP_ID)
        originalList = arguments?.getStringArrayList(ORIGINAL_LIST)!!
        minSuggestedBid = arguments?.getInt(MIN_SUGGESTION)!!
        viewModel.getSuggestionKeyword(productIds, groupId, this::onSuccessSuggestion)
    }

    private fun onKeywordSelected(pos: Int) {
        showSelectMessage()
        if (pos != -1 && pos < keywordListAdapter.items.size && keywordListAdapter.items[pos] is KeywordItemViewModel) {
            if ((keywordListAdapter.items[pos] as KeywordItemViewModel).data.totalSearch != "Tidak diketahui")
                keywordListAdapter.setSelectedKeywords(getFavouredData())
        }
    }

    private fun showSelectMessage() {
        onCheckedItem()
        val count = keywordListAdapter.getSelectedItems().size
        btn_next.isEnabled = count <= COUNT
        error_text.text = if (count > COUNT) getString(R.string.error_max_selected_keyword) else ""
        error_text.visibility = if (count > COUNT) View.INVISIBLE else View.VISIBLE
    }

    private fun onCheckedItem() {
        val count = keywordListAdapter.getSelectedItems().size
        selected_info.text = String.format(getString(R.string.format_selected_keyword), count)
    }

    private fun onSuccessSuggestion(keywords: List<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem>) {
        keywordListAdapter.favoured.clear()
        keywordListAdapter.manualKeywords.clear()
        val list: MutableList<KeywordViewModel> = mutableListOf()
        list.add(KeywordGroupViewModel("Rekomendasi"))
        keywords.forEach { key->
            key.keywordData.forEach {
                list.add(KeywordItemViewModel(it))
                keywordList.add(KeywordItemViewModel(it).data.keyword)

            }
        }
        keywordListAdapter.setList(list)
        addManualKeywords()
        showSelectMessage()
    }

    private fun addManualKeywords() {

        if (!favoured.isNullOrEmpty())
            keywordListAdapter.addRestoredData(favoured, selected, manual)
    }

    private fun getFavouredData(): List<KeywordItemViewModel> {
        val manual = mutableListOf<KeywordItemViewModel>()
        val selected = mutableListOf<KeywordItemViewModel>()
        selected.addAll(keywordListAdapter.getSelectedItems())
        manual.addAll(keywordListAdapter.manualKeywords)
        manual.addAll(selected)
        selected.forEach { index ->
            if (index.data.totalSearch == NOT_KNOWN) {
                manual.remove(index)

            }
        }
        return manual
    }

    private fun getArrayList(list: List<KeywordItemViewModel>): ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem> {
        val finalList: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem> = arrayListOf()
        list.forEach {
            finalList.add(it.data)
        }
        return finalList
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
        favoured = arguments?.getParcelableArrayList(FAVOURED_DATA)
        selected = arguments?.getParcelableArrayList(SELECTED_DATA)
        manual = arguments?.getParcelableArrayList(MANUAL_DATA)
        add_btn.isEnabled = false
        add_btn.setOnClickListener {
            keywordValidation(editText.text.toString().trim())
        }
        btn_next.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putParcelableArrayListExtra(FAVOURED_DATA, getArrayList(getFavouredData()))
            returnIntent.putParcelableArrayListExtra(SELECTED_DATA, getArrayList(keywordListAdapter.getSelectedItems()))
            returnIntent.putParcelableArrayListExtra(MANUAL_DATA, getArrayList(keywordListAdapter.manualKeywords))
            activity?.setResult(Activity.RESULT_OK, returnIntent)
            activity?.finish()
        }
        keyword_list.adapter = keywordListAdapter
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
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keywordValidation(editText.text.toString().trim())
                Constants.dismissKeyboard(context, v)

            }
            true
        }
    }
    private fun keywordValidation(key: String) {
        if (key.isNotEmpty()) {
            val alreadyExists: Boolean = keywordListAdapter.addNewKeyword(viewModel.addNewKeyword(key, minSuggestedBid), originalList)
            showSelectMessage()
            if (alreadyExists) {
                makeToast(getString(R.string.keyword_already_exists))
            }
        }
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
        } else if (!text.isNullOrBlank() && !text.matches(REGEX.toRegex())) {
            getString(R.string.error_keyword)
        } else if (text!!.length > COUNT) {
            getString(R.string.error_max_length)
        } else {
            null
        }
    }

}