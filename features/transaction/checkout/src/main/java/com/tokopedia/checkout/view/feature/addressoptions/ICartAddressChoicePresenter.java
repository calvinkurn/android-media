package com.tokopedia.checkout.view.feature.addressoptions;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoicePresenter extends CustomerPresenter<ICartAddressChoiceView> {

    void getAddressShortedList(Context context, RecipientAddressModel currentAddress, boolean isNewlyCreatedAddress);

    void setSelectedRecipientAddress(RecipientAddressModel model);

    RecipientAddressModel getSelectedRecipientAddress();
}
