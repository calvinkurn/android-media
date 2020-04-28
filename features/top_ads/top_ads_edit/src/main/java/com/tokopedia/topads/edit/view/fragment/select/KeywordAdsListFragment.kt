package com.tokopedia.topads.edit.view.fragment.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.common.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.Utils
import com.tokopedia.topads.edit.di.TopAdsEditComponent
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
    private var selected: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>? = arrayListOf()
    private var favoured: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>? = arrayListOf()
    private var manual: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>? = arrayListOf()


    companion object {
        private const val FAVOURED_DATA = "favouredData"
        private const val SELECTED_DATA = "selectedData"
        private const val MANUAL_DATA = "manualData"
        private const val ORIGINAL_LIST = "originalList"
        private const val NOT_KNOWN = "Tidak diketahui"
        private const val PRODUCT_ID = "product"
        private const val MIN_SUGGESTION = "minSuggestedBid"
        private const val GROUP_ID = "groupId"


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
        val productIds = arguments?.getString(PRODUCT_ID)!!
        productId = productIds.substring(1, productIds.length - 1)
        val groupId = arguments?.getInt(GROUP_ID)
        originalList = arguments?.getStringArrayList(ORIGINAL_LIST)!!
        minSuggestedBid = arguments?.getInt(MIN_SUGGESTION)!!
        viewModel.getSugestionKeyword(productId, groupId, this::onSuccessSuggestion, this::onErrorSuggestion)
    }

    private fun onKeywordSelected(pos: Int) {
        showSelectMessage()
        if (pos != -1 && keywordListAdapter.items[pos] is KeywordItemViewModel) {
            if ((keywordListAdapter.items[pos] as KeywordItemViewModel).data.totalSearch != "Tidak diketahui")
                keywordListAdapter.setSelectedKeywords(getFavouredData())
        }
    }

    private fun showSelectMessage() {
        val count = keywordListAdapter.getSelectedItems().size
        selected_info.text = String.format(getString(R.string.format_selected_keyword), count)
        if (count >= 50) {
            btn_next.isEnabled = false
            error_text.visibility = View.VISIBLE
            error_text.text = getString(R.string.error_max_selected_keyword)
        } else {
            btn_next.isEnabled = true
            error_text.visibility = View.INVISIBLE
        }
    }

    private fun onSuccessSuggestion(keywords: List<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>) {
        keywordListAdapter.favoured.clear()
        keywordListAdapter.manualKeywords.clear()
        var list: MutableList<KeywordViewModel> = mutableListOf()
        list.add(KeywordGroupViewModel("Rekomendasi"))
        keywords.forEach { index ->
            list.add(KeywordItemViewModel(index))
            keywordList.add(KeywordItemViewModel(index).data.keyword)
        }

        keywordListAdapter.setList(list)
        keywordListAdapter.notifyDataSetChanged()

        addManualKeywords()
        showSelectMessage()
    }

    private fun addManualKeywords() {

        if (!favoured.isNullOrEmpty())
            keywordListAdapter.addRestoredData(favoured, selected, manual)
    }

    private fun onErrorSuggestion(throwable: Throwable) {
        keywordListAdapter.favoured.clear()
        keywordListAdapter.manualKeywords.clear()
        keywordListAdapter.items.clear()
        addManualKeywords()
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

    private fun getFavouredDataArray(): ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data> {
        var finalList: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data> = arrayListOf()
        getFavouredData().forEach {
            finalList.add(it.data)
        }
        return finalList
    }

    private fun getSelectedKeywordArray(): ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data> {
        var finalList: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data> = arrayListOf()
        keywordListAdapter.getSelectedItems().forEach {
            finalList.add(it.data)
        }
        return finalList
    }

    private fun getManualKeywordArray(): ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data> {
        var finalList: ArrayList<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data> = arrayListOf()
        keywordListAdapter.manualKeywords.forEach {
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
            returnIntent.putParcelableArrayListExtra(FAVOURED_DATA, getFavouredDataArray())
            returnIntent.putParcelableArrayListExtra(SELECTED_DATA, getSelectedKeywordArray())
            returnIntent.putParcelableArrayListExtra(MANUAL_DATA, getManualKeywordArray())
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
                Utils.dismissKeyboard(context, v)

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
        } else if (!text.isNullOrBlank() && !text.matches("^[A-Za-z0-9 ]*$".toRegex())) {
            getString(R.string.error_keyword)
        } else if (text!!.length > 50) {
            getString(R.string.error_max_length)
        } else {
            null
        }
    }

}