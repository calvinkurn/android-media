package com.tokopedia.core.invoice.presenter;

import android.content.Context;

import com.tkpd.library.utils.ConnectionDetector;
import com.tokopedia.core2.R;
import com.tokopedia.core.invoice.interactor.InvoiceNetInteractor;
import com.tokopedia.core.invoice.interactor.InvoiceNetInteractorImpl;
import com.tokopedia.core.invoice.listener.InvoiceViewListener;
import com.tokopedia.core.invoice.model.InvoiceRenderParam;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * InvoiceRenderPresenterImpl
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public class InvoiceRenderPresenterImpl implements InvoiceRenderPresenter {
    private final InvoiceViewListener viewListener;
    private final InvoiceNetInteractorImpl netInteractor;
    private ConnectionDetector connectionDetector;

    public InvoiceRenderPresenterImpl(InvoiceViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new InvoiceNetInteractorImpl();

    }

    @Override
    public void processGetRenderedInvoice(Context context, InvoiceRenderParam data) {
        Map<String, String> params = new HashMap<>();
        params.put("pdf", data.getInvoicePdf());
        params.put("id", data.getId());
        params.put("recharge", data.getRecharge() + "");
        params.put("tkpd", data.getGoldMerchant() + "");
        connectionDetector = new ConnectionDetector(context);
        if(connectionDetector.isConnectingToInternet()) {
            netInteractor.renderInvoice(context, AuthUtil.generateParams(context, params),
                    new InvoiceNetInteractor.OnRenderInvoice() {
                        @Override
                        public void onSuccess(String htmlSource) {
                            viewListener.dismissProgress();
                            viewListener.renderInvoiceWeb(htmlSource);
                        }

                        @Override
                        public void onTimeout(String message) {
                            viewListener.dismissProgress();
                            viewListener.renderInvoiceWeb(message);
                        }

                        @Override
                        public void onError(String message) {
                            viewListener.dismissProgress();
                            viewListener.renderInvoiceError(message);
                        }

                        @Override
                        public void onNullData(String message) {
                            viewListener.dismissProgress();
                            viewListener.renderInvoiceWeb(message);
                        }

                        @Override
                        public void onNoInternetConnection(String message) {
                            viewListener.dismissProgress();
                            viewListener.renderInvoiceWeb(message);
                        }
                    });
        } else {
            viewListener.dismissProgress();
            viewListener.renderInvoiceError(context.getString(R.string.msg_no_connection));
        }

    }
}
