package com.tokopedia.abstraction.base.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.abstraction.R;

/**
 * @author okasurya on 2/7/2018
 */

public abstract class BaseWebViewActivity extends BaseSimpleActivity {

    public static final String EXTRA_TITLE = "web_view_extra_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_help) {
            startActivity(getHelpIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            updateTitle(title);
        }
    }

    protected abstract Intent getHelpIntent();
}