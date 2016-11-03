package com.tokopedia.tkpd.rescenter.detail.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.database.CacheUtil;
import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.database.model.AttachmentResCenterDB;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.rescenter.detail.dialog.InputShippingRefNumDialog;
import com.tokopedia.tkpd.rescenter.detail.dialog.UploadImageDialog;
import com.tokopedia.tkpd.rescenter.detail.facade.NetworkParam;
import com.tokopedia.tkpd.rescenter.detail.fragment.DetailResCenterFragment;
import com.tokopedia.tkpd.rescenter.detail.interactor.RetrofitInteractor;
import com.tokopedia.tkpd.rescenter.detail.interactor.RetrofitInteractorImpl;
import com.tokopedia.tkpd.rescenter.detail.listener.DetailResCenterView;
import com.tokopedia.tkpd.rescenter.detail.listener.ResCenterView;
import com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata.ResCenterKurir;
import com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;
import com.tokopedia.tkpd.rescenter.detail.model.passdata.ActivityParamenterPassData;
import com.tokopedia.tkpd.rescenter.detail.service.DetailResCenterService;
import com.tokopedia.tkpd.rescenter.detail.service.DetailResCenterServiceConstant;
import com.tokopedia.tkpd.rescenter.utils.LocalCacheManager;

import java.util.List;

/**
 * Created on 2/9/16.
 */
public class DetailResCenterImpl implements DetailResCenterPresenter {

    private static final String TAG = DetailResCenterImpl.class.getSimpleName();
    private static final int REQUEST_CODE_SCAN_BARCODE = 4;
    private static final int REQUEST_APPEAL_RESOLUTION = 5;
    private static final String CURRENT_DATA = "CURRENT_DATA";
    private static final String CURRENT_DATA_PASS = "CURRENT_DATA_PASS";

    private final DetailResCenterView view;
    private final RetrofitInteractor retrofitInteractor;

    private UploadImageDialog uploadImageDialog;
    private ResCenterView mListener;
    private GlobalCacheManager globalCacheManager;
    private LocalCacheManager.MessageConversation cache;
    private boolean isEditShippingRefNum;

    public DetailResCenterImpl(DetailResCenterFragment fragment) {
        this.view = fragment;
        this.retrofitInteractor = new RetrofitInteractorImpl();
        this.globalCacheManager = new GlobalCacheManager();
        cache = LocalCacheManager.MessageConversation.Builder(view.getResolutionID()).getCache();
        uploadImageDialog = new UploadImageDialog(fragment, view.getResolutionID());
    }

    @Override
    public void setInteractionListener(ResCenterView listener) {
        mListener = listener;
    }

    @Override
    public void onFirstTimeLaunched(@NonNull Fragment fragment, @NonNull ActivityParamenterPassData passData) {
        view.setProgressLoading(true);
        requestResCenterDetail(fragment.getActivity(), passData);
    }

    @Override
    public void onButtonSendClick(@NonNull Context context, String param) {
        if (!param.trim().isEmpty()) {
            cache.setMessage(param).save();
            view.setErrorComment(null);
            processReply();
        } else {
            view.setErrorComment(context.getString(R.string.error_field_required));
        }
    }

    @Override
    public void onButtonAttachmentClick(Context context) {
        uploadImageDialog.showDialog();
    }

