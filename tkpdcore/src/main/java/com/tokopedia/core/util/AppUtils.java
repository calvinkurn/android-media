package com.tokopedia.core.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;

import com.tokopedia.core2.R;
import com.tokopedia.core.invoice.activity.InvoiceRendererActivity;
import com.tokopedia.core.invoice.model.InvoiceRenderParam;

public class AppUtils {

    public static void DownloadInvoiceDialog(final Activity context, final String PDFUri, final String PDFName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_ask_download_inv) + "?");
        builder.setPositiveButton(context.getString(R.string.title_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadInvoiceWSV4(context, InvoiceRenderParam.instanceFromUrl(PDFUri, null, null), false, false);
            }
        });
        builder.setNegativeButton(context.getString(R.string.title_no), null);

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public static void InvoiceDialog(final Context context, final String url, final String invoiceNum) {
        showInvoiceDialog(context, url, invoiceNum, false, false);
    }

    public static void InvoiceDialog(final Context context, final String url, final String invoiceNum, boolean seller) {
        showInvoiceDialog(context, url, invoiceNum, false, seller);
    }

    public static void InvoiceDialogDeeplink(final Context context, final String url, final String invoiceNum) {
        showInvoiceDialog(context, url, invoiceNum, true, false);
    }

    private static void showInvoiceDialog(final Context context, final String url, final String invoiceNum,
                                          final boolean callback, final boolean seller) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(invoiceNum);
        builder.setPositiveButton(context.getString(R.string.dialog_ask_download_inv),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadInvoiceWSV4(context,
                                InvoiceRenderParam.instanceFromUrl(url, null, null),
                                callback,
                                seller
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

    private static void downloadInvoiceWSV4(Context context, InvoiceRenderParam param, Boolean callback, boolean seller) {
        Intent intent = InvoiceRendererActivity.newInstance(context, param, seller);
        context.startActivity(intent);
        if(callback){
            ((Activity) context).finish();
        }
    }
}
