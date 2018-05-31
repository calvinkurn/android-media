package com.tokopedia.checkout.view.view.addressoptions;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoiceView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void renderSaveButtonEnabled();

    void renderRecipientData(List<RecipientAddressModel> recipientAddressModels);

    Activity getActivity();

    void renderEmptyRecipientData();
}
