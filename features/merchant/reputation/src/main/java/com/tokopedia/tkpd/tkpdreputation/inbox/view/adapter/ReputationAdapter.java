package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.SmileyModel;

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

        void onFavoriteShopClicked(int shopId);

        void onGoToShopDetail(int shopId);

        void onGoToPeopleProfile(int userId);
    }

    ArrayList<SmileyModel> list;
    ReputationListener listener;

    private ReputationAdapter(ReputationListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
        this.canGiveReputation = true;
    }

    public static ReputationAdapter createInstance(ReputationListener listener) {
        return new ReputationAdapter(listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView smiley;
        TextView smileyText;
        View main;

        public ViewHolder(View itemView) {
            super(itemView);
            smiley = (ImageView) itemView.findViewById(R.id.smiley);
            smileyText = (TextView) itemView.findViewById(R.id.smiley_name);
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
        holder.smileyText.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void showAllSmiley() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.ic_smiley_bad_empty,
                MainApplication.getAppContext().getString(R.string.smiley_bad),
                SMILEY_BAD));
        this.list.add(new SmileyModel(R.drawable.ic_smiley_netral_empty,
                MainApplication.getAppContext().getString(R.string.smiley_netral),
                SMILEY_NEUTRAL));
        this.list.add(new SmileyModel(R.drawable.ic_smiley_good_empty,
                MainApplication.getAppContext().getString(R.string.smiley_good),
                SMILEY_GOOD));
        notifyDataSetChanged();
    }

    public void showSmileyBad() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.ic_smiley_bad,
                MainApplication.getAppContext().getString(R.string.smiley_bad),
                SMILEY_BAD));
        this.canGiveReputation = false;
        notifyDataSetChanged();
    }

    public void showSmileyNeutral() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.ic_smiley_netral,
                MainApplication.getAppContext().getString(R.string.smiley_netral),
                SMILEY_NEUTRAL));
        this.canGiveReputation = false;
        notifyDataSetChanged();
    }

    public void showSmileyGood() {
        this.list.clear();
        this.list.add(new SmileyModel(R.drawable.ic_smiley_good,
                MainApplication.getAppContext().getString(R.string.smiley_good),
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
            this.list.add(new SmileyModel(R.drawable.ic_smiley_bad,
                    MainApplication.getAppContext().getString(R.string.smiley_bad),
                    SMILEY_BAD,
                    true));
            this.list.add(new SmileyModel(R.drawable.ic_smiley_netral_empty,
                    MainApplication.getAppContext().getString(R.string.smiley_netral),
                    SMILEY_NEUTRAL,
                    true));
            this.list.add(new SmileyModel(R.drawable.ic_smiley_good_empty,
                    MainApplication.getAppContext().getString(R.string.smiley_good),
                    SMILEY_GOOD,
                    true));
        } else if (reviewerScore == InboxReputationDetailHeaderViewHolder.SMILEY_NEUTRAL) {
            this.list.clear();
            this.list.add(new SmileyModel(R.drawable.ic_smiley_bad_empty,
                    MainApplication.getAppContext().getString(R.string.smiley_bad),
                    SMILEY_BAD,
                    true));
            this.list.add(new SmileyModel(R.drawable.ic_smiley_netral,
                    MainApplication.getAppContext().getString(R.string.smiley_netral),
                    SMILEY_NEUTRAL,
                    true));
            this.list.add(new SmileyModel(R.drawable.ic_smiley_good_empty,
                    MainApplication.getAppContext().getString(R.string.smiley_good),
                    SMILEY_GOOD,
                    true));
        }
        notifyDataSetChanged();
    }
}
