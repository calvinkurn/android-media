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
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SwitchSettingItemViewModel;
import com.tokopedia.home.account.presentation.widget.TagRoundedSpan;

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
        return settingItems.size();
    }

    class GeneralSettingViewHolder extends RecyclerView.ViewHolder{
        private LabelView labelView;


        public GeneralSettingViewHolder(View itemView) {
            super(itemView);
            labelView = itemView.findViewById(R.id.labelview);

            itemView.setOnClickListener(view -> {
                if (listener != null){
                    if(getAdapterPosition()>=0 && getAdapterPosition() < settingItems.size()) {
                        listener.onItemClicked(settingItems.get(getAdapterPosition()).getId());
                    }
                }
            });
        }

        public void bind(SettingItemViewModel item){
            SpannableString title = generateSpannableTitle(item);
            labelView.setTitle(title);
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

        private SpannableString generateSpannableTitle(SettingItemViewModel item) {
            String indicatorNew = " BARU";
            String title = item.getTitle();
            String notificationTitle = itemView
                    .getContext()
                    .getResources()
                    .getString(R.string.title_notification_setting);
            String preferenceTitle = itemView
                    .getContext()
                    .getResources()
                    .getString(R.string.title_occ_preference_setting);
            int boxColor = -1;

            if (title.equals(notificationTitle)) {
                boxColor = com.tokopedia.unifyprinciples.R.color.Unify_R400;
            } else if (title.equals(preferenceTitle)) {
                boxColor = com.tokopedia.unifyprinciples.R.color.Unify_R500;
            }

            if (boxColor > -1 && !hasBeenOneMonth(title)) {
                int startPosition = title.length() + 1;
                int endPosition = startPosition + indicatorNew.length() - 1;
                title += indicatorNew;

                SpannableString spannable = new SpannableString(title);
                TagRoundedSpan newTag = new TagRoundedSpan(
                        itemView.getContext(),
                        4,
                        boxColor,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                );

                spannable.setSpan(
                        new RelativeSizeSpan(0.57f),
                        startPosition,
                        endPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannable.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        startPosition,
                        endPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannable.setSpan(
                        newTag,
                        startPosition,
                        endPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                return spannable;
            } else {
                return new SpannableString(title);
            }
        }

        private boolean hasBeenOneMonth(String title) {
            String key = title + ".NewTag";
            String prefKey = this.getClass().getName() + ".pref";
            int dayOffset = 30;
            long now = new Date().getTime();

            SharedPreferences preferences = itemView.getContext().getSharedPreferences(prefKey, Context.MODE_PRIVATE);

            if (!preferences.contains(key)) {
                preferences.edit().putLong(key, now).apply();
                return false;
            }

            long firstTimeSeenDate = preferences.getLong(key, -1);
            long duration = Math.abs(firstTimeSeenDate - now);
            long dayPassed = TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS);

            return dayPassed >= dayOffset;
        }
    }

    class SwitchSettingViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView summaryextView;
        private Switch aSwitch;
        private View view;

        public SwitchSettingViewHolder(View itemView) {
            super(itemView);
            view = itemView;
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
