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
public class DigitalWidgetView<T> extends FrameLayout {

    private MitraClientNumberInputView mitraClientNumberInputView;
    private DigitalWidgetDropdownInputView digitalWidgetDropdownInputView;
    private RecyclerView recyclerview;
    private DigitalWidgetRadioInputView digitalWidgetRadioInputView;

    private ActionListener actionListener;

    interface ActionListener<T> {

        void onItemSelected(T itemId);

        void onOperatorNotFound();

        void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId);

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

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderWidget(InputFieldModel inputFieldModel, List<T> items, String defaultId) {
        if (inputFieldModel.getType().equals("tel") || inputFieldModel.getType().equals("numeric") ||
                inputFieldModel.getType().equals("text")) {
            showClientNumber(transformInputFieldToClientNumber(inputFieldModel), items);
        } else if (inputFieldModel.getType().equals("select")) {
            showDropdown(items, inputFieldModel, defaultId);
        } else if (inputFieldModel.getType().equals("list")) {
            showList(items, defaultId);
        } else if (inputFieldModel.getType().equals("radio")) {
            showRadio(items, inputFieldModel, defaultId);
        } else if (inputFieldModel.getType().equals("select_list")) {
            showList(items, defaultId);
        }
    }

    private void showList(List<T> items, String defaultId) {
        recyclerview.setVisibility(VISIBLE);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        WidgetItemAdapter widgetItemAdapter = new WidgetItemAdapter(new WidgetItemAdapter.ActionListener() {
            @Override
            public void onItemSelected(Product product) {
                actionListener.onItemSelected(product);
            }
        }, items, defaultId);
        recyclerview.setAdapter(widgetItemAdapter);

        widgetItemAdapter.notifyDataSetChanged();
    }

    private void showRadio(List<T> items, InputFieldModel inputFieldModel, String defaultId) {
        digitalWidgetRadioInputView.setVisibility(VISIBLE);
        digitalWidgetRadioInputView.setActionListener(item -> {
            actionListener.onItemSelected(item);
        });
        digitalWidgetRadioInputView.renderInitDataList(items, inputFieldModel, defaultId);
    }

    private void showDropdown(List<T> items, InputFieldModel inputFieldModel, String defaultId) {
        digitalWidgetDropdownInputView.setVisibility(VISIBLE);
        digitalWidgetDropdownInputView.setActionListener(new DigitalWidgetDropdownInputView.ActionListener() {
            @Override
            public void onClickDropdown(InputFieldModel inputFieldModel, String selectedItemId) {
                actionListener.onClickDropdown(inputFieldModel, selectedItemId);
            }

            @Override
            public void onItemSelected(Object item) {
                actionListener.onItemSelected(item);
            }
        });
        digitalWidgetDropdownInputView.renderDropdownView(items, inputFieldModel, defaultId);
    }

    private void showClientNumber(ClientNumber clientNumber, List<T> items) {
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
            public void onOperatorNotFound() {
                actionListener.onOperatorNotFound();
            }
        });
    }

    private ClientNumber transformInputFieldToClientNumber(InputFieldModel inputFieldModel) {
        return new ClientNumber(inputFieldModel.getName(), inputFieldModel.getType(), inputFieldModel.getText(),
                inputFieldModel.getPlaceholder(), inputFieldModel.getDefault(), inputFieldModel.getValidation());
    }

}
