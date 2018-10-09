package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFilterFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterActivity extends BasePresenterActivity {

    public interface ResetListener {
        void resetFilter();
    }

    ResetListener listener;

    public static Intent createIntent(Context context, String timeFilter,
                                      String scoreFilter,
                                      int tab) {
        Intent intent = new Intent(context, InboxReputationFilterActivity.class);
        intent.putExtra(InboxReputationFilterFragment.SELECTED_TIME_FILTER, timeFilter);
        intent.putExtra(InboxReputationFilterFragment.SELECTED_SCORE_FILTER, scoreFilter);
        intent.putExtra(InboxReputationFragment.PARAM_TAB, tab);
        return intent;
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.action_reset, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_reset);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(getResetMenu());
        return true;
    }

    private Drawable getResetMenu() {
        TextDrawable drawable = new TextDrawable(this);
        drawable.setText(getResources().getString(R.string.action_reset));
        drawable.setTextColor(R.color.black_70b);
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset
                && listener != null) {
            listener.resetFilter();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    protected void initView() {
        String timeFilter = getIntent().getStringExtra(InboxReputationFilterFragment
                .SELECTED_TIME_FILTER);
        String statusFilter = getIntent().getStringExtra(InboxReputationFilterFragment
                .SELECTED_SCORE_FILTER);
        int tab = getIntent().getIntExtra(InboxReputationFragment
                .PARAM_TAB, -1);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxReputationFilterFragment
                        .class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationFilterFragment.createInstance(timeFilter, statusFilter, tab);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

        listener = (InboxReputationFilterFragment) fragment;
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setPadding(0,0,20,0);
    }
}
