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
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactoryImpl;
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.DetailFeedAdapter;
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.listener.WishlistListener;
import com.tokopedia.feedplus.view.presenter.FeedPlusDetailPresenter;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.SingleFeedDetailViewModel;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.kol.KolComponentInstance;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailFragment extends BaseDaggerFragment
        implements FeedPlusDetail.View, WishlistListener {

    private static final String ARGS_DETAIL_ID = "DETAIL_ID";
    private static final int REQUEST_OPEN_PDP = 111;

    RecyclerView recyclerView;
    TextView shareButton;
    TextView seeShopButon;
    View footer;

    @Inject
    FeedPlusDetailPresenter presenter;


    private EndlessRecyclerViewScrollListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private DetailFeedAdapter adapter;
    private CallbackManager callbackManager;
    private PagingHandler pagingHandler;
    private TkpdProgressDialog progressDialog;

    private String detailId;

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
        DaggerFeedPlusComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                .build()
                .inject(this);
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
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        pagingHandler = new PagingHandler();
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus_detail, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.detail_list);
        shareButton = (TextView) parentView.findViewById(R.id.share_button);
        seeShopButon = (TextView) parentView.findViewById(R.id.see_shop);
        footer = parentView.findViewById(R.id.footer);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getFeedDetail(detailId, pagingHandler.getPage());

    }

    private View.OnClickListener onShareClicked(final String url,
                                                final String title,
                                                final String imageUrl,
                                                final String description) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData shareData = ShareData.Builder.aShareData()
                        .setName(title)
                        .setId(detailId)
                        .setDescription(description)
                        .setImgUri(imageUrl)
                        .setUri(url)
                        .setType(ShareData.FEED_TYPE)
                        .build();
                onProductShareClicked(shareData);
            }
        };
    }

    @Override
    public void onWishlistClicked(int adapterPosition, Integer productId, boolean isWishlist) {
        if (!isWishlist) {
            presenter.addToWishlist(adapterPosition, String.valueOf(productId));
            UnifyTracking.eventFeedClickProduct(
                    String.valueOf(productId),
                    getArguments().getString(FeedPlusDetailActivity
                            .EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                            + FeedTrackingEventLabel.Click.ADD_TO_WISHLIST +
                            FeedTrackingEventLabel.PAGE_PRODUCT_LIST);
        } else {
            presenter.removeFromWishlist(adapterPosition, String.valueOf(productId));
            UnifyTracking.eventFeedClickProduct(
                    String.valueOf(productId),
                    getArguments().getString(FeedPlusDetailActivity
                            .EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                            + FeedTrackingEventLabel.Click.REMOVE_WISHLIST +
                            FeedTrackingEventLabel.PAGE_PRODUCT_LIST);
        }
    }

    @Override
    public void onGoToShopDetail(Integer shopId) {
        goToShopDetail(shopId);
        UnifyTracking.eventFeedViewShop(String.valueOf(shopId),
                getArguments().getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                        + FeedTrackingEventLabel.View.PRODUCTLIST_SHOP);

    }

    @Override
    public void onErrorGetFeedDetail(String errorMessage) {
        finishLoading();
        footer.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getFeedDetail(detailId, pagingHandler.getPage());
                    }
                });
    }

    private void finishLoading() {
        adapter.dismissLoading();
    }

    @Override
    public void onEmptyFeedDetail() {
        finishLoading();
        adapter.showEmpty();
        footer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void onGoToBuyProduct(String productId, String price, String imageSource) {
        ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                .setProductId(productId)
                .setPrice(price)
                .setImageUri(imageSource)
                .build();

        Intent intent = TransactionAddToCartRouter
                .createInstanceAddToCartActivity(getActivity(), pass);
        startActivity(intent);
    }

    @Override
    public void onSuccessGetSingleFeedDetail(final FeedDetailHeaderViewModel header,
                                             SingleFeedDetailViewModel singleFeedDetailViewModel) {
        finishLoading();
        footer.setVisibility(View.VISIBLE);

        if (pagingHandler.getPage() == 1) {
            adapter.add(header);
        }

        adapter.add(singleFeedDetailViewModel);

        shareButton.setOnClickListener(onShareClicked(
                header.getShareLinkURL(),
                header.getShopName(),
                header.getShopAvatar(),
                header.getShareLinkDescription()));

        seeShopButon.setOnClickListener(onGoToShopDetailFromButton(header.getShopId()));

        pagingHandler.setHasNext(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessGetFeedDetail(
            final FeedDetailHeaderViewModel header,
            ArrayList<Visitable> listDetail,
            boolean hasNextPage) {
        finishLoading();
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

        pagingHandler.setHasNext(hasNextPage);

        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener onGoToShopDetailFromButton(final Integer shopId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShopDetail(shopId);
                UnifyTracking.eventFeedClickShop(String.valueOf(shopId),
                        getArguments().getString(FeedPlusDetailActivity
                                .EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.VISIT_SHOP);
            }
        };
    }


    private void goToShopDetail(Integer shopId) {
        Intent intent = ((FeedModuleRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), String.valueOf(shopId));
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void onErrorAddWishList(String errorMessage, int adapterPosition) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(int adapterPosition) {
        dismissLoadingProgress();
        if (adapter.getList().get(adapterPosition) instanceof FeedDetailViewModel) {
            ((FeedDetailViewModel) adapter.getList().get(adapterPosition)).setWishlist(true);
            adapter.notifyItemChanged(adapterPosition);
        }
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, int adapterPosition) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);

    }

    @Override
    public void onSuccessRemoveWishlist(int adapterPosition) {
        dismissLoadingProgress();
        if (adapter.getList().get(adapterPosition) instanceof FeedDetailViewModel) {
            ((FeedDetailViewModel) adapter.getList().get(adapterPosition)).setWishlist(false);
            adapter.notifyItemChanged(adapterPosition);
        }
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void onGoToProductDetail(String productId, boolean isWishlist, int adapterPosition) {
        if (getActivity().getApplication() instanceof PdpRouter) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetailForResult(this,
                    productId, adapterPosition, REQUEST_OPEN_PDP);
            UnifyTracking.eventFeedViewProduct(productId,
                    getArguments().getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                            + FeedTrackingEventLabel.View.PRODUCTLIST_PDP);
        }
    }

    @Override
    public int getColor(int resId) {
        return MethodChecker.getColor(getActivity(), resId);
    }

    private void dismissLoadingProgress() {
        if (progressDialog != null && progressDialog.isProgress())
            progressDialog.dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_DETAIL_ID, detailId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_PDP
                && data != null
                && data.getExtras() != null
                && data.getExtras().getInt(ProductDetailRouter
                .WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            int position = data.getExtras().getInt(ProductDetailRouter
                    .WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist = data.getExtras().getBoolean(ProductDetailRouter
                    .WIHSLIST_STATUS_IS_WISHLIST, false);

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

    private void onProductShareClicked(@NonNull ShareData data) {
        new DefaultShare(getActivity(), data).show();
    }
}
