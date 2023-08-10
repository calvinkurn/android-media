package com.tokopedia.content.common.di

import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPrefImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on May 26, 2023
 */
@Module
abstract class ContentCoachMarkSharedPrefModule {

    @Binds
    abstract fun bindContentCoachMarkSharedPref(
        contentCoachMarkSharedPref: ContentCoachMarkSharedPrefImpl
    ): ContentCoachMarkSharedPref
}
