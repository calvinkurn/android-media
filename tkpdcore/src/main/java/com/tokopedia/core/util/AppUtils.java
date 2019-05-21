package com.tokopedia.core.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tokopedia.core.invoice.activity.InvoiceRendererActivity;
import com.tokopedia.core.invoice.model.InvoiceRenderParam;
import com.tokopedia.core2.R;

public class AppUtils {

    public static void InvoiceDialog(final Activity context, final String url, final String invoiceNum) {
        showInvoiceDialog(context, url, invoiceNum, false, false);
    }

    public static void InvoiceDialog(final Activity context, final String url, final String invoiceNum, boolean seller) {
        showInvoiceDialog(context, url, invoiceNum, false, seller);
    }

    public static void InvoiceDialogDeeplink(final Activity context, final String url, final String invoiceNum) {
        showInvoiceDialog(context, url, invoiceNum, true, false);
    }

    private static void showInvoiceDialog(final Activity context, final String url, final String invoiceNum,
                                          final boolean callback, final boolean seller) {
        final com.tokopedia.design.component.Dialog dialog =
                new com.tokopedia.design.component.Dialog(context,
                        com.tokopedia.design.component.Dialog.Type.PROMINANCE);
        dialog.setTitle("");
        dialog.setDesc(invoiceNum);
        dialog.setBtnOk(context.getString(R.string.dialog_ask_download_inv));
        dialog.setBtnCancel(context.getString(R.string.title_copy));
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadInvoiceWSV4(context,
                        InvoiceRenderParam.instanceFromUrl(url, null, null),
                        callback,
                        seller
                );
            }
        });
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipBoard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", invoiceNum);
                if (clipBoard != null) {
                    clipBoard.setPrimaryClip(clip);
                }
                if(callback){
                    ((Activity) context).finish();
                }
            }
        });
        dialog.getTitleTextView().setVisibility(View.GONE);
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
