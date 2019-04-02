package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseDatePickerFragment;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailPresenter;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public abstract class TopAdsDetailFragment<T extends TopAdsDetailPresenter, V extends Ad> extends
        TopAdsBaseDatePickerFragment<T> implements TopAdsDetailListener<V> {

    protected static final int REQUEST_CODE_AD_EDIT = 1;

    protected SwipeToRefresh swipeToRefresh;
    protected ProgressDialog progressDialog;
    protected SnackbarRetry snackbarRetry;

    protected V ad;
    protected String adId;
    protected V adFromIntent;
    protected boolean isForceRefresh;

    protected abstract void refreshAd();

    protected abstract void updateMainView(V ad);

    @Override
    protected BaseDatePickerPresenter getDatePickerPresenter() {
        BaseDatePickerPresenterImpl baseDatePickerPresenter = new BaseDatePickerPresenterImpl(getActivity());
        return baseDatePickerPresenter;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshAd();
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        adFromIntent = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
        isForceRefresh = bundle.getBoolean(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, false);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null && requestCode == REQUEST_CODE_AD_EDIT &&
                intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false)) {
            setResultAdDetailChanged();
            if (intent.hasExtra(TopAdsExtraConstant.EXTRA_AD_ID)) {
                adId = intent.getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            if (startDate == null || endDate == null) {
                return;
            }
            refreshAd();
        }
    }

    @Override
    protected void loadData() {
        showLoading();
        if(isForceRefresh){
            refreshAd();
            isForceRefresh = false;
            return;
        }
        if (adFromIntent != null) {
            onAdLoaded(adFromIntent);
            adId = adFromIntent.getId();
            adFromIntent = null;
        } else {
            refreshAd();
        }
    }

    protected void showLoading() {
        if (!swipeToRefresh.isRefreshing()) {
            progressDialog.show();
        }
    }

    @Override
    public void onAdLoaded(V ad) {
        if(ad == null){
            onLoadAdError();
            return;
        }
        this.ad = fillFromPrevious(ad, this.adFromIntent);

        hideLoading();
        loadAdDetail(ad);
    }

    protected V fillFromPrevious(V current, V previous){
        return current;
    }

    protected void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onLoadAdError() {
        hideLoading();
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onAdEmpty() {
        hideLoading();
        CommonUtils.UniversalToast(getActivity(), getString(R.string.error_data_not_found));
        getActivity().finish();
    }

    protected void loadAdDetail(V ad) {
        updateMainView(ad);
    }

    protected void setResultAdDetailChanged() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.unSubscribe();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        outState.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        adId = savedInstanceState.getString(TopAdsExtraConstant.EXTRA_AD_ID);
        ad = savedInstanceState.getParcelable(TopAdsExtraConstant.EXTRA_AD);
        adFromIntent = null;
        onAdLoaded(ad);
    }
}
