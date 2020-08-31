package com.tokopedia.topads.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.sheet.TipSheetKeywordList
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.adapter.keyword.KeywordSearchAdapter
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment.Companion.PRODUCT_IDS_SELECTED
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment.Companion.SEARCH_QUERY
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment.Companion.SELECTED_KEYWORDS
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_keyword_search_layout.*
import javax.inject.Inject

private const val CLICK_MANUAL_SEARCH = "click - ceklist rekomendasi kata kunci manual search"
private const val EVENT_CLICK_MANUAL_SEARCH = "kata kunci terpilih yang di ceklist"
private const val CLICK_SUBMIT_BUTT = "'click - pilih kata kunci"
private const val EVENT_CLICK_SUBMIT_BUTT = "kata kunci terpilih yang di ceklist"

class KeywordSearchActivity : BaseActivity(), HasComponent<CreateAdsComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var adapter: KeywordSearchAdapter
    private lateinit var search: SearchBarUnify
    private var userID: String = ""
    private var shopID = ""

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun initInjector() {
        DaggerCreateAdsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = UserSession(this).userId
        shopID = UserSession(this).shopId
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initInjector()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(KeywordAdsViewModel::class.java)
        adapter = KeywordSearchAdapter(::onCheckedItem)
        setContentView(R.layout.topads_create_keyword_search_layout)
        setSearchBar()
        fetchData()
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(this)
        tip_btn.setOnClickListener {
            TipSheetKeywordList().show(supportFragmentManager, KeywordAdsListFragment::class.java.name)
        }
        btn_next.setOnClickListener {
            val eventLabel = "$shopID - $EVENT_CLICK_SUBMIT_BUTT"
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SUBMIT_BUTT, eventLabel, userID)
            val returnIntent = Intent()
            returnIntent.putParcelableArrayListExtra(SELECTED_KEYWORDS, ArrayList(adapter.getSelectedItem()))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun setSearchBar() {
        setSearchParam()
        setHeader()
    }

    private fun setSearchParam() {
        search = SearchBarUnify(this)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        search.iconDrawable = null
        search.showIcon = false
        search.searchBarTextField.setText(intent.getStringExtra(SEARCH_QUERY))
        search.layoutParams = param
        val searchTextField = search.searchBarTextField
        searchTextField.hint = getString(R.string.topads_common_search_hint_activity)
        Utils.setSearchListener(search, this, rootView, ::fetchData)
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
            viewModel.searchKeyword(search.searchBarTextField.text.toString(), intent?.getStringExtra(PRODUCT_IDS_SELECTED)
                    ?: "", ::onSuccessSearch)
        }
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

    private fun onSuccessSearch(data: List<SearchData>) {
        data.forEach {
            adapter.items.add(it)
        }
        setEmpty(data.isEmpty())
        onSelectedItem()
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

    private fun onCheckedItem() {
        val eventLabel = "$shopID - $EVENT_CLICK_MANUAL_SEARCH"
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_MANUAL_SEARCH, eventLabel, userID)
        onSelectedItem()
    }
}


