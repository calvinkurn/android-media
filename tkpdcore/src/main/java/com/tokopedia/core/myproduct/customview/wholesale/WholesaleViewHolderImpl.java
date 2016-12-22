package com.tokopedia.core.myproduct.customview.wholesale;

import android.renderscript.Double2;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.utils.CurrencyFormatter;
import com.tokopedia.core.myproduct.utils.PriceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.tokopedia.core.myproduct.customview.wholesale.WholesaleAdapterImpl.QTY_ONE;
import static com.tokopedia.core.myproduct.customview.wholesale.WholesaleAdapterImpl.QTY_TWO;
import static com.tokopedia.core.myproduct.customview.wholesale.WholesaleAdapterImpl.QTY_PRICE;

/**
 * Created by sebastianuskh on 12/2/16.
 */
public class WholesaleViewHolderImpl extends RecyclerView.ViewHolder implements WholesaleViewHolder {

    private static final String TAG = "Wholesale View Holder";
    private int position;
    private WholesaleAdapter listener;
    private final int currency;

    @BindView(R2.id.wholesale_item_qty_one)
    EditText qtyOne;

    @BindView(R2.id.wholesale_item_qty_two)
    EditText qtyTwo;

    @BindView(R2.id.wholesale_item_qty_price)
    EditText qtyPrice;

    private boolean onPriceEdit = false;

    @OnTextChanged(value = R2.id.wholesale_item_qty_one, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void qtyOneChanged(Editable editable){
        listener.onUpdateData(QTY_ONE, position, String.valueOf(editable));
    }

    @OnTextChanged(value = R2.id.wholesale_item_qty_two, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void qtyTwoChanged(Editable editable){
        listener.onUpdateData(QTY_TWO, position, String.valueOf(editable));
    }

    @OnTextChanged(value = R2.id.wholesale_item_qty_price, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void qtyPriceChanged(Editable editable){
        if(onPriceEdit)
            return;
        onPriceEdit = true;
        String rawString = "";
        switch (currency) {
            case PriceUtils.CURRENCY_RUPIAH:
                CurrencyFormatHelper.SetToRupiah(qtyPrice);
                rawString = CurrencyFormatter.getRawString(editable.toString());
                break;
            case PriceUtils.CURRENCY_DOLLAR:
                CurrencyFormatHelper.SetToDollar(qtyPrice);
                rawString = CurrencyFormatter.getRawString(editable.toString());
                break;
        }
        listener.onUpdateData(QTY_PRICE, position, rawString);
        onPriceEdit = false;
    }

    @OnClick(R2.id.button_delete_wholesale)
    void deleteWholesale(){
        listener.removeWholesaleItem(position);
    }

    public WholesaleViewHolderImpl(View itemView, int currency) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.currency = currency;
    }

    @Override
    public void bindView(WholesaleAdapter listener, int position, WholesaleModel wholesaleModel) {
        this.listener = listener;
        this.position = position;
        if(wholesaleModel != null) {
            this.qtyOne.setText(String.format("%d", wholesaleModel.getQtyOne()));
            this.qtyTwo.setText(String.format("%d", wholesaleModel.getQtyTwo()));
            this.qtyPrice.setText(String.format("%.00f", wholesaleModel.getQtyPrice()));
        }
    }

    @Override
    public void onQtyOneError(String error) {
        qtyOne.setError(error);
    }

    @Override
    public void onQtyTwoError(String error) {
        qtyTwo.setError(error);
    }

    @Override
    public void onQtyPriceError(String error) {
        qtyPrice.setError(error);
    }

    @Override
    public CharSequence getQtyOneError() {
        return qtyOne.getError();
    }

    @Override
    public CharSequence getQtyTwoError() {
        return qtyTwo.getError();
    }

    @Override
    public CharSequence getQtyPriceError() {
        return qtyPrice.getError();
    }
}
