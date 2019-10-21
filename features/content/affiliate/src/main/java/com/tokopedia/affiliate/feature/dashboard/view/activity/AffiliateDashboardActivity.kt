package com.tokopedia.affiliate.feature.dashboard.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.constant.AffiliateConstant
import com.tokopedia.affiliate.feature.dashboard.view.fragment.AffiliateDashboardFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

/**
 * Created by jegul on 2019-09-05.
 */
class AffiliateDashboardActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return AffiliateDashboardFragment.newInstance(intent.extras ?: Bundle.EMPTY)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, R.id.action_help, 0, "")
        val menuItem = menu.findItem(R.id.action_help) // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = MethodChecker.getDrawable(this, R.drawable.ic_info_help)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_help) {
            navigateToHelp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToHelp() {
        RouteManager.route(
                this,
                String.format("%s?url=%s", ApplinkConst.WEBVIEW, AffiliateConstant.FAQ_URL)
        )
    }

    object DeeplinkIntent {

        @DeepLink(ApplinkConst.AFFILIATE_DASHBOARD)
        @JvmStatic
        fun getInstance(context: Context): Intent {
            val intent = Intent(context, AffiliateDashboardActivity::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            return intent
        }
    }
}