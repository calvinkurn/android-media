package com.tokopedia.digital.product.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConsInternalDigital;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams;
import com.tokopedia.common_digital.cart.DigitalCheckoutUtil;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.RechargeAnalytics;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.compoundview.ClientNumberInputView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.CheckETollBalanceView;
import com.tokopedia.digital.product.di.DigitalProductComponentInstance;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.activity.DigitalSearchNumberActivity;
import com.tokopedia.digital.product.view.adapter.PromoGuidePagerAdapter;
import com.tokopedia.digital.product.view.compoundview.DigitalWrapContentViewPager;
import com.tokopedia.digital.product.view.listener.IProductDigitalView;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.ContactData;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.product.view.presenter.ProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.permission.PermissionCheckerHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.digital.product.view.activity.DigitalSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER;

/**
 * @author anggaprasetiyo on 4/25/17.
 * Routing applink to add to cart use isFromWidget and button back will go back to caller page
 */
public class DigitalProductFragment extends BaseDaggerFragment
        implements IProductDigitalView, BaseDigitalProductView.ActionListener {

    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";
    private static final String ARG_PARAM_EXTRA_OPERATOR_ID = "ARG_PARAM_EXTRA_OPERATOR_ID";
    private static final String ARG_PARAM_EXTRA_PRODUCT_ID = "ARG_PARAM_EXTRA_PRODUCT_ID";
    private static final String ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER";
    private static final String ARG_PARAM_EXTRA_IS_FROM_WIDGET = "ARG_PARAM_EXTRA_IS_FROM_WIDGET";
    private static final String ARG_PARAM_EXTRA_IS_COUPON_APPLIED = "ARG_PARAM_EXTRA_IS_COUPON_APPLIED";
    private static final String ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_BALANCE =
            "ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_BALANCE";
    private static final String ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_UPDATE_DATE =
            "ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_UPDATE_DATE";
    private static final String ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_OPERATOR_NAME =
            "ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_OPERATOR_NAME";
    private static final String ARG_PARAM_EXTRA_RECHARGE_SLICE =
            "ARG_PARAM_EXTRA_RECHARGE_SLICE";

    private static final String EXTRA_STATE_OPERATOR_SELECTED = "EXTRA_STATE_OPERATOR_SELECTED";
    private static final String EXTRA_STATE_PRODUCT_SELECTED = "EXTRA_STATE_PRODUCT_SELECTED";
    private static final String EXTRA_STATE_CLIENT_NUMBER = "EXTRA_STATE_CLIENT_NUMBER";
    private static final String EXTRA_STATE_CATEGORY_DATA = "EXTRA_STATE_CATEGORY_DATA";
    private static final String EXTRA_STATE_BANNER_LIST_DATA = "EXTRA_STATE_BANNER_LIST_DATA";
    private static final String EXTRA_STATE_OTHER_BANNER_LIST_DATA = "EXTRA_STATE_OTHER_BANNER_LIST_DATA";
    private static final String EXTRA_STATE_GUIDE_LIST_DATA = "EXTRA_STATE_GUIDE_LIST_DATA";
    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";
    private static final String EXTRA_STATE_INSTANT_CHECKOUT_CHECKED =
            "EXTRA_STATE_INSTANT_CHECKOUT_CHECKED";
    private static final String EXTRA_STATE_HISTORY_CLIENT_NUMBER =
            "EXTRA_STATE_HISTORY_CLIENT_NUMBER";
    private static final String EXTRA_STATE_VOUCHER_CODE_COPIED =
            "EXTRA_STATE_VOUCHER_CODE_COPIED";
    private static final String EXTRA_STATE_HELP_URL = "EXTRA_STATE_HELP_URL";

    private static final String CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL =
            "CLIP_DATA_LABEL_VOUCHER_CODE_DIGITAL";

    private static final String DIGITAL_DETAIL_TRACE = "dg_detail";
    private static final String CLICK_PDP = "clickPDP";
    private static final String DIGITAL_HOMEPAGE = "digital - homepage";
    private static final String CLICK_UPDATE_SALDO = "click update saldo ";
    private static final String CLICK_HOMEPAGE_OCR = "clickHomepage";
    private static final String CATEGORY_OCR = "digital - native";
    private static final String ACTION_OCR = "click camera icon";

    public static final String PATH_TRANSACTION_LIST = "order-list/";
    public static final String PATH_SUBSCRIPTIONS = "subscribe/";

    private static final int DEFAULT_POST_DELAYED_VALUE = 500;
    private static final int PANDUAN_TAB_POSITION = 1;
    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final int REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER = 1002;
    private static final int REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER = 1003;
    private static final int REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 1004;
    private static final int REQUEST_CODE_CONTACT_PICKER = 1005;
    private static final int REQUEST_CODE_CART_DIGITAL = 1006;
    private static final int REQUEST_CODE_CHECK_SALDO_EMONEY = 1007;
    private static final int REQUEST_CODE_CAMERA_OCR = 1008;

    private NestedScrollView mainHolderContainer;
    private ProgressBar pbMainLoading;
    private LinearLayout holderProductDetail;
    private LinearLayout holderCheckBalance;
    private CheckETollBalanceView checkETollBalanceView;
    private TabLayout promoTabLayout;
    private DigitalWrapContentViewPager promoViewPager;
    private LinearLayout containerPromo;

    private Operator operatorSelectedState;
    private Product productSelectedState;
    private String clientNumberState;
    private CategoryData categoryDataState;
    private List<BannerData> bannerDataListState;
    private List<BannerData> otherBannerDataListState;
    private List<GuideData> guideDataListState;
    private HistoryClientNumber historyClientNumberState;
    private String voucherCodeCopiedState;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;
    private String digitalHelpUrl;

    private boolean isInstantCheckoutChecked;

    private String categoryId;
    private String operatorId;
    private String productId;
    private String clientNumber;
    private boolean isFromWidget;
    private String additionalETollLastBalance;
    private String additionalETollLastUpdatedDate;
    private String additionalETollOperatorName;

    private String rechargeParamFromSlice = "";

    private BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> digitalProductView;

    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    private ActionListener actionListener;

    private FirebaseRemoteConfigImpl remoteConfig;

    private DigitalAnalytics digitalAnalytics;

    private PromoGuidePagerAdapter promoGuidePagerAdapter;

    private boolean isCouponApplied;
    private boolean traceStop;
    private PerformanceMonitoring performanceMonitoring;

    private SaveInstanceCacheManager saveInstanceCacheManager;
    private PermissionCheckerHelper permissionCheckerHelper;

    @Inject
    ProductDigitalPresenter presenter;
    @Inject
    UserSessionInterface userSession;
    @Inject
    RechargeAnalytics rechargeAnalytics;

    public static Fragment newInstance(
            String categoryId, String operatorId, String productId, String clientNumber,
            boolean isFromWidget, boolean isCouponApplied, String additionalETollBalance,
            String additionalETollLastUpdatedDate, String additionalETollOperatorName, String rechargeParamFromSlice) {
        Fragment fragment = new DigitalProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_ID, operatorId);
        bundle.putString(ARG_PARAM_EXTRA_PRODUCT_ID, productId);
        bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumber);
        bundle.putBoolean(ARG_PARAM_EXTRA_IS_FROM_WIDGET, isFromWidget);
        bundle.putBoolean(ARG_PARAM_EXTRA_IS_COUPON_APPLIED, isCouponApplied);
        bundle.putString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_BALANCE, additionalETollBalance);
        bundle.putString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_UPDATE_DATE, additionalETollLastUpdatedDate);
        bundle.putString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_OPERATOR_NAME, additionalETollOperatorName);
        bundle.putString(ARG_PARAM_EXTRA_RECHARGE_SLICE, rechargeParamFromSlice);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(DIGITAL_DETAIL_TRACE);
        saveInstanceCacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
        permissionCheckerHelper = new PermissionCheckerHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        digitalAnalytics = new DigitalAnalytics();
        View view = inflater.inflate(R.layout.fragment_digital_product, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        renderViewShadow();
        setupArguments(getArguments());
        if (rechargeParamFromSlice != null && !rechargeParamFromSlice.isEmpty()) {
            digitalAnalytics.onOpenPageFromSlice();
            digitalAnalytics.onClickSliceRecharge(userSession.getUserId(), rechargeParamFromSlice);
        }
        // (Temporary) Ignore unparsable categoryId error
        try {
            if (categoryId != null)
                presenter.trackVisitRechargePushEventRecommendation(Integer.parseInt(categoryId));
        } catch (Exception e) {
            // do nothing
        }

        if (savedInstanceState != null) {
            categoryDataState = saveInstanceCacheManager.get(EXTRA_STATE_CATEGORY_DATA,
                    CategoryData.class, null);
            bannerDataListState = saveInstanceCacheManager.get(EXTRA_STATE_BANNER_LIST_DATA,
                    (new TypeToken<ArrayList<BannerData>>() {
                    }).getType(), new ArrayList<>());
            otherBannerDataListState = saveInstanceCacheManager.get(EXTRA_STATE_OTHER_BANNER_LIST_DATA,
                    (new TypeToken<ArrayList<BannerData>>() {
                    }).getType(), new ArrayList<>());
            guideDataListState = saveInstanceCacheManager.get(EXTRA_STATE_GUIDE_LIST_DATA,
                    (new TypeToken<ArrayList<GuideData>>() {
                    }).getType(), new ArrayList<>());
            digitalCheckoutPassDataState = saveInstanceCacheManager.get(EXTRA_STATE_CHECKOUT_PASS_DATA,
                    DigitalCheckoutPassData.class, null);

            clientNumberState = savedInstanceState.getString(EXTRA_STATE_CLIENT_NUMBER);
            operatorSelectedState = savedInstanceState.getParcelable(EXTRA_STATE_OPERATOR_SELECTED);
            productSelectedState = savedInstanceState.getParcelable(EXTRA_STATE_PRODUCT_SELECTED);
            isInstantCheckoutChecked = savedInstanceState.getBoolean(EXTRA_STATE_INSTANT_CHECKOUT_CHECKED);
            historyClientNumberState = savedInstanceState.getParcelable(EXTRA_STATE_HISTORY_CLIENT_NUMBER);
            voucherCodeCopiedState = savedInstanceState.getString(EXTRA_STATE_VOUCHER_CODE_COPIED);
            digitalHelpUrl = savedInstanceState.getString(EXTRA_STATE_HELP_URL);
            presenter.processStateDataToReRender();
        } else {
            if (!isFromWidget) {
                setHasOptionsMenu(true);
                presenter.processGetCategoryAndBannerData(
                        categoryId, operatorId, productId, clientNumber);
            } else {
                setMenuVisibility(false);
                presenter.getCategoryData(
                        categoryId, operatorId, productId, clientNumber
                );
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (digitalProductView != null) {
            outState.putString(EXTRA_STATE_CLIENT_NUMBER, digitalProductView.getClientNumber());
            outState.putParcelable(
                    EXTRA_STATE_OPERATOR_SELECTED, digitalProductView.getSelectedOperator()
            );
            outState.putParcelable(
                    EXTRA_STATE_PRODUCT_SELECTED, digitalProductView.getSelectedProduct()
            );
            outState.putBoolean(
                    EXTRA_STATE_INSTANT_CHECKOUT_CHECKED,
                    digitalProductView.isInstantCheckoutChecked()
            );
        }
        outState.putString(EXTRA_STATE_VOUCHER_CODE_COPIED, voucherCodeCopiedState);
        outState.putParcelable(EXTRA_STATE_HISTORY_CLIENT_NUMBER, historyClientNumberState);
        outState.putString(EXTRA_STATE_HELP_URL, digitalHelpUrl);
        saveInstanceCacheManager.onSave(outState);
        saveInstanceCacheManager.put(EXTRA_STATE_CATEGORY_DATA, categoryDataState);
        saveInstanceCacheManager.put(EXTRA_STATE_BANNER_LIST_DATA, bannerDataListState);
        saveInstanceCacheManager.put(EXTRA_STATE_OTHER_BANNER_LIST_DATA, otherBannerDataListState);
        saveInstanceCacheManager.put(EXTRA_STATE_GUIDE_LIST_DATA, guideDataListState);
        saveInstanceCacheManager.put(EXTRA_STATE_CHECKOUT_PASS_DATA, digitalCheckoutPassDataState);
    }

    @Override
    protected void initInjector() {
        DigitalProductComponentInstance.getDigitalProductComponent(getActivity().getApplication())
                .inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        actionListener = (ActionListener) context;
    }

    protected void setupArguments(Bundle arguments) {
        categoryId = arguments.getString(ARG_PARAM_EXTRA_CATEGORY_ID);
        operatorId = arguments.getString(ARG_PARAM_EXTRA_OPERATOR_ID);
        productId = arguments.getString(ARG_PARAM_EXTRA_PRODUCT_ID);
        clientNumber = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER);
        isFromWidget = arguments.getBoolean(ARG_PARAM_EXTRA_IS_FROM_WIDGET);
        isCouponApplied = arguments.getBoolean(ARG_PARAM_EXTRA_IS_COUPON_APPLIED);
        additionalETollLastBalance = arguments.getString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_BALANCE);
        additionalETollLastUpdatedDate = arguments.getString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_UPDATE_DATE);
        additionalETollOperatorName = arguments.getString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_OPERATOR_NAME);
        rechargeParamFromSlice = arguments.getString(ARG_PARAM_EXTRA_RECHARGE_SLICE);
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initView(View view) {
        pbMainLoading = view.findViewById(com.tokopedia.digital.R.id.pb_main_loading);
        holderProductDetail = view.findViewById(com.tokopedia.digital.R.id.digital_holder_product_detail);
        holderCheckBalance = view.findViewById(com.tokopedia.digital.R.id.digital_holder_check_balance);
        checkETollBalanceView = view.findViewById(com.tokopedia.digital.R.id.digital_holder_check_emoney_balance);
        promoTabLayout = view.findViewById(com.tokopedia.digital.R.id.indicator);
        promoViewPager = view.findViewById(com.tokopedia.digital.R.id.pager);
        containerPromo = view.findViewById(com.tokopedia.digital.R.id.digital_container_promo);
        mainHolderContainer = view.findViewById(com.tokopedia.digital.R.id.main_container);

        mainHolderContainer.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mainHolderContainer.setFillViewport(true);
        mainHolderContainer.setFocusable(true);
        mainHolderContainer.setFocusableInTouchMode(true);
        mainHolderContainer.setOnTouchListener((view1, motionEvent) -> {
            if (view1 instanceof ClientNumberInputView) {
                view1.requestFocusFromTouch();
            } else {
                view1.clearFocus();
            }
            return false;
        });

        checkETollBalanceView.setListener(() -> {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    CLICK_PDP,
                    DIGITAL_HOMEPAGE,
                    CLICK_UPDATE_SALDO + additionalETollOperatorName,
                    additionalETollOperatorName
            ));
            Intent intent = RouteManager.getIntent(getActivity(),
                    ApplinkConsInternalDigital.SMARTCARD, DigitalExtraParam.EXTRA_NFC_FROM_PDP, "false");
            startActivityForResult(intent, REQUEST_CODE_CHECK_SALDO_EMONEY);
        });
    }

    public void renderStateSelectedAllData() {
        digitalProductView.restoreStateData(
                categoryDataState, historyClientNumberState, operatorSelectedState,
                productSelectedState, clientNumberState, isInstantCheckoutChecked
        );
    }

    @Override
    public void renderBannerListData(String title, List<BannerData> bannerDataList) {
        this.bannerDataListState = getBannerDataWithoutEmptyItem(bannerDataList);
        promoGuidePagerAdapter.setBannerDataList(title, bannerDataList);
        promoViewPager.postDelayed(() -> {
            if (promoViewPager != null) {
                promoViewPager.setCurrentItem(0);
                promoViewPager.measureCurrentView(promoViewPager.getChildAt(0));
            }
        }, DEFAULT_POST_DELAYED_VALUE);
    }

    @Override
    public void renderOtherBannerListData(String title, List<BannerData> otherBannerDataList) {
        this.otherBannerDataListState = getBannerDataWithoutEmptyItem(otherBannerDataList);
        promoGuidePagerAdapter.setOtherBannerDataList(title, otherBannerDataList);
    }

    @Override
    public void renderGuideListData(List<GuideData> guideDataList) {
        this.guideDataListState = guideDataList;
        promoGuidePagerAdapter.setGuideDataList(guideDataList);
    }

    private List<BannerData> getBannerDataWithoutEmptyItem(List<BannerData> bannerDataList) {
        for (int i = bannerDataList.size() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(bannerDataList.get(i).getTitle()) &&
                    TextUtils.isEmpty(bannerDataList.get(i).getSubtitle())) {
                bannerDataList.remove(bannerDataList.get(i));
            }
        }
        return bannerDataList;
    }

    @Override
    public void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData, HistoryClientNumber historyClientNumber) {
        this.categoryDataState = categoryData;
        this.historyClientNumberState = historyClientNumber;
        actionListener.updateTitleToolbar(categoryData.name);
        holderProductDetail.removeAllViews();
        if (this.digitalProductView == null) {
            this.digitalProductView = digitalProductView;
        }
        this.digitalProductView.setActionListener(this);
        this.digitalProductView.renderData(categoryData, historyClientNumber);

        if (isCouponApplied) {
            holderProductDetail.addView(getTickerCouponApplied());
        }

        holderProductDetail.addView(this.digitalProductView);
    }

    @Override
    public void stopTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace();
            traceStop = true;
        }
    }

    @Override
    public void sendOpenScreenEventTracking(CategoryData categoryData) {
        if (categoryData.name != null && categoryData.categoryId != null) {
            rechargeAnalytics.eventOpenScreen(
                    userSession.getUserId(),
                    categoryData.name,
                    categoryData.categoryId
            );
            rechargeAnalytics.eventViewPdpPage(categoryData.name, userSession.getUserId());
        }
    }

    @Override
    public void renderCheckETollBalance(String text, String buttonText) {
        if (TextUtils.isEmpty(additionalETollLastBalance)) {
            checkETollBalanceView.setVisibility(View.VISIBLE);
            checkETollBalanceView.showCheckBalance(text, buttonText);
        } else {
            checkETollBalanceView.setVisibility(View.VISIBLE);
            checkETollBalanceView.showRemainingBalance(clientNumber, additionalETollLastBalance,
                    additionalETollLastUpdatedDate);
        }
    }

    @Override
    public boolean isDigitalSmartcardEnabled() {
        return remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_RECHARGE_SMARTCARD, false);
    }

    @Override
    public void renderErrorStyleNotSupportedProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorHttpProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorNoConnectionProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public void renderErrorTimeoutConnectionProductDigitalData(String message) {
        showSnackBarCallbackCloseView(message);
    }

    @Override
    public CategoryData getCategoryDataState() {
        return categoryDataState;
    }

    @Override
    public List<BannerData> getBannerDataListState() {
        return bannerDataListState;
    }

    @Override
    public List<BannerData> getOtherBannerDataListState() {
        return otherBannerDataListState;
    }

    @Override
    public List<GuideData> getGuideDataListState() {
        return guideDataListState;
    }

    @Override
    public HistoryClientNumber getHistoryClientNumberState() {
        return historyClientNumberState;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showInitialProgressLoading() {
        pbMainLoading.setVisibility(View.VISIBLE);
        mainHolderContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideInitialProgressLoading() {
        pbMainLoading.setVisibility(View.GONE);
        mainHolderContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearContentRendered() {
        if (pbMainLoading != null) {
            pbMainLoading.setVisibility(View.GONE);
        }
        if (mainHolderContainer != null) {
            mainHolderContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) {
            Toaster.build(view, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show();
        } else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackBar(String message) {
        View view = getView();
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSnackBarCallbackCloseView(String message) {
        View view = getView();
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    clearContentRendered();
                    closeView();
                }
            });
            snackbar.show();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            closeView();
        }
    }

    @Override
    public boolean isUserLoggedIn() {
        return userSession.isLoggedIn();
    }

    @Override
    public void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData) {
        this.digitalCheckoutPassDataState = digitalCheckoutPassData;
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
        navigateToActivityRequest(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public Map<String, String> getGeneratedAuthParamNetwork(
            Map<String, String> originParams) {
        return AuthHelper.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), originParams);
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return DeviceUtil.getDigitalIdentifierParam(getActivity());
    }

    @Override
    public void closeView() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onButtonBuyClicked(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
                                   boolean isInstantCheckoutChecked) {
        String isInstant = isInstantCheckoutChecked ? "instant" : "no instant";
        digitalAnalytics.eventClickBuyOnNative(categoryDataState.name, isInstant);

        if (!preCheckoutProduct.isCanBeCheckout()) {
            if (!TextUtils.isEmpty(preCheckoutProduct.getErrorCheckout())) {
                showToastMessage(preCheckoutProduct.getErrorCheckout());
            }
            return;
        }
        preCheckoutProduct.setVoucherCodeCopied(voucherCodeCopiedState);

        DigitalCheckoutPassData digitalCheckoutPassData = presenter.generateCheckoutPassData(preCheckoutProduct,
                GlobalConfig.VERSION_NAME,
                userSession.getUserId());

        if (userSession.isLoggedIn()) {
            presenter.addToCart(digitalCheckoutPassData, DeviceUtil.getDigitalIdentifierParam(getContext()), new DigitalSubscriptionParams());
        } else {
            interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }

    @Override
    public void navigateToDigitalCart(DigitalCheckoutPassData digitalCheckoutPassData) {
        Intent intent = RouteManager.getIntent(getActivity(), DigitalCheckoutUtil.Companion.getApplinkCartDigital(getActivity()));
        intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, digitalCheckoutPassData);
        startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL);
    }

    @Override
    public void onBuyButtonLoading(Boolean showLoading) {
        digitalProductView.onBuyButtonLoading(showLoading);
    }

    @Override
    public void showPromoContainer() {
        containerPromo.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToCartPage(ProductDigitalData productDigitalData) {
        DigitalCheckoutPassData digitalCheckoutPassData = presenter.generateCheckoutPassData2(productDigitalData,
                categoryId,
                operatorId,
                productId,
                clientNumber,
                GlobalConfig.VERSION_NAME,
                userSession.getUserId());

        if (userSession.isLoggedIn()) {
            presenter.addToCart(digitalCheckoutPassData, DeviceUtil.getDigitalIdentifierParam(getContext()), new DigitalSubscriptionParams());
        } else {
            interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }

    @Override
    public void onProductChooserClicked(List<Product> productListData, String operatorId, String titleChooser) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceProductChooser(
                        getActivity(), categoryId, operatorId, titleChooser
                ),
                REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
        );
    }

    @Override
    public void onOperatorChooserStyle3Clicked(List<Operator> operatorListData, String titleChooser) {
        startActivityForResult(
                DigitalChooserActivity.newInstanceOperatorChooser(
                        getActivity(), categoryId, titleChooser,
                        categoryDataState.operatorLabel,
                        categoryDataState.name
                ),
                REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER
        );
    }

    @Override
    public void onButtonContactPickerClicked() {
        permissionCheckerHelper.checkPermission(getActivity(),
                PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT, new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        permissionCheckerHelper.onPermissionDenied(getActivity(), permissionText);
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        permissionCheckerHelper.onNeverAskAgain(getActivity(), permissionText);
                    }

                    @Override
                    public void onPermissionGranted() {
                        openContactPicker();
                    }
                }, "");
    }

    @Override
    public void onButtonCameraPickerClicked() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_HOMEPAGE_OCR, CATEGORY_OCR, ACTION_OCR, ""));
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConsInternalDigital.CAMERA_OCR);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_OCR);
    }

    @Override
    public void onProductDetailLinkClicked(String url) {
        RouteManager.route(getContext(), url);
    }

    @Override
    public boolean isRecentInstantCheckoutUsed(String categoryId) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    getActivity(), DigitalCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        return cacheHandlerRecentInstantCheckoutUsed.getBoolean(
                DigitalCache.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, false
        );
    }

    @Override
    public void storeLastInstantCheckoutUsed(String categoryId, boolean checked) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    getActivity(), DigitalCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        cacheHandlerRecentInstantCheckoutUsed.putBoolean(
                DigitalCache.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, checked
        );
        cacheHandlerRecentInstantCheckoutUsed.applyEditor();
    }

    @Override
    public void onClientNumberClicked(String number, ClientNumber clientNumber, List<OrderClientNumber> numberList) {
        if (!numberList.isEmpty()) {
            startActivityForResult(
                    DigitalSearchNumberActivity.newInstance(
                            getActivity(), categoryId, clientNumber, number, numberList
                    ),
                    REQUEST_CODE_DIGITAL_SEARCH_NUMBER
            );
        }
    }

    @Override
    public void onClientNumberCleared(ClientNumber clientNumber, List<OrderClientNumber> numberList) {
        if (!numberList.isEmpty()) {
            startActivityForResult(
                    DigitalSearchNumberActivity.newInstance(
                            getActivity(), categoryId, clientNumber, "", numberList
                    ),
                    REQUEST_CODE_DIGITAL_SEARCH_NUMBER
            );
        }
    }

    @Override
    public void onItemAutocompletedSelected(OrderClientNumber orderClientNumber) {

    }

    @Override
    public void onOperatorSelected(String categoryName, String operatorName) {
        digitalAnalytics.eventSelectOperatorOnNativePage(categoryName,
                operatorName);
    }

    @Override
    public void onProductSelected(String categoryName, String productDesc) {
        digitalAnalytics.eventSelectProductOnNativePage(categoryName,
                productDesc);
    }

    @Override
    public void onInstantCheckoutChanged(String categoryName, boolean isChecked) {
        digitalAnalytics.eventCheckInstantSaldo(categoryName, isChecked);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallBackOperatorChooser(
                            data.getParcelableExtra(DigitalChooserActivity.EXTRA_CALLBACK_OPERATOR_DATA)
                    );
                break;
            case REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallBackProductChooser(
                            data.getParcelableExtra(DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA)
                    );
                break;
            case REQUEST_CODE_CART_DIGITAL:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(DigitalExtraParam.EXTRA_MESSAGE)) {
                        String message = data.getStringExtra(DigitalExtraParam.EXTRA_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            showToastMessage(message);
                        }
                    }
                }
                //handle back button from applink cart widget
                if (isFromWidget) {
                    isFromWidget = false;
                    closeView();
                }
                break;
            case REQUEST_CODE_CONTACT_PICKER:
                try {
                    Uri contactURI = data.getData();
                    ContactData contact = presenter.processGenerateContactDataFromUri(contactURI,
                            getActivity().getContentResolver());
                    renderContactDataToClientNumber(contact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_LOGIN:
                if (isUserLoggedIn() && digitalCheckoutPassDataState != null) {
                    presenter.addToCart(digitalCheckoutPassDataState, DeviceUtil.getDigitalIdentifierParam(getContext()), new DigitalSubscriptionParams());
                }
                break;
            case REQUEST_CODE_DIGITAL_SEARCH_NUMBER:
                if (digitalProductView != null) {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        OrderClientNumber orderClientNumber = data.getParcelableExtra(EXTRA_CALLBACK_CLIENT_NUMBER);
                        handleCallbackSearchNumber(orderClientNumber);
                    } else {
                        handleCallbackSearchNumberCancel();
                    }
                }
                break;
            case REQUEST_CODE_CHECK_SALDO_EMONEY:
                if (checkETollBalanceView != null) {
                    if (resultCode == Activity.RESULT_OK && data != null &&
                            data.getParcelableExtra(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA) != null) {
                        DigitalCategoryDetailPassData passData = data.getParcelableExtra(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA);
                        Bundle bundle = new Bundle();
                        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, passData.getCategoryId());
                        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_ID, passData.getOperatorId());
                        bundle.putString(ARG_PARAM_EXTRA_PRODUCT_ID, passData.getProductId());
                        bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, passData.getClientNumber());
                        bundle.putBoolean(ARG_PARAM_EXTRA_IS_FROM_WIDGET, passData.isFromWidget());
                        bundle.putBoolean(ARG_PARAM_EXTRA_IS_COUPON_APPLIED, passData.isCouponApplied());
                        bundle.putString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_BALANCE, passData.getAdditionalETollBalance());
                        bundle.putString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_LAST_UPDATE_DATE, passData.getAdditionalETollLastUpdatedDate());
                        bundle.putString(ARG_PARAM_EXTRA_ADDITIONAL_ETOLL_OPERATOR_NAME, passData.getAdditionalETollOperatorName());
                        setupArguments(bundle);
                        setHasOptionsMenu(true);
                        //clear cache
                        presenter.processGetCategoryAndBannerData(
                                categoryId, operatorId, productId, clientNumber);
                    }
                }
                break;
            case REQUEST_CODE_CAMERA_OCR:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String clientNumber = data.getStringExtra(DigitalExtraParam.EXTRA_NUMBER_FROM_CAMERA_OCR);
                    showToastMessage(getString(R.string.digital_success_message_scan_ocr));
                    digitalProductView.renderClientNumber(clientNumber);
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_digital_product_detail, menu);
        if (GlobalConfig.isSellerApp()) {
            menu.findItem(R.id.action_menu_subscription_digital).setVisible(false);
        }
        menu.findItem(R.id.action_menu_help_digital).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_subscription_digital) {
            RouteManager.route(getActivity(), TokopediaUrl.Companion.getInstance().getPULSA()
                    + PATH_SUBSCRIPTIONS);
            return true;
        } else if (item.getItemId() == R.id.action_menu_transaction_list_digital) {
            if (categoryDataState != null) {
                digitalAnalytics.eventClickDaftarTransaksiEvent(categoryDataState.name);
            }
            if (GlobalConfig.isSellerApp()) {
                RouteManager.route(getActivity(), TokopediaUrl.Companion.getInstance().getPULSA()
                        + PATH_TRANSACTION_LIST);
            } else {
                RouteManager.route(getActivity(), ApplinkConst.DIGITAL_ORDER);
            }
            return true;
        } else if (item.getItemId() == R.id.action_menu_help_digital) {
            RouteManager.route(getActivity(), ApplinkConst.CONTACT_US_NATIVE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
    }

    public void openContactPicker() {
        Intent contactPickerIntent = new Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        );
        try {
            navigateToActivityRequest(
                    contactPickerIntent, REQUEST_CODE_CONTACT_PICKER
            );
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            View view = getView();
            if (view != null) {
                Toaster.build(view, getString(R.string.error_message_contact_not_found), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show();
            }
        }
    }

    private void renderContactDataToClientNumber(ContactData contactData) {
        digitalProductView.renderClientNumber(contactData.getContactNumber());
    }

    private void handleCallbackSearchNumber(OrderClientNumber orderClientNumber) {
        if (orderClientNumber != null) {
            digitalAnalytics.eventSelectNumberOnUserProfileNative(categoryDataState.name);
        }
        if (categoryDataState.isSupportedStyle()) {
            switch (categoryDataState.operatorStyle) {
                case CategoryData.STYLE_PRODUCT_CATEGORY_1:
                case CategoryData.STYLE_PRODUCT_CATEGORY_99:
                    handleStyle1(orderClientNumber);
                    break;
                case CategoryData.STYLE_PRODUCT_CATEGORY_2:
                case CategoryData.STYLE_PRODUCT_CATEGORY_3:
                case CategoryData.STYLE_PRODUCT_CATEGORY_4:
                case CategoryData.STYLE_PRODUCT_CATEGORY_5:
                    handleStyleOther(orderClientNumber);
                    break;
            }
        }
    }

    private void handleStyleOther(OrderClientNumber orderClientNumber) {
        Operator selectedOperator = null;
        if (orderClientNumber.getOperatorId() != null) {
            for (Operator operator : categoryDataState.operatorList) {
                if (orderClientNumber.getOperatorId().equals(operator.getOperatorId())) {
                    selectedOperator = operator;
                    digitalProductView.renderUpdateOperatorSelected(operator);
                }
            }
        }

        digitalProductView.renderClientNumber(orderClientNumber.getClientNumber());
        digitalProductView.clearFocusOnClientNumber();

        if (selectedOperator != null) {
            for (Product product : selectedOperator.getProductList()) {
                if (orderClientNumber.getProductId() != null) {
                    if (orderClientNumber.getProductId().equals(product.getProductId())) {
                        digitalProductView.renderUpdateProductSelected(product);
                    }
                }
            }
        }
    }

    private void handleStyle1(OrderClientNumber orderClientNumber) {
        digitalProductView.renderClientNumber(orderClientNumber.getClientNumber());
        digitalProductView.clearFocusOnClientNumber();
        if (orderClientNumber.getOperatorId() != null) {
            for (Operator operator : categoryDataState.operatorList) {
                if (orderClientNumber.getOperatorId().equals(operator.getOperatorId())) {
                    for (Product product : operator.getProductList()) {
                        if (orderClientNumber.getProductId() != null) {
                            if (orderClientNumber.getProductId().equals(product.getProductId())) {
                                digitalProductView.renderUpdateProductSelected(product);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleCallbackSearchNumberCancel() {
        digitalProductView.clearFocusOnClientNumber();
    }

    private void handleCallBackProductChooser(Product product) {
        if (digitalProductView != null) digitalProductView.renderUpdateProductSelected(product);
    }

    private void handleCallBackOperatorChooser(Operator operator) {
        if (digitalProductView != null) digitalProductView.renderUpdateOperatorSelected(operator);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (digitalProductView != null && categoryDataState != null) {
            Operator selectedOperator = digitalProductView.getSelectedOperator();
            Product selectedProduct = digitalProductView.getSelectedProduct();

            presenter.storeLastClientNumberTyped(
                    categoryId,
                    selectedOperator != null ? selectedOperator.getOperatorId() : "",
                    digitalProductView.getClientNumber(),
                    selectedProduct != null ? selectedProduct.getProductId() : "");
        }
    }

    public interface ActionListener {
        void updateTitleToolbar(String title);
    }

    @Override
    public void renderPromoGuideTab(int tabCount, String firstTab) {
        promoTabLayout.setupWithViewPager(promoViewPager);
        promoViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(promoTabLayout));
        promoViewPager.setAdapter(getViewPagerAdapter(tabCount, firstTab));
        promoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == PANDUAN_TAB_POSITION) {
                    if (digitalAnalytics != null)
                        digitalAnalytics.eventClickPanduanPage(categoryDataState.name);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private PagerAdapter getViewPagerAdapter(int tabCount, String firstTab) {
        promoGuidePagerAdapter = new PromoGuidePagerAdapter(getChildFragmentManager(), getActivity(),
                tabCount, firstTab);
        return promoGuidePagerAdapter;
    }

    @Override
    public void hidePromoGuideTab() {
        promoTabLayout.setVisibility(View.GONE);
        promoViewPager.setVisibility(View.GONE);
    }

    @Override
    public void showPromoGuideTab() {
        promoTabLayout.setVisibility(View.VISIBLE);
        promoViewPager.setVisibility(View.VISIBLE);
    }

    private void renderViewShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holderCheckBalance.setElevation(10);
            holderProductDetail.setElevation(10);
            checkETollBalanceView.setElevation(10);
            containerPromo.setElevation(10);

            holderCheckBalance.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0);
            holderProductDetail.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0);
            checkETollBalanceView.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0);
            containerPromo.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0);
        } else {
            holderCheckBalance.setBackgroundResource(R.drawable.digital_bg_drop_shadow);
            holderProductDetail.setBackgroundResource(R.drawable.digital_bg_drop_shadow);
            checkETollBalanceView.setBackgroundResource(R.drawable.digital_bg_drop_shadow);
            containerPromo.setBackgroundResource(R.drawable.digital_bg_drop_shadow);
        }
    }

    private View getTickerCouponApplied() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_digital_ticker_coupon_applied, null);
        return view;
    }
}