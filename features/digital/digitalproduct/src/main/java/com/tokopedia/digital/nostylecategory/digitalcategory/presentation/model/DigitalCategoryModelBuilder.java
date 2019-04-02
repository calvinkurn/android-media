package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model;

import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.RenderOperatorModel;

import java.util.List;

public class DigitalCategoryModelBuilder {
    private String id;
    private String name;
    private String title;
    private String operatorLabel;
    private String operatorStyle;
    private String defaultOperatorId;
    private String icon;
    private RenderOperatorModel renderOperatorModel;
    private List<ClientNumber> clientNumberList;
    private List<Operator> operators;

    public DigitalCategoryModelBuilder id(String id) {
        this.id = id;
        return this;
    }

    public DigitalCategoryModelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DigitalCategoryModelBuilder title(String title) {
        this.title = title;
        return this;
    }

    public DigitalCategoryModelBuilder operatorLabel(String operatorLabel) {
        this.operatorLabel = operatorLabel;
        return this;
    }

    public DigitalCategoryModelBuilder operatorStyle(String operatorStyle) {
        this.operatorStyle = operatorStyle;
        return this;
    }

    public DigitalCategoryModelBuilder defaultOperatorId(String defaultOperatorId) {
        this.defaultOperatorId = defaultOperatorId;
        return this;
    }

    public DigitalCategoryModelBuilder renderOperatorModel(RenderOperatorModel renderOperatorModel) {
        this.renderOperatorModel = renderOperatorModel;
        return this;
    }

    public DigitalCategoryModelBuilder icon(String icon) {
        this.icon = icon;
        return this;
    }

    public DigitalCategoryModelBuilder clientNumberList(List<ClientNumber> clientNumberList) {
        this.clientNumberList = clientNumberList;
        return this;
    }

    public DigitalCategoryModelBuilder operators(List<Operator> operators) {
        this.operators = operators;
        return this;
    }

    public DigitalCategoryModel createDigitalCategoryModel() {
        return new DigitalCategoryModel(id, name, title, operatorLabel, operatorStyle, defaultOperatorId,
                icon, renderOperatorModel, clientNumberList, operators);
    }
}