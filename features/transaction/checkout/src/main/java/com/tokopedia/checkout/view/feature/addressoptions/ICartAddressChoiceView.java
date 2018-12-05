package com.tokopedia.checkout.view.feature.addressoptions;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.RecipientAddressModel;
import com.tokopedia.core.manage.people.address.model.Token;

import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoiceView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void renderSaveButtonEnabled();

    void renderRecipientData(List<RecipientAddressModel> recipientAddressModels, boolean isNewlyCreatedAddress);

    void setToken(Token token);

    Activity getActivityContext();

    void renderEmptyRecipientData();
}
