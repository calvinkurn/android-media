package com.tokopedia.digital.categorylist.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.component.ticker.TickerView;
import com.tokopedia.design.widget.WarningTickerView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.categorylist.data.cloud.DigitalCategoryListApi;
import com.tokopedia.digital.categorylist.data.mapper.CategoryDigitalListDataMapper;
import com.tokopedia.digital.categorylist.data.mapper.ICategoryDigitalListDataMapper;
import com.tokopedia.digital.categorylist.data.repository.DigitalCategoryListRepository;
import com.tokopedia.digital.categorylist.domain.interactor.DigitalCategoryListInteractor;
import com.tokopedia.digital.categorylist.view.adapter.DigitalCategoryListAdapter;
import com.tokopedia.digital.categorylist.view.compoundview.DigitalItemHeaderHolder;
import com.tokopedia.digital.categorylist.view.listener.IDigitalCategoryListView;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemDataError;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemHeader;
import com.tokopedia.digital.categorylist.view.presenter.DigitalCategoryListPresenter;
import com.tokopedia.digital.categorylist.view.presenter.IDigitalCategoryListPresenter;
import com.tokopedia.digital.product.view.activity.DigitalProductActivity;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.gcm.Constants.FROM_APP_SHORTCUTS;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListFragment extends BasePresenterFragment<IDigitalCategoryListPresenter>
        implements IDigitalCategoryListView, DigitalCategoryListAdapter.ActionListener,
        RefreshHandler.OnRefreshHandlerListener, DigitalItemHeaderHolder.ActionListener {

    public static final int NUMBER_OF_COLUMN_GRID_CATEGORY_LIST = 4;
    private static final String EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA =
            "EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA";
    private static final String FIREBASE_DIGITAL_OMS_REMOTE_CONFIG_KEY = "app_enable_oms_native";
    public static final String PARAM_IS_COUPON_ACTIVE = "PARAM_IS_COUPON_APPLIED";

    private static final int DEFAULT_DELAY_TIME = 500;

    public static final int DEFAULT_COUPON_APPLIED = 1;
    public static final int DEFAULT_COUPON_NOT_APPLIED = 0;

    private RecyclerView rvDigitalCategoryList;
    private LinearLayout headerContainer;
    private DigitalItemHeaderHolder headerMyTransaction;
    private DigitalItemHeaderHolder headerSubscription;
    private DigitalItemHeaderHolder headerFavNumber;
    private TickerView tickerView;
    private WarningTickerView terminateAnnouncementTicker;
    private LinearLayout terminateAnnouncementLayout;
    private View separatorForTicker;

    private CompositeSubscription compositeSubscription;
    private DigitalCategoryListAdapter adapter;
    private RefreshHandler refreshHandler;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TokoCashData tokoCashBalanceData;
    private List<DigitalCategoryItemData> digitalCategoryListDataState;
    private boolean fromAppShortcut = false;
    private int isCouponApplied = DEFAULT_COUPON_NOT_APPLIED;
    private RemoteConfig remoteConfig;

    public static DigitalCategoryListFragment newInstance() {
        return new DigitalCategoryListFragment();
    }

    public static DigitalCategoryListFragment newInstance(int isCouponApplied) {
        DigitalCategoryListFragment fragment = new DigitalCategoryListFragment();
        Bundle extras = new Bundle();
        extras.putInt(PARAM_IS_COUPON_ACTIVE, isCouponApplied);
        fragment.setArguments(extras);
        return fragment;
    }

    public static DigitalCategoryListFragment newInstance(boolean isFromAppShortcut) {
        Bundle args = new Bundle();
        args.putBoolean(FROM_APP_SHORTCUTS, isFromAppShortcut);
        DigitalCategoryListFragment fragment = new DigitalCategoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelableArrayList(EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA,
                (ArrayList<? extends Parcelable>) digitalCategoryListDataState);
        state.putInt(PARAM_IS_COUPON_ACTIVE, isCouponApplied);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        digitalCategoryListDataState = savedState.getParcelableArrayList(
                EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA
        );
        isCouponApplied = savedState.getInt(PARAM_IS_COUPON_ACTIVE);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isFromAppShortcut()) {
            UnifyTracking.eventBillShortcut(getActivity());
        }
    }

    @Override
    protected void initialPresenter() {
        ICategoryDigitalListDataMapper mapperData = new CategoryDigitalListDataMapper();
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        Retrofit retrofit = RetrofitFactory.createRetrofitDefaultConfig(TkpdBaseURL.MOJITO_DOMAIN)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(
                                OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
                        )
                        .buildClientNoAuth())
                .build();
        DigitalCategoryListApi digitalCategoryListApi = retrofit.create(DigitalCategoryListApi.class);
        presenter = new DigitalCategoryListPresenter(
                new DigitalCategoryListInteractor(
                        compositeSubscription,
                        new DigitalCategoryListRepository(digitalCategoryListApi
                                , new GlobalCacheManager(), mapperData,
                                sessionHandler)), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        if (arguments != null) {
            if (arguments.containsKey(FROM_APP_SHORTCUTS)) {
                fromAppShortcut = arguments.getBoolean(FROM_APP_SHORTCUTS);
            }

            isCouponApplied = arguments.getInt(PARAM_IS_COUPON_ACTIVE, 0);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_digital_category_list_digital_module;
    }

    @Override
    protected void initView(View view) {
        rvDigitalCategoryList = view.findViewById(R.id.rv_digital_category);
        headerContainer = view.findViewById(R.id.header_container);
        headerMyTransaction = view.findViewById(R.id.header_my_transaction);
        headerSubscription = view.findViewById(R.id.header_subscription);
        headerFavNumber = view.findViewById(R.id.header_fav_number);
        tickerView = view.findViewById(R.id.ticker_view);
        separatorForTicker = view.findViewById(R.id.separator_for_ticker);
        terminateAnnouncementTicker = view.findViewById(R.id.ticker_terminate_announcement);
        terminateAnnouncementLayout = view.findViewById(R.id.terminate_announcement_view);

        refreshHandler = new RefreshHandler(getActivity(), view, this);

        if (isCouponApplied == DEFAULT_COUPON_APPLIED) {
            showCouponAppliedTicker();
        } else if (isCouponApplied == DEFAULT_COUPON_NOT_APPLIED) {
            hideCouponAppliedTicker();
        }
    }

    @Override
    protected void setViewListener() {
        rvDigitalCategoryList.setAdapter(adapter);
        headerMyTransaction.setActionListener(this);
        headerFavNumber.setActionListener(this);
        headerSubscription.setActionListener(this);
        headerMyTransaction.setData(
                new DigitalCategoryItemHeader.Builder()
                        .title(getString(
                                R.string.title_header_menu_digital_categories_transaction_list_digital_module
                        ))
                        .siteUrl(TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                + TkpdBaseURL.DigitalWebsite.PATH_TRANSACTION_LIST)
                        .resIconId(R.drawable.ic_digital_homepage_header_my_transaction)
                        .typeMenu(DigitalCategoryItemHeader.TypeMenu.TRANSACTION)
                        .build()
        );

        headerFavNumber.setData(
                new DigitalCategoryItemHeader.Builder()
                        .title(getString(
                                R.string.title_header_menu_digital_categories_favorite_number_digital_module
                        ))
                        .siteUrl(TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                + TkpdBaseURL.DigitalWebsite.PATH_FAVORITE_NUMBER)
                        .resIconId(R.drawable.ic_digital_homepage_header_fav_number)
                        .typeMenu(DigitalCategoryItemHeader.TypeMenu.FAVORITE_NUMBER)
                        .build()
        );

        headerSubscription.setData(
                new DigitalCategoryItemHeader.Builder()
                        .title(getString(
                                R.string.title_header_menu_digital_categories_subscription_digital_module
                        ))
                        .siteUrl(TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                + TkpdBaseURL.DigitalWebsite.PATH_MY_BILLS)
                        .resIconId(R.drawable.ic_digital_homepage_header_mybills)
                        .typeMenu(DigitalCategoryItemHeader.TypeMenu.SUBSCRIPTION)
                        .build()
        );
        headerMyTransaction.invalidate();
        headerFavNumber.invalidate();
        headerSubscription.invalidate();
        headerMyTransaction.setActionListener(this);
        headerSubscription.setActionListener(this);
        headerFavNumber.setActionListener(this);
    }

    @Override
    protected void initialVar() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_OF_COLUMN_GRID_CATEGORY_LIST);
        adapter = new DigitalCategoryListAdapter(this, this, NUMBER_OF_COLUMN_GRID_CATEGORY_LIST);
        initRemoteConfig();
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    public boolean isDigitalOmsEnable() {
        return remoteConfig.getBoolean(FIREBASE_DIGITAL_OMS_REMOTE_CONFIG_KEY, true);

    }

    @Override
    protected void setActionVar() {
        presenter.processGetTokoCashData();
        if (digitalCategoryListDataState == null || digitalCategoryListDataState.isEmpty())
            refreshHandler.startRefresh();
        else renderDigitalCategoryDataList(digitalCategoryListDataState);
    }

    @Override
    public void renderDigitalCategoryDataList(List<DigitalCategoryItemData> digitalCategoryItemDataList) {
        this.digitalCategoryListDataState = digitalCategoryItemDataList;
        refreshHandler.finishRefresh();
        rvDigitalCategoryList.setLayoutManager(gridLayoutManager);
        adapter.addAllDataList(digitalCategoryListDataState);
        if (GlobalConfig.isSellerApp()){
            renderTerminateTicker();
        }
    }

    @Override
    public void renderErrorGetDigitalCategoryList(String message) {
        renderErrorStateData(message);
    }

    @Override
    public void renderErrorHttpGetDigitalCategoryList(String message) {
        renderErrorStateData(message);
    }

    @Override
    public void renderErrorNoConnectionGetDigitalCategoryList(String message) {
        renderErrorStateData(message);
    }

    @Override
    public void renderErrorTimeoutConnectionGetDigitalCategoryList(String message) {
        renderErrorStateData(message);
    }

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }

    @Override
    public boolean isUserLogin() {
        return SessionHandler.isV4Login(getActivity());
    }

    @Override
    public void renderTokoCashData(TokoCashData tokoCashData) {
        this.tokoCashBalanceData = tokoCashData;
    }

    @Override
    public Context getAppContext() {
        return getActivity();
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
    public String getStringFromResource(@StringRes int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return null;
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return null;
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }

    @Override
    public void onDigitalCategoryItemClicked(DigitalCategoryItemData itemData) {
        UnifyTracking.eventClickProductOnDigitalHomepage(getActivity(),itemData.getName().toLowerCase());
        if (itemData.getCategoryId().equalsIgnoreCase(
                String.valueOf(DigitalCategoryItemData.DEFAULT_TOKOCASH_CATEGORY_ID
                )) && tokoCashBalanceData != null && !tokoCashBalanceData.getLink()) {
            WalletRouterUtil.navigateWallet(
                    getActivity().getApplication(),
                    this,
                    IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                    tokoCashBalanceData.getAction().getmAppLinks(),
                    tokoCashBalanceData.getAction().getRedirectUrl(),
                    new Bundle()
            );
        } else {
            if (getActivity().getApplication() instanceof IDigitalModuleRouter)
                if (((IDigitalModuleRouter) getActivity().getApplication())
                        .isSupportedDelegateDeepLink(itemData.getAppLinks())) {
                    DigitalCategoryDetailPassData passData =
                            new DigitalCategoryDetailPassData.Builder()
                                    .appLinks(itemData.getAppLinks())
                                    .categoryId(itemData.getCategoryId())
                                    .categoryName(itemData.getName())
                                    .url(itemData.getRedirectValue())
                                    .build();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DigitalProductActivity.EXTRA_CATEGORY_PASS_DATA, passData);
                    bundle.putBoolean(Constants.EXTRA_APPLINK_FROM_INTERNAL, true);
                    ((IDigitalModuleRouter) getActivity().getApplication())
                            .actionNavigateByApplinksUrl(getActivity(), itemData.getAppLinks(), bundle);
                } else {
                    String redirectValueUrl = itemData.getRedirectValue();
                    if (redirectValueUrl != null && redirectValueUrl.length() > 0) {
                        String resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                                Uri.encode(redirectValueUrl), MainApplication.getAppContext());
                        Intent intent = new Intent(getActivity(), BannerWebView.class);
                        intent.putExtra("url", resultGenerateUrl);
                        navigateToActivity(intent);
                    }
                }
        }
    }

    @Override
    public void onDigitalCategoryRetryClicked() {
        refreshHandler.startRefresh();
    }

    @Override
    public void onRefresh(View view) {
        if (refreshHandler.isRefreshing()) {
            presenter.processGetDigitalCategoryList(getDeviceAndAppVersion());
        }
    }

    private String getDeviceAndAppVersion() {
        if (GlobalConfig.isCustomerApp()) {
            return String.format(getString(R.string.digital_list_device_consumer_app_prefix), GlobalConfig.VERSION_NAME);
        } else if (GlobalConfig.isSellerApp()) {
            return String.format(getString(R.string.digital_list_device_seller_app_prefix), GlobalConfig.VERSION_NAME);
        }
        return "";
    }

    private void renderErrorStateData(String message) {
        refreshHandler.finishRefresh();
        rvDigitalCategoryList.setLayoutManager(linearLayoutManager);
        adapter.addErrorData(new DigitalCategoryItemDataError.Builder().message(message).build());
    }

    @Override
    public void onClickCategoryHeaderMenu(DigitalCategoryItemHeader data) {
        switch (data.getTypeMenu()) {
            case TRANSACTION:
                if (isDigitalOmsEnable()) {
                    if (GlobalConfig.isCustomerApp()) {
                        RouteManager.route(getActivity(), ApplinkConst.DIGITAL_ORDER);
                    } else {
                        startActivity(((IDigitalModuleRouter) getActivity().getApplication()).
                                getOrderListIntent(getActivity()));
                    }
                    break;
                }
            default:
                startActivity(DigitalWebActivity.newInstance(getActivity(), data.getSiteUrl()));
                break;
        }
    }

    public boolean isFromAppShortcut() {
        return fromAppShortcut;
    }

    private void showCouponAppliedTicker() {
        ArrayList<String> messages = new ArrayList<>();
        messages.add(getString(R.string.digital_coupon_applied_ticker_message));
        tickerView.setVisibility(View.INVISIBLE);
        tickerView.setListMessage(messages);
        tickerView.setHighLightColor(ContextCompat.getColor(context, R.color.green_200));
        tickerView.buildView();

        tickerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                tickerView.setItemPadding(
                        getResources().getDimensionPixelSize(R.dimen.dp_10),
                        getResources().getDimensionPixelSize(R.dimen.dp_15),
                        getResources().getDimensionPixelSize(R.dimen.dp_10),
                        getResources().getDimensionPixelSize(R.dimen.dp_15)
                );
                tickerView.setItemTextAppearance(R.style.TextView_Micro);
            }
        }, DEFAULT_DELAY_TIME);

        separatorForTicker.setVisibility(View.VISIBLE);
    }

    private void hideCouponAppliedTicker() {
        tickerView.setVisibility(View.GONE);
        separatorForTicker.setVisibility(View.GONE);
    }

    private void renderTerminateTicker(){
        terminateAnnouncementLayout.setVisibility(View.VISIBLE);
        terminateAnnouncementTicker.setDescription(getString(R.string.digital_terminate_announcement));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            terminateAnnouncementTicker.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}