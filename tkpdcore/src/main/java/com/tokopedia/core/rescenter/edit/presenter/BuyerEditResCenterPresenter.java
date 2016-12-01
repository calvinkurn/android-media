package com.tokopedia.core.rescenter.edit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/24/16.
 */
public interface BuyerEditResCenterPresenter {

    void setOnLaunching(@NonNull Context context);

    void renderView(EditResCenterFormData formData);

    void setOnRadioPackageStatus(boolean received);

    void setOnRadioPackageReceived();

    void setOnRadioPackageNotReceived();

    void setOnCategoryTroubleSelected(EditResCenterFormData.TroubleCategoryData troubleCategoryData);

    void setOnCategoryTroubleNothingSelected();

    void setOnButtonNextClick(@NonNull Context context);

    void unsubscribe();
}
