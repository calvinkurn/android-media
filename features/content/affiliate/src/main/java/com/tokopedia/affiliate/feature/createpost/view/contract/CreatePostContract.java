package com.tokopedia.affiliate.feature.createpost.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.feature.createpost.data.pojo.FeedContentForm;

/**
 * @author by milhamj on 9/26/18.
 */
public interface CreatePostContract {
    interface View extends CustomerView {
        void onSuccessGetContentForm(FeedContentForm feedContentForm);

        void onErrorGetContentForm(String message);
    }
    interface Presenter extends CustomerPresenter<View> {
        void fetchContentForm(String productId, String adId);
    }
}
