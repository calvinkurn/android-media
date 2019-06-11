package com.tokopedia.profilecompletion.addname.di

import android.content.Context
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profilecompletion.addname.presenter.AddNamePresenter
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 06/05/19.
 */
class DependencyInjector() {
    fun injectPresenter(context: Context): AddNamePresenter {
        val userSession =  UserSession(context)
        return AddNamePresenter(
                RegisterUseCase((context.resources),
                        GraphqlUseCase(),
                        userSession)
        )

    }

}