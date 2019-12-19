package com.tokopedia.createpost.view.contract

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.domain.entity.FeedDetail
import com.tokopedia.createpost.view.type.ShareType
import com.tokopedia.createpost.view.viewmodel.ProductSuggestionItem
import com.tokopedia.twitter_share.TwitterAuthenticator

/**
 * @author by milhamj on 9/26/18.
 */
interface CreatePostContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoading()

        fun hideLoading()

        fun onSuccessGetContentForm(feedContentForm: FeedContentForm, isFromTemplateToken: Boolean)

        fun onErrorGetContentForm(message: String)

        fun onErrorNoQuota()

        fun onSuccessGetPostEdit(feedDetail: FeedDetail)

        fun onErrorGetPostEdit(e: Throwable?)

        fun onGetAvailableShareTypeList(typeList: List<ShareType>)

        fun onAuthenticateTwitter(authenticator: TwitterAuthenticator)

        fun changeShareHeaderText(text: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun invalidateShareOptions()

        fun onShareButtonClicked(type: ShareType, isChecked: Boolean)

        fun fetchContentForm(idList: MutableList<String>, type: String, postId: String)

        fun fetchContentFormByToken(token: String, type: String)

        fun getFeedDetail(postId: String, isAffiliate: Boolean)

        fun fetchProductSuggestion(type: String,
                                   onSuccess: (List<ProductSuggestionItem>) -> Unit,
                                   onError: (Throwable) -> Unit)
    }
}
