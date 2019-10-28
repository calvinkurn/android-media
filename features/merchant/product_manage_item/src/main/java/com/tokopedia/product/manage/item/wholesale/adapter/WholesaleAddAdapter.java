package com.tokopedia.product.manage.item.wholesale.adapter;

import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.common.util.CurrencyUsdPrefixEdittextTextWatcher;
import com.tokopedia.product.manage.item.common.util.PrefixEditText;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.main.base.data.model.ProductWholesaleViewModel;
import com.tokopedia.product.manage.item.utils.ProductPriceRangeUtils;
import com.tokopedia.product.manage.item.wholesale.model.WholesaleModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.tokopedia.product.manage.item.common.util.CurrencyUsdPrefixEdittextTextWatcher.PREFIX_DOLLAR;
import static com.tokopedia.product.manage.item.common.util.CurrencyUsdPrefixEdittextTextWatcher.PREFIX_RUPIAH;

/**
 * Created by yoshua on 02/05/18.
 */

public class WholesaleAddAdapter extends RecyclerView.Adapter<WholesaleAddAdapter.ViewHolder> {

    public static final int MINIMUM_QTY = 1;
    private List<WholesaleModel> wholesaleModels;
    private Listener listener;
    private int currentPositionFocusPrice = 0;
    private int currentPositionFocusQty = -1;
    private double mainPrice;
    private boolean officialStore;

