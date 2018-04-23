package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter;

import android.content.Context;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity;
import com.tokopedia.inbox.contactus.interactor.ContactUsRetrofitInteractor;
import com.tokopedia.inbox.contactus.interactor.ContactUsRetrofitInteractorImpl;
import com.tokopedia.inbox.contactus.model.ContactUsPass;
import com.tokopedia.inbox.contactus.model.solution.SolutionResult;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.SubmitTicketUsecase;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketPresenter extends BaseDaggerPresenter<SubmitTicketContract.View> implements SubmitTicketContract.Presenter {
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
   /*     String description = getView().getDescription();
        List<ImageUpload> imageUploadList = getView().getImageList();
        SubmitTicketInvoiceData submitTicketInvoiceData = getView().getSubmitTicketInvoiceData();
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt("solution_id", submitTicketInvoiceData.getQueryTicket().getId());
        requestParams.putString("message",description);
        if(imageUploadList != null && imageUploadList.size()>0) {
            requestParams.putInt("p_photo",1);
        }
    }*/
        this.networkInteractor = new ContactUsRetrofitInteractorImpl();    if (isTicketValid()) {
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
    }
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



}
