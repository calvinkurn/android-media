package com.tokopedia.affiliate.feature.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.constant.AffiliateConstant;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.DashboardFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;

/**
 * @author by yfsx on 13/09/18.
 */
public class DashboardActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.AFFILIATE_DASHBOARD)
    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return DashboardFragment.getInstance(getIntent().getExtras());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.action_help, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_help); // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(MethodChecker.getDrawable(this, R.drawable.ic_bantuan));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            navigateToHelp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToHelp() {
        RouteManager.route(
                this,
                String.format("%s?url=%s", ApplinkConst.WEBVIEW, AffiliateConstant.FAQ_URL)
        );
    }
}
