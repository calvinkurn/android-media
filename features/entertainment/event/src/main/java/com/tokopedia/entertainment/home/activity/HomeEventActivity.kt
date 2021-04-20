package com.tokopedia.entertainment.home.activity

import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.di.DaggerEventHomeComponent
import com.tokopedia.entertainment.home.di.EventHomeComponent
import com.tokopedia.entertainment.home.fragment.EventHomeFragment
import kotlinx.android.synthetic.main.ent_home_activity.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class HomeEventActivity : BaseSimpleActivity(), HasComponent<EventHomeComponent> {
    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txt_search.searchBarTextField.inputType = 0
        txt_search.searchBarTextField.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> openEventSearch()
            }
            return@setOnTouchListener true
        }
    }

    private fun openEventSearch(): Boolean {
        RouteManager.route(this, ApplinkConstInternalEntertainment.EVENT_SEARCH)
        return true
    }

    override fun getComponent(): EventHomeComponent = DaggerEventHomeComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getLayoutRes(): Int {
        return R.layout.ent_home_activity
    }
}