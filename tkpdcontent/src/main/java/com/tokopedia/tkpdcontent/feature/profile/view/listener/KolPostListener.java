package com.tokopedia.tkpdcontent.feature.profile.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdcontent.KolRouter;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

import java.util.List;

/**
 * @author by milhamj on 19/02/18.
 */

public interface KolPostListener {
    interface View extends CustomerView {
        Context getContext();

        KolRouter getKolRouter();

        void showLoading();

        void hideLoading();

        void onSuccessGetProfileData(List<Visitable> visitableList);

        void onErrorGetProfileData(String message);

        void updateCursor(String lastCursor);

        void onGoToKolProfile(int page, int rowNumber, String url);

        void onOpenKolTooltip(int page, int rowNumber, String url);

        void onFollowKolClicked(int page, int rowNumber, int id);

        void onUnfollowKolClicked(int page, int rowNumber, int id);

        void onLikeKolClicked(int page, int rowNumber, int id);

        void onUnlikeKolClicked(int page, int adapterPosition, int id);

        void onGoToKolComment(int page, int rowNumber, KolPostViewModel kolPostViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initView(String userId);

        void getKolPost(String userId);

        void updateCursor(String lastCursor);

        void followKol(int id, int rowNumber, View kolListener);

        void unfollowKol(int id, int rowNumber, View kolListener);

        void likeKol(int id, int rowNumber, View kolListener);

        void unlikeKol(int id, int rowNumber, View kolListener);
    }
}
