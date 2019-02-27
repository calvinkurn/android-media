package com.tokopedia.affiliate.feature.createpost.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm;

/**
 * @author by milhamj on 9/26/18.
 */
public interface CreatePostContract {
    interface View extends CustomerView {
        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetContentForm(FeedContentForm feedContentForm);

        void onErrorGetContentForm(String message);

        void onErrorNotAffiliate();

        void onErrorNoQuota();
    }
    interface Presenter extends CustomerPresenter<View> {
        void fetchContentForm(String productId, String adId);
    }
}
