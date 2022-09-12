package com.tokopedia.abstraction.base.view.activity;

import static com.tokopedia.utils.view.DarkModeUtil.isDarkMode;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.utils.view.MenuTintUtils;

/**
 * Created by nathan on 7/11/17.
 */

public abstract class BaseToolbarActivity extends BaseActivity {

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
    }

    @SuppressLint("DeprecatedMethod")
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDarkMode(this)) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background));
    }

    protected int getCloseButton() {
        return com.tokopedia.resources.common.R.drawable.ic_system_close_default;
    }

    protected boolean isShowCloseButton() {
        return false;
    }

    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
        toolbar = getInitToolbarView();
        setUpActionBar(toolbar);
    }

    private @Nullable
    Toolbar getInitToolbarView() {
        return findViewById(getToolbarResourceID());
    }

    public void setUpActionBar(@Nullable Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            if (this.getTitle() != null) {
                actionBar.setTitle(this.getTitle());
            } else {
                try {
                    ActivityInfo activityInfo = this.getPackageManager().getActivityInfo(
                            this.getComponentName(), 0);
                    try {
                        actionBar.setTitle(getString(activityInfo.labelRes));
                    } catch (Exception e) {
                        actionBar.setTitle(activityInfo.nonLocalizedLabel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isShowCloseButton()) {
                actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, getCloseButton()));
            }
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        tintTextAndIcon(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void tintTextAndIcon(Menu menu) {
        try {
            int menuSize = menu.size();
            for (int i = 0; i < menuSize; i++) {
                MenuItem item = menu.getItem(i);
                tintTextColorMenuItem(item);
                tintIcon(item);
                if (item.hasSubMenu()) {
                    SubMenu subMenu = item.getSubMenu();
                    int subMenuSize = item.getSubMenu().size();
                    for (int j = 0; j < subMenuSize; j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        tintTextColorMenuItem(subMenuItem);
                        tintIcon(subMenuItem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tintIcon(MenuItem menuItem) {
        try {
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawable's with this id will have a color
                // filter applied to it.
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(this,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN900), PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tintTextColorMenuItem(MenuItem menuItem) {
        try {
            SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950)),
                    0, spanString.length(), 0);
            menuItem.setTitle(spanString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}