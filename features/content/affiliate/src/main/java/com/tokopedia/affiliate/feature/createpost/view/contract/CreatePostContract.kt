package com.tokopedia.affiliate.feature.createpost.view.contract

import android.content.Context

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.affiliate.feature.createpost.view.type.ShareType
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.twitter_share.TwitterAuthenticator

/**
 * @author by milhamj on 9/26/18.
 */
interface CreatePostContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoading()

        fun hideLoading()

        fun onSuccessGetContentForm(feedContentForm: FeedContentForm)

        fun onErrorGetContentForm(message: String)

        fun onErrorNoQuota()

        fun onGetAvailableShareTypeList(typeList: List<ShareType>)

        fun onAuthenticateTwitter(authenticator: TwitterAuthenticator)

        fun changeShareHeaderText(text: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun fetchContentForm(idList: MutableList<String>, type: String)

        fun shouldGetShareOptions()

        fun onShareButtonClicked(type: ShareType, isChecked: Boolean)

        fun postContentToOtherService(viewModel: CreatePostViewModel)
    }
}
