package com.tokopedia.discovery.categorynav.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterNoLayoutActivity;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.applink.DiscoveryAppLink;


public class CategoryNavigationActivity extends BasePresenterNoLayoutActivity {

    public static final int DESTROY_BROWSE_PARENT = 99;
    public static final int DESTROY_INTERMEDIARY = 98;
    public static final String EXTRA_SELECTED_CATEGORY_ID = "EXTRA_SELECTED_CATEGORY_ID";
    public static final String EXTRA_SELECTED_CATEGORY_NAME = "EXTRA_SELECTED_CATEGORY_NAME";
    public static final String EXTRA_DEPARTMENT_ID = "EXTRA_DEPARTMENT_ID";

    private FragmentManager fragmentManager;
    private String departmentId = "0";
    private String rootDepartementId = "";
    private TextView topBarTitle;
    private FrameLayout frameLayout;
    View buttonClose;

    ProgressBar progressBar;

    @DeepLink(DiscoveryAppLink.CATEGORY)
    public static Intent getAppLinkIntent(Context context, Bundle bundle) {
        return new Intent(context, CategoryNavigationActivity.class).putExtras(bundle);
    }

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
        if ( extras.getString(EXTRA_DEPARTMENT_ID) !=null) {
            departmentId = extras.getString(EXTRA_DEPARTMENT_ID);
        }
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void setViewListener() {

    }

    private void initView() {
        frameLayout = findViewById(R.id.container);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    CategoryNavigationActivity.this.overridePendingTransition(0,com.tokopedia.core2.R.anim.push_down);}
                });topBarTitle = (TextView) findViewById(R.id.top_bar_title);
        topBarTitle.setText(getString(R.string.title_category));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
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
            fragmentActivity.overridePendingTransition(com.tokopedia.core2.R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    public static Intent createInstance(Context context, String departmentId) {
        Intent intent = new Intent(context, CategoryNavigationActivity.class);
        intent.putExtra(CategoryNavigationPresenter.EXTRA_DEPARTMENT_ID, departmentId);
        return intent;
    }
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, com.tokopedia.core2.R.anim.push_down);
    }


}
