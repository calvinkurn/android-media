package com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor;

import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class GenerateHostUseCase extends UseCase<GenerateHostDomain> {

    private static final String PARAM_NEW_ADD = "new_add";
    private ImageUploadRepository imageUploadRepository;

    public GenerateHostUseCase(ImageUploadRepository imageUploadRepository) {
        super();
        this.imageUploadRepository = imageUploadRepository;
    }

    @Override
    public Observable<GenerateHostDomain> createObservable(RequestParams requestParams) {
        return imageUploadRepository.generateHost(requestParams);
    }

    public static RequestParams getParam() {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_NEW_ADD, "2");
        return params;
    }
}
