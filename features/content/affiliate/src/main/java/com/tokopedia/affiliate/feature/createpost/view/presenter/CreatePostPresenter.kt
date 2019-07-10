package com.tokopedia.affiliate.feature.createpost.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.affiliate.feature.createpost.view.type.ShareType
import com.tokopedia.twitter_share.TwitterManager
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
        view?.onGetAvailableShareTypeList(
                listOf(
                        ShareType.Tokopedia(0),
                        ShareType.Twitter(true)
                )
        )
    }

    override fun shouldGetShareOptions() {
        view?.onGetAvailableShareTypeList(
                listOf(
                        ShareType.Tokopedia(0),
                        ShareType.Twitter(false)
                )
        )
    }

    override fun onShareButtonClicked(type: ShareType, isChecked: Boolean) {
        if (isChecked) {
            when (type) {
                is ShareType.Twitter -> view?.onAuthenticateTwitter(twitterManager.getAuthenticator())
            }
        }
    }
}
