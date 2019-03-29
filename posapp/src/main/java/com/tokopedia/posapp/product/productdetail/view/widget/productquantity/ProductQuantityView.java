package com.tokopedia.posapp.product.productdetail.view.widget.productquantity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.posapp.R;

/**
 * Created by okasurya on 8/11/17.
 */

public class ProductQuantityView extends BaseView<ProductQuantity,ProductQuantityView.ProductQuantityViewListener> {

    interface ProductQuantityViewListener {

    }

    private ProductQuantityViewListener listener;

    private View buttonDecrease;
    private TextInputLayout tilQuantity;
    private EditText etQuantity;
    private View buttonIncrease;

    public ProductQuantityView(Context context) {
        super(context);
    }

    public ProductQuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        buttonDecrease = findViewById(R.id.decrease_button);
        tilQuantity = findViewById(R.id.til_quantity);
        etQuantity = findViewById(R.id.et_quantity);
        buttonIncrease = findViewById(R.id.increase_button);

        buttonDecrease.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionDecreaseQuantity();
            }
        });

        buttonIncrease.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionIncreaseQuantity();
            }
        });
        etQuantity.setText("1");
    }

    @Override
    public void setListener(ProductQuantityViewListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_product_quantity;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull ProductQuantity data) {
        etQuantity.setText(String.valueOf(data.getQuantity()));
        setVisibility(VISIBLE);
    }

    private void actionIncreaseQuantity() {
        if (!etQuantity.getText().toString().isEmpty()
                && Integer.parseInt(etQuantity.getText().toString()) > 0) {
            etQuantity.setText(String
                    .valueOf(Integer.parseInt(etQuantity.getText().toString()) + 1));
        } else etQuantity.setText("1");
    }

    private void actionDecreaseQuantity() {
        if (!etQuantity.getText().toString().isEmpty()
                && Integer.parseInt(etQuantity.getText().toString()) > 1) {
            etQuantity.setText(String
                    .valueOf(Integer.parseInt(etQuantity.getText().toString()) - 1));
        } else etQuantity.setText("1");
    }

    public int getProductQuantity() {
        return Integer.parseInt(etQuantity.getText().toString());
    }
}