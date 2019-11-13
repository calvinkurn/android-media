package com.tokopedia.kol.feature.post.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

/**
 * @author by milhamj on 19/02/18.
 */

public interface KolPostListener {
    interface View extends CustomerView {
        Context getContext();

        AbstractionRouter getAbstractionRouter();

        UserSessionInterface getUserSession();

        void showLoading();

        void hideLoading();

        void onSuccessGetProfileData(List<Visitable> visitableList);

        void onEmptyKolPost();

        void onErrorGetProfileData(String message);

        void updateCursor(String lastCursor);

        void onSuccessDeletePost(int rowNumber);

        void onErrorDeletePost(String message, int rowNumber, int id);

    }

    interface Presenter extends CustomerPresenter<View> {
        void initView(String userId);

        void getKolPost(String userId);

        void updateCursor(String lastCursor);

        void followKol(int id, int rowNumber, View kolListener);

        void unfollowKol(int id, int rowNumber, View kolListener);

        void likeKol(int id, int rowNumber, KolPostLikeListener likeListener);

        void unlikeKol(int id, int rowNumber, KolPostLikeListener likeListener);

        void deletePost(int rowNumber, int id);
    }

}
