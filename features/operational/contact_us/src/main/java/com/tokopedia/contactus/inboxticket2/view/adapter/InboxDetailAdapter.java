package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private ViewRplyButtonListener viewRplyButtonListener;
    
    private static final String KEY_LIKED = "101";
    private static final String KEY_DIS_LIKED = "102";


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
        viewRplyButtonListener = (ViewRplyButtonListener) context;
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
        holder.bindViewHolder(position,mPresenter);
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

    class DetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_profile)
        ImageView ivProfile;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_date_recent)
        TextView tvDateRecent;
        @BindView(R2.id.tv_comment)
        TextView tvComment;
        @BindView(R2.id.tv_collapsed_time)
        TextView tvCollapsedTime;
        @BindView(R2.id.layout_item_message)
        View itemView;
        @BindView(R2.id.rv_attached_image)
        RecyclerView rvAttachedImage;
        @BindView(R2.id.tv_hint_attachment)
        TextView tvAttachmentHint;

        ImageView ratingThumbsUp;
        ImageView ratingThumbsDown;
        private AttachmentAdapter attachmentAdapter;
        private LinearLayoutManager layoutManager;

        DetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ratingThumbsUp = itemView.findViewById(R.id.iv_csast_status_good);
            ratingThumbsDown = itemView.findViewById(R.id.iv_csast_status_bad);

            layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            rvAttachedImage.setLayoutManager(layoutManager);
        }

        void bindViewHolder(int position, InboxDetailContract.InboxDetailPresenter mPresenter) {
            if (commentList.get(position).getAttachment() != null && commentList.get(position).getAttachment().size() > 0) {
                if (attachmentAdapter == null) {
                    attachmentAdapter = new AttachmentAdapter(mContext, commentList.get(position).getAttachment(), InboxDetailAdapter.this.mPresenter);
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
            if(item.getRating()!=null && item.getRating().equals(KEY_DIS_LIKED)){
                ratingThumbsDown.setVisibility(View.VISIBLE);
                ratingThumbsDown.setColorFilter(ContextCompat.getColor(mContext, R.color.red_600));
                ratingThumbsUp.setVisibility(View.GONE);
            }else if(item.getRating()!=null && item.getRating().equals(KEY_LIKED)) {
                ratingThumbsUp.setVisibility(View.VISIBLE);
                ratingThumbsUp.setColorFilter(ContextCompat.getColor(mContext, R.color.g_500));
                ratingThumbsDown.setVisibility(View.GONE);
            }
            if (position == commentList.size() - 1 || !commentList.get(position).isCollapsed() || searchMode) {
                tvDateRecent.setText(item.getCreateTime());
                tvCollapsedTime.setText("");
                tvCollapsedTime.setVisibility(View.GONE);
//                if(item.getRating()!=null){
//                    if(mPresenter.getTicketStatus().equalsIgnoreCase(utils.CLOSED) && !item.getRating().equals(KEY_LIKED) && !item.getRating().equals(KEY_DIS_LIKED)){
//                        ratingThumbsUp.setVisibility(View.GONE);
//                        ratingThumbsDown.setVisibility(View.GONE);
//                    }
//                    if(!mPresenter.getTicketStatus().equalsIgnoreCase(utils.CLOSED) && item.getCreatedBy().getRole().equals("agent") && !item.getRating().equals(KEY_LIKED) && !item.getRating().equals(KEY_DIS_LIKED) ){
//                        ratingThumbsUp.setVisibility(View.VISIBLE);
//                        ratingThumbsDown.setVisibility(View.VISIBLE);
//                        ratingThumbsUp.clearColorFilter();
//                        ratingThumbsDown.clearColorFilter();
//                    }
//                }
//
//                if(!item.getCreatedBy().getRole().equals("agent")||item.getRating()==null){
//                    ratingThumbsUp.setVisibility(View.GONE);
//                    ratingThumbsDown.setVisibility(View.GONE);
//               }

                if(!mPresenter.getTicketStatus().equalsIgnoreCase(utils.CLOSED) && item.getCreatedBy().getRole().equals("agent")&&(item.getRating()==null|| item.getRating().equals(""))){
                      ratingThumbsUp.setVisibility(View.VISIBLE);
                      ratingThumbsDown.setVisibility(View.VISIBLE);
                      ratingThumbsUp.clearColorFilter();
                      ratingThumbsDown.clearColorFilter();
                      viewRplyButtonListener.viewRplyButtonVisibility();
                }

                if((mPresenter.getTicketStatus().equalsIgnoreCase(utils.CLOSED) && item.getRating()!=null && !item.getRating().equals(KEY_LIKED) && !item.getRating().equals(KEY_DIS_LIKED))|| !item.getCreatedBy().getRole().equals("agent")|| item.getId()==null){
                      ratingThumbsUp.setVisibility(View.GONE);
                      ratingThumbsDown.setVisibility(View.GONE);
                }
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
                ratingThumbsUp.setVisibility(View.GONE);
                ratingThumbsDown.setVisibility(View.GONE);
                tvComment.setVisibility(View.GONE);
                rvAttachedImage.setVisibility(View.GONE);
            }


            ratingThumbsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getRating()!= null &&(item.getRating().equals(KEY_LIKED)||item.getRating().equals(KEY_DIS_LIKED))){

                    }else{
                        ratingThumbsUp.setColorFilter(ContextCompat.getColor(mContext, R.color.g_500));
                        ratingThumbsDown.setVisibility(View.GONE);
                        mPresenter.onClick("yes",position,item.getId());
                    }

                }
            });

            ratingThumbsDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getRating().equals(KEY_LIKED)||item.getRating().equals(KEY_DIS_LIKED)){
                    }else{
                    ratingThumbsDown.setColorFilter(ContextCompat.getColor(mContext, R.color.red_600));
                    ratingThumbsUp.setVisibility(View.GONE);
                    mPresenter.onClick("no",position,item.getId());
                    }
                }
            });

        }

        @OnClick({
                R2.id.layout_item_message,
                R2.id.tv_comment,
                R2.id.tv_date_recent
        })
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
    }

   public interface ViewRplyButtonListener{
        void viewRplyButtonVisibility();
    }
}
