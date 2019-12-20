package com.tokopedia.salam.umrah.pdp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.common.presentation.activity.UmrahBaseActivity
import com.tokopedia.salam.umrah.pdp.di.DaggerUmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.di.UmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.presentation.fragment.UmrahPdpFragment


/**
 * @author by M on 30/10/19
 */
class UmrahPdpActivity : UmrahBaseActivity(), HasComponent<UmrahPdpComponent> {
    private var slugName: String = ""

    override fun getComponent(): UmrahPdpComponent = DaggerUmrahPdpComponent.builder()
            .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
            .build()

    override fun getNewFragment(): Fragment? = UmrahPdpFragment.getInstance(slugName)

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getMenuButton() = R.menu.umrah_base_menu_white
    override fun getShareLink(): String = getString(R.string.umrah_pdp_link_share,slugName)

    private fun getIntentData() {
        val uri = intent.data
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        slugName = if (uri != null) uri.lastPathSegment
        else intent.getStringExtra(EXTRA_SLUG_NAME)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragment is OnBackListener) {
            (fragment as OnBackListener).onBackPressed()
        }
    }

    interface OnBackListener {
        fun onBackPressed()
    }

    companion object {
        const val EXTRA_SLUG_NAME = "EXTRA_SLUG_NAME"
        const val EXTRA_VARIANT_ROOM = "EXTRA_VARIANT_ROOM"
        const val EXTRA_TOTAL_PASSENGER = "EXTRA_TOTAL_PASSENGER"
        const val EXTRA_MAX_PASSENGER = "EXTRA_MAX_PASSENGER"
        const val EXTRA_TOTAL_PRICE = "EXTRA_TOTAL_PRICE"
        const val EXTRA_IS_EMPTY = "EXTRA_IS_EMPTY"
        const val REQUEST_PDP_DETAIL = 0x11

        fun createIntent(context: Context, slugName: String): Intent =
                Intent(context, UmrahPdpActivity::class.java)
                        .putExtra(EXTRA_SLUG_NAME, slugName)
    }
}