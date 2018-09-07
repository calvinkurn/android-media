package com.tokopedia.mitra.welcome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.mitra.R;

public class MitraWelcomeActivity extends BaseSimpleActivity {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_mitra_welcome;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return MitraWelcomeFragment.newInstance();
    }
}
