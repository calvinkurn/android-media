package com.tokopedia.home.account.presentation.adapter.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingType;
import com.tokopedia.home.account.data.model.AppNotificationSettingModel;
import com.tokopedia.home.account.presentation.viewmodel.EmailNotifViewModel;

import java.util.HashMap;
import java.util.List;

public class EmailNotifAdapter extends RecyclerView.Adapter<EmailNotifAdapter.EmailNotifViewHolder>{
    private static final String SELECTED = "1";
    private static final String NOT_SELECTED = "0";

    private List<EmailNotifViewModel> items;
    private AppNotificationSettingModel notification;
    private HashMap<String, String> selectedSetting = new HashMap<>();

    public EmailNotifAdapter(List<EmailNotifViewModel> items) {
        this.items = items;
    }

    public HashMap<String, String> getSelectedSetting() {
        return selectedSetting;
    }

    @NonNull
    @Override
    public EmailNotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmailNotifViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_notif_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmailNotifViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setNotifSetting(AppNotificationSettingModel notification) {
        this.notification = notification;
        generateMapSetting();
        notifyDataSetChanged();
    }

    private void generateMapSetting(){
        selectedSetting.put(SettingType.FLAG_ADMIN_MESSAGE, String.valueOf(notification.getFlagAdminMessage()));
        selectedSetting.put(SettingType.FLAG_MESSAGE, String.valueOf(notification.getFlagMessage()));
        selectedSetting.put(SettingType.FLAG_NEWSLETTER, String.valueOf(notification.getFlagNewsletter()));
        selectedSetting.put(SettingType.FLAG_REVIEW, String.valueOf(notification.getFlagreview()));
        selectedSetting.put(SettingType.FLAG_TALK, String.valueOf(notification.getFlagTalkProduct()));
    }

    class EmailNotifViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView summaryextView;
        private Switch aSwitch;

        public EmailNotifViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            summaryextView = itemView.findViewById(R.id.subtitle);
            aSwitch = itemView.findViewById(R.id.switchWidget);
            aSwitch.setOnCheckedChangeListener((compoundButton, isChecked) ->
                    selectedSetting.put(items.get(getAdapterPosition()).getId(), isChecked ? SELECTED : NOT_SELECTED));

            itemView.setOnClickListener(view -> aSwitch.toggle());
        }

        public void bind(EmailNotifViewModel item){
            titleTextView.setText(item.getTitle());
            summaryextView.setText(item.getSummary());
            if (selectedSetting.containsKey(item.getId())) {
                aSwitch.setChecked(selectedSetting.get(item.getId()).equals(SELECTED));
            } else {
                aSwitch.setChecked(false);
            }
        }
    }
}
