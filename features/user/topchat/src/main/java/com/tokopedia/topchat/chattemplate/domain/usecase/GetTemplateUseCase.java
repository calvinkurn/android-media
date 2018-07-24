package com.tokopedia.topchat.chattemplate.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class GetTemplateUseCase extends UseCase<GetTemplateViewModel> {

    private final TemplateRepository templateRepository;

    public GetTemplateUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TemplateRepository templateRepository) {
        super(threadExecutor, postExecutionThread);
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<GetTemplateViewModel> createObservable(RequestParams requestParams) {
        return templateRepository.getTemplate(requestParams.getParameters());
    }

    public static RequestParams generateParam() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