    @Override
    public void showScanBarcode(Context context) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_SCAN_BARCODE);
    }

    @Override
    public void actionEditShippingRefNum() {
        view.setProgressLoading(true);
        mListener.actionUpdateShippingRefNum(view.getResolutionID());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN_BARCODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                LocalCacheManager.ReturnPackage.Builder(view.getResolutionID())
                        .getCache()
                        .setShippingRefNum(data.getStringExtra("SCAN_RESULT"))
                        .save();

                if (isEditShippingRefNum) {
                    actionEditShippingRefNum();
                } else {
                    actionInputShippingRefNum();
                }
            }
        } else if (requestCode == REQUEST_APPEAL_RESOLUTION) {
            if (resultCode == Activity.RESULT_OK) {
                view.refreshPage();
            }
        } else {
            uploadImageDialog.onResult(requestCode, resultCode, data, new UploadImageDialog.UploadImageDialogListener() {
                @Override
                public void onSuccess(List<AttachmentResCenterDB> data) {
                    view.showAttachment(data);
                    view.setAttachmentArea(true);
                }

                @Override
                public void onFailed() {

                }
            });
        }
    }

    @Override
    public void processReply() {
        view.setProgressLoading(true);
        mListener.replyConversation(view.getResolutionID());
    }

    @Override
    public void processChangeSolution() {
        view.setProgressLoading(true);
        mListener.changeSolution(view.getResolutionID());
    }

    @Override
    public void requestResCenterDetail(@NonNull final Context context,
                                       @NonNull final ActivityParamenterPassData activityParamenterPassData) {
        retrofitInteractor.getResCenterDetail(context,
                NetworkParam.paramResCenterDetail(activityParamenterPassData),
                new RetrofitInteractor.ResCenterDetailListener() {

                    @Override
                    public void onSuccess(@NonNull DetailResCenterData data) {
                        storeStateCache(data);
                        view.loadDetailResCenterData(data);
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setFailSaveRespond();
                        view.showTimeOutView(listener);
                    }

                    @Override
                    public void onFailAuth() {
                        view.setFailSaveRespond();
                        view.showErrorView(null);
                    }

                    @Override
                    public void onThrowable(Throwable e) {
                        view.setFailSaveRespond();
                        view.showTimeOutView(null);
                    }

                    @Override
                    public void onError(String message) {
                        view.setFailSaveRespond();
                        view.showErrorView(message);
                    }

                    @Override
                    public void onNullData() {
                        view.setFailSaveRespond();
                        view.showErrorView(null);
                    }
                });
    }

    private void storeStateCache(DetailResCenterData data) {
        LocalCacheManager.StateDetailResCenter.Builder(view.getResolutionID())
                .setByCustomer(data.getDetail().getResolutionBy().getByCustomer())
                .setBySeller(data.getDetail().getResolutionBy().getBySeller())
                .setLastFlagReceived(data.getDetail().getResolutionLast().getLastFlagReceived())
                .setLastTroubleType(data.getDetail().getResolutionLast().getLastTroubleType())
                .setLastTroubleString(data.getDetail().getResolutionLast().getLastTroubleTypeString())
                .setLastSolutionType(data.getDetail().getResolutionLast().getLastSolution())
                .setLastSolutionString(data.getDetail().getResolutionLast().getLastSolutionString())
                .setOrderPriceFmt(data.getDetail().getResolutionOrder().getOrderOpenAmountIdr())
                .setOrderPriceRaw(data.getDetail().getResolutionOrder().getOrderOpenAmount())
                .setShippingPriceFmt(data.getDetail().getResolutionOrder().getOrderShippingPriceIdr())
                .setShippingPriceRaw(data.getDetail().getResolutionOrder().getOrderShippingPrice())
                .save();
    }

    @Override
    public void requestTrackDelivery(@NonNull final Context context,
                                     @NonNull String url) {
        view.showLoadingDialog(true);
        retrofitInteractor.trackShipping(context,
                AuthUtil.generateParams(context, NetworkParam.paramTrackingDelivery(url)),
                new RetrofitInteractor.TrackShippingListener() {
                    @Override
                    public void onSuccess(ResCenterTrackShipping resCenterTrackShipping) {
                        Log.d(TAG, CacheUtil.convertModelToString(resCenterTrackShipping, new TypeToken<ResCenterTrackShipping>() {
                        }.getType()));
                        view.showLoadingDialog(false);
                        if (resCenterTrackShipping.getTrackShipping() != null) {
                            view.showTrackingDialog(resCenterTrackShipping.getTrackShipping());
                        } else {
                            view.showInvalidTrackingDialog();
                        }
                    }

                    @Override
                    public void onTimeOut(String message, NetworkErrorHelper.RetryClickedListener listener) {
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {

                    }

                    @Override
                    public void onThrowable(Throwable e) {
                        view.showLoadingDialog(false);
                    }

                    @Override
                    public void onError(String message) {
                        Log.d(TAG, message);
                        view.showLoadingDialog(false);
                        view.showToastMessage(message);
                    }

                    @Override
                    public void onNullData() {

                    }
                });

    }

    @Override
    public void requestCourierList(@NonNull final Context context,
                                   @NonNull final InputShippingRefNumDialog.Listener listener) {
        retrofitInteractor.getKurirList(context,
                new RetrofitInteractor.GetKurirListListener() {
                    @Override
                    public void onSuccess(ResCenterKurir resCenterKurirList) {
                        globalCacheManager.setKey(view.getResolutionID());
                        globalCacheManager.setValue(
                                CacheUtil.convertModelToString(resCenterKurirList,
                                        new TypeToken<ResCenterKurir>() {
                                        }.getType()));
                        globalCacheManager.setCacheDuration(1800000); // expired in 30minutes
                        globalCacheManager.store();

                        view.showInputShippingRefNumDialog(view.getResolutionID(), listener);
                        Log.d(TAG, CacheUtil.convertModelToString(resCenterKurirList, new TypeToken<ResCenterKurir>() {
                        }.getType()));
                    }

                    @Override
                    public void onTimeOut(String message, NetworkErrorHelper.RetryClickedListener listener) {
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                    }

                    @Override
                    public void onThrowable(Throwable e) {
                        for (int i = 0; i < e.getStackTrace().length; i++) {
                            StackTraceElement[] stackTraceElements = e.getStackTrace();
                            Log.d(TAG + "onThrowable", String.valueOf(stackTraceElements[i]));
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Log.d(TAG, message);
                        view.showToastMessage(message);
                    }

                    @Override
                    public void onNullData() {

                    }
                });
    }

    @Override
    public void onOverflowShippingRefNumClick(@NonNull Context context,
                                              @NonNull String url,
                                              @NonNull InputShippingRefNumDialog.Listener listener) {

        LocalCacheManager.ReturnPackage
                .Builder(view.getResolutionID())
                .setConversationID(Uri.parse(url).getQueryParameter("conv_id"))
                .setShippingID(Uri.parse(url).getQueryParameter("ship_id"))
                .setShippingRefNum(Uri.parse(url).getQueryParameter("ship_ref"))
                .save();

        showShippingRefNumDialog(true, context, listener);
    }

    @Override
    public void showShippingRefNumDialog(boolean isEditShippingRefNum, Context context, InputShippingRefNumDialog.Listener listener) {
        try {
            this.isEditShippingRefNum = isEditShippingRefNum;
            if (globalCacheManager.getValueString(view.getResolutionID()) != null) {
                view.showInputShippingRefNumDialog(view.getResolutionID(), listener);
            } else {
                requestCourierList(context, listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestCourierList(context, listener);
        }
    }

    @Override
    public void actionAcceptSolution() {
        view.setProgressLoading(true);
        mListener.actionAcceptSolution(view.getResolutionID());
    }

    @Override
    public void actionAcceptAdminSolution() {
        view.setProgressLoading(true);
        mListener.actionAcceptAdminSolution(view.getResolutionID());
    }

    @Override
    public void actionInputShippingRefNum() {
        view.setProgressLoading(true);
        mListener.actionInputShippingRefNum(view.getResolutionID());
    }

    @Override
    public void actionFinishReturSolution() {
        view.setProgressLoading(true);
        mListener.actionFinishReturSolution(view.getResolutionID());
    }

    @Override
    public void actionCancelResolution() {
        view.setProgressLoading(true);
        mListener.actionCancelResolution(view.getResolutionID());
    }

    @Override
    public void actionReportResolution() {
        view.setProgressLoading(true);
        mListener.actionReportResolution(view.getResolutionID());
    }

    @Override
    public void onReceiveServiceResult(int resultCode, Bundle resultData) {
        int typePostData = resultData.getInt(DetailResCenterService.EXTRA_PARAM_ACTION_TYPE, 0);
        if (resultCode == DetailResCenterServiceConstant.STATUS_FINISHED) {
            switch (typePostData) {
                case DetailResCenterService.ACTION_CHANGE_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_REPLY_CONVERSATION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_INPUT_SHIPPING_REF_NUM:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_UPDATE_SHIPPING_REF_NUM:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_ACCEPT_ADMIN_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_ACCEPT_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_FINISH_RETUR_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_CANCEL_RESOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_REPORT_RESOLUTION:
                    view.refreshPage();
                    break;
                default:
                    throw new UnsupportedOperationException("unknown operation");
            }
        } else {
            String errorMessage = resultData.getString(DetailResCenterServiceConstant.EXTRA_PARAM_NETWORK_ERROR_MESSAGE);
            view.setProgressLoading(false);
            switch (resultData.getInt(DetailResCenterServiceConstant.EXTRA_PARAM_NETWORK_ERROR_TYPE)) {
                case DetailResCenterServiceConstant.STATUS_TIME_OUT :
                    view.showTimeOutMessage();
                    break;
                default:
                    view.showToastMessage(errorMessage);
                    break;
            }
        }
    }

    @Override
    public void refreshPage(Context context, ActivityParamenterPassData activityParamenterPassData) {
        view.setProgressLoading(true);
        view.setAttachmentArea(false);
        view.setReplyAreaEmpty();
        requestResCenterDetail(context, activityParamenterPassData);
    }

    @Override
    public void saveState(Bundle state, ActivityParamenterPassData dataPass, DetailResCenterData data) {
        state.putParcelable(CURRENT_DATA_PASS, dataPass);
        state.putParcelable(CURRENT_DATA, data);
    }

    @Override
    public void restoreState(Bundle savedState) {
        DetailResCenterData data = savedState.getParcelable(CURRENT_DATA);
        view.loadDetailResCenterData(data);
    }

    @Override
    public void onDestroyView() {
        retrofitInteractor.unsubscribe();
    }

    @Override
    public void actionInputAddress(Context context, String addressID) {
        retrofitInteractor.postAddress(context,
                NetworkParam.getParamInputAddress(addressID, view.getResolutionID()),
                new RetrofitInteractor.OnPostAddressListener() {
                    @Override
                    public void onStart() {
                        view.setProgressLoading(true);
                    }

                    @Override
                    public void onSuccess() {
                        view.refreshPage();
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setProgressLoading(false);
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                        view.setProgressLoading(false);
                    }

                    @Override
                    public void onError(String message) {
                        view.setProgressLoading(false);
                        view.showToastMessage(message);
                    }
                });
    }

    @Override
    public void actionEditAddress(Context context, String addressID, String ahrefEditAddressURL) {
        retrofitInteractor.editAddress(context,
                NetworkParam.getParamInputEditAddress(addressID, ahrefEditAddressURL, view.getResolutionID()),
                new RetrofitInteractor.OnPostAddressListener() {
                    @Override
                    public void onStart() {
                        view.setProgressLoading(true);
                    }

                    @Override
                    public void onSuccess() {
                        view.refreshPage();
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setProgressLoading(false);
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                        view.setProgressLoading(false);
                    }

                    @Override
                    public void onError(String message) {
                        view.setProgressLoading(false);
                        view.showToastMessage(message);
                    }
                });
    }

    @Override
    public void actionImagePicker() {
        uploadImageDialog.openImagePicker();
    }

    @Override
    public void actionCamera() {
        uploadImageDialog.openCamera();
    }
}
