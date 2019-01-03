package com.tokopedia.topchat.chattemplate.data.mapper;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateChatMapper implements Func1<Response<TokopediaWsV4Response>, EditTemplateViewModel> {

    @Inject
    public EditTemplateChatMapper() {
    }

    @Override
    public EditTemplateViewModel call(Response<TokopediaWsV4Response> response) {
        //TODO ADD ERROR INTERCEPTOR
        if (response.isSuccessful() && ((!response.body().isNullData()
                && response.body().getErrorMessageJoined().equals(""))
                || !response.body().isNullData() && response.body().getErrorMessages() == null)) {
            TemplateData data = response.body().convertDataObj(TemplateData.class);
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
