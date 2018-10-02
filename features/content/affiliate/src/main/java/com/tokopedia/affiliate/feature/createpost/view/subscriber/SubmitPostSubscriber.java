package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.createpost.data.pojo.submitpost.response.SubmitPostData;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;

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
        view.onErrorSubmitPost(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(SubmitPostData submitPostData) {
        view.hideLoading();
        if (submitPostData == null || submitPostData.getFeedContentSubmit() == null) {
            throw new RuntimeException();
        } else if (!TextUtils.isEmpty(submitPostData.getFeedContentSubmit().getError())) {
            view.onErrorSubmitPost(submitPostData.getFeedContentSubmit().getError());
            return;
        }
        view.onSuccessSubmitPost();
    }
}
