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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.feature.explore.di.DaggerExploreComponent;
import com.tokopedia.affiliate.feature.explore.view.adapter.ExploreAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactoryImpl;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.presenter.ExplorePresenter;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.EmptyExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.design.text.SearchInputView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    private ImageView ivBack;
    private ProgressBar progressBar;
    private static final int ITEM_COUNT = 10;

    private String cursor;
    private boolean isCanLoadMore;
    private String searchKey;

    @Inject
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
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        presenter.attachView(this);
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
        testData();
//        presenter.getFirstData(searchKey);
    }

    private void resetData() {
        isCanLoadMore = true;
        cursor = "";
        searchKey = "";
    }

    private void initListener() {
        ivBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
    }

    @Override
    protected void initInjector() {

        DaggerAffiliateComponent affiliateComponent = (DaggerAffiliateComponent) DaggerAffiliateComponent.builder()
                .baseAppComponent(((BaseMainApplication)getActivity().getApplicationContext()).getBaseAppComponent()).build();

        DaggerExploreComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onRefresh() {
        resetData();
        presenter.getFirstData(searchKey, true);
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
                    adapter.addElement(new LoadingMoreModel());
                    presenter.loadMoreData(cursor, searchKey);
                }
            }
        };
    }

    @Override
    public void onSearchSubmitted(String text) {
        resetData();
        searchKey = text;
        presenter.getFirstData(text, false);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchReset() {
        resetData();
        presenter.getFirstData(searchKey, true);
    }

    @Override
    public void showLoading() {
        adapter.addElement(new LoadingModel());
    }

    @Override
    public void hideLoading() {
        adapter.hideLoading();
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
    public void onProductClicked(ExploreViewModel model) {

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

    private void testData() {
        List<Visitable> itemList = new ArrayList<>();

        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi",
                "Rp. 12.000",
                "1",
                "1"));
        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi",
                "Rp. 12.000",
                "1",
                "1"));
        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi",
                "Rp. 12.000",
                "1",
                "1"));
        itemList.add(new ExploreViewModel(
                "1",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp. 10.000",
                "1",
                "1"));

        itemList.add(new ExploreViewModel(
                "2",
                "https://i0.wp.com/resepkoki.id/wp-content/uploads/2016/10/Resep-Nasgor-sapi.jpg?fit=3264%2C2448&ssl=1",
                "Nasi Goreng Sapi",
                "Rp. 12.000",
                "1",
                "1"));

        onSuccessGetFirstData(itemList,"");
    }
}
