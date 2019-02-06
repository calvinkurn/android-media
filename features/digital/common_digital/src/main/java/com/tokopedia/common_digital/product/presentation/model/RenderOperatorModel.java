package com.tokopedia.common_digital.product.presentation.model;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class RenderOperatorModel {

    private List<InputFieldModel> inputFieldModels;
    private List<RenderProductModel> renderProductModels;

    public RenderOperatorModel(List<InputFieldModel> inputFieldModels, List<RenderProductModel> renderProductModels) {
        this.inputFieldModels = inputFieldModels;
        this.renderProductModels = renderProductModels;
    }

    public List<InputFieldModel> getInputFieldModels() {
        return inputFieldModels;
    }

    public List<RenderProductModel> getRenderProductModels() {
        return renderProductModels;
    }

}
