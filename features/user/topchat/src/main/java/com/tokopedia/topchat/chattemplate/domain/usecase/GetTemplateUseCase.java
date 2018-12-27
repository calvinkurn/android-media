package com.tokopedia.topchat.chattemplate.domain.usecase;

import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class GetTemplateUseCase extends UseCase<GetTemplateViewModel> {

    private final TemplateRepository templateRepository;

    @Inject
    public GetTemplateUseCase(TemplateRepository templateRepository) {
        super();
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
