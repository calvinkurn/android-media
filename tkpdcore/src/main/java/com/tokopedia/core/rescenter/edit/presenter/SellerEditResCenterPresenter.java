package com.tokopedia.core.rescenter.edit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/29/16.
 */
public interface SellerEditResCenterPresenter {

    void setOnLaunching(@NonNull Context context);

    void renderView(EditResCenterFormData formData);

    void setOnButtonNextClick(@NonNull Context context);

    void unsubscribe();
}
