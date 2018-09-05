package com.tokopedia.home.account.presentation.adapter.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SwitchSettingItemViewModel;

import java.util.List;

public class GeneralSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GENERAL = 0;
    private static final int TYPE_SWITCH = 1;

    private List<SettingItemViewModel> settingItems;
    private OnSettingItemClicked listener;
    private SwitchSettingListener switchSettingListener;

    public GeneralSettingAdapter(List<SettingItemViewModel> settingItems, OnSettingItemClicked listener) {
        this.settingItems = settingItems;
        this.listener = listener;
    }

    public void setListener(OnSettingItemClicked listener) {
        this.listener = listener;
    }

    public void setSwitchSettingListener(SwitchSettingListener switchSettingListener) {
        this.switchSettingListener = switchSettingListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (settingItems.get(position) instanceof SwitchSettingItemViewModel){
            return TYPE_SWITCH;
        } else {
            return TYPE_GENERAL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SWITCH){
            return new SwitchSettingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notif_setting, parent, false));
        } else {
            return new GeneralSettingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_general_setting, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SWITCH){
            ((SwitchSettingViewHolder) holder).bind((SwitchSettingItemViewModel) settingItems.get(position));
        } else {
            ((GeneralSettingViewHolder) holder).bind(settingItems.get(position));
        }
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

    class SwitchSettingViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView summaryextView;
        private Switch aSwitch;

        public SwitchSettingViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            summaryextView = itemView.findViewById(R.id.subtitle);
            aSwitch = itemView.findViewById(R.id.switchWidget);
            aSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (switchSettingListener != null) {
                switchSettingListener.onChangeChecked(settingItems.get(getAdapterPosition()).getId(), isChecked);
            }});

            itemView.setOnClickListener(view -> aSwitch.toggle());
        }

        public void bind(SwitchSettingItemViewModel item){
            titleTextView.setText(item.getTitle());
            summaryextView.setText(item.getSubtitle());
            aSwitch.setChecked(switchSettingListener != null && switchSettingListener.isSwitchSelected(item.getId()));
        }
    }

    public interface OnSettingItemClicked{
        void onItemClicked(int settingId);
    }

    public interface SwitchSettingListener{
        boolean isSwitchSelected(int settingId);
        void onChangeChecked(int settingId, boolean value);
    }
}
