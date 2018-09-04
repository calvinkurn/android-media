package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.common_digital.product.presentation.compoundview.ClientNumberInputView;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.mitra.R;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class DigitalOperatorChooserView extends FrameLayout {

    private ClientNumberInputView clientNumberInputView;
    private DigitalOperatorDropdownInputView operatorDropdownInputView;
    private DigitalOperatorRadioInputView operatorRadioInputView;

    public DigitalOperatorChooserView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DigitalOperatorChooserView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalOperatorChooserView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalOperatorChooserView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_digital_operator_chooser,
                this, true);
        clientNumberInputView = view.findViewById(R.id.operator_client_number_input_view);
        operatorDropdownInputView = view.findViewById(R.id.operator_dropdown_input_view);
        operatorRadioInputView = view.findViewById(R.id.operator_radio_input_view);
    }

    public void showOperatorChooser(List<InputFieldModel> inputFieldModels,
                                    List<RenderProductModel> renderProductModels,
                                    String defaultOperatorId) {
        if (inputFieldModels.get(0).getType().equals("tel")) {
            showClientNumber(transformInputFieldToClientNumber(inputFieldModels.get(0)));
        } else if (inputFieldModels.get(0).getType().equals("select")) {
            showDropdown(renderProductModels, defaultOperatorId);
        } else if (inputFieldModels.get(0).getType().equals("radio")) {
            showRadio(renderProductModels, defaultOperatorId);
        }
    }

    private void showRadio(List<RenderProductModel> renderProductModels, String defaultOperatorId) {
        operatorRadioInputView.setVisibility(VISIBLE);
    }

    private void showDropdown(List<RenderProductModel> renderProductModels, String defaultOperatorId) {
        operatorDropdownInputView.renderOperatorList(renderProductModels, defaultOperatorId);
        operatorDropdownInputView.setVisibility(VISIBLE);
    }

    private void showClientNumber(ClientNumber clientNumber) {
        clientNumberInputView.renderData(clientNumber);
        clientNumberInputView.setActionListener(new ClientNumberInputView.ActionListener() {
            @Override
            public void onButtonContactPickerClicked() {

            }

            @Override
            public void onClientNumberInputValid(String tempClientNumber) {

            }

            @Override
            public void onClientNumberInputInvalid() {

            }

            @Override
            public void onClientNumberHasFocus(String clientNumber) {

            }

            @Override
            public void onClientNumberCleared() {

            }
        });
        clientNumberInputView.setVisibility(VISIBLE);
    }

    private ClientNumber transformInputFieldToClientNumber(InputFieldModel inputFieldModel) {
        return new ClientNumber(inputFieldModel.getName(), inputFieldModel.getType(), inputFieldModel.getText(),
                inputFieldModel.getPlaceholder(), inputFieldModel.getDefault(), inputFieldModel.getValidation());
    }

}
