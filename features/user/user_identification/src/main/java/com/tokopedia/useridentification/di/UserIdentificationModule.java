package com.tokopedia.useridentification.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.domain.usecase.GetApprovalStatusUseCase;
import com.tokopedia.useridentification.domain.usecase.GetUserProjectInfoUseCase;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;
import com.tokopedia.useridentification.view.presenter.UserIdentificationInfoPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationScope
@Module(includes = ImageUploaderModule.class)
public class UserIdentificationModule {

    @UserIdentificationScope
    @Provides
    UserIdentificationInfo.Presenter provideUserIdentificationInfoPresenter(GetUserProjectInfoUseCase getUserProjectInfoUseCase, GetApprovalStatusUseCase getApprovalStatusUseCase) {
        return new UserIdentificationInfoPresenter(getUserProjectInfoUseCase, getApprovalStatusUseCase);
    }

    @UserIdentificationScope
    @Provides
    Resources provideResources(@ApplicationContext Context context){
        return context.getResources();
    }


    @UserIdentificationScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }


    @UserIdentificationScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @UserIdentificationScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
