package com.tokopedia.tkpd.purchase.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.tkpd.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.tkpd.purchase.activity.TxVerDetailActivity;
import com.tokopedia.tkpd.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.tkpd.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.tkpd.purchase.interactor.TxUploadInteractor;
import com.tokopedia.tkpd.purchase.interactor.TxUploadInteractorImpl;
import com.tokopedia.tkpd.purchase.listener.TxVerDetailViewListener;
import com.tokopedia.tkpd.purchase.model.response.txverification.TxVerData;
import com.tokopedia.tkpd.purchase.model.response.txverinvoice.TxVerInvoiceData;
import com.tokopedia.tkpd.util.UploadImageReVamp;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public class TxVerDetailPresenterImpl implements TxVerDetailPresenter {
    private static final String TAG = TxVerDetailPresenterImpl.class.getSimpleName();
    private final TxVerDetailViewListener viewListener;
    private final TxOrderNetInteractor netInteractor;
    private TxUploadInteractor txUploadInteractor;

    public TxVerDetailPresenterImpl(TxVerDetailViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
        this.txUploadInteractor = new TxUploadInteractorImpl();
    }

    @Override
    public void getTxInvoiceData(Context context, TxVerData txVerData) {
        Map<String, String> params = new HashMap<>();
        params.put("payment_id", txVerData.getPaymentId());
        netInteractor.getInvoiceData(context, params, new TxOrderNetInteractor.OnGetInvoiceData() {

            @Override
            public void onSuccess(TxVerInvoiceData data) {
                viewListener.renderInvoiceList(data.getTxOrderDetail().getDetail());
            }

            @Override
            public void onError(String message) {
                viewListener.showToastMessage(message);
                viewListener.closeView();
            }
        });
    }

    @Override
    public void processEditPayment(Context context, TxVerData txVerData) {
       viewListener.navigateToActivityRequest(ConfirmPaymentActivity.instanceEdit(context,
                txVerData.getPaymentId()),
                TxVerDetailActivity.REQUEST_EDIT_PAYMENT);
    }

    @Override
    public int getTypePaymentMethod(TxVerData data) {
        if (data.getBankName().contains("Klik") && data.getBankName().contains("BCA")) {
            return 1;
        } else {
            return data.getButton().getButtonEditPayment() == 0 &&
                    data.getButton().getButtonUploadProof() == 0 &&
                    data.getButton().getButtonViewProof() == 0 ? 2 : 3;
        }
    }

    @Override
    public void processUploadProof(final Context context, UploadImageReVamp uploadImage,
                                   TxVerData txVerData) {
        uploadImage.addParam("payment_id", txVerData.getPaymentId());
        uploadImage.setOnUploadListener(new UploadImageReVamp.UploadImageListener() {
            @Override
            public void onSuccess(JSONObject result) {
                String picSrc = result.optString("pic_src");
                String picObj = result.optString("pic_obj");
                verifyUploadedImageProof(context, picSrc, picObj);
            }

            @Override
            public void onStart() {
                viewListener.showProgressLoading();
            }

            @Override
            public void onFailure() {
                viewListener.hideProgressLoading();
            }
        });
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }

    @Override
    public void uploadProofImageWSV4(Activity activity, String imagePath, TxVerData txVerData) {
        viewListener.showProgressLoading();
        txUploadInteractor.uploadImageProof(activity, imagePath, txVerData,
                new TxUploadInteractor.OnImageProofUpload() {
                    @Override
                    public void onSuccess(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }

                    @Override
                    public void onFailed(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }
                });
    }

    private void verifyUploadedImageProof(Context context, String picSrc, String picObj) {
        Map<String, String> params = new HashMap<>();
        params.put("pic_obj", picObj);
        params.put("pic_src", picSrc);
        netInteractor.uploadValidProofByPayment(context, params,
                new TxOrderNetInteractor.OnUploadProof() {
                    @Override
                    public void onSuccess(String message) {
                        viewListener.showToastMessage(message);
                        viewListener.setResult(Activity.RESULT_OK);
                    }

                    @Override
                    public void onFailed(String message) {
                        viewListener.showToastMessage(message);
                    }
                });
    }
}
