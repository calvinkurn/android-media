package com.tokopedia.tkpd.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.adapter.Visitable;

import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedPlusTypeFactory;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedPlusTypeFactoryImpl;
import com.tokopedia.tkpd.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductFeedViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductFeedViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromoViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromotedShopViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusFragment extends BaseDaggerFragment
        implements FeedPlus.View,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @Inject
    FeedPlusPresenter presenter;

    private Unbinder unbinder;
    private EndlessRecyclerviewListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private FeedPlusAdapter adapter;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_PRODUCT_FEED;
    }

    @Override
    protected void initInjector() {

        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .activityModule(new ActivityModule(getActivity()))
                .build();

        DaggerFeedPlusComponent daggerFeedPlusComponent = (DaggerFeedPlusComponent) DaggerFeedPlusComponent.builder()
                .appComponent(daggerAppComponent)
                .build();

        daggerFeedPlusComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    private void initVar() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewScrollListener = onRecyclerViewListener();
        FeedPlusTypeFactory typeFactory = new FeedPlusTypeFactoryImpl(this);
        adapter = new FeedPlusAdapter(typeFactory);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        prepareView();
        presenter.attachView(this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerviewScrollListener);
        swipeToRefresh.setOnRefreshListener(this);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProductFeedViewModel prod1 = new ProductFeedViewModel(
                "Produk1",
                "Rp 10.000",
                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");
        ProductFeedViewModel prod2 = new ProductFeedViewModel(
                "Produk2",
                "Rp 11.0000",
                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");
        ProductFeedViewModel prod3 = new ProductFeedViewModel(
                "Produk3",
                "Rp 21.0000",
                "http://img03.deviantart.net/ebe3/i/2007/294/e/c/kerbau_by_jin_concepts.jpg");
        ProductFeedViewModel prod4 = new ProductFeedViewModel(
                "Produk4",
                "Rp 13.000",
                "http://4.bp.blogspot.com/-THtQuGtlkH8/UwbV8vtw9gI/AAAAAAAACHI/d9m4DYOo-0s/s1600/Singa+Hewan+Karnivora.jpg");

        ProductFeedViewModel prod5 = new ProductFeedViewModel(
                "Produk5",
                "Rp 23.000",
                "http://riesalam.com/wp-content/uploads/2015/06/Penyakit-akibat-tikus-di-rumah.jpg");

        ProductFeedViewModel prod6 = new ProductFeedViewModel(
                "Produk6",
                "Rp 43.000",
                "https://hellosehat.com/wp-content/uploads/2016/05/anjing-yang-aman-untuk-alergi.jpg");

        ProductFeedViewModel prod7 = new ProductFeedViewModel(
                "Produk7",
                "Rp 43.000",
                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");

        ProductFeedViewModel prod8 = new ProductFeedViewModel(
                "Produk8",
                "Rp 43.000",
                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");

        ArrayList<ProductFeedViewModel> listProduct = new ArrayList<>();
        listProduct.add(prod1);

        ArrayList<ProductFeedViewModel> listProduct2 = new ArrayList<>();
        listProduct2.add(prod1);
        listProduct2.add(prod2);

        ArrayList<ProductFeedViewModel> listProduct3 = new ArrayList<>();
        listProduct3.add(prod1);
        listProduct3.add(prod2);
        listProduct3.add(prod3);

        ArrayList<ProductFeedViewModel> listProduct4 = new ArrayList<>();
        listProduct4.addAll(listProduct3);
        listProduct4.add(prod4);


        ArrayList<ProductFeedViewModel> listProduct5 = new ArrayList<>();
        listProduct5.addAll(listProduct4);
        listProduct5.add(prod5);

        ArrayList<ProductFeedViewModel> listProduct6 = new ArrayList<>();
        listProduct6.addAll(listProduct5);
        listProduct6.add(prod6);

        ArrayList<ProductFeedViewModel> listProduct8 = new ArrayList<>();
        listProduct8.addAll(listProduct6);
        listProduct8.add(prod7);
        listProduct8.add(prod8);

        List<Visitable> list = new ArrayList<>();

        list.add(new ProductCardViewModel("Nisie 1", listProduct));
//        list.add(new ProductCardViewModel("Nisie 2", listProduct2));
//        list.add(new ProductCardViewModel("Nisie 3", listProduct3));
//        list.add(new PromoViewModel(listProduct6));
//        list.add(new ProductCardViewModel("Nisie 4", listProduct4));
//        list.add(new ProductCardViewModel("Nisie 5", listProduct5));
//        list.add(new ProductCardViewModel("Nisie 6", listProduct6));
        list.add(new PromotedShopViewModel("Tep Shop 1", true, "Toko terbaik", listProduct3));
        list.add(new ProductCardViewModel("Nisie 8", listProduct8));

        adapter.addList(list);
        adapter.notifyDataSetChanged();
    }


    private EndlessRecyclerviewListener onRecyclerViewListener() {
        return new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void onShareButtonClicked() {

    }

    @Override
    public void onGoToProductDetail() {

    }

    @Override
    public void onGoToFeedDetail() {

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        try {
//            if (isVisibleToUser && isAdded() && getActivity() != null) {
//                if (isAdapterNotEmpty()) {
//                    validateMessageError();
//                } else {
//                    favoritePresenter.loadInitialData();
//                }
//                ScreenTracking.screen(getScreenName());
//
//            } else {
//                if (messageSnackbar != null && messageSnackbar.isShown()) {
//                    messageSnackbar.hideRetrySnackbar();
//                }
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            onCreate(new Bundle());
//        }
//    }
}
