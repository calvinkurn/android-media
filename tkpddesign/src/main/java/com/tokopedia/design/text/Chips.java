package com.tokopedia.design.text;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.tokopedia.design.R;

public class Chips extends AppCompatTextView {
    public Chips(Context context) {
        super(context);
    }

    public Chips(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chips(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) this.getLayoutParams();
        int marginDefault = (int) (getContext().getResources().getDimension(R.dimen.dp_16));
        int paddingTopBottom = (int) (getContext().getResources().getDimension(R.dimen.dp_8));
        params.height = (int) (getContext().getResources().getDimension(R.dimen.dp_40));
        params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(marginDefault, marginDefault, 0, 0);
        setBackgroundResource(R.drawable.chips_background);
        setPadding(marginDefault, paddingTopBottom, marginDefault, paddingTopBottom);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setTextColor(getResources().getColor(R.color.black_56));
        setLayoutParams(params);
    }
}
