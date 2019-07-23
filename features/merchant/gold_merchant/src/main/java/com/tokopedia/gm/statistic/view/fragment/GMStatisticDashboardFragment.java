package com.tokopedia.gm.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.datepicker.range.model.DatePickerViewModel;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.common.widget.PowerMerchantSuccessBottomSheet;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.di.component.DaggerGMStatisticDashboardComponent;
import com.tokopedia.gm.statistic.di.module.GMStatisticModule;
import com.tokopedia.gm.statistic.view.holder.GMStatisticGrossViewHolder;
import com.tokopedia.gm.statistic.view.holder.GMStatisticMarketInsightViewHolder;
import com.tokopedia.gm.statistic.view.holder.GMStatisticProductViewHolder;
import com.tokopedia.gm.statistic.view.holder.GMStatisticSummaryViewHolder;
import com.tokopedia.gm.statistic.view.holder.GMStatisticTransactionViewHolder;
import com.tokopedia.gm.statistic.view.holder.GmStatisticBuyerViewHolder;
import com.tokopedia.gm.statistic.view.listener.GMStatisticDashboardView;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.gm.statistic.view.presenter.GMDashboardPresenter;
import com.tokopedia.gm.subscribe.GMSubscribeInternalRouter;
import com.tokopedia.user.session.UserSession;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.gm.common.constant.GMCommonConstantKt.IMG_URL_POWER_MERCHANT_IDLE_POPUP;
import static com.tokopedia.gm.common.constant.GMCommonConstantKt.URL_MARKET_INSIGHT;

/**
 * A placeholder fragment containing a simple view.
 * created by norman 02/01/2017
 */
