package com.tokopedia.affiliate.feature.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.domain.usecase.SubmitPostUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.affiliate.feature.createpost.view.subscriber.SubmitPostSubscriber
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

    override fun submitPost(productId: String, adId: String, token: String, imageList: List<String>,
                            mainImageIndex: Int) {
        view.showLoading()
        submitPostUseCase.execute(
                SubmitPostUseCase.createRequestParams(
                        productId,
                        adId,
                        token,
                        imageList,
                        mainImageIndex
                ),
                SubmitPostSubscriber(view)
        )
    }
}
