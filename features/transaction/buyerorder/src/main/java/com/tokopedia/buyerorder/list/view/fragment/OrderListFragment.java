package com.tokopedia.buyerorder.list.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.common.util.BuyerUtils;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.list.common.OrderListContants;
import com.tokopedia.buyerorder.list.common.SaveDateBottomSheetActivity;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.OrderLabelList;
import com.tokopedia.buyerorder.list.data.bomorderfilter.CustomDate;
import com.tokopedia.buyerorder.list.data.bomorderfilter.DefaultDate;
import com.tokopedia.buyerorder.list.di.DaggerOrderListComponent;
import com.tokopedia.buyerorder.list.di.OrderListComponent;
import com.tokopedia.buyerorder.list.di.OrderListUseCaseModule;
import com.tokopedia.buyerorder.list.view.activity.OrderListActivity;
import com.tokopedia.buyerorder.list.view.adapter.OrderListAdapter;
import com.tokopedia.buyerorder.list.view.adapter.WishListResponseListener;
import com.tokopedia.buyerorder.list.view.adapter.factory.OrderListAdapterFactory;
import com.tokopedia.buyerorder.list.view.adapter.viewholder.EmptyStateMarketPlaceFilterViewHolder;
import com.tokopedia.buyerorder.list.view.adapter.viewholder.OrderListRecomListViewHolder;
import com.tokopedia.buyerorder.list.view.adapter.viewholder.OrderListViewHolder;
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListRecomUiModel;
import com.tokopedia.buyerorder.list.view.presenter.OrderListContract;
import com.tokopedia.buyerorder.list.view.presenter.OrderListPresenterImpl;
import com.tokopedia.datepicker.DatePickerUnify;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewRounderCornerFilterView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import kotlin.Unit;


