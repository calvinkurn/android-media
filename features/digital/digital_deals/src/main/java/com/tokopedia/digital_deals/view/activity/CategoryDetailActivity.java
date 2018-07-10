package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.utils.CategoryDetailCallbacks;
import com.tokopedia.digital_deals.view.model.CategoriesModel;

public class CategoryDetailActivity extends BaseSimpleActivity implements CategoryDetailCallbacks {

    private final String BRAND_FRAGMENT = "BRAND_FRAGMENT";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String CATEGORIES_DATA = "CATEGORIES_DATA";
    private String categoryName;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals_appbar;
    }

    @Override
    protected Fragment getNewFragment() {
        categoryName = getIntent().getStringExtra(CATEGORY_NAME);
        toolbar.setTitle(categoryName);
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        return CategoryDetailHomeFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void replaceFragment(CategoriesModel categoriesModel) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.parent_view, AllBrandsFragment.newInstance(categoriesModel));
        transaction.addToBackStack(BRAND_FRAGMENT);
        transaction.commit();
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    if (manager.getBackStackEntryCount() >= 1) {
                        String topOnStack = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        if (topOnStack.equals(BRAND_FRAGMENT)) {
                            toolbar.setTitle(String.format(getResources().getString(R.string.brand_category),
                                    categoryName));

                        } else {
                            toolbar.setTitle(categoryName);
                        }
                    } else {
                        toolbar.setTitle(categoryName);
                    }
                }
            }
        };

        return result;
    }
}
