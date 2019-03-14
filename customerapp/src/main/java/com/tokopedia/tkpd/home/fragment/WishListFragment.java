package com.tokopedia.tkpd.home.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.home.wishlist.domain.model.GqlWishListDataResponse;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.WishListProductAdapter;
import com.tokopedia.tkpd.home.feed.data.source.cloud.AddFavoriteShopService;
import com.tokopedia.tkpd.home.presenter.WishList;
import com.tokopedia.tkpd.home.presenter.WishListImpl;
import com.tokopedia.tkpd.home.presenter.WishListView;
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsAddToCart;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.design.utils.CurrencyFormatHelper.convertRupiahToInt;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public class WishListFragment extends TkpdBaseV4Fragment implements WishListView,
        SearchView.OnQueryTextListener, TopAdsItemClickListener,
        WishListProductAdapter.OnWishlistActionButtonClicked {

    private static final String SHOP_TYPE_OFFICIAL_STORE = "official_store";
    private static final String SHOP_TYPE_GOLD_MERCHANT = "gold_merchant";
    private static final String SHOP_TYPE_REGULER = "reguler";

    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

    public static final String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;
    public static final String FRAGMENT_TAG = "WishListFragment";
    private CheckoutAnalyticsAddToCart checkoutAnalyticsAddToCart;

    private WishlistAnalytics wishlistAnalytics;

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
        wishList = new WishListImpl(getActivity(), this);
        checkoutAnalyticsAddToCart = new CheckoutAnalyticsAddToCart(getAnalyticTracker());
        wishlistAnalytics = new WishlistAnalytics(getAnalyticTracker());
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
        Intent intent = getProductIntent(product.getId());
        getActivity().startActivity(intent);
    }

    private Intent getProductIntent(String productId){
        if (getActivity() != null) {
            return RouteManager.getIntentInternal(getActivity(),
                    UriUtil.buildUri(ApplinkConstInternal.Marketplace.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
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
        trackingAppshortcut();
        return parentView;
    }

    private void initView(View view) {
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        searchEditText = view.findViewById(R.id.wishlist_search_edittext);
    }

    private void trackingAppshortcut() {
        boolean isFromAppShortcut = getArguments() != null && getArguments().getBoolean(FROM_APP_SHORTCUTS);
        if (isFromAppShortcut) {
            wishlistAnalytics.eventWishlistShortcut();
        }
    }

    private AnalyticTracker getAnalyticTracker() {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
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
        Object object = DataLayer.mapOf(
                "name", dataDetail.getName(),
                "id", dataDetail.getId(),
                "price", convertRupiahToInt(String.valueOf(dataDetail.getPrice())),
                "brand", DEFAULT_VALUE_NONE_OTHER,
                "category", DEFAULT_VALUE_NONE_OTHER,
                "variant", DEFAULT_VALUE_NONE_OTHER,
                "quantity", dataDetail.getMinimumOrder(),
                "shopId", dataDetail.getShop().getId(),
                "shopType", generateShopType(dataDetail.getShop()),
                "shopName", dataDetail.getShop().getName(),
                "picture", dataDetail.getImageUrl(),
                "url", dataDetail.getUrl(),
                "categoryId", DEFAULT_VALUE_NONE_OTHER,
                "cartId", addToCartResult.getCartId(),
                "dimension38", DEFAULT_VALUE_NONE_OTHER
        );
        wishlistAnalytics.trackEventAddToCardProductWishlist(object);
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
    public void setSearchNotFound(String query) {
        adapter.setSearchNotFound(query);
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
                    wishlistAnalytics.eventRemoveWishlist();
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
        if (getActivity() != null  && getView() != null) {
            ToasterNormal.make(getView(), getActivity().getString(R.string.msg_delete_wishlist_success), BaseToaster.LENGTH_SHORT).show();
            displayPull(false);

            if (adapter != null) {
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        }
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
    public void showAddToCartErrorMessage(String message) {
        if (getActivity() != null && getView() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.default_request_error_unknown_short);
            }
            ToasterError.make(getView(),
                    message.replace("\n", " "), BaseToaster.LENGTH_LONG).show();
        }
    }

    @Override
    public void showAddToCartMessage(String message) {
        if (getActivity() != null && getView() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.default_request_error_unknown_short);
            }
            ToasterNormal.make(getView(),
                    message.replace("\n", " "), BaseToaster.LENGTH_LONG).setAction(getString(R.string.wishlist_check_cart),v -> {
                            if (getActivity() != null && getActivity().getApplication() != null) {
                                wishlistAnalytics.eventClickCartWishlist();
                                getActivity().startActivity(((PdpRouter) getActivity().getApplication())
                                        .getCartIntent(getActivity()));
                            }
                    }).show();
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
        adapter = new WishListProductAdapter(getActivity(), data, wishlistAnalytics);
        adapter.setWishlistView(this);
        adapter.setActionButtonClicked(this);
    }

    @Override
    public void showAllWishlist() {
        clearSearchResult();
    }

    @Override
    public void findProduct() {
        wishlistAnalytics.eventClickCariEmptyWishlist();
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
                if (position == wishList.getData().size()) {
                    return 2;
                } else if(position % 5 == 0 && adapter.getItemViewType(position) == TkpdState.RecyclerViewItem.TYPE_LIST
                        || adapter.getItemViewType(position) == TkpdState.RecyclerView.VIEW_EMPTY_SEARCH
                        || adapter.getItemViewType(position)  == TkpdState.RecyclerView.VIEW_EMPTY_STATE
                        || adapter.getItemViewType(position)  == TkpdState.RecyclerView.VIEW_TOP_ADS_LIST) {
                    return 2;
                } else {
                    return 1;
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
        wishlistAnalytics.eventClickCariWishlist(query);
        wishList.searchWishlist(query);
        sendSearchGTM(query);
        return false;
    }

    private void sendSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            wishlistAnalytics.eventSearchWishlist(keyword);
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

    @Override
    public void sendWishlistImpressionAnalysis(GqlWishListDataResponse.GqlWishList wishListData, int currentSize) {
        wishlistAnalytics.trackEventImpressionOnProductWishlist(getProductAsObjectDataLayerForWishlistImpression(wishListData.getWishlistDataList(), currentSize));
    }

    public List<Object> getProductAsObjectDataLayerForWishlistImpression(List<Wishlist> wishlistDataList, int currentSize) {
        int position = currentSize+1;
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i<wishlistDataList.size() ; i++){
            Wishlist wishlist = wishlistDataList.get(i);
            objects.add(DataLayer.mapOf(
                    "name", wishlist.getName(),
                    "id", wishlist.getId(),
                    "price", Integer.toString(convertRupiahToInt(String.valueOf(wishlist.getPrice()))),
                    "brand", DEFAULT_VALUE_NONE_OTHER,
                    "category", DEFAULT_VALUE_NONE_OTHER,
                    "variant", DEFAULT_VALUE_NONE_OTHER,
                    "list", "/wishlist",
                    "position", Integer.toString(position++)
            ));
        }
        return objects;
    }

}
