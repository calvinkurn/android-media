package com.tokopedia.feedplus.view.listener

import android.content.Context
import android.content.res.Resources

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem

import java.util.ArrayList

/**
 * @author by nisie on 5/15/17.
 */

interface FeedPlus {

    interface View : CustomerView {

        fun onSearchShopButtonClicked()

        fun onRetryClicked()

        fun onGoToLogin()

    }

    interface Presenter : CustomerPresenter<View> {

    }
}
