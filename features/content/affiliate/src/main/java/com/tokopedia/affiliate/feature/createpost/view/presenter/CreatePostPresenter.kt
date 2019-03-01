package com.tokopedia.affiliate.feature.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class CreatePostPresenter @Inject constructor(
        private val getContentFormUseCase: GetContentFormUseCase)
    : BaseDaggerPresenter<CreatePostContract.View>(), CreatePostContract.Presenter {

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubscribe()
    }

    override fun fetchContentForm(idList: MutableList<String>) {
        view.showLoading()
        //TODO milhamj
//        getContentFormUseCase.execute(
//                GetContentFormUseCase.createRequestParams(productId, adId),
//                GetContentFormSubscriber(view)
//        )
    }
}
