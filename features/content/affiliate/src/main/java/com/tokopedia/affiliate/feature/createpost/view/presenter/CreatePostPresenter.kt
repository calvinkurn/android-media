package com.tokopedia.affiliate.feature.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.data.pojo.productsuggestion.TagItem
import com.tokopedia.affiliate.feature.createpost.domain.entity.FeedDetail
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetFeedForEditUseCase
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetProductSuggestionUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase.Companion.SOURCE_DETAIL
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class CreatePostPresenter @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getContentFormUseCase: GetContentFormUseCase,
        private val getFeedUseCase: GetFeedForEditUseCase,
        private val getProductSuggestionUseCase: GetProductSuggestionUseCase)
    : BaseDaggerPresenter<CreatePostContract.View>(), CreatePostContract.Presenter {

    companion object{
        private const val MESSAGE_POST_NOT_FOUND = "Post tidak ditemukan"
    }

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubscribe()
        getFeedUseCase.unsubscribe()
        getProductSuggestionUseCase.cancelJobs()
    }

    override fun fetchContentForm(idList: MutableList<String>, type: String, postId: String) {
        view.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(idList, type, postId),
                GetContentFormSubscriber(view, type)
        )
    }

    override fun getFeedDetail(postId: String, isAffiliate: Boolean) {
        view?.showLoading()
        getFeedUseCase.execute(GetDynamicFeedUseCase.createRequestParams(
                if (isAffiliate) userSession.userId else userSession.shopId,
                source = SOURCE_DETAIL, sourceId = postId), object : Subscriber<FeedDetail?>() {
            override fun onNext(t: FeedDetail?) {
                if (t == null) onError(Throwable(MESSAGE_POST_NOT_FOUND))
                else view?.onSuccessGetPostEdit(t)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                view?.onErrorGetPostEdit(e)
            }

        })
    }

    override fun fetchProductSuggestion(shopId: String,
                                        onSuccess: (List<TagItem>) -> Unit,
                                        onError: (Throwable) -> Unit) {
        getProductSuggestionUseCase.params =
                GetProductSuggestionUseCase.createRequestParams(shopId.toIntOrZero())
        getProductSuggestionUseCase.execute(onSuccess, onError)
    }
}
