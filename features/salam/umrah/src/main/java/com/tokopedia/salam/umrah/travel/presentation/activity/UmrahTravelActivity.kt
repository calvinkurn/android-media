package com.tokopedia.salam.umrah.travel.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.common.presentation.activity.UmrahBaseActivity
import com.tokopedia.salam.umrah.travel.di.DaggerUmrahTravelComponent
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelFragment

/**
 * @author by Firman on 22/1/20
 */

class UmrahTravelActivity : UmrahBaseActivity(), HasComponent<UmrahTravelComponent>{
    private var slugName: String = ""

    override fun getMenuButton(): Int = R.menu.umrah_base_menu
    override fun onClickShare() {
        if (fragment is TravelListener) {
            (fragment as TravelListener).shareTravelLink()
        }
    }

    override fun onClickSalam() {
        if (fragment is TravelListener) {
            (fragment as TravelListener).clickSalam()
        }
    }

    override fun onClickHelp() {
        if (fragment is TravelListener) {
            (fragment as TravelListener).clickHelp()
        }
    }

    override fun shouldShowMenuWhite(): Boolean = false

    override fun getNewFragment(): Fragment?= UmrahTravelFragment.getInstance(slugName)

    override fun getComponent(): UmrahTravelComponent =
            DaggerUmrahTravelComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if(uri != null){
            slugName = uri.lastPathSegment ?: ""
        }else if(savedInstanceState != null){
            slugName = savedInstanceState.getString(EXTRA_SLUG_NAME,"")
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SLUG_NAME, slugName)
    }
    companion object {
        const val EXTRA_SLUG_NAME = "EXTRA_SLUG_NAME"

        fun createIntent(context: Context, slugName:String): Intent =
                Intent(context, UmrahTravelActivity::class.java)
                        .putExtra(EXTRA_SLUG_NAME, slugName)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragment is TravelListener) {
            (fragment as TravelListener).onBackPressed()
        }
    }

    interface TravelListener {
        fun onBackPressed()
        fun shareTravelLink()
        fun clickHelp()
        fun clickSalam()
    }
}