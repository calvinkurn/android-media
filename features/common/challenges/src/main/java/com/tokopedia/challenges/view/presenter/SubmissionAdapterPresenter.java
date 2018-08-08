package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.challenges.view.contractor.SubmissionAdapterContract;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import javax.inject.Inject;

public class SubmissionAdapterPresenter extends BaseDaggerPresenter<SubmissionAdapterContract.View>
        implements SubmissionAdapterContract.Presenter {

//    private PostUpdateDealLikesUseCase postUpdateDealLikesUseCase;
    private UserSession userSession;

    @Inject
    public SubmissionAdapterPresenter() {
//        this.postUpdateDealLikesUseCase = postUpdateDealLikesUseCase;
    }

    public void initialize() {
        this.userSession = ((AbstractionRouter) getView().getActivity().getApplication()).getSession();

    }

    @Override
    public void onDestroy() {
//        postUpdateDealLikesUseCase.unsubscribe();
    }

    public boolean setDealLike(final SubmissionResult model, final int position) {
        if (userSession.isLoggedIn()) {
//            LikeUpdateModel requestModel = new LikeUpdateModel();
//            Rating rating = new Rating();
//            if (model.isLiked()) {
//                rating.setIsLiked("false");
//            } else {
//                rating.setIsLiked("true");
//            }
//            rating.setUserId(Integer.parseInt(userSession.getUserId()));
//            rating.setProductId(model.getId());
//            rating.setFeedback("");
//            requestModel.setRating(rating);
//            com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
//            requestParams.putObject(PostUpdateDealLikesUseCase.REQUEST_BODY, requestModel);
//            postUpdateDealLikesUseCase.setRequestParams(requestParams);
//            postUpdateDealLikesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
//                }
//            });
            return true;
        } else {
            getView().showLoginSnackbar("Please Login to like deals", position);
            return false;
        }

    }
}
