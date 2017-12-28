package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle4View extends
        BaseDigitalProductView<CategoryData, Operator, Product, HistoryClientNumber> {
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
    protected void onCreateView() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_4;
    }


    @Override
    protected void onInitialDataRendered() {

    }

    @Override
    protected void onUpdateSelectedProductData() {

    }

    @Override
    protected void onUpdateSelectedOperatorData() {

    }

    @Override
    protected void onInstantCheckoutUnChecked() {

    }

    @Override
    protected void onInstantCheckoutChecked() {

    }

    @Override
    public void renderClientNumberFromContact(String clientNumber) {

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
    protected void onRestoreSelectedData(
            Operator operatorSelectedState, Product productSelectedState,
            String clientNumberState, boolean isInstantCheckoutChecked
    ) {

    }

    @Override
    public void clearFocusOnClientNumber() {

    }

}
