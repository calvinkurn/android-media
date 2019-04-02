package com.tokopedia.kol.feature.post.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.List;

/**
 * @author by milhamj on 23/08/18.
 */

public interface KolPostShopContract {
    interface View extends KolPostListener.View {

        void onSuccessGetKolPostShop(List<Visitable> visitables, String lastCursor);

        void onErrorGetKolPostShop(String message);

        interface Like {
            Context getContext();

            void onLikeKolSuccess(int rowNumber, int action);

            void onLikeKolError(String message);
        }
    }

    interface Presenter extends CustomerPresenter<View> {
        void initView(String shopId);

        void getKolPostShop(String shopId);

        void updateCursor(String lastCursor);
    }
}
