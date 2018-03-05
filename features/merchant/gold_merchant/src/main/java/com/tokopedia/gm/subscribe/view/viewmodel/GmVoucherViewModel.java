package com.tokopedia.gm.subscribe.view.viewmodel;

import com.tokopedia.gm.subscribe.domain.cart.model.GmVoucherCheckDomainModel;

/**
 * Created by sebastianuskh on 1/31/17.
 */
public class GmVoucherViewModel {
    private boolean success;
    private String message;

    public static GmVoucherViewModel mapFromDomain(GmVoucherCheckDomainModel gmVoucherCheckDomainModel) {
        GmVoucherViewModel viewModel = new GmVoucherViewModel();
        viewModel.setSuccess(true);
        viewModel.setMessage(gmVoucherCheckDomainModel.getMessage());
        return viewModel;
    }

    public static GmVoucherViewModel generateClassWithError(String message) {
        GmVoucherViewModel viewModel = new GmVoucherViewModel();
        viewModel.setSuccess(false);
        viewModel.setMessage(message);
        return viewModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
