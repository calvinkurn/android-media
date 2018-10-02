package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.ContentFormData;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by milhamj on 9/26/18.
 */
public class GetContentFormSubscriber extends Subscriber<GraphqlResponse> {

    private CreatePostContract.View view;

    public GetContentFormSubscriber(CreatePostContract.View view) {
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
        view.onErrorGetContentForm(
                ErrorHandler.getErrorMessage(view.getContext(), e)
        );
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoading();
        ContentFormData data = graphqlResponse.getData(ContentFormData.class);
        if (data == null || data.getFeedContentForm() == null) {
            throw new RuntimeException();
        } else if (!TextUtils.isEmpty(data.getFeedContentForm().getError())) {
            view.onErrorGetContentForm(data.getFeedContentForm().getError());
            return;
        }
        view.onSuccessGetContentForm(data.getFeedContentForm());
    }
}
