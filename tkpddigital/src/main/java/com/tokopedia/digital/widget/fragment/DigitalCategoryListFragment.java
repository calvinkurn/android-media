package com.tokopedia.digital.widget.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.digital.tokocash.listener.TokoCashReceivedListener;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;
import com.tokopedia.digital.tokocash.receiver.TokoCashBroadcastReceiver;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;
import com.tokopedia.digital.widget.adapter.DigitalCategoryListAdapter;
import com.tokopedia.digital.widget.data.mapper.CategoryDigitalListDataMapper;
import com.tokopedia.digital.widget.data.mapper.ICategoryDigitalListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalCategoryListRepository;
import com.tokopedia.digital.widget.interactor.DigitalCategoryListInteractor;
import com.tokopedia.digital.widget.listener.IDigitalCategoryListView;
import com.tokopedia.digital.widget.model.DigitalCategoryItemData;
import com.tokopedia.digital.widget.model.DigitalCategoryItemDataError;
import com.tokopedia.digital.widget.presenter.DigitalCategoryListPresenter;
import com.tokopedia.digital.widget.presenter.IDigitalCategoryListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListFragment extends BasePresenterFragment<IDigitalCategoryListPresenter>
        implements IDigitalCategoryListView, DigitalCategoryListAdapter.ActionListener,
        RefreshHandler.OnRefreshHandlerListener, TokoCashReceivedListener {
    public static final int NUMBER_OF_COLUMN_GRID_CATEGORY_LIST = 4;
    private static final String EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA =
            "EXTRA_STATE_DIGITAL_CATEGORY_LIST_DATA";

    @BindView(R2.id.rv_digital_category)
    RecyclerView rvDigitalCategoryList;

    private CompositeSubscription compositeSubscription;
    private DigitalCategoryListAdapter adapter;
    private RefreshHandler refreshHandler;
    private RecyclerView.LayoutManager gridLayoutManager;
    private RecyclerView.LayoutManager linearLayoutManager;
    private TokoCashBroadcastReceiver tokoCashBroadcastReceiver;
    private TokoCashData tokoCashData;
    private List<DigitalCategoryItemData> digitalCategoryListDataState;

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
    protected void initialPresenter() {
        MojitoService mojitoService = new MojitoService();
        ICategoryDigitalListDataMapper mapperData = new CategoryDigitalListDataMapper();
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        TokoCashService tokoCashService = new TokoCashService(sessionHandler.getAccessToken(
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
        tokoCashBroadcastReceiver = new TokoCashBroadcastReceiver(this);
        getActivity().registerReceiver(tokoCashBroadcastReceiver, new IntentFilter(
                TokoCashBroadcastReceiver.ACTION_GET_TOKOCASH_DIGITAL
        ));
    }

    @Override
    protected void setViewListener() {
        rvDigitalCategoryList.setAdapter(adapter);
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
    public void sendBroadcastReceiver(Intent intent) {
        getActivity().sendBroadcast(intent);
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
        getActivity().unregisterReceiver(tokoCashBroadcastReceiver);
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }

    @Override
    public void onDigitalCategoryItemClicked(DigitalCategoryItemData itemData) {
        if (itemData.getCategoryId().equalsIgnoreCase("103") && tokoCashData != null
                && tokoCashData.getLink() != 1) {
            String urlActivation = getTokoCashActionRedirectUrl(tokoCashData);
            String seamlessUrl = URLGenerator.generateURLSessionLogin((Uri.encode(urlActivation)),
                    getActivity());
            if (getActivity() != null) {
                if (getActivity().getApplication() instanceof TkpdCoreRouter) {
                    ((TkpdCoreRouter) getActivity().getApplication())
                            .goToWallet(getActivity(), seamlessUrl);
                }
            }
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
                    /*Intent intent = (((IDigitalModuleRouter) getActivity().getApplication())
                            .getIntentDeepLinkHandlerActivity());
                    intent.putExtras(bundle);
                    intent.setData(Uri.parse(itemData.getAppLinks()));
                    startActivity(intent);*/
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

    @Override
    public void onReceivedTokoCashData(TokoCashData tokoCashData) {
        this.tokoCashData = tokoCashData;
    }

    @Override
    public void onTokoCashDataError(String errorMessage) {

    }

    private String getTokoCashActionRedirectUrl(TokoCashData tokoCashData) {
        if (tokoCashData.getAction() == null) return "";
        else return tokoCashData.getAction().getRedirectUrl();
    }

    private void renderErrorStateData(String message) {
        refreshHandler.finishRefresh();
        rvDigitalCategoryList.setLayoutManager(linearLayoutManager);
        adapter.addErrorData(new DigitalCategoryItemDataError.Builder().message(message).build());
    }

}
