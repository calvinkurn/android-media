package com.tokopedia.entertainment.pdp.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.di.DaggerEventPDPComponent
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.fragment.EventPDPFragment

/**
 * Author firman on 06-04-20
 */

class EventPDPActivity : BaseSimpleActivity(), HasComponent<EventPDPComponent> {

    private var urlPDP: String = ""

    override fun getNewFragment(): Fragment? = EventPDPFragment.newInstance(urlPDP)

    override fun getComponent(): EventPDPComponent = DaggerEventPDPComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            urlPDP = uri.lastPathSegment ?: ""
        } else if (savedInstanceState != null) {
            urlPDP = savedInstanceState.getString(EXTRA_URL_PDP, "")
        } else if (intent.extras != null) {
            urlPDP = intent.getStringExtra(EXTRA_URL_PDP)
        }
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    fun onClickShare() {
        if (fragment is PDPListener) {
            (fragment as PDPListener).shareLink()
        }
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

    companion object {
        const val EXTRA_URL_PDP = "EXTRA_URL_PDP"
    }

    interface PDPListener {
        fun shareLink()
    }
}
