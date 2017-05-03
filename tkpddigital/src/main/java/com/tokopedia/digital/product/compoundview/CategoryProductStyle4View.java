package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;

import com.tokopedia.digital.product.model.CategoryData;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle4View extends BaseDigitalProductView<CategoryData> {
    public CategoryProductStyle4View(Context context) {
        super(context);
    }

    public CategoryProductStyle4View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryProductStyle4View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getHolderLayoutId() {
        return 0;
    }

    @Override
    public void renderData(CategoryData data) {

    }
}