public class GMStatisticDashboardFragment extends GMStatisticBaseDatePickerFragment
        implements GMStatisticDashboardView,
        GMStatisticTransactionViewHolder.Listener,
        GMStatisticMarketInsightViewHolder.Listener,
        PowerMerchantSuccessBottomSheet.BottomSheetListener
{

    @Inject
    GMDashboardPresenter gmDashboardPresenter;

    private NestedScrollView nestedScrollView;

    private GMStatisticSummaryViewHolder gmStatisticSummaryViewHolder;
    private GMStatisticGrossViewHolder gmStatisticGrossViewHolder;
    private GMStatisticProductViewHolder gmStatisticProductViewHolder;
    private GMStatisticTransactionViewHolder gmStatisticTransactionViewHolder;
    private GmStatisticBuyerViewHolder gmStatisticBuyerViewHolder;
    private GMStatisticMarketInsightViewHolder GMStatisticMarketInsightViewHolder;
    private UserSession userSession;
    public final String FEATURE_MARKET_INSIGHT = "Wawasan Pasar";



    private SnackbarRetry snackbarRetry;

    public GMStatisticDashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datePickerPresenter.clearDatePickerSetting();
    }

    @Override
    protected void initInjector() {
        DaggerGMStatisticDashboardComponent
                .builder()
                .gMComponent(getComponent(GMComponent.class))
                .gMStatisticModule(new GMStatisticModule())
                .build().inject(this);
        gmDashboardPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_statistic_dashboard, container, false);
        gmStatisticSummaryViewHolder = new GMStatisticSummaryViewHolder(view);
        gmStatisticGrossViewHolder = new GMStatisticGrossViewHolder(view);
        gmStatisticProductViewHolder = new GMStatisticProductViewHolder(view);
        gmStatisticTransactionViewHolder = new GMStatisticTransactionViewHolder(view);
        GMStatisticMarketInsightViewHolder = new GMStatisticMarketInsightViewHolder(view);
        gmStatisticBuyerViewHolder = new GmStatisticBuyerViewHolder(view);

        // analytic below : https://phab.tokopedia.com/T18496
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.content_gmstat);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() + nestedScrollView.getPaddingBottom()
                        - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    UnifyTracking.eventScrollGMStat(getActivity());
                }
            }
        });
        gmStatisticTransactionViewHolder.setListener(this);
        GMStatisticMarketInsightViewHolder.setListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSession = new UserSession(getContext());
        disableDateLabelView();
    }

    @Override
    public void loadDataByDate(DatePickerViewModel datePickerViewModel) {
        loadDataByDate();
    }

    private void loadDataByDate(){
        resetToLoading();
        gmDashboardPresenter.fetchData(getStartDate(), getEndDate());
    }

    @Override
    public boolean isAllowToCompareDate() {
        return false;
    }

    private void resetToLoading() {
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticGrossViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticProductViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticTransactionViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticBuyerViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        GMStatisticMarketInsightViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
    }

    @Override
    public void onSuccessLoadTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph, boolean isGoldMerchant) {
        gmStatisticGrossViewHolder.setData(getTransactionGraph);
        gmStatisticTransactionViewHolder.bindData(getTransactionGraph.gmTransactionGraphViewModel.totalTransactionModel,
                isGoldMerchant);
    }

    @Override
    public void onErrorLoadTransactionGraph(Throwable t) {
        gmStatisticGrossViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        gmStatisticTransactionViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadProductGraph(GetProductGraph getProductGraph) {
        gmStatisticSummaryViewHolder.setData(getProductGraph);
        UnifyTracking.eventLoadGMStat(getActivity());
    }

    @Override
    public void onErrorLoadProductGraph(Throwable t) {
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadPopularProduct(GetPopularProduct getPopularProduct) {
        gmStatisticProductViewHolder.bindData(getPopularProduct);
    }

    @Override
    public void onErrorLoadPopularProduct(Throwable t) {
        gmStatisticProductViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadBuyerGraph(GetBuyerGraph getBuyerGraph) {
        gmStatisticBuyerViewHolder.bindData(getBuyerGraph);
    }

    @Override
    public void onErrorLoadBuyerGraph(Throwable t) {
        gmStatisticBuyerViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onGetShopCategoryEmpty(boolean goldMerchant) {
        GMStatisticMarketInsightViewHolder.bindNoShopCategory(goldMerchant, isIdlePowerMerchant());
    }

    @Override
    public void onSuccessGetKeyword(List<GetKeyword> getKeywords, boolean isGoldMerchant) {
        GMStatisticMarketInsightViewHolder.bindData(getKeywords, isGoldMerchant);
    }

    @Override
    public void onSuccessGetCategory(String categoryName) {
        GMStatisticMarketInsightViewHolder.bindCategory(categoryName);
    }

    @Override
    public void onErrorLoadMarketInsight(Throwable t) {
        GMStatisticMarketInsightViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onErrorLoadShopInfo(Throwable t) {
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadShopInfo(boolean isGoldMerchant) {
        if (isGoldMerchant) {
            enableDateLabelView();
        } else {
            disableDateLabelView();
        }
    }

    @Override
    public void onButtonRedirectToClicked() {
        if(isIdlePowerMerchant())
            showIdlePowerMerchantBottomSheet(FEATURE_MARKET_INSIGHT);
        else if(!isPowerMerchant())
            startActivity(GMSubscribeInternalRouter.getGMSubscribeHomeIntent(getActivity()));

    }

    @Override
    public void onReadMoreClicked() {
        RouteManager.route(
                getContext(),
                ApplinkConstInternalGlobal.WEBVIEW, URL_MARKET_INSIGHT
        );
    }

    private boolean isPowerMerchant() {
        return userSession.isGoldMerchant();
    }

    private boolean isIdlePowerMerchant() {
        return userSession.isPowerMerchantIdle();
    }

    private void showIdlePowerMerchantBottomSheet(String featureName) {
        String title = String.format(
                getString(R.string.bottom_sheet_idle_title),
                featureName
        );
        String description = String.format(
                getString(R.string.bottom_sheet_idle_desc),
                featureName
        );
        String buttonName = getString(R.string.bottom_sheet_idle_btn);
        showBottomSheet(title, IMG_URL_POWER_MERCHANT_IDLE_POPUP, description, buttonName);
    }

    private void showBottomSheet(String title, String imageUrl, String description, String buttonName) {
        PowerMerchantSuccessBottomSheet.BottomSheetModel model = new PowerMerchantSuccessBottomSheet.BottomSheetModel(
                title,
                description,
                imageUrl,
                buttonName,
                ""
        );
        PowerMerchantSuccessBottomSheet bottomSheet = PowerMerchantSuccessBottomSheet.newInstance(model);
        bottomSheet.setListener(this);
        bottomSheet.show(getChildFragmentManager(), "merchant_warning_bottom_sheet");
    }

    private void showSnackbarRetry() {
        if (snackbarRetry == null) {
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    loadDataByDate();
                }
            });
            snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        }
        //!important, the delay will help the snackbar re-show after it is being hidden.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    snackbarRetry.showRetrySnackbar();
                }
            }
        },700);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gmDashboardPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onViewNotGmClicked() {

    }

    @Override
    public void onButtonClicked() {

    }
}