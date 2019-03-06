package com.tokopedia.affiliate.feature.createpost.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import rx.Subscriber

/**
 * @author by milhamj on 9/26/18.
 */
class GetContentFormSubscriber(private val view: CreatePostContract.View)
    : Subscriber<GetContentFormDomain>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e!!.printStackTrace()
        }
        view.hideLoading()
        if (e != null && e.localizedMessage.contains(
                        view.getContext()!!.getString(R.string.error_default_non_affiliate))) {
            view.onErrorNotAffiliate()
        } else {
            view.onErrorGetContentForm(
                    ErrorHandler.getErrorMessage(view.getContext(), e)
            )
        }
    }

    override fun onNext(domain: GetContentFormDomain) {
        val data = domain.feedContentResponse
        if (data?.feedContentForm == null) {
            onError(RuntimeException())
            return
        }

        val checkQuotaQuery = domain.checkQuotaQuery
        if (checkQuotaQuery == null || checkQuotaQuery.data == null) {
            onError(RuntimeException())
            return
        }
        if (checkQuotaQuery.data.number == 0) {
            view.onErrorNoQuota()
            return
        }

        view.hideLoading()
        if (!TextUtils.isEmpty(data.feedContentForm.error)) {
            view.onErrorGetContentForm(data.feedContentForm.error)
            return
        }
        view.onSuccessGetContentForm(data.feedContentForm)
    }
}
