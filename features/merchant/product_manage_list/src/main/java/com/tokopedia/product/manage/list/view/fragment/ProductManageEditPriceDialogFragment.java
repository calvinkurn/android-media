package com.tokopedia.product.manage.list.view.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.utils.ProductPriceRangeUtils;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.product.manage.item.common.util.CurrencyUsdTextWatcher;

import java.util.Locale;

/**
 * Created by zulfikarrahman on 10/3/17.
 */

public class ProductManageEditPriceDialogFragment extends DialogFragment {

    public static final String IS_OFFICIAL_STORE = "isOfficialStore";

    public interface ListenerDialogEditPrice {
        void onSubmitEditPrice(String productId, String price, String currencyId, String currencyText);
    }

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_CURRENCY_ID = "product_currency_id";
    public static final String IS_GOLD_MERCHANT = "isGoldMerchant";
    private CurrencyIdrTextWatcher idrTextWatcher;
    private CurrencyUsdTextWatcher usdTextWatcher;

    private String productId;
    private String productPrice;
    private int productCurrencyId;
    private boolean isGoldMerchant;
    private boolean isOfficialStore;
    private ListenerDialogEditPrice listenerDialogEditPrice;

    private CounterInputView counterInputView;
    private TextView saveButton;
    private TextView cancelButton;

    public static ProductManageEditPriceDialogFragment createInstance(final String productId, String productPrice,
                                                                      int productCurrencyId, boolean isGoldMerchant, boolean isOfficialStore) {
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        args.putString(PRODUCT_PRICE, productPrice);
        args.putInt(PRODUCT_CURRENCY_ID, productCurrencyId);
        args.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        args.putBoolean(IS_OFFICIAL_STORE, isOfficialStore);
        ProductManageEditPriceDialogFragment fragment = new ProductManageEditPriceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListenerDialogEditPrice(ListenerDialogEditPrice listenerDialogEditPrice) {
        this.listenerDialogEditPrice = listenerDialogEditPrice;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(PRODUCT_ID);
        productPrice = getArguments().getString(PRODUCT_PRICE);
        productCurrencyId = getArguments().getInt(PRODUCT_CURRENCY_ID);
        isGoldMerchant = getArguments().getBoolean(IS_GOLD_MERCHANT);
        isOfficialStore = getArguments().getBoolean(IS_OFFICIAL_STORE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_product_manage_edit_price, container, false);
        counterInputView = view.findViewById(R.id.counter_input_view);
        saveButton = (TextView) view.findViewById(R.id.string_picker_dialog_confirm);
        cancelButton = (TextView) view.findViewById(R.id.string_picker_dialog_cancel);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPriceValid() && listenerDialogEditPrice != null) {
                    UnifyTracking.eventProductManageOverflowMenu(getActivity(), getString(R.string.product_manage_menu_set_price) + " - " + saveButton.getText());
                    listenerDialogEditPrice.onSubmitEditPrice(productId,
                            formatDecimal(counterInputView.getDoubleValue()),
                            "1", "Rp");
                    dismiss();
                } else {
                    counterInputView.requestFocus();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                UnifyTracking.eventProductManageOverflowMenu(getActivity(), getString(R.string.product_manage_menu_set_price) + " - " + saveButton.getText());
            }
        });

        idrTextWatcher = new CurrencyIdrTextWatcher(counterInputView.getEditText()) {
            @Override
            public void onNumberChanged(double number) {
                isPriceValid();
            }
        };
        counterInputView.addTextChangedListener(idrTextWatcher);
        counterInputView.setValue(Double.valueOf(productPrice));
        return view;
    }

    private boolean isPriceValid() {
        double priceValue = getPriceValue();
        int currencyType = getCurrencyType();
        if (!ProductPriceRangeUtils.isPriceValid(priceValue, currencyType, isOfficialStore )) {
            counterInputView.setError(
                    counterInputView.getContext().getString(R.string.product_error_product_price_not_valid,
                            ProductPriceRangeUtils.getMinPriceString(currencyType, isOfficialStore),
                            ProductPriceRangeUtils.getMaxPriceString(currencyType, isOfficialStore)));
            return false;
        }
        counterInputView.setError(null);
        return true;
    }

    public double getPriceValue() {
        return counterInputView.getDoubleValue();
    }

    public int getCurrencyType() {
        return CurrencyTypeDef.TYPE_IDR;
    }

    private String formatDecimal(double productPrice) {
        if (productPrice == (long) productPrice)
            return String.format(Locale.US, "%d", (long) productPrice);
        else
            return String.format("%s", productPrice);
    }
}
