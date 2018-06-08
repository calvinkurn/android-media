package com.tokopedia.settingbank.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingbank.domain.usecase.GetBankListUseCase
import com.tokopedia.settingbank.view.listener.SettingBankContract
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankPresenter(val userSession: UserSession,
                           val getBankAccountUseCase: GetBankListUseCase) :
        SettingBankContract.Presenter,
        BaseDaggerPresenter<SettingBankContract.View>() {


    override fun detachView() {
        super.detachView()
        getBankAccountUseCase.unsubscribe()
    }

}