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
import com.tokopedia.withdraw.view.fragment.WithdrawFragment;

public class WithdrawActivity extends BaseSimpleActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
    }

    private void setToolbar() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(MethodChecker.getColor(this, R.color.white)));
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.black_70));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop(), 30, toolbar.getPaddingBottom());
        toolbar.setTitleTextAppearance(this, R.style.WebViewToolbarText);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_action_back);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return WithdrawFragment.createInstance(bundle);
    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, WithdrawActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }


}
