package com.tokopedia.home.beranda.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper;
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFeedFragment extends Fragment implements HomeFeedListener {

    public static final String ARG_TAB_INDEX = "ARG_TAB_INDEX";

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private HomeRecycleAdapter adapter;
    private int tabIndex;
    private GetHomeFeedUseCase getHomeFeedUseCase;
    private UserSession userSession;
    private HomeEggListener homeEggListener;

    public static HomeFeedFragment newInstance(int tabIndex) {
        HomeFeedFragment homeFeedFragment = new HomeFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HomeFeedFragment.ARG_TAB_INDEX, tabIndex);
        homeFeedFragment.setArguments(bundle);
        return homeFeedFragment;
    }

    public void setListener(HomeEggListener homeEggListener) {
        this.homeEggListener = homeEggListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_feed_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        tabIndex = getArguments().getInt(ARG_TAB_INDEX);
        initAdapter();
        initListeners();
        loadData();
    }

    private void initAdapter() {
        layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        HomeAdapterFactory adapterFactory = new HomeAdapterFactory(
                getChildFragmentManager(),
                null,
                this,
                null
        );
        adapter = new HomeRecycleAdapter(adapterFactory, new ArrayList<Visitable>());
        recyclerView.setAdapter(adapter);
    }

    private void initListeners() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (homeEggListener != null) {
                    homeEggListener.hideEggOnScroll();
                }
            }
        });
    }

    private void loadData() {
        getHomeFeedUseCase = new GetHomeFeedUseCase(getContext(), new GraphqlUseCase(), new HomeFeedMapper());
        userSession = new UserSession(getContext());
        fetchCurrentPageFeed();
    }

    public void fetchCurrentPageFeed() {
        getHomeFeedUseCase.execute(
                getHomeFeedUseCase.getFeedPlusParam(
                        userSession.getUserId(),
                        ""),
                new GetHomeFeedsSubscriber(getContext(), this, 1));
    }

    @Override
    public void onGoToProductDetailFromInspiration(String productId,
                                                   String imageSource,
                                                   String name,
                                                   String price) {
        goToProductDetail(productId, imageSource, name, price);
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        if (getActivity().getApplication() instanceof IHomeRouter) {
            ((IHomeRouter) getActivity().getApplication()).goToProductDetail(
                    getActivity(),
                    productId,
                    imageSourceSingle,
                    name,
                    price
            );
        }
    }

    @Override
    public void updateCursor(String currentCursor) {

    }

    @Override
    public void onSuccessGetFeed(ArrayList<Visitable> visitables) {
        adapter.hideLoading();
        int posStart = adapter.getItemCount();
        adapter.addItems(visitables);
        adapter.notifyItemRangeInserted(posStart, visitables.size());
    }

    @Override
    public void onRetryClicked() {

    }

    @Override
    public void onShowRetryGetFeed() {
        if (adapter != null) {
            adapter.hideLoading();
            adapter.showRetry();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateCursorNoNextPageFeed() {

    }

}
