package com.tokopedia.digital_deals.view.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.tokopedia.digital_deals.R;


public class SearchInputView extends com.tokopedia.design.text.SearchInputView {


    public SearchInputView(Context context) {
        super(context);
    }

    public SearchInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.widget_search_input_view_deals;
    }

    public void setSearchImageView(Drawable drawable){
        getSearchImageView().setImageDrawable(drawable);
    }

    public void setSearchImageViewDimens(int width, int height){
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) getSearchImageView().getLayoutParams();
        layoutParams.width=width;
        layoutParams.height=height;
    }

    public void setSearchTextSize(float size){
        getSearchTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setSearchTextColor(int textColor) {
        getSearchTextView().setTextColor(textColor);
    }

}
