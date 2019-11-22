package com.tokopedia.imagesearch.search.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.common.data.Sort;
import com.tokopedia.filter.common.manager.FilterSortManager;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.helper.SortHelper;
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener;
import com.tokopedia.imagesearch.analytics.ImageSearchEventTracking;
import com.tokopedia.imagesearch.di.component.DaggerImageSearchComponent;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.analytics.ImageSearchTracking;
import com.tokopedia.imagesearch.di.component.ImageSearchComponent;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;
import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;
import com.tokopedia.imagesearch.helper.UrlParamUtils;
import com.tokopedia.imagesearch.search.fragment.product.ImageProductListAdapter;
import com.tokopedia.imagesearch.search.fragment.product.ImageProductListFragmentView;
import com.tokopedia.imagesearch.search.fragment.product.ImageProductListPresenter;
import com.tokopedia.imagesearch.search.fragment.product.adapter.decoration.ProductItemDecoration;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ImageSearchNavigationListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.CategoryFilterListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.RedirectionListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactory;
import com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactoryImpl;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.LANDSCAPE_COLUMN_MAIN;
import static com.tokopedia.discovery.common.constants.SearchConstant.PORTRAIT_COLUMN_MAIN;
import static com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_IS_WISHLIST;
import static com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchProductListFragment extends BaseDaggerFragment implements
        ImageProductListFragmentView,
        ProductListener, WishListActionListener, CategoryFilterListener {

    private static final String NO_RESPONSE = "no response";
    private static final String SUCCESS = "success match found";

    public static final String SCREEN_IMAGE_SEARCH_TAB = "Image Search result - Image tab";
    public static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;

    private static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    private static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";
    private static final String EXTRA_IS_FROM_CAMERA = "EXTRA_IS_FROM_CAMERA";

    protected RecyclerView recyclerView;
    @Inject
    ImageProductListPresenter presenter;
    @Inject
    UserSessionInterface userSession;

    private ImageProductListAdapter adapter;
    private ImageProductListTypeFactory imageProductListTypeFactory;
    private SearchParameter searchParameter = new SearchParameter();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private EndlessRecyclerViewScrollListener staggeredGridLayoutLoadMoreTriggerListener;
    private RedirectionListener redirectionListener;
    private SwipeRefreshLayout refreshLayout;

    public int spanCount;
    private ImageSearchNavigationListener imageSearchNavigationListener;
    private BottomSheetListener bottomSheetListener;
    private FilterTrackingData filterTrackingData;
    private ArrayList<Sort> sort;
    private ArrayList<Filter> filters;
    private HashMap<String, String> selectedSort;
    private String imagePath;
    private boolean isFromCamera;
    private String queryKey = "";


    public static ImageSearchProductListFragment newInstance(String imagePath, boolean isFromCamera) {
        Bundle args = new Bundle();
        args.putString(EXTRA_IMAGE_PATH, imagePath);
        args.putBoolean(EXTRA_IS_FROM_CAMERA, isFromCamera);
        ImageSearchProductListFragment imageSearchProductListFragment = new ImageSearchProductListFragment();
        imageSearchProductListFragment.setArguments(args);
        return imageSearchProductListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState);
        } else {
            loadDataFromArguments();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RedirectionListener) {
            this.redirectionListener = (RedirectionListener) context;
        }
        if (context instanceof ImageSearchNavigationListener) {
            this.imageSearchNavigationListener = (ImageSearchNavigationListener) context;
        }
        if (context instanceof BottomSheetListener) {
            this.bottomSheetListener = (BottomSheetListener) context;
        }
    }

    private void loadDataFromSavedState(Bundle savedInstanceState) {
        setSearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
        imagePath = savedInstanceState.getString(EXTRA_IMAGE_PATH);
        isFromCamera = savedInstanceState.getBoolean(EXTRA_IS_FROM_CAMERA);
    }

    private void loadDataFromArguments() {
        imagePath = getArguments().getString(EXTRA_IMAGE_PATH);
        isFromCamera = getArguments().getBoolean(EXTRA_IS_FROM_CAMERA);
    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }

    @Override
    protected void initInjector() {
        ImageSearchComponent component = DaggerImageSearchComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public BaseAppComponent getBaseAppComponent() {
        if(getActivity() == null || getActivity().getApplication() == null) return null;

        return ((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent();
    }

    public String getScreenNameId() {
        return SCREEN_IMAGE_SEARCH_TAB;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this, this);
        return inflater.inflate(R.layout.fragment_image_search, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpan();
        bindView(view);
        setupAdapter();
        setupListener();
        setupSearchNavigation();
        initSwipeToRefresh(view);
        performImageSearch();
    }

    private void performImageSearch() {
        presenter.requestImageSearch(imagePath);
    }

    protected void setupSearchNavigation() {
        imageSearchNavigationListener
                .setupSearchNavigation(new ImageSearchNavigationListener.ClickListener() {
                    @Override
                    public void onFilterClick() {
                        openFilterPage();
                    }

                    @Override
                    public void onSortClick() {
                        openSortActivity();
                    }
                });
    }

    private void openFilterPage() {
        if (!isFilterDataAvailable() && getActivity() != null) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_filter_data_not_ready));
            return;
        }

        if (bottomSheetListener != null) {
            openBottomSheetFilter();
        }
    }

    private boolean isFilterDataAvailable() {
        return filters != null && !filters.isEmpty();
    }

    private void openBottomSheetFilter() {
        if(searchParameter == null || getFilters() == null) return;

        FilterTracking.eventOpenFilterPage(getFilterTrackingData());

        bottomSheetListener.loadFilterItems(getFilters(), searchParameter.getSearchParameterHashMap());
        bottomSheetListener.launchFilterBottomSheet();
    }

    private FilterTrackingData getFilterTrackingData() {
        if (filterTrackingData == null) {
            filterTrackingData = new FilterTrackingData(FilterEventTracking.Event.CLICK_IMAGE_SEARCH_RESULT,
                    FilterEventTracking.Category.FILTER_PRODUCT,
                    "",
                    FilterEventTracking.Category.PREFIX_IMAGE_SEARCH_RESULT_PAGE
            );
        }
        return filterTrackingData;
    }

    protected void openSortActivity() {
        if(getActivity() == null) return;

        if (!FilterSortManager.openSortActivity(this, sort, getSelectedSort())) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
    }

    private void handleSortResult(Map<String, String> selectedSort, String selectedSortName, String autoApplyFilter) {
        setSelectedSort(new HashMap<>(selectedSort));
        if(searchParameter != null) {
            searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                    SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE);

            searchParameter.getSearchParameterHashMap().putAll(UrlParamUtils.getParamMap(autoApplyFilter));

            searchParameter.getSearchParameterHashMap().putAll(getSelectedSort());
        }
        clearDataFilterSort();
        refreshData();
    }

    private void setFilterData(List<Filter> filters) {
        this.filters = new ArrayList<>();
        if (filters == null) {
            return;
        }

        this.filters.addAll(filters);
    }

    protected ArrayList<Filter> getFilters() {
        return filters;
    }

    protected void setSortData(List<Sort> sorts) {
        this.sort = new ArrayList<>();
        if (sorts == null) {
            return;
        }

        this.sort.addAll(sorts);
    }

    private ArrayList<Sort> getSort() {
        return sort;
    }

    private HashMap<String, String> getSelectedSort() {
        return selectedSort;
    }

    private void setSelectedSort(HashMap<String, String> selectedSort) {
        this.selectedSort = selectedSort;
    }

    public void clearDataFilterSort() {
        if (filters != null) {
            this.filters.clear();
        }
        if (sort != null) {
            this.sort.clear();
        }
    }

    @Override
    public void renderDynamicFilter(DynamicFilterModel pojo) {
        setFilterData(pojo.getData().getFilter());
        setSortData(pojo.getData().getSort());

        if(searchParameter == null
                || getFilters() == null || getSort() == null) return;

        initSelectedSort();
    }

    private void initSelectedSort() {
        if(getSort() == null) return;

        HashMap<String, String> selectedSort = new HashMap<>(
                SortHelper.Companion.getSelectedSortFromSearchParameter(searchParameter.getSearchParameterHashMap(), getSort())
        );
        addDefaultSelectedSort(selectedSort);
        setSelectedSort(selectedSort);
    }

    private void addDefaultSelectedSort(HashMap<String, String> selectedSort) {
        if (selectedSort.isEmpty()) {
            selectedSort.put(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT);
        }
    }

    public void refreshSearchParameter(Map<String, String> queryParams) {
        if(searchParameter == null) return;

        this.searchParameter.getSearchParameterHashMap().clear();
        this.searchParameter.getSearchParameterHashMap().putAll(queryParams);
        this.searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_IMAGE_PATH, imagePath);
        outState.putBoolean(EXTRA_IS_FROM_CAMERA, isFromCamera);
        outState.putParcelable(EXTRA_SEARCH_PARAMETER, getSearchParameter());
    }

    private void setupAdapter() {
        imageProductListTypeFactory = new ImageProductListTypeFactoryImpl(this, this);
        adapter = new ImageProductListAdapter(imageProductListTypeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ProductItemDecoration(getContext().getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)));
        recyclerView.setLayoutManager(getStaggeredGridLayoutManager());
    }

    private void setupListener() {
        staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(getStaggeredGridLayoutManager());
        recyclerView.addOnScrollListener(staggeredGridLayoutLoadMoreTriggerListener);
    }

    private EndlessRecyclerViewScrollListener getEndlessRecyclerViewListener(RecyclerView.LayoutManager recyclerViewLayoutManager) {
        return new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isAllowLoadMore()) {
                    presenter.loadMoreData(page - 1);
                } else {
                    adapter.hideLoading();
                }
            }
        };
    }

    private boolean isAllowLoadMore() {
        return adapter.isLoading();
    }

    protected StaggeredGridLayoutManager getStaggeredGridLayoutManager() {
        return staggeredGridLayoutManager;
    }

    private void bindView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.image_search_recyclerview);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    }

    private void initSwipeToRefresh(View view) {
        refreshLayout = view.findViewById(R.id.image_search_swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    public void refreshData() {
        adapter.clearAllElements();
        staggeredGridLayoutLoadMoreTriggerListener.resetState();
        presenter.refreshData();
    }

    protected ImageProductListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOTO_PRODUCT_DETAIL
                && data != null
                && data.getExtras() != null
                && data.getExtras().getInt(WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            int position = data.getExtras().getInt(WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist = data.getExtras().getBoolean(WISHLIST_STATUS_IS_WISHLIST, false);

            updateWishlistFromPDP(position, isWishlist);
        }

        FilterSortManager.handleOnActivityResult(requestCode, resultCode, data, new FilterSortManager.Callback() {
            @Override
            public void onFilterResult(Map<String, String> queryParams, Map<String, String> selectedFilters,
                                       List<Option> selectedOptions) {
                //not handling this, because using bottom sheet
            }

            @Override
            public void onSortResult(Map<String, String> selectedSort, String selectedSortName, String autoApplyFilter) {
                handleSortResult(selectedSort, selectedSortName, autoApplyFilter);
            }
        });
    }

    private void updateWishlistFromPDP(int position, boolean isWishlist) {
        if (adapter != null && adapter.isProductItem(position)) {
            adapter.updateWishlistStatus(position, isWishlist);
        }
    }

    private void initSpan() {
        setSpanCount(calcColumnSize(getResources().getConfiguration().orientation));
    }

    public static int calcColumnSize(int orientation) {
        int defaultColumnNumber = 1;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    private void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    private int getSpanCount() {
        return spanCount;
    }

    @Override
    public void launchLoginActivity(Bundle extras) {
        if (getActivity() == null) return;

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public boolean isUserHasLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    @Override
    public void onLoadMoreEmpty() {
        adapter.hideLoading();
    }

    @Override
    public void appendProductList(List<Visitable> list, boolean hasNextPage) {
        sendImageTrackingData(list);
        adapter.hideLoading();
        adapter.addMoreData(list);

        if (hasNextPage) {
            adapter.showLoading();
        }
        staggeredGridLayoutLoadMoreTriggerListener.updateStateAfterGetData();
    }

    @Override
    public void reloadData() {
        adapter.clearAllElements();
        staggeredGridLayoutLoadMoreTriggerListener.resetState();
        presenter.loadMoreData(0);
    }

    @Override
    public void displayErrorRefresh() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(com.tokopedia.abstraction.R.string.default_request_error_unknown));
    }

    private void sendImageTrackingData(List<Visitable> list) {
        if (list != null && list.size() > 0) {
            List<Object> dataLayerList = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j) instanceof ProductItem) {
                    ProductItem productItem = (ProductItem)list.get(j);
                    dataLayerList.add(productItem.getProductAsObjectDataLayerForImageSearchImpression());
                }
            }
            ImageSearchTracking.eventImpressionImageSearchResultProduct(
                    dataLayerList, getToken());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void disableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, false);
    }

    @Override
    public void enableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, true);
    }

    @Override
    public void setQueryKey(String query) {
        this.queryKey = query;
    }

    private String getQueryKey() {
        return queryKey;
    }

    @Override
    public SearchParameter getSearchParameter() {
        return searchParameter;
    }

    @Override
    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    @Override
    public void backToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void showRefreshLayout() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(com.tokopedia.abstraction.R.string.default_request_error_unknown));
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        ImageSearchTracking.eventSearchResultProductWishlistClick(true, getQueryKey(), userSession.getUserId(), getToken());
        adapter.updateWishlistStatus(productId, true);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.image_search_msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(com.tokopedia.abstraction.R.string.default_request_error_unknown));
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        ImageSearchTracking.eventSearchResultProductWishlistClick(false, getQueryKey(), userSession.getUserId(), getToken());
        adapter.updateWishlistStatus(productId, false);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.image_search_msg_remove_wishlist));
    }

    private void moveToProductDetailPage(int adapterPosition, String productId) {
        Intent intent = getProductIntent(productId);

        if(intent != null) {
            intent.putExtra(WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
            startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
        }
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onItemClicked(ProductItem item, int adapterPosition) {
        sendItemClickTrackingEvent(item);
        moveToProductDetailPage(adapterPosition, item.getProductID());
    }

    @Override
    public void onProductImpressed(ProductItem item, int adapterPosition) {

    }

    public void onLongClick(ProductItem item, int adapterPosition) {
        ImageSearchTracking.trackEventProductLongPress(getQueryKey(), item.getProductID(), getToken());
        startSimilarSearch(item.getProductID());
    }

    private void startSimilarSearch(String productId) {
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, productId);
        intent.putExtra(SearchConstant.SimilarSearch.QUERY, getQueryKey());

        startActivity(intent);
    }

    public void onWishlistButtonClicked(ProductItem productItem) {
        presenter.handleWishlistButtonClicked(productItem);
    }

    private void sendItemClickTrackingEvent(ProductItem item) {
        ImageSearchTracking.trackEventClickImageSearchResultProduct(
                item.getProductAsObjectDataLayerForImageSearchClick(),
                getToken()
        );
    }

    private String getToken() {
        return presenter != null ? presenter.getToken() : "";
    }

    @Override
    public boolean isCategoryFilterSelected(String categoryId) {
        return presenter.isCategoryFilterSelected(categoryId);
    }

    @Override
    public void onCategoryFilterSelected(CategoryFilterModel.Item item) {
        boolean isCategoryFilterSelectedReversed = !isCategoryFilterSelected(item.getCategoryId());
        if (isCategoryFilterSelectedReversed) {
            presenter.setFilterCategory(item.getCategoryId());
        } else {
            presenter.setFilterCategory("");
        }
        reloadData();
    }

    @Override
    public String getEmptyResultMessage() {
        return getString(R.string.image_search_msg_empty_product_title);
    }

    @Override
    public void onHandleImageResponseSearch(ProductViewModel productViewModel) {
        trackEventOnSuccessImageSearch(productViewModel);
    }

    @Override
    public void onHandleInvalidImageSearchResponse() {

        if (isFromCamera) {
            sendCameraImageSearchResultGTM(NO_RESPONSE);
        } else {
            sendGalleryImageSearchResultGTM(NO_RESPONSE);
        }

        Toast.makeText(getContext(), getString(R.string.invalid_image_search_response), Toast.LENGTH_LONG).show();
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void showImageNotSupportedError() {
        Toast.makeText(getContext(), getResources().getString(R.string.image_not_supported), Toast.LENGTH_LONG).show();
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void sendGalleryImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                ImageSearchEventTracking.Category.IMAGE_SEARCH,
                ImageSearchEventTracking.Action.GALLERY_SEARCH_RESULT,
                label);
    }


    private void sendCameraImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                ImageSearchEventTracking.Category.IMAGE_SEARCH,
                ImageSearchEventTracking.Action.CAMERA_SEARCH_RESULT,
                label);
    }

    private void trackEventOnSuccessImageSearch(ProductViewModel productViewModel) {
        if (productViewModel.getProductList() == null || productViewModel.getProductList().size() == 0) {
            return;
        }
        sendGTMEventSuccessImageSearch();
        sendAppsFlyerEventSuccessImageSearch(productViewModel);
    }

    private void sendGTMEventSuccessImageSearch() {
        if (isFromCamera) {
            sendCameraImageSearchResultGTM(SUCCESS);
        } else {
            sendGalleryImageSearchResultGTM(SUCCESS);
        }
    }

    private void sendAppsFlyerEventSuccessImageSearch(ProductViewModel productViewModel) {
        JSONArray afProdIds = new JSONArray();
        HashMap<String, String> category = new HashMap<String, String>();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                } else {
                    break;
                }
                category.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());
            }
        }

        eventAppsFlyerViewListingSearch(afProdIds,productViewModel.getQuery(),prodIdArray);
        sendMoEngageSearchAttemptSuccessImageSearch(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);
    }

    private void eventAppsFlyerViewListingSearch(JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put("af_content_id", prodIds);
        listViewEvent.put("af_currency", "IDR");
        listViewEvent.put("af_content_type", "product");
        listViewEvent.put("af_search_string", keyword);
        if (productsId.length() > 0) {
            listViewEvent.put("af_success", "success");
        } else {
            listViewEvent.put("af_success", "fail");
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent("af_search", listViewEvent);
    }

    private void sendMoEngageSearchAttemptSuccessImageSearch(String keyword, boolean isResultFound, HashMap<String, String> category) {
        Map<String, Object> value = DataLayer.mapOf(
                ImageSearchEventTracking.MOENGAGE.KEYWORD, keyword,
                ImageSearchEventTracking.MOENGAGE.IS_RESULT_FOUND, isResultFound
        );
        if (category != null) {
            value.put(ImageSearchEventTracking.MOENGAGE.CATEGORY_ID_MAPPING, new JSONArray(Arrays.asList(category.keySet().toArray())));
            value.put(ImageSearchEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING, new JSONArray((category.values())));
        }
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, ImageSearchEventTracking.EventMoEngage.SEARCH_ATTEMPT);
    }

    @Override
    public void setTotalSearchResultCount(String formattedResultCount) {
        if (bottomSheetListener != null) {
            bottomSheetListener.setFilterResultCount(formattedResultCount);
        }
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDestroy();
    }
}
