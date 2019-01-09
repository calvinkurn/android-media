package com.tokopedia.loyalty.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerLoyaltyViewComponent;
import com.tokopedia.loyalty.di.component.LoyaltyViewComponent;
import com.tokopedia.loyalty.di.module.LoyaltyViewModule;
import com.tokopedia.loyalty.listener.LoyaltyActivityTabSelectedListener;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.adapter.GlobalMainTabSelectedListener;
import com.tokopedia.loyalty.view.adapter.LoyaltyPagerAdapter;
import com.tokopedia.loyalty.view.data.LoyaltyPagerItem;
import com.tokopedia.loyalty.view.fragment.PromoCodeFragment;
import com.tokopedia.loyalty.view.fragment.PromoCouponFragment;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TAB;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CART_ID;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_SELECTED_TAB;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_CODE;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_ID;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_LIST;
import static com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class LoyaltyActivity extends BaseSimpleActivity
        implements HasComponent<BaseAppComponent>,
        PromoCodeFragment.ManualInsertCodeListener,
        PromoCouponFragment.ChooseCouponListener {

    public static final String DEFAULT_COUPON_TAB_SELECTED = "coupon";
    ViewPager viewPager;
    TabLayout indicator;

    @Inject
    LoyaltyPagerAdapter loyaltyPagerAdapter;
    @Inject
    @Named("coupon_active")
    List<LoyaltyPagerItem> loyaltyPagerItemListCouponActive;
    @Inject
    @Named("coupon_not_active")
    List<LoyaltyPagerItem> loyaltyPagerItemListCouponNotActive;
    @Inject
    CheckoutAnalyticsCart checkoutAnalyticsCart;
    @Inject
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    @Inject
    LoyaltyModuleRouter loyaltyModuleRouter;

    private boolean isCouponActive;
    private String platformString;
    private String platformPageString;
    private String defaultSelectedTabString;
    private String categoryString;
    private int categoryId;
    private int productId;
    private String cartIdString;
    private String additionalDataString;
    private String trainReservationId;
    private String trainReservationCode;

    public boolean isCouponActive() {
        return isCouponActive;
    }

    public String getPlatformString() {
        return platformString;
    }

    public String getPlatformPageString() {
        return platformPageString;
    }

    public String getDefaultSelectedTabString() {
        return defaultSelectedTabString;
    }

    public String getCategoryString() {
        return categoryString;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getProductId() {
        return productId;
    }

    public String getCartIdString() {
        return cartIdString;
    }

    public String getAdditionalDataString() {
        return additionalDataString;
    }

    public String getTrainReservationId() {
        return trainReservationId;
    }

    public String getTrainReservationCode() {
        return trainReservationCode;
    }

    private OnTabSelectedForTrackingCheckoutMarketPlace onTabSelectedForTrackingCheckoutMarketPlace;

    @Override
    protected Fragment getNewFragment(){
        return null;
    }

    @LayoutRes
    protected int getLayoutRes() {
        return R.layout.activity_loyalty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        initialPresenter();
        initView();
        setViewListener();
    }

    protected void setupBundlePass(Bundle extras) {
        this.isCouponActive = extras.getBoolean(
                EXTRA_COUPON_ACTIVE
        );
        this.platformString = extras.getString(
                EXTRA_PLATFORM, ""
        );
        this.platformPageString = extras.getString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM_PAGE, ""
        );
        this.defaultSelectedTabString = extras.getString(
                EXTRA_SELECTED_TAB, ""
        );
        this.cartIdString = extras.getString(
                EXTRA_CART_ID, ""
        );
        this.categoryString = extras.getString(
                EXTRA_CATEGORY, ""
        );
        this.categoryId = extras.getInt(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORYID
        );
        this.productId = extras.getInt(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PRODUCTID
        );
        this.cartIdString = extras.getString(
                EXTRA_CART_ID, ""
        );
        this.additionalDataString = extras.getString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_ADDITIONAL_STRING_DATA, ""
        );
        this.trainReservationId = extras.getString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_ID, ""
        );
        this.trainReservationCode = extras.getString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_CODE, ""
        );

    }

    protected void initialPresenter() {
        LoyaltyViewComponent loyaltyViewComponent = DaggerLoyaltyViewComponent.builder()
                .baseAppComponent((BaseAppComponent) getComponent())
                .loyaltyViewModule(new LoyaltyViewModule(this))
                .build();
        loyaltyViewComponent.inject(this);
    }

    protected void initView() {
        viewPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);

    }

    protected void setViewListener() {
        if (isCouponActive) {
            onTabSelectedForTrackingCheckoutMarketPlace =
                    new OnTabSelectedForTrackingCheckoutMarketPlace(true);
            renderViewWithCouponTab();
        } else {
            onTabSelectedForTrackingCheckoutMarketPlace =
                    new OnTabSelectedForTrackingCheckoutMarketPlace(false);
            renderViewSingleTabPromoCode();
        }
    }

    private void renderViewSingleTabPromoCode() {
        indicator.setVisibility(View.GONE);
        viewPager.setOffscreenPageLimit(loyaltyPagerItemListCouponNotActive.size());
        loyaltyPagerAdapter.addAllItem(loyaltyPagerItemListCouponNotActive);
        viewPager.setAdapter(loyaltyPagerAdapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        if (IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING.equalsIgnoreCase(platformString))
            indicator.addOnTabSelectedListener(onTabSelectedForTrackingCheckoutMarketPlace);
    }

    private void renderViewWithCouponTab() {
        for (LoyaltyPagerItem loyaltyPagerItem : loyaltyPagerItemListCouponActive)
            indicator.addTab(indicator.newTab().setText(loyaltyPagerItem.getTabTitle()));
        setTabProperties();
        indicator.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setOffscreenPageLimit(loyaltyPagerItemListCouponActive.size());
        loyaltyPagerAdapter.addAllItem(loyaltyPagerItemListCouponActive);
        viewPager.setAdapter(loyaltyPagerAdapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new LoyaltyActivityTabSelectedListener(viewPager, loyaltyModuleRouter));
        if (IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING.equalsIgnoreCase(platformString))
            indicator.addOnTabSelectedListener(onTabSelectedForTrackingCheckoutMarketPlace);
        setShowCase();
        if (getIntent().hasExtra(EXTRA_SELECTED_TAB)) {
            viewPager.setCurrentItem(getIntent().getIntExtra(
                    EXTRA_SELECTED_TAB,
                    IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_TAB));
        }
    }

    private void setShowCase() {
        ShowCaseObject showCase = new ShowCaseObject(
                ((ViewGroup) indicator.getChildAt(0)).getChildAt(1),
                getString(R.string.show_case_title),
                getString(R.string.show_case_text),
                ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();

        showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                return false;
            }
        });
        if (!ShowCasePreference.hasShown(this, LoyaltyActivity.class.getName()))
            showCaseDialog.show(this, LoyaltyActivity.class.getName(), showCaseObjectList);

    }

    private void setTabProperties() {
        indicator.setTabMode(TabLayout.MODE_FIXED);
        indicator.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tkpd_main_green));
        indicator.setTabTextColors(
                ContextCompat.getColor(this, R.color.black_38),
                ContextCompat.getColor(this, R.color.tkpd_main_green));
        indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }


    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }


    @Override
    public void onCodeSuccess(String voucherCode, String voucherMessage, String voucherAmount) {
        if (platformString.equals(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING)) {
            if (platformPageString.equals(PLATFORM_PAGE_MARKETPLACE_CART_LIST)) {
                checkoutAnalyticsCart.eventClickCouponCartClickGunakanKodeFormGunakanKodePromoAtauKuponSuccess();
            } else if (platformPageString.equals(PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT)) {
                checkoutAnalyticsCourierSelection.eventClickCouponCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKuponSuccess();
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, voucherCode
        );
        bundle.putString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, voucherMessage
        );
        bundle.putString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_AMOUNT, voucherAmount
        );
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onDigitalCodeSuccess(String voucherCode,
                                     String voucherMessage,
                                     long discountAmount,
                                     long cashBackAmount) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, voucherCode);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, voucherMessage);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT, discountAmount);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CASHBACK_AMOUNT, cashBackAmount);
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onUsePromoCodeClicked() {
        if (platformPageString.equalsIgnoreCase(PLATFORM_PAGE_MARKETPLACE_CART_LIST)) {
            checkoutAnalyticsCart.eventClickAtcCartClickGunakanKodeFormGunakanKodePromoAtauKupon();
        } else {
            checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKupon();
        }

    }


    @Override
    public void onCouponSuccess(
            String promoCode,
            String promoMessage,
            String amount,
            String couponTitle
    ) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, promoCode);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, promoMessage);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_AMOUNT, amount);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, couponTitle);
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onDigitalCouponSuccess(String promoCode,
                                       String promoMessage,
                                       String couponTitle,
                                       long discountAmount,
                                       long cashbackAmount) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, promoCode);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, promoMessage);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT, discountAmount);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CASHBACK_AMOUNT, cashbackAmount);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, couponTitle);
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void sendAnalyticsOnCouponItemClickedCartListPage() {
        checkoutAnalyticsCart.eventClickAtcCartClickKuponFromGunakanKodePromoAtauKupon();
    }

    @Override
    public void sendAnalyticsOnCouponItemClickedCartListPageSuccess() {
        checkoutAnalyticsCart.eventClickCouponCartClickKuponFromGunakanKodePromoAtauKuponSuccess();
    }

    @Override
    public void sendAnalyticsOnCouponItemClickedCartListPageFailed() {
        checkoutAnalyticsCart.eventClickCouponCartClickKuponFromGunakanKodePromoAtauKuponFailed();
    }

    @Override
    public void sendAnalyticsOnCouponItemClicked(String couponName) {
        checkoutAnalyticsCourierSelection.eventClickCouponCourierSelectionClickKuponFromKuponSaya(couponName);
    }

    @Override
    public void sendAnalyticsOnCouponItemClickedCartShipmentPage() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickKuponFromGunakanKodePromoAtauKupon();
    }

    @Override
    public void sendAnalyticsOnCouponItemClickedCartShipmentPageSuccess() {
        checkoutAnalyticsCourierSelection.eventClickCouponCourierSelectionClickKuponFromGunakanKodePromoAtauKuponSuccess();
    }

    @Override
    public void sendAnalyticsOnCouponItemClickedCartShipmentPageFailed() {
        checkoutAnalyticsCourierSelection.eventClickCouponCourierSelectionClickKuponFromGunakanKodePromoAtauKuponFailed();
    }

    @Override
    public void sendAnalyticsImpressionCouponEmptyCartListPage() {
        checkoutAnalyticsCart.eventViewAtcCartImpressionOnPopUpKupon();
    }

    @Override
    public void sendAnalyticsImpressionCouponEmptyShipmentPage() {
        checkoutAnalyticsCourierSelection.eventViewAtcCourierSelectionImpressionOnPopUpKupon();
    }

    @Override
    public void sendAnalyticsScreenNameCoupon() {
        if (platformString.equalsIgnoreCase(MARKETPLACE_STRING)) {
            switch (platformPageString) {
                case PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                    checkoutAnalyticsCart.sendScreenName(this,
                            ConstantTransactionAnalytics.ScreenName.PROMO_PAGE_FROM_CART_TAB_COUPON
                    );
                    break;
                case PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                    checkoutAnalyticsCourierSelection.sendScreenName(
                            this,
                            ConstantTransactionAnalytics.ScreenName.PROMO_PAGE_FROM_CHECKOUT_TAB_COUPON
                    );
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void sendAnalyticsScreenNamePromoCode() {
        if (platformString.equalsIgnoreCase(MARKETPLACE_STRING)) {
            switch (platformPageString) {
                case PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                    checkoutAnalyticsCart.sendScreenName(this,
                            ConstantTransactionAnalytics.ScreenName.PROMO_PAGE_FROM_CART_TAB_PROMO
                    );
                    break;
                case PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                    checkoutAnalyticsCourierSelection.sendScreenName(
                            this,
                            ConstantTransactionAnalytics.ScreenName.PROMO_PAGE_FROM_CHECKOUT_TAB_PROMO
                    );
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void sendAnalyticsOnErrorGetPromoCode(String errorMessage) {
        checkoutAnalyticsCourierSelection.eventViewPromoCourierSelectionValidationErrorVoucherPromoFromGunakanKodePromoAtauKupon(errorMessage);
        if (platformString.equals(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING)) {
            if (platformPageString.equals(PLATFORM_PAGE_MARKETPLACE_CART_LIST)) {
                checkoutAnalyticsCart.eventClickCouponCartClickGunakanKodeFormGunakanKodePromoAtauKuponFailed();
            } else if (platformPageString.equals(PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT)) {
                checkoutAnalyticsCourierSelection.eventClickCouponCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKuponFailed();
            }
        }
    }

    public static Intent newInstanceCouponActive(Activity activity, String platform, String categoryId, String cartId) {
        Intent intent = new Intent(activity, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, categoryId);
        bundle.putString(EXTRA_CART_ID, cartId);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceCouponActiveAndSelected(Context context, String platform, String categoryId, String cartId) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putInt(EXTRA_SELECTED_TAB, COUPON_TAB);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, categoryId);
        bundle.putString(EXTRA_CART_ID, cartId);
        intent.putExtras(bundle);
        return intent;
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            hideKeyboard();
        }

        private void hideKeyboard() {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    }

    @Deprecated
    public static Intent newInstanceCouponActiveAndSelected(Context context, String platform, String categoryId) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putInt(EXTRA_SELECTED_TAB,
                COUPON_TAB);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, categoryId);
        intent.putExtras(bundle);
        return intent;
    }

    @Deprecated
    public static Intent newInstanceCouponActive(Context context, String platform, String category) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, category);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceCouponActive(Context context, String platform, String category, String defaultSelectedTab) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, category);
        if (!TextUtils.isEmpty(defaultSelectedTab) && defaultSelectedTab.contains(DEFAULT_COUPON_TAB_SELECTED)) {
            bundle.putInt(EXTRA_SELECTED_TAB,
                    COUPON_TAB);
        }
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceCouponNotActive(Context context, String platform, String category) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, false);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, category);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceTrainCouponActive(Context context, String platform, String category,
                                                      String trainReservationId, String trainReservationCode) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, category);
        bundle.putString(EXTRA_TRAIN_RESERVATION_ID, trainReservationId);
        bundle.putString(EXTRA_TRAIN_RESERVATION_CODE, trainReservationCode);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartListCouponNotActive(Context context, String additionalStringData) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, false);
        bundle.putString(EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM_PAGE,
                PLATFORM_PAGE_MARKETPLACE_CART_LIST);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_ADDITIONAL_STRING_DATA,
                additionalStringData);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartListCouponActive(
            Context context, String additionalStringData, String defaultSelectedTab
    ) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM_PAGE,
                PLATFORM_PAGE_MARKETPLACE_CART_LIST);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_ADDITIONAL_STRING_DATA,
                additionalStringData);
        if (!TextUtils.isEmpty(defaultSelectedTab) && defaultSelectedTab.contains(DEFAULT_COUPON_TAB_SELECTED)) {
            bundle.putInt(EXTRA_SELECTED_TAB,
                    COUPON_TAB);
        }
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartShipmentCouponNotActive(Context context, String additionalStringData) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, false);
        bundle.putString(EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM_PAGE,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_ADDITIONAL_STRING_DATA,
                additionalStringData);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartShipmentCouponActive(
            Context context, String additionalStringData, String defaultSelectedTab
    ) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM_PAGE,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_ADDITIONAL_STRING_DATA,
                additionalStringData);
        if (!TextUtils.isEmpty(defaultSelectedTab) && defaultSelectedTab.contains(DEFAULT_COUPON_TAB_SELECTED)) {
            bundle.putInt(EXTRA_SELECTED_TAB,
                    COUPON_TAB);
        }
        intent.putExtras(bundle);
        return intent;
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_hachiko)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.sp_12)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (platformString.equalsIgnoreCase(MARKETPLACE_STRING))
            checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickBackArrowFromGunakanKodePromoAtauKupon();
        loyaltyModuleRouter.sendEventCouponPageClosed();
    }

    private class OnTabSelectedForTrackingCheckoutMarketPlace implements
            TabLayout.OnTabSelectedListener {
        private final boolean doubleTab;

        public OnTabSelectedForTrackingCheckoutMarketPlace(boolean doubleTab) {
            this.doubleTab = doubleTab;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (doubleTab) {
                if (tab.getPosition() == 1) {
                    switch (platformPageString) {
                        case PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                            checkoutAnalyticsCart.eventClickAtcCartClickKuponSayaFromGunakanPromoAtauKupon();
                            break;
                        case PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                            checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickKuponSayaFromGunakanKodePromoAtauKupon();
                            break;
                    }
                } else {
                    switch (platformPageString) {
                        case PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                            checkoutAnalyticsCart.eventClickAtcCartClickKodePromoFromGunakanPromoAtauKupon();
                            break;
                        case PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                            checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickKodePromoFromGunakanKodePromoAtauKupon();
                            break;
                    }
                }
            } else {
                if (tab.getPosition() == 0) {
                    switch (platformPageString) {
                        case PLATFORM_PAGE_MARKETPLACE_CART_LIST:
                            checkoutAnalyticsCart.eventClickAtcCartClickKodePromoFromGunakanPromoAtauKupon();
                            break;
                        case PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT:
                            checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickKodePromoFromGunakanKodePromoAtauKupon();
                            break;
                    }
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
