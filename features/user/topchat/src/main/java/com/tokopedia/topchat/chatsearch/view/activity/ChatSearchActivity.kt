package com.tokopedia.topchat.chatsearch.view.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.di.ChatSearchComponent
import com.tokopedia.topchat.chatsearch.di.DaggerChatSearchComponent
import com.tokopedia.topchat.chatsearch.view.fragment.ChatSearchFragment
import com.tokopedia.topchat.chatsearch.view.fragment.ChatSearchFragmentListener
import com.tokopedia.topchat.chatsearch.view.fragment.ContactLoadMoreChatFragment
import com.tokopedia.topchat.chatsearch.view.fragment.ContactLoadMoreChatListener
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.activity_chat_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author by steven on 14/08/19.
 * For navigate: use {@link ApplinkConstInternalMarketplace.CHAT_SEARCH}
 */
open class ChatSearchActivity : BaseSimpleActivity(), HasComponent<ChatSearchComponent>,
        CoroutineScope, ChatSearchFragmentListener, ContactLoadMoreChatListener {

    private val textDebounce = 500L
    private var containerSearch: RelativeLayout? = null
    private var txtToolbarTittle: Typography? = null
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    interface Listener {
        fun onSearchQueryChanged(query: String)
    }

    override fun getNewFragment() = ChatSearchFragment.createFragment()
    override fun getParentViewResourceID() = R.id.parent_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search)
        bindView()
        initWindowBackground()
        useLightNotificationBar()
        setupToolbar()
    }

    private fun bindView() {
        containerSearch = findViewById(R.id.container_search)
        txtToolbarTittle = findViewById(R.id.txt_toolbar_tittle)
    }

    override fun onClickContactLoadMore(query: String, firstPageContacts: GetChatSearchResponse) {
        KeyboardHandler.hideSoftKeyboard(this)
        val loadMoreContactDetail = ContactLoadMoreChatFragment.create(query, firstPageContacts)
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(parentViewResourceID, loadMoreContactDetail, "ContactLoadMoreChatFragment")
                .commit()
    }

    override fun getComponent(): ChatSearchComponent {
        return DaggerChatSearchComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    @SuppressLint("SetTextI18n")
    override fun changeToolbarTitle(tittle: String) {
        containerSearch?.hide()
        txtToolbarTittle?.show()
        txtToolbarTittle?.text = "\"$tittle\""
    }

    override fun showSearchBar() {
        containerSearch?.show()
        txtToolbarTittle?.hide()
    }

    override fun hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(this)
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

    private fun setupToolbar() {
        assignBackButtonClick()
        assignClearButtonClick()
        initSearchLayout()
    }

    private fun assignBackButtonClick() {
        ivBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun assignClearButtonClick() {
        ivClear?.setOnClickListener {
            searchTextView?.text?.clear()
        }
    }

    private fun initSearchLayout() {
        searchTextView?.apply {
            addTextChangedListener(textWatcher)
            setOnEditorActionListener(editorActionListener)
            requestFocus()
        }
    }

    private val textWatcher = object : TextWatcher {
        private var searchFor = ""
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
            if (query == null) return
            ivClear?.showWithCondition(query.isNotEmpty())
            val searchText = query.toString().trim()
            if (searchText == searchFor) return
            searchFor = searchText
            launch {
                delay(textDebounce)
                if (searchText != searchFor) {
                    return@launch
                }
                notifyQueryChanged(searchFor)
            }
        }
    }

    private val editorActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        notifyQueryChanged(v.text.toString())
        true
    }

    private fun notifyQueryChanged(query: String) {
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is Listener) {
                fragment.onSearchQueryChanged(query)
            }
        }
    }

    override fun onDestroy() {
        searchTextView?.removeTextChangedListener(textWatcher)
        super.onDestroy()
    }

    override fun onClickChangeKeyword() {
        searchTextView?.requestFocus()
        searchTextView?.performClick()
        KeyboardHandler.showSoftKeyboard(this)
    }
}
