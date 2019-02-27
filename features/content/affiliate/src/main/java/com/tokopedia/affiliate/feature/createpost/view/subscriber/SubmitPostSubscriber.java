package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData;

import rx.Subscriber;

/**
 * @author by milhamj on 10/2/18.
 */
public class SubmitPostSubscriber extends Subscriber<SubmitPostData> {

    private final CreatePostContract.View view;

    public SubmitPostSubscriber(CreatePostContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        view.hideLoading();
        showError(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(SubmitPostData submitPostData) {
        view.hideLoading();
        if (submitPostData == null
                || submitPostData.getFeedContentSubmit().getSuccess() != SubmitPostData.SUCCESS) {
            onError(new RuntimeException());
            return;
        } else if (!TextUtils.isEmpty(submitPostData.getFeedContentSubmit().getError())) {
            showError(submitPostData.getFeedContentSubmit().getError());
            return;
        }
        view.onSuccessSubmitPost();
    }

    private void showError(String message) {
        view.onErrorSubmitPost(message);
    }
}
