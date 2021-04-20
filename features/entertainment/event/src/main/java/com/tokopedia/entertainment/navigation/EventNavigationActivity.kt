package com.tokopedia.entertainment.navigation

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.di.DaggerEventHomeComponent
import com.tokopedia.entertainment.home.di.EventHomeComponent

class EventNavigationActivity : BaseSimpleActivity(), HasComponent<EventHomeComponent> {

    override fun getComponent(): EventHomeComponent = DaggerEventHomeComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_navigation_event
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_event_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId ?: "" == R.id.action_overflow_menu) {
            onClickShare()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    interface PDPListener {
        fun shareLink()
    }

    fun onClickShare() {
        if (fragment is PDPListener) {
            (fragment as PDPListener).shareLink()
        }
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }
}