package com.tokopedia.review.feature.inbox.buyerreview.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.text.TextDrawable;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFilterFragment;
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFragment;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterActivity extends BaseSimpleActivity {

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

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        String timeFilter = getIntent().getStringExtra(InboxReputationFilterFragment
                .SELECTED_TIME_FILTER);
        String statusFilter = getIntent().getStringExtra(InboxReputationFilterFragment
                .SELECTED_SCORE_FILTER);
        int tab = getIntent().getIntExtra(InboxReputationFragment
                .PARAM_TAB, -1);

        Fragment fragment = InboxReputationFilterFragment.createInstance(timeFilter, statusFilter, tab);
        listener = (InboxReputationFilterFragment) fragment;

        return fragment;
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
        drawable.setText(getResources().getString(R.string.reset_title));
        drawable.setTextColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68);
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset
                && listener != null) {
            listener.resetFilter();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        toolbar.setPadding(0,0,20,0);
    }
}
