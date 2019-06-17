package com.tokopedia.search.result.presentation

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.search.result.presentation.view.listener.FollowActionListener
import com.tokopedia.search.result.presentation.view.listener.SearchProfileListener

interface ProfileListSectionContract {

    interface View : CustomerView {
        fun getBaseAppComponent() : BaseAppComponent
    }

    interface Presenter : CustomerPresenter<View> {
        fun initInjector()
        fun attachSearchProfileListener(searchProfileListener: SearchProfileListener)
        fun attachFollowActionListener(followActionListener: FollowActionListener)
        fun requestProfileListData(query: String, page: Int)
        fun handleFollowAction(adapterPosition: Int,
                               userToFollowId: Int,
                               followedStatus: Boolean)
    }
}