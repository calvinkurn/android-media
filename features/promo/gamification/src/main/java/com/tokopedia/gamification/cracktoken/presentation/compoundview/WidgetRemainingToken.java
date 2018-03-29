package com.tokopedia.gamification.cracktoken.presentation.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.gamification.R;

/**
 * @author Rizky on 29/03/18.
 */

public class WidgetRemainingToken extends LinearLayout {

    private ImageView imageRemainingToken;
    private TextView textRemainingToken;

    public WidgetRemainingToken(Context context) {
        super(context);
        init();
    }

    public WidgetRemainingToken(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetRemainingToken(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_remaining_token, this, true);
        imageRemainingToken = view.findViewById(R.id.image_remaining_token);
        textRemainingToken = view.findViewById(R.id.text_remaining_token);
    }

    public void showRemainingToken() {

    }

}
