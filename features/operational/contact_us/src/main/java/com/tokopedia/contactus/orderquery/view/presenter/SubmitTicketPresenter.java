package com.tokopedia.contactus.orderquery.view.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.orderquery.data.ContactUsPass;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.data.QueryTicket;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.contactus.orderquery.domain.SubmitTicketUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketPresenter extends BaseDaggerPresenter<SubmitTicketContract.View> implements SubmitTicketContract.Presenter {
    private static final int MESSAGE_WRONG_DIMENSION = 0;
    private static final int MESSAGE_WRONG_FILE_SIZE = 1;
    private Context context;
    private SubmitTicketUseCase submitTicketUseCase;

    @Inject
    public SubmitTicketPresenter(@ApplicationContext Context context, SubmitTicketUseCase submitTicketUseCase) {
        this.context = context;
        this.submitTicketUseCase = submitTicketUseCase;
    }

    @Override
    public void attachView(SubmitTicketContract.View view) {
        super.attachView(view);
        BuyerPurchaseList buyerPurchaseList = getView().getSubmitTicketInvoiceData().getBuyerPurchaseList();
        QueryTicket queryTicket = getView().getSubmitTicketInvoiceData().getQueryTicket();
        getView().setInvoiceImage(buyerPurchaseList.getProducts().get(0).getImage());
        getView().setInvoiceNumber(buyerPurchaseList.getDetail().getCode());
        getView().setInvoiceTitle(buyerPurchaseList.getProducts().get(0).getName());
        getView().setQueryTitle(queryTicket.getName());
    }


    @Override
    public void onSendButtonClick() {
        if (isTicketValid() && isUploadImageValid()) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putObject("submitTicket", getSendTicketParam());
            getView().showProgress(context.getString(R.string.please_wait));
            submitTicketUseCase.execute(requestParams, new Subscriber<CreateTicketResult>() {
                @Override
                public void onCompleted() {
                    getView().hideProgress();
                }

                @Override
                public void onError(Throwable e) {
                    showMessage(e.getLocalizedMessage());
                }

                @Override
                public void onNext(CreateTicketResult tkpdResponseResponse) {
                    getView().showSuccessDialog();
                    getView().hideProgress();

                }
            });
        }
    }

    private void showMessage(String s) {
        getView().hideProgress();
        getView().showMessage(s);
    }

    @Override
    public void onImageSelect(ImageUpload image) {
        if (!fileSizeValid(image.getFileLoc())) {
            showErrorMessage(MESSAGE_WRONG_FILE_SIZE);
        } else if (!getBitmapDimens(image.getFileLoc())) {
            showErrorMessage(MESSAGE_WRONG_DIMENSION);
        } else {
            getView().addimage(image);
        }
    }

    private void showErrorMessage(int messageWrongParam) {
        if (messageWrongParam == MESSAGE_WRONG_FILE_SIZE) {
            getView().setSnackBarErrorMessage(context.getString(R.string.error_msg_wrong_size));
        } else if (messageWrongParam == MESSAGE_WRONG_DIMENSION) {
            getView().setSnackBarErrorMessage(context.getString(R.string.error_msg_wrong_height_width));
        }
    }

    private boolean isUploadImageValid() {
        ArrayList<ImageUpload> uploadImageList = getView().getImageList();
        int numOfImages = uploadImageList.size();
        if (numOfImages > 0) {
            for (int item = 0; item < numOfImages; item++) {
                ImageUpload image = uploadImageList.get(item);
                if (fileSizeValid(image.getFileLoc()) && getBitmapDimens(image.getFileLoc())) {
                    return true;
                }
            }
        } else if (numOfImages == 0) {
            return true;
        }
        return false;
    }

    private boolean fileSizeValid(String fileLoc) {
        File file = new File(fileLoc);
        long size = file.length();
        return ((size / 1024) < 10240);

    }

    private boolean getBitmapDimens(String fileLoc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(fileLoc).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return !(imageHeight < 300 && imageWidth < 300);
    }

    private ContactUsPass getSendTicketParam() {
        SubmitTicketInvoiceData submitTicketInvoiceData = getView().getSubmitTicketInvoiceData();
        ContactUsPass pass = new ContactUsPass();
        pass.setSolutionId(String.valueOf(submitTicketInvoiceData.getQueryTicket().getId()));
        pass.setMessageBody(getView().getDescription());
        pass.setAttachment(getView().getImageList());
        UserSessionInterface userSession = new UserSession(context);
        pass.setName(userSession.getName());



        if (submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getId() > 0)
            pass.setOrderId(String.valueOf(submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getId()));

        if(submitTicketInvoiceData.getBuyerPurchaseList().getDetail()!= null && !TextUtils.isEmpty(submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getCode())) {
            pass.setInvoiceNumber(submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getCode());
        }
        return pass;
    }

    private boolean isTicketValid() {
        if (getView().getDescription().trim().length() == 0) {
            return false;
        } else if (getView().getDescription().trim().length() < 30) {
            return false;
        }
        return true;
    }

    public void onToolTipClick() {
        getView().showToolTip();
    }
}
