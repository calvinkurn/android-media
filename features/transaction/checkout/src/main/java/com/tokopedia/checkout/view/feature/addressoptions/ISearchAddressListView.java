package com.tokopedia.checkout.view.feature.addressoptions;

import android.app.Activity;

import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public interface ISearchAddressListView<T> {

    void showList(T t);

    void setCorner(CornerAddressModel cornerAddressModel);

    void populateCorner(List<CornerAddressModel> cornerAddressModelList);

    void showCornerBottomSheet();

    void updateList(T t);

    void showListEmpty();

    void showError(Throwable e);

    void showLoading();

    void hideLoading();

    void resetPagination();

    void setToken(Token token);

    void navigateToCheckoutPage(RecipientAddressModel recipientAddressModel);

    void stopTrace();

}
