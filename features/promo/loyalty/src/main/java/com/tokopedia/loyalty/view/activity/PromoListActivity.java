package com.tokopedia.loyalty.view.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.ticker.TouchViewPager2;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.applink.LoyaltyAppLink;
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

    public static final String DEFAULT_AUTO_SELECTED_CATEGORY_ID = "0";
    public static final String DEFAULT_AUTO_SELECTED_MENU_ID = "0";

    TouchViewPager2 viewPager;
    TabLayout tabLayout;
    View containerError;
    Toolbar toolbar;

    private PromoPagerAdapter adapter;

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
    @DeepLink(LoyaltyAppLink.PROMO_NATIVE)
    public static Intent getAppLinkIntent(Context context, Bundle extras) {
        String autoSelectedMenuId = extras.getString(
                LoyaltyAppLink.PROMO_NATIVE_QUERY_MENU_ID, DEFAULT_AUTO_SELECTED_MENU_ID
        );
        String autoSelectedCategoryId = extras.getString(
                LoyaltyAppLink.PROMO_NATIVE_QUERY_CATEGORY_ID, DEFAULT_AUTO_SELECTED_CATEGORY_ID
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
        containerError = findViewById(R.id.container_error);
        toolbar = (Toolbar) findViewById(com.tokopedia.abstraction.R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
        }
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
        viewPager.setOffscreenPageLimit(promoMenuDataList.size());
        adapter = new PromoPagerAdapter(getSupportFragmentManager(), promoMenuDataList, autoSelectedCategoryId);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        int indexMenuAutoSelected = Integer.parseInt(autoSelectedMenuId);

        for (int i = 0; i < promoMenuDataList.size(); i++) {
            MenuPromoTab menuPromoTab = new MenuPromoTab(this);
            menuPromoTab.renderData(promoMenuDataList.get(i));
            tabLayout.getTabAt(i).setCustomView(menuPromoTab);

            String menuId = promoMenuDataList.get(i).getMenuId();
            if (menuId.equalsIgnoreCase(autoSelectedMenuId)) {
                indexMenuAutoSelected = i;
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    ((MenuPromoTab) tab.getCustomView()).renderActiveState();
                    autoSelectedMenuId = String.valueOf(tab.getPosition());
                }
                promoTrackingUtil.eventPromoListClickCategory(PromoListActivity.this,promoMenuDataList.get(tab.getPosition()).getTitle());
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
        handlerError(message);
    }

    @Override
    public void renderErrorHttpGetPromoMenuDataList(String message) {
        handlerError(message);
    }

    @Override
    public void renderErrorNoConnectionGetPromoMenuDataList(String message) {
        handlerError(message);
    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoMenuDataListt(String message) {
        handlerError(message);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

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

    }

    @Override
    public void onChangeFilter(String categoryId) {
        autoSelectedCategoryId = categoryId;
    }

    private void handlerError(String message) {
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        containerError.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(this, containerError,
                message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        dPresenter.processGetPromoMenu();
                        containerError.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

}