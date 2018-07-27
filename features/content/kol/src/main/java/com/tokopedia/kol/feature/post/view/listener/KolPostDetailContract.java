package com.tokopedia.kol.feature.post.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by milhamj on 27/07/18.
 */

public interface KolPostDetailContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {
        void getCommentFirstTime(int id);

        void followKol(int id, int rowNumber, KolPostListener.View kolListener);

        void unfollowKol(int id, int rowNumber, KolPostListener.View kolListener);

        void likeKol(int id, int rowNumber, KolPostListener.View.Like likeListener);

        void unlikeKol(int id, int rowNumber, KolPostListener.View.Like likeListener);
    }
}
