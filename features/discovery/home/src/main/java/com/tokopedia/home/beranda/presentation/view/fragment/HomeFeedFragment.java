package com.tokopedia.home.beranda.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedViewHolder;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.home.constant.ConstantKey;

import javax.inject.Inject;

public class HomeFeedFragment extends BaseListFragment<HomeFeedViewModel, HomeFeedTypeFactory> implements HomeFeedContract.View {

    public static final String ARG_TAB_INDEX = "ARG_TAB_INDEX";
    public static final String ARG_RECOM_ID = "ARG_RECOM_ID";

    private static final int DEFAULT_TOTAL_ITEM_PER_PAGE = 12;
    private static final int DEFAULT_SPAN_COUNT = 2;

    @Inject
    HomeFeedPresenter presenter;

    private int totalScrollY;
    private int tabIndex;
    private int recomId;
    private HomeEggListener homeEggListener;
    private HomeTabFeedListener homeTabFeedListener;

    public static HomeFeedFragment newInstance(int tabIndex, int recomId) {
        HomeFeedFragment homeFeedFragment = new HomeFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HomeFeedFragment.ARG_TAB_INDEX, tabIndex);
        bundle.putInt(HomeFeedFragment.ARG_RECOM_ID, recomId);
        homeFeedFragment.setArguments(bundle);
        return homeFeedFragment;
    }

    public void setListener(HomeEggListener homeEggListener) {
        this.homeEggListener = homeEggListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabIndex = getArguments().getInt(ARG_TAB_INDEX);
        recomId = getArguments().getInt(ARG_RECOM_ID);
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }

    @Override
    public void loadData(int page) {
        presenter.attachView(this);
        presenter.loadData(recomId, DEFAULT_TOTAL_ITEM_PER_PAGE, page);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (getAdapter().getItemViewType(position) == HomeFeedViewHolder.LAYOUT) {
                    return 1;
                }
                return DEFAULT_SPAN_COUNT;
            }
        });
        return gridLayoutManager;
    }

    @Override
    protected HomeFeedTypeFactory getAdapterTypeFactory() {
        return new HomeFeedTypeFactory();
    }

    private void initListeners() {
        getRecyclerView(getView()).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalScrollY += dy;
                if (homeEggListener != null) {
                    homeEggListener.hideEggOnScroll();
                }
                if (homeTabFeedListener != null) {
                    homeTabFeedListener.onFeedContentScrolled(dy, totalScrollY);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (homeTabFeedListener != null) {
                    homeTabFeedListener.onFeedContentScrollStateChanged(newState);
                }
            }
        });
    }

    @Override
    public void onItemClicked(HomeFeedViewModel homeFeedViewModel) {
        goToProductDetail(homeFeedViewModel.getProductId(),
                homeFeedViewModel.getImageUrl(),
                homeFeedViewModel.getProductName(), homeFeedViewModel.getPrice());
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
    protected void initInjector() {
        if (getActivity() != null) {
            BerandaComponent component = DaggerBerandaComponent.builder().baseAppComponent(((BaseMainApplication)
                    getActivity().getApplication()).getBaseAppComponent()).build();
            component.inject(this);
            component.inject(presenter);
        }
    }

    @Override
    protected String getScreenName() {
        return ConstantKey.Analytics.AppScreen.UnifyTracking.SCREEN_UNIFY_HOME_BERANDA;
    }

    public void scrollToTop() {
        getRecyclerView(getView()).smoothScrollToPosition(0);
    }
}
