package com.tokopedia.challenges.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.Prize;

import java.util.List;

public class AwardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Prize> awardsList;
    Context context;

    public AwardAdapter(List<Prize> awardsList) {
        this.awardsList = awardsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v = inflater.inflate(R.layout.challenges_award_item, parent, false);
        holder = new AwardViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AwardAdapter.AwardViewHolder) holder).setIndex(position);
        ((AwardAdapter.AwardViewHolder) holder).bindData(awardsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (awardsList != null && awardsList.size() > 0) {
            return awardsList.size();
        } else {
            return 3;
        }
    }

    public class AwardViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView awardTitle;
        private TextView awardDescription;
        private ImageView awardImage;
        private int index;

        public AwardViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            awardTitle = itemView.findViewById(R.id.tv_award_title);
            awardDescription = itemView.findViewById(R.id.tv_award_price);
            awardImage = itemView.findViewById(R.id.award_image);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int devicewidth = (int) (displaymetrics.widthPixels / 1.2);
            itemView.getLayoutParams().width = devicewidth;
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        public void bindData(final Prize prize) {
            awardTitle.setText(prize.getTitle());
            awardDescription.setText(prize.getDescription().trim());
//            ImageHandler.loadImageCircle2(context, awardImage, prize.);
        }
    }
}
