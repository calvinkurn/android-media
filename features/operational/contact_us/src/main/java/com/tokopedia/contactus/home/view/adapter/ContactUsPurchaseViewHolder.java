package com.tokopedia.contactus.home.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.orderquery.view.OrderQueryTicketActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class ContactUsPurchaseViewHolder extends RecyclerView.ViewHolder {


    private TextView txtOrderId;
    private TextView txtOrderDate;
    private ImageView imgProduct;
    private TextView txtProductName;
    private TextView txtMoreItem;
    private TextView txtInvalidMsg;
    private TextView txtTotalPrice;

    private BuyerPurchaseList buyerPurchaseList;

    ContactUsPurchaseViewHolder(View view, String type) {
        super(view);
        findingViewsId(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactUsTracking.eventHomeInvoiceClick(type,txtOrderId.getText().toString());
                view.getContext().startActivity(OrderQueryTicketActivity.getOrderQueryTicketIntent(view.getContext(), buyerPurchaseList));
            }
        });
    }

    private void findingViewsId(View view) {
        txtOrderId  = view.findViewById(R.id.txt_order_id);
        txtOrderDate  = view.findViewById(R.id.txt_order_date);
        imgProduct  = view.findViewById(R.id.img_product);
        txtProductName  = view.findViewById(R.id.txt_product_name);
        txtMoreItem  = view.findViewById(R.id.txt_more_item);
        txtInvalidMsg  = view.findViewById(R.id.txt_invalid_msg);
        txtTotalPrice  = view.findViewById(R.id.txt_total_price);
    }

    void bind(BuyerPurchaseList buyerPurchaseList) {
        this.buyerPurchaseList = buyerPurchaseList;
        txtOrderId.setText(buyerPurchaseList.getDetail().getCode());
        txtOrderDate.setText(lastUpdatedDate(buyerPurchaseList.getDetail().getCreateTime()));
        ImageHandler.loadImageThumbs(imgProduct.getContext(), imgProduct, buyerPurchaseList.getProducts().get(0).getImage());
        txtProductName.setText(buyerPurchaseList.getProducts().get(0).getName());
        if(buyerPurchaseList.getProducts().size() > 1) {
            txtMoreItem.setText(String.format(txtMoreItem.getContext().getResources().getString(R.string.more_items),
                    buyerPurchaseList.getProducts().size() - 1));
        }else {
            txtMoreItem.setVisibility(View.INVISIBLE);
        }
        txtTotalPrice.setText(buyerPurchaseList.getDetail().getTotalAmount());
        txtInvalidMsg.setText(buyerPurchaseList.getDetail().getStatus());
    }

    private static String lastUpdatedDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        String formattedTime = "";
        try {
            Date d = sdf.parse(date);
            formattedTime = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }
}
