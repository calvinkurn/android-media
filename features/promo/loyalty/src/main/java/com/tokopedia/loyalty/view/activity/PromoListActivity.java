package com.tokopedia.loyalty.view.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
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
import com.tokopedia.loyalty.view.widget.TouchViewPager2;
import com.tokopedia.track.TrackApp;

import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListActivity extends BaseActivity implements IPromoListActivityView, PromoListFragment.OnFragmentInteractionListener {

    private static final String PROMO_NATIVE_QUERY_MENU_ID = "menuID";
    private static final String PROMO_NATIVE_QUERY_CATEGORY_ID = "categoryID";

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

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(PROMO_NATIVE_QUERY_MENU_ID, autoSelectedMenuId);
        savedInstanceState.putString(PROMO_NATIVE_QUERY_CATEGORY_ID, autoSelectedCategoryId);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getMenuAndCategory();
        } else {
            autoSelectedMenuId = savedInstanceState.getString(PROMO_NATIVE_QUERY_MENU_ID);
            autoSelectedCategoryId = savedInstanceState.getString(PROMO_NATIVE_QUERY_CATEGORY_ID);
        }
        setContentView(getContentId());
        initialPresenter();
        initView();
        setActionVar();
        trackCampaign(getIntent().getData());
    }

    private void trackCampaign(Uri uri){
        //track campaign in case there is utm/gclid in url
        TrackApp.getInstance().getGTM().sendCampaign(this, uri.toString(), getScreenName(), false);
    }

    private void getMenuAndCategory(){
        Uri data = getIntent().getData();
        autoSelectedMenuId = data.getQueryParameter(PROMO_NATIVE_QUERY_MENU_ID);
        if (TextUtils.isEmpty(autoSelectedMenuId)){
            autoSelectedMenuId = DEFAULT_AUTO_SELECTED_MENU_ID;
        }
        autoSelectedCategoryId = data.getQueryParameter(PROMO_NATIVE_QUERY_CATEGORY_ID);
        if (TextUtils.isEmpty(autoSelectedCategoryId)){
            autoSelectedCategoryId = DEFAULT_AUTO_SELECTED_CATEGORY_ID;
        }
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