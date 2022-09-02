package com.tokopedia.shopadmin.feature.invitationconfirmation.di.module

import com.tokopedia.shopadmin.feature.invitationconfirmation.di.scope.AdminInvitationConfirmationScope
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParamImpl
import dagger.Module
import dagger.Provides

@Module(includes = [AdminInvitationConfirmationViewModelModule::class])
class AdminInvitationConfirmationModule {

    @AdminInvitationConfirmationScope
    @Provides
    fun provideInvitationConfirmationParam(): InvitationConfirmationParam {
        return InvitationConfirmationParamImpl()
    }
}