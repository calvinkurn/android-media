package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.mapper;

import com.tokopedia.common_digital.product.presentation.model.AdditionalButton;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.OperatorBuilder;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.RenderOperatorModel;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.common_digital.product.presentation.model.Rule;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseInputField;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseOperator;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseProduct;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseRechargeCategoryDetail;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseRenderOperator;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseRenderProduct;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseValidation;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model.DigitalCategoryModelBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class RechargeCategoryDetailMapper {

    public DigitalCategoryModel map(ResponseAgentDigitalCategory responseAgentDigitalCategory) {
        ResponseRechargeCategoryDetail responseRechargeCategoryDetail = responseAgentDigitalCategory.getRechargeCategoryDetail();
        String id = responseRechargeCategoryDetail.getId();
        String name = responseRechargeCategoryDetail.getName();
        String title = responseRechargeCategoryDetail.getTitle();
        String operatorLabel = responseRechargeCategoryDetail.getOperatorLabel();
        String operatorStyle = responseRechargeCategoryDetail.getOperatorStyle();
        String defaultOperatorId = responseRechargeCategoryDetail.getDefaultOperatorId();
        String icon = responseRechargeCategoryDetail.getIcon();
        ResponseRenderOperator responseRenderOperator = responseRechargeCategoryDetail.getRenderOperator();
        RenderOperatorModel renderOperatorModel = transformRenderOperator(responseRenderOperator);

        return new DigitalCategoryModelBuilder()
                .id(id)
                .name(name)
                .title(title)
                .operatorLabel(operatorLabel)
                .operatorStyle(operatorStyle)
                .defaultOperatorId(defaultOperatorId)
                .icon(icon)
                .renderOperatorModel(renderOperatorModel)
                .createDigitalCategoryModel();
    }

    private RenderOperatorModel transformRenderOperator(ResponseRenderOperator responseRenderOperator) {
        List<InputFieldModel> inputFieldModels = transformInputFields(responseRenderOperator.getInputFields());
        List<RenderProductModel> renderProductModels =
                transformRenderProductModels(responseRenderOperator.getRenderProduct());
        return new RenderOperatorModel(inputFieldModels, renderProductModels);
    }

    private List<RenderProductModel> transformRenderProductModels(List<ResponseRenderProduct> responseRenderProducts) {
        List<RenderProductModel> renderProductModels = new ArrayList<>();
        for (ResponseRenderProduct responseRenderProduct : responseRenderProducts) {
            renderProductModels.add(new RenderProductModel(transformOperator(responseRenderProduct.getOperator()),
                    transformInputFields(responseRenderProduct.getInputFields())));
        }
        return renderProductModels;
    }

    private Operator transformOperator(ResponseOperator responseOperator) {
        String id = responseOperator.getId();
        int defaultProducId = responseOperator.getAttributes().getDefaultProductId();
        String image = responseOperator.getAttributes().getImage();
        String name = responseOperator.getAttributes().getName();
        Rule rule = responseOperator.getAttributes().getRule();
        List<String> prefixList = responseOperator.getAttributes().getPrefix();
        List<Product> products = transformProducts(responseOperator.getAttributes().getProduct());
        return new OperatorBuilder()
                .operatorId(id)
                .name(name)
                .image(image)
                .prefixList(prefixList)
                .rule(rule)
                .defaultProductId(defaultProducId)
                .products(products)
                .createOperator();
    }

    private List<InputFieldModel> transformInputFields(List<ResponseInputField> responseInputFields) {
        List<InputFieldModel> inputFieldModels = new ArrayList<>();
        for (ResponseInputField responseInputField : responseInputFields) {
            String name = responseInputField.getName();
            String type = responseInputField.getType();
            String text = responseInputField.getText();
            String placeholder = responseInputField.getPlaceholder();
            String _default = responseInputField.getDefault();
            List<Validation> validation = transformValidation(responseInputField.getValidation());
            InputFieldModel inputFieldModel = new InputFieldModel(name, type, text, placeholder, _default, validation);
            if (responseInputField.getType().equals("numeric")) {
                inputFieldModel.setAdditionalButton(new AdditionalButton("inquiry", "Cek"));
            }
            inputFieldModels.add(inputFieldModel);
        }
        return inputFieldModels;
    }

    private List<Validation> transformValidation(List<ResponseValidation> responseValidations) {
        List<Validation> validation = new ArrayList<>();
        if (responseValidations != null && !responseValidations.isEmpty()) {
            for (ResponseValidation responseValidation : responseValidations) {
                validation.add(new Validation(responseValidation.getRegex(), responseValidation.getError()));
            }
        }
        return validation;
    }

    private List<Product> transformProducts(List<ResponseProduct> responseProducts) {
        List<Product> products = new ArrayList<>();
        for (ResponseProduct responseProduct : responseProducts) {
            String productId = responseProduct.getId();
            String info = responseProduct.getAttributes().getInfo();
            int status = responseProduct.getAttributes().getStatus();
            String detail = responseProduct.getAttributes().getDetail();
            String detailUrl = responseProduct.getAttributes().getDetailUrl();
            String detailUrlText = responseProduct.getAttributes().getDetailUrlText();
            String desc = responseProduct.getAttributes().getDesc();
            String price = responseProduct.getAttributes().getPrice();
            products.add(new Product.Builder()
                    .info(info)
                    .productId(productId)
                    .status(status)
                    .detail(detail)
                    .detailUrl(detailUrl)
                    .detailUrlText(detailUrlText)
                    .desc(desc)
                    .price(price)
                    .build());
        }
        return products;
    }

}
