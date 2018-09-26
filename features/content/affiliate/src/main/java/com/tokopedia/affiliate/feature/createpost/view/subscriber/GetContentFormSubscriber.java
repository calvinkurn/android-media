package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import android.util.Log;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.affiliate.feature.createpost.data.pojo.ContentFormData;
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
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        ContentFormData data = graphqlResponse.getData(ContentFormData.class);
        Log.d("milhamj", data.getFeedContentForm().getError());
    }
}
