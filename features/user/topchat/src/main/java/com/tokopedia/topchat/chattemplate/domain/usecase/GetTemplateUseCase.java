package com.tokopedia.topchat.chattemplate.domain.usecase;

import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class GetTemplateUseCase extends UseCase<GetTemplateUiModel> {

    private final TemplateRepository templateRepository;

    @Inject
    public GetTemplateUseCase(TemplateRepository templateRepository) {
        super();
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<GetTemplateUiModel> createObservable(RequestParams requestParams) {
        return templateRepository.getTemplate(requestParams.getParameters());
    }

    public static RequestParams generateParam(Boolean isSeller) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean("is_seller", isSeller);
        return requestParams;
    }
}
