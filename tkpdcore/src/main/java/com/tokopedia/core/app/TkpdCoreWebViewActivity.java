package com.tokopedia.core.app;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.core.R;
import com.tokopedia.core.router.InboxRouter;

/**
 * Created by henrypriyono on 7/21/17.
 */

/**
 * Extends fragment webview from tkpd abstraction
 */
@Deprecated
public class TkpdCoreWebViewActivity extends TActivity {

    public static final String EXTRA_TITLE = "core_web_view_extra_title";

    @Override
    protected void setupToolbar() {
        super.setupToolbar();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_webview_back_button);

        toolbar.setBackgroundResource(R.color.white);
        toolbar.setTitleTextAppearance(this, R.style.WebViewToolbarText);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            toolbar.setTitle(title);
        }
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
            Intent intent = InboxRouter.getContactUsActivityIntent(this);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
