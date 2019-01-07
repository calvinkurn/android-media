package com.tokopedia.design.text;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.design.R;

public class Chips extends AppCompatTextView {
    public Chips(Context context) {
        super(context);
        init();
    }

    public Chips(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chips(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin_default = (int) (getContext().getResources().getDimension(R.dimen.dp_16));
        int padding_top_bottom = (int) (getContext().getResources().getDimension(R.dimen.dp_8));
        params.height = (int) (getContext().getResources().getDimension(R.dimen.dp_40));
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        ((LinearLayout.LayoutParams) params).setMargins(margin_default, margin_default, 0, margin_default);
        setBackgroundResource(R.drawable.bg_button_select_deselect);
        setPadding(margin_default, padding_top_bottom, margin_default, padding_top_bottom);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setLayoutParams(params);
        setTextColor(getResources().getColor(R.color.black_56));
    }
}
