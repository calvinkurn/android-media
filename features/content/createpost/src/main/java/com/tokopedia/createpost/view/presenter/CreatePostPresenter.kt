package com.tokopedia.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.createpost.domain.entity.FeedDetail
import com.tokopedia.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.createpost.domain.usecase.GetFeedForEditUseCase
import com.tokopedia.createpost.domain.usecase.GetProductSuggestionUseCase
import com.tokopedia.createpost.domain.usecase.GetShopProductSuggestionUseCase
import com.tokopedia.createpost.view.contract.CreatePostContract
import com.tokopedia.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.createpost.view.type.ShareType
import com.tokopedia.createpost.view.viewmodel.ProductSuggestionItem
import com.tokopedia.feedcomponent.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetProfileHeaderUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.twitter_share.TwitterManager
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import rx.Subscriber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by milhamj on 9/26/18.
 */
class CreatePostPresenter @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getContentFormUseCase: GetContentFormUseCase,
        private val getFeedUseCase: GetFeedForEditUseCase,
        private val getProfileHeaderUseCase: GetProfileHeaderUseCase,
        private val twitterManager: TwitterManager,
        private val getProductSuggestionUseCase: GetProductSuggestionUseCase
) : BaseDaggerPresenter<CreatePostContract.View>(), CreatePostContract.Presenter, TwitterManager.TwitterManagerListener, CoroutineScope {

    private var followersCount: Int? = null

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    companion object {
        private const val MESSAGE_POST_NOT_FOUND = "Post tidak ditemukan"
    }

    override fun attachView(view: CreatePostContract.View?) {
        super.attachView(view)
        twitterManager.setListener(this)
        getFollowersCount()
        invalidateShareOptions()
    }

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubscribe()
        getFeedUseCase.unsubscribe()
        getProfileHeaderUseCase.unsubscribe()
        getProductSuggestionUseCase.cancelAllJobs()
    }

    override fun fetchContentForm(idList: MutableList<String>, type: String, postId: String) {
        view.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(idList, type, postId),
                GetContentFormSubscriber(view, type, null)
        )
    }

    override fun fetchContentFormByToken(token: String, type: String) {
        view.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(
                        try {
                            token.decodeToUtf8()
                        } catch (e: Exception) {
                            token
                        },
                        type
                ),
                GetContentFormSubscriber(view, type, token)
        )
    }

    override fun onAuthenticationSuccess(oAuthToken: String, oAuthSecret: String) {
        twitterManager.shouldPostToTwitter = true
        invalidateShareOptions()
    }

    override fun invalidateShareOptions() {
        view?.onGetAvailableShareTypeList(getShareOptions())
        shouldChangeShareHeaderText()
    }

    private fun shouldChangeShareHeaderText() {
        view?.changeShareHeaderText(getShareHeaderText())
    }

    private fun getFollowersCount() {
        getProfileHeaderUseCase.execute(
                GetProfileHeaderUseCase.createRequestParams(userSession.userId.toInt()),
                object : Subscriber<GraphqlResponse>() {
                    override fun onNext(t: GraphqlResponse?) {
                        followersCount = t?.let(::getFollowersCount)
                        invalidateShareOptions()
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.debugTrace()
                    }
                }
        )
    }

    override fun onShareButtonClicked(type: ShareType, isChecked: Boolean) {
        if (isChecked) {
            when (type) {
                is ShareType.Twitter -> if (!twitterManager.isAuthenticated) {
                    authenticateTwitter()
                    invalidateShareOptions()
                } else {
                    twitterManager.shouldPostToTwitter = true
                }
            }
        } else {
            twitterManager.shouldPostToTwitter = false
        }

        shouldChangeShareHeaderText()
    }

    private fun authenticateTwitter() {
        launch {
            view?.onAuthenticateTwitter(
                    twitterManager.getAuthenticator()
            )
        }
    }

    private fun getShareOptions(): List<ShareType> {
        return listOf(
                ShareType.Tokopedia(followersCount),
                ShareType.Twitter(twitterManager.shouldPostToTwitter)
        )
    }

    private fun getShareHeaderText(): String {
        val activeShareOptions = getShareOptions().filter(ShareType::isActivated)
        val context = view?.getContext()
        return if (context == null) "" else activeShareOptions.joinToString { context.getString(it.keyRes) }
    }

    override fun getFeedDetail(postId: String, isAffiliate: Boolean) {
        view?.showLoading()
        getFeedUseCase.execute(GetDynamicFeedUseCase.createRequestParams(
                userId = if (isAffiliate) userSession.userId else userSession.shopId,
                source = GetDynamicFeedUseCase.FeedV2Source.Detail,
                sourceId = postId),
                getFeedDetailSubscriber()
        )
    }

    override fun fetchProductSuggestion(type: String,
                                        onSuccess: (List<ProductSuggestionItem>) -> Unit,
                                        onError: (Throwable) -> Unit) {
        if (type == ProductSuggestionItem.TYPE_SHOP) {
            getProductSuggestionUseCase.params =
                    GetShopProductSuggestionUseCase.createRequestParams(
                            userSession.shopId.toIntOrZero()
                    )
        }
        getProductSuggestionUseCase.type = type
        getProductSuggestionUseCase.execute(onSuccess, onError)
    }

    private fun getFeedDetailSubscriber(): Subscriber<FeedDetail?> {
        return object : Subscriber<FeedDetail?>() {
            override fun onNext(t: FeedDetail?) {
                if (t == null) onError(MessageErrorException(MESSAGE_POST_NOT_FOUND))
                else view?.onSuccessGetPostEdit(t)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                view?.onErrorGetPostEdit(e)
            }

        }
    }

    private fun getFollowersCount(response: GraphqlResponse): Int {
        val data: ProfileHeaderData = response.getData(ProfileHeaderData::class.java)
        return data.bymeProfileHeader.profile.totalFollower.number
    }
}
