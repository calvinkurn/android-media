package com.tokopedia.contactus.inboxticket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.customadapter.ImageUploadAdapter;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.TicketReplyDatum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 4/25/16.
 */
public class TicketDataBinder extends DataBinder<TicketDataBinder.ViewHolder> {

    private static final String TICKET_CLOSED = "2";
    private static final String TICKET_OPEN = "1";
    private static final String TICKET_RATING_BAD = "2";
    private static final String TICKET_RATING_GOOD = "1";
    private static final String STATE_ADMIN = "1";

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.user_ava)
        ImageView userAva;

        @BindView(R2.id.user_name)
        TextView userName;

        @BindView(R2.id.create_time)
        TextView createTime;

        @BindView(R2.id.image_upload_layout)
        RecyclerView imageUploadLayout;

        @BindView(R2.id.message)
        TextView message;

        LabelUtils label;
        ImageUploadAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            label = LabelUtils.getInstance(itemView.getContext(), userName);
            adapter = ImageUploadAdapter.createAdapter(itemView.getContext());
            imageUploadLayout.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            imageUploadLayout.setAdapter(adapter);
        }
    }


    private List<TicketReplyDatum> list;
    private Context context;

    public TicketDataBinder(DataBindAdapter dataBindAdapter, List<TicketReplyDatum> list, Context context) {
        super(dataBindAdapter);
        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_ticket_detail2, parent, false));
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(list.get(position).getTicketDetailUserName());
        holder.label.giveLabel(list.get(position).getTicketDetailUserLabel());
        switch (list.get(position).getTicketDetailIsCs()) {
            case STATE_ADMIN:
                holder.userName.setTextColor(context.getResources().getColor(R.color.black));
                holder.userName.setOnClickListener(null);
                break;
            default:
                holder.userName.setTextColor(context.getResources().getColorStateList(R.color.href_link));
                holder.userName.setOnClickListener(onUsernameClicked(position));
                break;
        }
        holder.createTime.setText(list.get(position).getTicketDetailCreateTimeFmt());
        ImageHandler.loadImageCircle2(context, holder.userAva, list.get(position).getTicketDetailUserImage());

        if (!list.get(position).getTicketDetailMessage().equals("")) {
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(list.get(position).getTicketDetailMessage());
        } else {
            holder.message.setVisibility(View.GONE);
        }

        if (list.get(position).getTicketDetailAttachment() != null && list.get(position).getTicketDetailAttachment().size() > 0) {
            holder.imageUploadLayout.setVisibility(View.VISIBLE);
            List<ImageUpload> listImage = new ArrayList<>();
            for (int i = 0; i < list.get(position).getTicketDetailAttachment().size(); i++) {
                ImageUpload image = new ImageUpload();
                image.setPicSrc(list.get(position).getTicketDetailAttachment().get(i).getImgSrc());
                image.setPicSrcLarge(list.get(position).getTicketDetailAttachment().get(i).getImgLink());
                listImage.add(image);
            }
            holder.adapter.addList(listImage);
            holder.adapter.setListener(onImageClickedListener());
            holder.adapter.notifyDataSetChanged();
        } else {
            holder.imageUploadLayout.setVisibility(View.GONE);
        }

    }

    private View.OnClickListener onUsernameClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getApplicationContext() instanceof ContactUsModuleRouter) {
                    context.startActivity(
                            ((ContactUsModuleRouter) context.getApplicationContext())
                                    .getTopProfileIntent(context, list.get(position)
                                            .getTicketDetailUserId())
                    );
                }
            }
        };
    }

    private ImageUploadAdapter.ProductImageListener onImageClickedListener() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return null;
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, final ArrayList<ImageUpload> imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PreviewProductImage.class);
                        Bundle bundle = new Bundle();
                        ArrayList<String> listImage = new ArrayList<>();
                        for (int i = 0; i < imageUpload.size(); i++) {
                            listImage.add(imageUpload.get(i).getPicSrcLarge());
                        }
                        bundle.putStringArrayList("fileloc", listImage);
                        bundle.putInt("img_pos", position);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                };
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<TicketReplyDatum> ticketReplyData) {
        this.list.clear();
        this.list.addAll(ticketReplyData);
    }

    public List<TicketReplyDatum> getData() {
        return list;
    }
}
