package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.common_digital.product.presentation.model.AdditionalButton;
import com.tokopedia.common_digital.product.presentation.model.BaseWidgetItem;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.digitalcategory.presentation.adapter.WidgetItemAdapter;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class DigitalWidgetView extends FrameLayout {

    private MitraClientNumberInputView mitraClientNumberInputView;
    private DigitalWidgetDropdownInputView digitalWidgetDropdownInputView;
    private RecyclerView recyclerview;
    private DigitalWidgetRadioInputView digitalWidgetRadioInputView;

    private ActionListener actionListener;

    private int position;

    interface ActionListener {

        void onItemSelected(BaseWidgetItem item);

        void onOperatorByPrefixNotFound();

        void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId);

        void onClickInquiryButton();

    }

    public DigitalWidgetView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DigitalWidgetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalWidgetView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalWidgetView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_digital_operator_chooser,
                this, true);
        mitraClientNumberInputView = view.findViewById(R.id.client_number_input_view);
        recyclerview = view.findViewById(R.id.recyclerview);
        digitalWidgetDropdownInputView = view.findViewById(R.id.dropdown_input_view);
        digitalWidgetRadioInputView = view.findViewById(R.id.radio_input_view);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderWidget(InputFieldModel inputFieldModel, List<BaseWidgetItem> items, String defaultId) {
        switch (inputFieldModel.getType()) {
            case InputFieldModel.TYPE_TEL:
            case InputFieldModel.TYPE_NUMERIC:
            case InputFieldModel.TYPE_TEXT:
                showClientNumber(transformInputFieldToClientNumber(inputFieldModel), items);
                break;
            case InputFieldModel.TYPE_SELECT:
                showDropdown(items, inputFieldModel, defaultId);
                break;
            case InputFieldModel.TYPE_RADIO:
                showRadio(items, inputFieldModel, defaultId);
                break;
            case InputFieldModel.TYPE_SELECT_LIST:
                showList(items, defaultId);
                break;
        }
    }

    private void showList(List<BaseWidgetItem> items, String defaultId) {
        recyclerview.setVisibility(VISIBLE);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        WidgetItemAdapter widgetItemAdapter = new WidgetItemAdapter(product ->
                actionListener.onItemSelected(product), items, defaultId);
        recyclerview.setAdapter(widgetItemAdapter);

        widgetItemAdapter.notifyDataSetChanged();
    }

    private void showRadio(List<BaseWidgetItem> items, InputFieldModel inputFieldModel, String defaultId) {
        digitalWidgetRadioInputView.setVisibility(VISIBLE);
        digitalWidgetRadioInputView.setActionListener(item -> actionListener.onItemSelected(item));
        digitalWidgetRadioInputView.renderInitDataList(items, inputFieldModel, defaultId);
    }

    private void showDropdown(List<BaseWidgetItem> items, InputFieldModel inputFieldModel, String defaultId) {
        digitalWidgetDropdownInputView.setVisibility(VISIBLE);
        digitalWidgetDropdownInputView.setActionListener(new DigitalWidgetDropdownInputView.ActionListener() {
            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                actionListener.onClickDropdown(inputFieldModel, selectedItemId);
            }

            @Override
            public void onItemSelected(BaseWidgetItem item) {
                actionListener.onItemSelected(item);
            }
        });
        digitalWidgetDropdownInputView.renderDropdownView(items, inputFieldModel, defaultId);
    }

    private void showClientNumber(ClientNumber clientNumber, List<BaseWidgetItem> items) {
        mitraClientNumberInputView.setVisibility(VISIBLE);
        mitraClientNumberInputView.renderData(clientNumber, items);
        mitraClientNumberInputView.setActionListener(new MitraClientNumberInputView.MitraClientNumberActionListener() {
            @Override
            public void onButtonContactPickerClicked() {

            }

            @Override
            public void onClientNumberHasFocus(String clientNumber) {

            }

            @Override
            public void onClientNumberCleared() {

            }

            @Override
            public void onOperatorFoundByPrefix(Operator operator) {
                actionListener.onItemSelected(operator);
            }

            @Override
            public void onOperatorByPrefixNotFound() {
                actionListener.onOperatorByPrefixNotFound();
            }

            @Override
            public void onClickAdditionalButton(AdditionalButton additionalButton) {
                if (additionalButton.getType().equals("inquiry")) {
                    actionListener.onClickInquiryButton();
                }
            }
        });
    }

    private ClientNumber transformInputFieldToClientNumber(InputFieldModel inputFieldModel) {
        ClientNumber clientNumber = new ClientNumber(inputFieldModel.getName(), inputFieldModel.getType(),
                inputFieldModel.getText(), inputFieldModel.getPlaceholder(),
                inputFieldModel.getDefault(), inputFieldModel.getValidation());
        clientNumber.setAdditionalButton(inputFieldModel.getAdditionalButton());
        return clientNumber;
    }

}
