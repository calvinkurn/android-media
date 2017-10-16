package com.tokopedia.core;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.fragment.FragmentTermPrivacy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hangnadi on 6/5/15.
 * modified by m.normansyah on 10/02/2016 AN-1385
 */
public class TermPrivacy extends TActivity {

    private Fragment fragment;
    private Uri data;
    FragmentManager supportFragmentManager;
    private Unbinder unbinder;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TERM_PRIVACY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.d("MNORMANSYAH", "back stack changed [count : " + backCount);
                if (backCount == 0) {
                    // block where back has been pressed. since backstack is zero.
                    finish();
                }
            }
        });

//        try {
        data = getIntent().getData();
        if (data != null) {
            if (data.getQueryParameter("param").equals("0")) {

                toolbar.setTitle(R.string.custom_string_term_condition);
                fragment = FragmentTermPrivacy.createInstance(FragmentTermPrivacy.TERM_MODE);
            } else {
                toolbar.setTitle(R.string.custom_string_privacy_policy);
                fragment = FragmentTermPrivacy.createInstance(FragmentTermPrivacy.PRIVACY_MODE);
            }
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//            finish();
//        }

        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getExtras().getString("param") != null) {
            switch (getIntent().getExtras().getString("param")) {
                case T_AND_C:
                    toolbar.setTitle(R.string.custom_string_term_condition);
                    fragment = FragmentTermPrivacy.createInstance(FragmentTermPrivacy.TERM_MODE);
                    break;
                case P_P:
                    toolbar.setTitle(R.string.custom_string_privacy_policy);
                    fragment = FragmentTermPrivacy.createInstance(FragmentTermPrivacy.PRIVACY_MODE);
                    break;
            }
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public static final String T_AND_C = "0";
    public static final String P_P = "1";

    public static void start(Context context, String param) {
        Intent intent = new Intent(context, TermPrivacy.class);
        Bundle bundle = new Bundle();
        bundle.putString("param", param);
        intent.putExtras(bundle);
        switch (param) {
            case T_AND_C:
            case P_P:
                context.startActivity(intent);
                break;
            default:
                throw new RuntimeException("please register your type");

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (data != null && data.getQueryParameter("param").equals("0")) {
            toolbar.setTitle(R.string.custom_string_term_condition);
        } else {
            toolbar.setTitle(R.string.custom_string_privacy_policy);
        }

        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getExtras().getString("param") != null) {
            switch (getIntent().getExtras().getString("param")) {
                case T_AND_C:
                    toolbar.setTitle(R.string.custom_string_term_condition);
                    break;
                case P_P:
                    toolbar.setTitle(R.string.custom_string_privacy_policy);
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
