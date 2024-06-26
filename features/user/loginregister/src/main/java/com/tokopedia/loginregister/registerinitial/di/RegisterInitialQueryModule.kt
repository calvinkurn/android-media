package com.tokopedia.loginregister.registerinitial.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.loginregister.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-10-17.
 * ade.hadian@tokopedia.com
 */

@Module
class RegisterInitialQueryModule {
    companion object {
        private const val MUTATION_ACTIVATE_USER = "activate_user"
    }

    @Provides
    @IntoMap
    @StringKey(MUTATION_ACTIVATE_USER)
    fun provideRawMutationActivateUser(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_activate_user)
}
