package com.tokopedia.core.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.deposit.interactor.WithdrawRetrofitInteractor;
import com.tokopedia.core.deposit.interactor.WithdrawRetrofitInteractorImpl;
import com.tokopedia.core.invoice.activity.InvoiceRendererActivity;
import com.tokopedia.core.invoice.model.InvoiceRenderParam;

import java.util.HashMap;

public class AppUtils {

    public static void DownloadInvoiceDialog(final Activity context, final String PDFUri, final String PDFName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_ask_download_inv) + "?");
        builder.setPositiveButton(context.getString(R.string.title_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadInvoiceWSV4(context, InvoiceRenderParam.instanceFromUrl(PDFUri, null, null), false);
            }
        });
        builder.setNegativeButton(context.getString(R.string.title_no), null);

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public static void InvoiceDialog(final Context context, final String url, final String invoiceNum) {
        showInvoiceDialog(context, url, invoiceNum, false);
    }

    public static void InvoiceDialogDeeplink(final Context context, final String url, final String invoiceNum) {
        showInvoiceDialog(context, url, invoiceNum, true);
    }

    private static void showInvoiceDialog(final Context context, final String url, final String invoiceNum,
                                          final boolean callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(invoiceNum);
        builder.setPositiveButton(context.getString(R.string.dialog_ask_download_inv),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadInvoiceWSV4(context,
                                InvoiceRenderParam.instanceFromUrl(url, null, null),
                                callback
                        );
                    }
                });

        builder.setNegativeButton(context.getString(R.string.title_copy),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipBoard = (ClipboardManager)
                                context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", invoiceNum);
                        clipBoard.setPrimaryClip(clip);
                        if(callback){
                            ((Activity) context).finish();
                        }
                    }
                });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }


    @Deprecated
    public static void InvoiceDialog(final Activity context, final String PDFUri, final String PDFName, final String Invoice) {
        InvoiceDialog(context, PDFUri, Invoice);
    }

    private static void downloadInvoiceWSV4(Context context, InvoiceRenderParam param, Boolean callback) {
        Intent intent = InvoiceRendererActivity.newInstance(context, param);
        context.startActivity(intent);
        if(callback){
            ((Activity) context).finish();
        }
    }

    public static void sendOTP(final Context context) {
        final TkpdProgressDialog loading = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        loading.showDialog();
        WithdrawRetrofitInteractor networkInteractor = new WithdrawRetrofitInteractorImpl();
        networkInteractor.sendOTP(context, new HashMap<String, String>(), new WithdrawRetrofitInteractor.SendOTPListener() {
            @Override
            public void onSuccess(String message) {
                CommonUtils.UniversalToast(context, message);
                loading.dismiss();

            }

            @Override
            public void onTimeout(String message) {
                CommonUtils.UniversalToast(context, message);
                loading.dismiss();

            }

            @Override
            public void onError(String error) {
                CommonUtils.UniversalToast(context, error);
                loading.dismiss();
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoNetworkConnection() {
                SnackbarManager.make((Activity) context,
                        context.getString(R.string.msg_no_connection),
                        Snackbar.LENGTH_LONG).show();
                loading.dismiss();

            }
        });
    }

}
