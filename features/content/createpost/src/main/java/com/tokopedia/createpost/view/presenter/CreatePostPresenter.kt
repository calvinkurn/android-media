package com.tokopedia.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.usecase.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by milhamj on 9/26/18.
 */
class CreatePostPresenter @Inject constructor(
    private val getContentFormUseCase: UseCase<GetContentFormDomain>,
) : BaseDaggerPresenter<CreatePostContract.View>(), CreatePostContract.Presenter, CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun attachView(view: CreatePostContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubscribe()
    }

    override fun fetchContentForm(idList: MutableList<String>, type: String, postId: String) {
        view?.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(idList, type, postId),
                GetContentFormSubscriber(view, type, null)
        )
    }
}
