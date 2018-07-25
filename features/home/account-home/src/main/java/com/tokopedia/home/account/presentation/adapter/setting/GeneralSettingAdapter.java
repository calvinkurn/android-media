package com.tokopedia.home.account.presentation.adapter.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.List;

public class GeneralSettingAdapter extends RecyclerView.Adapter<GeneralSettingAdapter.GeneralSettingViewHolder> {
    private List<SettingItemViewModel> settingItems;
    private OnSettingItemClicked listener;

    public GeneralSettingAdapter(List<SettingItemViewModel> settingItems) {
        this.settingItems = settingItems;
    }

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
        private TextView titleTextView;
        private TextView subtitleTextView;
        private ImageView iconSetting;
        private ImageView rightArrow;

        public GeneralSettingViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.subtitle);
            iconSetting = itemView.findViewById(R.id.icon_setting);
            rightArrow = itemView.findViewById(R.id.image_arrow);
            itemView.setOnClickListener(view -> {
                if (listener != null){
                    listener.onItemClicked(settingItems.get(getAdapterPosition()).getId());
                }
            });
        }

        public void bind(SettingItemViewModel item){
            titleTextView.setText(item.getTitle());
            if (!TextUtils.isEmpty(item.getSubtitle())){
                subtitleTextView.setText(item.getSubtitle());
                subtitleTextView.setVisibility(View.VISIBLE);
            } else {
                subtitleTextView.setVisibility(View.GONE);
            }
            if (item.getIconResource() > 0){
                iconSetting.setVisibility(View.VISIBLE);
                iconSetting.setImageResource(item.getIconResource());
            } else {
                iconSetting.setVisibility(View.GONE);
            }
            rightArrow.setVisibility(item.isHideArrow()? View.GONE : View.VISIBLE);
        }
    }

    public interface OnSettingItemClicked{
        void onItemClicked(int settingId);
    }
}
