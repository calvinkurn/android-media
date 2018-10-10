package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model;

import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.RenderOperatorModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class DigitalCategoryModel {

    private String id;
    private String name;
    private String title;
    private String operatorLabel;
    private String operatorStyle;
    private String defaultOperatorId;
    private String icon;
    private RenderOperatorModel renderOperatorModel;
    private List<ClientNumber> clientNumberList = new ArrayList<>();
    private List<Operator> operators;

    public DigitalCategoryModel(String id, String name, String title, String operatorLabel, String operatorStyle,
                                String defaultOperatorId, String icon, RenderOperatorModel renderOperatorModel,
                                List<ClientNumber> clientNumberList, List<Operator> operators) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.operatorLabel = operatorLabel;
        this.operatorStyle = operatorStyle;
        this.defaultOperatorId = defaultOperatorId;
        this.icon = icon;
        this.renderOperatorModel = renderOperatorModel;
        this.clientNumberList = clientNumberList;
        this.operators = operators;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getOperatorLabel() {
        return operatorLabel;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public String getIcon() {
        return icon;
    }

    public RenderOperatorModel getRenderOperatorModel() {
        return renderOperatorModel;
    }

    public List<ClientNumber> getClientNumberList() {
        return clientNumberList;
    }

    public List<Operator> getOperators() {
        return operators;
    }



}