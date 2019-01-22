package com.tokopedia.checkout.view.feature.addressoptions;

import android.app.Activity;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public interface ISearchAddressListView<T> {

    void showList(T t);

    void updateList(T t);

    void showListEmpty();

    void showError(String message);

    void showLoading();

    void hideLoading();

    void resetPagination();

    Activity getActivityContext();

    void setToken(Token token);

    void navigateToCheckoutPage(RecipientAddressModel recipientAddressModel);

    void stopTrace();

}
