package com.tokopedia.design.reputation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.card.ToolTipUtils;

/**
 * Created by stevenfredian on 8/16/17.
 */

public class UserReputationView extends BaseCustomView {

    private TextView percentageTextView;
    private ImageView imageViewIcon;

    private boolean showTooltip;

    public UserReputationView(Context context) {
        super(context);
        init();
    }

    public UserReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UserReputationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.UserReputationView);
        try {
            showTooltip = styledAttributes.getBoolean(R.styleable.UserReputationView_usv_show_tooltip, false);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_reputation_user, this);
        imageViewIcon = (ImageView) view.findViewById(R.id.image_view_icon);
        percentageTextView = (TextView) view.findViewById(R.id.text_view_percentage);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public void setValue(int percentageValue, boolean noReputation, final int positiveValue, final int neutralValue, final int negativeValue) {
        if (noReputation) {
            imageViewIcon.setImageResource(R.drawable.ic_smiley_empty);
            percentageTextView.setVisibility(GONE);
        } else {
            imageViewIcon.setImageResource(R.drawable.ic_smiley_positive);
            percentageTextView.setVisibility(VISIBLE);
            percentageTextView.setText(String.valueOf(percentageValue));
            if (showTooltip) {
                setToolTip(positiveValue, neutralValue, negativeValue);
            }
        }
    }

    private void setToolTip(final int positiveValue, final int neutralValue, final int negativeValue) {
        imageViewIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(positiveValue, neutralValue, negativeValue), v);
            }
        });
        percentageTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(positiveValue, neutralValue, negativeValue), v);
            }
        });
    }

    private View setViewToolTip(final int positive, final int neutral, final int negative) {
        return ToolTipUtils.setToolTip(getContext(), R.layout.item_tooltip_user_reputation,
                new ToolTipUtils.ToolTipListener() {
                    @Override
                    public void setView(View view) {
                        TextView smileTextView = (TextView) view.findViewById(R.id.text_view_positive);
                        TextView neutralTextView = (TextView) view.findViewById(R.id.text_view_neutral);
                        TextView negativeTextView = (TextView) view.findViewById(R.id.text_view_negative);
                        smileTextView.setText(String.valueOf(positive));
                        neutralTextView.setText(String.valueOf(neutral));
                        negativeTextView.setText(String.valueOf(negative));
                    }

                    @Override
                    public void setListener() {

                    }
                });
    }
}