package com.tokopedia.core.rescenter.edit.customadapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.core.R;
import com.tokopedia.core.database.model.AttachmentResCenterDB;
import com.tokopedia.core.rescenter.utils.ResCenterUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created on 8/28/16.
 */
public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private final AttachmentAdapterListener listener;
    private List<AttachmentResCenterDB> dataSet;

    public interface AttachmentAdapterListener {
        void onClickAddAttachment(View view);
        void onClickOpenAttachment(View view, int position);
        void onClickRemoveAttachment(View view, int position);
    }

    public AttachmentAdapter(AttachmentAdapterListener listener, List<AttachmentResCenterDB> list) {
        this.listener = listener;
        this.dataSet = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView attachment;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mainView = itemView;
            this.attachment = (ImageView) itemView.findViewById(R.id.attachment);
        }
    }

    @Override
    public AttachmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_attachment_rescenter_create, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentAdapter.ViewHolder holder, int position) {
        setValue(holder, position);
        setListener(holder, position);
    }

    private void setListener(final ViewHolder holder, final int position) {
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataSet.size() != 5 && holder.getAdapterPosition() == dataSet.size()) {
                    listener.onClickAddAttachment(view);
                } else {
                    listener.onClickOpenAttachment(view, position);
                }
            }
        });
    }

    private void remove(int position) {
        dataSet.get(position).delete();
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    private void setValue(ViewHolder holder, int position) {
        if (position < dataSet.size()) {
            loadImage(holder, position);
        } else {
            if (dataSet.size() == 5) {
                loadImage(holder, position);
            } else {
                holder.attachment.setImageResource(android.R.color.transparent);
            }
        }
    }

    private void loadImage(ViewHolder holder, int position) {
        File imgFile = new  File(dataSet.get(position).imagePath);
        if(imgFile.exists()){
            Bitmap bitmapFile;
            try {
                bitmapFile = ResCenterUtils.getBitmapFromFile(imgFile.getAbsolutePath());
                holder.attachment.setImageBitmap(bitmapFile);
            } catch (IOException e) {
                e.printStackTrace();
                holder.attachment.setImageResource(R.drawable.remove_thin);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (dataSet.size() == 5) {
            return dataSet.size();
        } else {
            return dataSet.size() + 1;
        }
    }
}
