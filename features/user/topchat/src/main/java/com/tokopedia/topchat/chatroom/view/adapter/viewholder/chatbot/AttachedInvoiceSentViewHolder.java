package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.topchat.common.util.ChatTimeConverter;

import java.util.Date;

/**
 * Created by Hendri on 27/03/18.
 */

public class AttachedInvoiceSentViewHolder extends BaseChatViewHolder<AttachInvoiceSentViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.attached_invoice_sent_chat_item;

    private TextView productName;
    private TextView productDesc;
    private TextView totalAmount;
    private ImageView productImage;

    ImageView chatStatus;
    private ImageView action;
    ChatRoomContract.View viewListener;
    private String ROLE_USER = "User";
    private Context context;
    private long dateTimeInMilis;

    public AttachedInvoiceSentViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        productName = itemView.findViewById(R.id.attach_invoice_sent_item_product_name);
        productDesc = itemView.findViewById(R.id.attach_invoice_sent_item_product_desc);
        totalAmount = itemView.findViewById(R.id.attach_invoice_sent_item_invoice_total);
        productImage = itemView.findViewById(R.id.attach_invoice_sent_item_product_image);
        chatStatus = itemView.findViewById(R.id.chat_status);
        action = itemView.findViewById(R.id.left_action);
    }



    @Override
    public void bind(AttachInvoiceSentViewModel element) {
        prerequisiteUISetup(element);
        productName.setText(element.getMessage());
        productDesc.setText(element.getDescription());
        totalAmount.setText(element.getTotalAmount());
        if(!TextUtils.isEmpty(element.getImageUrl())){
            productImage.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(productImage, element.getImageUrl());
        }
        else {
            productImage.setVisibility(View.GONE);
        }
    }

    protected void prerequisiteUISetup(final AttachInvoiceSentViewModel element){
        action.setVisibility(View.GONE);

        chatStatus.setImageResource(element.isDummy() ?
                R.drawable.ic_chat_pending :
                (element.isRead() ?
                        R.drawable.ic_chat_read :
                        R.drawable.ic_chat_unread));
    }

    public void onViewRecycled() {
        if(productImage != null) {
            Glide.clear(productImage);
        }
    }
}
