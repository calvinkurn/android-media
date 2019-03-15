package com.tokopedia.tkpd.tkpdreputation.review.shop.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.imagepreview.ImagePreviewActivity;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTrackingConstant;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationReportActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopTypeFactoryAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.presenter.ReviewShopContract;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.presenter.ReviewShopPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ReviewShopFragment extends BaseListFragment<ReviewShopModelContent, ReviewShopTypeFactoryAdapter>
        implements ReviewProductContentViewHolder.ListenerReviewHolder, ReviewShopContract.View, ReviewShopViewHolder.ShopReviewHolderListener {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "shop_domain";
    @Inject
    ReviewShopPresenter shopReviewPresenter;
    @Inject
    ReputationTracking reputationTracking;

    private ProgressDialog progressDialog;

    protected String shopId;
    protected String shopDomain;

    public static ReviewShopFragment createInstance(String shopId, String shopDomain) {
        ReviewShopFragment shopReviewFragment = new ReviewShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ID, shopId);
        bundle.putString(SHOP_DOMAIN, shopDomain);
        shopReviewFragment.setArguments(bundle);
        return shopReviewFragment;
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setContent(getString(R.string.review_shop_empty_list_content));
        return emptyModel;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(SHOP_ID, "");
        shopDomain = getArguments().getString(SHOP_DOMAIN, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        return inflater.inflate(R.layout.fragment_shop_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_vertical_product_review));
        getRecyclerView(view).addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shopReviewPresenter.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerReputationComponent
                .builder()
                .reputationModule(new ReputationModule())
                .appComponent(((MainApplication) getActivity().getApplication()).getAppComponent())
                .build()
                .inject(this);
        shopReviewPresenter.attachView(this);
    }

    @Override
    public void onItemClicked(ReviewShopModelContent productReviewModelContent) {

    }

    @Override
    public void loadData(int page) {
        shopReviewPresenter.getShopReview(shopDomain, shopId, page);
    }

    @Override
    protected ReviewShopTypeFactoryAdapter getAdapterTypeFactory() {
        return new ReviewShopTypeFactoryAdapter(this, this);
    }

    @Override
    public void onGoToProfile(String reviewerId, int adapterPosition) {
    		onGoToProfileTracking(adapterPosition);
        if (getActivity().getApplicationContext() instanceof ReputationRouter) {
            startActivity(((ReputationRouter) getActivity().getApplicationContext())
                    .getTopProfileIntent(getActivity(),
                            String.valueOf(reviewerId)));
        }
    }

    protected void onGoToProfileTracking(int adapterPosition) {
        reputationTracking.eventClickUserAccountPage(getString(R.string.review), adapterPosition, shopId,
                shopReviewPresenter.isMyShop(shopId));
    }


    @Override
    public void goToPreviewImage(int position, ArrayList<ImageUpload> list, ReviewProductModelContent element) {
        ArrayList<String> listLocation = new ArrayList<>();
        ArrayList<String> listDesc = new ArrayList<>();

        for (ImageUpload image : list) {
            listLocation.add(image.getPicSrcLarge());
            listDesc.add(image.getDescription());
        }

        startActivity(ImagePreviewActivity.getCallingIntent(getActivity(),
                listLocation,
                listDesc,
                position));
    }

    @Override
    public void onGoToShopInfo(String shopId) {
        Intent intent = ((ReputationRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shopId);
        startActivity(intent);
    }

    @Override
    public void onDeleteReviewResponse(ReviewProductModelContent element, int adapterPosition) {
        onDeleteReviewResponseTracking(element, adapterPosition);
        shopReviewPresenter.deleteReview(element.getReviewId(), element.getReputationId(), element.getProductId());
    }

    protected void onDeleteReviewResponseTracking(ReviewProductModelContent element, int adapterPosition) {
        reputationTracking.eventClickChooseThreeDotMenuPage(getString(R.string.review), adapterPosition, ReputationTrackingConstant.DELETE, shopId,
                shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void onSmoothScrollToReplyView(int adapterPosition) {
        getRecyclerView(getView()).smoothScrollToPosition(adapterPosition);
    }

    @Override
    public void onGoToReportReview(String shopId, String reviewId, int adapterPosition) {
        onGoToReportReviewTracking(shopId, adapterPosition);
        startActivity(InboxReputationReportActivity.getCallingIntent(
                getActivity(),
                Integer.valueOf(shopId),
                reviewId));
    }

    protected void onGoToReportReviewTracking(String shopId, int adapterPosition) {
        reputationTracking.eventClickChooseThreeDotMenuPage(getString(R.string.review), adapterPosition, ReputationTrackingConstant.REPORT, shopId,
                shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void onMenuClicked(int adapterPosition) {
        reputationTracking.eventCLickThreeDotMenuPage(getString(R.string.review), adapterPosition, shopId, shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void onSeeReplied(int adapterPosition) {
        reputationTracking.eventClickSeeRepliesPage(getString(R.string.review), adapterPosition, shopId, shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void onLikeDislikePressed(String reviewId, int likeStatus, String productId, boolean status, int adapterPosition) {
        onLikeDislikeTracking(productId, status, adapterPosition);
        shopReviewPresenter.postLikeDislikeReview(reviewId, likeStatus, productId);
    }

    protected void onLikeDislikeTracking(String productId, boolean status, int adapterPosition) {
        reputationTracking.eventClickLikeDislikeReviewPage(getString(R.string.review), status, adapterPosition, shopId,
                shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void onErrorDeleteReview(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain, String reviewId) {
        ((ReviewProductAdapter) getAdapter()).updateDeleteReview(reviewId);
    }

    @Override
    public void onErrorPostLikeDislike(Throwable e, String reviewId, int likeStatus) {
        ((ReviewProductAdapter) getAdapter()).updateLikeStatusError(reviewId, likeStatus);
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain, String reviewId) {
        ((ReviewProductAdapter) getAdapter()).updateLikeStatus(likeDislikeDomain.getLikeStatus(),
                likeDislikeDomain.getTotalLike(), reviewId);
    }

    @Override
    public void onGoToDetailProduct(String productId, int adapterPosition) {
        onGoToDetailProductTracking(productId, adapterPosition);
        if (getContext()!= null) {
            RouteManager.route(getContext(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        }
    }

    protected void onGoToDetailProductTracking(String productId, int adapterPosition) {
        reputationTracking.eventClickProductPictureOrNamePage(getString(R.string.review), adapterPosition, productId,
                shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showProgressLoading() {
        progressDialog.show();
    }

    @NonNull
    @Override
    protected BaseListAdapter<ReviewShopModelContent, ReviewShopTypeFactoryAdapter> createAdapterInstance() {
        return new ReviewProductAdapter(getAdapterTypeFactory());
    }
}
