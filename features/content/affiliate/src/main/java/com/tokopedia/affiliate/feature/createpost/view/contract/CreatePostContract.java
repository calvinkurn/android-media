package com.tokopedia.affiliate.feature.createpost.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm;

import java.util.List;

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

        void onSuccessSubmitPost();

        void onErrorSubmitPost(String message);
    }
    interface Presenter extends CustomerPresenter<View> {
        void fetchContentForm(String productId, String adId);

        void submitPost(String productId, String adId, String token, List<String> imageList,
                        int mainImageIndex);
    }
}
