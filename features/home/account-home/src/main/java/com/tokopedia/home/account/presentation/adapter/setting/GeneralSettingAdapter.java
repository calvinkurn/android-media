package com.tokopedia.home.account.presentation.adapter.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.view.GeneralSettingMenuLabel;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SwitchSettingItemViewModel;
import com.tokopedia.home.account.presentation.widget.TagRoundedSpan;
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify;
import com.tokopedia.unifyprinciples.Typography;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GeneralSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GENERAL = 0;
    private static final int TYPE_SWITCH = 1;

    private static final int POSITION_UNDEFINED = -1;

    private List<SettingItemViewModel> settingItems;
    private OnSettingItemClicked listener;
    private SwitchSettingListener switchSettingListener;

    public GeneralSettingAdapter(List<SettingItemViewModel> settingItems, OnSettingItemClicked listener) {
        this.settingItems = settingItems;
        this.listener = listener;
    }

    public void setSettingItems(List<SettingItemViewModel> settingItems) {
        this.settingItems = settingItems;
    }

    public void setListener(OnSettingItemClicked listener) {
        this.listener = listener;
    }

    public void setSwitchSettingListener(SwitchSettingListener switchSettingListener) {
        this.switchSettingListener = switchSettingListener;
    }

    public void updateSettingItem(int settingId) {
        int position = findSwitchPosition(settingId);
        if (position != POSITION_UNDEFINED) {
            SettingItemViewModel settingItemViewModel =
                    settingItems.get(position);
            if (settingItemViewModel instanceof SwitchSettingItemViewModel) {
                notifyItemChanged(position);
            }
        }
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

        public void bind(SettingItemViewModel item){
            SpannableString title = GeneralSettingMenuLabel.INSTANCE.generateSpannableTitle(
                    itemView.getContext(), item, GeneralSettingMenuLabel.LABEL_NEW);
            titleView.setText(title);
            bodyView.setText(item.getSubtitle());
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
            aSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (switchSettingListener != null) {
                switchSettingListener.onChangeChecked(settingItems.get(getAdapterPosition()).getId(), isChecked);
            }});
            itemView.setOnClickListener(view -> aSwitch.toggle());
        }

        public void bind(SwitchSettingItemViewModel item){
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
