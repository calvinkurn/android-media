package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ViewFlipper;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.Tabs;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.adapter.CouponFilterPagerAdapter;
import com.tokopedia.tokopoints.view.contract.CouponActivityContract;
import com.tokopedia.tokopoints.view.fragment.MyCouponListingFragment;
import com.tokopedia.tokopoints.view.model.CouponFilterItem;
import com.tokopedia.tokopoints.view.presenter.CouponActivityPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.TabUtil;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.tokopoints.view.util.CommonConstant.TAB_SETUP_DELAY_MS;

public class MyCouponListingActivity extends BaseSimpleActivity implements CouponActivityContract.View, HasComponent<TokoPointComponent> {
    private static final int REQUEST_CODE_LOGIN = 1;
    private TokoPointComponent tokoPointComponent;
    private ViewFlipper mContainerMain;
    private ViewPager mPagerFilter;
    private Tabs mTabsFilter;
    CouponFilterPagerAdapter mAdapter;

    @Inject
    CouponActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_label_my_coupon));
        getComponent().inject(this);
        mPresenter.attachView(this);
        mContainerMain = findViewById(R.id.container);
        initViews();
        UserSessionInterface userSession = new UserSession(this);
        if (userSession.isLoggedIn()) {
            mPresenter.getFilter(getIntent().getStringExtra(CommonConstant.EXTRA_SLUG));
            showLoading();
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.tp_activity_coupon_list;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    @DeepLink({ApplinkConstant.COUPON_LISTING,
            ApplinkConstant.COUPON_LISTING2,
            ApplinkConstant.COUPON_LISTING3,
            ApplinkConstant.COUPON_LISTING4})
    public static Intent getCallingIntent(Context context, @NonNull Bundle extras) {
        Intent intent = new Intent(context, MyCouponListingActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MyCouponListingActivity.class);
    }

    private void initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            mPresenter.getFilter(getIntent().getStringExtra(CommonConstant.EXTRA_SLUG));
            showLoading();
        } else {
            finish();
        }
    }

    public void showLoading() {
        mContainerMain.setDisplayedChild(0);
    }

    @Override
    public void hideLoading() {
        mContainerMain.setDisplayedChild(1);
    }

    @Override
    public void onSuccess(List<CouponFilterItem> data) {

        //Setting up sort types tabs
        mAdapter = new CouponFilterPagerAdapter(getSupportFragmentManager(), data);

        mPagerFilter.setAdapter(mAdapter);
        mTabsFilter.setupWithViewPager(mPagerFilter);
        mTabsFilter.setVisibility(View.VISIBLE);

        //excluding extra padding from tabs
        TabUtil.wrapTabIndicatorToTitle(mTabsFilter,
                (int) getResources().getDimension(R.dimen.tp_margin_medium),
                (int) getResources().getDimension(R.dimen.tp_margin_regular));

        mPagerFilter.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MyCouponListingFragment fragment = (MyCouponListingFragment) mAdapter.getRegisteredFragment(position);

                if (fragment != null
                        && fragment.isAdded()) {
                    if (fragment.getPresenter() != null && fragment.getPresenter().isViewAttached()) {
                        fragment.getPresenter().setCategoryId(data.get(position).getId());
                        fragment.getPresenter().getCoupons(fragment.getPresenter().getCategoryId());
                    }

                    AnalyticsTrackerUtil.sendEvent(getActivityContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA,
                            "click " + data.get(position),
                            "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagerFilter.postDelayed(() -> {
            int[] ids = getSelectedCategoryId(data);
            mPagerFilter.setCurrentItem(ids[1]);
            loadFirstTab(ids[0]);
        }, TAB_SETUP_DELAY_MS);
    }

    @Override
    public void onError(String error) {
        mContainerMain.setDisplayedChild(2);
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public String getStringRaw(int id) {
        return GraphqlHelper.loadRawString(getResources(), id);
    }

    private void initViews() {
        mPagerFilter = findViewById(R.id.view_pager_sort_type);
        mTabsFilter = findViewById(R.id.tabs_sort_type);
    }

    public void loadFirstTab(int categoryId) {
        MyCouponListingFragment fragment = (MyCouponListingFragment) mAdapter.getRegisteredFragment(mPagerFilter.getCurrentItem());
        if (fragment != null
                && fragment.isAdded()) {
            if (fragment.getPresenter() != null && fragment.getPresenter().isViewAttached()) {
                fragment.getPresenter().setCategoryId(categoryId);
                fragment.getPresenter().getCoupons(fragment.getPresenter().getCategoryId());
            }
        }
    }

    private int[] getSelectedCategoryId(List<CouponFilterItem> data) {
        int[] ids = {0, 0};
        int counter = 0;

        for (CouponFilterItem item : data) {
            if (item.isSelected()) {
                ids[0] = item.getId();
                ids[1] = counter;
                break;
            }
            counter++;
        }

        return ids;
    }
}
