package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailPassModel;

import javax.inject.Inject;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailActivity extends BasePresenterActivity implements HasComponent {

    public static final String ARGS_POSITION = "ARGS_POSITION";
    public static final String ARGS_TAB = "ARGS_TAB";
    public static final String ARGS_IS_FROM_APPLINK = "ARGS_IS_FROM_APPLINK";
    public static final String REPUTATION_ID = "reputation_id";
    public static final String CACHE_PASS_DATA = InboxReputationDetailActivity.class.getName() + "-passData";

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        int tab = -1;
        boolean isFromApplink = false;
        String reputationId = "0";
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getInt(ARGS_TAB, -1) != -1) {
                tab = getIntent().getExtras().getInt(ARGS_TAB);
            }
            isFromApplink = getIntent().getExtras().getBoolean(ARGS_IS_FROM_APPLINK, false);
            reputationId = getIntent().getExtras().getString(REPUTATION_ID, "");
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(InboxReputationDetailFragment
                .class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationDetailFragment.createInstance(tab, isFromApplink, reputationId);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getCallingIntent(Context context,
                                          int adapterPosition, int tab) {
        Intent intent = new Intent(context, InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(InboxReputationDetailActivity.ARGS_POSITION, adapterPosition);
        bundle.putInt(InboxReputationDetailActivity.ARGS_TAB, tab);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink(ApplinkConst.REPUTATION_DETAIL)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        extras.putBoolean(ARGS_IS_FROM_APPLINK, true);

        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, InboxReputationDetailActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_700));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey_500));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_action_back);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700),
                    PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }
}
