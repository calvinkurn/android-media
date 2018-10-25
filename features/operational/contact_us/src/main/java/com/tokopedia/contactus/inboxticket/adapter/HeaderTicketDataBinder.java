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
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.contactus.inboxticket.presenter.InboxTicketDetailFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 4/25/16.
 */
public class HeaderTicketDataBinder extends DataBinder<HeaderTicketDataBinder.ViewHolder> {


    private static final int ON_GOING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.status)
        TextView status;

        @BindView(R2.id.title)
        TextView title;

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

        @BindView(R2.id.view_more_layout)
        View viewMoreLayout;

        @BindView(R2.id.view_more)
        TextView viewMore;

        LabelUtils label;
        ImageUploadAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            label = LabelUtils.getInstance(itemView.getContext(), userName);
            adapter = ImageUploadAdapter.createAdapter(itemView.getContext());
            imageUploadLayout.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
            imageUploadLayout.setAdapter(adapter);
        }
    }

    InboxTicketDetail data;
    Context context;
    InboxTicketDetailFragmentPresenter presenter;

    public HeaderTicketDataBinder(DataBindAdapter dataBindAdapter, InboxTicketDetail data, Context context) {
        super(dataBindAdapter);
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_header_ticket_detail, parent, false));
        return holder;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        if (data.getTicket().getTicketStatus() == ON_GOING) {
            holder.status.setText(context.getString(R.string.title_status_1));
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_orange));
        } else {
            holder.status.setText(context.getString(R.string.title_status_2));
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_gray));
        }

        holder.title.setText(data.getTicket().getTicketTitle());
        holder.userName.setText(data.getTicket().getTicketFirstMessageName());
        holder.userName.setOnClickListener(onUserNameClicked());
        if(data.getTicket().getTicketUserLabel() != null && !data.getTicket().getTicketUserLabel().equals(""))
        holder.label.giveLabel(data.getTicket().getTicketUserLabel());
        ImageHandler.loadImageCircle2(context, holder.userAva, data.getTicket().getTicketFirstMessageImage());
        holder.createTime.setText(data.getTicket().getTicketCreateTimeFmt());
        holder.message.setText(data.getTicket().getTicketFirstMessage());

        if (data.getTicket().getTicketAttachment() != null && data.getTicket().getTicketAttachment().size() > 0) {
            holder.imageUploadLayout.setVisibility(View.VISIBLE);
            List<ImageUpload> listImage = new ArrayList<>();
            for (int i = 0; i < data.getTicket().getTicketAttachment().size(); i++) {
                ImageUpload image = new ImageUpload();
                image.setPicSrc(data.getTicket().getTicketAttachment().get(i).getImgSrc());
                image.setPicSrcLarge(data.getTicket().getTicketAttachment().get(i).getImgLink());
                listImage.add(image);
            }
            holder.adapter.addList(listImage);
            holder.adapter.setListener(onImageClickedListener());
        }else{
            holder.imageUploadLayout.setVisibility(View.GONE);
        }

        if (data.getTicketReply().getTicketReplyData() != null && data.getTicketReply().getTicketReplyData().size() < data.getTicket().getTicketTotalMessage() - 1) {
            holder.viewMoreLayout.setVisibility(View.VISIBLE);
        } else {
            holder.viewMoreLayout.setVisibility(View.GONE);
        }

        holder.viewMore.setOnClickListener(onViewMoreClicked());

    }

    private View.OnClickListener onViewMoreClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getViewMore();
            }
        };
    }

    private View.OnClickListener onUserNameClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getApplicationContext() instanceof ContactUsModuleRouter) {
                    context.startActivity(
                            ((ContactUsModuleRouter) context.getApplicationContext())
                                    .getTopProfileIntent(context,
                                            SessionHandler.getLoginID(context))
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
            public View.OnClickListener onImageClicked(int position, final ArrayList<ImageUpload> imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PreviewProductImage.class);
                        Bundle bundle = new Bundle();
                        ArrayList<String> listImage = new ArrayList<>();
                        for(int i = 0 ; i < imageUpload.size(); i++){
                            listImage.add(imageUpload.get(i).getPicSrcLarge());
                        }
                        bundle.putStringArrayList("fileloc", listImage);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                };
            }
        };
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setPresenter(InboxTicketDetailFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    public void setData(InboxTicketDetail data) {
        this.data = data;
    }

    public InboxTicketDetail getData() {
        return data;
    }
}
