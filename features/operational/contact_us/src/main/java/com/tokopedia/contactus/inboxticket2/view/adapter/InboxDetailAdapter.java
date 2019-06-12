package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;

import java.util.List;


public class InboxDetailAdapter extends RecyclerView.Adapter<InboxDetailAdapter.DetailViewHolder> {


    private List<CommentsItem> commentList;
    private Context mContext;
    private InboxDetailContract.InboxDetailPresenter mPresenter;
    private boolean needAttachment;
    private int indexExpanded;
    private boolean searchMode;
    private String searchText;
    private Utils utils;
    private SpannableString hintAttachmentString;


    public InboxDetailAdapter(Context context, List<CommentsItem> data, boolean needAttachment, InboxDetailContract.InboxDetailPresenter presenter) {
        mContext = context;
        commentList = data;
        mPresenter = presenter;
        utils = new Utils(mContext);
        indexExpanded = -1;
        this.needAttachment = needAttachment;
        String src = mContext.getString(R.string.hint_attachment);
        hintAttachmentString = new SpannableString(src);
        hintAttachmentString.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.layout_item_message, parent, false);
        return new DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.bindViewHolder(position);
    }

    public void enterSearchMode(String text) {
        if (text.length() > 0) {
            searchMode = true;
            searchText = text;
            notifyDataSetChanged();
        }
    }

    public void exitSearchMode() {
        searchMode = false;
        searchText = "";
        notifyDataSetChanged();
    }

    public void setNeedAttachment(boolean val) {
        needAttachment = val;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfile;
        private TextView tvName;
        private TextView tvDateRecent;
        private TextView tvComment;
        private TextView tvCollapsedTime;
        private View itemView;
        private RecyclerView rvAttachedImage;
        private TextView tvAttachmentHint;
        private AttachmentAdapter attachmentAdapter;
        private LinearLayoutManager layoutManager;

        DetailViewHolder(View itemView) {
            super(itemView);
            findindViewsId(itemView);
            layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            rvAttachedImage.setLayoutManager(layoutManager);
        }

        private void findindViewsId(View view) {
            ivProfile = view.findViewById(R.id.iv_profile);
            tvName = view.findViewById(R.id.tv_name);
            tvDateRecent = view.findViewById(R.id.tv_date_recent);
            tvComment = view.findViewById(R.id.tv_comment);
            tvCollapsedTime = view.findViewById(R.id.tv_collapsed_time);
            itemView = view.findViewById(R.id.layout_item_message);
            rvAttachedImage = view.findViewById(R.id.rv_attached_image);
            tvAttachmentHint = view.findViewById(R.id.tv_hint_attachment);
        }

        void bindViewHolder(int position) {
            if (commentList.get(position).getAttachment() != null && commentList.get(position).getAttachment().size() > 0) {
                if (attachmentAdapter == null) {
                    attachmentAdapter = new AttachmentAdapter(mContext, commentList.get(position).getAttachment(), mPresenter);
                } else {
                    attachmentAdapter.addAll(commentList.get(position).getAttachment());
                }
                rvAttachedImage.setAdapter(attachmentAdapter);
                rvAttachedImage.setVisibility(View.VISIBLE);
                tvCollapsedTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.attach_rotated, 0, 0, 0);
            } else {
                rvAttachedImage.setVisibility(View.GONE);
                tvCollapsedTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            CommentsItem item = commentList.get(position);
            if (item == null) {
                return;
            }
            if (item.getCreatedBy() != null) {
                ImageHandler.loadImageCircle2(mContext, ivProfile, item.getCreatedBy().getPicture());
                tvName.setText(item.getCreatedBy().getName());
            }
            if (position == commentList.size() - 1 || !commentList.get(position).isCollapsed() || searchMode) {
                tvDateRecent.setText(item.getCreateTime());
                tvCollapsedTime.setText("");
                tvCollapsedTime.setVisibility(View.GONE);
                if (searchMode) {
                    tvComment.setText(utils.getHighlightText(searchText, item.getMessagePlaintext()));
                } else {
                    tvComment.setText(MethodChecker.fromHtml(item.getMessage()));
                }
                tvComment.setVisibility(View.VISIBLE);
                if (position == commentList.size() - 1 && needAttachment) {
                    tvAttachmentHint.setText(hintAttachmentString);
                    tvAttachmentHint.setVisibility(View.VISIBLE);
                } else {
                    tvAttachmentHint.setVisibility(View.GONE);
                }

                if (commentList.get(position).getAttachment() != null && commentList.get(position).getAttachment().size() > 0)
                    rvAttachedImage.setVisibility(View.VISIBLE);
            } else {
                tvAttachmentHint.setVisibility(View.GONE);
                tvDateRecent.setText(MethodChecker.fromHtml(item.getMessage()));
                tvComment.setText("");
                tvCollapsedTime.setText(item.getShortTime());
                tvCollapsedTime.setVisibility(View.VISIBLE);
                tvComment.setVisibility(View.GONE);
                rvAttachedImage.setVisibility(View.GONE);
            }

           itemView.setOnClickListener(this);
           tvComment.setOnClickListener(this);
           tvDateRecent.setOnClickListener(this);

        }

        void toggleCollapse() {
            int tapIndex = getAdapterPosition();
            if (tapIndex != commentList.size() - 1) {
                CommentsItem item = commentList.get(tapIndex);
                boolean isCollapsed = item.isCollapsed();
                item.setCollapsed(!isCollapsed);
            }
            notifyItemChanged(tapIndex);
            ((InboxDetailActivity) mContext).scrollTo(indexExpanded);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id==R.id.layout_item_message||id==R.id.tv_comment||id==R.id.tv_date_recent){
                toggleCollapse();
            }
        }
    }
}
