package com.tokopedia.design.reputation;

/**
 * Created by stevenfredian on 8/16/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.card.ToolTipUtils;

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

    //
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

    public void setBuyer(String text, final int positive, final int neutral, final int negative, int noReputation) {
        viewType = ROLE_BUYER;
        init();
        if (noReputation == 1) {
            setIcon(getResources().getDrawable(R.drawable.ic_smiley_empty));
            if (percent != null)
                percent.setVisibility(GONE);
        } else {
            setIcon(getResources().getDrawable(R.drawable.ic_smiley_good));
            setTitleText(text);
            if (iconView != null)
                iconView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToolTipUtils.showToolTip(setViewToolTip(positive, neutral, negative), v);
                    }

                });
            if (percent != null)
                percent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToolTipUtils.showToolTip(setViewToolTip(positive, neutral, negative), v);
                    }
                });
        }
    }

    private View setViewToolTip(final int positive, final int neutral, final int negative) {
        return ToolTipUtils.setToolTip(getContext(), R.layout.tooltip_reputation,
                new ToolTipUtils.ToolTipListener() {
                    @Override
                    public void setView(View view) {
                        TextView smile = (TextView) view.findViewById(R.id.text_smile);
                        TextView netral = (TextView) view.findViewById(R.id.text_netral);
                        TextView bad = (TextView) view.findViewById(R.id.text_bad);
                        smile.setText(String.valueOf(positive));
                        netral.setText(String.valueOf(neutral));
                        bad.setText(String.valueOf(negative));
                    }

                    @Override
                    public void setListener() {

                    }
                });
    }

    private void setIcon(Drawable drawable) {
        if (iconView != null)
            iconView.setImageDrawable(drawable);
    }

    public void setSeller(int typeMedal, int levelMedal, String reputationPoints) {
        viewType = ROLE_SELLER;
        init();
        ReputationBadgeUtils.setReputationMedals(getContext(), layout, typeMedal, levelMedal, reputationPoints);
    }

    private void setTitleText(String text) {
        if (!TextUtils.isEmpty(text) && percent != null) {
            percent.setVisibility(VISIBLE);
            percent.setText(text);
        }
    }
}
