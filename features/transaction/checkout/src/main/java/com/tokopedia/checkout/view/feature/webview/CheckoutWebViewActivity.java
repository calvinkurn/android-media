package com.tokopedia.checkout.view.feature.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
import com.tokopedia.logisticanalytics.CodAnalytics;

/**
 * Created by Fajar Ulin Nuha on 23/11/18.
 */
public class CheckoutWebViewActivity extends BaseWebViewActivity {

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_CALLER_CODE = "EXTRA_CALLER_CODE";

    public static final int CALLER_CODE_COD = 7;

    private CodAnalytics mCodTracker;
    private int mCallerCode = -1;

    public static Intent newInstance(Context context, String url, String title) {
        return newInstance(context, url, null, -1);
    }

    /**
     * @param context activity context
     * @param url of webview to be displayed
     * @param title will display on toolbar Title, see implementation on BaseWebViewActivity@setupToolbar
     * @param callerCode for analytics purpose
     * @return Intent to start activity with
     */
    public static Intent newInstance(Context context, String url, String title, int callerCode) {
        Intent intent = new Intent(context, CheckoutWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_CALLER_CODE, callerCode);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCodTracker = new CodAnalytics(((AbstractionRouter) getApplication()).getAnalyticTracker());
        mCallerCode = getIntent().getIntExtra(EXTRA_CALLER_CODE, -1);
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        String mUrl = getIntent().getStringExtra(EXTRA_URL);
        return BaseSessionWebViewFragment.newInstance(mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mCallerCode == CALLER_CODE_COD) mCodTracker.eventClickXPadaSyarat();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
