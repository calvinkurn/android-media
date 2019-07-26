package com.tokopedia.kol.feature.postdetail.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import java.util.List;

/**
 * @author by milhamj on 27/07/18.
 */

public interface KolPostDetailContract {
    interface View extends CustomerView {
        Context getContext();

        void showLoading();

        void dismissLoading();

        void onSuccessGetKolPostDetail(List<Visitable> list, PostDetailViewModel postDetailViewModel);

        void onErrorGetKolPostDetail(String message);

        void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber);

        void onSuccessFollowUnfollowKol(int rowNumber);

        void stopTrace();

        void onErrorToggleFavoriteShop(String errorMessage, String shopId);

        void onSuccessToggleFavoriteShop();

        void onErrorSendVote(String errorMessage);

        void onSuccessSendVote(int positionInFeed, String optionId, VoteStatisticDomainModel voteStatisticDomainModel);

        void onErrorDeletePost(Throwable e);

        void onSuccessDeletePost(int rowNumber);

        void onEmptyDetailFeed();

        void onEmptyDetailClicked();

        void onAddToCartSuccess();

        void onAddToCartFailed(String pdpAppLink);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getCommentFirstTime(int id);

        void followKol(int id, int rowNumber);

        void unfollowKol(int id, int rowNumber);

        void likeKol(int id, int rowNumber, KolPostListener.View.Like likeListener);

        void unlikeKol(int id, int rowNumber, KolPostListener.View.Like likeListener);

        void toggleFavoriteShop(String shopId);

        void sendVote(int positionInFeed, String pollId, String optionId);

        void trackAffiliate(String clickURL);

        void deletePost(int id, int rowNumber);

        void addPostTagItemToCart(PostTagItem postTagItem);
    }
}
