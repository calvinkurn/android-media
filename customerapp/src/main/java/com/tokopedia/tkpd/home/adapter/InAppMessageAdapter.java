package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
    private InappAdapterLisner inappAdapterLisner;

    public InAppMessageAdapter(Context context, ArrayList<InAppMessageModel> messageList, InappAdapterLisner inappAdapterLisner) {
        this.messageList = messageList;
        this.context = context;
        this.inappAdapterLisner = inappAdapterLisner;
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
        final InAppMessageModel inAppMessageModel = messageList.get(position);
        ImageHandler.LoadImage(holder.img, inAppMessageModel.getImageUrl());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click on " + inAppMessageModel.getDeeplink(), Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                Uri uri = Uri.parse(inAppMessageModel.getDeeplink());
                intent.setData(uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                inappAdapterLisner.onRowItemClick();
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

   public interface InappAdapterLisner {
         void onRowItemClick();
    }
}