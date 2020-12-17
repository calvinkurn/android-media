package com.tokopedia.entertainment.pdp.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.di.DaggerEventPDPComponent
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.fragment.EventPDPTicketFragment

class EventPDPTicketActivity: BaseSimpleActivity(), HasComponent<EventPDPComponent> {
    private var urlPDP = ""
    private var startDate = ""
    private var selectedDate = ""
    private var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if(uri != null){
            urlPDP = uri.lastPathSegment ?: ""
            startDate = uri.getQueryParameter("startDate") ?: ""
            selectedDate = uri.getQueryParameter("selectedDate") ?: ""
            endDate = uri.getQueryParameter("endDate") ?: ""
        }else if(savedInstanceState != null){
            urlPDP = savedInstanceState.getString(EXTRA_URL_PDP,"")
        } else if(intent.extras!=null){
            urlPDP = intent.getStringExtra(EXTRA_URL_PDP)
            startDate = intent.getStringExtra(START_DATE)
            selectedDate = intent.getStringExtra(SELECTED_DATE)
            endDate = intent.getStringExtra(END_DATE)
        }
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getLayoutRes(): Int {
        return R.layout.ent_ticket_listing_activity
    }

    override fun getNewFragment(): Fragment?  = EventPDPTicketFragment.newInstance(urlPDP, selectedDate, startDate, endDate)

    override fun getComponent(): EventPDPComponent = DaggerEventPDPComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    companion object{
        const val EXTRA_URL_PDP= "EXTRA_URL_PDP"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val SELECTED_DATE = "selectedDate"
    }
}