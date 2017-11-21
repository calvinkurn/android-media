package com.tokopedia.abstraction.base.view.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.utils.MenuTintUtils;

/**
 * Created by nathan on 7/11/17.
 */

abstract class BaseToolbarActivity extends BaseActivity {

    private final static int TOOLBAR_ELEVATION = 10;
    private final static int TEXT_COLOR_BACKGROUND_WHITE = R.color.black;
    protected Toolbar toolbar;

    protected abstract void setupFragment(Bundle savedInstanceState);

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getThemeActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green_600));
        }
        setupLayout(savedInstanceState);
        setupFragment(savedInstanceState);
        if (isToolbarWhite()) {
            setToolbarColorWhite();
        }
        if (getSupportActionBar() != null && isShowCloseButton()) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close));
        }
    }

    protected int getThemeActivity() {
        return R.style.Theme_Tokopedia3;
    }

    protected void setToolbarColorWhite() {
        if (isShadowDisplayed() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(TOOLBAR_ELEVATION);
        }
        int textColor = ContextCompat.getColor(this, TEXT_COLOR_BACKGROUND_WHITE);
        toolbar.setTitleTextColor(textColor);
        toolbar.setSubtitleTextColor(textColor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_toolbar_overflow_level_two_black);
        drawable.setBounds(5, 5, 5, 5);
        toolbar.setOverflowIcon(drawable);
    }

    protected boolean isShowCloseButton() {
        return false;
    }

    protected boolean isToolbarWhite() {
        return false;
    }

    @CallSuper
    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOptionMenuColor(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void updateOptionMenuColor(Menu menu) {
        if (isToolbarWhite()) {
            MenuTintUtils.tintAllIcons(menu, TEXT_COLOR_BACKGROUND_WHITE);
        }
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

    protected boolean isShadowDisplayed() {
        return true;
    }
}