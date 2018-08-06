package com.tokopedia.topchat.attachinvoice.view.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.attachinvoice.view.model.InvoiceViewModel;

/**
 * Created by Hendri on 22/03/18.
 */

public class InvoiceViewHolder extends AbstractViewHolder<InvoiceViewModel> {
    public static int LAYOUT = R.layout.item_invoice_attach;
    private TextView invoiceNo;
    private TextView invoiceDate;
    private TextView productName;
    private TextView productDesc;
    private TextView invoiceStatus;
    private TextView totalAmount;
    private ImageView productImage;

    public InvoiceViewHolder(View itemView) {
        super(itemView);
        invoiceNo = itemView.findViewById(R.id.attach_invoice_item_invoice_no);
        invoiceDate = itemView.findViewById(R.id.attach_invoice_item_invoice_date);
        productName = itemView.findViewById(R.id.attach_invoice_item_product_name);
        productDesc = itemView.findViewById(R.id.attach_invoice_item_product_desc);
        invoiceStatus = itemView.findViewById(R.id.attach_invoice_item_invoice_status);
        totalAmount = itemView.findViewById(R.id.attach_invoice_item_invoice_total);
        productImage = itemView.findViewById(R.id.attach_invoice_item_product_image);
    }


    @Override
    public void bind(InvoiceViewModel element) {
        invoiceNo.setText(element.getInvoiceNumber());
        if (element.getProductTopImage() != null && !TextUtils.isEmpty(element.getProductTopImage
                ())) {
            productImage.setVisibility(View.VISIBLE);
            ImageHandler.loadImageAndCache(productImage, element.getProductTopImage());
        } else {
            productImage.setVisibility(View.GONE);
        }
        invoiceDate.setText(element.getDate());
        productName.setText(element.getProductTopName());
        productDesc.setText(element.getDescription());
        invoiceStatus.setText(element.getStatus());
        totalAmount.setText(element.getTotal());
    }
}
