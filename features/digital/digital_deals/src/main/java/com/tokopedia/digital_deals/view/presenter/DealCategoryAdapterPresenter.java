package com.tokopedia.digital_deals.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital_deals.view.model.response.LikeUpdateResult;
import com.tokopedia.digital_deals.domain.postusecase.PostUpdateDealLikesUseCase;
import com.tokopedia.digital_deals.view.model.response.LikeUpdateModel;
import com.tokopedia.digital_deals.view.model.Rating;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.ProductItem;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DealCategoryAdapterPresenter extends BaseDaggerPresenter<DealCategoryAdapterContract.View>
        implements DealCategoryAdapterContract.Presenter {

    private PostUpdateDealLikesUseCase postUpdateDealLikesUseCase;

    @Inject
    public DealCategoryAdapterPresenter(PostUpdateDealLikesUseCase postUpdateDealLikesUseCase) {
        this.postUpdateDealLikesUseCase = postUpdateDealLikesUseCase;
    }

    @Override
    public void onDestroy() {
        postUpdateDealLikesUseCase.unsubscribe();
    }

    public boolean setDealLike(final ProductItem model, final int position) {
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
            postUpdateDealLikesUseCase.setRequestParams(requestParams);
            postUpdateDealLikesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                }
            });
            return true;
        } else {
            getView().showLoginSnackbar("Please Login to like deals");
            return false;
        }

    }

    public void setDealLike(final DealsDetailsResponse model, final int position) {
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
            postUpdateDealLikesUseCase.setRequestParams(requestParams);
            postUpdateDealLikesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                    Type token = new TypeToken<DataResponse<LikeUpdateResult>>() {
                    }.getType();
                    RestResponse restResponse = typeRestResponseMap.get(token);
                    DataResponse dataResponse = restResponse.getData();
                    LikeUpdateResult likeUpdateResult = (LikeUpdateResult) dataResponse.getData();
                    model.setIsLiked(likeUpdateResult.isLiked());
                    if (likeUpdateResult.isLiked())
                        model.setLikes(model.getLikes() + 1);
                    else
                        model.setLikes(model.getLikes() - 1);
                    getView().notifyDataSetChanged(position);
                }
            });
        } else {
            getView().showLoginSnackbar("Please Login to like or share deals");
        }
    }
}
