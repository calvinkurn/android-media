package com.tokopedia.kol.feature.post.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by milhamj on 23/08/18.
 */

public interface KolPostShopContract {
    interface View extends CustomerView {

        interface Like {
            Context getContext();

            void onLikeKolSuccess(int rowNumber);

            void onLikeKolError(String message);
        }

    }

    interface Presenter extends CustomerPresenter<View> {
        void initView(String userId);

        void getKolPostShop(String userId);

        void updateCursor(String lastCursor);
    }
}
