package com.tokopedia.abstraction.base.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.utils.view.MenuTintUtils;

/**
 * Created by nathan on 7/11/17.
 */

abstract class BaseToolbarActivity extends BaseActivity {

    private final static int TEXT_COLOR_BACKGROUND_WHITE = R.color.black;
    protected Toolbar toolbar;

    protected abstract void setupFragment(Bundle savedInstanceState);

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setupLayout(savedInstanceState);
        setupFragment(savedInstanceState);
        setupActionBarHomeIndicatorIcon();
    }

    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green_600));
        }
    }

    private void setupActionBarHomeIndicatorIcon() {
        if (getSupportActionBar() != null && isShowCloseButton()) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default));
        }
    }

    protected boolean isShowCloseButton() {
        return false;
    }

    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
        toolbar = (Toolbar) findViewById(getToolbarId());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
        }
    }

    protected int getToolbarId() {
        return R.id.toolbar;
    }

    public void updateOptionMenuColorWhite(Menu menu) {
        MenuTintUtils.tintAllIcons(menu, TEXT_COLOR_BACKGROUND_WHITE);
    }

    public void updateTitle(String title) {
        updateTitle(title, null);
    }

    public void updateTitle(String title, String subTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (TextUtils.isEmpty(subTitle)) {
            subTitle = "";
        }
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}