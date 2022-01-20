package com.tokopedia.digital_product_detail.presentation.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPPulsaFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.toBitmap

/**
 * @author by firmanda on 04/01/21
 *
 * access internal applink tokopedia-android-internal://recharge/pdp_pulsa
 */


class DigitalPDPPulsaActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

    lateinit var menuPdp: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAppBar()
    }

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
            val iconUnify = getIconUnifyDrawable(
                this,
                IconUnify.LIST_TRANSACTION,
                ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            )
            iconUnify?.toBitmap()?.let {
                menuPdp.getItem(0).icon = BitmapDrawable(
                    resources,
                    Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true))

            }
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_order_list_menu -> {
                navigateToOrderList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAppBar() {
        (toolbar as HeaderUnify).transparentMode = true
    }

    private fun navigateToOrderList() {
        RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
    }

    companion object {
        private const val TOOLBAR_ICON_SIZE = 64
    }
}