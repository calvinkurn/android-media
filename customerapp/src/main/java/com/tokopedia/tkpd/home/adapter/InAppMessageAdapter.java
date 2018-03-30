package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.model.InAppMessageModel;

import java.util.ArrayList;

/**
 * Created by ashwanityagi on 30/03/18.
 */

public class InAppMessageAdapter extends RecyclerView.Adapter<InAppMessageAdapter.MessageHolder> {

    private ArrayList<InAppMessageModel> messageList;
    private Context context;

    public InAppMessageAdapter(Context context, ArrayList<InAppMessageModel> messageList) {
        this.messageList = messageList;
        this.context = context;
    }


    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.inapp_message_item, parent, false);
        return new MessageHolder(v);
    }

    /*
    BIND
     */
    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        InAppMessageModel inAppMessageModel = messageList.get(position);
        ImageHandler.LoadImage(holder.img, inAppMessageModel.getImageUrl());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }


    class MessageHolder extends RecyclerView.ViewHolder {

        ImageView img;


        public MessageHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_inapp);
        }
    }
}