    public WholesaleAddAdapter(double mainPrice, boolean officialStore) {
        wholesaleModels = new CopyOnWriteArrayList<>();
        this.mainPrice = mainPrice;
        this.officialStore = officialStore;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public WholesaleAddAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.item_product_add_wholesale, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setEditTextValue(wholesaleModels.get(position));
        holder.isPriceValid(wholesaleModels.get(position), position);
        holder.isQtyValid(wholesaleModels.get(position), position);
        holder.initFocus(position);
        holder.setEditTextState(wholesaleModels.get(position));
        setButtonSaveState();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final PrefixEditText etRangeWholesale;
        private final TextInputLayout tilRangeWholesale;
        private final ImageView imageWholesale;
        private final PrefixEditText etWholeSalePrice;
        private final TextInputLayout tilWholeSalePrice;
        private final Locale dollarLocale = Locale.US;
        private NumberFormat formatter;

        public ViewHolder(View itemView) {
            super(itemView);

            if (listener == null) {
                throw new IllegalArgumentException("listener must be implemented !!");
            }

            formatter();

            etRangeWholesale = itemView.findViewById(R.id.et_range_whole_sale);
            tilRangeWholesale = itemView.findViewById(R.id.til_range_whole_sale);
            imageWholesale = itemView.findViewById(R.id.image_whole_sale);
            etWholeSalePrice = itemView.findViewById(R.id.et_whole_sale_price);
            tilWholeSalePrice = itemView.findViewById(R.id.til_whole_sale_price);

            etRangeWholesale.setPrefix(MethodChecker.fromHtml("&#8805; &emsp;").toString());

            TextWatcher textWatcher = new CurrencyIdrTextWatcher(etWholeSalePrice);
            switch (listener.getCurrencyType()) {
                case CurrencyTypeDef.TYPE_USD:
                    etWholeSalePrice.setPrefix(PREFIX_DOLLAR);
                    textWatcher = new CurrencyUsdPrefixEdittextTextWatcher(etWholeSalePrice) {
                        @Override
                        public void onNumberChanged(double number) {
                            if (wholesaleModels.get(getAdapterPosition()).isFocusPrice()) {
                                setValueAndRefresh(number);
                            }
                        }
                    };
                    break;
                case CurrencyTypeDef.TYPE_IDR:
                    etWholeSalePrice.setPrefix(PREFIX_RUPIAH);
                    textWatcher = new CurrencyIdrTextWatcher(etWholeSalePrice) {
                        @Override
                        public void onNumberChanged(double number) {
                            if (wholesaleModels.get(getAdapterPosition()).isFocusPrice()) {
                                setValueAndRefresh(number);
                            }
                        }
                    };
                    break;
            }
            etWholeSalePrice.addTextChangedListener(textWatcher);

            etWholeSalePrice.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    if(isWithinDataset(getAdapterPosition())){
                        setFocusFalse();
                        wholesaleModels.get(getAdapterPosition()).setFocusPrice(true);
                    }
                }
            });

            etRangeWholesale.addTextChangedListener(new NumberTextWatcher(etRangeWholesale){
                @Override
                public void onNumberChanged(double number) {
                    if(wholesaleModels.get(getAdapterPosition()).isFocusQty()){
                        wholesaleModels.get(getAdapterPosition()).setQtyMin((int)number);
                        wholesaleModels.get(getAdapterPosition()).setFocusQty(false);
                        currentPositionFocusQty = getAdapterPosition();
                        currentPositionFocusPrice = -1;
                        etRangeWholesale.post(() -> notifyDataSetChanged());
                    }
                }
            });

            etRangeWholesale.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    if(isWithinDataset(getAdapterPosition())){
                        setFocusFalse();
                        wholesaleModels.get(getAdapterPosition()).setFocusQty(true);
                    }
                }
            });

            imageWholesale.setOnClickListener(v -> removeItem(getAdapterPosition()));
        }

        private void setValueAndRefresh(double number){
            wholesaleModels.get(getAdapterPosition()).setQtyPrice(number);
            wholesaleModels.get(getAdapterPosition()).setFocusPrice(false);
            currentPositionFocusPrice = getAdapterPosition();
            currentPositionFocusQty = -1;
            isPriceValid(wholesaleModels.get(getAdapterPosition()), getAdapterPosition());
            isQtyValid(wholesaleModels.get(getAdapterPosition()), getAdapterPosition());
            initFocus(getAdapterPosition());
            setEditTextState(wholesaleModels.get(getAdapterPosition()));
            setButtonSaveState();
            for(int i = 0 ; i < wholesaleModels.size() ; i++){
                final int j = i;
                if(j!= getAdapterPosition()){
                    etWholeSalePrice.post(() -> notifyItemChanged(j));
                }
            }
        }

        private void initFocus(int position){
            if(currentPositionFocusPrice == position){
                wholesaleModels.get(position).setFocusPrice(true);
            }
            if(currentPositionFocusQty == position){
                wholesaleModels.get(position).setFocusQty(true);
            }

            if(wholesaleModels.get(position).isFocusPrice()){
                etWholeSalePrice.requestFocus();
            }
            if(wholesaleModels.get(position).isFocusQty()){
                etRangeWholesale.requestFocus();
            }
        }

        private void setEditTextValue(WholesaleModel model){
            NumberFormat quantityNumberFormat = new DecimalFormat();
            quantityNumberFormat.setMinimumIntegerDigits(0);
            etRangeWholesale.setText(formatValue((double)model.getQtyMin()));
            etWholeSalePrice.setText(formatValue(model.getQtyPrice()));
        }

        private void setEditTextState(WholesaleModel model){
            if(!TextUtils.isEmpty(model.getStatusPrice())){
                setErrorEditText(tilWholeSalePrice, model.getStatusPrice());
            } else {
                removeErrorEdittext(tilWholeSalePrice);
            }

            if(!TextUtils.isEmpty(model.getStatusQty())){
                setErrorEditText(tilRangeWholesale, model.getStatusQty());
            } else {
                removeErrorEdittext(tilRangeWholesale);
            }
        }

        private void removeErrorEdittext(TextInputLayout textInputLayout){
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }

        private void setErrorEditText(TextInputLayout textInputLayout, String message){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(message);
        }

        protected void formatter() {
            formatter = NumberFormat.getNumberInstance(dollarLocale);
            switch (listener.getCurrencyType()) {
                case CurrencyTypeDef.TYPE_USD:
                    formatter.setMinimumFractionDigits(2);
                    break;
                default:
                case CurrencyTypeDef.TYPE_IDR:
                    formatter.setMinimumFractionDigits(0);
                    break;
            }
        }

        private String formatValue(Double value) {
            return formatter.format(value);
        }

        private void isPriceValid(WholesaleModel model, int position) {
            if (!ProductPriceRangeUtils.isPriceValid(model.getQtyPrice(), listener.getCurrencyType(), officialStore)) {
                model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_error_product_price_not_valid,
                        ProductPriceRangeUtils.getMinPriceString(listener.getCurrencyType(), officialStore),
                        ProductPriceRangeUtils.getMaxPriceString(listener.getCurrencyType(), officialStore)));
                return;
            }
            if(position > 0){
                if(!getItem(position-1).getStatusPrice().equalsIgnoreCase("")){
                    model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_fix_previous_wholesale));
                    return;
                }
                if (model.getQtyPrice() >= getItem(position-1).getQtyPrice()) {
                    model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
                    return;
                }
            } else {
                if (model.getQtyPrice() >= mainPrice) {
                    model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_price_should_be_cheaper_than_main_price));
                    return;
                }
            }
            model.setStatusPrice("");
        }

        private void isQtyValid(WholesaleModel model, final int position) {
            if(position > 0){
                if(!getItem(position-1).getStatusQty().equalsIgnoreCase("")){
                    model.setStatusQty(tilRangeWholesale.getContext().getString(R.string.product_fix_previous_wholesale));
                    return;
                }
                if (model.getQtyMin() <= getItem(position-1).getQtyMin()) {
                    model.setStatusQty(tilRangeWholesale.getContext().getString(R.string.wholesale_qty_not_valid));
                    return;
                }
            }else if(position == 0){
                if (model.getQtyMin() <= MINIMUM_QTY) {
                    model.setStatusQty(tilRangeWholesale.getContext().getString(R.string.wholesale_qty_not_valid));
                    return;
                }
            }
            model.setStatusQty("");
        }
    }

    private void setFocusFalse(){
        for(int i = 0; i < getItemSize(); i++){
            wholesaleModels.get(i).setFocusQty(false);
            wholesaleModels.get(i).setFocusPrice(false);
        }
    }

    @Override
    public long getItemId(int position) {
        if (isWithinDataset(position)) {
            return position;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return (null != wholesaleModels ? wholesaleModels.size() : 0);
    }

    public int getItemSize() {
        return wholesaleModels.size();
    }

    public synchronized int addItem(WholesaleModel wholesaleModel) {
        wholesaleModels.add(wholesaleModel);
        notifyItemInserted(wholesaleModels.size() - 1);
        return wholesaleModels.size() - 1;
    }

    public synchronized void removeItem(int position) {
        if (isWithinDataset(position)) {
            setFocusFalse();
            currentPositionFocusPrice = 0;
            currentPositionFocusQty = -1;
            wholesaleModels.remove(position);
            notifyDataSetChanged();
        }
        listener.notifySizeChanged(wholesaleModels.size());
    }

    public synchronized void removeAll() {
        wholesaleModels.clear();
    }

    protected WholesaleModel getItem(int prevPosition) {
        if (isWithinDataset(prevPosition)) {
            return wholesaleModels.get(prevPosition);
        }
        return null;
    }

    private boolean isWithinDataset(int position) {
        return position >= 0 && position <= wholesaleModels.size() - 1;
    }

    public WholesaleModel getLastItem() {
        if (isWithinDataset(wholesaleModels.size() - 1)) {
            return getItem(wholesaleModels.size() - 1);
        }
        return null;
    }

    public void addAllWholeSalePrice(List<ProductWholesaleViewModel> wholesalePrice) {
        for (int i = 0; i < wholesalePrice.size(); i++) {
            WholesaleModel wholesaleModel = new WholesaleModel(
                    wholesalePrice.get(i).getMinQty(),
                    wholesalePrice.get(i).getPriceValue()
            );
            wholesaleModels.add(wholesaleModel);
        }
    }

    public synchronized ArrayList<ProductWholesaleViewModel> getProductWholesaleViewModels() {
        ArrayList<ProductWholesaleViewModel> productWholesaleViewModels =
                new ArrayList<>();
        for (int i = 0; i < wholesaleModels.size(); i++) {
            ProductWholesaleViewModel productWholesaleViewModel
                    = new ProductWholesaleViewModel();
            WholesaleModel wholesaleModel = wholesaleModels.get(i);
            productWholesaleViewModel.setMinQty(wholesaleModel.getQtyMin());
            productWholesaleViewModel.setPriceValue(wholesaleModel.getQtyPrice());

            productWholesaleViewModels.add(productWholesaleViewModel);
        }
        return productWholesaleViewModels;
    }

    private void setButtonSaveState() {
        for(int i = 0; i < getItemSize(); i++){
            if(!wholesaleModels.get(i).getStatusPrice().equalsIgnoreCase("") ||
                    !wholesaleModels.get(i).getStatusQty().equalsIgnoreCase("") )
            {
                listener.setButtonSubmit(false);
                return;
            }
        }
        listener.setButtonSubmit(true);
    }

    public interface Listener {

        void notifySizeChanged(int currentSize);

        void setButtonSubmit(boolean state);

        @CurrencyTypeDef
        int getCurrencyType();
    }
}
