package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

    private List<AttachmentItem> attachmentList;
    private Context mContext;
    private ImageHandler imageHandler;
    private InboxDetailPresenter mPresenter;

    AttachmentAdapter(Context context, List<AttachmentItem> data, InboxDetailPresenter presenter) {
        mContext = context;
        attachmentList = new ArrayList<>();
        attachmentList.addAll(data);
        imageHandler = new ImageHandler(mContext);
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_attachment_item, parent, false);
        return new AttachmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int position) {
        holder.bindView(position);
    }

    public void addAll(List<AttachmentItem> attachmentItems) {
        attachmentList.clear();
        attachmentList.addAll(attachmentItems);
    }

    private boolean isUrl(String src) {
        return src.substring(0, 4).equals("http");
    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    class AttachmentViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAttachment;

        AttachmentViewHolder(View itemView) {
            super(itemView);
            ivAttachment = itemView.findViewById(R.id.iv_attachment);
        }

        void bindView(int index) {
            String thumbnail = attachmentList.get(index).getThumbnail();
            if (isUrl(thumbnail)) {
                imageHandler.loadImage(ivAttachment, thumbnail);
            } else {
                ivAttachment.setImageURI(Uri.fromFile(new File(thumbnail)));
            }

            ivAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previewImage();
                }
            });
        }

        void previewImage() {
            mPresenter.showImagePreview(getAdapterPosition(), attachmentList);
        }
    }
}
