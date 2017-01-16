package com.tokopedia.core.manage.people.address.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.adapter.ChooseAddressAdapter;
import com.tokopedia.core.manage.people.address.listener.ChooseAddressFragmentView;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.presenter.ChooseAddressFragmentPresenter;
import com.tokopedia.core.manage.people.address.presenter.ChooseAddressFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity.OnChooseAddressViewListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressFragment extends BasePresenterFragment<ChooseAddressFragmentPresenter> implements ChooseAddressFragmentView,
        OnChooseAddressViewListener{

    @BindView(R2.id.main_view)
    View mainView;

    @BindView(R2.id.address_list)
    RecyclerView addressRV;

    @BindView(R2.id.search)
    EditText search;

    @BindView(R2.id.search_but)
    ImageView searchBut;

    ChooseAddressAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;
    Snackbar snackbar;


    public static ChooseAddressFragment createInstance() {
        ChooseAddressFragment fragment = new ChooseAddressFragment();
        Bundle bundle = new Bundle();
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
        presenter.setCache(search.getText()!=null ? search.getText().toString() : "");

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
        presenter = new ChooseAddressFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_choose_address;
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh(search.getText()!=null ? search.getText().toString() : "");
            }
        };
    }

    @Override
    protected void initView(View view) {
        this.refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        snackbar = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_INDEFINITE);
        searchBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refresh();

            }
        });
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                presenter.loadMore(lastItemPosition, visibleItem,search.getText()!=null ? search.getText().toString() : "");
            }
        };
    }

    @Override
    protected void setViewListener() {
        addressRV.addOnScrollListener(onScroll());
    }

    @Override
    protected void initialVar() {

        adapter = ChooseAddressAdapter.createInstance(getActivity());
        adapter.setOnRetryListenerRV(presenter.onRetry(search.getText()!=null ? search.getText().toString() : ""));
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addressRV.setLayoutManager(linearLayoutManager);
        addressRV.setAdapter(adapter);

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
    public ChooseAddressAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {
        adapter.showLoading(true);
    }

    @Override
    public void showErrorMessage(String s) {
        snackbar = SnackbarManager.make(getActivity(), s, Snackbar.LENGTH_INDEFINITE)
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
        presenter.onRefresh(search.getText()!=null ? search.getText().toString() : "");
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
                presenter.setCache(search.getText()!=null ? search.getText().toString() : "");
            }
        });
    }

    @Override
    public void setRetry() {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setCache(search.getText()!=null ? search.getText().toString() : "");
            }
        }).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setCache(search.getText()!=null ? search.getText().toString() : "");
            }
        });
    }

    @Override
    public void setRetry(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setCache(search.getText()!=null ? search.getText().toString() : "");
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public ArrayList<Destination> onActivityBackPressed() {
        return getAdapter().getList();
    }
}
