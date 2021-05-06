package com.tokopedia.abstraction.base.view.activity;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.utils.view.MenuTintUtils;
import static com.tokopedia.utils.view.DarkModeUtil.isDarkMode;

/**
 * Created by nathan on 7/11/17.
 */

abstract class BaseToolbarActivity extends BaseActivity {

    private final static int TEXT_COLOR_BACKGROUND_WHITE = com.tokopedia.unifyprinciples.R.color.Unify_N700;
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!isDarkMode(this)) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }
    }

    private void setupActionBarHomeIndicatorIcon() {
        if (getSupportActionBar() != null && isShowCloseButton()) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, getCloseButton()));
        }
    }

    protected int getCloseButton() {
        return  com.tokopedia.resources.common.R.drawable.ic_system_close_default;
    }

    protected boolean isShowCloseButton() {
        return false;
    }

    protected int getToolbarResourceID(){
        return R.id.toolbar;
    }

    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
        toolbar = (Toolbar) findViewById(getToolbarResourceID());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
        }
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