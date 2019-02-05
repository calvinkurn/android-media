package com.tokopedia.logisticaddaddress.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.features.manage.ManagePeopleAddressActivityPresenter;

/**
 * Created on 5/25/16.
 */
public class BottomSheetFilterDialog {

    private final Context context;
    private final BottomSheetDialog dialog;
    private final Spinner spinnerSort;
    private final EditText searchBox;
    private final Button reset;
    private final Button submit;
    private ArrayAdapter<CharSequence> adapterSort;

    public BottomSheetFilterDialog(Context context) {
        this.context = context;
        this.dialog = new BottomSheetDialog(context);
        this.dialog.setContentView(R.layout.logistic_layout_filter_manage_people_address);
        spinnerSort = (Spinner) dialog.findViewById(R.id.sort);
        searchBox = (EditText) dialog.findViewById(R.id.search);
        submit = (Button) dialog.findViewById(R.id.submit);
        reset = (Button) dialog.findViewById(R.id.reset);
        initAdapter();
        setAdapter();
    }

    public static BottomSheetFilterDialog Builder(Context context) {
        return new BottomSheetFilterDialog(context);
    }

    private void initAdapter() {
        adapterSort =  ArrayAdapter.createFromResource(context, R.array.logistic_address_sort_type, R.layout.logistic_dialog_item);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setAdapter() {
        spinnerSort.setAdapter(adapterSort);
    }

    public BottomSheetFilterDialog setListener(final ManagePeopleAddressActivityPresenter presenter) {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                presenter.setOnSubmitFilterDialog(spinnerSort.getSelectedItemPosition(), String.valueOf(searchBox.getText()));
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFilterDialog();
            }
        });

        return this;
    }

    private void resetFilterDialog() {
        searchBox.setText("");
        spinnerSort.setSelection(0);
    }

    public BottomSheetFilterDialog setView() {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout frameLayout = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

}
