package com.tokopedia.affiliate.feature.dashboard.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 13/09/18.
 */
public interface DashboardListener {

    interface View extends CustomerView {

        Context getContext();

        void showLoading();

        interface ViewHolder {
            void onGoToProfile(String url);

            boolean onDeleteCommentKol(String id, boolean canDeleteComment, int adapterPosition);
        }

        interface SeeAll {
            void onGoToKolComment(int rowNumber, int id);
        }
    }

    interface Presenter extends CustomerPresenter<View> {
    }
}
