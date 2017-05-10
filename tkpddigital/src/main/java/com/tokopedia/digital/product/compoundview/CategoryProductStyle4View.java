package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.Product;

import java.util.List;

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
    protected void initialViewListener(Context context) {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_4;
    }

    @Override
    public void renderData(CategoryData data) {

    }

    @Override
    public void renderUpdateProductSelected(Product product) {

    }

    @Override
    public void renderUpdateOperatorSelected(Operator operator) {

    }

    @Override
    public void renderClientNumberFromContact(String clientNumber) {

    }

    @Override
    public Operator getSelectedOperator() {
        return null;
    }

    @Override
    public Product getSelectedProduct() {
        return null;
    }

    @Override
    public boolean isInstantCheckoutChecked() {
        return false;
    }

    @Override
    public String getClientNumber() {
        return null;
    }

    @Override
    public void renderStateDataSelected(
            String clientNumberState, Operator operatorSelectedState,
            Product productSelectedState, boolean isInstantCheckoutChecked
    ) {

    }

    @Override
    public void renderDataRecentClientNumber(List<OrderClientNumber> recentClientNumberList,
                                             OrderClientNumber lastOrderClientNumber) {
        //TODO ANGGA
    }
}
