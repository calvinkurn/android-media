package com.tokopedia.pms.proof.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
import com.tokopedia.pms.proof.model.ResponseUploadImageProof;
import com.tokopedia.pms.proof.view.UploadProofPaymentPresenter;
import com.tokopedia.pms.proof.domain.UploadProofPaymentUseCase;

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
    UploadProofPaymentPresenter uploadProofPaymentPresenter(UploadProofPaymentUseCase uploadProofPaymentUseCase) {
        return new UploadProofPaymentPresenter(uploadProofPaymentUseCase, new GraphqlUseCase());
    }

    @UploadProofPaymentScope
    @Provides
    UploadProofPaymentUseCase uploadProofPaymentUseCase(UploadImageUseCase<ResponseUploadImageProof> uploadImageUseCase, @ApplicationContext Context context) {
        return new UploadProofPaymentUseCase(uploadImageUseCase, new GraphqlUseCase(), context.getResources());
    }

    @Provides
    @UploadProofPaymentScope
    UploadImageUseCase<ResponseUploadImageProof> provideUploadImageUseCase(@ImageUploaderQualifier UploadImageRepository uploadImageRepository,
                                                                           @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
                                                                           @ImageUploaderQualifier Gson gson,
                                                                           @ImageUploaderQualifier UserSession userSession,
                                                                           @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils) {
        return new UploadImageUseCase<>(uploadImageRepository, generateHostRepository, gson, userSession, ResponseUploadImageProof.class, imageUploaderUtils);
    }
}
