package com.tokopedia.settingbank.addeditaccount.di

import android.content.Context
import com.tokopedia.settingbank.addeditaccount.view.presenter.AddEditBankPresenter
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 6/22/18.
 */

class AddEditBankDependencyInjector {

    object Companion {
        fun inject(context: Context): AddEditBankPresenter {
            val session = UserSession(context)

            return AddEditBankPresenter(session)
        }
    }
}