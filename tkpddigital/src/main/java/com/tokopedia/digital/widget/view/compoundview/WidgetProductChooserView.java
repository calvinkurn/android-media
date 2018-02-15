package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.view.adapter.WidgetNominalAdapter;
import com.tokopedia.digital.widget.view.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.view.model.product.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 7/18/17.
 */
@Deprecated
public class WidgetProductChooserView extends LinearLayout {

    private final static int OUT_OF_STOCK = 3;

    @BindView(R2.id.nominal_textview)
    TextView nominalTextview;
    @BindView(R2.id.spinner_nominal)
    Spinner spinnerNominal;
    @BindView(R2.id.error_nominal)
    TextView errorNominal;

    private ProductChoserListener listener;

    public WidgetProductChooserView(Context context) {
        super(context);
        init();
    }

    public WidgetProductChooserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetProductChooserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(ProductChoserListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_widget_spinner_product, this);
        ButterKnife.bind(this);
    }

    public void setTitleProduct(String titleProduct) {
        if (titleProduct != null && titleProduct.length() > 0)
            nominalTextview.setText(titleProduct);
    }

    public void setVisibilityProduct(boolean isShowProduct) {
        if (!isShowProduct) {
            nominalTextview.setVisibility(GONE);
            spinnerNominal.setVisibility(GONE);
        } else {
            nominalTextview.setVisibility(VISIBLE);
            spinnerNominal.setVisibility(VISIBLE);
        }
    }

    public void renderDataView(List<Product> products, boolean showPrice, LastOrder lastOrder,
                               String lastProductSelected) {
        WidgetNominalAdapter adapter = new WidgetNominalAdapter(getContext(),
                android.R.layout.simple_spinner_item, products, showPrice);
        spinnerNominal.setAdapter(adapter);
        spinnerNominal.setOnItemSelectedListener(getItemSelected(products));
        setSpnNominalSelectionBasedStatus(products);
        setSpnNominalSelectionBasedLastOrder(products, lastOrder, lastProductSelected);
        checkStockProduct(products.get(spinnerNominal.getSelectedItemPosition()));
    }

    private AdapterView.OnItemSelectedListener getItemSelected(final List<Product> products) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.initDataView(products.get(i));
                listener.trackingProduct();
                checkStockProduct(products.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    public boolean checkStockProduct(Product product) {
        boolean isAvailable = product.getAttributes().getStatus() != OUT_OF_STOCK;
        if (isAvailable) {
            errorNominal.setVisibility(View.GONE);
        } else {
            errorNominal.setVisibility(View.VISIBLE);
            errorNominal.setText(R.string.title_empty_stock);
        }
        return isAvailable;
    }

    private void setSpnNominalSelectionBasedStatus(List<Product> productList) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getAttributes().getStatus() != OUT_OF_STOCK) {
                spinnerNominal.setSelection(i);
                break;
            }
        }
    }

    private void setSpnNominalSelectionBasedLastOrder(List<Product> productList, LastOrder lastOrder,
                                                      String lastProductSelected) {
        if (SessionHandler.isV4Login(getContext())
                && lastOrder != null && lastOrder.getAttributes() != null) {
            int lastProductId = lastOrder.getAttributes().getProductId();
            for (int i = 0; i < productList.size(); i++) {
                if (Integer.valueOf(productList.get(i).getId()) == (lastProductId)) {
                    spinnerNominal.setSelection(i);
                }
            }
        } else {
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getId()
                        .equalsIgnoreCase(lastProductSelected)) {
                    spinnerNominal.setSelection(i);
                }
            }
        }
    }

    public interface ProductChoserListener {
        void initDataView(Product selectedProduct);

        void trackingProduct();
    }
}