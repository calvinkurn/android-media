package com.tokopedia.topchat.chattemplate.data.mapper;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateChatMapper implements Func1<Response<DataResponse<TemplateData>>, EditTemplateViewModel> {

    @Inject
    public EditTemplateChatMapper() {
    }

    @Override
    public EditTemplateViewModel call(Response<DataResponse<TemplateData>> response) {
        if (response.isSuccessful() &&
                response.body().getHeader() == null ||
                (response.body().getHeader() != null && response.body().getHeader().getMessages().isEmpty()
                ) || (response.body().getHeader() != null && response.body().getHeader().getMessages().get(0).equals(""))) {
            TemplateData data = response.body().getData();
            return convertToDomain(data);
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }

    }

    private EditTemplateViewModel convertToDomain(TemplateData data) {
        EditTemplateViewModel model = new EditTemplateViewModel();
        model.setSuccess(data.isSuccess());
        model.setEnabled(data.isIsEnable());
        return model;
    }


}
