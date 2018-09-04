package com.tokopedia.mitra.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.fragment.MitraAccountFragment;
import com.tokopedia.mitra.homepage.fragment.MitraHelpFragment;
import com.tokopedia.mitra.homepage.fragment.MitraHomepageFragment;

public class MitraParentHomepageActivity extends BaseSimpleActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigation homeNavigation;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_mitra_parent_homepage;
    }

    @Override
    protected Fragment getNewFragment() {
        return MitraHomepageFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeNavigation = findViewById(R.id.mitra_bottom_nav);
        homeNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mitra_home:
                inflateFragment(MitraHomepageFragment.newInstance());
                break;
            case R.id.menu_mitra_help:
                inflateFragment(MitraHelpFragment.newInstance());
                break;
            case R.id.menu_mitra_account:
                inflateFragment(MitraAccountFragment.newInstance());
                break;
        }
        return false;
    }

    private void inflateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_view, fragment, getTagFragment())
                .commit();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MitraParentHomepageActivity.class);
    }
}
