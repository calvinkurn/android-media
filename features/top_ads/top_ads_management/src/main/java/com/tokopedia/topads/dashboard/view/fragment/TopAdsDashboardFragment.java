package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.design.component.FloatingButton;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.utils.DateLabelUtils;
import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsStatisticsType;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsDashboardComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.utils.TopAdsDatePeriodUtil;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddingPromoOptionActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordListActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsDashboardFragment extends BaseDaggerFragment implements TopAdsDashboardView {
    private static final int REQUEST_CODE_ADD_CREDIT = 1;
    public static final int REQUEST_CODE_AD_STATUS = 2;

    private ImageView shopIconImageView;
    private TextView shopTitleTextView;
    private TextView depositValueTextView;
    TextView addCreditTextView;
    private LabelView statisticsOptionLabelView;
    private LabelView groupSummaryLabelView;
    private LabelView itemSummaryLabelView;
    private LabelView keywordLabelView;
    private LabelView storeLabelView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TopAdsStatisticPagerAdapter pagerAdapter;
    private View contentStatisticsView;

    private LabelView dateLabelView;
    Date startDate, endDate;
    DataStatistic dataStatistic;
    @TopAdsStatisticsType int selectedStatisticType;

    private int totalProductAd;
    private int totalGroupAd;

    private Callback callback;

    @Inject
    TopAdsDashboardPresenter topAdsDashboardPresenter;

    public static TopAdsDashboardFragment createInstance(){
        return new TopAdsDashboardFragment();
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
        selectedStatisticType = TopAdsStatisticsType.ALL_ADS;
        totalProductAd = Integer.MIN_VALUE;

        initShopInfoComponent(view);
        initSummaryComponent(view);
        initStatisticComponent(view);
        initEmptyStateView(view);
        FloatingButton button = (FloatingButton) view.findViewById(R.id.button_topads_add_promo);
        button.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TopAdsAddingPromoOptionActivity.class));
            }
        });
    }

    private void initEmptyStateView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
        textView.setText(R.string.topads_dashboard_empty_usage_title);
        textView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
        textView.setText(R.string.topads_dashboard_empty_usage_desc);
        view.findViewById(R.id.button_add_promo).setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback){
            callback = (Callback) context;
        }
    }

    private void initStatisticComponent(View view) {
        contentStatisticsView = view.findViewById(R.id.topads_content_statistics);
        statisticsOptionLabelView = (LabelView) view.findViewById(R.id.label_view_statistics);
        statisticsOptionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetStatisticTypeOptions();
            }
        });
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        initTabLayouTitles();
        initTopAdsStatisticPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                trackingStatisticBar(position);
                getCurrentStatisticsFragment().updateDataStatistic(dataStatistic);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public View getStatisticsOptionLabelView() {
        return statisticsOptionLabelView;
    }

    public View getDateLabelView() {
        return dateLabelView;
    }

    public View getContentStatisticsView() {
        return contentStatisticsView;
    }

    public View getGroupSummaryLabelView() {
        return groupSummaryLabelView;
    }

    public View getItemSummaryLabelView() {
        return itemSummaryLabelView;
    }

    public View getKeywordLabelView() {
        return keywordLabelView;
    }

    public View getStoreLabelView() {
        return storeLabelView;
    }

    public ViewGroup getScrollView(){
        if (getView() != null) {
            return getView().findViewById(R.id.scroll_view);
        } else {
            return null;
        }
    }

    public View getButtonAddPromo(){
        if (getView() != null) {
            return getView().findViewById(R.id.button_add_promo);
        } else {
            return null;
        }
    }

    private void trackingStatisticBar(int position) {
        switch (position) {
            case 0:
                onImpressionSelected();
                break;
            case 1:
                onClickSelected();
                break;
            case 2:
                onCtrSelected();
                break;
            case 3:
                onConversionSelected();
                break;
            case 4:
                onAverageConversionSelected();
                break;
            case 5:
                onCostSelected();
                break;
            default:
                break;
        }
    }

    protected TopAdsDashboardStatisticFragment getCurrentStatisticsFragment() {
        if (pagerAdapter == null) {
            return null;
        }
        return (TopAdsDashboardStatisticFragment) pagerAdapter.instantiateItem(viewPager, tabLayout.getSelectedTabPosition());
    }

    private void initTabLayouTitles() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_top_ads_impression));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_top_ads_click));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_top_ads_ctr));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_top_ads_conversion));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_top_ads_average));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_top_ads_cost));
    }

    private void initTopAdsStatisticPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TopAdsStatisticImprFragment.createInstance());
        fragmentList.add(TopAdsStatisticKlikFragment.createInstance());
        fragmentList.add(TopAdsStatisticCtrFragment.createInstance());
        fragmentList.add(TopAdsStatisticConversionFragment.createInstance());
        fragmentList.add(TopAdsStatisticAvgFragment.createInstance());
        fragmentList.add(TopAdsStatisticSpentFragment.createInstance());
        pagerAdapter = new TopAdsStatisticPagerAdapter(getChildFragmentManager(), fragmentList);
    }

    private void initSummaryComponent(View view) {
        groupSummaryLabelView = view.findViewById(R.id.label_view_group_summary);
        itemSummaryLabelView = view.findViewById(R.id.label_view_item_summary);
        keywordLabelView = view.findViewById(R.id.label_view_keyword);
        storeLabelView = (LabelView) view.findViewById(R.id.label_view_shop);
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
        storeLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void onSummaryKeywordClicked() {
        UnifyTracking.eventTopAdsProductClickKeywordDashboard();
        Intent intent = new Intent(getActivity(), TopAdsKeywordListActivity.class);
        if (totalGroupAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_GROUP_ADS, totalGroupAd);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    private void onSummaryProductClicked() {
        UnifyTracking.eventTopAdsProductClickProductDashboard();
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    private void onSummaryGroupClicked() {
        UnifyTracking.eventTopAdsProductClickGroupDashboard();
        Intent intent = new Intent(getActivity(), TopAdsGroupAdListActivity.class);
        if (totalProductAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_PRODUCT_ADS, totalProductAd);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
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
        topAdsDashboardPresenter.getShopDeposit();
        topAdsDashboardPresenter.getShopInfo();
    }

    protected void loadStatisticsData(){
        statisticsOptionLabelView.setContent(getStatisticsTypeTitle(selectedStatisticType));
        updateLabelDateView(startDate, endDate);
        topAdsDashboardPresenter.getTopAdsStatistic(startDate, endDate, selectedStatisticType);
    }

    private CharSequence getStatisticsTypeTitle(int selectedStatisticType) {
        int resString = -1;
        switch (selectedStatisticType){
            case TopAdsStatisticsType.ALL_ADS : resString = R.string.topads_dashboard_all_promo_menu;
                                                break;
            case TopAdsStatisticsType.PRODUCT_ADS : resString = R.string.top_ads_title_product;
                                                break;
            case TopAdsStatisticsType.SHOP_ADS : resString = R.string.title_top_ads_store;
                                                break;
        }
        return getString(resString);
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
        dateLabelView = (LabelView) view.findViewById(R.id.date_label_view);
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
        if (requestCode == REQUEST_CODE_ADD_CREDIT) {

        } else if (requestCode == DatePickerConstant.REQUEST_CODE_DATE) {
            if (data != null) {
                handlingResultDateSelection(data);
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

            loadStatisticsData();
        }
    }

    @Override
    public void onLoadTopAdsShopDepositError(Throwable throwable) {

    }

    @Override
    public void onLoadTopAdsShopDepositSuccess(DataDeposit dataDeposit) {
        depositValueTextView.setText(dataDeposit.getAmountFmt());
        if (dataDeposit.isAdUsage()){
            topAdsDashboardPresenter.populateTotalAds();
            loadStatisticsData();
            getView().findViewById(R.id.topads_dashboard_empty).setVisibility(View.GONE);
            getView().findViewById(R.id.topads_dashboard_content).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.topads_dashboard_empty).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.topads_dashboard_content).setVisibility(View.GONE);
        }
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
        totalProductAd = totalAd.getTotalProductAd();
        totalGroupAd = totalAd.getTotalProductGroupAd();
        groupSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductGroupAd()));
        itemSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductAd()));
        keywordLabelView.setContent(String.valueOf(totalAd.getTotalKeyword()));
    }

    @Override
    public void onErrorGetStatisticsInfo(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onSuccesGetStatisticsInfo(DataStatistic dataStatistic) {
        this.dataStatistic = dataStatistic;
        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment != null && fragment instanceof TopAdsDashboardStatisticFragment) {
            ((TopAdsDashboardStatisticFragment) fragment).updateDataStatistic(this.dataStatistic);
        }
    }

    public void updateLabelDateView(Date startDate, Date endDate) {
        dateLabelView.setContent(DateLabelUtils.getRangeDateFormatted(getActivity(), startDate.getTime(), endDate.getTime()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topAdsDashboardPresenter.detachView();
    }

    protected void showBottomSheetStatisticTypeOptions(){
        CheckedBottomSheetBuilder checkedBottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity());
        checkedBottomSheetBuilder = (CheckedBottomSheetBuilder) checkedBottomSheetBuilder.setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(R.string.drawer_title_statistic);

        checkedBottomSheetBuilder.addItem(TopAdsStatisticsType.ALL_ADS, R.string.topads_dashboard_all_promo_menu,
                null, (selectedStatisticType == TopAdsStatisticsType.ALL_ADS));
        checkedBottomSheetBuilder.addItem(TopAdsStatisticsType.PRODUCT_ADS, R.string.top_ads_title_product,
                null, (selectedStatisticType == TopAdsStatisticsType.PRODUCT_ADS));
        checkedBottomSheetBuilder.addItem(TopAdsStatisticsType.SHOP_ADS, R.string.title_top_ads_store,
                null, (selectedStatisticType == TopAdsStatisticsType.SHOP_ADS));

        BottomSheetDialog bottomSheetDialog = checkedBottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        selectedStatisticType = item.getItemId();
                        loadStatisticsData();
                    }
                }).createDialog();
        bottomSheetDialog.show();


    }

    public void startShowCase(){
        if (callback != null){
            callback.startShowCase();
        }
    }

    private void onCostSelected(){
        switch (selectedStatisticType){
            case TopAdsStatisticsType.PRODUCT_ADS:
                UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CPC);
                break;
            case TopAdsStatisticsType.SHOP_ADS:
                UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CPC);
                break;
            default:
                break;
        }
    }

    private void onAverageConversionSelected(){
        switch (selectedStatisticType){
            case TopAdsStatisticsType.PRODUCT_ADS:
                UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_AVERAGE_CONVERSION);
                break;
            case TopAdsStatisticsType.SHOP_ADS:
                UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_AVERAGE_CONVERSION);
                break;
            default:
                break;
        }
    }

    private void onConversionSelected(){
        switch (selectedStatisticType){
            case TopAdsStatisticsType.PRODUCT_ADS:
                UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CONVERSION);
                break;
            case TopAdsStatisticsType.SHOP_ADS:
                UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CONVERSION);
                break;
            default:
                break;
        }
    }

    private void onCtrSelected(){
        switch (selectedStatisticType){
            case TopAdsStatisticsType.PRODUCT_ADS:
                UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CTR);
                break;
            case TopAdsStatisticsType.SHOP_ADS:
                UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CTR);
                break;
            default:
                break;
        }
    }

    private void onClickSelected(){
        switch (selectedStatisticType){
            case TopAdsStatisticsType.PRODUCT_ADS:
                UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CLICK);
                break;
            case TopAdsStatisticsType.SHOP_ADS:
                UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CLICK);
                break;
            default:
                break;
        }
    }

    private void onImpressionSelected(){
        switch (selectedStatisticType){
            case TopAdsStatisticsType.PRODUCT_ADS:
                UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_IMPRESSION);
                break;
            case TopAdsStatisticsType.SHOP_ADS:
                UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_IMPRESSION);
                break;
            default:
                break;
        }
    }

    public interface Callback{
        void startShowCase();
    }
}
