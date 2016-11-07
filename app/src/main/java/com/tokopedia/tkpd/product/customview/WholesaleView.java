package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpd.product.model.productdetail.ProductWholesalePrice;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */
public class WholesaleView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = WholesaleView.class.getSimpleName();

    @Bind({R2.id.wholesale_1, R2.id.wholesale_2, R2.id.wholesale_3, R2.id.wholesale_4, R2.id.wholesale_5})
    List<TableRow> rowWholesale;
    @Bind({R2.id.qty_ws_1, R2.id.qty_ws_2, R2.id.qty_ws_3, R2.id.qty_ws_4, R2.id.qty_ws_5})
    List<TextView> wholesaleQty;
    @Bind({R2.id.price_ws_1, R2.id.price_ws_2, R2.id.price_ws_3, R2.id.price_ws_4, R2.id.price_ws_5})
    List<TextView> wholesalePrice;

    public WholesaleView(Context context) {
        super(context);
    }

    public WholesaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_wholesale_price_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
        for (TableRow tableRow : rowWholesale) {
            tableRow.setVisibility(GONE);
        }
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        List<ProductWholesalePrice> wholesalePrices = data.getWholesalePrice();
        setVisibility(wholesalePrices.isEmpty() ? GONE : VISIBLE);

        int length = rowWholesale.size() <= wholesalePrices.size() ?
                rowWholesale.size() : wholesalePrices.size();
        for (int i = 0; i < length; i++) {
            if (i == wholesalePrices.size() - 1) {
                wholesaleQty.get(i).setText(String.format(">= %s", wholesalePrices.get(i).getWholesaleMin()));
            } else {
                wholesaleQty.get(i).setText(String.format("%s - %s", wholesalePrices.get(i).getWholesaleMin(),
                        data.getWholesalePrice().get(i).getWholesaleMax()));
            }
            wholesalePrice.get(i).setText(wholesalePrices.get(i).getWholesalePrice());
            rowWholesale.get(i).setVisibility(View.VISIBLE);
        }
    }
}
