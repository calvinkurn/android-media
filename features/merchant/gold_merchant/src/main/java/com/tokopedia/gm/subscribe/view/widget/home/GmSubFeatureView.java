package com.tokopedia.gm.subscribe.view.widget.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.gm.R;

/**
 * Created by sebastianuskh on 1/20/17.
 */

public class GmSubFeatureView extends FrameLayout {

    ImageView imageViewSubFeature;

    TextView textViewDescSubFeature;

    private Drawable imageSubFeature;
    private String descSubFeature;

    /**
     * CONSTRUCTOR
     */
    public GmSubFeatureView(Context context) {
        super(context);
        initView();
    }

    public GmSubFeatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public GmSubFeatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * VIEW CODE
     */
    private void initView(AttributeSet attrs) {
        initView();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GMFeatureHomeView);
        try {
            imageSubFeature = styledAttributes.getDrawable(R.styleable.GMFeatureHomeView_feature_img);
            descSubFeature = styledAttributes.getString(R.styleable.GMFeatureHomeView_feature_desc);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.partial_gm_subscribe_sub_feature, null);
        imageViewSubFeature = (ImageView) view.findViewById(R.id.image_gm_subscribe_sub_feature);
        textViewDescSubFeature = (TextView) view.findViewById(R.id.desc_gm_subscribe_sub_feature);
        addView(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageViewSubFeature.setImageDrawable(imageSubFeature);
        textViewDescSubFeature.setText(descSubFeature);
        invalidate();
        requestLayout();
    }
}
