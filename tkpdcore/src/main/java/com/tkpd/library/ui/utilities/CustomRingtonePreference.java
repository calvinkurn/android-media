package com.tkpd.library.ui.utilities;

import android.content.Context;
import android.preference.RingtonePreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;

/**
 * Created by raditya.gumay on 07/03/2016.
 */
public class CustomRingtonePreference extends RingtonePreference {

    public CustomRingtonePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomRingtonePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomRingtonePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRingtonePreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView textView = (TextView) view.findViewById(android.R.id.title);
        textView.setTextAppearance(view.getContext(), R.style.Setting_TextView);
    }
}
