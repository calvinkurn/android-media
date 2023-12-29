package com.tokopedia.entertainment.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntSearchActivityBinding
import com.tokopedia.entertainment.search.analytics.EventSearchPageTracking
import com.tokopedia.entertainment.search.di.DaggerEventSearchComponent
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.fragment.EventSearchFragment
import com.tokopedia.entertainment.search.listener.EventSearchBarActionListener
import com.tokopedia.entertainment.search.listener.EventSearchBarDataListener

/**
 * Author errysuprayogi on 27,February,2020
 */
class EventSearchActivity : BaseSimpleActivity(), HasComponent<EventSearchComponent>, EventSearchBarDataListener {

    companion object {
        val EXTRAS_QUERY = "query_text"
    }

    var binding: EntSearchActivityBinding? = null
    var eventSearchBarActionListener: EventSearchBarActionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EntSearchActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding?.txtSearch?.run {
            searchBarTextField.setText(getExtrasQuery())
            searchBarTextField.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                searchBarTextField.post(Runnable {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(searchBarTextField, InputMethodManager.SHOW_IMPLICIT)
                })
            }
            searchBarTextField.requestFocus()
            searchBarTextField.setOnClickListener { EventSearchPageTracking.getInstance().clickSearchBarOnSearchActivity() }
            searchBarTextField.setOnEditorActionListener { view, i, keyEvent ->
                if(i == EditorInfo.IME_ACTION_SEARCH || keyEvent.action == KeyEvent.KEYCODE_ENTER){
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            searchBarTextField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        eventSearchBarActionListener?.afterSearchBarTextChanged(keyword)
                    }
                })
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
        val fragment = EventSearchFragment.newInstance()
        fragment.setListener(this)
        return fragment
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun getKeyWord(): Editable? {
        return binding?.txtSearch?.searchBarTextField?.text
    }
}
