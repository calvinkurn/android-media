package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 02/07/18.
 */

public class InboxDetailAdapter extends RecyclerView.Adapter<InboxDetailAdapter.DetailViewHolder> {


    private List<CommentsItem> commentList;
    private Context mContext;
    private InboxDetailContract.InboxDetailPresenter mPresenter;
    private ImageHandler imageHandler;
    private boolean collapsed;
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
        imageHandler = new ImageHandler(mContext);
        collapsed = true;
        utils = new Utils(mContext);
        indexExpanded = -1;
        this.needAttachment = needAttachment;
        String src = mContext.getString(R.string.hint_attachment);
        hintAttachmentString = new SpannableString(src);
        hintAttachmentString.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.layout_item_message, parent, false);
        return new DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        holder.bindViewHolder(position);
    }

    public void enterSearchMode(String text) {
        if (text.length() > 0) {
            collapsed = false;
            searchMode = true;
            searchText = text;
            notifyDataSetChanged();
        }
    }

    public void exitSearchMode() {
        collapsed = true;
        searchMode = false;
        searchText = "";
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_profile)
        ImageView ivProfile;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_date_recent)
        TextView tvDateRecent;
        @BindView(R2.id.tv_comment)
        TextView comment;
        @BindView(R2.id.tv_collapsed_time)
        TextView tvCollapsedTime;
        @BindView(R2.id.layout_item_message)
        View itemView;
        @BindView(R2.id.rv_attached_image)
        RecyclerView rvAttachedImage;
        @BindView(R2.id.tv_hint_attachment)
        TextView tvAttachmentHint;

        private AttachmentAdapter attachmentAdapter;
        private LinearLayoutManager layoutManager;

        DetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            rvAttachedImage.setLayoutManager(layoutManager);
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
            imageHandler.loadImage(ivProfile, item.getCreatedBy().getPicture());
            tvName.setText(item.getCreatedBy().getName());
            if (position == indexExpanded || position == commentList.size() - 1 || searchMode) {
                tvDateRecent.setText(item.getCreateTime());
                tvCollapsedTime.setText("");
                if (searchMode) {
                    comment.setText(utils.getHighlightText(searchText, item.getMessagePlaintext()));
                } else {
                    comment.setText(item.getMessagePlaintext());
                }
                comment.setVisibility(View.VISIBLE);
                if (position == commentList.size() - 1 && needAttachment) {
                    tvAttachmentHint.setText(hintAttachmentString);
                    tvAttachmentHint.setVisibility(View.VISIBLE);
                } else {
                    tvAttachmentHint.setVisibility(View.GONE);
                }
                if (commentList.get(position).getAttachment() != null && commentList.get(position).getAttachment().size() > 0)
                    rvAttachedImage.setVisibility(View.VISIBLE);
            } else {
                tvDateRecent.setText(item.getMessagePlaintext());
                comment.setText("");
                tvCollapsedTime.setText(item.getShortTime());
                comment.setVisibility(View.GONE);
                rvAttachedImage.setVisibility(View.GONE);
            }

        }

        @OnClick({
                R2.id.layout_item_message,
                R2.id.tv_comment,
                R2.id.tv_date_recent
        })
        void toggleCollapse() {
            int tapIndex = getAdapterPosition();
            if (tapIndex != commentList.size() - 1) {
                if (indexExpanded != tapIndex) {
                    indexExpanded = tapIndex;
                    collapsed = false;
                } else {
                    indexExpanded = commentList.size() - 1;
                    collapsed = true;
                }
                notifyItemRangeChanged(0, commentList.size() - 1);
                if (!collapsed)
                    ((InboxDetailActivity) mContext).scrollTo(indexExpanded);
                else
                    ((InboxDetailActivity) mContext).scrollTo(0);
            }
        }
    }
}
