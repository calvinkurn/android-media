package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.widget.view.adapter.NewWidgetNominalAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Rizky on 31/01/18.
 */

public class NewWidgetProductChooserView extends BaseDigitalChooserView<Product> {

    private final static int OUT_OF_STOCK = 3;

    @BindView(R2.id.nominal_textview)
    TextView nominalTextview;
    @BindView(R2.id.spinner_nominal)
    Spinner spinnerNominal;
    @BindView(R2.id.error_nominal)
    TextView errorNominal;

    public NewWidgetProductChooserView(Context context) {
        super(context);
    }

    public NewWidgetProductChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewWidgetProductChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void renderInitDataList(List<Product> products, boolean showPrice, String defaultProductId) {
        this.dataList = products;
        NewWidgetNominalAdapter adapter = new NewWidgetNominalAdapter(getContext(),
                android.R.layout.simple_spinner_item, products, showPrice);
        spinnerNominal.setAdapter(adapter);
        spinnerNominal.setOnItemSelectedListener(getItemSelected());
        setSpnNominalSelectionBasedLastOrder(defaultProductId);
        showProductStatus(products.get(spinnerNominal.getSelectedItemPosition()));
    }

    private AdapterView.OnItemSelectedListener getItemSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                actionListener.onUpdateDataDigitalChooserSelectedRendered(dataList.get(i));
                actionListener.tracking();
                showProductStatus(dataList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void setSpnNominalSelectionBasedLastOrder(String defaultProductId) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getProductId().equals(defaultProductId)) {
                spinnerNominal.setSelection(i, true);
                break;
            }
        }
    }

    public void showProductStatus(Product product) {
        boolean isAvailable = product.getStatus() != OUT_OF_STOCK;
        if (isAvailable) {
            errorNominal.setVisibility(View.GONE);
        } else {
            errorNominal.setVisibility(View.VISIBLE);
            errorNominal.setText(R.string.title_empty_stock);
        }
    }

    @Override
    protected void initialViewListener() {

    }

    @Override
    protected int getHolderLayoutId() {
        return R.layout.view_widget_spinner_product;
    }

    @Override
    public void setLabelText(String labelText) {
        if (labelText != null && labelText.length() > 0)
            nominalTextview.setText(labelText);
    }

    @Override
    public void enableError(String errorMessage) {

    }

    @Override
    public void disableError() {

    }

    @Override
    public void renderUpdateDataSelected(Product data) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getProductId().equals(data.getProductId())) {
                spinnerNominal.setSelection(i, true);
                break;
            }
        }
    }

}
