package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdcustomer.R;
import com.tokopedia.tkpd.tkpdcustomer.R2;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.DetailFeedAdapter;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.presenter.FeedPlusDetailPresenter;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.util.TimeConverter;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.FeedDetailViewModel;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.ProductCardViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailFragment extends BaseDaggerFragment
        implements FeedPlusDetail.View {

    private static final String ARGS_DATA = "ARGS_DATA";
    @BindView(R2.id.title)
    TextView title;

    @BindView(R2.id.shop_avatar)
    ImageView shopAvatar;

    @BindView(R2.id.gold_merchant)
    ImageView goldMerchantBadge;

    @BindView(R2.id.time)
    TextView time;

    @BindView(R2.id.detail_list)
    RecyclerView recyclerView;

    @BindView(R2.id.share_button)
    TextView shareButton;

    @BindView(R2.id.see_shop)
    TextView seeShopButon;

    @Inject
    FeedPlusDetailPresenter presenter;

    private Unbinder unbinder;
    private EndlessRecyclerviewListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private DetailFeedAdapter adapter;
    private ProductCardViewModel productCardViewModel;

    public static FeedPlusDetailFragment createInstance(Bundle bundle) {
        FeedPlusDetailFragment fragment = new FeedPlusDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_FEED_DETAIL;
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
        initVar(savedInstanceState);
    }

    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            productCardViewModel = savedInstanceState.getParcelable(ARGS_DATA);
        else if (getArguments() != null)
            productCardViewModel = getArguments().getParcelable(FeedPlusDetailActivity.EXTRA_DATA);
        else
            productCardViewModel = new ProductCardViewModel();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewScrollListener = onRecyclerViewListener();
        FeedPlusDetailTypeFactory typeFactory = new FeedPlusDetailTypeFactoryImpl(this);
        adapter = new DetailFeedAdapter(typeFactory);

    }

    private EndlessRecyclerviewListener onRecyclerViewListener() {
        return new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus_detail, container, false);
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FeedDetailViewModel prod1 = new FeedDetailViewModel(
                "Produk1",
                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");

        FeedDetailViewModel prod2 = new FeedDetailViewModel(
                "Produk2",
                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");

        FeedDetailViewModel prod3 = new FeedDetailViewModel(
                "Produk3",
                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");

        FeedDetailViewModel prod4 = new FeedDetailViewModel(
                "Produk4",
                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");

        ArrayList<FeedDetailViewModel> listProduct = new ArrayList<>();
        listProduct.add(prod1);
        listProduct.add(prod2);
        listProduct.add(prod3);
        listProduct.add(prod4);

        adapter.addList(listProduct);

        String titleText = "<b>" + productCardViewModel.getShopName() + "</b> "
                + productCardViewModel.getActionText();
        title.setText(MethodChecker.fromHtml(titleText));
        ImageHandler.LoadImage(shopAvatar, productCardViewModel.getShopAvatar());

        if (productCardViewModel.isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        time.setText(TimeConverter.generateTime(productCardViewModel.getPostTime()));

        shopAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToShopDetail();
            }
        });
    }

    private void onGoToShopDetail() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_DATA, productCardViewModel);
    }

    @Override
    public void onWishlistClicked() {

    }
}
