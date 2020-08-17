package com.tokopedia.pms.proof.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.pms.proof.domain.UploadPaymentProofUseCase;
import com.tokopedia.pms.proof.view.UploadProofPaymentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@UploadProofPaymentScope
@Module(includes = ImageUploaderModule.class)
public class UploadProofPaymentModule {

    @UploadProofPaymentScope
    @Provides
    UploadProofPaymentPresenter uploadProofPaymentPresenter(UploadPaymentProofUseCase uploadPaymentProofUseCase) {
        return new UploadProofPaymentPresenter(new GraphqlUseCase(), uploadPaymentProofUseCase);
    }

  /*  @UploadProofPaymentScope
    @Provides
    UploadProofPaymentUseCase uploadProofPaymentUseCase(UploadImageUseCase<ResponseUploadImageProof> uploadImageUseCase, @ApplicationContext Context context) {
        return new UploadProofPaymentUseCase(uploadImageUseCase, new GraphqlUseCase(), context.getResources());
    }

    @Provides
    @UploadProofPaymentScope
    UploadImageUseCase<ResponseUploadImageProof> provideUploadImageUseCase(@ImageUploaderQualifier UploadImageRepository uploadImageRepository,
                                                                           @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
                                                                           @ImageUploaderQualifier Gson gson,
                                                                           @ImageUploaderQualifier UserSessionInterface userSession,
                                                                           @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils) {
        return new UploadImageUseCase<>(uploadImageRepository, generateHostRepository, gson, userSession, ResponseUploadImageProof.class, imageUploaderUtils);
    }*/
}
