package com.tokopedia.product.manage.item.variant.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.common.util.CurrencyUsdTextWatcher;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.utils.ProductPriceRangeUtils;

/**
 * @author hendry on 4/20/17.
 */
public class ProductChangeVariantPriceDialogFragment extends DialogFragment {

    public static final String TAG = ProductChangeVariantPriceDialogFragment.class.getSimpleName();

    public static final String KEY_CURRENCY_TYPE = "curr_type";
    public static final String KEY_CURRENCY_VALUE = "curr_value";
    public static final String IS_OFFICIAL_STORE = "is_os";
    public static final String IS_GOLD_MERCHANT = "is_gm";

    private OnDismissListener onDismissListener;
    private OnProductChangeVariantPriceFragmentListener onProductChangeVariantPriceFragmentListener;

    private SpinnerCounterInputView counterInputPrice;

    private @CurrencyTypeDef int currencyType = CurrencyTypeDef.TYPE_IDR;
    private boolean isOfficialStore;
    private boolean isGoldMerchant;

    private NumberTextWatcher numberTextWatcher;

    public interface OnProductChangeVariantPriceFragmentListener {
        void onChangeAllPriceVariantSubmit(@CurrencyTypeDef int currencyType, double currencyValue);
    }

    public static ProductChangeVariantPriceDialogFragment newInstance(@CurrencyTypeDef int currencyType,
                                                                      boolean isGoldMerchant,
                                                                      double prevCurrencyValue,
                                                                      boolean isOfficialStore) {
        ProductChangeVariantPriceDialogFragment addWholeSaleDialog = new ProductChangeVariantPriceDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(KEY_CURRENCY_VALUE, prevCurrencyValue);
        bundle.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        bundle.putInt(KEY_CURRENCY_TYPE, currencyType);
        bundle.putBoolean(IS_OFFICIAL_STORE, isOfficialStore);
        addWholeSaleDialog.setArguments(bundle);
        return addWholeSaleDialog;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        isOfficialStore = bundle.getBoolean(IS_OFFICIAL_STORE, false);
        isGoldMerchant = bundle.getBoolean(IS_GOLD_MERCHANT, false);
        if (savedInstanceState == null) {
            currencyType = bundle.getInt(KEY_CURRENCY_TYPE);
        } else {
            currencyType = savedInstanceState.getInt(KEY_CURRENCY_TYPE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_price_variant, container, false);

        counterInputPrice = view.findViewById(R.id.spinner_counter_input_view_price);
        if (!isGoldMerchant) {
            counterInputPrice.getSpinnerTextView().setEnabled(false);
            counterInputPrice.getSpinnerTextView().setClickable(false);
        } else {
            counterInputPrice.getSpinnerTextView().setEnabled(true);
            counterInputPrice.getSpinnerTextView().setClickable(true);
            counterInputPrice.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
                @Override
                public void onItemChanged(int position, String entry, String value) {
                    setTextWatcher(value);
                }

                private void setTextWatcher(String spinnerValue) {
                    counterInputPrice.removeTextChangedListener(numberTextWatcher);
                    if (spinnerValue.equalsIgnoreCase(
                            counterInputPrice.getContext().getString(R.string.product_currency_value_idr))) {
                        currencyType = CurrencyTypeDef.TYPE_IDR;
                    } else {
                        currencyType = CurrencyTypeDef.TYPE_USD;
                    }
                    determineTextWatcher();
                    counterInputPrice.addTextChangedListener(numberTextWatcher);
                }
            });
        }
        counterInputPrice.setSpinnerValue(String.valueOf(currencyType));

        counterInputPrice.removeTextChangedListener(numberTextWatcher);
        determineTextWatcher();
        counterInputPrice.addTextChangedListener(numberTextWatcher);

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            counterInputPrice.setCounterValue(bundle.getDouble(KEY_CURRENCY_VALUE));
            counterInputPrice.getCounterEditText().setSelection(counterInputPrice.getCounterEditText().getText().length());
        }

        view.findViewById(R.id.string_picker_dialog_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });
        view.findViewById(R.id.string_picker_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void determineTextWatcher() {
        if (currencyType == CurrencyTypeDef.TYPE_USD) {
            numberTextWatcher = new CurrencyUsdTextWatcher(counterInputPrice.getCounterEditText(), "0.00") {
                @Override
                public void onNumberChanged(double number) {
                    onNumberPriceChanged();
                }
            };
        } else {
            numberTextWatcher = new CurrencyIdrTextWatcher(counterInputPrice.getCounterEditText(), "0") {
                @Override
                public void onNumberChanged(double number) {
                    onNumberPriceChanged();
                }
            };
        }
    }

    private void onSubmitClicked() {
        if (!checkPriceValid(counterInputPrice.getCounterValue())) {
            CommonUtils.hideKeyboard(getActivity(), getView());
            return;
        }
        if(onProductChangeVariantPriceFragmentListener!=null){
            onProductChangeVariantPriceFragmentListener.onChangeAllPriceVariantSubmit(currencyType, counterInputPrice.getCounterValue());
        }
        this.dismiss();
    }

    private void onNumberPriceChanged() {
        double counterValue = counterInputPrice.getCounterValue();
        checkPriceValid(counterValue);
    }

    private boolean checkPriceValid(double value) {
        if (ProductPriceRangeUtils.isPriceValid(value, currencyType, isOfficialStore)) {
            counterInputPrice.setCounterError(null);
            return true;
        }
        counterInputPrice.setCounterError(getContext().getString(R.string.product_error_product_price_not_valid,
                ProductPriceRangeUtils.getMinPriceString(currencyType, isOfficialStore),
                ProductPriceRangeUtils.getMaxPriceString(currencyType, isOfficialStore)));
        return false;
    }

    public void attachListener(OnProductChangeVariantPriceFragmentListener OnProductChangeVariantPriceFragmentListener) {
        onProductChangeVariantPriceFragmentListener = OnProductChangeVariantPriceFragmentListener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENCY_TYPE, currencyType);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
            onDismissListener = null;
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

}
