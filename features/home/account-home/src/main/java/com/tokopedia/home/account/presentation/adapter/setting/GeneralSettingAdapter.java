package com.tokopedia.home.account.presentation.adapter.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.List;

public class GeneralSettingAdapter extends RecyclerView.Adapter<GeneralSettingAdapter.GeneralSettingViewHolder> {
    private List<SettingItemViewModel> settingItems;
    private OnSettingItemClicked listener;

    public GeneralSettingAdapter(List<SettingItemViewModel> settingItems, OnSettingItemClicked listener) {
        this.settingItems = settingItems;
        this.listener = listener;
    }

    public void setListener(OnSettingItemClicked listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GeneralSettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralSettingViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_general_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralSettingViewHolder holder, int position) {
        holder.bind(settingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }

    class GeneralSettingViewHolder extends RecyclerView.ViewHolder{
        private LabelView labelView;


        public GeneralSettingViewHolder(View itemView) {
            super(itemView);
            labelView = itemView.findViewById(R.id.labelview);

            itemView.setOnClickListener(view -> {
                if (listener != null){
                    listener.onItemClicked(settingItems.get(getAdapterPosition()).getId());
                }
            });
        }

        public void bind(SettingItemViewModel item){
            labelView.setTitle(item.getTitle());
            labelView.setSubTitle(item.getSubtitle());
            labelView.setImageResource(item.getIconResource());

            if (item.getIconResource() > 0){
                labelView.getImageView().setVisibility(View.VISIBLE);
                labelView.getImageView().setImageResource(item.getIconResource());
            } else {
                labelView.getImageView().setVisibility(View.GONE);
            }
            labelView.showRightArrow(item.isHideArrow());
        }
    }

    public interface OnSettingItemClicked{
        void onItemClicked(int settingId);
    }
}
