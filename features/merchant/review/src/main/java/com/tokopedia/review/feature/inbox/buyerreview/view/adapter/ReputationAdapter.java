package com.tokopedia.review.feature.inbox.buyerreview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SmileyModel;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;

/**
 * @author by nisie on 8/28/17.
 */

public class ReputationAdapter extends RecyclerView.Adapter<ReputationAdapter.ViewHolder> {

    public static final String SMILEY_BAD = "-1";
    public static final String SMILEY_NEUTRAL = "1";
    public static final String SMILEY_GOOD = "2";
    private boolean canGiveReputation;

    public ArrayList<SmileyModel> getList() {
        return list;
    }

    public void setList(ArrayList<SmileyModel> list) {
        this.list = list;
    }

    public interface ReputationListener {
        void onReputationSmileyClicked(String name, String value);

        void onGoToShopDetail(long shopId);

        void onGoToPeopleProfile(long userId);
    }

    ArrayList<SmileyModel> list;
    ReputationListener listener;
    Context context;

    private ReputationAdapter(Context context, ReputationListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
        this.canGiveReputation = true;
        this.context = context;
    }

    public static ReputationAdapter createInstance(Context context, ReputationListener listener) {
        return new ReputationAdapter(context, listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView smiley;
        Typography smileyText;
        View main;

        public ViewHolder(View itemView) {
            super(itemView);
            smiley = (ImageView) itemView.findViewById(R.id.smiley);
            smileyText = (Typography) itemView.findViewById(R.id.smiley_name);
            main = itemView.findViewById(R.id.main);
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && canGiveReputation)
                        listener.onReputationSmileyClicked(list.get(getAdapterPosition())
                                .getName(), list.get(getAdapterPosition()).getScore());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_smiley, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(holder.smiley,
                list.get(position).getResId());

        if(list.get(position).getName().isEmpty())
            holder.smileyText.setVisibility(View.GONE);
        else
            holder.smileyText.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void showAllSmiley() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.ic_smiley_bad_empty,
                context.getString(R.string.smiley_bad),
                SMILEY_BAD));
        this.list.add(new SmileyModel(R.drawable.ic_smiley_netral_empty,
                context.getString(R.string.smiley_netral),
                SMILEY_NEUTRAL));
        this.list.add(new SmileyModel(R.drawable.ic_smiley_good_empty,
                context.getString(R.string.smiley_good),
                SMILEY_GOOD));
        notifyDataSetChanged();
    }

    public void showSmileyBad() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.review_ic_smiley_bad,
                context.getString(R.string.smiley_bad),
                SMILEY_BAD));
        this.canGiveReputation = false;
        notifyDataSetChanged();
    }

    public void showSmileyNeutral() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.review_ic_smiley_neutral,
                context.getString(R.string.smiley_netral),
                SMILEY_NEUTRAL));
        this.canGiveReputation = false;
        notifyDataSetChanged();
    }

    public void showSmileyGood() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.review_ic_smiley_good,
                context.getString(R.string.smiley_good),
                SMILEY_GOOD));
        this.canGiveReputation = false;
        notifyDataSetChanged();
    }

    public void showLockedSmiley() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.ic_locked2,
                "",
                ""));
        this.canGiveReputation = false;
    }

    public void showChangeSmiley(int reviewerScore) {
        this.canGiveReputation = true;

        if (reviewerScore == InboxReputationDetailHeaderViewHolder.SMILEY_BAD) {
            this.list.clear();
            this.list.add(new SmileyModel(R.drawable.review_ic_smiley_bad,
                    context.getString(R.string.smiley_bad),
                    SMILEY_BAD,
                    true));
            this.list.add(new SmileyModel(R.drawable.ic_smiley_netral_empty,
                    context.getString(R.string.smiley_netral),
                    SMILEY_NEUTRAL,
                    true));
            this.list.add(new SmileyModel(R.drawable.ic_smiley_good_empty,
                    context.getString(R.string.smiley_good),
                    SMILEY_GOOD,
                    true));
        } else if (reviewerScore == InboxReputationDetailHeaderViewHolder.SMILEY_NEUTRAL) {
            this.list.clear();
            this.list.add(new SmileyModel(R.drawable.ic_smiley_bad_empty,
                    context.getString(R.string.smiley_bad),
                    SMILEY_BAD,
                    true));
            this.list.add(new SmileyModel(R.drawable.review_ic_smiley_neutral,
                    context.getString(R.string.smiley_netral),
                    SMILEY_NEUTRAL,
                    true));
            this.list.add(new SmileyModel(R.drawable.ic_smiley_good_empty,
                    context.getString(R.string.smiley_good),
                    SMILEY_GOOD,
                    true));
        }
        notifyDataSetChanged();
    }
}
