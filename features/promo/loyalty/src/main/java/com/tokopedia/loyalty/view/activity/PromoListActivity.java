package com.tokopedia.loyalty.view.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.loyalty.view.widget.TouchViewPager2;
import com.tokopedia.device.info.DeviceConnectionInfo;
import com.tokopedia.globalerror.GlobalError;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoListActivityComponent;
import com.tokopedia.loyalty.di.component.PromoListActivityComponent;
import com.tokopedia.loyalty.di.module.PromoListActivityModule;
import com.tokopedia.loyalty.view.adapter.PromoPagerAdapter;
import com.tokopedia.loyalty.view.compoundview.MenuPromoTab;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;
import com.tokopedia.loyalty.view.presenter.IPromoListActivityPresenter;
import com.tokopedia.loyalty.view.util.PromoTrackingUtil;
import com.tokopedia.loyalty.view.view.IPromoListActivityView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListActivity extends BaseActivity implements IPromoListActivityView, PromoListFragment.OnFragmentInteractionListener {

    private static final String EXTRA_AUTO_SELECTED_MENU_ID = "EXTRA_AUTO_SELECTED_MENU_ID";
    private static final String EXTRA_AUTO_SELECTED_CATEGORY_ID = "EXTRA_AUTO_SELECTED_CATEGORY_ID";

    private static final String PROMO_NATIVE_QUERY_CATEGORY_ID = "categoryID";
    private static final String PROMO_NATIVE_QUERY_MENU_ID = "menuID";

    public static final String DEFAULT_AUTO_SELECTED_CATEGORY_ID = "0";
    public static final String DEFAULT_AUTO_SELECTED_MENU_ID = "0";

    private TouchViewPager2 viewPager;
    private TabLayout tabLayout;
    private View shimmerLayout;
    private Toolbar toolbar;
    private AppCompatImageView shimmerImageBack;
    private GlobalError globalError;

    private String autoSelectedMenuId;
    private String autoSelectedCategoryId;

    @Inject
    IPromoListActivityPresenter dPresenter;

    @Inject
    PromoTrackingUtil promoTrackingUtil;

    public static Intent newInstance(Context context, String menuId, String categoryId) {
        return new Intent(context, PromoListActivity.class)
                .putExtra(EXTRA_AUTO_SELECTED_MENU_ID, menuId)
                .putExtra(EXTRA_AUTO_SELECTED_CATEGORY_ID, categoryId);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, PromoListActivity.class)
                .putExtra(EXTRA_AUTO_SELECTED_MENU_ID, DEFAULT_AUTO_SELECTED_MENU_ID)
                .putExtra(EXTRA_AUTO_SELECTED_CATEGORY_ID, DEFAULT_AUTO_SELECTED_CATEGORY_ID);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(EXTRA_AUTO_SELECTED_MENU_ID, autoSelectedMenuId);
        savedInstanceState.putString(EXTRA_AUTO_SELECTED_CATEGORY_ID, autoSelectedCategoryId);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        autoSelectedMenuId = savedInstanceState.getString(EXTRA_AUTO_SELECTED_MENU_ID);
        autoSelectedCategoryId = savedInstanceState.getString(EXTRA_AUTO_SELECTED_CATEGORY_ID);
    }

    @SuppressWarnings("unused")
    @DeepLink({ApplinkConst.PROMO_LIST, ApplinkConst.PROMO})
    public static Intent getAppLinkIntent(Context context, Bundle extras) {
        String autoSelectedMenuId = extras.getString(
                PROMO_NATIVE_QUERY_MENU_ID, DEFAULT_AUTO_SELECTED_MENU_ID
        );
        String autoSelectedCategoryId = extras.getString(
                PROMO_NATIVE_QUERY_CATEGORY_ID, DEFAULT_AUTO_SELECTED_CATEGORY_ID
        );
        return PromoListActivity.newInstance(context, autoSelectedMenuId, autoSelectedCategoryId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        setContentView(getContentId());
        initialPresenter();
        initView();
        setActionVar();
    }

    protected void setupBundlePass(Bundle extras) {
        autoSelectedMenuId = extras.getString(EXTRA_AUTO_SELECTED_MENU_ID);
        autoSelectedCategoryId = extras.getString(EXTRA_AUTO_SELECTED_CATEGORY_ID);
    }

    protected void initialPresenter() {
        PromoListActivityComponent promoListActivityComponent = DaggerPromoListActivityComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplicationContext()).getBaseAppComponent())
                .promoListActivityModule(new PromoListActivityModule(this))
                .build();
        promoListActivityComponent.inject(this);
    }

    protected int getContentId() {
        return R.layout.activity_promo_list;
    }

    protected void initView() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        shimmerLayout = findViewById(R.id.shimmer_layout);
        shimmerImageBack = findViewById(R.id.shimmer_back);
        toolbar = (Toolbar) findViewById(com.tokopedia.abstraction.R.id.toolbar);
        globalError = findViewById(R.id.global_error);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        shimmerImageBack.setOnClickListener(v -> onBackPressed());

        globalError.getErrorAction().setOnClickListener(v -> {
            dPresenter.processGetPromoMenu();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setActionVar() {
        dPresenter.processGetPromoMenu();
    }

    @Override
    public void renderPromoMenuDataList(final List<PromoMenuData> promoMenuDataList) {
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);

        viewPager.setOffscreenPageLimit(promoMenuDataList.size());
        PromoPagerAdapter adapter = new PromoPagerAdapter(this, getSupportFragmentManager(), promoMenuDataList, autoSelectedCategoryId);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        int indexMenuAutoSelected = 0;
        if (!TextUtils.isEmpty(autoSelectedMenuId)) {
            indexMenuAutoSelected = Integer.parseInt(autoSelectedMenuId);
        }


        for (int i = 0; i < promoMenuDataList.size(); i++) {
            MenuPromoTab menuPromoTab = new MenuPromoTab(this);
            menuPromoTab.renderData(promoMenuDataList.get(i));
            tabLayout.getTabAt(i).setCustomView(menuPromoTab);

            if (!TextUtils.isEmpty(autoSelectedMenuId)) {
                String menuId = promoMenuDataList.get(i).getMenuId();
                if (menuId.equalsIgnoreCase(autoSelectedMenuId)) {
                    indexMenuAutoSelected = i;
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    ((MenuPromoTab) tab.getCustomView()).renderActiveState();
                    autoSelectedMenuId = String.valueOf(tab.getPosition());
                }
                promoTrackingUtil.eventPromoListClickCategory(PromoListActivity.this, promoMenuDataList.get(tab.getPosition()).getTitle());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    ((MenuPromoTab) tab.getCustomView()).renderNormalState();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        TabLayout.Tab firstTab = tabLayout.getTabAt(indexMenuAutoSelected);

        if (firstTab != null) {
            firstTab.select();
            ((MenuPromoTab) firstTab.getCustomView()).renderActiveState();
            promoTrackingUtil.eventPromoListClickCategory(PromoListActivity.this,
                    promoMenuDataList.get(firstTab.getPosition()).getTitle()
            );
        }
    }

    @Override
    public void renderErrorGetPromoMenuDataList(String message) {
        globalError.setType(GlobalError.Companion.getSERVER_ERROR());
        handlerError();
    }

    @Override
    public void renderErrorHttpGetPromoMenuDataList(String message) {
        globalError.setType(GlobalError.Companion.getSERVER_ERROR());
        handlerError();
    }

    @Override
    public void renderErrorNoConnectionGetPromoMenuDataList(String message) {
        globalError.setType(GlobalError.Companion.getNO_CONNECTION());
        handlerError();
    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoMenuDataListt(String message) {
        globalError.setType(GlobalError.Companion.getSERVER_ERROR());
        handlerError();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {
        //Do nothing
    }

    @Override
    public void showInitialProgressLoading() {
        //Do nothing
    }

    @Override
    public void hideInitialProgressLoading() {
        //Do nothing
    }

    @Override
    public void clearContentRendered() {
        //Do nothing
    }

    @Override
    public void showProgressLoading() {
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        shimmerLayout.setVisibility(View.VISIBLE);
        globalError.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressLoading() {
        shimmerLayout.setVisibility(View.GONE);
        globalError.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {
        //Do nothing
    }

    @Override
    public void showDialog(Dialog dialog) {
        //Do nothing
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        //Do nothing
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {
        //Do nothing
    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {
        //Do nothing
    }

    @Override
    public void onChangeFilter(String categoryId) {
        autoSelectedCategoryId = categoryId;
    }

    private void handlerError() {
        if (!isConnectedToInternet()) {
            globalError.setType(GlobalError.Companion.getNO_CONNECTION());
        }

        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        globalError.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        shimmerLayout.setVisibility(View.GONE);
    }

    private boolean isConnectedToInternet() {
        if (getApplicationContext() != null) {
            return DeviceConnectionInfo.isConnectCellular(getApplicationContext()) || DeviceConnectionInfo.isConnectWifi(getApplicationContext());
        }
        return false;
    }
}