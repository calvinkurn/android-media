package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.Field;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.product.model.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class CategoryProductStyle1View extends BaseDigitalProductView<CategoryData> {

    @BindView(R2.id.tv_label_client_number)
    TextView tvLabelClientNumber;
    @BindView(R2.id.et_client_number)
    ClientNumberInputView clientNumberInputView;
    @BindView(R2.id.tv_label_spinner_1)
    TextView tvLabelProduct;
    @BindView(R2.id.spinner_1)
    Spinner spProduct;
    @BindView(R2.id.tv_error_spinner_1)
    TextView tvErrorProduct;
    @BindView(R2.id.btn_buy_digital)
    TextView btnBuy;

    private List<Product> productList = new ArrayList<>();

    public CategoryProductStyle1View(Context context) {
        super(context);
    }

    public CategoryProductStyle1View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryProductStyle1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_holder_category_product_style_1;
    }

    @Override
    public void renderData(final CategoryData data) {
        final Field field = data.getFieldList().get(0);
        tvLabelClientNumber.setText(field.getText());
        //  tvLabelOperator.setText(data.get);
        clientNumberInputView.setActionListener(new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {

            }

            @Override
            public void onClientNumberTextChanged(CharSequence charSequence, int start, int before, int count) {
                String tempInput = charSequence.toString();
                for (Validation validation : field.getValidation()) {
                    if (Pattern.matches(validation.getRegex(), tempInput)) {
                        spProduct.setVisibility(GONE);
                        tvLabelProduct.setVisibility(GONE);
                        tvErrorProduct.setVisibility(GONE);
                        tvLabelProduct.setText("");
                        tvErrorProduct.setText("");

                        clientNumberInputView.enableErrorClientNumber(validation.getError());
                        return;
                    } else {
                        for (Operator operator : data.getOperatorList()) {
                            for (String prefix : operator.getPrefixList()) {
                                if (tempInput.startsWith(prefix)) {
                                    productList.clear();
                                    productList.addAll(operator.getProductList());
                                    spProduct.setVisibility(VISIBLE);
                                    return;
                                } else {
                                    productList.clear();
                                }
                            }
                        }
                        tvLabelProduct.setVisibility(GONE);
                        tvErrorProduct.setVisibility(GONE);
                        tvLabelProduct.setText("");
                        tvErrorProduct.setText("");
                        clientNumberInputView.disableErrorClientNumber();
                    }
                }

            }

            @Override
            public void onClientNumberClear() {

            }
        });
        setPhoneBookVisibility(field);
    }

    private void setPhoneBookVisibility(Field field) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (field.getType().equalsIgnoreCase("tel")) {
            clientNumberInputView.getBtnContactPicker().setVisibility(View.VISIBLE);
            layoutParams.weight = 0.92f;
        } else {
            clientNumberInputView.getBtnContactPicker().setVisibility(View.GONE);
            layoutParams.weight = 1;
        }
        clientNumberInputView.getPulsaFramelayout().setLayoutParams(layoutParams);
    }


}
