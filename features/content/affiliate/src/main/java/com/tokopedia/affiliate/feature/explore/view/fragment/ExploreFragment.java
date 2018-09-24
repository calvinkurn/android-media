package com.tokopedia.affiliate.feature.explore.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.adapter.ExploreAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactoryImpl;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.presenter.ExplorePresenter;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.EmptyExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.design.text.SearchInputView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreFragment
        extends BaseDaggerFragment
        implements ExploreContract.View,
        SearchInputView.Listener,
        SearchInputView.ResetListener, SwipeToRefresh.OnRefreshListener {

    private RecyclerView rvExplore;
    private GridLayoutManager layoutManager;
    private SwipeToRefresh swipeRefreshLayout;
    private SearchInputView searchView;
    private ExploreAdapter adapter;
    private ProgressBar progressBar;
    private static final int ITEM_COUNT = 10;

    private String cursor;
    private boolean isCanLoadMore;
    private String searchKey;

    ExplorePresenter presenter;

    public static ExploreFragment getInstance(Bundle bundle) {
        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_explore, container, false);
        rvExplore = (RecyclerView) view.findViewById(R.id.rv_explore);
        swipeRefreshLayout = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        searchView = (SearchInputView) view.findViewById(R.id.search_input_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        rvExplore.addOnScrollListener(getScrollListener());
        resetData();
        searchView.setListener(this);
        searchView.setResetListener(this);
        searchView.getSearchTextView().setOnClickListener(v -> {
            searchView.getSearchTextView().setCursorVisible(true);
        });
        presenter.getFirstData(searchKey);
    }

    private void resetData() {
        isCanLoadMore = true;
        cursor = "";
        searchKey = "";
    }

    private void initListener() {

    }


    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onRefresh() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
            resetData();
            presenter.getFirstData(searchKey);
        }
    }

    private RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
                if (isCanLoadMore
                        && !TextUtils.isEmpty(cursor)
                        && totalItemCount <= lastVisibleItemPos + ITEM_COUNT){
                    isCanLoadMore = false;
                    presenter.loadMoreData(cursor, searchKey);
                }
            }
        };
    }

    @Override
    public void onSearchSubmitted(String text) {
        resetData();
        searchKey = text;
        presenter.getFirstData(text);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchReset() {
        resetData();
        presenter.getFirstData(searchKey);
    }

    @Override
    public void showLoading() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void dropKeyboard() {
        searchView.getSearchTextView().setCursorVisible(false);
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onBymeClicked(ExploreViewModel model) {

    }

    @Override
    public void onSuccessGetFirstData(List<Visitable> itemList, String cursor) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        if (itemList.size() == 0) {
            layoutManager = new GridLayoutManager(getActivity(), 1);
            itemList = new ArrayList<>();
            itemList.add(new EmptyExploreViewModel());
            isCanLoadMore = false;
            this.cursor = "";
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
            isCanLoadMore = true;
            this.cursor = cursor;
        }
        rvExplore.setLayoutManager(layoutManager);
        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this), itemList);
        rvExplore.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetFirstData(String error) {
        layoutManager = new GridLayoutManager(getActivity(), 1);
        rvExplore.setLayoutManager(layoutManager);
        List<Visitable> itemList = new ArrayList<>();
        itemList.add(new EmptyExploreViewModel());
        adapter = new ExploreAdapter(new ExploreTypeFactoryImpl(this), itemList);
        rvExplore.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessGetMoreData(List<Visitable> itemList, String cursor) {
        adapter.hideLoading();
        adapter.addElement(itemList);
        adapter.notifyDataSetChanged();
        if (TextUtils.isEmpty(cursor)) {
            isCanLoadMore = false;
            this.cursor = "";
        } else {
            isCanLoadMore = true;
            this.cursor = cursor;
        }
    }

    @Override
    public void onErrorGetMoreData(String error) {
        NetworkErrorHelper.createSnackbarWithAction(
                getActivity(),
                error,
                () -> {
                    presenter.loadMoreData(cursor, searchKey);
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
