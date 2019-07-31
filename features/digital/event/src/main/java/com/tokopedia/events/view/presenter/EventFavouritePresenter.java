package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetUserLikesUseCase;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventFavouriteContract;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 16/05/18.
 */

public class EventFavouritePresenter extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventFavouriteContract.EventFavoritePresenter {

    private GetUserLikesUseCase mGetUserLikesCase;
    private PostUpdateEventLikesUseCase postUpdateEventLikesUseCase;
    private List<CategoryItemsViewModel> favouriteItemList;
    private EventFavouriteContract.EventFavouriteView mView;
    private EventsAnalytics eventsAnalytics;

    public EventFavouritePresenter(GetUserLikesUseCase getUserLikesUseCase,
                                   PostUpdateEventLikesUseCase eventLikesUseCase, EventsAnalytics eventsAnalytics) {
        this.mGetUserLikesCase = getUserLikesUseCase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
        this.eventsAnalytics = eventsAnalytics;
    }

    private void getFavourites() {
        Intent inIntent = mView.getActivity().getIntent();
        favouriteItemList = inIntent.getParcelableArrayListExtra(Utils.Constants.FAVOURITEDATA);
        if (favouriteItemList != null && favouriteItemList.size() > 0)
            mView.renderFavourites(favouriteItemList);
        else
            mView.toggleEmptyLayout(View.VISIBLE);
    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventFavouriteContract.EventFavouriteView) view;
        getFavourites();
    }

    @Override
    public void removeEventLike(final CategoryItemsViewModel model, final int position) {
        Utils.getSingletonInstance().removeLikedEvent(model.getId());
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
        if (Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
            Utils.getSingletonInstance().setEventLike(mView.getActivity(), model, postUpdateEventLikesUseCase, subscriber);
        }
    }

    @Override
    public void shareEvent(CategoryItemsViewModel model) {
        Utils.getSingletonInstance().shareEvent(mView.getActivity(), model.getTitle(), model.getSeoUrl(), model.getImageWeb());
    }

    @Override
    public String getSCREEN_NAME() {
        return null;
    }


    @Override
    public boolean onClickOptionMenu(int id) {
        mView.getActivity().onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {

    }

    @Override
    public void onDestroy() {

    }
}
