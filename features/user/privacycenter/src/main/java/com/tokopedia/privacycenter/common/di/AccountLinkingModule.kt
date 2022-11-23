package com.tokopedia.privacycenter.common.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.accountlinking.LinkAccountTracker
import dagger.Module
import dagger.Provides

@Module
class AccountLinkingModule {

    @Provides
    @ActivityScope
    fun provideLinkAccountTracker(): LinkAccountTracker {
        return LinkAccountTracker()
    }

}
