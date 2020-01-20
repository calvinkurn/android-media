package com.tokopedia.digital_deals.view.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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
        return com.tokopedia.digital_deals.R.layout.widget_search_input_view_deals;
    }

    public void setSearchImageView(Drawable drawable){
        ImageView imgView = findViewById(com.tokopedia.digital_deals.R.id.image_view_search);
        imgView.setImageDrawable(drawable);
    }

    public void setSearchImageViewDimens(int width, int height){
        ImageView imgView = findViewById(com.tokopedia.digital_deals.R.id.image_view_search);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) imgView.getLayoutParams();
        layoutParams.width=width;
        layoutParams.height=height;
    }

    public void setSearchTextSize(float size){
        TextView searchText = findViewById(com.tokopedia.digital_deals.R.id.edit_text_search);
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setSearchTextColor(int textColor) {
        TextView searchText = findViewById(com.tokopedia.digital_deals.R.id.edit_text_search);
        searchText.setTextColor(textColor);
    }

    @Override
    public int getCloseImageButtonResourceId() {
        return com.tokopedia.digital_deals.R.id.image_button_close;
    }

    @Override
    public int getSearchImageViewResourceId() {
        return com.tokopedia.digital_deals.R.id.image_view_search;
    }

    @Override
    public int getSearchTextViewResourceId() {
        return com.tokopedia.digital_deals.R.id.edit_text_search;
    }
}
