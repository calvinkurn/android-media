package com.tokopedia.tkpd.home.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.GridLayoutProductAdapter;
import com.tokopedia.tkpd.home.presenter.WishList;
import com.tokopedia.tkpd.home.presenter.WishListImpl;
import com.tokopedia.tkpd.home.presenter.WishListView;
import com.tokopedia.tkpd.home.wishlist.domain.SearchWishlistUsecase;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public class WishListFragment extends TkpdBaseV4Fragment implements WishListView, SearchView.OnQueryTextListener {

    public static final String FRAGMENT_TAG = "WishListFragment";
    private Unbinder unbinder;

    public WishListFragment() {
    }

    public static final Fragment newInstance() {
        return new WishListFragment();
    }

    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.main_content)
    LinearLayout mainContent;
    @BindView(R.id.wishlist_search_edittext)
    SearchView searchEditText;

    GridLayoutManager layoutManager;
    GridLayoutProductAdapter adapter;
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
    protected String getScreenName() {
        return AppScreen.SCREEN_FRAGMENT_WISHLIST;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_wishlist, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        wishList.subscribe();
        wishList.initAnalyticsHandler(getActivity());
        prepareView();
        setListener();
        return parentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wishList.saveDataBeforeRotate(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        wishList.unSubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        wishList.setLocalyticFlow(getActivity(), getString(R.string.home_wishlist));
        if (wishList.isAfterRotation()) {
            if (!wishList.isLoadedFirstPage())
                wishList.refreshData(getActivity());
        } else {
            wishList.fetchDataFromCache(getActivity());
        }
        UnifyTracking.eventViewWishlist();
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
                if(searchEditText.getQuery().length()>0){
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
                searchEditText.setQuery("",false);
                searchEditText.setIconified(true);
                wishList.fetchDataActerClearSearch(getActivity());
            }
        });
    }

    @Override
    public void clearSearch() {
        searchEditText.setQuery("", false);
        searchEditText.clearFocus();
    }

    @Override
    public void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        searchEditText.setOnQueryTextListener(this);
        searchEditText.setSuggestionsAdapter(null);
        searchEditText.setFocusable(false);
        searchEditText.clearFocus();
        searchEditText.requestFocusFromTouch();
        setAdapter();
    }

    @Override
    public void setAdapter() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayMainContent(boolean isShow) {
        if (isShow)
            mainContent.setVisibility(View.VISIBLE);
        else
            mainContent.setVisibility(View.GONE);
    }

    @Override
    public void displayLoading(boolean isShow) {
        if (isShow)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayDeleteWishlistDialog(final String productId) {
        if (!isDeleteDialogShown) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.msg_delete_wishlist_confirmation);
            builder.setPositiveButton(R.string.title_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wishList.deleteWishlist(getActivity(), productId);
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
    public void onSuccessDeleteWishlist() {
        SnackbarManager.make(getActivity(),
                MainApplication.getAppContext().getString(R.string.msg_delete_wishlist_success),
                Snackbar.LENGTH_SHORT)
                .show();
        displayPull(false);
        wishList.refreshData(getActivity());
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
            displayMainContent(false);
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    getRetryListener());
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
        adapter = new GridLayoutProductAdapter(getActivity(), data);
        adapter.setWishlistView(this);
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
                } else if (position % 5 == 0 && wishList.getData().get(position).getType() == TkpdState.RecyclerViewItem.TYPE_LIST) {
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
    public boolean onQueryTextSubmit(String query) {
        wishList.searchWishlist(query);
        return false;
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
