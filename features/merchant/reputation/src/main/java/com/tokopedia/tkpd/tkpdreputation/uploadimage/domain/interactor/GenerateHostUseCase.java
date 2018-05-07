package com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;

import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class GenerateHostUseCase extends UseCase<GenerateHostDomain> {

    private static final String PARAM_NEW_ADD = "new_add";
    private ImageUploadRepository imageUploadRepository;

    public GenerateHostUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               ImageUploadRepository imageUploadRepository) {
        super(threadExecutor, postExecutionThread);
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
