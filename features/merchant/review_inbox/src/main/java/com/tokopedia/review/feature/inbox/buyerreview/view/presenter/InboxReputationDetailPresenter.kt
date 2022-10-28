package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendSmileyReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetInboxReputationDetailSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.RefreshInboxReputationDetailSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailPresenter @Inject internal constructor(
    private val getInboxReputationDetailUseCase: GetInboxReputationDetailUseCase,
    private val sendSmileyReputationUseCase: SendSmileyReputationUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseDaggerPresenter<InboxReputationDetail.View>(), InboxReputationDetail.Presenter, CoroutineScope {

    private var viewListener: InboxReputationDetail.View? = null

    private val parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext = dispatchers.main + parentJob

    override fun attachView(view: InboxReputationDetail.View?) {
        super.attachView(view)
        viewListener = view
    }

    override fun detachView() {
        super.detachView()
        getInboxReputationDetailUseCase.unsubscribe()
        parentJob.cancelChildren()
    }

    override fun getInboxDetail(id: String, anInt: Int) {
        viewListener?.showLoading()
        getInboxReputationDetailUseCase.execute(
            GetInboxReputationDetailUseCase.getParam(
                id,
                anInt
            ),
            viewListener?.let { GetInboxReputationDetailSubscriber(it) }
        )
    }

    override fun sendSmiley(reputationId: String, score: String, role: String) {
        launchCatchError(block = {
            viewListener?.showLoadingDialog()
            val success = withContext(dispatchers.io) {
                sendSmileyReputationUseCase.execute(
                    SendSmileyReputationUseCase.getParam(reputationId, score, role.toIntOrZero())
                )
            }
            viewListener?.let { viewListener ->
                viewListener.finishLoadingDialog()
                if (success) {
                    viewListener.onSuccessSendSmiley(score.toIntOrZero())
                } else {
                    viewListener.onErrorSendSmiley(viewListener.getErrorMessage())
                }
            }
        }, onError = {
            viewListener?.let { viewListener ->
                viewListener.finishLoadingDialog()
                viewListener.onErrorSendSmiley(viewListener.getErrorMessage(it))
            }
        })
    }

    fun refreshPage(reputationId: String, tab: Int) {
        viewListener?.showRefresh()
        getInboxReputationDetailUseCase.execute(
            GetInboxReputationDetailUseCase.getParam(
                reputationId,
                tab
            ),
            viewListener?.let { RefreshInboxReputationDetailSubscriber(it) }
        )
    }
}