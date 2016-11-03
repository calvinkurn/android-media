package com.tokopedia.tkpd.rescenter.detail.customadapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.database.model.AttachmentResCenterDB;
import com.tokopedia.tkpd.rescenter.detail.listener.DetailResCenterView;
import com.tokopedia.tkpd.rescenter.utils.ResCenterUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private final DetailResCenterView listener;
    private List<AttachmentResCenterDB> dataSet;

    public AttachmentAdapter(DetailResCenterView view, List<AttachmentResCenterDB> list) {
        this.listener = view;
        this.dataSet = list;
    }

    public void setDataSet(List<AttachmentResCenterDB> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public List<AttachmentResCenterDB> getItemList() {
        return dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView attachment;
        View removeAttachment;
        View loadingAttachment;
        public ViewHolder(View itemView) {
            super(itemView);
            this.attachment = (ImageView) itemView.findViewById(R.id.attachment);
            this.removeAttachment = itemView.findViewById(R.id.remove);
            this.loadingAttachment = itemView.findViewById(R.id.loading);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setVisibility();
    }

    @Override
    public AttachmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_attachment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentAdapter.ViewHolder holder, int position) {
        setValue(holder, position);
        setListener(holder, position);
    }

    private void setValue(ViewHolder holder, int position) {
        File imgFile = new  File(dataSet.get(position).imagePath);
        if(imgFile.exists()){
            Bitmap bitmapFile;
            try {
                bitmapFile = ResCenterUtils.getBitmapFromFile(imgFile.getAbsolutePath());
                holder.attachment.setImageBitmap(bitmapFile);
                holder.removeAttachment.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                holder.attachment.setImageResource(R.drawable.remove_thin);
                holder.removeAttachment.setVisibility(View.GONE);
            }
        }
    }

    private void setListener(ViewHolder holder, int position) {
        holder.removeAttachment.setOnClickListener(OnRemoveAttachment(holder));
        holder.attachment.setOnClickListener(OnRemoveAttachment(holder));
    }

    private View.OnClickListener OnRemoveAttachment(final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSet.get(holder.getAdapterPosition()).delete();
                dataSet.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                setVisibility();
            }
        };
    }

    private void setVisibility() {
        if (getItemCount() > 0) {
            listener.setAttachmentArea(true);
        } else {
            listener.setAttachmentArea(false);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
