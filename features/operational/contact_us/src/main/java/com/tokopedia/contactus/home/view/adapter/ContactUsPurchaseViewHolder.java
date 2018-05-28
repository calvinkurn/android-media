package com.tokopedia.contactus.home.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.orderquery.view.OrderQueryTicketActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsPurchaseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.txt_order_id)
        TextView txtOrderId;

        @BindView(R2.id.txt_order_date)
        TextView txtOrderDate;

        @BindView(R2.id.img_product)
        ImageView imgProduct;

        @BindView(R2.id.txt_product_name)
        TextView txtProductName;

        @BindView(R2.id.txt_more_item)
        TextView txtMoreItem;

        @BindView(R2.id.txt_invalid_msg)
        TextView txtInvalidMsg;

        @BindView(R2.id.txt_total_price)
        TextView txtTotalPrice;

        BuyerPurchaseList buyerPurchaseList;
        ContactUsPurchaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(OrderQueryTicketActivity.getOrderQueryTicketIntent(view.getContext(),buyerPurchaseList));
                }
            });
        }

        void bind(BuyerPurchaseList buyerPurchaseList) {
            this.buyerPurchaseList = buyerPurchaseList;
            txtOrderId.setText(buyerPurchaseList.getDetail().getCode());
            txtOrderDate.setText(lastUpdatedDate(buyerPurchaseList.getDetail().getCreateTime()));
            ImageHandler.loadImageThumbs(imgProduct.getContext(),imgProduct,buyerPurchaseList.getProducts().get(0).getImage());
            txtProductName.setText(buyerPurchaseList.getProducts().get(0).getName());
            txtMoreItem.setText("+"+(buyerPurchaseList.getProducts().size()-1)+" barang lainnya");
            txtTotalPrice.setText(buyerPurchaseList.getDetail().getTotalAmount());
            txtInvalidMsg.setText(buyerPurchaseList.getDetail().getStatus());
        }
    public static String lastUpdatedDate(String date) {
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
