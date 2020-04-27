package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ReviewTips;

import java.util.ArrayList;

/**
 * @author by nisie on 9/26/17.
 */

public class ReviewTipsAdapter extends RecyclerView.Adapter<ReviewTipsAdapter.ViewHolder> {

    ArrayList<ReviewTips> list;
    private boolean isExpanded;
    private Context context;

    private ReviewTipsAdapter(Context context) {
        this.list = new ArrayList<>();
        this.isExpanded = false;
        this.context = context;
    }

    public static ReviewTipsAdapter createInstance(Context context) {
        return new ReviewTipsAdapter(context);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public void collapse() {
        int size = this.list.size();
        this.list.clear();
        notifyItemRangeRemoved(0, size);
        setExpanded(false);
    }

    public void expand() {

        list.add(new ReviewTips(context.getString(R.string.tips1_title),
                context.getString(R.string.tips1_tips)
        ));
        list.add(new ReviewTips(context.getString(R.string.tips2_title),
                context.getString(R.string.tips2_tips)
        ));
        list.add(new ReviewTips(context.getString(R.string.tips3_title),
                context.getString(R.string.tips3_tips)
        ));
        notifyItemRangeInserted(0, list.size());
        setExpanded(true);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView tips;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    @Override
    public ReviewTipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_review_tips, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewTipsAdapter.ViewHolder holder, int position) {
        holder.tips.setText(list.get(position).getTips());
        holder.title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
