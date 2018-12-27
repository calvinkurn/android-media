//package com.tokopedia.topchat.attachinvoice.view.presenter;
//
//import android.content.Context;
//
//import com.tokopedia.topchat.attachinvoice.domain.usecase.AttachInvoicesUseCase;
//import com.tokopedia.topchat.attachinvoice.view.AttachInvoiceContract;
//import com.tokopedia.topchat.attachinvoice.view.subscriber.AttachInvoicesLoadInvoiceDataSubscriber;
//
//import javax.inject.Inject;
//
///**
// * Created by Hendri on 22/03/18.
// */
//
//public class AttachInvoicePresenter implements AttachInvoiceContract.Presenter {
//    private final AttachInvoicesUseCase useCase;
//    AttachInvoiceContract.Activity activity;
//    AttachInvoiceContract.View view;
//
//    public AttachInvoicePresenter(AttachInvoicesUseCase useCase) {
//        this.useCase = useCase;
//    }
//
//    @Override
//    public void loadInvoiceData(String query, String userId, int page, int messageId, Context
//            context) {
//        useCase.execute(AttachInvoicesUseCase.createRequestParam(query, userId, page, messageId,
//                context), new AttachInvoicesLoadInvoiceDataSubscriber(view));
//    }
//
//    @Override
//    public void attachView(AttachInvoiceContract.View view) {
//        this.view = view;
//    }
//
//    @Override
//    public void attachActivityContract(AttachInvoiceContract.Activity activityContract) {
//        this.activity = activityContract;
//    }
//
//    @Override
//    public void detachView() {
//        if(useCase != null) useCase.unsubscribe();
//        if(view != null) view = null;
//        if(activity != null) activity = null;
//    }
//}
