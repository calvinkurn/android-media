package com.tokopedia.kol.feature.postdetail.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;

import java.util.List;

/**
 * @author by milhamj on 27/07/18.
 */

public interface KolPostDetailContract {
    interface View extends CustomerView {
        Context getContext();

        void showLoading();

        void dismissLoading();

        void onSuccessGetKolPostDetail(List<Visitable> list);

        void onErrorGetKolPostDetail(String message);

        void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber);

        void onSuccessFollowUnfollowKol(int rowNumber);

        void stopTrace();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getCommentFirstTime(int id);

        void followKol(int id, int rowNumber);

        void unfollowKol(int id, int rowNumber);

        void likeKol(int id, int rowNumber, KolPostListener.View.Like likeListener);

        void unlikeKol(int id, int rowNumber, KolPostListener.View.Like likeListener);
    }
}
