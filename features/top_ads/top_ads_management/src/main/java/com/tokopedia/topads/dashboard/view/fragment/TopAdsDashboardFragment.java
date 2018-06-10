package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.topads.R;
import com.tokopedia.design.label.DateLabelView;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.data.model.data.Summary;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseDatePickerFragment;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardFragmentListener;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.topads.dashboard.view.widget.TopAdsStatisticLabelView;


public abstract class TopAdsDashboardFragment<T extends TopAdsDashboardPresenter> extends
        TopAdsBaseDatePickerFragment<T> implements TopAdsDashboardFragmentListener {

    private static final int REQUEST_CODE_ADD_CREDIT = 1;
    private SwipeToRefresh swipeToRefresh;
    private ImageView shopIconImageView;
    private TextView shopTitleTextView;
    private TextView depositDescTextView;
    private ImageView addDepositButton;
    private DateLabelView dateLabelView;
    private TopAdsStatisticLabelView impressionStatisticLabelView;
    private TopAdsStatisticLabelView clickStatisticLabelView;
    private TopAdsStatisticLabelView ctrStatisticLabelView;
    private TopAdsStatisticLabelView conversionStatisticLabelView;
    private TopAdsStatisticLabelView averageStatisticLabelView;
    private TopAdsStatisticLabelView costStatisticLabelView;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected BaseDatePickerPresenter getDatePickerPresenter() {
        BaseDatePickerPresenterImpl baseDatePickerPresenter = new BaseDatePickerPresenterImpl(getActivity());
        return baseDatePickerPresenter;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        shopIconImageView = (ImageView) view.findViewById(R.id.image_view_shop_icon);
        shopTitleTextView = (TextView) view.findViewById(R.id.text_view_shop_title);
        depositDescTextView = (TextView) view.findViewById(R.id.text_view_deposit_desc);
        addDepositButton = (ImageView) view.findViewById(R.id.image_button_add_deposit);
        addDepositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddCredit();
            }
        });
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateLayoutClicked();
            }
        });
        impressionStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_impression);
        clickStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_click);
        ctrStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_ctr);
        conversionStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_conversion);
        averageStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_average);
        costStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_cost);
        depositDescTextView.setText(getString(R.string.label_top_ads_deposit_desc, getString(R.string.top_ads_statistic_info_default_value)));
        impressionStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticImpressionClicked();
            }
        });
        clickStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticClickClicked();
            }
        });
        ctrStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticCtrClicked();
            }
        });
        conversionStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticConversionClicked();
            }
        });
        averageStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticAverageClicked();
            }
        });
        costStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticCostClicked();
            }
        });
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        RefreshHandler refresh = new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        });
    }

    protected void loadData() {
        swipeToRefresh.setRefreshing(true);
        dateLabelView.setDate(startDate, endDate);
        presenter.populateSummary(startDate, endDate);
        presenter.populateDeposit();
        presenter.populateShopInfo();
    }

    @Override
    public void onSummaryLoaded(@NonNull Summary summary) {
        updateSummaryLayout(summary);
        hideLoading();
        onLoadDataSuccess();
    }

    private void updateSummaryLayout(Summary summary) {
        impressionStatisticLabelView.setContent(String.valueOf(summary.getImpressionSumFmt()));
        clickStatisticLabelView.setContent(String.valueOf(summary.getClickSumFmt()));
        ctrStatisticLabelView.setContent(String.valueOf(summary.getCtrPercentageFmt()));
        conversionStatisticLabelView.setContent(String.valueOf(summary.getConversionSumFmt()));
        averageStatisticLabelView.setContent(String.valueOf(summary.getCostAvgFmt()));
        costStatisticLabelView.setContent(String.valueOf(summary.getCostSumFmt()));
    }

    @Override
    public void onLoadSummaryError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    @Override
    public void onDepositTopAdsLoaded(@NonNull DataDeposit dataDeposit) {
        depositDescTextView.setText(getString(R.string.label_top_ads_deposit_desc, dataDeposit.getAmountFmt()));
        hideLoading();
        onLoadDataSuccess();
    }

    @Override
    public void onLoadDepositTopAdsError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    @Override
    public void onShopDetailLoaded(@NonNull ShopModel shopModel) {
        ImageHandler.loadImageCircle2(getActivity(), shopIconImageView, shopModel.info.shopAvatar);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            shopTitleTextView.setText(Html.fromHtml(shopModel.info.shopName, Html.FROM_HTML_MODE_LEGACY));
        } else {
            shopTitleTextView.setText(Html.fromHtml(shopModel.info.shopName));
        }

        hideLoading();
        onLoadDataSuccess();
    }

    @Override
    public void onLoadShopDetailError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    protected void showNetworkError() {
        if (callback != null) {
            callback.onLoadDataError();
        }
    }

    protected void onLoadDataSuccess() {
        if (callback != null) {
            callback.onLoadDataSuccess();
        }
    }

    protected void showShowCase() {
        if (callback != null) {
            callback.startShowCase();
        }
    }

    protected void hideLoading() {
        swipeToRefresh.setRefreshing(false);
    }

    void onDateLayoutClicked() {
        openDatePicker();
    }

    protected void onStatisticImpressionClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_IMPR);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void onStatisticClickClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_CLICK);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void onStatisticCtrClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_CTR);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void onStatisticConversionClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_CONVERTION);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void onStatisticAverageClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_AVG);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void onStatisticCostClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_SPENT);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void goToAddCredit() {
        UnifyTracking.eventTopAdsProductAddBalance();
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_CREDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE_ADD_CREDIT) {
            if (callback != null) {
                callback.onCreditAdded();
            }
        }
    }

    public void populateDeposit() {
        presenter.populateDeposit();
    }

    protected abstract Class<?> getClassIntentStatistic();

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    public interface Callback {

        void onLoadDataError();

        void onLoadDataSuccess();

        void onCreditAdded();

        void startShowCase();
    }
}