package com.tokopedia.checkout.subfeature.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.tokopedia.logisticCommon.data.analytics.CodAnalytics;
import com.tokopedia.webview.BaseSessionWebViewFragment;
import com.tokopedia.webview.BaseSimpleWebViewActivity;

import static com.tokopedia.webview.ConstantKt.KEY_TITLE;

/**
 * Created by Fajar Ulin Nuha on 23/11/18.
 */
public class CheckoutWebViewActivity extends BaseSimpleWebViewActivity {

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_CALLER_CODE = "EXTRA_CALLER_CODE";

    public static final int CALLER_CODE_COD = 7;

    private CodAnalytics mCodTracker;
    private int mCallerCode = -1;

    public static Intent newInstance(Context context, String url, String title) {
        return newInstance(context, url, title, -1);
    }

    /**
     * @param context activity context
     * @param url of webview to be displayed
     * @param title will display on toolbar Title
     * @param callerCode for analytics purpose
     * @return Intent to start activity with
     */
    public static Intent newInstance(Context context, String url, String title, int callerCode) {
        Intent intent = new Intent(context, CheckoutWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_CALLER_CODE, callerCode);
        intent.putExtra(KEY_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCodTracker = new CodAnalytics();
        mCallerCode = getIntent().getIntExtra(EXTRA_CALLER_CODE, -1);
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
