package com.tokopedia.discovery.categorynav.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterNoLayoutActivity;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.discovery.fragment.BrowseParentFragment;
import com.tokopedia.discovery.intermediary.view.IntermediaryFragment;
import com.tokopedia.tkpdpdp.InstallmentActivity;

import butterknife.BindView;


public class CategoryNavigationActivity extends BasePresenterNoLayoutActivity {

    public static final int DESTROY_BROWSE_PARENT = 99;
    public static final int DESTROY_INTERMEDIARY = 98;

    private FragmentManager fragmentManager;
    private String departmentId = "0";
    private String rootDepartementId = "";
    private TextView topBarTitle;

    ProgressBar progressBar;

    @BindView(R2.id.container)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_navigation);
        initView();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        if ( extras.getString(DynamicFilterPresenter.EXTRA_DEPARTMENT_ID) !=null) {
            departmentId = extras.getString(DynamicFilterPresenter.EXTRA_DEPARTMENT_ID);
        }
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void setViewListener() {

    }

    private void initView() {
        topBarTitle = (TextView) findViewById(com.tokopedia.tkpdpdp.R.id.simple_top_bar_title);
        topBarTitle.setText(getString(R.string.title_category));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        findViewById(com.tokopedia.tkpdpdp.R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        CategoryNavigationActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                    }
                });
        Fragment fragment =
                (fragmentManager.findFragmentByTag(CategoryNavigationFragment.TAG));
        if (fragment == null) {
            fragment = CategoryNavigationFragment.createInstance(departmentId);
        }
        inflateFragment(
                fragment,
                false,
                CategoryNavigationFragment.TAG);
    }

    @Override
    protected void initVar() {
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void setActionVar() {

    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        if (isFinishing()) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public static void moveTo(FragmentActivity fragmentActivity, String departmentId) {
        if (fragmentActivity != null) {
            Intent intent = new Intent(fragmentActivity, CategoryNavigationActivity.class);
            intent.putExtra(CategoryNavigationPresenter.EXTRA_DEPARTMENT_ID, departmentId);
            fragmentActivity.startActivityForResult(intent,DESTROY_BROWSE_PARENT);
            fragmentActivity.overridePendingTransition(com.tokopedia.core.R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CategoryNavigationActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
    }


}
