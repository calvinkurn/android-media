package com.tokopedia.topchat.chattemplate.view.viewmodel;


import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class GetTemplateUiModel {

    boolean isSuccess;

    boolean isEnabled;

    private List<Visitable> listTemplate;

    public void setListTemplate(List<Visitable> listTemplate) {
        this.listTemplate = listTemplate;
    }

    public List<Visitable> getListTemplate() {
        return listTemplate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
