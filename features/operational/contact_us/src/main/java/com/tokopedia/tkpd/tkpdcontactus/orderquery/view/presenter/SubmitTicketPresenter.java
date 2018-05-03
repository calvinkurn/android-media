package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity;
import com.tokopedia.inbox.contactus.interactor.ContactUsRetrofitInteractor;
import com.tokopedia.inbox.contactus.interactor.ContactUsRetrofitInteractorImpl;
import com.tokopedia.inbox.contactus.model.ContactUsPass;
import com.tokopedia.inbox.contactus.model.ImageUpload;
import com.tokopedia.inbox.contactus.model.solution.SolutionResult;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.SubmitTicketUsecase;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketPresenter extends BaseDaggerPresenter<SubmitTicketContract.View> implements SubmitTicketContract.Presenter {
    private static final int MESSAGE_WRONG_DIMENSION = 0;
    private static final int MESSAGE_WRONG_FILE_SIZE = 1;
    Context context;
    SubmitTicketUsecase submitTicketUsecase;
    private String cameraFileLoc;
    private ContactUsRetrofitInteractorImpl networkInteractor;


    @Inject
    public SubmitTicketPresenter(@ApplicationContext Context context, SubmitTicketUsecase submitTicketUsecase) {
        this.context = context;
        this.submitTicketUsecase = submitTicketUsecase;
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
        this.networkInteractor = new ContactUsRetrofitInteractorImpl();
        if (isTicketValid() && isUploadImageValid()) {
        networkInteractor.sendTicket( context,getSendTicketParam(), new ContactUsRetrofitInteractor.SendTicketListener() {
            @Override
            public void onSuccess() {
                getView().showMessage("Success Load");
                getView().finish();
            }

            @Override
            public void onNoNetworkConnection() {


            }

            @Override
            public void onTimeout(String error) {

            }

            @Override
            public void onError(String s) {

            }

            @Override
            public void onNullData() {

            }
        });
    } else {
            ArrayList<ImageUpload> uploadImageList = getView().getImageList();
            int numOfImages = uploadImageList.size();
            if(numOfImages > 0) {
                for (int item = 0; item < numOfImages; item++) {
                    ImageUpload image = uploadImageList.get(item);
                    if(!fileSizeValid(image.getFileLoc())){
                        showErrorMessage(MESSAGE_WRONG_FILE_SIZE);
                    }
                }
            }
        }
}

    private void showErrorMessage(int messageWrongParam) {
        if(messageWrongParam == MESSAGE_WRONG_FILE_SIZE){
            getView().setSnackBarErrorMessage("Ups! Ukuran gambar yang di-upload lebih dari 10MB");
        }
    }

    private boolean isUploadImageValid() {
        ArrayList<ImageUpload> uploadImageList = getView().getImageList();
        int numOfImages = uploadImageList.size();
        if(numOfImages > 0){
            for(int item = 0; item < numOfImages; item++){
                ImageUpload image = uploadImageList.get(item);
                if(fileSizeValid(image.getFileLoc())){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean fileSizeValid(String fileLoc) {
        File file = new File(fileLoc);
        long size = file.length();
        return ((size/1024) < 10240);

    }

    private boolean getBitmapDimens(String fileLoc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(fileLoc).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if(imageHeight <= 300 && imageWidth <= 300){
            return true;
        }
        return false;
    }

    private ContactUsPass getSendTicketParam() {
        String description = getView().getDescription();
        SubmitTicketInvoiceData submitTicketInvoiceData = getView().getSubmitTicketInvoiceData();
        ContactUsPass pass = new ContactUsPass();
        pass.setSolutionId(submitTicketInvoiceData.getQueryTicket().getId()+"");
        pass.setMessageBody(getView().getDescription());
            pass.setAttachment(getView().getImageList());
        pass.setName(SessionHandler.getLoginName(context));
        if (submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getId() > 0)
            pass.setOrderId(submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getId()+"");
        if (submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getId() > 0)
            pass.setInvoiceNumber(submitTicketInvoiceData.getBuyerPurchaseList().getDetail().getId()+"");
        if (!SessionHandler.isV4Login(context)) {
        }
        return pass;
    }

    private boolean isTicketValid() {

        if (!SessionHandler.isV4Login(context)) {

        }

        if (getView().getDescription().toString().trim().length() == 0) {
            return false;
        } else if (getView().getDescription().toString().trim().length() < 30) {
            return false;
        }


        return true;
    }


    public void sendCount(int count) {

    }
}
