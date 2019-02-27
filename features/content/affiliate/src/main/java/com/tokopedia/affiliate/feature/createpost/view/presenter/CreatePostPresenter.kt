package com.tokopedia.affiliate.feature.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.domain.usecase.SubmitPostUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class CreatePostPresenter @Inject constructor(
        private val getContentFormUseCase: GetContentFormUseCase,
        private val submitPostUseCase: SubmitPostUseCase) : BaseDaggerPresenter<CreatePostContract.View>(), CreatePostContract.Presenter {

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubcribe()
        submitPostUseCase.unsubscribe()
    }

    override fun fetchContentForm(productId: String, adId: String) {
        view.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(productId, adId),
                GetContentFormSubscriber(view)
        )
    }
}
