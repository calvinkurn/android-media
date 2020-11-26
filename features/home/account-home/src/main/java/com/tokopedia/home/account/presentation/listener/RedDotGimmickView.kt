package com.tokopedia.home.account.presentation.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

/**
 * @author hadi putra
 */
interface RedDotGimmickView : CustomerView {
    fun onSuccessSendNotif()
    fun onErrorSendNotif(throwable: Throwable)
}