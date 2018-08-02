package com.tokopedia.digital_deals.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.FavouriteDealsContract;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.FavouriteDealsPresenter;

import java.util.List;

import javax.inject.Inject;

public class FavouriteDealsFragment extends BaseDaggerFragment implements FavouriteDealsContract.View,
        DealsCategoryAdapter.INavigateToActivityRequest {

    @Inject
    FavouriteDealsPresenter mPresenter;
    private RecyclerView recyclerViewDeals;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private DealsCategoryAdapter dealsAdapter;
    private final boolean IS_SHORT_LAYOUT = true;
    private final boolean IS_BRAND_CARD = true;
    private final boolean IS_FAVOURITE_CARD = true;
    private FrameLayout mainContent;
    private LinearLayout noContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_deals, container, false);
        setViewIds(view);
        mPresenter.getFavouriteDeals();
        return view;
    }


    private void setViewIds(View view) {
        recyclerViewDeals = view.findViewById(R.id.recycler_view_deals);
        progressBar = view.findViewById(R.id.prog_bar);
        mainContent = view.findViewById(R.id.main_content);
        noContent = view.findViewById(R.id.no_content);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(layoutManager);
        dealsAdapter = new DealsCategoryAdapter(null, this, !IS_SHORT_LAYOUT, !IS_BRAND_CARD, IS_FAVOURITE_CARD);
        recyclerViewDeals.setAdapter(dealsAdapter);

    }

    public static Fragment createInstance(){
        return new FavouriteDealsFragment();
    }

    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void renderDealsList(List<ProductItem> deals) {
        if (deals != null && deals.size() > 0) {
            dealsAdapter.clearList();
            dealsAdapter.addAll(deals, false);
            dealsAdapter.notifyDataSetChanged();
            recyclerViewDeals.setVisibility(View.VISIBLE);
            recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
            noContent.setVisibility(View.GONE);
        } else {
            recyclerViewDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
    }


    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void removeFooter() {

        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addFooter();

    }

    @Override
    public void addDealsToCards(List<ProductItem> productItems) {
        if (productItems != null) {
            ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addAll(productItems);
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }


    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode) {
        navigateToActivityRequest(intent, requestCode);
    }
}
