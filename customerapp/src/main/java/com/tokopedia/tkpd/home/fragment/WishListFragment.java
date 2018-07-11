package com.tokopedia.tkpd.home.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartResult;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.WishListProductAdapter;
import com.tokopedia.tkpd.home.feed.data.source.cloud.AddFavoriteShopService;
import com.tokopedia.tkpd.home.presenter.WishList;
import com.tokopedia.tkpd.home.presenter.WishListImpl;
import com.tokopedia.tkpd.home.presenter.WishListView;
import com.tokopedia.tkpd.home.wishlist.domain.SearchWishlistUsecase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsAddToCart;
import com.tokopedia.transactionanalytics.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.EnhancedECommerceProductCartMapData;

import java.util.List;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public class WishListFragment extends TkpdBaseV4Fragment implements WishListView,
        SearchView.OnQueryTextListener, TopAdsItemClickListener,
        WishListProductAdapter.OnWishlistActionButtonClicked {

    private static final String SHOP_TYPE_OFFICIAL_STORE = "official_store";
    private static final String SHOP_TYPE_GOLD_MERCHANT = "gold_merchant";
    private static final String SHOP_TYPE_REGULER = "reguler";

    public static final String FRAGMENT_TAG = "WishListFragment";
    private CheckoutAnalyticsAddToCart checkoutAnalyticsAddToCart;

    public WishListFragment() {
    }

    public static final Fragment newInstance() {
        return new WishListFragment();
    }

    private SwipeToRefresh swipeToRefresh;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchView searchEditText;

    GridLayoutManager layoutManager;
    WishListProductAdapter adapter;
    TkpdProgressDialog progressDialog;
    Boolean isDeleteDialogShown;
    Boolean isLoadingMore = false;

    WishList wishList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wishList = new WishListImpl(this, new SearchWishlistUsecase());
        progressDialog = new TkpdProgressDialog(getContext(), TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);
        wishList.fetchSavedsInstance(savedInstanceState);
        wishList.initDataInstance(getActivity());
        isDeleteDialogShown = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shop.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data data) {
        Shop shop = data.getShop();
        Intent intent = new Intent(getActivity(), AddFavoriteShopService.class);
        intent.putExtra(
                AddFavoriteShopService.EXTRAS_SESSION_ID, SessionHandler.getLoginID(getContext()));
        intent.putExtra(AddFavoriteShopService.EXTRAS_SHOP_ID, shop.getId());
        intent.putExtra(AddFavoriteShopService.EXTRAS_AD_KEY, data.getAdRefKey());
        getActivity().startService(intent);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_FRAGMENT_WISHLIST;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_wishlist, container, false);
        initView(parentView);
        wishList.subscribe();
        wishList.initAnalyticsHandler(getActivity());
        prepareView();
        setListener();
        loadWishlistData();
        return parentView;
    }

    private void initView(View view) {
        checkoutAnalyticsAddToCart = new CheckoutAnalyticsAddToCart(getAnalyticTracker());
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        searchEditText = view.findViewById(R.id.wishlist_search_edittext);
    }

    private AnalyticTracker getAnalyticTracker() {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            return ((AbstractionRouter) getActivity().getApplication()).getAnalyticTracker();
        }
        return null;
    }

    private void loadWishlistData() {
        if (searchEditText.getQuery().length() > 0) {
            wishList.refreshDataOnSearch(searchEditText.getQuery());
        } else {
            wishList.fetchDataFromInternet(getContext());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wishList.saveDataBeforeRotate(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wishList.unSubscribe();
    }

    @Override
    public void setListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading() && layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    wishList.loadMore(getActivity());
                }
            }
        });
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (searchEditText.getQuery().length() > 0) {
                    wishList.refreshDataOnSearch(searchEditText.getQuery());
                } else {
                    wishList.refreshData(getActivity());
                }
            }
        });
        searchEditText.findViewById(R.id.search_close_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSearchResult();
                    }
                });
    }

    public void clearSearchResult() {
        searchEditText.setQuery("", false);
        searchEditText.setIconified(true);
        wishList.fetchDataAfterClearSearch(getActivity());
    }

    @Override
    public void clearSearchView() {
        searchEditText.setQuery("", false);
        searchEditText.clearFocus();
    }

    @Override
    public void sendAddToCartAnalytics(Wishlist dataDetail, AddToCartResult addToCartResult) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductName(dataDetail.getName());
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(dataDetail.getId()));
        enhancedECommerceProductCartMapData.setPrice(String.valueOf(dataDetail.getPrice()));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCartId(addToCartResult.getCartId());
        enhancedECommerceProductCartMapData.setCategory(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setQty(String.valueOf(dataDetail.getMinimumOrder()));
        enhancedECommerceProductCartMapData.setShopId(dataDetail.getShop().getId());
        enhancedECommerceProductCartMapData.setShopType(generateShopType(dataDetail.getShop()));
        enhancedECommerceProductCartMapData.setShopName(dataDetail.getShop().getName());
        enhancedECommerceProductCartMapData.setCategoryId(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setDimension38(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setAttribution(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setDimension40(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setListName(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();
        enhancedECommerceCartMapData.addProduct(enhancedECommerceProductCartMapData.getProduct());
        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        enhancedECommerceCartMapData.setAction(EnhancedECommerceCartMapData.ADD_ACTION);
        checkoutAnalyticsAddToCart.enhancedECommerceAddToCart(
                enhancedECommerceCartMapData.getCartMap(), dataDetail.getName());
    }

    private String generateShopType(com.tokopedia.core.network.entity.wishlist.Shop shop) {
        if (shop.isOfficial())
            return SHOP_TYPE_OFFICIAL_STORE;
        else if (shop.isGoldMerchant())
            return SHOP_TYPE_GOLD_MERCHANT;
        else return SHOP_TYPE_REGULER;
    }

    @Override
    public void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        recyclerView.setHasFixedSize(false);
        searchEditText.setOnQueryTextListener(this);
        searchEditText.setSuggestionsAdapter(null);
        searchEditText.setFocusable(false);
        searchEditText.clearFocus();
        searchEditText.requestFocusFromTouch();
        TextView searchText = (TextView) searchEditText.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.black_38));
        setAdapter();
    }

    @Override
    public void setAdapter() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayContentList(boolean isShow) {
        if (isShow)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setSearchNotFound() {
        adapter.setSearchNotFound();
    }

    @Override
    public void displayLoading(boolean isShow) {
        if (isShow)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayDeleteWishlistDialog(final String productId, final int position) {
        if (!isDeleteDialogShown) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.msg_delete_wishlist_confirmation);
            builder.setPositiveButton(R.string.title_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UnifyTracking.eventRemoveWishlist();
                    wishList.deleteWishlist(getActivity(), productId, position);
                    isDeleteDialogShown = false;
                }
            });
            builder.setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isDeleteDialogShown = false;

                }
            });
            dialog.show();
            isDeleteDialogShown = true;
        }
    }

    @Override
    public void displayAddToCart(String productId) {
        wishList.addToCart(getActivity(), productId);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessDeleteWishlist(String searchTerm, int position) {
        SnackbarManager.make(getActivity(),
                MainApplication.getAppContext().getString(R.string.msg_delete_wishlist_success),
                Snackbar.LENGTH_SHORT)
                .show();
        displayPull(false);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
    }

    @Override
    public void displayErrorNetwork(Boolean isAction) {
        if (isAction) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else if (isPullToRefresh()) {
            swipeToRefresh.setRefreshing(false);
            NetworkErrorHelper.showSnackbar(getActivity());
        } else if (adapter.getData().size() > 0) {
            showLoadMoreError();
        } else {
            displayLoading(false);
            displayContentList(false);
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    getRetryListener());

        }
    }

    @Override
    public void showAddToCartMessage(String message) {
        if (getView() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.default_request_error_unknown_short);
            }
            Snackbar snackbar = Snackbar.make(getView(),
                    message.replace("\n", " "),
                    Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            TextView tv = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setMaxLines(7);
            snackbar.show();
        }
    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(getActivity());
    }

    private void showLoadMoreError() {
        isLoadingMore = true;
        NetworkErrorHelper.createSnackbarWithAction(
                getActivity(), getRetryListener()
        ).showRetrySnackbar();
    }

    @Override
    public void initGridLayoutManager() {
        layoutManager = new GridLayoutManager(getActivity(), calcColumnSize());
        layoutManager.setSpanSizeLookup(onSpanSizeLookup());
    }

    @Override
    public void initItemDecoration() {
//        itemDecorator = new ItemDecorator(getActivity(), R.dimen.item_offset);
    }

    @Override
    public void initAdapterWithData(List<RecyclerViewItem> data) {
        adapter = new WishListProductAdapter(getActivity(), data);
        adapter.setWishlistView(this);
        adapter.setActionButtonClicked(this);
    }

    @Override
    public void showAllWishlist() {
        clearSearchResult();
    }

    @Override
    public void findProduct() {
        UnifyTracking.eventClickCariEmptyWishlist();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public int calcColumnSize() {
        int defaultColumnNumber = 1;
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = com.tokopedia.tkpd.home.WishList.PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = com.tokopedia.tkpd.home.WishList.LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    @Override
    public boolean isPullToRefresh() {
        if (swipeToRefresh == null)
            return false;
        return swipeToRefresh.isRefreshing();
    }

    @Override
    public boolean isLoading() {
        return adapter.getItemViewType(layoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        Log.d(TAG, WishListFragment.class.getSimpleName() + (isLoadMore ? " tampilkan" : " hilangkan ") + " load more");
        adapter.setIsLoading(isLoadMore);
    }

    @Override
    public void displayPull(boolean isShow) {
        Log.d(TAG, WishListFragment.class.getSimpleName() + (isShow ? " tampilkan" : " hilangkan ") + " pull to refresh");
        swipeToRefresh.setRefreshing(isShow);
    }

    @Override
    public void setPullEnabled(boolean isPullEnabled) {
        Log.d(TAG, WishListFragment.class.getSimpleName() + (isPullEnabled ? " hidupkan" : " matikan") + " pull to refresh");
        swipeToRefresh.setEnabled(isPullEnabled);
    }

    @Override
    public GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                // column size default is one
                int headerColumnSize = 1,
                        footerColumnSize = 1,
                        regularColumnSize = 1;

                // check the orientation to determine landscape or portrait
                switch (getResources().getConfiguration().orientation) {
                    case Configuration.ORIENTATION_LANDSCAPE:
                        headerColumnSize = com.tokopedia.tkpd.home.WishList.LANDSCAPE_COLUMN_HEADER;
                        regularColumnSize = com.tokopedia.tkpd.home.WishList.LANDSCAPE_COLUMN;
                        footerColumnSize = com.tokopedia.tkpd.home.WishList.LANDSCAPE_COLUMN_FOOTER;
                        break;
                    case Configuration.ORIENTATION_PORTRAIT:
                        headerColumnSize = com.tokopedia.tkpd.home.WishList.PORTRAIT_COLUMN_HEADER;
                        regularColumnSize = com.tokopedia.tkpd.home.WishList.PORTRAIT_COLUMN;
                        footerColumnSize = com.tokopedia.tkpd.home.WishList.PORTRAIT_COLUMN_FOOTER;
                        break;
                }

                // set the value of footer, regular and header
                if (position == wishList.getData().size()) {
                    // header column
                    return footerColumnSize;
                } else if (position % 5 == 0 && wishList.getData().get(position).getType() == TkpdState.RecyclerViewItem.TYPE_LIST
                        || wishList.getData().get(position).getType() == TkpdState.RecyclerView.VIEW_EMPTY_SEARCH
                        || wishList.getData().get(position).getType() == TkpdState.RecyclerView.VIEW_EMPTY_STATE) {
                    // top ads span column
                    return headerColumnSize;
                } else {
                    // regular one column
                    return regularColumnSize;
                }
            }
        };
    }

    @Override
    public void loadDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setEmptyState() {
        adapter.setEmptyState();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        UnifyTracking.eventClickCariWishlist(query);
        wishList.searchWishlist(query);
        sendSearchGTM(query);
        return false;
    }

    private void sendSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventSearchWishlist(keyword);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public boolean isDestroying() {
        return isRemoving();
    }

    private NetworkErrorHelper.RetryClickedListener getRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                wishList.fetchDataFromInternet(getActivity());
                if (!isLoadingMore) {
                    displayLoading(true);
                } else {
                    isLoadingMore = false;
                }
            }
        };
    }

}
