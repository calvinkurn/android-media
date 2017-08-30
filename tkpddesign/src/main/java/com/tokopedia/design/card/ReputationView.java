package com.tokopedia.design.card;

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
import android.widget.TextView;

import com.tokopedia.design.R;;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by stevenfredian on 8/16/17.
 */

public class ReputationView extends BaseCustomView {

    public static int ROLE_BUYER = 1;
    public static int ROLE_SELLER = 2;


    private TextView percent;
    private String defaultText;
    private Drawable defaultIcon;
    private ImageView iconView;
    private String defaultView;
    private int viewType;

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

        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ReputationView);
        try {
            defaultView = styledAttributes.getString(R.styleable.ReputationView_rep_layout);
            defaultIcon = styledAttributes.getDrawable(R.styleable.ReputationView_rep_icon);
            defaultText = styledAttributes.getString(R.styleable.ReputationView_rep_title_text);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view;
        if (viewType == ROLE_BUYER) {
            view = inflate(getContext(), R.layout.buyer_reputation, this);
            iconView = (ImageView) view.findViewById(R.id.icon);
            percent = (TextView) view.findViewById(R.id.percent);
        } else {
            view = inflate(getContext(), R.layout.seller_reputation, this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setIconDrawable(defaultIcon);
        setTitleText(defaultText);
        invalidate();
        requestLayout();
    }


    public void setRole(int role) {
        viewType = role;
        init();
    }

    public void setIconDrawable(Drawable icon) {
        if (icon != null && iconView != null) {
            iconView.setImageDrawable(icon);
        }
    }

    public void setTitleText(String text) {
        if (!TextUtils.isEmpty(text) && percent != null) {
            percent.setText(text);
        }
    }
}
