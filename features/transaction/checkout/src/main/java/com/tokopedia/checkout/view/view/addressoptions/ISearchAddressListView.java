package com.tokopedia.checkout.view.view.addressoptions;

import android.app.Activity;

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

}
