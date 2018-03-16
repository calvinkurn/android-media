package com.tokopedia.posapp.product.management.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.product.management.di.component.DaggerEditProductComponent;
import com.tokopedia.posapp.product.management.di.component.EditProductComponent;
import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.seller.common.widget.PrefixEditText;

import javax.inject.Inject;

/**
 * @author okasurya on 3/14/18.
 */

public class EditProductDialogFragment extends DialogFragment implements EditProduct.View {
    public static final String TAG = EditProductDialogFragment.class.getSimpleName();
    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";

    @Inject
    EditProduct.Presenter presenter;

    private TextView textProductName;
    private TextInputLayout layoutOnlinePrice;
    private PrefixEditText editOnlinePrice;
    private TextInputLayout layoutOutletPrice;
    private PrefixEditText editOutletPrice;
    private Button buttonCancel;
    private Button buttonSave;

    public static void show(FragmentManager fragmentManager, ProductViewModel productViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_VIEW_MODEL, productViewModel);

        EditProductDialogFragment editProductDialogFragment = new EditProductDialogFragment();
        editProductDialogFragment.setArguments(bundle);

        editProductDialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            return new AlertDialog
                    .Builder(getContext())
                    .setView(getDialogView(
                        (ProductViewModel) getArguments().getParcelable(PRODUCT_VIEW_MODEL)
                    ))
                    .create();
        }

        return super.onCreateDialog(savedInstanceState);
    }

    private void initInjector() {
        EditProductComponent component = DaggerEditProductComponent
                .builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
        presenter.attachView(this);
    }

    private View getDialogView(final ProductViewModel product) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_product, null);

        textProductName = view.findViewById(R.id.text_product_name);
        layoutOnlinePrice = view.findViewById(R.id.layout_online_price);
        editOnlinePrice = view.findViewById(R.id.edit_online_price);
        layoutOutletPrice = view.findViewById(R.id.layout_outlet_price);
        editOutletPrice = view.findViewById(R.id.edit_outlet_price);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonSave = view.findViewById(R.id.button_save);

        textProductName.setText(product.getName());
        editOnlinePrice.setText(getFormattedCurrency(product.getOnlinePrice()));
        editOutletPrice.setText(getFormattedCurrency(product.getOutletPrice()));
//        editOutletPrice.addTextChangedListener(new CurrencyTextWatcher(editOutletPrice, CurrencyEnum.RP));
//        editOnlinePrice.addTextChangedListener(new CurrencyTextWatcher(editOnlinePrice, CurrencyEnum.RP));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save(product, editOutletPrice.getText().toString());
            }
        });

        return view;
    }

    private String getFormattedCurrency(double price) {
        return CurrencyFormatUtil.getThousandSeparatorString(
                price, false, 0
        ).getFormattedString();
    }

    @Override
    public void onSuccessSave() {
        dismiss();
    }

    @Override
    public void onErrorSave(Throwable e) {

    }
}
