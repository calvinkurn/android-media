package com.tokopedia.createpost.common.view.contract

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentForm

/**
 * @author by milhamj on 9/26/18.
 */
interface CreatePostContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoading()

        fun hideLoading()

        fun onSuccessGetContentForm(feedContentForm: FeedContentForm, isFromTemplateToken: Boolean)

        fun onErrorGetContentForm(message: String, throwable: Throwable?)
    }

    interface Presenter : CustomerPresenter<View> {

        fun fetchContentForm(idList: MutableList<String>, type: String, postId: String)
    }
}
