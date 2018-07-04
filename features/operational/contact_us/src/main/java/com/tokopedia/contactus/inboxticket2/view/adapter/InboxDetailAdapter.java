package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;

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

    public InboxDetailAdapter(Context context, List<CommentsItem> data, InboxDetailContract.InboxDetailPresenter presenter) {
        mContext = context;
        commentList = data;
        mPresenter = presenter;
        imageHandler = new ImageHandler(mContext);
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
        @BindView(R2.id.layout_item_message)
        View itemView;

        DetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(int position) {
            CommentsItem item = commentList.get(position);
            imageHandler.loadImage(ivProfile, item.getCreatedBy().getPicture());
            tvName.setText(item.getCreatedBy().getName());
            if (!collapsed) {
                tvDateRecent.setText(item.getCreateTime());
                comment.setText(item.getMessagePlaintext());
                comment.setVisibility(View.VISIBLE);
            } else {
                tvDateRecent.setText(item.getMessagePlaintext());
                comment.setText("");
                comment.setVisibility(View.GONE);
            }
        }

        @OnClick({
                R2.id.layout_item_message,
                R2.id.tv_comment,
                R2.id.tv_date_recent
        })
        void toggleCollapse() {
            if (getAdapterPosition() != commentList.size() - 1) {
                collapsed = !collapsed;
                notifyItemRangeChanged(0, commentList.size() - 1);
            }
        }
    }
}
