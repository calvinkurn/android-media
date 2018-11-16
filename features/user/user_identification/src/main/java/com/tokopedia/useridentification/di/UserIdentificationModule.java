package com.tokopedia.useridentification.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user_identification_common.usecase.GetApprovalStatusUseCase;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;
import com.tokopedia.useridentification.view.presenter.UserIdentificationInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationScope
@Module
public class UserIdentificationModule {

    @UserIdentificationScope
    @Provides
    UserIdentificationInfo.Presenter provideUserIdentificationInfoPresenter(@ApplicationContext Context context,
                                                                            GetApprovalStatusUseCase getApprovalStatusUseCase) {
        return new UserIdentificationInfoPresenter(context, getApprovalStatusUseCase);
    }

    @UserIdentificationScope
    @Provides
    Resources provideResources(@ApplicationContext Context context){
        return context.getResources();
    }
}
