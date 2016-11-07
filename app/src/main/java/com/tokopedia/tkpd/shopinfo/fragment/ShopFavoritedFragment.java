package com.tokopedia.tkpd.shopinfo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.app.BasePresenterFragment;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.shopinfo.adapter.ShopFavoritedAdapter;
import com.tokopedia.tkpd.shopinfo.listener.ShopFavoritedFragmentView;
import com.tokopedia.tkpd.shopinfo.presenter.ShopFavoritedFragmentPresenter;
import com.tokopedia.tkpd.shopinfo.presenter.ShopFavoritedFragmentPresenterImpl;
import com.tokopedia.tkpd.util.RefreshHandler;

import butterknife.Bind;

/**
 * Created by Alifa on 10/5/2016.
 */
public class ShopFavoritedFragment extends BasePresenterFragment<ShopFavoritedFragmentPresenter> implements ShopFavoritedFragmentView {

    @Bind(R2.id.main_view)
    View mainView;

    @Bind(R2.id.favoritee_list)
    RecyclerView favoriteeRV;

    ShopFavoritedAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;
    Snackbar snackbar;

    public static ShopFavoritedFragment createInstance(String shopId) {
        ShopFavoritedFragment fragment = new ShopFavoritedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shop_id", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        setActionsEnabled(false);
        String shopId = getArguments().getString("shop_id");
        presenter.setCache(shopId);
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ShopFavoritedFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_favorited;
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh();
            }
        };
    }

    @Override
    protected void initView(View view) {
        this.refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        snackbar = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_INDEFINITE);
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                presenter.loadMore(lastItemPosition, visibleItem);
            }
        };
    }

    @Override
    protected void setViewListener() {
        favoriteeRV.addOnScrollListener(onScroll());
    }

    @Override
    protected void initialVar() {
        adapter = ShopFavoritedAdapter.createInstance(getActivity());
        adapter.setOnRetryListenerRV(presenter.onRetry());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        favoriteeRV.setLayoutManager(linearLayoutManager);
        favoriteeRV.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void finishLoading() {
        adapter.showLoading(false);
        adapter.showEmpty(false);
        refreshHandler.setPullEnabled(true);
        refreshHandler.setRefreshing(false);
    }
    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }


    @Override
    public ShopFavoritedAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {
        adapter.showLoading(true);
    }

    @Override
    public void showErrorMessage(String error) {
        snackbar = SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_close), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }
                );
        snackbar.show();
    }

    @Override
    public void removeError() {
        snackbar.dismiss();
        adapter.showEmpty(false);
    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {
        refreshHandler.setPullEnabled(isEnabled);
    }

    @Override
    public boolean isRefreshing() {
            return refreshHandler.isRefreshing();
    }

    @Override
    public void refresh() {
        presenter.onRefresh();
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void showEmptyState() {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                String shopId = getArguments().getString("shop_id");
                presenter.setCache(shopId);
            }
        });
    }

    @Override
    public void setRetry() {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                String shopId = getArguments().getString("shop_id");
                presenter.setCache(shopId);
            }
        }).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                String shopId = getArguments().getString("shop_id");
                presenter.setCache(shopId);
            }
        });
    }

    @Override
    public void setRetry(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                String shopId = getArguments().getString("shop_id");
                presenter.setCache(shopId);
            }
        }).showRetrySnackbar();
    }

    @Override
    public boolean isEmpty() {
        if (adapter.getList().size() == 0) {
            return true;
        }
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

}
