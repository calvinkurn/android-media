package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.CategoryDetailCallbacks;
import com.tokopedia.digital_deals.view.viewmodel.CategoriesModel;

public class CategoryDetailActivity extends BaseSimpleActivity implements CategoryDetailCallbacks{


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_category_detail;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DealDetailsPresenter.HOME_DATA, getIntent().getParcelableExtra(DealDetailsPresenter.HOME_DATA));

        return CategoryDetailHomeFragment.createInstance(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void replaceFragment(CategoriesModel detailsViewModel) {
        toolbar.setTitle(String.format(getResources().getString(R.string.brand_category),
                        detailsViewModel.getTitle()));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, AllBrandsFragment.newInstance(detailsViewModel));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
