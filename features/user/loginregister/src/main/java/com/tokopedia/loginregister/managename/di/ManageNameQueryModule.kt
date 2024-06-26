package com.tokopedia.loginregister.managename.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.managename.constant.ManageNameConstants
import com.tokopedia.loginregister.managename.di.scope.ManageNameContext
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 04/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
class ManageNameQueryModule {
    @Provides
    @ManageNameScope
    @Named(ManageNameConstants.Query.UPDATE_NAME_QUERY)
    fun provideRawQueryUpdateName(@ManageNameContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_update_name)
}
