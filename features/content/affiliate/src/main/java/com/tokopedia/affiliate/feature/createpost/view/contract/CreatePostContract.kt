package com.tokopedia.affiliate.feature.createpost.view.contract

import android.content.Context

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.affiliate.feature.createpost.domain.entity.FeedDetail

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

        fun onSuccessGetPostEdit(feedDetail: FeedDetail)

        fun onErrorGetPostEdit(e: Throwable?)
    }

    interface Presenter : CustomerPresenter<View> {
        fun fetchContentForm(idList: MutableList<String>, type: String, postId: String)
        fun getFeedDetail(postId: String, isAffiliate: Boolean)
    }
}
