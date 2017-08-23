package com.tokopedia.digital.base;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;

/**
 * @author anggaprasetiyo on 8/22/17.
 */

public abstract class BaseDigitalPresenterActivity<P> extends BasePresenterActivity<P> {

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (isLightToolbarThemes()) {
            setLightToolbarStyle();
        }
    }

    private void setLightToolbarStyle() {
        setTheme(R.style.WebViewActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (parentView != null) {
                    parentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                window.setStatusBarColor(getResources().getColor(R.color.white, null));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(
                    com.tokopedia.core.R.drawable.ic_webview_back_button
            );

        toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
        toolbar.setTitleTextAppearance(this, com.tokopedia.core.R.style.WebViewToolbarText);
    }

    protected abstract boolean isLightToolbarThemes();
}
