package com.tokopedia.tkpd.tkpdreputation.review.product.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterView;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationReportActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DetailReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelTitleHeader;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductTypeFactoryAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.presenter.ReviewProductContract;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.presenter.ReviewProductPresenter;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.widget.RatingBarReview;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.widget.ReviewProductItemFilterView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewProductFragment extends BaseListFragment<ReviewProductModel, ReviewProductTypeFactoryAdapter>
        implements ReviewProductContract.View, ReviewProductContentViewHolder.ListenerReviewHolder {

    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final int TOTAL_FILTER_ITEM = 5;
    public static final int INITIAL_PAGE = 1;

    @Inject
    ReviewProductPresenter productReviewPresenter;

    private TextView ratingProduct;
    private RatingBar ratingProductStar;
    private TextView counterReview;
    private RatingBarReview fiveStarReview;
    private RatingBarReview fourStarReview;
    private RatingBarReview threeStarReview;
    private RatingBarReview twoStarReview;
    private RatingBarReview oneStarReview;
    private CustomViewQuickFilterView customViewQuickFilterView;
    private ProgressDialog progressDialog;

    List<ReviewProductModel> listReviewHelpful;

    private String productId;

    public static ReviewProductFragment getInstance(String productId) {
        ReviewProductFragment productReviewFragment = new ReviewProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PRODUCT_ID, productId);
        productReviewFragment.setArguments(bundle);
        return productReviewFragment;
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
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        productReviewPresenter.attachView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(EXTRA_PRODUCT_ID, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_review, container, false);
        ratingProduct = view.findViewById(R.id.rating_value);
        ratingProductStar = view.findViewById(R.id.product_rating);
        counterReview = view.findViewById(R.id.total_review);
        fiveStarReview = view.findViewById(R.id.five_star);
        fourStarReview = view.findViewById(R.id.four_star);
        threeStarReview = view.findViewById(R.id.three_star);
        twoStarReview = view.findViewById(R.id.two_star);
        oneStarReview = view.findViewById(R.id.one_star);
        customViewQuickFilterView = view.findViewById(R.id.filter_review);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        setupFilterView();
        return view;
    }

    private void setupFilterView() {
        List<QuickFilterItem> quickFilterItemList = new ArrayList<>();
        for (int i = 1; i <= TOTAL_FILTER_ITEM; i++) {
            CustomViewQuickFilterItem quickFilterItem = new CustomViewQuickFilterItem();
            quickFilterItem.setType(String.valueOf(i));
            ReviewProductItemFilterView productReviewItemFilterView = new ReviewProductItemFilterView(getActivity());
            productReviewItemFilterView.setActive(false);
            productReviewItemFilterView.setRating(i);
            quickFilterItem.setDefaultView(productReviewItemFilterView);

            ReviewProductItemFilterView productReviewItemFilterViewActive = new ReviewProductItemFilterView(getActivity());
            productReviewItemFilterViewActive.setActive(true);
            productReviewItemFilterViewActive.setRating(i);
            quickFilterItem.setSelectedView(productReviewItemFilterViewActive);

            quickFilterItemList.add(quickFilterItem);
        }
        customViewQuickFilterView.renderFilter(quickFilterItemList);
        customViewQuickFilterView.setListener(new QuickSingleFilterView.ActionListener() {
            @Override
            public void selectFilter(String typeFilter) {
                loadInitialData();
            }
        });
    }

    @Override
    public void loadData(int page) {
        if (page <= INITIAL_PAGE && !customViewQuickFilterView.isAnyItemSelected()) {
            productReviewPresenter.getRatingReview(productId);
            productReviewPresenter.getHelpfulReview(productId);
        }
        productReviewPresenter.getProductReview(productId, page, customViewQuickFilterView.getSelectedFilter());
    }

    @Override
    protected ReviewProductTypeFactoryAdapter getAdapterTypeFactory() {
        return new ReviewProductTypeFactoryAdapter(this);
    }

    @Override
    public void onItemClicked(ReviewProductModel productReviewModel) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productReviewPresenter.detachView();
    }

    @Override
    public void onGoToProfile(String reviewerId, int adapterPosition) {
        if (getActivity().getApplicationContext() instanceof ReputationRouter) {
            startActivity(((ReputationRouter) getActivity().getApplicationContext())
                    .getTopProfileIntent(getActivity(),
                            String.valueOf(reviewerId)));
        }
    }

    @Override
    public void goToPreviewImage(int position, ArrayList<ImageUpload> list) {
        if (MainApplication.getAppContext() instanceof PdpRouter) {
            ArrayList<String> listLocation = new ArrayList<>();
            ArrayList<String> listDesc = new ArrayList<>();

            for (ImageUpload image : list) {
                listLocation.add(image.getPicSrcLarge());
                listDesc.add(image.getDescription());
            }

            ((PdpRouter) MainApplication.getAppContext()).openImagePreview(
                    getActivity(),
                    listLocation,
                    listDesc,
                    position
            );
        }
    }

    @Override
    public void onGoToShopInfo(String shopId) {
        Intent intent = ((ReputationRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), String.valueOf(shopId));
        startActivity(intent);
    }

    @Override
    public void onDeleteReviewResponse(ReviewProductModelContent element, int adapterPosition) {
        productReviewPresenter.deleteReview(element.getReviewId(), element.getReputationId(), productId);
    }

    @Override
    public void onSmoothScrollToReplyView(int adapterPosition) {
        getRecyclerView(getView()).smoothScrollToPosition(adapterPosition);
    }

    @Override
    public void onGoToReportReview(String shopId, String reviewId, int adapterPosition) {
        startActivity(InboxReputationReportActivity.getCallingIntent(
                getActivity(),
                Integer.valueOf(shopId),
                reviewId));
    }

    @Override
    public void onGetListReviewProduct(List<ReviewProductModel> map, boolean isHasNextPage) {
        if (isLoadingInitialData && !customViewQuickFilterView.isAnyItemSelected()) {
            map.add(0, new ReviewProductModelTitleHeader(getString(R.string.product_review_label_all_review)));
            if (listReviewHelpful != null) {
                for (int i = 0; i < listReviewHelpful.size(); i++) {
                    map.add(i, listReviewHelpful.get(i));
                }
            }
        }
        renderList(map, isHasNextPage);
    }

    @Override
    public void onErrorGetListReviewProduct(Throwable e) {
        showGetListError(e);
    }

    @Override
    public void onGetListReviewHelpful(List<ReviewProductModel> map) {
        if (map.size() > 0) {
            setListReviewHelpful(map);
        }
    }

    private void setListReviewHelpful(List<ReviewProductModel> map) {
        map.add(0, new ReviewProductModelTitleHeader(getString(R.string.product_review_label_helpful_review)));
        for (int i = 0; i < map.size(); i++) {
            getAdapter().addElement(i, map.get(i));
        }
        listReviewHelpful = map;
    }

    @Override
    public void onErrorGetListReviewHelpful(Throwable e) {

    }

    @Override
    public void onGetRatingReview(DataResponseReviewStarCount dataResponseReviewStarCount) {
        ratingProduct.setText(dataResponseReviewStarCount.getRatingScore());
        ratingProductStar.setRating(Float.parseFloat(dataResponseReviewStarCount.getRatingScore()));
        counterReview.setText(getString(R.string.product_review_counter_review_formatted, dataResponseReviewStarCount.getTotalReview()));
        for (DetailReviewStarCount detailReviewStarCount : dataResponseReviewStarCount.getDetail()) {
            Float percentageFloatReview = Float.parseFloat(detailReviewStarCount.getPercentage().replace("%", "").replace(",", "."));
            switch (detailReviewStarCount.getRate()) {
                case 5:
                    fiveStarReview.setPercentageProgress(percentageFloatReview);
                    fiveStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 4:
                    fourStarReview.setPercentageProgress(percentageFloatReview);
                    fourStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 3:
                    threeStarReview.setPercentageProgress(percentageFloatReview);
                    threeStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 2:
                    twoStarReview.setPercentageProgress(percentageFloatReview);
                    twoStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 1:
                    oneStarReview.setPercentageProgress(percentageFloatReview);
                    oneStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
            }
        }
    }

    @NonNull
    @Override
    protected BaseListAdapter<ReviewProductModel, ReviewProductTypeFactoryAdapter> createAdapterInstance() {
        return new ReviewProductAdapter<>(getAdapterTypeFactory());
    }

    @Override
    public void onErrorGetRatingView(Throwable e) {

    }

    @Override
    public void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain, String reviewId) {
        ((ReviewProductAdapter) getAdapter()).updateLikeStatus(likeDislikeDomain.getLikeStatus(),
                likeDislikeDomain.getTotalLike(), reviewId);
    }

    @Override
    public void onErrorPostLikeDislike(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain, String reviewId) {
        ((ReviewProductAdapter) getAdapter()).updateDeleteReview(reviewId);
    }

    @Override
    public void onErrorDeleteReview(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onLikeDislikePressed(String reviewId, int likeStatus, String productId, boolean status, int adapterPosition) {
        productReviewPresenter.postLikeDislikeReview(reviewId, likeStatus, productId);
    }

    @Override
    public void onMenuClicked(int adapterPosition) {

    }

    @Override
    public void onSeeReplied(int adapterPosition) {

    }

    @Override
    public void showProgressLoading() {
        progressDialog.show();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }
}
