package com.tokopedia.common_digital.product.presentation.model;

import java.util.List;

/**
 * Created by Rizky on 04/09/18.
 */
public class RenderProductModel {

    private Operator operator;
    private List<InputFieldModel> inputFieldModels;

    public RenderProductModel(Operator operator, List<InputFieldModel> inputFieldModels) {
        this.operator = operator;
        this.inputFieldModels = inputFieldModels;
    }

    public Operator getOperator() {
        return operator;
    }

    public List<InputFieldModel> getInputFieldModels() {
        return inputFieldModels;
    }

}
