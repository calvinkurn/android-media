package com.tokopedia.core.rescenter.edit.listener;

/**
 * Created on 8/24/16.
 */
public interface EditResCenterListener {

    void finishActivity();

    void inflateBuyerEditResolutionForm();

    void inflateSellerEditResolutionForm();

    boolean isCustomer();

    boolean isEdit();

    void inflateAppealFragment();
}
