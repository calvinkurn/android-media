package com.tokopedia.affiliate.feature.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.affiliate.feature.createpost.view.type.ShareType
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.twitter_share.TwitterManager
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class CreatePostPresenter @Inject constructor(
        private val getContentFormUseCase: GetContentFormUseCase,
        private val twitterManager: TwitterManager
) : BaseDaggerPresenter<CreatePostContract.View>(), CreatePostContract.Presenter, TwitterManager.TwitterManagerListener {

    override fun attachView(view: CreatePostContract.View?) {
        super.attachView(view)
        twitterManager.setListener(this)
        shouldChangeShareHeaderText()
    }

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubscribe()
    }

    override fun fetchContentForm(idList: MutableList<String>, type: String) {
        view.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(idList, type),
                GetContentFormSubscriber(view, type)
        )
    }

    override fun onAuthenticationSuccess(oAuthToken: String, oAuthSecret: String) {
        invalidateShareOptions()
    }

    override fun invalidateShareOptions() {
        view?.onGetAvailableShareTypeList(getShareOptions())
    }

    private fun shouldChangeShareHeaderText() {
        view?.changeShareHeaderText(getShareHeaderText())
    }

    override fun onShareButtonClicked(type: ShareType, isChecked: Boolean) {
        if (isChecked) {
            when (type) {
                is ShareType.Twitter -> if (!twitterManager.isAuthenticated) {
                    authenticateTwitter()
                    invalidateShareOptions()
                } else {
                    twitterManager.shouldPostToTwitter = true
                    shouldChangeShareHeaderText()
                }
            }
        }
    }

    override fun postContentToOtherService(viewModel: CreatePostViewModel) {
        twitterManager.postTweet(viewModel.caption, emptyList())
                .subscribe {
                    Timber.tag("Post to Twitter").d("Success")
                }
    }

    private fun authenticateTwitter() {
        twitterManager.getAuthenticator()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.onAuthenticateTwitter(it) }
    }

    private fun getShareOptions(): List<ShareType> {
        return listOf(
                ShareType.Tokopedia(0),
                ShareType.Twitter(twitterManager.isAuthenticated)
        )
    }

    private fun getShareHeaderText(): String {
        val shareOptions = getShareOptions()
        val shareHeaderText = shareOptions
                .filter { it.isActivated }
                .map { view?.getContext()?.getString(it.keyRes) }
                .joinToString()

        return if (shareOptions.size == 1) "$shareHeaderText aja" else shareHeaderText
    }
}
