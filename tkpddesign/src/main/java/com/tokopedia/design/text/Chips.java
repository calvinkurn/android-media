package com.tokopedia.design.text;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.getLayoutParams();//new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup parent = (ViewGroup) getParent();
        if (parent instanceof ConstraintLayout)
            Log.d("erege", "vreve");
        int margin_default = (int) (getContext().getResources().getDimension(R.dimen.dp_16));
        int padding_top_bottom = (int) (getContext().getResources().getDimension(R.dimen.dp_8));
        params.height = (int) (getContext().getResources().getDimension(R.dimen.dp_40));
        params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(margin_default, margin_default, 0, 0);
        setBackgroundResource(R.drawable.bg_button_select_deselect);
        setPadding(margin_default, padding_top_bottom, margin_default, padding_top_bottom);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setTextColor(getResources().getColor(R.color.black_56));
        setLayoutParams(params);
    }
}
