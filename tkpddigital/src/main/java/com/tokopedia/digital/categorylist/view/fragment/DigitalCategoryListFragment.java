package com.tokopedia.digital.categorylist.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.apiservices.tokocash.TokoCashService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TokoCashTypeDef;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
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
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListFragment extends BasePresenterFragment<IDigitalCategoryListPresenter>
        implements IDigitalCategoryListView, DigitalCategoryListAdapter.ActionListener,
        RefreshHandler.OnRefreshHandlerListener, DigitalItemHeaderHolder.ActionListener {

    public static final int NUMBER_OF_COLUMN_GRID_CATEGORY_LIST = 4;
    private static final String EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA =
            "EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA";

    @BindView(R2.id.rv_digital_category)
    RecyclerView rvDigitalCategoryList;
    @BindView(R2.id.header_container)
    LinearLayout headerContainer;

    @BindView(R2.id.header_my_transaction)
    DigitalItemHeaderHolder headerMyTransaction;
    @BindView(R2.id.header_subscription)
    DigitalItemHeaderHolder headerSubscription;
    @BindView(R2.id.header_fav_number)
    DigitalItemHeaderHolder headerFavNumber;

    private CompositeSubscription compositeSubscription;
    private DigitalCategoryListAdapter adapter;
    private RefreshHandler refreshHandler;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TokoCashBalanceData tokoCashBalanceData;
    private List<DigitalCategoryItemData> digitalCategoryListDataState;
    private boolean fromAppShortcut = false;

    public static DigitalCategoryListFragment newInstance() {
        return new DigitalCategoryListFragment();
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
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        digitalCategoryListDataState = savedState.getParcelableArrayList(
                EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA
        );
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isFromAppShortcut()){
            UnifyTracking.eventBayarLongClick();
        }
    }

    @Override
    protected void initialPresenter() {
        MojitoService mojitoService = new MojitoService();
        ICategoryDigitalListDataMapper mapperData = new CategoryDigitalListDataMapper();
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        TokoCashService tokoCashService = new TokoCashService(SessionHandler.getAccessToken(
                MainApplication.getAppContext()
        ));
        presenter = new DigitalCategoryListPresenter(
                new DigitalCategoryListInteractor(
                        compositeSubscription,
                        new DigitalCategoryListRepository(
                                mojitoService, new GlobalCacheManager(), mapperData,
                                sessionHandler), tokoCashService), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_digital_category_list_digital_module;
    }

    @Override
    protected void initView(View view) {
        refreshHandler = new RefreshHandler(getActivity(), view, this);
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
                        .resIconId(R.drawable.ic_header_digital_category_my_transaction)
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
                        .resIconId(R.drawable.ic_header_digital_category_favorit_number)
                        .typeMenu(DigitalCategoryItemHeader.TypeMenu.FAVORITE_NUMBER)
                        .build()
        );

        headerSubscription.setData(
                new DigitalCategoryItemHeader.Builder()
                        .title(getString(
                                R.string.title_header_menu_digital_categories_subscription_digital_module
                        ))
                        .siteUrl(TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                + TkpdBaseURL.DigitalWebsite.PATH_SUBSCRIPTIONS)
                        .resIconId(R.drawable.ic_header_digital_category_subscription)
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
    public void renderTokoCashData(TokoCashBalanceData tokoCashData) {
        this.tokoCashBalanceData = tokoCashData;
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
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

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
        UnifyTracking.eventClickProductOnDigitalHomepage(itemData.getName());
        if (itemData.getCategoryId().equalsIgnoreCase(
                String.valueOf(DigitalCategoryItemData.DEFAULT_TOKOCASH_CATEGORY_ID
                )) && tokoCashBalanceData != null && tokoCashBalanceData.getLink() != TokoCashTypeDef.TOKOCASH_ACTIVE) {
            WalletRouterUtil.navigateWallet(
                    getActivity().getApplication(),
                    this,
                    IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                    tokoCashBalanceData.getAction().getApplinks(),
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
                    ((IDigitalModuleRouter) getActivity().getApplication()).actionNavigateByApplinksUrl(getActivity(), itemData.getAppLinks(), bundle);
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
        if (refreshHandler.isRefreshing()) presenter.processGetDigitalCategoryList();
    }

    private void renderErrorStateData(String message) {
        refreshHandler.finishRefresh();
        rvDigitalCategoryList.setLayoutManager(linearLayoutManager);
        adapter.addErrorData(new DigitalCategoryItemDataError.Builder().message(message).build());
    }

    @Override
    public void onClickCategoryHeaderMenu(DigitalCategoryItemHeader data) {
        switch (data.getTypeMenu()) {
            default:
                startActivity(DigitalWebActivity.newInstance(getActivity(), data.getSiteUrl()));
                break;
        }
    }

    public boolean isFromAppShortcut() {
        return fromAppShortcut;
    }

    public void setFromAppShortcut(boolean fromAppShortcut) {
        this.fromAppShortcut = fromAppShortcut;
    }
}