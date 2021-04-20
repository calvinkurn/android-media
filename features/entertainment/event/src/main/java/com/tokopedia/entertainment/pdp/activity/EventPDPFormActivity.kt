package com.tokopedia.entertainment.pdp.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.di.DaggerEventPDPComponent
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.fragment.EventPDPFormFragment

class EventPDPFormActivity: BaseSimpleActivity(), HasComponent<EventPDPComponent>{
    private var urlPDP = ""

    lateinit var toolbarForm : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if(uri != null){
            urlPDP = uri.lastPathSegment ?: ""
        }else if(savedInstanceState != null){
            urlPDP = savedInstanceState.getString(EXTRA_URL_PDP,"")
        }
        super.onCreate(savedInstanceState)
        toolbarForm = toolbar
    }

    override fun getNewFragment(): Fragment?  = EventPDPFormFragment.newInstance(urlPDP)

    override fun getComponent(): EventPDPComponent = DaggerEventPDPComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    companion object{
        const val EXTRA_URL_PDP= "EXTRA_URL_PDP"
        const val EXTRA_ADDITIONAL_DATA = "EXTRA_ADDITIONAL_DATA"
    }
}