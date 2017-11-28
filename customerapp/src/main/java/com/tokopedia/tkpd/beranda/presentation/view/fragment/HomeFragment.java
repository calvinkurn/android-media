package com.tokopedia.tkpd.beranda.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.di.DaggerHomeComponent;
import com.tokopedia.tkpd.beranda.di.HomeComponent;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.tkpd.beranda.presentation.view.HomeContract;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.HomeRecycleAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeFragment extends BaseDaggerFragment implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    HomePresenter presenter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        HomeComponent component = DaggerHomeComponent.builder().appComponent(getComponent(AppComponent.class)).build();
        component.inject(this);
        component.inject(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        initRefreshLayout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void initRefreshLayout() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.getHomeData();
            }
        });
        refreshLayout.setOnRefreshListener(this);
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new HomeRecycleAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        presenter.getHomeData();
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void setItems(List<Visitable> items) {
        adapter.setItems(items);
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void removeNetworkError() {

    }
}
