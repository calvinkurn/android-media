package com.tokopedia.contactus.common.customview;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.contactus.R;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public abstract class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin_default = (int)(getContext().getResources().getDimension(R.dimen.margin_default));
        setPadding(0,margin_default,margin_default,margin_default);
        setLayoutParams(params);
        setTextSize(14);
        setTextColor(getResources().getColor(R.color.black3_text));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClick();
            }
        });
    }

    public abstract void onViewClick();
}
