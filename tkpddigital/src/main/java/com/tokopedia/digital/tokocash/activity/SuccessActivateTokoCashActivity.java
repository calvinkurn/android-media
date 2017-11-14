package com.tokopedia.digital.tokocash.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import butterknife.BindView;

/**
 * Created by nabillasabbaha on 7/25/17.
 */

public class SuccessActivateTokoCashActivity extends BasePresenterActivity {

    @BindView(R2.id.desc_success)
    TextView descSuccess;
    @BindView(R2.id.back_to_home_btn)
    Button backToHomeBtn;

    public static Intent newInstance(Context context) {
        return new Intent(context, SuccessActivateTokoCashActivity.class);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_success_activate_tokocash;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(getResources().getString(R.string.tokocash_toolbar_success_activation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        String phoneNumber = "<b>" + SessionHandler.getPhoneNumber() + "</b>";
        String desc = String.format(getApplicationContext().getString(R.string.desc_success_tokocash), phoneNumber);
        descSuccess.setText(MethodChecker.fromHtml(desc));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCacheBalanceTokoCash();
                onBackPressed();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @NonNull
    private void deleteCacheBalanceTokoCash() {
        GlobalCacheManager cacheBalance = new GlobalCacheManager();
        cacheBalance.delete(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}