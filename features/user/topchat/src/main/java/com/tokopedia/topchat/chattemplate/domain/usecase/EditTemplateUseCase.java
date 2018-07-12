package com.tokopedia.topchat.chattemplate.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository;

import rx.Observable;

/**
 * Created by stevenfredian on 12/27/17.
 */

public class EditTemplateUseCase extends UseCase<EditTemplateViewModel>{

    private final EditTemplateRepository templateRepository;

    public EditTemplateUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EditTemplateRepository templateRepository) {
        super(threadExecutor, postExecutionThread);
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<EditTemplateViewModel> createObservable(RequestParams requestParams) {
        return templateRepository.editTemplate(requestParams.getInt("index",0), requestParams.getParameters());
    }

    public static RequestParams generateParam(int index, String value) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("value", value);
        requestParams.putInt("index", index);
        return requestParams;
    }
}