public class OrderListFragment extends BaseDaggerFragment implements

        OrderListRecomListViewHolder.ActionListener, RefreshHandler.OnRefreshHandlerListener,
        OrderListContract.View, QuickSingleFilterView.ActionListener, SearchInputView.Listener, QuickSingleFilterView.QuickFilterAnalytics,
        SearchInputView.ResetListener, OrderListViewHolder.OnMenuItemListener, View.OnClickListener,
        EmptyStateMarketPlaceFilterViewHolder.ActionListener {


    private static final String ORDER_CATEGORY = "orderCategory";
    private static final String ORDER_TAB_LIST = "TAB_LIST";
    private static final int MINIMUM_CHARATERS_HIT_API = 3;
    private static final int FILTER_DATE_REQUEST = 1;
    private static final int ANIMATION_DURATION = 500;
    private static final int SUBMIT_SURVEY_REQUEST = 2;
    public static final String OPEN_SURVEY_PAGE = "2";
    private static final String KEY_URI = "tokopedia";
    private static final String KEY_URI_PARAMETER = "idem_potency_key";
    private static final String KEY_URI_PARAMETER_EQUAL = "idem_potency_key=";
    private static final String INVOICE_URL = "invoiceUrl";
    private static final String TX_ASK_SELLER = "tx_ask_seller";
    public static final String STATUS_CODE_220 = "220";
    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_11 = "11";
    public static final int REQUEST_CANCEL_ORDER = 101;
    public static final int REJECT_BUYER_REQUEST = 102;
    public static final int CANCEL_BUYER_REQUEST = 103;
    private static final long KEYBOARD_SEARCH_WAITING_TIME = 300;
    public static final String ACTION_BUY_AGAIN = "beli lagi";
    public static final String ACTION_ASK_SELLER = "tanya penjual";
    public static final String ACTION_TULIS_REVIEW = "tulis review";
    private static final String ACTION_TRACK_IT = "lacak";
    public static final String ACTION_SUBMIT_CANCELLATION = "ajukan pembatalan";
    private static final String ACTION_DONE = "selesai";
    private static final String ACTION_SIMILAR_PRODUCT = "rekomendasi";
    private static final String CLICK_SIMILAR_PRODUCT = "click lihat produk serupa";
    private static final String CLICK_TULIS_REVIEW = "click button tulis review";
    private static final String MULAI_DARI = "Mulai Dari";
    private static final String SAMPAI = "Sampai";
    private static final int DEFAULT_FILTER_YEAR = 2017;
    private static final int DEFAULT_FILTER_MONTH = 0;
    private static final int DEFAULT_FILTER_DATE = 1;

    OrderListComponent orderListComponent;
    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    LinearLayout filterDate;
    ImageView check;
    RelativeLayout mainContent;
    private View categoryView;
    private ImageView crossIcon;
    private EditText mulaiButton;
    private EditText sampaiButton;
    private UnifyButton terapkan;
    private RelativeLayout datePickerlayout;
    private RadioGroup radioGroup;
    private RadioButtonUnify radio1, radio2;
    private DatePickerUnify datePickerUnify;
    private RefreshHandler refreshHandler;
    private TextView reset;
    private boolean isLoading = false;
    private int page_num = 1;
    private Bundle savedState;
    private String startDate = "";
    private String endDate = "";
    private String defStartDate = "";
    private String defEndDate = "";
    private String customStartDate = "";
    private String customEndDate = "";
    private String datePickerStartDate = "";
    private String datePickerEndDate = "";
    private boolean customFilter = false;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_FORMAT_1 = "yyyy/MM/dd";
    private static final String DATE_FORMAT_2 = "d MMM yyyy";
    private static final String DATE_FORMAT_3 = "d M yyyy";
    private static long _days90 = 90;

    private int orderId = 1;
    private String selectedOrderId = "0";
    private String actionButtonUri = "";
    private HashMap<String, String> selectedDateMap = new HashMap<>();
    private SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT_1, Locale.getDefault());
    private SimpleDateFormat format2 = new SimpleDateFormat(DATE_FORMAT_2, new Locale("ind", "IND"));

    @Inject
    OrderListAnalytics orderListAnalytics;

    @Inject
    UserSessionInterface userSession;

    private String selectedFilter = "18";

    private CustomViewRounderCornerFilterView quickSingleFilterView;

    private SearchInputView simpleSearchView;

    private ImageView surveyBtn;

    private String searchedString = "";

    @Inject
    OrderListPresenterImpl presenter;

    private boolean hasRecyclerListener = false;

    private String mOrderCategory;
    private OrderLabelList orderLabelList;
    private boolean isSurveyBtnVisible = true;
    private OrderListAdapter orderListAdapter;
    private Boolean isRecommendation = false;
    private GridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private TrackingQueue trackingQueue;
    private CloseableBottomSheetDialog changeDateBottomSheetDialog;

    private boolean isPulledToRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackingQueue = new TrackingQueue(getActivity());
        setRetainInstance(isRetainInstance());
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        initialPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_list_order_module, container, false);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_list_order_module;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setViewListener();
        setActionVar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(getOptionsMenuEnable());
        initialListener(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b == null) b = new Bundle();
            b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }

    @Override
    public void onDestroyView() {
        saveStateToArguments();
        presenter.detachView();
        super.onDestroyView();
    }

    protected boolean isRetainInstance() {
        return false;
    }


    protected void onFirstTimeLaunched() {

    }


    public void onSaveState(Bundle state) {

    }


    public void onRestoreState(Bundle savedState) {

    }

    protected boolean getOptionsMenuEnable() {
        return false;
    }


    protected void initialPresenter() {
        presenter.attachView(this);
    }

    protected void initInjector() {
        orderListComponent = DaggerOrderListComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .orderListUseCaseModule(new OrderListUseCaseModule())
                .build();
        orderListComponent.inject(this);
    }

    protected void initialListener(Activity activity) {
    }

    protected void setupArguments(Bundle arguments) {
        mOrderCategory = arguments.getString(ORDER_CATEGORY);
        orderLabelList = arguments.getParcelable(ORDER_TAB_LIST);
        if (arguments.getString(OrderListContants.ORDER_FILTER_ID) != null) {
            selectedFilter = arguments.getString(OrderListContants.ORDER_FILTER_ID);
        }
    }

    private Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else return context.getResources().getConfiguration().locale;
    }


    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.order_list_rv);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        quickSingleFilterView = view.findViewById(R.id.quick_filter);
        simpleSearchView = view.findViewById(R.id.simpleSearchView);
        simpleSearchView.setSearchHint(getContext().getResources().getString(R.string.search_hint_text));
        filterDate = view.findViewById(R.id.filterDate);
        check = view.findViewById(R.id.checkImageView);
        surveyBtn = view.findViewById(R.id.survey_bom);
        surveyBtn.setOnClickListener(this);
        mainContent = view.findViewById(R.id.mainContent);
        //default 90 days filter
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -90);
        Date today90 = cal.getTime();
        endDate = selectedDateMap.get(SAMPAI) != null ? selectedDateMap.get(SAMPAI) : format.format(date);
        startDate = selectedDateMap.get(MULAI_DARI) != null ? selectedDateMap.get(MULAI_DARI) : format.format(today90);
        changeDateBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        if (orderLabelList != null && orderLabelList.getFilterStatusList() != null && orderLabelList.getFilterStatusList().size() > 0) {
            presenter.buildAndRenderFilterList(orderLabelList.getFilterStatusList());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SUBMIT_SURVEY_REQUEST) {
                presenter.insertSurveyRequest(getContext(), data.getIntExtra(SaveDateBottomSheetActivity.SURVEY_RATING, 3),
                        data.getStringExtra(SaveDateBottomSheetActivity.SURVEY_COMMENT));
            }
        } else if (requestCode == REQUEST_CANCEL_ORDER) {
            String reason = "";
            int reasonCode = 1;
            if (resultCode == REJECT_BUYER_REQUEST) {
                reason = data.getStringExtra(OrderListContants.REASON);
                reasonCode = data.getIntExtra(OrderListContants.REASON_CODE, 1);
                presenter.updateOrderCancelReason(getContext(), reason, selectedOrderId, reasonCode, actionButtonUri);
            } else if (resultCode == CANCEL_BUYER_REQUEST) {
                reason = data.getStringExtra(OrderListContants.REASON);
                reasonCode = data.getIntExtra(OrderListContants.REASON_CODE, 1);
                presenter.updateOrderCancelReason(getContext(), reason, selectedOrderId, reasonCode, actionButtonUri);
            }
        }
    }

    protected void setViewListener() {
        refreshHandler = new RefreshHandler(getActivity(), getView().findViewById(R.id.swipe_refresh_layout), this);
        refreshHandler.setPullEnabled(true);
        layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(onSpanSizeLookup());
        orderListAdapter = new OrderListAdapter(new OrderListAdapterFactory(orderListAnalytics, this, this, this));
        orderListAdapter.setVisitables(new ArrayList<>());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderListAdapter);
        recyclerView.setHasFixedSize(false);
        quickSingleFilterView.setListener(this);
        quickSingleFilterView.setquickFilterListener(this);
        simpleSearchView.setListener(this);
        simpleSearchView.setResetListener(this);
        filterDate.setOnClickListener(this);

        swipeToRefresh.setOnRefreshListener(() -> {
            isPulledToRefresh = true;
            doRefresh();
        });
    }

    private void addRecyclerListener() {
        hasRecyclerListener = true;
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isRecommendation) {
                    presenter.processGetRecommendationData(getContext(), endlessRecyclerViewScrollListener.getCurrentPage(), false);
                } else {
                    page_num++;
                    if (!isLoading) {
                        isLoading = true;
                        presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.LOAD_MORE, page_num, orderId);
                    }
                    orderListAnalytics.sendLoadMoreEvent("load-" + page_num);
                }
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                if (dy > 0 || dy < 0 && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA)))
                    setVisibilitySurveyBtn(false);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA))) {
                    setVisibilitySurveyBtn(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @Override
    public void onRefresh(View view) {
        doRefresh();
    }

    private void doRefresh() {
        page_num = 1;
        isLoading = true;
        presenter.onRefresh();
        if (orderListAdapter != null) {
            orderListAdapter.clearAllElements();
        }
        if (mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
            quickSingleFilterView.setVisibility(View.VISIBLE);
            simpleSearchView.setVisibility(View.VISIBLE);
        }
        if (isPulledToRefresh && getActivity() != null) {
            ((OrderListActivity)getActivity()).getInitialData(mOrderCategory);
            isPulledToRefresh = false;
        }
        presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num, 1);
    }

    @Override
    public void removeProgressBarView() {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
    }

    @Override
    public void unregisterScrollListener() {
        hasRecyclerListener = false;
    }

    @Override
    public void showErrorNetwork(String errorMessage) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_no_connection_initial_cart_data),
                getString(R.string.label_transaction_error_message_try_again),
                getString(R.string.label_title_button_retry), 0,
                getEditShipmentRetryListener()
        );
    }

    @Override
    public void renderEmptyList(int typeRequest, long elapsedDays) {
        if (typeRequest == TxOrderNetInteractor.TypeRequest.INITIAL) {
            swipeToRefresh.setVisibility(View.VISIBLE);
            if (!hasRecyclerListener) {
                addRecyclerListener();
            }
            if (mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
                orderListAdapter.setEmptyMarketplaceFilter();
                presenter.processGetRecommendationData(getContext(), endlessRecyclerViewScrollListener.getCurrentPage(), true);
            } else {
                orderListAdapter.setEmptyOrderList();
            }
            filterDate.setVisibility(View.GONE);
            surveyBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void setLastOrderId(int orderid) {
        this.orderId = orderid;
    }

    private NetworkErrorHelper.RetryClickedListener getEditShipmentRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (orderListAdapter != null) {
                    orderListAdapter.clearAllElements();
                }
                presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num, 1);
            }
        };
    }


    protected void setActionVar() {
        initialData();
    }

    private void initialData() {
        if (!isLoading && getActivity() != null
                && (orderListAdapter == null || orderListAdapter.getItemCount() == 0)) {
            refreshHandler.startRefresh();
        }
    }

    @Override
    public void showProcessGetData() {
        if (!refreshHandler.isRefreshing()) {
            refreshHandler.setRefreshing(true);
            refreshHandler.setPullEnabled(false);
        }
    }


    @Override
    public void showFailedResetData(String message) {

    }

    @Override
    public void showNoConnectionResetData(String message) {

    }

    @Override
    public void showEmptyData(int typeRequest) {

    }

    @Override
    public void startUri(String uri) {
        if (!uri.equals("")) {
            RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, uri);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void selectFilter(String typeFilter) {
        selectedFilter = typeFilter;
        refreshHandler.startRefresh();
    }


    @Override
    public void renderOrderStatus(List<QuickFilterItem> filterItems, int selctedIndex) {
        quickSingleFilterView.setDefaultItem(filterItems.get(selctedIndex));
        quickSingleFilterView.renderFilter(filterItems, selctedIndex);
    }

    @Override
    public void showSurveyButton(boolean isEligible) {
        if (isEligible && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA))) {
            surveyBtn.setVisibility(View.VISIBLE);
        } else {
            surveyBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public String getSearchedString() {
        return searchedString;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public void showSuccessMessage(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.design.R.string.close), v->{}).show();
        }
    }

    @Override
    public void showFailureMessage(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, "", v->{}).show();
        }
    }

    @Override
    public void showSuccessMessageWithAction(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.bom_check_cart), v -> RouteManager.route(getContext(), ApplinkConst.CART)).show();
        }
    }

    @Override
    public void setFilterRange(DefaultDate defaultDate, CustomDate customDate) {

        defStartDate = BuyerUtils.setFormat(format, format1, defaultDate.getStartRangeDate());
        defEndDate = BuyerUtils.setFormat(format, format1, defaultDate.getEndRangeDate());
        customEndDate = BuyerUtils.setFormat(format, format1, customDate.getEndRangeDate());
        customStartDate = BuyerUtils.setFormat(format, format1, customDate.getStartRangeDate());
    }

    @Override
    public void sendATCTrackingUrl(String url, String productId, String productName, String imageUrl) {
        String clickUrl = url + "&click_source=ATC_direct_click";
        new TopAdsUrlHitter(this.getClass().getCanonicalName()).hitClickUrl(getContext(), clickUrl, productId, productName, imageUrl);
    }

    @Override
    public void addData(List<Visitable> data, Boolean isRecommendation, boolean isInitial) {
        this.isRecommendation = isRecommendation;
        if (!hasRecyclerListener) {
            addRecyclerListener();
        }
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (isInitial) {
            orderListAdapter.setElements(data);
        } else {
            orderListAdapter.addElement(data);
        }
        endlessRecyclerViewScrollListener.updateStateAfterGetData();
        swipeToRefresh.setVisibility(View.VISIBLE);
        if ((mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || (mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) && !isRecommendation) || orderLabelList.getOrderCategory().equalsIgnoreCase(OrderCategory.DIGITAL)) {
            filterDate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        if (orderListAdapter != null) {
            if (isLoadMore) {
                orderListAdapter.showLoading();
            } else {
                orderListAdapter.hideLoading();
            }
        }
    }

    @Override
    public void triggerSendEnhancedEcommerceAddToCartSuccess(AddToCartDataModel addToCartDataResponseModel, Object productModel) {
        if (productModel instanceof OrderListRecomUiModel) {
            OrderListRecomUiModel orderListRecomUiModel = (OrderListRecomUiModel) productModel;
            orderListAnalytics.eventRecommendationAddToCart(orderListRecomUiModel, addToCartDataResponseModel);
        }
    }

    @Override
    public String getSelectedFilter() {
        return String.valueOf(selectedFilter);
    }

    @Override
    public void onSearchSubmitted(String text) {

        searchedString = text;
        orderListAnalytics.sendSearchFilterClickEvent(text);

    }

    @Override
    public void onSearchTextChanged(String text) {
        if (text.length() >= MINIMUM_CHARATERS_HIT_API || text.length() == 0) {
            searchedString = text;
            filterDate.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(() -> refreshHandler.startRefresh(), KEYBOARD_SEARCH_WAITING_TIME);
        }
    }

    @Override
    public void onSearchReset() {
        searchedString = "";
        refreshHandler.startRefresh();
        orderListAnalytics.sendSearchFilterCancelClickEvent();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.survey_bom) {
            startActivityForResult(SaveDateBottomSheetActivity.getSurveyInstance(getContext(), OPEN_SURVEY_PAGE), SUBMIT_SURVEY_REQUEST);
        } else if (v.getId() == R.id.terapkan) {
            if (radio1.isChecked()) {
                startDate = defStartDate;
                endDate = defEndDate;
                customFilter = false;
            } else {
                customFilter = true;
                startDate = BuyerUtils.setFormat(format, format2, mulaiButton.getText().toString());
                endDate = BuyerUtils.setFormat(format, format2, sampaiButton.getText().toString());
            }
            selectedDateMap.clear();
            selectedDateMap.put(SAMPAI, endDate);
            selectedDateMap.put(MULAI_DARI, startDate);
            check.setVisibility(View.VISIBLE);
            refreshHandler.startRefresh();
            orderListAnalytics.sendDateFilterSubmitEvent();
            changeDateBottomSheetDialog.dismiss();

        } else if (v.getId() == R.id.filterDate) {
            filter();
        }
    }

    private void filter() {
        initBottomSheet();
        reset.setOnClickListener(view -> {
            radio1.setChecked(true);
            selectedDateMap.clear();
            customFilter = false;
            datePickerlayout.setVisibility(View.GONE);
            sampaiButton.setText(BuyerUtils.setFormat(format2, format, customEndDate));
            mulaiButton.setText(BuyerUtils.setFormat(format2, format, customStartDate));
            datePickerStartDate = "";
            datePickerEndDate = "";
        });
        if (customFilter) {
            radio2.setChecked(true);
            datePickerlayout.setVisibility(View.VISIBLE);
        } else {
            radio1.setChecked(true);
        }
        sampaiButton.setText(BuyerUtils.setFormat(format2, format, selectedDateMap.get(SAMPAI) != null ? selectedDateMap.get(SAMPAI) : customEndDate));
        mulaiButton.setText(BuyerUtils.setFormat(format2, format, selectedDateMap.get(MULAI_DARI) != null ? selectedDateMap.get(MULAI_DARI) : customStartDate));

        crossIcon.setOnClickListener((View view) -> {
            changeDateBottomSheetDialog.dismiss();
        });
        changeDateBottomSheetDialog.setCustomContentView(categoryView, "", false);
        changeDateBottomSheetDialog.show();

        radioGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            if (checkedId == R.id.radio2) {
                datePickerlayout.setVisibility(View.VISIBLE);

            } else {
                datePickerlayout.setVisibility(View.GONE);
            }
        });
        mulaiButton.setOnClickListener((View view) -> {
            showDatePicker(MULAI_DARI);
        });
        sampaiButton.setOnClickListener((View view) -> {
            showDatePicker(SAMPAI);
        });
    }

    private void initBottomSheet() {
        categoryView = getLayoutInflater().inflate(R.layout.change_bom_deadline_bottomsheet, null);
        crossIcon = categoryView.findViewById(R.id.cross_icon_bottomsheet);
        mulaiButton = categoryView.findViewById(R.id.et_start_date);
        sampaiButton = categoryView.findViewById(R.id.et_end_date);
        terapkan = categoryView.findViewById(R.id.terapkan);
        radio1 = categoryView.findViewById(R.id.radio1);
        radio2 = categoryView.findViewById(R.id.radio2);
        datePickerlayout = categoryView.findViewById(R.id.date_picker);
        radioGroup = categoryView.findViewById(R.id.radio_grp);
        reset = categoryView.findViewById(R.id.reset);
        terapkan.setOnClickListener(this);
        orderListAnalytics.sendDateFilterClickEvent();
    }

    private String[] split(String date) {
        String[] dateFormat = new String[0];
        if (date != null) {
            dateFormat = date.split("/", 5);
        }
        return dateFormat;
    }


    private void showDatePicker(String title) {
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        Calendar defaultDate = Calendar.getInstance();

        String[] minStartDate = customStartDate.split("/");

        if (!TextUtils.isEmpty(datePickerStartDate) && !TextUtils.isEmpty(datePickerEndDate)) {
            String[] resultStartDate = split(datePickerStartDate);
            String[] resultEndDate = split(datePickerEndDate);

            if (title.equalsIgnoreCase(MULAI_DARI)) {
                minDate.set(Integer.parseInt(minStartDate[2]), Integer.parseInt(minStartDate[1])-1, Integer.parseInt(minStartDate[0]));

                defaultDate.set(Integer.parseInt(resultStartDate[2]), Integer.parseInt(resultStartDate[1]), Integer.parseInt(resultStartDate[0]));
                maxDate.set(Integer.parseInt(resultEndDate[2]), Integer.parseInt(resultEndDate[1]), Integer.parseInt(resultEndDate[0]));

            } else {
                minDate.set(Integer.parseInt(resultStartDate[2]), Integer.parseInt(resultStartDate[1]), Integer.parseInt(resultStartDate[0]));
                defaultDate.set(Integer.parseInt(resultEndDate[2]), Integer.parseInt(resultEndDate[1]), Integer.parseInt(resultEndDate[0]));
            }
        } else {
            if (title.equalsIgnoreCase(MULAI_DARI)) {
                minDate.set(Integer.parseInt(minStartDate[2]), Integer.parseInt(minStartDate[1])-1, Integer.parseInt(minStartDate[0]));
                defaultDate.set(DEFAULT_FILTER_YEAR, DEFAULT_FILTER_MONTH, DEFAULT_FILTER_DATE);
            } else {
                minDate.set(Integer.parseInt(minStartDate[2]), Integer.parseInt(minStartDate[1])-1, Integer.parseInt(minStartDate[0]));
            }
        }

        datePickerUnify = new DatePickerUnify(getActivity(), minDate, defaultDate, maxDate, null);

        if (title.equalsIgnoreCase(MULAI_DARI)) {
            datePickerUnify.setTitle(MULAI_DARI);

        } else {
            datePickerUnify.setTitle(SAMPAI);
        }

        datePickerUnify.show(getFragmentManager(), "");
        datePickerUnify.getDatePickerButton().setOnClickListener((View v) -> {
            Integer[] date = datePickerUnify.getDate();
            if (title.equalsIgnoreCase(SAMPAI)) {
                sampaiButton.setText(date[0] + " " + BuyerUtils.convertMonth(date[1], getActivity()) + " " + date[2]);
                datePickerEndDate = date[0] + "/" + date[1] + "/" + date[2];

            } else {
                mulaiButton.setText(date[0] + " " + BuyerUtils.convertMonth(date[1], getActivity()) + " " + date[2]);
                datePickerStartDate = date[0] + "/" + date[1] + "/" + date[2];
            }
            datePickerUnify.dismiss();
        });

        datePickerUnify.setCloseClickListener(view -> {
            datePickerUnify.dismiss();
            return Unit.INSTANCE;
        });

    }

    private void setVisibilitySurveyBtn(boolean isVisible) {
        if (isVisible && !isSurveyBtnVisible) {
            surveyBtn.animate().translationY(0).setDuration(ANIMATION_DURATION).start();
            isSurveyBtnVisible = true;
        } else if (!isVisible && isSurveyBtnVisible) {
            surveyBtn.animate().translationY(surveyBtn.getHeight() + getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_10)).setDuration(ANIMATION_DURATION).start();
            isSurveyBtnVisible = false;
        }
    }

    public GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (orderListAdapter.getItemViewType(position) == OrderListRecomListViewHolder.LAYOUT) {
                    return 1;
                } else {
                    return 2;
                }
            }
        };
    }

    @Override
    public void onCartClicked(@NotNull Object productModel) {
        presenter.processAddToCart(productModel);
    }

    @Override
    public void onPause() {
        super.onPause();
        orderListAnalytics.sendEmptyWishlistProductImpression(trackingQueue);
        trackingQueue.sendAll();
    }

    @Override
    public void onWishListClicked(@NotNull Object productModel, boolean isSelected, @NotNull WishListResponseListener wishListResponseListener) {
        if (productModel instanceof OrderListRecomUiModel) {
            if (isSelected) {
                presenter.addWishlist(((OrderListRecomUiModel) productModel).getRecommendationItem(), wishListResponseListener);
            } else {
                presenter.removeWishlist(((OrderListRecomUiModel) productModel).getRecommendationItem(), wishListResponseListener);
            }
        }
    }

    @Override
    public void setSelectFilterName(String selectFilterName) {
        orderListAnalytics.sendQuickFilterClickEvent(selectFilterName);
    }

    private void trackOrder() {
        String routingAppLink;
        routingAppLink = ApplinkConst.ORDER_TRACKING.replace("{order_id}", selectedOrderId);
        String trackingUrl;
        Uri uri = Uri.parse(actionButtonUri);
        trackingUrl = uri.getQueryParameter("url");
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, trackingUrl);
        routingAppLink += uriBuilder.toString();
        RouteManager.route(getContext(), routingAppLink);
    }

    @Override
    public void finishOrderDetail() {
        refreshHandler.startRefresh();
    }

    @Override
    public void filterClicked() {
        filter();
    }
}

