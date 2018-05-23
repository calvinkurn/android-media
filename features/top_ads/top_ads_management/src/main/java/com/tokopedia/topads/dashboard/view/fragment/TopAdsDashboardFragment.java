package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.component.FloatingButton;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.utils.DateLabelUtils;
import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.constant.TopAdsAddingOption;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsStatisticsType;
import com.tokopedia.topads.dashboard.data.model.data.DashboardPopulateResponse;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsDashboardComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.utils.TopAdsDatePeriodUtil;
import com.tokopedia.topads.dashboard.view.activity.SellerCenterActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddingPromoOptionActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailShopActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordListActivity;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

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
    public static final int REQUEST_CODE_AD_OPTION = 3;

    private View shopLayoutView;
    private ImageView shopIconImageView;
    private TextView shopTitleTextView;
    private TextView depositValueTextView;
    TextView addCreditTextView;
    private LabelView statisticsOptionLabelView;
    private LabelView groupSummaryLabelView;
    private LabelView itemSummaryLabelView;
    private LabelView keywordLabelView;
    LabelView storeLabelView;
    private ViewPager viewPager;
    private TopAdsStatisticPagerAdapter pagerAdapter;
    private View contentStatisticsView;
    private RecyclerView recyclerTabLayout;
    private TopAdsTabAdapter topAdsTabAdapter;
    private View viewGroupPromo;
    private SwipeToRefresh swipeToRefresh;
    private SnackbarRetry snackbarRetry;

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
        topAdsDashboardPresenter.resetDate();
        selectedStatisticType = TopAdsStatisticsType.ALL_ADS;
        totalProductAd = Integer.MIN_VALUE;
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        RefreshHandler refresh = new RefreshHandler(getActivity(), swipeToRefresh, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                topAdsDashboardPresenter.clearStatisticsCache();
                topAdsDashboardPresenter.clearTotalAdCache();
                loadData();
            }
        });
        initShopInfoComponent(view);
        initSummaryComponent(view);
        initStatisticComponent(view);
        initEmptyStateView(view);
        FloatingButton button = (FloatingButton) view.findViewById(R.id.button_topads_add_promo);
        button.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), TopAdsAddingPromoOptionActivity.class), REQUEST_CODE_AD_OPTION);
            }
        });
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadData();
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        setHasOptionsMenu(true);
    }

    private void initEmptyStateView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.no_result_image);
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_top_ads_dashboard_empty));
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
        recyclerTabLayout = view.findViewById(R.id.recyclerview_tabLayout);
        final LinearLayoutManager tabLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerTabLayout.setLayoutManager(tabLayoutManager);
        topAdsTabAdapter = new TopAdsTabAdapter(getActivity());
        topAdsTabAdapter.setListener(new TopAdsTabAdapter.OnRecyclerTabItemClick() {
            @Override
            public void onTabItemClick(int position) {
                viewPager.setCurrentItem(position);
            }
        });
        recyclerTabLayout.setAdapter(topAdsTabAdapter);
        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()){
            @Override
            protected int getHorizontalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        viewPager = view.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        initTabLayouTitles();
        initTopAdsStatisticPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                smoothScroller.setTargetPosition(position);
                tabLayoutManager.startSmoothScroll(smoothScroller);
                topAdsTabAdapter.selected(position);
                trackingStatisticBar(position);
                getCurrentStatisticsFragment().updateDataStatistic(dataStatistic);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getCenterOffset(RecyclerView recyclerView, int position) {
        if (recyclerView == null || recyclerView.getChildAt(position) == null){
            return 0;
        } else {
            return (recyclerView.getWidth() - recyclerView.getChildAt(position).getWidth()) / 2;
        }
    }

    public View getShopInfoLayout() {
        return shopLayoutView;
    }

    public View getContentStatisticsView() {
        return contentStatisticsView;
    }

    public View getGroupSummaryLabelView() {
        return groupSummaryLabelView;
    }

    public View getViewGroupPromo(){
        return viewGroupPromo;
    }

    public ScrollView getScrollView(){
        if (getView() != null) {
            return (ScrollView) getView().findViewById(R.id.scroll_view);
        } else {
            return null;
        }
    }

    public boolean isContentVisible(){
        return getView().findViewById(R.id.topads_dashboard_content).getVisibility() == View.VISIBLE;
    }

    public View getButtonAddPromo(){
        if (getView() != null) {
            return getView().findViewById(R.id.button_topads_add_promo);
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
        return (TopAdsDashboardStatisticFragment) pagerAdapter.instantiateItem(viewPager, topAdsTabAdapter.getSelectedTabPosition());
    }

    private TopAdsStatisticConversionFragment getConversionFragment(){
        if (pagerAdapter == null) {
            return null;
        }
        return (TopAdsStatisticConversionFragment) pagerAdapter.getItem(5);
    }

    private void initTabLayouTitles() {
        topAdsTabAdapter.setSummary(null, getResources().getStringArray(R.array.top_ads_tab_statistics_labels));
    }

    private void initTopAdsStatisticPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TopAdsStatisticImprFragment.createInstance());
        fragmentList.add(TopAdsStatisticKlikFragment.createInstance());
        fragmentList.add(TopAdsStatisticSpentFragment.createInstance());
        fragmentList.add(TopAdsStatisticIncomeFragment.createInstance());
        fragmentList.add(TopAdsStatisticCtrFragment.createInstance());
        fragmentList.add(TopAdsStatisticConversionFragment.createInstance());
        fragmentList.add(TopAdsStatisticAvgFragment.createInstance());
        fragmentList.add(TopAdsStatisticSoldFragment.createInstance());
        pagerAdapter = new TopAdsStatisticPagerAdapter(getChildFragmentManager(), fragmentList);
    }

    private void initSummaryComponent(View view) {
        viewGroupPromo = view.findViewById(R.id.view_group_promo);
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
                onStoreClicked();
            }
        });
    }

    private void onStoreClicked() {
        topAdsDashboardPresenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_SHOP);
        Intent intent = new Intent(getActivity(), TopAdsDetailShopActivity.class);
        intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, true);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
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
        swipeToRefresh.setRefreshing(true);
        topAdsDashboardPresenter.getPopulateDashboardData();
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
        shopLayoutView = view.findViewById(R.id.view_group_deposit);
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
        if (requestCode == REQUEST_CODE_AD_STATUS && data != null) {
            if (startDate == null || endDate == null) {
                return;
            }
            boolean adStatusChanged = data.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            if (adStatusChanged) {
                topAdsDashboardPresenter.clearTotalAdCache();
                loadData();
            }
        } else if (requestCode == REQUEST_CODE_ADD_CREDIT) {
            loadData();
        } else if (requestCode == DatePickerConstant.REQUEST_CODE_DATE) {
            if (data != null) {
                handlingResultDateSelection(data);
            }
        } else if (requestCode == REQUEST_CODE_AD_OPTION){
            if (data != null){
                int option = data.getIntExtra(TopAdsAddingPromoOptionFragment.EXTRA_SELECTED_OPTION, -1);
                switch (option){
                    case TopAdsAddingOption.SHOP_OPT: onStoreClicked(); break;
                    case TopAdsAddingOption.GROUP_OPT: onSummaryGroupClicked(); break;
                    case TopAdsAddingOption.PRODUCT_OPT: gotoCreateProductAd(); break;
                    case TopAdsAddingOption.KEYWORDS_OPT: gotoCreateKeyword(); break;
                    default: break;
                }
            }
        }
    }

    private void gotoCreateProductAd() {
        topAdsDashboardPresenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_DASHBOARD_PRODUCT);
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    private void gotoCreateKeyword() {
        topAdsDashboardPresenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_KEYWORD_POSITIVE);
        TopAdsKeywordNewChooseGroupActivity.start(this, getActivity(), REQUEST_CODE_AD_STATUS, true);
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
            trackingDateTopAds(lastSelection, selectionType);
            loadStatisticsData();
        }
    }

    private void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsShopChooseDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsShopDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsShopDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsShopDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsShopDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsShopDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onLoadTopAdsShopDepositError(Throwable throwable) {
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onLoadTopAdsShopDepositSuccess(DataDeposit dataDeposit) {
        snackbarRetry.hideRetrySnackbar();
        depositValueTextView.setText(dataDeposit.getAmountFmt());
    }

    @Override
    public void onErrorGetShopInfo(Throwable throwable) {
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.showRetrySnackbar();
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
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onSuccessPopulateTotalAds(TotalAd totalAd) {
        snackbarRetry.hideRetrySnackbar();
        swipeToRefresh.setRefreshing(false);
        totalProductAd = totalAd.getTotalProductAd();
        totalGroupAd = totalAd.getTotalProductGroupAd();
        groupSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductGroupAd()));
        itemSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductAd()));
        keywordLabelView.setContent(String.valueOf(totalAd.getTotalKeyword()));
    }

    @Override
    public void onErrorGetStatisticsInfo(Throwable throwable) {
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onSuccesGetStatisticsInfo(DataStatistic dataStatistic) {
        snackbarRetry.hideRetrySnackbar();
        this.dataStatistic = dataStatistic;
        if (dataStatistic != null) {
            topAdsTabAdapter.setSummary(dataStatistic.getSummary(), getResources().getStringArray(R.array.top_ads_tab_statistics_labels));
        }
        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment != null && fragment instanceof TopAdsDashboardStatisticFragment) {
            ((TopAdsDashboardStatisticFragment) fragment).updateDataStatistic(this.dataStatistic);
        }
    }

    @Override
    public void onErrorPopulateData(Throwable throwable) {
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onSuccessPopulateData(DashboardPopulateResponse dashboardPopulateResponse) {
        boolean isUsageExists = dashboardPopulateResponse.getDataDeposit() != null && dashboardPopulateResponse.getDataDeposit().isAdUsage();
        boolean isAdExists = dashboardPopulateResponse.getTotalAd() != null && getTotalAd(dashboardPopulateResponse.getTotalAd()) > 0;
        snackbarRetry.hideRetrySnackbar();
        swipeToRefresh.setRefreshing(false);
        if (isUsageExists || isAdExists){
            onLoadTopAdsShopDepositSuccess(dashboardPopulateResponse.getDataDeposit());
            onSuccessPopulateTotalAds(dashboardPopulateResponse.getTotalAd());
            loadStatisticsData();
            getView().findViewById(R.id.topads_dashboard_empty).setVisibility(View.GONE);
            getView().findViewById(R.id.topads_dashboard_content).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.topads_dashboard_empty).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.topads_dashboard_content).setVisibility(View.GONE);
        }
    }

    private int getTotalAd(TotalAd totalAd) {
        return totalAd.getTotalShopAd() + totalAd.getTotalKeyword()+ totalAd.getTotalProductAd() + totalAd.getTotalProductGroupAd();
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
                        if (!isAdded()) {
                            return;
                        }
                        selectedStatisticType = item.getItemId();
                        topAdsTabAdapter.setStatisticsType(selectedStatisticType);
                        if (getConversionFragment() != null){
                            getConversionFragment().updateTitle(selectedStatisticType);
                        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_ads_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {
            showMoreBottomSheetDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMoreBottomSheetDialog() {
        final Menus menus = new Menus(getActivity());
        menus.setItemMenuList(R.array.top_ads_dashboard_menu_more);
        menus.setActionText(getString(R.string.close));
        menus.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menus.dismiss();
            }
        });

        menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
            @Override
            public void onClick(Menus.ItemMenus itemMenus, int pos) {
                switch (pos){
                    case 0: {
                        getScrollView().scrollTo(0,0);
                        startShowCase();
                        menus.dismiss();
                        break;
                    }
                    case 1: {
                        menus.dismiss();
                        startActivity(new Intent(getActivity(), SellerCenterActivity.class));
                        break;
                    }
                    default: break;
                }
            }
        });

        menus.show();
    }

    public interface Callback{
        void startShowCase();
    }
}
