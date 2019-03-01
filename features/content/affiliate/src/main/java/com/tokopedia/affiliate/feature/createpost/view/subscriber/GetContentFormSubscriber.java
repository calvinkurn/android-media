package com.tokopedia.affiliate.feature.createpost.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.ContentFormData;
import com.tokopedia.affiliate.feature.createpost.domain.entity.GetContentFormDomain;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;

import rx.Subscriber;

/**
 * @author by milhamj on 9/26/18.
 */
public class GetContentFormSubscriber extends Subscriber<GetContentFormDomain> {

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
        if (e != null && e.getLocalizedMessage().contains(
                view.getContext().getString(R.string.error_default_non_affiliate))) {
            view.onErrorNotAffiliate();
        } else {
            view.onErrorGetContentForm(
                    ErrorHandler.getErrorMessage(view.getContext(), e)
            );
        }
    }

    @Override
    public void onNext(GetContentFormDomain domain) {
        ContentFormData data = domain.getContentFormData();
        if (data == null || data.getFeedContentForm() == null || data.getAffiliateCheck() == null) {
            onError(new RuntimeException());
            return;
        }
        if (!data.getAffiliateCheck().isIsAffiliate()) {
            view.onErrorNotAffiliate();
            return;
        }

        CheckQuotaQuery checkQuotaQuery = domain.getCheckQuotaQuery();
        if (checkQuotaQuery == null || checkQuotaQuery.getData() == null) {
            onError(new RuntimeException());
            return;
        }
        if (checkQuotaQuery.getData().getNumber() == 0) {
            view.onErrorNoQuota();
            return;
        }

        view.hideLoading();
        if (!TextUtils.isEmpty(data.getFeedContentForm().getError())) {
            view.onErrorGetContentForm(data.getFeedContentForm().getError());
            return;
        }
        view.onSuccessGetContentForm(data.getFeedContentForm());
    }
}
