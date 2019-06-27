package com.tokopedia.gamification.cracktoken.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
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

    private ImageView imageEggSource;
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
        imageEggSource = view.findViewById(R.id.image_egg_source);
        textEggSource = view.findViewById(R.id.text_egg_source);
    }

    public void showEggSource(String eggSourceImageUrl, String[] eggSourceString) {
        ImageHandler.loadImageAndCache(imageEggSource, eggSourceImageUrl);
        if (eggSourceString == null || eggSourceString.length == 0) {
            this.setVisibility(View.GONE);
        } else {

            String stringToShow = getContext().getString(R.string.egg_source, eggSourceString[0],
                    eggSourceString[1]);
            Spannable spannable = new SpannableString(stringToShow);
            int indexStart = eggSourceString[0].length() + 1;
            int indexEnd = stringToShow.length();

            spannable.setSpan(new ForegroundColorSpan(
                    ContextCompat.getColor(getContext(), R.color.egg_source_color)
            ), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textEggSource.setText(spannable);
            textEggSource.setVisibility(View.VISIBLE);

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
