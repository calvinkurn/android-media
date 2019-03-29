package com.tokopedia.logisticinsurance.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.logisticinsurance.R;

public class InsuranceTnCActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, InsuranceTnCActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragmentWebView();
    }

    private void showFragmentWebView() {
        Fragment fragment = new InsuranceTnCFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.parent_view, fragment).commit();
    }

    @Override
    protected Fragment getNewFragment() {
        return InsuranceTnCFragment.createInstance();
    }
}
