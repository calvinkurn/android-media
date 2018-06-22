package com.tokopedia.digital_deals.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital_deals.domain.postusecase.PostUpdateDealLikesUseCase;
import com.tokopedia.digital_deals.domain.model.LikeUpdateResultDomain;
import com.tokopedia.digital_deals.domain.model.request.likes.LikeUpdateModel;
import com.tokopedia.digital_deals.domain.model.request.likes.Rating;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DealCategoryAdapterPresenter extends BaseDaggerPresenter<DealCategoryAdapterContract.View>
        implements DealCategoryAdapterContract.Presenter {

    PostUpdateDealLikesUseCase postUpdateDealLikesUseCase;


    @Inject
    public DealCategoryAdapterPresenter(PostUpdateDealLikesUseCase postUpdateDealLikesUseCase) {
        this.postUpdateDealLikesUseCase = postUpdateDealLikesUseCase;
    }

    @Override
    public void onDestroy() {
        postUpdateDealLikesUseCase.unsubscribe();
    }

    public void setDealLike(final CategoryItemsViewModel model, final int position) {
        if (SessionHandler.isV4Login(getView().getActivity())) {
            LikeUpdateModel requestModel = new LikeUpdateModel();
            Rating rating = new Rating();
            if (model.isLiked()) {
                rating.setIsLiked("false");
            } else {
                rating.setIsLiked("true");
            }
            rating.setUserId(Integer.parseInt(SessionHandler.getLoginID(getView().getActivity())));
            rating.setProductId(model.getId());
            rating.setFeedback("");
            requestModel.setRating(rating);
            com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
            requestParams.putObject("request_body", requestModel);
            postUpdateDealLikesUseCase.createObservable(requestParams).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LikeUpdateResultDomain>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(LikeUpdateResultDomain likeUpdateResultDomain) {
                            model.setLiked(likeUpdateResultDomain.isLiked());
                            getView().notifyDataSetChanged(position);
                        }
                    });
        } else {
            getView().showLoginSnackbar("Please Login to like or share deals");
        }
    }

    public void setDealLike(final DealsDetailsViewModel model, final int position) {
        if (SessionHandler.isV4Login(getView().getActivity())) {
            LikeUpdateModel requestModel = new LikeUpdateModel();
            Rating rating = new Rating();
            if (model.getIsLiked()) {
                rating.setIsLiked("false");
            } else {
                rating.setIsLiked("true");
            }
            rating.setUserId(Integer.parseInt(SessionHandler.getLoginID(getView().getActivity())));
            rating.setProductId(model.getId());
            rating.setFeedback("");
            requestModel.setRating(rating);
            com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
            requestParams.putObject("request_body", requestModel);
            postUpdateDealLikesUseCase.createObservable(requestParams).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LikeUpdateResultDomain>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(LikeUpdateResultDomain likeUpdateResultDomain) {
                            model.setIsLiked(likeUpdateResultDomain.isLiked());
                            getView().notifyDataSetChanged(position);
                        }
                    });
        } else {
            getView().showLoginSnackbar("Please Login to like or share deals");
        }
    }
}
