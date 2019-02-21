package com.tokopedia.digital.product.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.RequestPermissionUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.design.component.ticker.TickerView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.compoundview.ClientNumberInputView;
import com.tokopedia.digital.product.additionalfeature.etoll.view.activity.DigitalCheckETollBalanceNFCActivity;
import com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview.CheckETollBalanceView;
import com.tokopedia.digital.product.di.DigitalProductComponentInstance;
import com.tokopedia.digital.product.receiver.USSDBroadcastReceiver;
import com.tokopedia.digital.product.service.USSDAccessibilityService;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.activity.DigitalSearchNumberActivity;
import com.tokopedia.digital.product.view.activity.DigitalUssdActivity;
import com.tokopedia.digital.product.view.adapter.PromoGuidePagerAdapter;
import com.tokopedia.digital.product.view.compoundview.CheckPulsaBalanceView;
import com.tokopedia.digital.product.view.compoundview.DigitalWrapContentViewPager;
import com.tokopedia.digital.product.view.listener.IProductDigitalView;
import com.tokopedia.digital.product.view.listener.IUssdUpdateListener;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.ContactData;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.product.view.presenter.ProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.digital.product.view.activity.DigitalSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER;

/**
 * @author anggaprasetiyo on 4/25/17.
 */
@RuntimePermissions
public class DigitalProductFragment extends BaseDaggerFragment
        implements IProductDigitalView, BaseDigitalProductView.ActionListener, IUssdUpdateListener,
        CheckPulsaBalanceView.ActionListener {

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

    private static final String DIGITAL_SMARTCARD = "mainapp_digital_smartcard";
    private static final String DIGITAL_DETAIL_TRACE = "dg_detail";

    private static final int DEFAULT_POST_DELAYED_VALUE = 500;
    private static final int PANDUAN_TAB_POSITION = 1;
    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final int REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER = 1002;
    private static final int REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER = 1003;
    private static final int REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 1004;
    private static final int REQUEST_CODE_CONTACT_PICKER = 1005;
    private static final int REQUEST_CODE_CART_DIGITAL = 1006;

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

    private CheckPulsaBalanceView selectedCheckPulsaBalanceView;

    private BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> digitalProductView;

    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    private ActionListener actionListener;

    private FirebaseRemoteConfigImpl remoteConfig;

    private DigitalAnalytics digitalAnalytics;

    private USSDBroadcastReceiver ussdBroadcastReceiver;
    private ShowCaseDialog showCaseDialog;
    private int selectedSimIndex = 0;//start from 0
    private boolean ussdInProgress = false;
    private PromoGuidePagerAdapter promoGuidePagerAdapter;

    private boolean isCouponApplied;
    private boolean traceStop;
    private PerformanceMonitoring performanceMonitoring;

    private SaveInstanceCacheManager saveInstanceCacheManager;

    @Inject
    ProductDigitalPresenter presenter;
    @Inject
    UserSession userSession;
    @Inject
    DigitalModuleRouter digitalModuleRouter;

    public static Fragment newInstance(
            String categoryId, String operatorId, String productId, String clientNumber,
            boolean isFromWidget, boolean isCouponApplied, String additionalETollBalance,
            String additionalETollLastUpdatedDate) {
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
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(DIGITAL_DETAIL_TRACE);
        saveInstanceCacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        if (getActivity().getApplicationContext() instanceof AbstractionRouter) {
            digitalAnalytics = new DigitalAnalytics(((AbstractionRouter) getActivity().getApplicationContext()).getAnalyticTracker(), getActivity());
        }
        View view = inflater.inflate(R.layout.fragment_product_digital_module, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        renderViewShadow();
        setupArguments(getArguments());

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
                presenter.processGetHelpUrlData(categoryId);
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
    }

    protected void initView(View view) {
        pbMainLoading = view.findViewById(R.id.pb_main_loading);
        holderProductDetail = view.findViewById(R.id.holder_product_detail);
        holderCheckBalance = view.findViewById(R.id.holder_check_balance);
        checkETollBalanceView = view.findViewById(R.id.holder_check_emoney_balance);
        promoTabLayout = view.findViewById(R.id.indicator);
        promoViewPager = view.findViewById(R.id.pager);
        containerPromo = view.findViewById(R.id.container_promo);
        mainHolderContainer = view.findViewById(R.id.main_container);

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

        selectedCheckPulsaBalanceView = null;
        checkETollBalanceView.setListener(() -> {
            Intent intent = DigitalCheckETollBalanceNFCActivity.newInstance(getActivity());
            startActivity(intent);
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
        actionListener.updateTitleToolbar(categoryData.getName());
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
    public void renderCheckPulsaBalanceData(int selectedSim, String ussdCode, String phoneNumber, String operatorErrorMsg, Boolean isSimActive, String carrierName) {
        CheckPulsaBalanceView checkPulsaBalanceView = new CheckPulsaBalanceView(getActivity());
        checkPulsaBalanceView.setActionListener(this);
        checkPulsaBalanceView.renderData(selectedSim, ussdCode, phoneNumber, operatorErrorMsg, isSimActive, carrierName);
        holderCheckBalance.addView(checkPulsaBalanceView);
        startShowCaseUSSD();
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
    public void removeCheckPulsaCards() {
        holderCheckBalance.removeAllViews();
    }

    @Override
    public void showHelpMenu(String url) {
        digitalHelpUrl = url;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public String getHelpUrl() {
        return digitalHelpUrl;
    }

    @Override
    public void navigateToWebview(String helpUrl) {
        if (getActivity() != null && getActivity().getApplication() instanceof DigitalModuleRouter) {
            Intent intent = ((DigitalModuleRouter) getActivity().getApplication())
                    .getDefaultContactUsIntent(getActivity(), helpUrl, getString(R.string.digital_product_help_menu_label));
            if (intent != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean isDigitalSmartcardEnabled() {
        return remoteConfig.getBoolean(DIGITAL_SMARTCARD, false);
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
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        Intent intent = ((DigitalModuleRouter) getContext().getApplicationContext())
                .getLoginIntent(getActivity());
        navigateToActivityRequest(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public Map<String, String> getGeneratedAuthParamNetwork(
            Map<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), originParams);
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
        digitalAnalytics.eventClickBuyOnNative(categoryDataState.getName(), isInstant);

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
            if (getActivity().getApplication() instanceof DigitalRouter) {
                DigitalRouter digitalModuleRouter =
                        (DigitalRouter) getActivity().getApplication();
                navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        REQUEST_CODE_CART_DIGITAL
                );
            }
        } else {
            interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
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
            if (getActivity().getApplication() instanceof DigitalRouter) {
                DigitalRouter digitalModuleRouter =
                        (DigitalRouter) getActivity().getApplication();
                navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        REQUEST_CODE_CART_DIGITAL
                );
                getActivity().overridePendingTransition(0, 0);
            }
        } else {
            interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }

    @Override
    public void onButtonCheckBalanceClicked(int simPosition, String ussdCode, CheckPulsaBalanceView checkPulsaBalanceView) {
        if (ussdInProgress) {
            showToastMessage(getString(R.string.msg_ussd_please_wait));
        } else {
            selectedSimIndex = simPosition;
            selectedCheckPulsaBalanceView = checkPulsaBalanceView;
            Operator operator = presenter.getSelectedUssdOperator(simPosition);
            String phoneNumber = presenter.getUssdPhoneNumberFromCache(simPosition);
            String carrierName = DeviceUtil.getOperatorName(getActivity(), simPosition);
            if (carrierName != null && !presenter.isCarrierSignalsNotAvailable(carrierName)
                    && !DeviceUtil.validateNumberAndMatchOperator(categoryDataState.getClientNumberList().get(0).getValidation(),
                    operator, phoneNumber)) {
                presenter.storeUssdPhoneNumber(simPosition, "");
            }
            DigitalProductFragmentPermissionsDispatcher.checkBalanceByUSSDWithCheck(this, simPosition, ussdCode);
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
                        categoryDataState.getOperatorLabel(),
                        categoryDataState.getName()
                ),
                REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER
        );
    }

    @Override
    public void onButtonContactPickerClicked() {
        DigitalProductFragmentPermissionsDispatcher.openContactPickerWithCheck(this);
    }

    @Override
    public void onProductDetailLinkClicked(String url) {
        startActivity(digitalModuleRouter.getWebviewActivityWithIntent(getActivity(), url));
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
                    if (data.hasExtra(DigitalRouter.Companion.getEXTRA_MESSAGE())) {
                        String message = data.getStringExtra(DigitalRouter.Companion.getEXTRA_MESSAGE());
                        if (!TextUtils.isEmpty(message)) {
                            showToastMessage(message);
                        }
                    }
                }
                if (isFromWidget) {
                    isFromWidget = false;
                    presenter.processGetHelpUrlData(categoryId);
                    presenter.processGetCategoryAndBannerData(
                            categoryId, operatorId, productId, clientNumber);
                    setMenuVisibility(true);
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
                    if (getActivity().getApplication() instanceof DigitalRouter) {
                        DigitalRouter digitalModuleRouter =
                                (DigitalRouter) getActivity().getApplication();
                        navigateToActivityRequest(
                                digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassDataState),
                                REQUEST_CODE_CART_DIGITAL
                        );
                    }
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
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_digital_product_detail, menu);
        if (GlobalConfig.isSellerApp()) {
            menu.findItem(R.id.action_menu_subscription_digital).setVisible(false);
            menu.findItem(R.id.action_menu_product_list_digital).setVisible(false);
        }
        if (digitalHelpUrl != null && digitalHelpUrl.length() > 0) {
            menu.findItem(R.id.action_menu_help_digital).setVisible(true);
        } else {
            menu.findItem(R.id.action_menu_help_digital).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_product_list_digital) {
            navigateToActivity(
                    digitalModuleRouter.getWebviewActivityWithIntent(
                            getActivity(), TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_PRODUCT_LIST
                    )
            );
            return true;
        } else if (item.getItemId() == R.id.action_menu_subscription_digital) {
            navigateToActivity(
                    digitalModuleRouter.getWebviewActivityWithIntent(
                            getActivity(), TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_SUBSCRIPTIONS
                    )
            );
            return true;
        } else if (item.getItemId() == R.id.action_menu_transaction_list_digital) {
            if (categoryDataState != null) {
                digitalAnalytics.eventClickDaftarTransaksiEvent(categoryDataState.getName());
            }
            if (GlobalConfig.isSellerApp()) {
                navigateToActivity(
                        digitalModuleRouter.getWebviewActivityWithIntent(getActivity(), TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                + TkpdBaseURL.DigitalWebsite.PATH_TRANSACTION_LIST)
                );
            } else {
                RouteManager.route(getActivity(), ApplinkConst.DIGITAL_ORDER);
            }
            return true;
        } else if (item.getItemId() == R.id.action_menu_help_digital) {
            presenter.onHelpMenuClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DigitalProductFragmentPermissionsDispatcher.onRequestPermissionsResult(
                this, requestCode, grantResults
        );
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
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
            NetworkErrorHelper.showSnackbar(getActivity(),
                    getString(R.string.error_message_contact_not_found));
        }
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForContacts() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_CONTACTS);
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showNeverAskForContacts() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_CONTACTS);
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForContacts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(),
                new RequestPermissionUtil.PermissionRequestListener() {
                    @Override
                    public void onProceed() {
                        request.proceed();
                    }

                    @Override
                    public void onCancel() {
                        request.cancel();
                    }
                }, Manifest.permission.READ_CONTACTS);
    }

    @NeedsPermission({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    public void checkBalanceByUSSD(int simPosition, String ussdCode) {
        presenter.processToCheckBalance(null, simPosition, ussdCode);

        digitalAnalytics.eventUssd(
                categoryDataState.getName(),
                String.format("%s - %s", DeviceUtil.getOperatorName(getActivity(), simPosition),
                        presenter.getDeviceMobileNumber(simPosition)
                )
        );
        digitalAnalytics.eventUssdAttempt(categoryDataState.getName(),
                getString(R.string.ussd_permission_allowed_label));
    }

    @OnPermissionDenied({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void showDeniedForPhone() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CALL_PHONE);
        digitalAnalytics.eventUssdAttempt(categoryDataState.getName(),
                getString(R.string.ussd_permission_denied_label));
    }

    @OnNeverAskAgain({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void showNeverAskForPhone() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CALL_PHONE);
        digitalAnalytics.eventUssdAttempt(categoryDataState.getName(),
                getString(R.string.ussd_permission_denied_label));
    }

    private void renderContactDataToClientNumber(ContactData contactData) {
        digitalProductView.renderClientNumber(contactData.getContactNumber());
    }

    private void handleCallbackSearchNumber(OrderClientNumber orderClientNumber) {
        if (orderClientNumber != null) {
            digitalAnalytics.eventSelectNumberOnUserProfileNative(categoryDataState.getName());
        }
        if (categoryDataState.isSupportedStyle()) {
            switch (categoryDataState.getOperatorStyle()) {
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
            for (Operator operator : categoryDataState.getOperatorList()) {
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
            for (Operator operator : categoryDataState.getOperatorList()) {
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
        digitalProductView.renderUpdateProductSelected(product);
    }

    private void handleCallBackOperatorChooser(Operator operator) {
        digitalProductView.renderUpdateOperatorSelected(operator);
    }

    @Override
    public void showMessageAlert(String message, String title) {
        View view = getView();
        if (view == null) {
            return;
        }
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        Dialog dialog = alertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void showAccessibilityAlertDialog() {
        View view = getView();
        if (view == null) {
            return;
        }
        AlertDialog.Builder accessibiltyDialog = new AlertDialog.Builder(getActivity());
        accessibiltyDialog.setMessage(getActivity().getString(R.string.dialog_accessibility_service_on));
        accessibiltyDialog.setPositiveButton("ALLOW", (dialogInterface, i) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, 0);
        });
        accessibiltyDialog.setNegativeButton("DENY", (dialogInterface, i) -> {

        });
        Dialog dialog = accessibiltyDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void registerUssdReciever() {
        if (selectedCheckPulsaBalanceView != null)
            selectedCheckPulsaBalanceView.showCheckBalanceProgressbar();
        if (ussdBroadcastReceiver == null) {
            ussdBroadcastReceiver = new USSDBroadcastReceiver(this);
            getActivity().registerReceiver(ussdBroadcastReceiver, new IntentFilter(
                    USSDBroadcastReceiver.ACTION_GET_BALANCE_FROM_USSD
            ));
        }
        Intent intent = new Intent(getActivity(), USSDAccessibilityService.class);
        intent.putExtra(USSDAccessibilityService.KEY_START_SERVICE_FROM_APP, true);
        getActivity().startService(intent);
        ussdInProgress = true;
    }

    @Override
    public void renderPulsaBalance(PulsaBalance pulsaBalance, int selectedSim) {
        if (getActivity() != null) {
            ussdInProgress = false;
            String number = "";
            if (selectedCheckPulsaBalanceView != null) {
                selectedCheckPulsaBalanceView.hideProgressbar();
                number = selectedCheckPulsaBalanceView.getPhoneNumberText();
            }
            if (pulsaBalance != null && pulsaBalance.isSuccess()) {
                pulsaBalance.setMobileNumber(number);
                digitalAnalytics.eventUssdAttempt(categoryDataState.getName(), getString(R.string.status_success_label));
                startActivity(DigitalUssdActivity.newInstance(getActivity(), pulsaBalance, presenter.getSelectedUssdOperator(selectedSim),
                        categoryDataState.getClientNumberList().get(0).getValidation(),
                        categoryId, categoryDataState.getName(), selectedSim, presenter.getSelectedUssdOperatorList(selectedSim)));
            } else {
                showMessageAlert(getActivity().getString(R.string.error_message_ussd_msg_not_parsed), getActivity().getString(R.string.message_ussd_title));
                digitalAnalytics.eventUssdAttempt(categoryDataState.getName(), getString(R.string.status_failed_label) + getString(R.string.error_message_ussd_msg_not_parsed));
            }
        }
    }

    private void startShowCaseUSSD() {
        final String showCaseTag = DigitalProductFragment.class.getName();
        if (ShowCasePreference.hasShown(getActivity(), showCaseTag)) {
            return;
        }
        if (showCaseDialog != null) {
            return;
        }
        showCaseDialog = createShowCase();
        showCaseDialog.setShowCaseStepListener((previousStep, nextStep, showCaseObject) -> false);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();
        showCaseObjectList.add(new ShowCaseObject(
                holderCheckBalance,
                getString(R.string.title_showcase_ussd),
                getString(R.string.message_showcase_ussd),
                ShowCaseContentPosition.UNDEFINED,
                R.color.tkpd_main_green));
        showCaseDialog.show(getActivity(), showCaseTag, showCaseObjectList);
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .customView(R.layout.view_layout_showcase)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.dp_12)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .prevStringRes(R.string.digital_navigate_back_showcase)
                .nextStringRes(R.string.next)
                .finishStringRes(R.string.digital_navigate_done_showcase)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(false)
                .build();
    }

    @Override
    public void showPulsaBalanceError(String message) {
        if (getActivity() != null) {
            ussdInProgress = false;
            if (selectedCheckPulsaBalanceView != null)
                selectedCheckPulsaBalanceView.hideProgressbar();
            showMessageAlert(message, getActivity().getString(R.string.message_ussd_title));
        }
    }

    @Override
    public void onReceivedUssdData(String result) {
        presenter.processPulsaBalanceUssdResponse(result, selectedSimIndex);
    }

    @Override
    public void onUssdDataError(String errorMessage) {

    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.renderCheckPulsa();
    }

    @Override
    public void onDestroy() {
        if (ussdBroadcastReceiver != null)
            getActivity().unregisterReceiver(ussdBroadcastReceiver);
        presenter.removeUssdTimerCallback();

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
                        digitalAnalytics.eventClickPanduanPage(categoryDataState.getName());
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

            holderCheckBalance.setBackgroundResource(R.color.white);
            holderProductDetail.setBackgroundResource(R.color.white);
            checkETollBalanceView.setBackgroundResource(R.color.white);
            containerPromo.setBackgroundResource(R.color.white);
        } else {
            holderCheckBalance.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);
            holderProductDetail.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);
            checkETollBalanceView.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);
            containerPromo.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);
        }
    }

    private View getTickerCouponApplied() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_digital_ticker_coupon_applied_view, null);

        TickerView tickerView = view.findViewById(R.id.ticker_view);
        setupTickerCouponApplied(tickerView);

        return view;
    }

    private void setupTickerCouponApplied(TickerView tickerView) {
        ArrayList<String> messages = new ArrayList<>();
        messages.add(getString(R.string.digital_coupon_applied_ticker_message));
        tickerView.setVisibility(View.INVISIBLE);
        tickerView.setListMessage(messages);
        tickerView.setHighLightColor(ContextCompat.getColor(getContext(), R.color.green_200));
        tickerView.buildView();

        tickerView.postDelayed(() -> {
            tickerView.setItemPadding(
                    getResources().getDimensionPixelSize(R.dimen.dp_10),
                    getResources().getDimensionPixelSize(R.dimen.dp_15),
                    getResources().getDimensionPixelSize(R.dimen.dp_10),
                    getResources().getDimensionPixelSize(R.dimen.dp_15)
            );
            tickerView.setItemTextAppearance(R.style.TextView_Micro);
        }, DEFAULT_POST_DELAYED_VALUE);
    }

}