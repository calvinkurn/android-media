package com.tokopedia.tkpdcontent.feature.profile.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;

/**
 * @author by milhamj on 19/02/18.
 */

public interface KolPostListener {
    interface View extends CustomerView {
        void onGoToKolProfile(int page, int rowNumber, String url);

        void onOpenKolTooltip(int page, int rowNumber, String url);

        void onFollowKolClicked(int page, int rowNumber, int id);

        void onUnfollowKolClicked(int page, int rowNumber, int id);

        void onLikeKolClicked(int page, int rowNumber, int id);

        void onUnlikeKolClicked(int page, int adapterPosition, int id);

        void onGoToKolComment(int page, int rowNumber, KolViewModel kolViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initView();

        void getKolPost();

        void followKol(int id, int rowNumber, View kolListener);

        void unfollowKol(int id, int rowNumber, View kolListener);

        void likeKol(int id, int rowNumber, View kolListener);

        void unlikeKol(int id, int rowNumber, View kolListener);
    }
}
