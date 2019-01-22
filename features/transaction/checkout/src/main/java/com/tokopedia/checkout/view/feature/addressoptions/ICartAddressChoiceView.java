package com.tokopedia.checkout.view.feature.addressoptions;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

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
