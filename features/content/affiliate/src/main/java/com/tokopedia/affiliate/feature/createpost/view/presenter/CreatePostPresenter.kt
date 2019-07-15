package com.tokopedia.affiliate.feature.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.affiliate.feature.createpost.view.type.ShareType
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.twitter_share.TwitterManager
import rx.Subscription
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

    private val twitterSubscription: MutableList<Subscription> = mutableListOf()

    override fun attachView(view: CreatePostContract.View?) {
        super.attachView(view)
        twitterManager.setListener(this)
        invalidateShareOptions()
        postContentToOtherService(CreatePostViewModel(caption = "Jual barang di https://news.detik.com"))
    }

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubscribe()
        twitterSubscription.forEach(Subscription::unsubscribe)
    }

    override fun fetchContentForm(idList: MutableList<String>, type: String) {
        view.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(idList, type),
                GetContentFormSubscriber(view, type)
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

    override fun postContentToOtherService(viewModel: CreatePostViewModel) {
        twitterSubscription.add(
                twitterManager.postTweet(viewModel.caption)
                        .subscribe(
                                { Timber.tag("Post to Twitter").d("Success") },
                                { Timber.tag("Post to Twitter").e(it) }
                        )
        )
    }

    private fun authenticateTwitter() {
        twitterSubscription.add(
                twitterManager.getAuthenticator()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { view?.onAuthenticateTwitter(it) }
        )
    }

    private fun getShareOptions(): List<ShareType> {
        return listOf(
                ShareType.Tokopedia(0),
                ShareType.Twitter(twitterManager.shouldPostToTwitter)
        )
    }

    private fun getShareHeaderText(): String {
        val activeShareOptions = getShareOptions().filter { it.isActivated }
        val shareHeaderText = activeShareOptions
                .map { view?.getContext()?.getString(it.keyRes) }
                .joinToString()

        return if (activeShareOptions.size == 1) "$shareHeaderText aja" else shareHeaderText
    }
}
