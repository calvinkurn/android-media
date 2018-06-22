package com.tokopedia.settingbank.addeditaccount.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 6/22/18.
 */

interface ChooseBankContract {

    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<AddEditBankContract.View> {

    }
}