package com.tokopedia.topchat.chatlist.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.fragment.ChatSearchFragment
import kotlinx.android.synthetic.main.activity_chat_search.*

/**
 * @author by steven on 14/08/19.
 * For navigate: use {@link ApplinkConstInternalMarketplace.CHAT_SEARCH}
 */
class ChatSearchActivity : BaseSimpleActivity() {

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
            requestFocus()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
            if (query == null) return
            ivClear?.showWithCondition(query.isNotEmpty())
        }
    }

    override fun onDestroy() {
        searchTextView?.removeTextChangedListener(textWatcher)
        super.onDestroy()
    }
}
