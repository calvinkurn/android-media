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
    public interface View extends CustomerView {
        public String getDescription();
        public void setDescriptionError(String error);
        public void setQueryTitle(String title);
        public SubmitTicketInvoiceData getSubmitTicketInvoiceData();
        public ArrayList<ImageUpload> getImageList();
        public void setInvoiceNumber(String number);
        public void setInvoiceTitle(String description);
        public void setInvoiceImage(String imagePath);


        public void addimage(ImageUpload image);
        public void showProgress(String message);
        public void hideProgress();
        public void showMessage(String showToast);
        public void finish();

        void setSubmitButtonEnabled(boolean enabled);

        void setSnackBarErrorMessage(String hello);

        void showToolTip();
        void showSuccessDialog();
    }
    public interface Presenter extends CustomerPresenter<View> {
        public void onSendButtonClick();
        public void onImageSelect(ImageUpload image);
    }
}
