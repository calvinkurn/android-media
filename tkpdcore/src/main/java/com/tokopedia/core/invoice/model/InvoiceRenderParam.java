package com.tokopedia.core.invoice.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * InvoiceRenderParam
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public class InvoiceRenderParam implements Parcelable {
    private static final String TAG = InvoiceRenderParam.class.getSimpleName();

    private String id;
    private String invoicePdf;
    private String invoiceUrl;
    private int goldMerchant;
    private int recharge;

    public static InvoiceRenderParam instanceFromUrl(String invoiceUrl, Integer goldMerchant,
                                                     Integer recharge) {
        try {
            Uri uri = Uri.parse(invoiceUrl);
            String pdf = uri.getQueryParameter("pdf");
            String id = uri.getQueryParameter("id");
            InvoiceRenderParam data = new InvoiceRenderParam();
            data.setId(id);
            data.setInvoicePdf(pdf);
            data.setInvoiceUrl(invoiceUrl);
            data.setRecharge(recharge == null ? 0 : 1);
            data.setGoldMerchant(goldMerchant == null ? 0 : 1);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InvoiceRenderParam instanceFromPdf(String id, String pdf) {
        InvoiceRenderParam data = new InvoiceRenderParam();
        data.setId(id);
        data.setInvoicePdf(pdf);
        return data;
    }

    public InvoiceRenderParam() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getInvoicePdf() {
        return invoicePdf;
    }

    public void setInvoicePdf(String invoicePdf) {
        this.invoicePdf = invoicePdf;
    }

    public int getGoldMerchant() {
        return goldMerchant;
    }

    public void setGoldMerchant(int goldMerchant) {
        this.goldMerchant = goldMerchant;
    }

    public int getRecharge() {
        return recharge;
    }

    public void setRecharge(int recharge) {
        this.recharge = recharge;
    }

    protected InvoiceRenderParam(Parcel in) {
        id = in.readString();
        invoicePdf = in.readString();
        invoiceUrl = in.readString();
        goldMerchant = in.readInt();
        recharge = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(invoicePdf);
        dest.writeString(invoiceUrl);
        dest.writeInt(goldMerchant);
        dest.writeInt(recharge);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<InvoiceRenderParam> CREATOR
            = new Parcelable.Creator<InvoiceRenderParam>() {
        @Override
        public InvoiceRenderParam createFromParcel(Parcel in) {
            return new InvoiceRenderParam(in);
        }

        @Override
        public InvoiceRenderParam[] newArray(int size) {
            return new InvoiceRenderParam[size];
        }
    };
}
