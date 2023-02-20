package com.tokopedia.createpost.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.config.GlobalConfig
import com.tokopedia.createpost.common.TYPE_AFFILIATE
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import rx.Subscriber

/**
 * @author by milhamj on 9/26/18.
 */
class GetContentFormSubscriber(private val view: CreatePostContract.View?,
                               private val type: String,
                               private val templateToken: String?)
    : Subscriber<GetContentFormDomain>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e!!.printStackTrace()
        }
        view?.hideLoading()
        view?.onErrorGetContentForm(ErrorHandler.getErrorMessage(view.getContext(), e), e)
    }

    override fun onNext(domain: GetContentFormDomain) {
        val data = domain.feedContentResponse
        if (data?.feedContentForm == null) {
            onError(RuntimeException())
            return
        }

        if (type == TYPE_AFFILIATE) {
            handleCheckQuota(domain)
        }

        view?.hideLoading()
        if (!TextUtils.isEmpty(data.feedContentForm.error)) {
            view?.onErrorGetContentForm(data.feedContentForm.error, null)
            return
        }
        view?.onSuccessGetContentForm(data.feedContentForm, !templateToken.isNullOrEmpty())
    }

    private fun handleCheckQuota(domain: GetContentFormDomain) {
        val checkQuotaQuery = domain.checkQuotaQuery
        if (checkQuotaQuery?.data == null) {
            onError(RuntimeException())
            return
        }
        if (checkQuotaQuery.data.number == 0) {
            return
        }
    }
}
