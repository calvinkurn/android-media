package com.tokopedia.productcard.v2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.productcard.R;

public class ProductCardViewList extends ProductCardView {

    public ProductCardViewList(@NonNull Context context) {
        super(context);
    }

    public ProductCardViewList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductCardViewList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.product_card_layout_v2_big_grid;
    }

    @Override
    public void realignLayout() {

    }
}
