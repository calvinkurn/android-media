package com.tokopedia.home_account.account_settings.presentation.adapter.setting;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.home_account.R;
import com.tokopedia.home_account.account_settings.presentation.view.GeneralSettingMenuLabel;
import com.tokopedia.home_account.account_settings.presentation.viewmodel.SettingItemUIModel;
import com.tokopedia.home_account.account_settings.presentation.viewmodel.base.SwitchSettingItemUIModel;
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify;
import com.tokopedia.unifyprinciples.Typography;

import java.util.List;

public class GeneralSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GENERAL = 0;
    private static final int TYPE_SWITCH = 1;

    private static final int POSITION_UNDEFINED = -1;

    private List<SettingItemUIModel> settingItems;
    private OnSettingItemClicked listener;
    private SwitchSettingListener switchSettingListener;

    public GeneralSettingAdapter(List<SettingItemUIModel> settingItems, OnSettingItemClicked listener) {
        this.settingItems = settingItems;
        this.listener = listener;
    }

    public void setSettingItems(List<SettingItemUIModel> settingItems) {
        this.settingItems = settingItems;
    }

    public void setListener(OnSettingItemClicked listener) {
        this.listener = listener;
    }

    public void setSwitchSettingListener(SwitchSettingListener switchSettingListener) {
        this.switchSettingListener = switchSettingListener;
    }

    public void updateSettingItem(int settingId) {
        try {
            int position = findSwitchPosition(settingId);
            if (position != POSITION_UNDEFINED) {
                SettingItemUIModel settingItemUIModel =
                        settingItems.get(position);
                if (settingItemUIModel instanceof SwitchSettingItemUIModel) {
                    notifyItemChanged(position);
                }
            }
        } catch (Throwable ignored) {}
    }

    private int findSwitchPosition(int settingId) {
        for (int i = 0; i<settingItems.size() ; i++) {
            if (settingId == settingItems.get(i).getId()) {
                return i;
            }
        }
        return POSITION_UNDEFINED;
    }

    @Override
    public int getItemViewType(int position) {
        if (settingItems.get(position) instanceof SwitchSettingItemUIModel){
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
            ((SwitchSettingViewHolder) holder).bind((SwitchSettingItemUIModel) settingItems.get(position));
        } else {
            ((GeneralSettingViewHolder) holder).bind(settingItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (settingItems != null) {
            return settingItems.size();
        } else {
            return 0;
        }
    }

    class GeneralSettingViewHolder extends RecyclerView.ViewHolder{
        private final Typography titleView;
        private final Typography bodyView;
        private final ImageView arrowIcon;

        public GeneralSettingViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.account_user_item_common_title);
            bodyView = itemView.findViewById(R.id.account_user_item_common_body);
            arrowIcon = itemView.findViewById(R.id.account_user_item_icon_arrow);

            itemView.setOnClickListener(view -> {
                if (listener != null){
                    if(getAdapterPosition()>=0 && getAdapterPosition() < settingItems.size()) {
                        listener.onItemClicked(settingItems.get(getAdapterPosition()).getId());
                    }
                }
            });
        }

        public void bind(SettingItemUIModel item){
            SpannableString title = GeneralSettingMenuLabel.INSTANCE.generateSpannableTitle(
                    itemView.getContext(), item, GeneralSettingMenuLabel.LABEL_NEW);
            titleView.setText(title);
            if(item.getSubtitle() != null && !item.getSubtitle().isEmpty()) {
                bodyView.setVisibility(View.VISIBLE);
                bodyView.setText(item.getSubtitle());
            } else bodyView.setVisibility(View.GONE);
            if(item.isHideArrow()) {
                arrowIcon.setVisibility(View.GONE);
            } else {
                arrowIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    class SwitchSettingViewHolder extends RecyclerView.ViewHolder{
        private final Typography titleTextView;
        private final Typography summaryextView;
        private final SwitchUnify aSwitch;
        private final View view;

        public SwitchSettingViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titleTextView = itemView.findViewById(R.id.account_user_item_common_title);
            summaryextView = itemView.findViewById(R.id.account_user_item_common_body);
            aSwitch = itemView.findViewById(R.id.account_user_item_common_switch);
            itemView.setOnClickListener(view -> aSwitch.toggle());
        }

        public void bind(SwitchSettingItemUIModel item){
            if(!item.labelType().isEmpty()) {
                SpannableString title = GeneralSettingMenuLabel.INSTANCE.generateSpannableTitle(
                        itemView.getContext(), item, item.labelType());
                titleTextView.setText(title);
            } else {
                titleTextView.setText(item.getTitle());
            }
            summaryextView.setText(item.getSubtitle());

            boolean switchState = switchSettingListener.isSwitchSelected(item.getId());
            aSwitch.setChecked(switchSettingListener != null && switchState);

            if (item.isUseOnClick()) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchSettingListener.onClicked(item.getId(), switchState);
                    }
                });
            }

            aSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (switchSettingListener != null) {
                    switchSettingListener.onChangeChecked(settingItems.get(getAdapterPosition()).getId(), isChecked);
                }});
        }
    }

    public interface OnSettingItemClicked{
        void onItemClicked(int settingId);
    }

    public interface SwitchSettingListener{
        boolean isSwitchSelected(int settingId);
        void onChangeChecked(int settingId, boolean value);
        void onClicked(int settingId, boolean currentValue);
    }
}
