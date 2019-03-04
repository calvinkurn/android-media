package com.tokopedia.digital_deals.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.postusecase.PostUpdateDealLikesUseCase;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.model.Rating;
import com.tokopedia.digital_deals.view.model.response.LikeUpdateModel;
import com.tokopedia.digital_deals.view.model.response.LikeUpdateResult;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DealCategoryAdapterPresenter extends BaseDaggerPresenter<DealCategoryAdapterContract.View>
        implements DealCategoryAdapterContract.Presenter {

    private PostUpdateDealLikesUseCase postUpdateDealLikesUseCase;
    private UserSessionInterface userSession;

    @Inject
    DealCategoryAdapterPresenter(PostUpdateDealLikesUseCase postUpdateDealLikesUseCase) {
        this.postUpdateDealLikesUseCase = postUpdateDealLikesUseCase;
    }

    public void initialize() {
        this.userSession = new UserSession(getView().getActivity());

    }

    @Override
    public void onDestroy() {
        postUpdateDealLikesUseCase.unsubscribe();
    }

    public boolean setDealLike(int id, boolean liked, final int position, int likes) {
        if (userSession.isLoggedIn()) {
            LikeUpdateModel requestModel = new LikeUpdateModel();
            Rating rating = new Rating();
            if (liked) {
                rating.setIsLiked("false");
            } else {
                rating.setIsLiked("true");
            }
            rating.setUserId(Integer.parseInt(userSession.getUserId()));
            rating.setProductId(id);
            rating.setFeedback("");
            requestModel.setRating(rating);
            com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
            requestParams.putObject(PostUpdateDealLikesUseCase.REQUEST_BODY, requestModel);
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
                    LikeUpdateResult updateResult = (LikeUpdateResult) dataResponse.getData();
                    if (updateResult.isLiked()) {
                        if (!(Utils.getSingletonInstance().containsLikedEvent(updateResult.getProductId()) > 0)) {
                            Utils.getSingletonInstance().addLikedEvent(id, likes);
                        }
                    } else {
                        if (Utils.getSingletonInstance().containsLikedEvent(updateResult.getProductId()) > 0) {
                            Utils.getSingletonInstance().removeLikedEvent(updateResult.getProductId());
                        }
                    }

                }
            });
            return true;
        } else {
            getView().showLoginSnackbar("Please Login to like deals", position);
            return false;
        }

    }
}
