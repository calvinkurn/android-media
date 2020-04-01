package com.tokopedia.topchat.chattemplate.domain.usecase;


import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 12/27/17.
 */

public class CreateTemplateUseCase extends UseCase<EditTemplateUiModel> {

    private final EditTemplateRepository templateRepository;

    @Inject
    public CreateTemplateUseCase(EditTemplateRepository templateRepository) {
        super();
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<EditTemplateUiModel> createObservable(RequestParams requestParams) {
        return templateRepository.createTemplate(requestParams.getParameters());
    }

    public static RequestParams generateParam(String value, Boolean isSeller) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean("is_seller", isSeller);
        requestParams.putString("value", value);
        return requestParams;
    }
}
