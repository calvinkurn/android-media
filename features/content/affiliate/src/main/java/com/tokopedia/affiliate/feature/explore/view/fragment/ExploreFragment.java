package com.tokopedia.affiliate.feature.explore.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.analytics.AffiliateEventTracking;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.common.preference.AffiliatePreference;
import com.tokopedia.affiliate.common.viewmodel.ExploreCardViewModel;
import com.tokopedia.affiliate.common.viewmodel.ExploreTitleViewModel;
import com.tokopedia.affiliate.common.widget.ExploreSearchView;
import com.tokopedia.affiliate.feature.education.view.activity.AffiliateEducationActivity;
import com.tokopedia.affiliate.feature.explore.di.DaggerExploreComponent;
import com.tokopedia.affiliate.feature.explore.view.activity.ExploreActivity;
import com.tokopedia.affiliate.feature.explore.view.activity.FilterActivity;
import com.tokopedia.affiliate.feature.explore.view.activity.SortActivity;
import com.tokopedia.affiliate.feature.explore.view.adapter.AutoCompleteSearchAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.ExploreAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.FilterAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactoryImpl;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.AutoCompleteViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreBannerChildViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreProductViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileChildViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ProductTitleViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.RecommendationViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel;
import com.tokopedia.affiliate.util.AffiliateHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.badge.BadgeView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.kotlin.extensions.view.ViewExtKt;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreFragment
        extends BaseDaggerFragment
        implements ExploreContract.View,
        SearchInputView.Listener,
        SearchInputView.ResetListener,
        SwipeToRefresh.OnRefreshListener,
        FilterAdapter.OnFilterClickedListener {

    private static final String TAG_SHOWCASE = ExploreActivity.class.getName() +
            ".bottomNavigation";
    private static final String PRODUCT_ID_PARAM = "{product_id}";
    private static final String AD_ID_PARAM = "{ad_id}";
    private static final String USER_ID_USER_ID = "{user_id}";
    private static final String PRODUCT_ID_QUERY_PARAM = "?product_id=";
    private static final String PERFORMANCE_AFFILIATE = "mp_affiliate";

    private static final int ITEM_COUNT = 10;
    private static final int FULL_SPAN_COUNT = 2;
    private static final int SINGLE_SPAN_COUNT = 1;
    private static final int LOGIN_CODE = 13;


    private static final int TIME_DEBOUNCE_MILIS = 500;
    public static final int REQUEST_DETAIL_FILTER = 1234;
    public static final int REQUEST_DETAIL_SORT = 2345;

    private ExploreAdapter adapter;
    private ExploreParams exploreParams;
    private EmptyModel emptyResultModel;
    private String firstCursor = "";
    private List<Visitable<?>> tempFirstData = new ArrayList<>();
    private List<Visitable<?>> sections = new ArrayList<>();
    private List<SortViewModel> tempLocalSortData = new ArrayList<>();
    private RemoteConfig remoteConfig;
    private PerformanceMonitoring performanceMonitoring;

    private FrameLayout autoCompleteLayout;
    private AutoCompleteSearchAdapter autoCompleteAdapter;
    private ImageView ivBack, ivBantuan, ivProfile;
    private RecyclerView rvExplore, rvAutoComplete;
    private GridLayoutManager layoutManager;
    private SwipeToRefresh swipeRefreshLayout;
    private ExploreSearchView searchView;
    private FrameLayout layoutEmpty;
    private FrameLayout layoutProfile;
    private BottomActionView bottomActionView;
    private FloatingActionButton btnBackToTop;
    private BadgeView badgeView;

    private boolean isCanDoAction;
    private boolean isTraceStopped;

    @Inject
    UserSessionInterface userSession;

    @Inject
    ExploreContract.Presenter presenter;

    @Inject
    AffiliateAnalytics affiliateAnalytics;

    @Inject
    AffiliatePreference affiliatePreference;

    public static ExploreFragment getInstance(Bundle bundle) {
        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(PERFORMANCE_AFFILIATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_explore, container, false);
        rvExplore = view.findViewById(R.id.rv_explore);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        searchView = view.findViewById(R.id.search_input_view);
        ivBack = view.findViewById(R.id.iv_back);
        ivBantuan = view.findViewById(R.id.action_bantuan);
        autoCompleteLayout = view.findViewById(R.id.layout_auto_complete);
        rvAutoComplete = view.findViewById(R.id.rv_search_auto_complete);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        layoutProfile = view.findViewById(R.id.action_profile);
        ivProfile = view.findViewById(R.id.iv_profile);
        bottomActionView = view.findViewById(R.id.bav);
        btnBackToTop = view.findViewById(R.id.btn_back_to_top);
        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this, this), new ArrayList<>());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        initView();
        initListener();
        loadFirstData(false);

        if (affiliatePreference.isFirstTimeEducation(userSession.getUserId())) {
            goToEducation();
        }
    }

    private void initView() {
        layoutEmpty.setVisibility(View.GONE);
        dropKeyboard();
        initEmptyResultModel();
        initProfileSection();
        autoCompleteLayout.setVisibility(View.GONE);
        btnBackToTop.hide();
        exploreParams = new ExploreParams();
        swipeRefreshLayout.setOnRefreshListener(this);
        searchView.setListener(this);
        searchView.setDelayTextChanged(TIME_DEBOUNCE_MILIS);
        searchView.setResetListener(this);
        searchView.getSearchTextView().setOnClickListener(v -> {
            searchView.getSearchTextView().setCursorVisible(true);
        });
        layoutManager = new GridLayoutManager(getContext(),
                FULL_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter == null || adapter.getData().isEmpty()) {
                    return 0;
                }

                if (adapter.getData().get(position) instanceof ExploreProductViewModel) {
                    return SINGLE_SPAN_COUNT;
                }
                return FULL_SPAN_COUNT;
            }
        });
        rvExplore.setLayoutManager(layoutManager);
        rvExplore.addOnScrollListener(getScrollListener());
        rvExplore.addItemDecoration(getItemDecoration());

        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this, this), new ArrayList<>());
        rvExplore.setAdapter(adapter);
    }

    private void initEmptyResultModel() {
        emptyResultModel = new EmptyModel();
        emptyResultModel.setIconRes(R.drawable.ic_empty_search);
        emptyResultModel.setTitle(
                getString(R.string.text_product_not_found)
        );
    }

    private void initProfileSection() {
        //init image
        if (userSession.isLoggedIn()) {
            ImageHandler.loadImageCircle2(getActivity(), ivProfile,
                    userSession.getProfilePicture(), R.drawable.loading_page);
        }

        if (getActivity() == null) {
            return;
        }

        //init red dot
        if (AffiliateHelper.isFirstTimeOpenProfileFromExplore(getActivity())) {
            if (badgeView == null) {
                badgeView = new BadgeView(getActivity());
            }
            badgeView.bindTarget(layoutProfile);
            badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
            badgeView.setBadgeNumber(-1);
        } else {
            if (badgeView != null) {
                badgeView.hide(true);
            }
        }
        if (!ShowCasePreference.hasShown(getActivity(), TAG_SHOWCASE)) {
            showShowCase();
        }
    }

    private void initListener() {
        if (getActivity() == null) {
            return;
        }

        ivBack.setOnClickListener(view -> getActivity().onBackPressed());
        ivBantuan.setOnClickListener(view -> {
            affiliateAnalytics.onInfoClicked();
            goToEducation();
        });
        btnBackToTop.setOnClickListener(view -> rvExplore.scrollToPosition(0));
        layoutProfile.setOnClickListener(view -> {
            if (!userSession.isLoggedIn()) {
                goToLogin();
            } else {
                AffiliateHelper.setFirstTimeOpenProfileFromExplore(getActivity());
                goToProfile();
                initProfileSection();
                affiliateAnalytics.onProfileClicked(userSession.getUserId());
            }
        });
        bottomActionView.setButton2OnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(
                    FilterActivity.PARAM_FILTER_LIST,
                    new ArrayList<>(getAllFilterList())
            );
            startActivityForResult(
                    FilterActivity.getIntent(getActivity(), bundle),
                    REQUEST_DETAIL_FILTER
            );
            affiliateAnalytics.onFilterClicked(userSession.getUserId());
        });
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent =
                ((BaseMainApplication) getActivity().getApplicationContext()).getBaseAppComponent();

        DaggerAffiliateComponent affiliateComponent =
                (DaggerAffiliateComponent) DaggerAffiliateComponent
                        .builder()
                        .baseAppComponent(baseAppComponent).build();

        DaggerExploreComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return AffiliateEventTracking.Screen.BYME_EXPLORE;
    }

    @Override
    public void onStart() {
        super.onStart();
        affiliateAnalytics.getAnalyticTracker().sendScreen(getActivity(), getScreenName());
    }

    private void loadFirstData(boolean isPullToRefresh) {
        if (!exploreParams.isLoading()) {
            exploreParams.setLoading(true);
            presenter.getFirstData(exploreParams, isPullToRefresh);
        }
    }

    @Override
    public void onRefresh() {
        exploreParams.setPullToRefreshData();
        loadFirstData(true);
    }

    private RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.unsubscribeAutoComplete();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
                if (exploreParams.isCanLoadMore()
                        && !TextUtils.isEmpty(exploreParams.getCursor())
                        && totalItemCount <= lastVisibleItemPos + ITEM_COUNT) {
                    exploreParams.setCanLoadMore(false);
                    adapter.addElement(new LoadingMoreModel());
                    presenter.loadMoreData(exploreParams);
                }
                if (layoutManager.findFirstCompletelyVisibleItemPosition() > FULL_SPAN_COUNT) {
                    btnBackToTop.show();
                } else {
                    btnBackToTop.hide();
                }

            }
        };
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                Visitable visitable = adapter.getData().get(position);
                if (visitable instanceof ExploreProductViewModel
                        && view.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {
                    int spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
                    if (spanIndex == 0) {
                        outRect.left = (int) getResources().getDimension(R.dimen.dp_4);
                    } else {
                        outRect.right = (int) getResources().getDimension(R.dimen.dp_4);
                    }
                }
            }
        };
    }

    @Override
    public void onSearchSubmitted(String text) {
        dropKeyboard();
        searchView.removeSearchTextWatcher();
        if (autoCompleteLayout.getVisibility() == View.VISIBLE) {
            autoCompleteLayout.setVisibility(View.GONE);
        }
        exploreParams.setSearchParam(text);
        adapter.clearProductElements();
        presenter.unsubscribeAutoComplete();
        presenter.getData(exploreParams);
    }

    @Override
    public void onSearchTextChanged(String text) {
        onSearchTextModified(text, false);
    }

    private void onSearchTextModified(String text, boolean isFromAutoComplete) {
        if (TextUtils.isEmpty(text)) {
            onSearchReset();
        } else {
            if (autoCompleteAdapter != null) autoCompleteAdapter.clearAdapter();
            if (!isFromAutoComplete && !exploreParams.isLoading()) {
                affiliateAnalytics.onSearchSubmitted(text);
                presenter.getAutoComplete(text);
            }
        }
    }

    @Override
    public void onSearchReset() {
        if (autoCompleteLayout.getVisibility() == View.VISIBLE)
            autoCompleteLayout.setVisibility(View.GONE);
        dropKeyboard();
        searchView.removeSearchTextWatcher();
        exploreParams.resetSearch();
        bottomActionView.show(false);
        populateLocalDataToAdapter();
    }

    private void populateLocalDataToAdapter() {
        adapter.clearAllElements();
        adapter.addElement(getLocalFirstData());
    }

    @Override
    public AffiliateAnalytics getAffiliateAnalytics() {
        return affiliateAnalytics;
    }

    @Override
    public void showLoading() {
        adapter.addElement(new LoadingModel());
    }

    @Override
    public void showLoadingScreen() {
        if (getView() != null) {
            ViewExtKt.showLoadingTransparent(getView());
        }
    }

    @Override
    public void hideLoading() {
        adapter.hideLoading();
        if (getView() != null) {
            ViewExtKt.hideLoadingTransparent(getView());
        }
    }

    @Override
    public void dropKeyboard() {
        searchView.getSearchTextView().setCursorVisible(false);
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onBymeClicked(ExploreCardViewModel model) {
        if (isCanDoAction) {
            isCanDoAction = false;
            if (userSession.isLoggedIn()) {
                presenter.checkIsAffiliate(model.getProductId(), model.getAdId());
            } else {
                goToLogin();
            }
        }
    }

    @Override
    public void onProductClicked(ExploreCardViewModel model, int adapterPosition) {
        trackProductClick(model, adapterPosition);

        if (getContext() != null && isCanDoAction) {
            RouteManager.route(
                    getContext(),
                    ApplinkConst.AFFILIATE_PRODUCT
                            .replace(PRODUCT_ID_PARAM, model.getProductId())
            );
        }
        isCanDoAction = false;
    }

    @Override
    public void onBannerClicked(ExploreBannerChildViewModel model) {
        goToLink(model.getRedirectUrl());
        affiliateAnalytics.onBannerClicked(model.getActivityId(), model.getImageUrl());
    }

    @Override
    public void onProfileClicked(PopularProfileChildViewModel model) {
        goToLink(model.getRedirectUrl());
        affiliateAnalytics.onPopularClicked(model.getUserId());
    }

    @Override
    public void onSuccessGetFirstData(List<Visitable<?>> sections,
                                      List<Visitable<?>> products,
                                      String cursor,
                                      boolean isSearch,
                                      boolean isPullToRefresh,
                                      List<SortViewModel> sortViewModels) {
        addTitleBeforeProducts(sections);

        List<Visitable<?>> itemList = new ArrayList<>();
        itemList.addAll(sections);
        itemList.addAll(products);

        trackImpression(itemList);

        populateFirstData(itemList, cursor);
        if (!isPullToRefresh) {
            populateSort(sortViewModels);
            if (!isSearch) saveFirstDataToLocal(sections, itemList, cursor, sortViewModels);
        }
    }

    private void addTitleBeforeProducts(List<Visitable<?>> sections) {
        sections.add(getProductTitleModel());
    }

    private ProductTitleViewModel getProductTitleModel() {
        return new ProductTitleViewModel(new ExploreTitleViewModel(
                getString(R.string.af_product_title),
                getString(R.string.af_product_desc)
        ));
    }

    private void populateFirstData(List<Visitable<?>> itemList, String cursor) {
        rvExplore.scrollTo(0, 0);
        layoutEmpty.setVisibility(View.GONE);
        exploreParams.setLoading(false);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        searchView.addTextWatcherToSearch();
        presenter.unsubscribeAutoComplete();
        bottomActionView.setVisibility(View.VISIBLE);
        populateExploreItem(itemList, cursor);
    }

    private void saveFirstDataToLocal(List<Visitable<?>> sections,
                                      List<Visitable<?>> itemList,
                                      String firstCursor,
                                      List<SortViewModel> sortViewModels) {
        this.sections = sections;
        this.tempFirstData = itemList;
        this.firstCursor = firstCursor;
        this.tempLocalSortData = sortViewModels;
    }

    private List<Visitable<?>> getLocalFirstData() {
        exploreParams.setCursorForLoadMore(this.firstCursor);
        return tempFirstData;
    }

    private void populateExploreItem(List<Visitable<?>> itemList, String cursor) {
        if (itemList.size() == 0) {
            itemList = new ArrayList<>();
            itemList.add(emptyResultModel);
            exploreParams.disableLoadMore();
            if (!TextUtils.isEmpty(exploreParams.getKeyword())) {
                affiliateAnalytics.onSearchNotFound(exploreParams.getKeyword());
            }
        } else {
            exploreParams.setCursorForLoadMore(cursor);
        }
        adapter.clearAllElements();
        adapter.addElement(itemList);
        if (autoCompleteLayout.getVisibility() == View.VISIBLE) {
            autoCompleteLayout.setVisibility(View.GONE);
        }
    }

    private void populateFilter(List<FilterViewModel> currentFilter) {
        adapter.setFilterList(currentFilter);
    }

    private List<FilterViewModel> getOnlySelectedFilter(List<FilterViewModel> filters) {
        ArrayList<FilterViewModel> selectedFilters = new ArrayList<>();
        for (FilterViewModel filter : filters) {
            if (filter.isSelected()) {
                selectedFilters.add(filter);
            }
        }
        return selectedFilters;
    }

    private void populateSort(List<SortViewModel> sortList) {
        //1. show button sort
        //2. handle onclick and passing sortlist and current selected sort (default is first data)
        if (sortList.size() > 0) {
            bottomActionView.setVisibility(View.VISIBLE);
            sortList.get(0).setSelected(true);
            exploreParams.setSort(sortList.get(0));
            bottomActionView.setButton1OnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SortActivity.PARAM_SORT_LIST,
                        new ArrayList<>(sortList));
                bundle.putParcelable(SortActivity.PARAM_SORT_SELECTED, exploreParams.getSort());
                startActivityForResult(SortActivity.getIntent(getActivity(), bundle),
                        REQUEST_DETAIL_SORT);
                affiliateAnalytics.onSortClicked(userSession.getUserId());
            });
        } else {
            bottomActionView.setVisibility(View.GONE);
        }
    }

    private void getFilteredFirstData(List<FilterViewModel> filters) {
        exploreParams.setFilters(filters);
        exploreParams.resetForFilterClick();
        exploreParams.setLoading(true);
        adapter.clearProductElements();
        presenter.getData(exploreParams);
    }

    private void getSortedData(SortViewModel sort) {
        exploreParams.setSort(sort);
        exploreParams.resetForFilterClick();
        exploreParams.setLoading(true);
        adapter.clearProductElements();
        presenter.getData(exploreParams);
    }

    private List<FilterViewModel> getAllFilterList() {
        return adapter.getFilterList();
    }

    @Override
    public void onSuccessGetData(List<Visitable<?>> products, String cursor, boolean isSearch) {
        trackImpression(products);

        exploreParams.setLoading(false);
        bottomActionView.show(false);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        int lastIndex = adapter.getLastIndex();
        int offset = products.size() > 4 ? 4 : products.size();
        adapter.addElement(products);
        rvExplore.smoothScrollToPosition(lastIndex + offset);

        if (TextUtils.isEmpty(cursor)) {
            exploreParams.disableLoadMore();
        } else {
            exploreParams.setCursorForLoadMore(cursor);
        }
        if (autoCompleteLayout.getVisibility() == View.VISIBLE) {
            autoCompleteLayout.setVisibility(View.GONE);
        }

        presenter.unsubscribeAutoComplete();
    }

    @Override
    public void onErrorGetData(String error) {
        showError(error, (view) -> presenter.getData(exploreParams));
    }

    @Override
    public void onErrorGetFirstData(String error) {
        bottomActionView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
        exploreParams.setLoading(false);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        NetworkErrorHelper.showEmptyState(getActivity(),
                layoutEmpty,
                error,
                () -> {
                    layoutEmpty.setVisibility(View.GONE);
                    loadFirstData(false);
                }
        );
        presenter.unsubscribeAutoComplete();
    }

    @Override
    public void onSuccessGetMoreData(List<Visitable<?>> itemList, String cursor) {
        trackImpression(itemList);
        adapter.hideLoading();
        adapter.addElement(itemList);
        if (TextUtils.isEmpty(cursor)) {
            exploreParams.disableLoadMore();
        } else {
            exploreParams.setCursorForLoadMore(cursor);
        }
        if (autoCompleteLayout.getVisibility() == View.VISIBLE) {
            autoCompleteLayout.setVisibility(View.GONE);
        }
        presenter.unsubscribeAutoComplete();
    }

    @Override
    public void onErrorGetMoreData(String error) {
        showError(error, (view) -> presenter.loadMoreData(exploreParams));
    }

    @Override
    public void onButtonEmptySearchClicked() {
        presenter.unsubscribeAutoComplete();
        bottomActionView.show();
        exploreParams.resetParams();
        exploreParams.setLoading(false);
        searchView.getSearchTextView().setText("");
        searchView.getSearchTextView().setCursorVisible(false);
        adapter.clearAllElements();
        adapter.showLoading();
        loadFirstData(false);
    }

    @Override
    public void onEmptySearchResult() {
        exploreParams.setLoading(false);
        presenter.unsubscribeAutoComplete();
        bottomActionView.hide();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        exploreParams.disableLoadMore();
        adapter.clearAllElements();
        adapter.addElement(new ExploreEmptySearchViewModel(
                getString(R.string.text_empty_search_title),
                getString(R.string.text_empty_search_desc)
        ));
    }

    @Override
    public void onEmptyProduct() {
        exploreParams.setLoading(false);
        presenter.unsubscribeAutoComplete();
        bottomActionView.hide();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        exploreParams.disableLoadMore();
        adapter.clearAllElements();
        adapter.addElement(new ExploreEmptySearchViewModel(
                getString(R.string.text_empty_product_title),
                getString(R.string.text_empty_product_desc)
        ));
    }

    @Override
    public void onSuccessCheckAffiliate(boolean isAffiliate, String productId, String adId) {
        if (isAffiliate) {
            presenter.checkAffiliateQuota(productId, adId);
        } else {
            if (getContext() != null) {
                String onboardingApplink = ApplinkConst.AFFILIATE_ONBOARDING
                        .concat(PRODUCT_ID_QUERY_PARAM)
                        .concat(productId);
                Intent intent = RouteManager.getIntent(getContext(), onboardingApplink);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onErrorCheckAffiliate(String error, String productId, String adId) {
        isCanDoAction = true;
    }

    @Override
    public void onSuccessCheckQuota(String productId, String adId) {
        if (getActivity() != null) {
            RouteManager.route(
                    getActivity(),
                    ApplinkConst.AFFILIATE_CREATE_POST
                            .replace(PRODUCT_ID_PARAM, productId)
                            .replace(AD_ID_PARAM, adId)
            );
        }
    }

    @Override
    public void onSuccessCheckQuotaButEmpty() {
        isCanDoAction = true;
        affiliateAnalytics.onJatahRekomendasiHabisDialogShow();
        Dialog dialog = buildDialog();
        dialog.setOnOkClickListener(view -> {
            goToProfile();
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void goToProfile() {
        if (getContext() != null) {
            RouteManager.route(
                    getContext(),
                    ApplinkConst.PROFILE.replace(USER_ID_USER_ID, userSession.getUserId()));
        }
    }

    private void goToLogin() {
        if (getContext() != null) {
            startActivityForResult(
                    RouteManager.getIntent(
                            getContext(),
                            ApplinkConst.LOGIN
                    ),
                    LOGIN_CODE);
        }
    }

    private void goToLink(String url) {
        if (getContext() != null && !TextUtils.isEmpty(url)) {
            if (RouteManager.isSupportApplink(getContext(), url)) {
                RouteManager.route(getContext(), url);
            } else {
                RouteManager.route(
                        getContext(),
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
                );
            }
        }
    }

    private Dialog buildDialog() {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(getString(R.string.text_full_affiliate_title));
        dialog.setDesc(getString(R.string.text_full_affiliate));
        dialog.setBtnOk(getString(R.string.text_full_affiliate_ok));
        dialog.setBtnCancel(getString(R.string.text_full_affiliate_no));
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onErrorCheckQuota(String error, String productId, String adId) {
        isCanDoAction = true;
        showError(error, (view) -> presenter.checkAffiliateQuota(productId, adId));
    }

    @Override
    public void onSuccessGetAutoComplete(List<AutoCompleteViewModel> modelList) {
        if (autoCompleteLayout.getVisibility() == View.GONE)
            autoCompleteLayout.setVisibility(View.VISIBLE);
        searchView.setDelayTextChanged(TIME_DEBOUNCE_MILIS);
        rvAutoComplete.setLayoutManager(new LinearLayoutManager(getActivity()));
        autoCompleteAdapter = new AutoCompleteSearchAdapter(this, modelList);
        rvAutoComplete.setAdapter(autoCompleteAdapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAutoCompleteItemClicked(String keyword) {
        clearAutoCompleteAdapter(keyword);
        onSearchSubmitted(keyword);
        autoCompleteLayout.setVisibility(View.GONE);
    }

    @Override
    public void onAutoCompleteIconClicked(String keyword) {
        clearAutoCompleteAdapter(keyword);
    }

    @Override
    public void stopTrace() {
        if (performanceMonitoring != null && !isTraceStopped) {
            performanceMonitoring.stopTrace();
            isTraceStopped = true;
        }
    }

    @Override
    public void unsubscribeAutoComplete() {
        presenter.unsubscribeAutoComplete();
    }

    @Override
    public boolean shouldBackPressed() {
        if (adapter == null) {
            return true;
        }

        for (Visitable visitable : adapter.getData()) {
            if (visitable instanceof ExploreEmptySearchViewModel) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void refresh() {
        onButtonEmptySearchClicked();
    }

    @Override
    public void onItemClicked(@NotNull List<FilterViewModel> filters,
                              @NotNull FilterViewModel filterViewModel) {
        getFilteredFirstData(filters);
        affiliateAnalytics.onQuickFilterClicked(filterViewModel.getName());
    }

    private void clearAutoCompleteAdapter(String keyword) {
        searchView.setDelayTextChanged(0);
        searchView.getSearchTextView().setText(keyword);
        onSearchTextModified(keyword, true);
        autoCompleteAdapter.clearAdapter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_DETAIL_FILTER) {
                List<FilterViewModel> currentFilter = new ArrayList<>(data.getParcelableArrayListExtra(FilterActivity.PARAM_FILTER_LIST));
                populateFilter(currentFilter);
                getFilteredFirstData(getOnlySelectedFilter(currentFilter));
            } else if (requestCode == REQUEST_DETAIL_SORT) {
                SortViewModel selectedSort =
                        data.getParcelableExtra(SortActivity.PARAM_SORT_SELECTED);
                getSortedData(selectedSort);
            } else if (requestCode == LOGIN_CODE) {
                initProfileSection();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isCanDoAction = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void trackImpression(List<Visitable<?>> visitables) {
        for(int i = 0; i < visitables.size(); i++) {
            Visitable visitable = visitables.get(i);
            int position = adapter.getData().size() + i;

            if (visitable instanceof ExploreProductViewModel) {
                ExploreProductViewModel model = (ExploreProductViewModel) visitable;
                trackProductImpression(model.getExploreCardViewModel(), position);
            } else if (visitable instanceof RecommendationViewModel) {
                RecommendationViewModel model = (RecommendationViewModel) visitable;
                for (ExploreCardViewModel card : model.getCards()) {
                    trackProductImpression(card, position);
                }
            }
        }
    }

    private void trackProductImpression(ExploreCardViewModel card, int position) {
        affiliateAnalytics.onProductImpression(
                card.getTitle(),
                card.getProductId(),
                card.getCommissionValue(),
                card.getSectionName(),
                position
        );
    }

    private void trackProductClick(ExploreCardViewModel card, int position) {
        affiliateAnalytics.onProductClicked(
                card.getTitle(),
                card.getProductId(),
                card.getCommissionValue(),
                card.getSectionName(),
                position
        );
    }

    private void showError(String message) {
        showError(message, null);
    }

    private void showError(String message, View.OnClickListener listener) {
        ToasterError.make(getView(), message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show();
    }

    private void goToEducation() {
        if (getContext() != null) {
            startActivity(AffiliateEducationActivity.Companion.createIntent(getContext()));
            affiliatePreference.setFirstTimeEducation(userSession.getUserId());
        }
    }

    private void showShowCase() {
        ShowCaseDialog showCaseDialog = createShowCase();

        ArrayList<ShowCaseObject> showcases = new ArrayList<>();
        showcases.add(new ShowCaseObject(
                layoutProfile,
                getString(R.string.title_showcase),
                getString(R.string.desc_showcase),
                ShowCaseContentPosition.UNDEFINED));

        showCaseDialog.show(getActivity(), TAG_SHOWCASE, showcases);
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .backgroundContentColorRes(R.color.black)
                .shadowColorRes(R.color.shadow)
                .titleTextColorRes(R.color.white)
                .textColorRes(R.color.grey_400)
                .textSizeRes(R.dimen.sp_12)
                .titleTextSizeRes(R.dimen.sp_16)
                .nextStringRes(R.string.next)
                .prevStringRes(R.string.previous)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }
}
