package com.tokopedia.review.feature.inbox.buyerreview.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.review.R;
import com.tokopedia.unifyprinciples.Typography;

/**
 * @author by stevenfredian on 8/16/17.
 */

public class UserReputationView extends BaseCustomView {

    private Typography percentageTextView;
    private ImageView imageViewIcon;
    private LinearLayout layout;
    private BottomSheetDialog dialog;
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
        imageViewIcon = view.findViewById(R.id.image_view_icon);
        percentageTextView = view.findViewById(R.id.text_view_percentage);
        layout = view.findViewById(R.id.buyer_reputation);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            return context.getResources().getDrawable(resId, context.getApplicationContext().getTheme());
        else
            return AppCompatResources.getDrawable(context, resId);
    }

    public void setValue(String percentageValue, boolean noReputation,
                         final int positiveValue, final int neutralValue,
                         final int negativeValue) {
        if (TextUtils.isEmpty(percentageValue)) {
            percentageTextView.setVisibility(GONE);
        } else {
            percentageTextView.setVisibility(VISIBLE);
            percentageTextView.setText(percentageValue);
        }

        if (noReputation) {
            imageViewIcon.setImageDrawable(MethodChecker.getDrawable(imageViewIcon.getContext(), R.drawable.review_ic_smiley_empty));
        } else {
            imageViewIcon.setImageDrawable(MethodChecker.getDrawable(imageViewIcon.getContext(), R.drawable.review_ic_smiley_good));
            if (showTooltip) {
                setBottomDialog(positiveValue, neutralValue, negativeValue);
            }
        }
    }

    private void setBottomDialog(final int positiveValue, final int neutralValue, final int negativeValue) {
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomSheetDialog(getContext());
                dialog.setContentView(R.layout.buyer_reputation_bottom_sheet_dialog);
                Typography positiveText = dialog.findViewById(R.id.score_good);
                if (positiveText != null) {
                    positiveText.setText(String.valueOf(positiveValue));
                }
                Typography neutralText = dialog.findViewById(R.id
                        .score_netral);
                if (neutralText != null) {
                    neutralText.setText(String.valueOf(neutralValue));
                }
                Typography negativeText = dialog.findViewById(R.id.score_bad);
                if (negativeText != null) {
                    negativeText.setText(String.valueOf(negativeValue));
                }
                Button closeButton = dialog.findViewById(R.id.close_button);
                if (closeButton != null) {
                    closeButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                dialog.show();
            }
        });
    }
}