package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.common_digital.product.presentation.model.AdditionalButton;
import com.tokopedia.common_digital.product.presentation.model.BaseWidgetItem;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.RenderOperatorModel;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.digital.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class DigitalCategoryNoStyleView extends LinearLayout {

    private LinearLayout containerDigitalOperatorWidgetView;
    private LinearLayout containerDigitalProductWidgetView;

    private ActionListener actionListener;

    public interface ActionListener {

        void onClickOperatorDropdown(InputFieldModel inputFieldModel, String selectedItemId);

        void onProductSelected(Product item);

        void removeBuyView();

        void onClickProductDropdown(InputFieldModel inputFieldModel, String operatorId, int position);

    }

    public DigitalCategoryNoStyleView(Context context) {
        super(context);
        init(context);
    }

    public DigitalCategoryNoStyleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalCategoryNoStyleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalCategoryNoStyleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_agent_digital_category,
                this, true);
        containerDigitalOperatorWidgetView = view.findViewById(R.id.container_digital_operator_widget_view);
        containerDigitalProductWidgetView = view.findViewById(R.id.container_digital_product_widget_view);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderCategory(RenderOperatorModel renderOperatorModel, String renderOperatorId) {
        if (containerDigitalOperatorWidgetView.getChildCount() > 0) {
            containerDigitalOperatorWidgetView.removeAllViews();
        }

        final String[] oldOperatorId = {null};

        DigitalWidgetView operatorWidgetView =
                new DigitalWidgetView(getContext());
        operatorWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
            @Override
            public void onItemSelected(BaseWidgetItem operatorItem) {
                if (operatorItem instanceof Operator) {
                    // re-render products for selected operator if only a new operator is actually selected
                    String selectedOperatorId = ((Operator) operatorItem).getOperatorId();
                    if (!selectedOperatorId.equals(oldOperatorId[0])) {
                        oldOperatorId[0] = selectedOperatorId;
                        renderProductsByOperatorId(renderOperatorModel, selectedOperatorId);
                    }
                }
            }

            @Override
            public void onOperatorByPrefixNotFound() {
                if (containerDigitalOperatorWidgetView.getChildCount() > 0) {
                    oldOperatorId[0] = "0";
                    containerDigitalProductWidgetView.removeAllViews();
                    actionListener.removeBuyView();
                }
            }

            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                actionListener.onClickOperatorDropdown(inputFieldModel, selectedItemId);
            }

            @Override
            public void onClickInquiryButton() {

            }
        });
        containerDigitalOperatorWidgetView.addView(operatorWidgetView);
        List<BaseWidgetItem> baseWidgetItems =
                new ArrayList<>(transformOperators(renderOperatorModel.getRenderProductModels()));
        operatorWidgetView.renderWidget(renderOperatorModel.getInputFieldModels().get(0),
                baseWidgetItems, renderOperatorId);
    }

    private void renderProductsByOperatorId(RenderOperatorModel renderOperatorModel, String operatorId) {
        // remove rendered products view
        if (containerDigitalProductWidgetView.getChildCount() > 0) {
            containerDigitalProductWidgetView.removeAllViews();
            actionListener.removeBuyView();
        }

        RenderProductModel renderProductModel = findOperatorById(operatorId,
                renderOperatorModel.getRenderProductModels());
        Operator operator = renderProductModel.getOperator();

        boolean showInputField = true;

        for (int i=0; i<renderProductModel.getInputFieldModels().size(); i++ ) {
            if (showInputField) {
                DigitalWidgetView productWidgetView =
                        new DigitalWidgetView(getContext());
                productWidgetView.setTag(i);
                productWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
                    @Override
                    public void onItemSelected(BaseWidgetItem item) {
                        if (item instanceof Product) {
                            actionListener.onProductSelected((Product) item);
                        }
                    }

                    @Override
                    public void onOperatorByPrefixNotFound() {

                    }

                    @Override
                    public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                        actionListener.onClickProductDropdown(inputFieldModel, operator.getOperatorId(),
                                (Integer) productWidgetView.getTag());
                    }

                    @Override
                    public void onClickInquiryButton() {
                        DigitalWidgetView productInquiryWidgetView =
                                createProductInquiryView(renderProductModel, operator, ((Integer) productWidgetView.getTag()));
                        List<BaseWidgetItem> productList = new ArrayList<>(operator.getProductList());
                        String productId = String.valueOf(operator.getDefaultProductId());
                        containerDigitalProductWidgetView.addView(productInquiryWidgetView);
                        productInquiryWidgetView.renderWidget(
                                renderProductModel.getInputFieldModels().get((Integer) productInquiryWidgetView.getTag()),
                                productList,
                                productId);
                    }
                });

                List<BaseWidgetItem> productList = new ArrayList<>(operator.getProductList());
                String productId = String.valueOf(operator.getDefaultProductId());
                containerDigitalProductWidgetView.addView(productWidgetView);
                productWidgetView.renderWidget(renderProductModel.getInputFieldModels().get(i),
                        productList,
                        productId);
            }
            if (isInquiry(renderProductModel.getInputFieldModels().get(i).getAdditionalButton())) {
                showInputField = false;
            }
        }
    }

    private boolean isInquiry(AdditionalButton additionalButton) {
        return additionalButton != null && additionalButton.getType().equals("inquiry");
    }

    private DigitalWidgetView createProductInquiryView(RenderProductModel renderProductModel, Operator operator, Integer tag) {
        DigitalWidgetView productInquiryWidgetView = new DigitalWidgetView(getContext());
        productInquiryWidgetView.setTag(renderProductModel.getInputFieldModels().size()-1);
        productInquiryWidgetView.setActionListener(new DigitalWidgetView.ActionListener() {
            @Override
            public void onItemSelected(BaseWidgetItem item) {
                if (item instanceof Product) {
                    actionListener.onProductSelected((Product) item);
                    showInquiryResult();
                }
            }

            private void showInquiryResult() {
                if (containerDigitalProductWidgetView.findViewWithTag("inquiry_result_view") == null) {
                    DigitalInquiryResultNoStyleView digitalInquiryResultNoStyleView =
                            new DigitalInquiryResultNoStyleView(getContext());
                    digitalInquiryResultNoStyleView.setTag("inquiry_result_view");
                    containerDigitalProductWidgetView.addView(digitalInquiryResultNoStyleView,
                            (Integer) productInquiryWidgetView.getTag());
                }
            }

            @Override
            public void onOperatorByPrefixNotFound() {

            }

            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                actionListener.onClickProductDropdown(inputFieldModel, operator.getOperatorId(),
                        ((Integer) productInquiryWidgetView.getTag()));
            }

            @Override
            public void onClickInquiryButton() {

            }
        });

        return productInquiryWidgetView;
    }

    public void updateProductDropdownView(RenderOperatorModel renderOperatorModel, InputFieldModel inputFieldModel, String operatorId,
                                          String productId, int viewTag) {
        DigitalWidgetView productWidgetView = containerDigitalProductWidgetView.findViewWithTag(viewTag);
        containerDigitalProductWidgetView.removeView(productWidgetView);

        RenderProductModel renderProductModel = findOperatorById(operatorId,
                renderOperatorModel.getRenderProductModels());
        Operator operator = renderProductModel.getOperator();
        List<BaseWidgetItem> baseWidgetItems = new ArrayList<>(operator.getProductList());

        productWidgetView.renderWidget(inputFieldModel,
                baseWidgetItems,
                productId);
        containerDigitalProductWidgetView.addView(productWidgetView);
    }

    private RenderProductModel findOperatorById(String operatorId,
                                                List<RenderProductModel> renderProductModels) {
        for (int i = 0, operatorsSize = renderProductModels.size(); i < operatorsSize; i++) {
            RenderProductModel renderProductModel = renderProductModels.get(i);
            if (String.valueOf(renderProductModel.getOperator().getOperatorId())
                    .equalsIgnoreCase(operatorId)) {
                return renderProductModel;
            }
        }
        return null;
    }

    private List<Operator> transformOperators(List<RenderProductModel> renderProductModels) {
        List<Operator> operators = new ArrayList<>();
        for (RenderProductModel renderProductModel : renderProductModels) {
            operators.add(renderProductModel.getOperator());
        }
        return operators;
    }

}