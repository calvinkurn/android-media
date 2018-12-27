package com.tokopedia.topchat.chattemplate.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class TemplateChatMapper implements Func1<Response<TkpdResponse>, GetTemplateViewModel> {

    @Inject
    public TemplateChatMapper() {
    }

    @Override
    public GetTemplateViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                TemplateData data = response.body().convertDataObj(TemplateData.class);
                return convertToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private GetTemplateViewModel convertToDomain(TemplateData data) {
        GetTemplateViewModel model = new GetTemplateViewModel();
        List<Visitable> list = new ArrayList<>();
        if (data.getTemplates() != null) {
            for (int i = 0; i < data.getTemplates().size(); i++) {
                if (!data.getTemplates().get(i).equals("_")) {
                    TemplateChatModel templateChatModel = new TemplateChatModel();
                    templateChatModel.setMessage(data.getTemplates().get(i));
                    list.add(templateChatModel);
                }
            }
        }
        model.setSuccess(data.isSuccess());
        model.setEnabled(data.isIsEnable());
        model.setListTemplate(list);
        return model;
    }


}
