package com.tokopedia.entertainment.search.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntSearchDetailActivityBinding
import com.tokopedia.entertainment.search.Link
import com.tokopedia.entertainment.search.di.DaggerEventSearchComponent
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.fragment.EventCategoryFragment
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Author errysuprayogi on 27,February,2020
 */
class EventCategoryActivity : BaseSimpleActivity(), HasComponent<EventSearchComponent> {

    var INIT_QUERY_TEXT: String = ""
    var INIT_CITY_ID: String = ""
    var INIT_CATEGORY: String = ""
    var bundle: Bundle? = Bundle.EMPTY
    var uriString: String? = ""
    var uri: Uri = Uri.EMPTY

    var binding: EntSearchDetailActivityBinding? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EntSearchDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        bundle = intent.extras
        uriString = intent.dataString

        INIT_QUERY_TEXT = parseQuery("query_text")
        INIT_CITY_ID = parseQuery("id_city")
        INIT_CATEGORY = parseQuery("category_id")

        binding?.txtSearch?.run {
            searchBarTextField.inputType = Int.ZERO
            searchBarTextField.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> openSearchActivity()
                }
                return@setOnTouchListener true
            }
            searchBarIcon.setOnClickListener {
                searchBarTextField.text.clear()
                INIT_QUERY_TEXT = ""
            }
            searchBarTextField.setText(INIT_QUERY_TEXT)
        }
    }

    private fun parseQuery(text_to_find: String): String{
        var text: String? = ""
        if(bundle != null){
            if(bundle?.getString(text_to_find) != null){
                text= bundle?.getString(text_to_find, "") ?: ""
                return text
            }
        }

        uri = Uri.parse(uriString)
        text = uri.getQueryParameter(text_to_find)

        return text ?: ""
    }

    private fun openSearchActivity(): Boolean {
        val intent = RouteManager.getIntent(this, Link.EVENT_SEARCH + "?query_text={query_text}", getQueryText())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        return true
    }

    override fun getNewFragment(): Fragment? {
        return EventCategoryFragment.newInstance()
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun getLayoutRes(): Int {
        return R.layout.ent_search_detail_activity
    }

    fun getQueryText(): String = INIT_QUERY_TEXT

    fun getCityId(): String = INIT_CITY_ID

    fun getCategoryId(): String = INIT_CATEGORY

    override fun getComponent(): EventSearchComponent = DaggerEventSearchComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

}
