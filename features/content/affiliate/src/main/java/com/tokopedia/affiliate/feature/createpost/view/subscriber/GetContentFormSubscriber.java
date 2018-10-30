package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.ContentFormData;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by milhamj on 9/26/18.
 */
public class GetContentFormSubscriber extends Subscriber<GraphqlResponse> {

    private CreatePostContract.View view;
    private boolean isEdit = false;

    public GetContentFormSubscriber(CreatePostContract.View view) {
       this.view = view;
    }

    public GetContentFormSubscriber(CreatePostContract.View view, boolean isEdit) {
        this.view = view;
        this.isEdit = isEdit;
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
        if (e != null && e.getLocalizedMessage().contains(
                view.getContext().getString(R.string.error_default_non_affiliate))) {
            view.onErrorNotAffiliate();
        } else if (!isEdit){
            view.onErrorGetContentForm(
                    ErrorHandler.getErrorMessage(view.getContext(), e)
            );
        } else {
            view.onErrorGetEditContentForm(
                    ErrorHandler.getErrorMessage(view.getContext(), e)
            );
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoading();
        ContentFormData data = graphqlResponse.getData(ContentFormData.class);
        if (data == null || data.getFeedContentForm() == null || data.getAffiliateCheck() == null) {
            onError(new RuntimeException());
            return;
        }
        if (!data.getAffiliateCheck().isIsAffiliate()) {
            view.onErrorNotAffiliate();
            return;
        }

        if (!isEdit) {
            CheckQuotaQuery checkQuotaQuery = graphqlResponse.getData(CheckQuotaQuery.class);
            if (checkQuotaQuery == null || checkQuotaQuery.getData() == null) {
                onError(new RuntimeException());
                return;
            }
            if (checkQuotaQuery.getData().getNumber() == 0) {
                view.onErrorNoQuota();
                return;
            }

        }

        if (!TextUtils.isEmpty(data.getFeedContentForm().getError())) {
            view.onErrorGetContentForm(data.getFeedContentForm().getError());
            return;
        }
        view.onSuccessGetContentForm(data.getFeedContentForm());
    }
}
