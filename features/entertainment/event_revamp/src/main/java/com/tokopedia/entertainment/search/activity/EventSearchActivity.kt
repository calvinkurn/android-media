package com.tokopedia.entertainment.search.activity

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.analytics.EventSearchPageTracking
import com.tokopedia.entertainment.search.di.DaggerEventSearchComponent
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.fragment.EventSearchFragment
import kotlinx.android.synthetic.main.ent_search_activity.*


/**
 * Author errysuprayogi on 27,February,2020
 */
class EventSearchActivity : BaseSimpleActivity(), HasComponent<EventSearchComponent> {

    companion object {
        val EXTRAS_QUERY = "query_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txt_search.searchBarTextField.setText(getExtrasQuery())
        txt_search.searchBarTextField.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            txt_search.searchBarTextField.post(Runnable {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(txt_search.searchBarTextField, InputMethodManager.SHOW_IMPLICIT)
            })
        }
        txt_search.searchBarTextField.requestFocus()
        txt_search.searchBarTextField.setOnClickListener { EventSearchPageTracking.getInstance().clickSearchBarOnSearchActivity() }
        txt_search.searchBarTextField.setOnEditorActionListener { view, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH || keyEvent.action == KeyEvent.KEYCODE_ENTER){
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun getExtrasQuery(): String {
        val query = intent.getStringExtra(EXTRAS_QUERY)
        if (query != null && !query.contains(EXTRAS_QUERY)) {
            return query
        } else {
            return ""
        }
    }

    override fun getNewFragment(): Fragment? {
        return EventSearchFragment.newInstance()
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getLayoutRes(): Int {
        return R.layout.ent_search_activity
    }

    override fun getComponent(): EventSearchComponent = DaggerEventSearchComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

}