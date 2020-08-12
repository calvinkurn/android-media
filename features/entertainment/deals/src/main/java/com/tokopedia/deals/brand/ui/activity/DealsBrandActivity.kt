package com.tokopedia.deals.brand.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.di.component.DaggerDealsBrandComponent
import com.tokopedia.deals.brand.di.component.DealsBrandComponent
import com.tokopedia.deals.brand.listener.DealsBrandSearchTabListener
import com.tokopedia.deals.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.ui.activity.DealsBaseBrandCategoryActivity
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.unifycomponents.SearchBarUnify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DealsBrandActivity: DealsBaseBrandCategoryActivity(), HasComponent<DealsBrandComponent>, CoroutineScope {

    private val listeners: ArrayList<DealsBrandSearchTabListener> = arrayListOf()
    private var searchBarContainer: ConstraintLayout? = null
    private var searchBar: SearchBarUnify? = null
    var userTyped = false
    var searchNotFound = false

    @Inject
    lateinit var analytics: DealsAnalytics

    override fun isSearchAble(): Boolean = true

    override fun getComponent(): DealsBrandComponent {
        return DaggerDealsBrandComponent.builder()
                .dealsComponent(getDealsComponent())
                .build()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        uri?.let {
            if (!uri.getQueryParameter(DealsCategoryActivity.PARAM_CATEGORY_ID).isNullOrEmpty()) {
                intent.putExtra(DealsCategoryActivity.EXTRA_CATEGORY_ID, uri.getQueryParameter(DealsCategoryActivity.PARAM_CATEGORY_ID))
            }
        }

        super.onCreate(savedInstanceState)
        initInjector()
        searchBarContainer = findViewById(R.id.container_search_bar)
        searchBar = searchBarContainer?.findViewById(R.id.searchBarDealsBaseSearch)
        setKeywordFromPreviousPage()
        setupListener()
    }

    private fun setKeywordFromPreviousPage() {
        val keyword = intent.getStringExtra(DealsSearchConstants.KEYWORD_EXTRA)
        searchBar?.searchBarTextField?.setText(keyword)
    }

    @Synchronized
    fun registerDataUpdateListener(listener: DealsBrandSearchTabListener) {
        listeners.add(listener)
    }

    @Synchronized
    fun unregisterDataUpdateListener(listener: DealsBrandSearchTabListener) {
        listeners.remove(listener)
    }

    @Synchronized
    fun queryUpdated() {
        for (listener in listeners) {
            listener.onTextChanged()
        }
    }

    @Synchronized
    fun userTyping() {
        for (listener in listeners) {
            listener.onUserType()
        }
    }

    private fun setupListener() {
        searchBar?.searchBarTextField?.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            KeyboardHandler.showSoftKeyboard(this)
            if(this::analytics.isInitialized) {
                analytics.eventClickSearchBrandPage()
            }
        }
        searchBar?.searchBarTextField?.afterTextChangedDelayed {
            onSearchTextChanged()
        }
        searchBar?.searchBarTextField?.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH || keyEvent.action == KeyEvent.KEYCODE_ENTER){
                onSearchSubmitted()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun getSearchKeyword(): String {
        var query = ""
        if(searchBar?.searchBarTextField?.text?.isNotEmpty() == true) {
            query = searchBar?.searchBarTextField?.text.toString()
        }
        return query
    }

    private fun onSearchSubmitted() {
        KeyboardHandler.hideSoftKeyboard(this)
        queryUpdated()
    }

    private fun onSearchTextChanged() {
        queryUpdated()
    }

    private fun TextView.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor && searchText.isNotEmpty()) return
                searchFor = searchText
                launch {
                    userTyping()
                    delay(DELAY)
                    userTyped = searchText.isNotEmpty()
                    searchNotFound = false
                    if(!userTyped) {
                        queryUpdated()
                    } else {
                        afterTextChanged.invoke(searchText)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        })
    }

    override fun getPageTAG(): String = TAG?: "DealsBrandActivity"
    override fun showAllItemPage(): Boolean = true
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun tabAnalytics(categoryName: String, position: Int) {
        if(this::analytics.isInitialized) {
            analytics.eventClickCategoryTabBrandPage(categoryName, position)
        }
    }

    companion object {
        val TAG = DealsBrandActivity::class.simpleName
        private const val DELAY: Long = 1000

        fun getCallingIntent(context: Context, keyword: String?, categoryId: String? = null): Intent {
            val intent = Intent(context, DealsBrandActivity::class.java)
            if(keyword != null) {
                intent.putExtra(DealsSearchConstants.KEYWORD_EXTRA, keyword)
            }
            intent.putExtra(DealsCategoryActivity.EXTRA_CATEGORY_ID, categoryId)
            return intent
        }
    }
}