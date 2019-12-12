package com.tokopedia.topchat.chatsearch.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.di.ChatSearchComponent
import com.tokopedia.topchat.chatsearch.di.DaggerChatSearchComponent
import com.tokopedia.topchat.chatsearch.view.fragment.ChatSearchFragment
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
class ChatSearchActivity : BaseSimpleActivity(),
        HasComponent<ChatSearchComponent>, CoroutineScope {

    private val textDebounce = 200L
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    interface Listener {
        fun onSearchQueryChanged(query: String)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        intent.extras?.let {
            bundle.putAll(it)
        }
        return ChatSearchFragment.createFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search)
        useLightNotificationBar()
        setupToolbar()
    }

    override fun getComponent(): ChatSearchComponent {
        return DaggerChatSearchComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    private fun setupToolbar() {
        assignBackButtonClick()
        assignClearButtonClick()
        initSearchLayout()
    }

    private fun assignBackButtonClick() {
        ivBack?.setOnClickListener {
            finish()
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
}
