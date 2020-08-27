package com.tokopedia.topads.edit.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.view.sheet.TipSheetKeywordList
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.view.adapter.edit_keyword.KeywordSearchAdapter
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment.Companion.PRODUCT_IDS_SELECTED
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment.Companion.SEARCH_QUERY
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment.Companion.SELECTED_KEYWORDS
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModel
import com.tokopedia.unifycomponents.SearchBarUnify
import kotlinx.android.synthetic.main.topads_edit_keyword_search_layout.*
import javax.inject.Inject

class KeywordSearchActivity : BaseActivity(), HasComponent<TopAdsEditComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var adapter: KeywordSearchAdapter
    private lateinit var search: SearchBarUnify

    override fun getComponent(): TopAdsEditComponent {
        return DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(KeywordAdsViewModel::class.java)
        adapter = KeywordSearchAdapter(::onSelectedItem)
        setContentView(R.layout.topads_edit_keyword_search_layout)
        setSearchBar()
        fetchData()
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(this)
        tip_btn.setOnClickListener {
            TipSheetKeywordList().show(supportFragmentManager, KeywordSearchActivity::class.java.name)
        }
        btn_next.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putParcelableArrayListExtra(SELECTED_KEYWORDS, ArrayList(adapter.getSelectedItem()))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun initInjector() {
        val toAdsEditComponent = DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
        toAdsEditComponent.inject(this)
    }

    private fun setSearchBar() {
        setSearchParam()
        setHeader()
        val searchTextField = search.searchBarTextField
        val searchClearButton = search.searchBarIcon
        searchTextField.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(textView: TextView?, actionId: Int, even: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchData()
                    Constants.dismissKeyboard(baseContext, rootView)
                    return true
                }
                return false
            }
        })
        searchClearButton.setOnClickListener {
            searchTextField.text?.clear()
            Constants.dismissKeyboard(this, rootView)
        }
    }

    private fun setSearchParam() {
        search = SearchBarUnify(this)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        search.iconDrawable = null
        search.showIcon = false
        search.searchBarTextField.setText(intent.getStringExtra(SEARCH_QUERY))
        search.layoutParams = param
    }

    private fun setHeader() {
        val wrapper = LinearLayout(this)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        wrapper.layoutParams = param
        wrapper.addView(search)
        header_toolbar?.customView(wrapper)
        header_toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun onSelectedItem() {
        selected_info.text = String.format(getString(R.string.format_selected_keyword), adapter.getSelectedItem().size)
    }

    private fun fetchData() {
        adapter.items.clear()
        txtError.text = validateKeyword(search.searchBarTextField.text.toString().trim())
        if (txtError.text.isNotEmpty()) {
            setEmpty(true)
            txtError.visibility = View.VISIBLE
            onSelectedItem()
        } else {
            txtError.visibility = View.GONE
        }
        viewModel.searchKeyword(search.searchBarTextField.text.toString(), intent?.getStringExtra(PRODUCT_IDS_SELECTED)
                ?: "", ::onSuccessSearch)
    }

    private fun validateKeyword(text: CharSequence?): CharSequence? {
        return if (!text.isNullOrBlank() && text.split(" ").size > 5) {
            getString(R.string.error_max_length_keyword)
        } else if (!text.isNullOrBlank() && !text.matches(Constants.REGEX.toRegex())) {
            getString(R.string.error_keyword)
        } else if (text!!.length > Constants.COUNT) {
            getString(R.string.error_max_length)
        } else {
            null
        }
    }

    private fun onSuccessSearch(data: List<SearchData>) {
        data.forEach {
            adapter.items.add(it)
        }
        setEmpty(data.isEmpty())
        adapter.notifyDataSetChanged()
    }

    private fun setEmpty(setEmpty: Boolean) {
        if (setEmpty) {
            tip_btn.visibility = View.GONE
            headlineList.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        } else {
            tip_btn.visibility = View.VISIBLE
            headlineList.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }
}