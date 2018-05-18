package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.domain.GetUserLikesUseCase;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.contractor.EventFavouriteContract;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 16/05/18.
 */

public class EventFavouritePresenter extends BaseDaggerPresenter<EventFavouriteContract.EventFavouriteView>
        implements EventFavouriteContract.Presenter {

    private GetUserLikesUseCase mGetUserLikesCase;
    private PostUpdateEventLikesUseCase postUpdateEventLikesUseCase;
    private List<CategoryItemsViewModel> favouriteItemList;

    @Inject
    public EventFavouritePresenter(GetUserLikesUseCase getUserLikesUseCase,
                                   PostUpdateEventLikesUseCase eventLikesUseCase) {
        this.mGetUserLikesCase = getUserLikesUseCase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
    }

    private void getFavourites() {
        Intent inIntent = getView().getActivity().getIntent();
        favouriteItemList = inIntent.getParcelableArrayListExtra(Utils.Constants.FAVOURITEDATA);
        if (favouriteItemList != null && favouriteItemList.size() > 0)
            getView().renderFavourites(favouriteItemList);
        else
            getView().toggleEmptyLayout(View.VISIBLE);
    }

    @Override
    public void attachView(EventFavouriteContract.EventFavouriteView view) {
        super.attachView(view);
        getFavourites();
    }

    @Override
    public void setEventLike(final CategoryItemsViewModel model, final int position) {
        Subscriber<LikeUpdateResultDomain> subscriber = new Subscriber<LikeUpdateResultDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("UPDATEEVENTLIKE", e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(LikeUpdateResultDomain likeUpdateResultDomain) {
                Log.d("UPDATEEVENTLIKE", "onNext");
            }
        };
        if (SessionHandler.isV4Login(getView().getActivity())) {
//            LikeUpdateModel requestModel = new LikeUpdateModel();
//            //todo set requestmodel values
//            Rating rating = new Rating();
//            if (model.isLiked()) {
//                rating.setIsLiked("false");
//                model.setLiked(false);
//            } else {
//                rating.setIsLiked("true");
//                model.setLiked(true);
//            }
//            rating.setUserId(Integer.parseInt(SessionHandler.getLoginID(getView().getActivity())));
//            rating.setProductId(model.getId());
//            rating.setFeedback("");
//            requestModel.setRating(rating);
//            com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
//            requestParams.putObject("request_body", requestModel);
//            postUpdateEventLikesUseCase.createObservable(requestParams).subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(subscriber);
            Utils.getSingletonInstance().setEventLike(getView().getActivity(), model, postUpdateEventLikesUseCase, subscriber);
        }
    }

    @Override
    public void shareEvent(CategoryItemsViewModel model) {
        Utils.getSingletonInstance().shareEvent(getView().getActivity(), model.getTitle(), model.getSeoUrl());
    }

    @Override
    public String getSCREEN_NAME() {
        return null;
    }


}
