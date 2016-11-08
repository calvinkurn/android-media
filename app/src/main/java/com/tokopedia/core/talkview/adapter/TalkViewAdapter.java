package com.tokopedia.core.talkview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.talkview.fragment.TalkViewFragment;
import com.tokopedia.core.talkview.model.TalkBaseModel;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.TokenHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by stevenfredian on 5/10/16.
 */
public abstract class TalkViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<TalkBaseModel> items;
    private OnRetryListener listener;
    protected TalkViewFragment fragment;
    protected boolean retry;
    protected boolean loading;
    protected LabelUtils label;
    protected TokenHandler token;

    public static class TalkViewHolder extends RecyclerView.ViewHolder {
        @Bind(R2.id.user_ava)
        public ImageView userImageView;
        @Bind(R2.id.user_name)
        public TextView userView;
        @Bind(R2.id.create_time)
        public TextView timeView;
        @Bind(R2.id.message)
        public TextView messageView;
        @Bind(R2.id.rank)
        public TextView rank;
        @Bind(R2.id.but_overflow_comment)
        public View buttonOverflow;
        @Bind(R2.id.reputation)
        public LinearLayout reputation;
        @Bind(R2.id.reputation_user)
        public View reputationUser;
        @Bind(R2.id.rep_icon)
        public ImageView iconReputation;
        @Bind(R2.id.rep_rating)
        public TextView textReputation;


        public TalkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (getType(position)) {
            case TalkBaseModel.LOADING:
                return TkpdState.RecyclerView.VIEW_LOADING;
            case TalkBaseModel.RETRY:
                return TkpdState.RecyclerView.VIEW_RETRY;
            case TalkBaseModel.EMPTY:
                return TkpdState.RecyclerView.VIEW_EMPTY;
            case TalkBaseModel.MAIN:
                return 123456789;
            default:
                return TkpdState.RecyclerView.VIEW_UNKNOWN;
        }
    }

    private int getType(int position) {
        return items.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TkpdState.RecyclerView.VIEW_LOADING:
                return createViewLoading(viewGroup);
            case TkpdState.RecyclerView.VIEW_RETRY:
                return createViewRetry(viewGroup);
            case TkpdState.RecyclerView.VIEW_EMPTY:
                return createViewEmpty(viewGroup);
            case 123456789:
                return createViewLayout(viewGroup);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TkpdState.RecyclerView.VIEW_RETRY) {
            bindRetryHolder((ViewHolderRetry) holder);
        } else if (getItemViewType(position) == 123456789)
            bindTalkView((TalkViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnRetryListener {
        void onRetryCliked();
    }

    public void setOnRetryListenerRV(TalkViewAdapter.OnRetryListener listener) {
        this.listener = listener;
    }

    private RecyclerView.ViewHolder createViewLayout(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_comment, viewGroup, false);
        return new TalkViewHolder(view);

    }

    private ViewHolder createViewEmpty(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_no_result, parent,false);
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolder(view);
    }

    public static class ViewHolderRetry extends RecyclerView.ViewHolder {
        TextView retry;

        public ViewHolderRetry(View itemView) {
            super(itemView);
            retry = (TextView) itemView.findViewById(R.id.main_retry);
        }
    }

    private ViewHolderRetry createViewRetry(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_retry_footer, viewGroup, false);
        return new ViewHolderRetry(view);
    }

    public ViewHolder createViewLoading(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.loading_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    private void bindRetryHolder(ViewHolderRetry viewHolder) {
        viewHolder.retry.setOnClickListener(onRetryListener());
    }

    private View.OnClickListener onRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.set(0, TalkBaseModel.create(TalkBaseModel.LOADING));
                notifyDataSetChanged();
                listener.onRetryCliked();
            }
        };
    }

    protected abstract void bindTalkView(TalkViewHolder holder, final int position);








}
