package com.tokopedia.posapp.product.management.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.base.util.RupiahNumberTextWatcher;
import com.tokopedia.posapp.product.management.di.component.DaggerProductManagementComponent;
import com.tokopedia.posapp.product.management.di.component.ProductManagementComponent;
import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.listener.EditProductListener;
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
    private PrefixEditText editOnlinePrice;
    private PrefixEditText editOutletPrice;
    private Button buttonCancel;
    private Button buttonSave;
    private RelativeLayout editProductContainer;
    private ProgressBar progressBar;
    private EditProductListener editProductListener;

    public static void show(FragmentManager fragmentManager, ProductViewModel productViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_VIEW_MODEL, productViewModel);

        EditProductDialogFragment editProductDialogFragment = new EditProductDialogFragment();
        editProductDialogFragment.setArguments(bundle);

        editProductDialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onFragmentAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onFragmentAttach(context);
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

    @Override
    public void showLoading() {
        if (progressBar != null) {
            editProductContainer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            this.setCancelable(false);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            editProductContainer.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            this.setCancelable(true);
        }
    }

    @Override
    public void onSuccessSave() {
        Toast.makeText(getContext(), R.string.editproduct_message_success, Toast.LENGTH_SHORT).show();
        editProductListener.onDialogDismiss();
        dismiss();
    }

    @Override
    public void onErrorSave(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void initInjector() {
        ProductManagementComponent component = DaggerProductManagementComponent
                .builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
        presenter.attachView(this);
    }

    private View getDialogView(final ProductViewModel product) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_product, null);

        editProductContainer = view.findViewById(R.id.container_edit_product);
        textProductName = view.findViewById(R.id.text_product_name);
        editOnlinePrice = view.findViewById(R.id.edit_online_price);
        editOutletPrice = view.findViewById(R.id.edit_outlet_price);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonSave = view.findViewById(R.id.button_save);
        progressBar = view.findViewById(R.id.progressBar);

        editOutletPrice.addTextChangedListener(new RupiahNumberTextWatcher(editOutletPrice));

        textProductName.setText(product.getName());
        editOnlinePrice.setText(product.getOnlinePrice());
        editOutletPrice.setText(String.valueOf((int) product.getOutletPriceUnformatted()));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save(product, editOutletPrice.getTextWithoutPrefix());
            }
        });

        return view;
    }

    private void onFragmentAttach(Context context) {
        if (context instanceof EditProductListener) {
            this.editProductListener = (EditProductListener) context;
        } else {
            throw new RuntimeException("Activity needs to implement EditProductListener");
        }
    }
}
