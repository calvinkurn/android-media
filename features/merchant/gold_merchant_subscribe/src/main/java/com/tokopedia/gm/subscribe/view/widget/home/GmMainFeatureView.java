package com.tokopedia.gm.subscribe.view.widget.home;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.gm.subscribe.R;

/**
 * Created by sebastianuskh on 1/20/17.
 * View for displaying the gm subscribe main features
 */

public class GmMainFeatureView extends FrameLayout {

    ImageView imageViewFeature;

    TextView textViewTitleFeature;

    TextView textViewDescFeature;

    private Drawable imgFeature;
    private String titleFeature;
    private String descFeature;


    /**
     * CONSTRUCTOR
     */
    public GmMainFeatureView(Context context) {
        super(context);
        initView();
    }

    public GmMainFeatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public GmMainFeatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        initView();

        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GMFeatureHomeView);
        try {
            imgFeature = styledAttributes.getDrawable(R.styleable.GMFeatureHomeView_feature_img);
            titleFeature = styledAttributes.getString(R.styleable.GMFeatureHomeView_feature_title);
            descFeature = styledAttributes.getString(R.styleable.GMFeatureHomeView_feature_desc);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.partial_gm_subscribe_main_feature, null);
        imageViewFeature = (ImageView) view.findViewById(R.id.image_gm_subscribe_main_feature);
        textViewTitleFeature = (TextView) view.findViewById(R.id.title_gm_subscribe_main_feature);
        textViewDescFeature = (TextView) view.findViewById(R.id.desc_gm_subscribe_main_feature);
        addView(view);
    }

    public void setImageViewFeature(Drawable imageDrawable){
        this.imgFeature = imageDrawable;
        this.imageViewFeature.setImageDrawable(imageDrawable);
        invalidate();
        requestLayout();
    }

    public void setTitleFeature(@StringRes int stringRes){
        setTitleFeature(getContext().getString(stringRes));
    }

    public void setTitleFeature(String title){
        this.titleFeature = title;
        this.textViewTitleFeature.setText(title);
        invalidate();
        requestLayout();
    }

    public void setDescFeature(String descFeature){
        this.descFeature = descFeature;
        this.textViewDescFeature.setText(descFeature);
        invalidate();
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageViewFeature.setImageDrawable(imgFeature);
        textViewTitleFeature.setText(titleFeature);
        textViewDescFeature.setText(descFeature);
        invalidate();
        requestLayout();
    }
}
