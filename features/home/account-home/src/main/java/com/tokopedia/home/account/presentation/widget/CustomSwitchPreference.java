package com.tokopedia.home.account.presentation.widget;

import android.content.Context;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.home.account.R;

public class CustomSwitchPreference extends SwitchPreference {

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleText = holder.itemView.findViewById(android.R.id.title);
        titleText.setTextAppearance(holder.itemView.getContext(), R.style.TextView_Small);
        TextView summaryText = holder.itemView.findViewById(android.R.id.summary);
        summaryText.setTextAppearance(holder.itemView.getContext(), R.style.TextView_Small_BlackDisabled);
        summaryText.setVisibility(View.GONE);
        holder.itemView.setMinimumHeight((int) holder.itemView.getResources().getDimension(R.dimen.dp_64));

        Switch aSwitch = findSwitch(holder.itemView.findViewById(android.R.id.widget_frame));
        if (aSwitch != null){
            aSwitch.setTrackDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(),
                    R.drawable.bg_switch_track_selector));

            aSwitch.setThumbDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(),
                    R.drawable.bg_switch_thumb_selector));
        }
    }

    private Switch findSwitch(ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View thisChildview = view.getChildAt(i);
            if (thisChildview instanceof Switch) {
                return (Switch) thisChildview;
            } else if (thisChildview instanceof ViewGroup) {
                Switch theSwitch = findSwitch((ViewGroup) thisChildview);
                if (theSwitch != null) return theSwitch;
            }
        }
        return null;
    }
}
