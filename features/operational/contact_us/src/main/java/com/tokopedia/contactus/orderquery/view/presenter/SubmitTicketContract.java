package com.tokopedia.contactus.orderquery.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;

import java.util.ArrayList;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public interface SubmitTicketContract {
    interface View extends CustomerView {
        String getDescription();

        void setDescriptionError(String error);

        void setQueryTitle(String title);

        SubmitTicketInvoiceData getSubmitTicketInvoiceData();

        ArrayList<ImageUpload> getImageList();

        void setInvoiceNumber(String number);

        void setInvoiceTitle(String description);

        void setInvoiceImage(String imagePath);

        void addimage(ImageUpload image);

        void showProgress(String message);

        void hideProgress();

        void showMessage(String showToast);

        void finish();

        void setSubmitButtonEnabled(boolean enabled);

        void setSnackBarErrorMessage(String hello);

        void showToolTip();

        void showSuccessDialog();
    }

    interface Presenter extends CustomerPresenter<View> {
        void onSendButtonClick();

        void onImageSelect(ImageUpload image);
    }
}
