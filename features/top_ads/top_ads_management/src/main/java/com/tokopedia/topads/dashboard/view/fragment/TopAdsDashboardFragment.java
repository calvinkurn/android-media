package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.label.DateLabelView;
import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsDashboardComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsDashboardModule;
import com.tokopedia.topads.dashboard.utils.TopAdsDatePeriodUtil;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsDashboardFragment extends BaseDaggerFragment implements TopAdsDashboardView {
    private static final int REQUEST_CODE_ADD_CREDIT = 1;

    private ImageView shopIconImageView;
    private TextView shopTitleTextView;
    private TextView depositValueTextView;
    TextView addCreditTextView;
    private LabelView groupSummaryLabelView;
    private LabelView itemSummaryLabelView;
    private LabelView keywordLabelView;

    private SessionHandler sessionHandler;

    DateLabelView dateLabelView;
    Date startDate, endDate;

    @Inject
    TopAdsDashboardPresenter topAdsDashboardPresenter;

    public static TopAdsDashboardFragment createInstance(){
        TopAdsDashboardFragment topAdsDashboardFragment = new TopAdsDashboardFragment();
        return topAdsDashboardFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .topAdsComponent(getComponent(TopAdsComponent.class))
                .build()
                .inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_ads_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topAdsDashboardPresenter.attachView(this);
        initShopInfoComponent(view);
        sessionHandler = new SessionHandler(getContext().getApplicationContext());
        initSummaryComponent(view);
    }

    private void initSummaryComponent(View view) {
        groupSummaryLabelView = view.findViewById(R.id.label_view_group_summary);
        itemSummaryLabelView = view.findViewById(R.id.label_view_item_summary);
        keywordLabelView = view.findViewById(R.id.label_view_keyword);
        groupSummaryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSummaryGroupClicked();
            }
        });
        itemSummaryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSummaryProductClicked();
            }
        });
        keywordLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSummaryKeywordClicked();
            }
        });
    }

    private void onSummaryKeywordClicked() {

    }

    private void onSummaryProductClicked() {

    }

    private void onSummaryGroupClicked() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (topAdsDashboardPresenter.isDateUpdated(startDate, endDate)){
            startDate = topAdsDashboardPresenter.getStartDate();
            endDate = topAdsDashboardPresenter.getEndDate();
            loadData();
        }
    }

    private void loadData() {
        topAdsDashboardPresenter.getShopDeposit(sessionHandler.getShopID());
        topAdsDashboardPresenter.getShopInfo(sessionHandler.getShopID());
        updateLabelDateView(startDate, endDate);
        topAdsDashboardPresenter.populateTotalAds(sessionHandler.getShopID());
    }

    private void initShopInfoComponent(View view) {
        shopIconImageView = view.findViewById(R.id.image_view_shop_icon);
        shopTitleTextView = view.findViewById(R.id.text_view_shop_title);
        depositValueTextView = view.findViewById(R.id.text_view_deposit_value);
        addCreditTextView = view.findViewById(R.id.text_view_add_deposit);
        addCreditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddCredit();
            }
        });
        dateLabelView = view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateLayoutClicked();
            }
        });
    }

    private void onDateLayoutClicked() {
        Intent intent = getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, DatePickerConstant.REQUEST_CODE_DATE);
    }

    private Intent getDatePickerIntent(Context context, Date start, Date end){
        Intent intent = new Intent(context, DatePickerActivity.class);
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.YEAR, -1);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, start.getTime());
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, end.getTime());

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, TopAdsConstant.MAX_DATE_RANGE);

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, TopAdsDatePeriodUtil.getPeriodRangeList(getActivity()));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, topAdsDashboardPresenter.getLastSelectionDatePickerIndex());
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, topAdsDashboardPresenter.getLastSelectionDatePickerType());

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, getActivity().getString(R.string.title_date_picker));
        return intent;
    }

    void goToAddCredit() {
        UnifyTracking.eventTopAdsProductAddBalance();
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_CREDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK){
            if (requestCode == REQUEST_CODE_ADD_CREDIT) {

            } else if (requestCode == DatePickerConstant.REQUEST_CODE_DATE){
                if (data != null){
                    handlingResultDateSelection(data);
                }
            }
        }

    }

    private void handlingResultDateSelection(Intent data){
        long sDate = data.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
        long eDate = data.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
        int lastSelection = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
        int selectionType = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
        if (sDate != -1 && eDate != -1) {
            startDate = new Date(sDate);
            endDate = new Date(eDate);
            topAdsDashboardPresenter.saveDate(startDate, endDate);
            topAdsDashboardPresenter.saveSelectionDatePicker(selectionType, lastSelection);

            loadData();
        }
    }

    @Override
    public void onLoadTopAdsShopDepositError(Throwable throwable) {

    }

    @Override
    public void onLoadTopAdsShopDepositSuccess(DataDeposit dataDeposit) {
        depositValueTextView.setText(dataDeposit.getAmountFmt());
    }

    @Override
    public void onErrorGetShopInfo(Throwable throwable) {

    }

    @Override
    public void onSuccessGetShopInfo(ShopInfo shopInfo) {
        ImageHandler.LoadImage(shopIconImageView, shopInfo.getInfo().getShopAvatar());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            shopTitleTextView.setText(Html.fromHtml(shopInfo.getInfo().getShopName(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            shopTitleTextView.setText(Html.fromHtml(shopInfo.getInfo().getShopName()));
        }
    }

    @Override
    public void onErrorPopulateTotalAds(Throwable throwable) {

    }

    @Override
    public void onSuccessPopulateTotalAds(TotalAd totalAd) {
        groupSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductGroupAd()));
        itemSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductAd()));
        keywordLabelView.setContent(String.valueOf(totalAd.getTotalKeyword()));
    }

    public void updateLabelDateView(Date startDate, Date endDate) {
        dateLabelView.setDate(startDate, endDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topAdsDashboardPresenter.detachView();
    }
}
