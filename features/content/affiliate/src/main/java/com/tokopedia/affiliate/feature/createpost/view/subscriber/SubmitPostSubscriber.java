package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
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

    }

    @Override
    public void onNext(SubmitPostData submitPostData) {
        view.hideLoading();

    }
}
