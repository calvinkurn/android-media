package com.tokopedia.pms.proof.di;

import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.pms.proof.domain.UploadPaymentProofUseCase;
import com.tokopedia.pms.proof.view.UploadProofPaymentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@Module(includes = ImageUploaderModule.class)
public class UploadProofPaymentModule {

    @UploadProofPaymentScope
    @Provides
    UploadProofPaymentPresenter uploadProofPaymentPresenter(UploadPaymentProofUseCase uploadPaymentProofUseCase) {
        return new UploadProofPaymentPresenter(uploadPaymentProofUseCase);
    }

}
