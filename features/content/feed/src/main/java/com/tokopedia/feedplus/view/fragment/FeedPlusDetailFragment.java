package com.tokopedia.feedplus.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactoryImpl;
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.DetailFeedAdapter;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailFragment extends BaseDaggerFragment
        implements FeedPlusDetail.View, WishListActionListener {

    private static final String ARGS_DETAIL_ID = "DETAIL_ID";
    private static final int REQUEST_OPEN_PDP = 111;

    public static final String WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition";
    public static final String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";

    RecyclerView recyclerView;
    TextView shareButton;
    TextView seeShopButon;
    View footer;

    @Inject
    FeedPlusDetail.Presenter presenter;

    @Inject
    FeedAnalytics analytics;

    private EndlessRecyclerViewScrollListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private DetailFeedAdapter adapter;
    private PagingHandler pagingHandler;
    private ProgressBar progressBar;
    private String detailId;

    public static FeedPlusDetailFragment createInstance(Bundle bundle) {
        FeedPlusDetailFragment fragment = new FeedPlusDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return FeedTrackingEventLabel.SCREEN_FEED_DETAIL;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            DaggerFeedPlusComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                    .build()
                    .inject(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            detailId = savedInstanceState.getString(ARGS_DETAIL_ID);
        else if (getArguments() != null)
            detailId = getArguments().getString(FeedPlusDetailActivity.EXTRA_DETAIL_ID);
        else
            detailId = "";

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewScrollListener = onRecyclerViewListener();
        FeedPlusDetailTypeFactory typeFactory = new FeedPlusDetailTypeFactoryImpl(this);
        adapter = new DetailFeedAdapter(typeFactory);
        GraphqlClient.init(getContext());
        pagingHandler = new PagingHandler();
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus_detail, container, false);
        recyclerView = parentView.findViewById(R.id.detail_list);
        shareButton = parentView.findViewById(R.id.share_button);
        seeShopButon = parentView.findViewById(R.id.see_shop);
        footer = parentView.findViewById(R.id.footer);
        progressBar = parentView.findViewById(R.id.progress_bar);
        prepareView();
        presenter.attachView(this, this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerviewScrollListener);
    }

    private EndlessRecyclerViewScrollListener onRecyclerViewListener() {
        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!adapter.isLoading() && pagingHandler.CheckNextPage()) {
                    pagingHandler.nextPage();
                    presenter.getFeedDetail(detailId, pagingHandler.getPage());
                }
            }
        };
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getFeedDetail(detailId, pagingHandler.getPage());

    }

    private View.OnClickListener onShareClicked(final String url,
                                                final String title,
                                                final String imageUrl,
                                                final String description) {
        return v -> {
            if (getActivity() != null) {
                ((FeedModuleRouter) getActivity().getApplicationContext()).shareFeed(
                        getActivity(), detailId, url, title, imageUrl, description);

            }
        };
    }

    @Override
    public void onWishlistClicked(int adapterPosition, Integer productId, boolean isWishlist) {
        if (getArguments() != null) {
            if (!isWishlist) {
                presenter.addToWishlist(adapterPosition, String.valueOf(productId));
                analytics.eventFeedClickProduct(
                        getScreenName(),
                        String.valueOf(productId),
                        getArguments().getString(FeedPlusDetailActivity
                                .EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.ADD_TO_WISHLIST +
                                FeedTrackingEventLabel.PAGE_PRODUCT_LIST);
            } else {
                presenter.removeFromWishlist(adapterPosition, String.valueOf(productId));
                analytics.eventFeedClickProduct(
                        getScreenName(),
                        String.valueOf(productId),
                        getArguments().getString(FeedPlusDetailActivity
                                .EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.REMOVE_WISHLIST +
                                FeedTrackingEventLabel.PAGE_PRODUCT_LIST);
            }
        }
    }

    @Override
    public void onGoToShopDetail(Integer shopId) {
        if (getArguments() != null) {
            goToShopDetail(shopId);
            analytics.eventFeedViewShop(
                    getScreenName(),
                    String.valueOf(shopId),
                    getArguments().getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                            + FeedTrackingEventLabel.View.PRODUCTLIST_SHOP);
        }

    }

    @Override
    public void onErrorGetFeedDetail(String errorMessage) {
        dismissLoading();
        footer.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                () -> presenter.getFeedDetail(detailId, pagingHandler.getPage()));
    }

    @Override
    public void onEmptyFeedDetail() {
        adapter.showEmpty();
        footer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @Override
    public void onGoToBuyProduct(String productId, String price, String imageSource) {


        if (getActivity() != null) {
            Intent intent = ((FeedModuleRouter) getActivity().getApplicationContext()).getAddToCartIntent(
                    getActivity(), productId, price, imageSource);
            startActivity(intent);
        }
    }

    @Override
    public void onSuccessGetFeedDetail(
            final FeedDetailHeaderViewModel header,
            ArrayList<Visitable> listDetail,
            boolean hasNextPage) {
        footer.setVisibility(View.VISIBLE);

        if (pagingHandler.getPage() == 1) {
            adapter.add(header);
        }

        adapter.addList(listDetail);

        shareButton.setOnClickListener(onShareClicked(
                header.getShareLinkURL(),
                header.getShopName(),
                header.getShopAvatar(),
                header.getShareLinkDescription()));

        seeShopButon.setOnClickListener(onGoToShopDetailFromButton(header.getShopId()));

        pagingHandler.setHasNext(listDetail.size() > 1 && hasNextPage);

        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener onGoToShopDetailFromButton(final Integer shopId) {
        return v -> {
            if (getArguments() != null) {
                goToShopDetail(shopId);
                analytics.eventFeedClickShop(
                        getScreenName(),
                        String.valueOf(shopId),
                        getArguments().getString(
                                FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.VISIT_SHOP);
            }
        };
    }


    private void goToShopDetail(Integer shopId) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            Intent intent = ((FeedModuleRouter) getActivity().getApplicationContext())
                    .getShopPageIntent(getActivity(), String.valueOf(shopId));
            startActivity(intent);
        }
    }

    @Override
    public void showLoading() {
        footer.setVisibility(View.GONE);
        adapter.showLoading();
    }

    @Override
    public void dismissLoading() {
        footer.setVisibility(View.VISIBLE);
        adapter.dismissLoading();
    }

    @Override
    public void showLoadingMore() {
        adapter.showLoadingMore();
    }

    @Override
    public void dismissLoadingMore() {
        adapter.dismissLoadingMore();
    }

    @Override
    public void showLoadingProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productID) {
        dismissLoadingProgress();

        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i) instanceof FeedDetailViewModel) {
                FeedDetailViewModel feedDetailViewModel = ((FeedDetailViewModel) adapter.getList().get(i));
                if (productID.equals(String.valueOf(feedDetailViewModel.getProductId()))) {
                    feedDetailViewModel.setWishlist(true);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);

    }

    @Override
    public void onSuccessRemoveWishlist(String productID) {
        dismissLoadingProgress();

        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i) instanceof FeedDetailViewModel) {
                FeedDetailViewModel feedDetailViewModel = ((FeedDetailViewModel) adapter.getList().get(i));
                if (productID.equals(String.valueOf(feedDetailViewModel.getProductId()))) {
                    feedDetailViewModel.setWishlist(false);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void onGoToProductDetail(String productId, boolean isWishlist, int adapterPosition) {
        if (getActivity() != null
                && getActivity().getApplicationContext() != null
                && getArguments() != null
                && getActivity().getApplicationContext() instanceof FeedModuleRouter) {

            getActivity().startActivityForResult(getProductIntent(productId), REQUEST_OPEN_PDP);

            analytics.eventFeedViewProduct(
                    getScreenName(),
                    productId,
                    getArguments().getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                            + FeedTrackingEventLabel.View.PRODUCTLIST_PDP);
        }
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }

    @Override
    public int getColor(int resId) {
        return MethodChecker.getColor(getActivity(), resId);
    }

    @Override
    public void setHasNextPage(boolean hasNextPage) {
        pagingHandler.setHasNext(hasNextPage);
    }

    private void dismissLoadingProgress() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_DETAIL_ID, detailId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_OPEN_PDP
                && data != null
                && data.getExtras() != null
                && data.getExtras().getInt(WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            int position = data.getExtras().getInt(WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist = data.getExtras().getBoolean(WIHSLIST_STATUS_IS_WISHLIST, false);

            updateWishlistFromPDP(position, isWishlist);
        }
    }

    private void updateWishlistFromPDP(int position, boolean isWishlist) {
        if (adapter != null
                && adapter.getList() != null
                && !adapter.getList().isEmpty()
                && position < adapter.getList().size()
                && adapter.getList().get(position) != null
                && adapter.getList().get(position) instanceof FeedDetailViewModel) {
            ((FeedDetailViewModel) adapter.getList().get(position)).setWishlist(isWishlist);
            adapter.notifyItemChanged(position);
        }
    }

}
