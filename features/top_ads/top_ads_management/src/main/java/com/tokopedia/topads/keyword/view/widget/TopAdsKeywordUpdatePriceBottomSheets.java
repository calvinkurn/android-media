package com.tokopedia.topads.keyword.view.widget;

import android.app.Dialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.topads.R;

/**
 * Created by hadi.putra on 24/05/18.
 */

public class TopAdsKeywordUpdatePriceBottomSheets extends BottomSheets {
    private OnSubmitClicked onSubmitClicked;

    public void setOnSubmitClicked(OnSubmitClicked onSubmitClicked) {
        this.onSubmitClicked = onSubmitClicked;
    }

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.topads_widget_bottomsheet;
    }

    @Override
    protected String title() {
        return getString(R.string.label_change_price);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.partial_top_ads_keyword_edit_price;
    }

    @Override
    public void initView(View view) {
        final PrefixEditText topAdsCostPerClick = view.findViewById(R.id.edit_text_top_ads_cost_per_click);
        TextView topAdsMaxPriceInstruction = view.findViewById(R.id.text_view_top_ads_max_price_description);
        final TextInputLayout textInputLayoutCostPerClick = view.findViewById(R.id.text_input_layout_top_ads_cost_per_click);
        final ButtonCompat btnSave = (ButtonCompat) view.findViewById(R.id.btn_save);

        CurrencyIdrTextWatcher textWatcher = new CurrencyIdrTextWatcher(topAdsCostPerClick){
            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                String errorMessage =
                        com.tokopedia.topads.dashboard.utils.ViewUtils.getKeywordClickBudgetError(getActivity(), number);
                if (!TextUtils.isEmpty(errorMessage)) {
                    textInputLayoutCostPerClick.setError(errorMessage);
                    btnSave.setEnabled(false);
                } else {
                    textInputLayoutCostPerClick.setError(null);
                    btnSave.setEnabled(true);
                }
            }
        };
        topAdsCostPerClick.addTextChangedListener(textWatcher);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = topAdsCostPerClick.getTextWithoutPrefix();
                if (!TextUtils.isEmpty(price) && onSubmitClicked != null){
                    dismiss();
                    onSubmitClicked.submitPrice(price);
                }
            }
        });
    }

    @Override
    public BottomSheetBehavior getBottomSheetBehavior() {
        return super.getBottomSheetBehavior();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        updateHeight(screenHeight/2);

        ImageButton btnClose = getDialog().findViewById(com.tokopedia.design.R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public interface OnSubmitClicked {
        void submitPrice(String price);
    }
}
