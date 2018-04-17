package com.tokopedia.gamification.cracktoken.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

public class WidgetRemainingToken extends LinearLayout {

    private ImageView imageRemainingToken;
    private TextView textRemainingToken;
    private TextView ivCounter;

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
        ivCounter = view.findViewById(R.id.tv_floating_counter);
    }

    public void showRemainingToken(String smallImageUrl, String remainingTokenString, String[] countingMessage) {
        ImageHandler.loadImageAndCache(imageRemainingToken, smallImageUrl);
        if (countingMessage == null || countingMessage.length == 0) {
            this.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(remainingTokenString) ) {
            ivCounter.setVisibility(View.GONE);
            textRemainingToken.setVisibility(View.GONE);

            this.setVisibility(View.VISIBLE);
        } else {
            ivCounter.setText(remainingTokenString);
            ivCounter.setVisibility(VISIBLE);

            String stringToShow = getContext().getString(R.string.egg_crack_x_token_left, countingMessage[0],
                    countingMessage[1], countingMessage[2]);
            Spannable spannable = new SpannableString(stringToShow);
            int indexStart = countingMessage[0].length() + 1;
            int indexEnd = indexStart + countingMessage[1].length();

            spannable.setSpan(new ForegroundColorSpan(
                    ContextCompat.getColor(getContext(), R.color.lucky_egg_color)
            ), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textRemainingToken.setText(spannable);
            textRemainingToken.setVisibility(View.VISIBLE);

            this.setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        this.setVisibility(INVISIBLE);
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

}
