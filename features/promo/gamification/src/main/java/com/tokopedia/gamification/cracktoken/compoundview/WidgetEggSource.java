package com.tokopedia.gamification.cracktoken.compoundview;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.R;

/**
 * @author Rizky on 29/03/18.
 */

public class WidgetEggSource extends LinearLayout {

    private TextView textEggSource;

    public WidgetEggSource(Context context) {
        super(context);
        init();
    }

    public WidgetEggSource(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetEggSource(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_egg_source, this, true);
        textEggSource = view.findViewById(R.id.text_egg_source);
    }

    public void showEggSource(String message) {
        if (TextUtils.isEmpty(message)) {
            this.setVisibility(View.GONE);
        } else {

            textEggSource.setText(message);
            this.setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }
}
