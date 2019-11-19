package com.tokopedia.imagesearch.search.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
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
import com.tokopedia.imagesearch.search.fragment.product.ImageProductListAdapter;
import com.tokopedia.imagesearch.search.fragment.product.ImageProductListFragmentView;
import com.tokopedia.imagesearch.search.fragment.product.ImageProductListPresenter;
import com.tokopedia.imagesearch.search.fragment.product.adapter.decoration.ProductItemDecoration;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.CategoryFilterListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.RedirectionListener;
import com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactory;
import com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactoryImpl;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.LANDSCAPE_COLUMN_MAIN;
import static com.tokopedia.discovery.common.constants.SearchConstant.PORTRAIT_COLUMN_MAIN;
import static com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WIHSLIST_STATUS_IS_WISHLIST;
import static com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchProductListFragment extends BaseDaggerFragment implements
        ImageProductListFragmentView,
        ProductListener, WishListActionListener, CategoryFilterListener {

    public static final String SCREEN_IMAGE_SEARCH_TAB = "Image Search result - Image tab";
    public static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;

    private static final String EXTRA_PRODUCT_LIST = "EXTRA_PRODUCT_LIST";
    private static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    private static final String EXTRA_SPAN_COUNT = "EXTRA_SPAN_COUNT";

    protected RecyclerView recyclerView;
    @Inject
    ImageProductListPresenter presenter;
    @Inject
    UserSessionInterface userSession;

    private Config topAdsConfig;
    private ImageProductListAdapter adapter;
    private ProductViewModel productViewModel;
    private ImageProductListTypeFactory imageProductListTypeFactory;
    private SearchParameter searchParameter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private EndlessRecyclerViewScrollListener staggeredGridLayoutLoadMoreTriggerListener;
    private RedirectionListener redirectionListener;

    public int spanCount;
    private TrackingQueue trackingQueue;
    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";


    public static ImageSearchProductListFragment newInstance(ProductViewModel productViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
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
        trackingQueue = new TrackingQueue(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RedirectionListener) {
            this.redirectionListener = (RedirectionListener) context;
        }
    }

    private void loadDataFromSavedState(Bundle savedInstanceState) {
        productViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_LIST);
        setSearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
    }

    private void loadDataFromArguments() {
        productViewModel = getArguments().getParcelable(ARG_VIEW_MODEL);
        if (productViewModel != null) {

            if (productViewModel.getSearchParameter() != null)
                setSearchParameter(productViewModel.getSearchParameter());
        }
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
        initTopAdsConfig();
        setupAdapter();
        setupListener();
    }

    private void initTopAdsConfig() {
        topAdsConfig = new Config.Builder()
                .setSessionId(userSession.getDeviceId())
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_LIST, productViewModel);
        outState.putParcelable(EXTRA_SEARCH_PARAMETER, getSearchParameter());
        outState.putInt(EXTRA_SPAN_COUNT, getSpanCount());
    }

    private void setupAdapter() {
        imageProductListTypeFactory = new ImageProductListTypeFactoryImpl(this, this, getQueryKey());
        adapter = new ImageProductListAdapter(imageProductListTypeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ProductItemDecoration(getContext().getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)));
        recyclerView.setLayoutManager(getStaggeredGridLayoutManager());

        presenter.initData(initMappingProduct(), productViewModel.getCategoryFilterModel());
        presenter.loadMoreData(0);
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

    private List<Visitable> initMappingProduct() {
        List<Visitable> list = new ArrayList<>();
        list.addAll(productViewModel.getProductList());
        return list;
    }

    protected StaggeredGridLayoutManager getStaggeredGridLayoutManager() {
        return staggeredGridLayoutManager;
    }

    private void bindView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.image_search_recyclerview);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
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
            boolean isWishlist = data.getExtras().getBoolean(WIHSLIST_STATUS_IS_WISHLIST, false);

            updateWishlistFromPDP(position, isWishlist);
        }
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

    public void reloadData() {
        adapter.clearAllElements();
        staggeredGridLayoutLoadMoreTriggerListener.resetState();
        presenter.loadMoreData(0);
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
            ImageSearchTracking.eventImpressionImageSearchResultProduct(getActivity(), dataLayerList);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TopAdsGtmTracker.getInstance().eventSearchResultProductView(trackingQueue, getQueryKey(), SCREEN_IMAGE_SEARCH_TAB);
        trackingQueue.sendAll();
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
    public String getQueryKey() {
        return productViewModel.getQuery();
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
    public void hideRefreshLayout() {
    }

    @Override
    public void onEmptyButtonClicked() {
        ImageSearchTracking.eventUserClickNewSearchOnEmptySearch(getContext(), getScreenName());
        redirectionListener.moveToAutoCompletePage();
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(com.tokopedia.abstraction.R.string.default_request_error_unknown));
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        ImageSearchTracking.eventSearchResultProductWishlistClick(true, getQueryKey(), userSession.getUserId());
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
        ImageSearchTracking.eventSearchResultProductWishlistClick(false, getQueryKey(), userSession.getUserId());
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
        ImageSearchTracking.trackEventProductLongPress(getQueryKey(), item.getProductID());
        startSimilarSearch(item.getProductID());
    }

    public void startSimilarSearch(String productId) {
        RouteManager.route(getContext(), ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, productId);
    }

    public void onWishlistButtonClicked(ProductItem productItem) {
        presenter.handleWishlistButtonClicked(productItem);
    }

    private void sendItemClickTrackingEvent(ProductItem item) {
        ImageSearchTracking.trackEventClickImageSearchResultProduct(
                item.getProductAsObjectDataLayerForImageSearchClick()
        );
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
}
