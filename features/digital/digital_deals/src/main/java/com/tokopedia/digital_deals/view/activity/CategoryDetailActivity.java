package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsCategoryDetailPresenter;
import com.tokopedia.digital_deals.view.utils.CategoryDetailCallbacks;
import com.tokopedia.digital_deals.view.utils.Utils;

public class CategoryDetailActivity extends DealsBaseActivity implements CategoryDetailCallbacks {

    private final String BRAND_FRAGMENT = "BRAND_FRAGMENT";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String CATEGORIES_DATA = "CATEGORIES_DATA";
    public static final String FROM_HOME = "FROM_HOME";
    private String categoryName;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals_appbar;
    }

    @DeepLink({DealsUrl.AppLink.DIGITAL_DEALS_CATEGORY})
    public static TaskStackBuilder getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Intent destination;

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = ((DealsModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(new Intent(context, DealsHomeActivity.class));

        Location location = Utils.getSingletonInstance().getLocation(context);
        Uri.Builder uri = Uri.parse(deepLink).buildUpon();
        String searchName = extras.getString("search_name");
        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.setCategoryId(Integer.parseInt(extras.getString("category_id")));
        categoriesModel.setTitle(extras.getString("name"));
        String categoryUrl = searchName + "?"+ Utils.QUERY_PARAM_CITY_ID + "=" + location.getId();
        categoriesModel.setCategoryUrl(DealsUrl.DEALS_DOMAIN + DealsUrl.HelperUrl.DEALS_CATEGORY + categoryUrl);

        if (TextUtils.isEmpty(categoriesModel.getTitle())) {
            if (!TextUtils.isEmpty(searchName)) {
                if (searchName.length() > 1)
                    searchName = searchName.substring(0,1).toUpperCase() + searchName.substring(1);
                else
                    searchName = searchName.toUpperCase();
                categoriesModel.setTitle(searchName);
            }
        }
        extras.putString(CATEGORY_NAME, categoriesModel.getTitle());
        extras.putParcelable(CATEGORIES_DATA, categoriesModel);
        destination = new Intent(context, CategoryDetailActivity.class)
                .setData(uri.build())
                .putExtras(extras);

        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected Fragment getNewFragment() {
        categoryName = getIntent().getStringExtra(CATEGORY_NAME);
        if (TextUtils.isEmpty(categoryName))
            categoryName = getString(R.string.text_deals);
        updateTitle(categoryName);
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        return CategoryDetailHomeFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getBooleanExtra(CategoryDetailActivity.FROM_HOME, false)) {
            overridePendingTransition(R.anim.slide_in_left_brands, R.anim.slide_out_right_brands);
        }
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
                            updateTitle(String.format(getResources().getString(R.string.brand_category),
                                    categoryName));

                        } else {
                            updateTitle(categoryName);
                        }
                    } else {
                        updateTitle(categoryName);
                    }
                }
            }
        };

        return result;
    }


}
