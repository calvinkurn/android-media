package com.tokopedia.pms.proof.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.pms.proof.domain.UploadPaymentProofUseCase;
import com.tokopedia.pms.proof.view.UploadProofPaymentPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@Module(includes = ImageUploaderModule.class)
public class UploadProofPaymentModule {

    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @UploadProofPaymentScope
    @Provides
    UploadProofPaymentPresenter uploadProofPaymentPresenter(UploadPaymentProofUseCase uploadPaymentProofUseCase) {
        return new UploadProofPaymentPresenter(uploadPaymentProofUseCase);
    }

}
