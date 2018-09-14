package com.tokopedia.challenges.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.challenges.R;

public class BulletTextView extends FrameLayout {
    private TextView mTvHowBuzzPointsText;

    public BulletTextView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = inflate(getContext(), getLayout(), this);
        mTvHowBuzzPointsText = view.findViewById(R.id.tv_how_buzz_points_text);
    }

    public void setBuzzPoint(String text) {
        mTvHowBuzzPointsText.setText(text);
    }

    protected int getLayout() {
        return R.layout.layout_bullet_textview;
    }

}
