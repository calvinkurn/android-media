package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.CategoryData;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle2View extends BaseDigitalProductView<CategoryData> {
    public CategoryProductStyle2View(Context context) {
        super(context);
    }

    public CategoryProductStyle2View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryProductStyle2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_2;
    }

    @Override
    public void renderData(CategoryData data) {

    }
}
