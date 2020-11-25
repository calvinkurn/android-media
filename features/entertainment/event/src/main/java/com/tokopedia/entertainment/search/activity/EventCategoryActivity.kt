package com.tokopedia.entertainment.search.activity

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
import com.tokopedia.entertainment.search.Link
import com.tokopedia.entertainment.search.di.DaggerEventSearchComponent
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.fragment.EventCategoryFragment
import kotlinx.android.synthetic.main.ent_search_activity.*
import kotlinx.android.synthetic.main.ent_search_detail_activity.*
import kotlinx.android.synthetic.main.ent_search_detail_activity.txt_search
import timber.log.Timber

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = intent.extras
        uriString = intent.dataString

        INIT_QUERY_TEXT = parseQuery("query_text")
        INIT_CITY_ID = parseQuery("id_city")
        INIT_CATEGORY = parseQuery("category_id")

        txt_search.searchBarTextField.inputType = 0
        txt_search.searchBarTextField.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> openSearchActivity()
            }
            return@setOnTouchListener true
        }
        txt_search.searchBarIcon.setOnClickListener {
            txt_search.searchBarTextField.text.clear()
            INIT_QUERY_TEXT = ""
        }
    }

    private fun parseQuery(text_to_find: String): String{
        var text: String? = ""
        if(bundle != null){
            if(bundle!!.getString(text_to_find) != null){
                text= bundle!!.getString(text_to_find, "")
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

    override fun getLayoutRes(): Int {
        return R.layout.ent_search_detail_activity
    }

    fun getQueryText(): String? = INIT_QUERY_TEXT

    fun getCityId(): String? = INIT_CITY_ID

    fun getCategoryId(): String? = INIT_CATEGORY

    override fun getComponent(): EventSearchComponent = DaggerEventSearchComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

}