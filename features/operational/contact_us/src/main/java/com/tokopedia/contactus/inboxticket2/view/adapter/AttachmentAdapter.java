package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pranaymohapatra on 05/07/18.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

    private List<AttachmentItem> attachmentList;
    private Context mContext;
    private ImageHandler imageHandler;

    public AttachmentAdapter(Context context, List<AttachmentItem> data) {
        mContext = context;
        attachmentList = new ArrayList<>();
        attachmentList.addAll(data);
        imageHandler = new ImageHandler(mContext);
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_attachment_item, parent, false);
        return new AttachmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AttachmentViewHolder holder, int position) {
        holder.bindView(position);
    }

    public void addAll(List<AttachmentItem> attachmentItems) {
        attachmentList.clear();
        attachmentList.addAll(attachmentItems);
    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    class AttachmentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_attachment)
        ImageView ivAttachment;

        AttachmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(int index) {
            imageHandler.loadImage(ivAttachment, attachmentList.get(index).getThumbnail());
        }
    }
}
