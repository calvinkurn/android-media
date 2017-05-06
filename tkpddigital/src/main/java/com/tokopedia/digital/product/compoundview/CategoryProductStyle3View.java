package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.CategoryData;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle3View extends BaseDigitalProductView<CategoryData> {
    public CategoryProductStyle3View(Context context) {
        super(context);
    }

    public CategoryProductStyle3View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryProductStyle3View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_3;
    }

    @Override
    public void renderData(CategoryData data) {

    }
}
