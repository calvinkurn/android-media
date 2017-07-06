package com.tokopedia.discovery.categorynav;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.entity.categories.Category;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterPresenter;

import java.util.List;

public class CategoryNavigationActivity extends TActivity implements CategoryNavigationView {

    private TextView topBarTitle;
    private String departmentId;

    CategoryNavigationPresenter categoryNavigationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_navigation);
        hideToolbar();
        initView();
        setupTopbar();
        categoryNavigationPresenter = new CategoryNavigationPresenterImpl(CategoryNavigationActivity.this);
        initData();
    }

    private void initView() {
        topBarTitle = (TextView) findViewById(com.tokopedia.tkpdpdp.R.id.simple_top_bar_title);
        findViewById(com.tokopedia.tkpdpdp.R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    public static void moveTo(FragmentActivity fragmentActivity, String departmentId) {
        if (fragmentActivity != null) {
            Intent intent = new Intent(fragmentActivity, CategoryNavigationActivity.class);
            intent.putExtra(DynamicFilterPresenter.EXTRA_DEPARTMENT_ID, departmentId);
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(com.tokopedia.core.R.anim.pull_up, android.R.anim.fade_out);
        }
    }


    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.title_category));
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            departmentId = getIntent().getExtras().getString(DynamicFilterPresenter.EXTRA_DEPARTMENT_ID);
        }


    }

    @Override
    public void setUpRootCategory(List<Category> categories) {
        
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, @Nullable Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
