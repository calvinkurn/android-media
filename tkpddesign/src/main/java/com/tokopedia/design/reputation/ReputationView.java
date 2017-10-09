package com.tokopedia.design.reputation;

/**
 * Created by stevenfredian on 8/16/17.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

;

/**
 * Created by stevenfredian on 8/16/17.
 */

public class ReputationView extends BaseCustomView {

    public final static int ROLE_BUYER = 1;
    public final static int ROLE_SELLER = 2;

    private TextView percent;
    private ImageView iconView;
    private int viewType;
    private LinearLayout layout;
    private BottomSheetDialog dialog;

    public ReputationView(Context context) {
        super(context);
        init();
    }

    public ReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ReputationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        removeAllViews();
        View view;
        if (viewType == 1) {
            view = inflate(getContext(), R.layout.buyer_reputation, this);
            iconView = (ImageView) view.findViewById(R.id.icon);
            percent = (TextView) view.findViewById(R.id.percent);
            layout = (LinearLayout) view.findViewById(R.id.buyer_reputation);
        } else {
            view = inflate(getContext(), R.layout.seller_reputation, this);
            layout = (LinearLayout) view.findViewById(R.id.seller_reputation);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public void setBuyer(String text, final int positive,
                         final int neutral, final int negative,
                         int noReputation, boolean isClickable) {
        viewType = ROLE_BUYER;
        init();
        if (noReputation == 1) {
            setIcon(getResources().getDrawable(R.drawable.ic_smiley_empty));
            if (percent != null)
                percent.setVisibility(GONE);
        } else {
            setIcon(getResources().getDrawable(R.drawable.ic_smiley_good));
            setTitleText(text);
            if (layout != null && isClickable)
                layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getContext() != null) {
                            dialog = new BottomSheetDialog(getContext());
                            dialog.setContentView(R.layout.buyer_reputation_bottom_sheet_dialog);
                            TextView positiveText = (TextView) dialog.findViewById(R.id.score_good);
                            if (positiveText != null)
                                positiveText.setText(String.valueOf(positive));
                            TextView neutralText = (TextView) dialog.findViewById(R.id
                                    .score_netral);
                            if (neutralText != null) neutralText.setText(String.valueOf(neutral));
                            TextView negativeText = (TextView) dialog.findViewById(R.id.score_bad);
                            if (negativeText != null)
                                negativeText.setText(String.valueOf(negative));
                            Button closeButton = (Button) dialog.findViewById(R.id.close_button);
                            if (closeButton != null)
                                closeButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            dialog.show();
                        }
                    }

                });
        }
    }

    private void setIcon(Drawable drawable) {
        if (iconView != null)
            iconView.setImageDrawable(drawable);
    }

    public void setSeller(final int typeMedal, final int levelMedal, final String
            reputationPoints, final boolean isClickable) {
        viewType = ROLE_SELLER;
        init();
        ReputationBadgeUtils.setReputationMedals(getContext(), layout, typeMedal, levelMedal,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (getContext() != null && isClickable) {
                            dialog = new BottomSheetDialog(getContext());
                            dialog.setContentView(R.layout.seller_reputation_bottom_sheet_dialog);
                            TextView point = (TextView) dialog.findViewById(R.id.reputation_point);

                            String pointText = TextUtils.isEmpty(String.valueOf(reputationPoints)) ?
                                    getContext().getString(R.string.no_reputation_yet) :
                                    String.valueOf(reputationPoints) +
                                            " " + getContext().getString(R.string.point);

                            if (point != null) point.setText(pointText);

                            LinearLayout sellerReputation = (LinearLayout) dialog.findViewById(R.id
                                    .seller_reputation);
                            ReputationBadgeUtils.setReputationMedals(getContext(),
                                    sellerReputation, typeMedal, levelMedal,
                                    null);
                            Button closeButton = (Button) dialog.findViewById(R.id.close_button);

                            if (closeButton != null)
                                closeButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            dialog.show();
                        }
                    }
                });
    }

    private void setTitleText(String text) {
        if (!TextUtils.isEmpty(text) && percent != null) {
            percent.setVisibility(VISIBLE);
            percent.setText(text);
        } else if (percent != null) {
            percent.setVisibility(GONE);
        }
    }
}
