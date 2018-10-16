package com.tokopedia.updateinactivephone.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepository;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;

import rx.Observable;

public class GetUploadHostUseCase extends UseCase<UploadHostModel> {

    public static final String PARAM_NEW_ADD = "new_add";
    public static final String DEFAULT_NEW_ADD = "2";


    private final UploadImageRepository uploadImageRepository;

    public GetUploadHostUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<UploadHostModel> createObservable(RequestParams requestParams) {
        return uploadImageRepository.getUploadHost(requestParams.getParameters());
    }
}