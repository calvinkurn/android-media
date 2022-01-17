package com.tokopedia.digital_product_detail.presentation.activity

import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPPulsaFragment
import com.tokopedia.header.HeaderUnify

/**
 * @author by firmanda on 04/01/21
 *
 * access internal applink tokopedia-android-internal://recharge/pdp_pulsa
 */


class DigitalPDPPulsaActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

    lateinit var menuPdp: Menu

    override fun getComponent(): DigitalPDPComponent {
        return DaggerDigitalPDPComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): Fragment? {
        return DigitalPDPPulsaFragment.newInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_pdp
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.pdp_toolbar
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuPdp = menu
            menuInflater.inflate(R.menu.menu_pdp, menu)
            return true
        }
        return false
    }

    fun setupAppBar() {
        (toolbar as HeaderUnify).transparentMode = true
        if (::menuPdp.isInitialized) {
            menuPdp.getItem(0).icon = ContextCompat.getDrawable(this@DigitalPDPPulsaActivity,
                com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_white)
        }
    }
}