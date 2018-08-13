package com.tokopedia.withdraw.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.view.fragment.WithdrawPasswordFragment;

public class WithdrawPasswordActivity extends BaseSimpleActivity {

    public final static String BUNDLE_BANK = "bank";
    public final static String BUNDLE_WITHDRAW = "withdraw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return WithdrawPasswordFragment.createInstance(bundle);
    }

    private void setToolbar() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(MethodChecker.getColor(this, R.color.white)));
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.grey_700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop(), 30, toolbar.getPaddingBottom());


        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_close);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, WithdrawPasswordActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
