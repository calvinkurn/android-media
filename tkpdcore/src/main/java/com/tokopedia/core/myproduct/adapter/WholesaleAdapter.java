package com.tokopedia.core.myproduct.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core2.R;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.WholesalePriceDB;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;
import com.tokopedia.core.myproduct.utils.VerificationUtils;
import com.tokopedia.core.util.Pair;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by m.normansyah on 10/12/2015.
 */
public class WholesaleAdapter extends RecyclerView.Adapter<WholesaleAdapter.ViewHolder>{

    String currencyUnit;
    String price;
    ArrayList<WholeSaleAdapterModel> datas;
    SparseArray<ViewHolderListener> listeners;

    public static final int QTY1 = 0;
    public static final int QTY2 = 1;
    public static final int PRICE = 2;
    public static final String TAG = WholesaleAdapter.class.getSimpleName();
    public static final String MESSAGE_TAG = TAG + " : ";

    public WholesaleAdapter(ArrayList<WholeSaleAdapterModel> wholeSaleAdapterModels){
        datas = new ArrayList<>(wholeSaleAdapterModels);
        listeners = new SparseArray<>();

        int count = 0;
        for (WholeSaleAdapterModel wholeSaleAdapterModel :
                datas) {
            long l = DbManagerImpl.getInstance().addHargaGrosir(wholeSaleAdapterModel);
            wholeSaleAdapterModel.setbDid(l);
            datas.set(count, wholeSaleAdapterModel);
            Log.d(TAG, MESSAGE_TAG+ " "+ l +" save : "+wholeSaleAdapterModel+" count : "+count);
            count++;
        }
    }

    public WholesaleAdapter(){
        datas = new ArrayList<>();
        listeners = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.wholesale_item_layout, parent, false
        );
        return new ViewHolder(root);
    }

    public void clearAll(){
        for(WholeSaleAdapterModel data : datas){
            DbManagerImpl.getInstance().removeHargaGrosir(data);
        }
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listeners.put(position,holder.getInterface());
        if(datas.size() > 1 && position > 0) {
            holder.setDataBefore(datas.get(position - 1));
        }
        holder.bindView(datas.get(position), currencyUnit);
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Deprecated
    public void add(String... data){
    }

    public void add(WholeSaleAdapterModel data){
        if(datas.size()>=5)
            return;

        if(datas.size() != 0){
            if(!listeners.get(datas.size() - 1).checkValue())
                return;
        }

        long id = DbManagerImpl.getInstance().addHargaGrosir(0,0,0);
        data.setbDid(id);
        datas.add(data);
        notifyItemInserted(datas.size() - 1);
    }

    public List<WholeSaleAdapterModel> getDatas(){
        return datas;
    }

    public static List<String> toWholeSaleAdapterModel(final String... data){
        if(data != null && data.length == 3){
            return new ArrayList<String>(){{add(data[QTY1]); add(data[QTY2]); add(data[PRICE]);}};
        }
        throw new RuntimeException(WholesaleAdapter.class.getSimpleName()+" please pass 3 String, int, int , int in String format ");
    }

    public boolean isNoError() {
        boolean isNoError = true;
        for (WholeSaleAdapterModel model : datas){
            isNoError &= model.isNoErrorQOne();
            isNoError &= model.isNoErrorQTwo();
            isNoError &= model.isNoErrorPrice();
        }
        return isNoError;
    }

    public interface ViewHolderListener{
        boolean checkValue();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ViewHolderListener{
        private String currency;
        EditText wholesaleItemQtyOne;
        EditText wholesaleItemQtyTwo;
        EditText wholesaleItemQtyPrice;
        boolean isFristEditOne = true;
        boolean isFristEditTwo = true;
        boolean isFristEditPrice = true;
        ImageView wholesaleItemQtyRemove;
        WholeSaleAdapterModel data;
        WholeSaleAdapterModel dataBefore;

        public void setDataBefore(WholeSaleAdapterModel dataBefore) {
            this.dataBefore = dataBefore;
        }

        DefaultTextWatcher qtyOneTextWatcher = new DefaultTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFirstText(s);
            }
        };

        private boolean checkFirstText(CharSequence s) {
            if ((getCurrencyUnit() != null || getPrice() != null)) {
                if(isFristEditOne || isFristEditTwo){
                    setQtyOne(wholesaleItemQtyOne.getText().toString(), true);
                }else{
                    Pair<Boolean, String> res = VerificationUtils.validateWholeSaleItemQuantity(
                            WholesaleAdapter.QTY1,
                            itemView.getContext(),
                            wholesaleItemQtyTwo.getText().toString(),
                            s.toString(), dataBefore);
                    if (!res.getModel1()) {
                        wholesaleItemQtyOne.setError(res.getModel2());
                        setQtyOne(wholesaleItemQtyOne.getText().toString(), false);
                        return false;
                    } else {
                        wholesaleItemQtyOne.setError(null);
                        if (data != null) {
                            setQtyOne(wholesaleItemQtyOne.getText().toString(), true);
                        }
                    }
                    Log.d(TAG, MESSAGE_TAG + "wholesaleItemQtyOne" + res);
                }
                isFristEditOne = false;
                return true;
            }
            return false;
        }

        private void setQtyOne(String value, boolean noError) {
            data.setNoErrorQOne(noError);
            if(noError){
                if (value != null && !value.isEmpty()) {
                    Log.d(TAG, MESSAGE_TAG + " [before] success edit for qty one : " + data);
                    data.setQuantityOne(Double.parseDouble(value));
                    Log.d(TAG, MESSAGE_TAG + " [after] success edit for qty one : " + data);
                    notifyChanged();
                }
            }
        }

        DefaultTextWatcher qtyPrice = new DefaultTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formatPrice();
                String price = wholesaleItemQtyPrice.getText().toString().replace(",", "");
                if(!currency.equals("US$")) {
                    // remove cent
                    price = price.replace(".", "");
                }

                checkPrice(price);
            }
        };

        private boolean checkPrice(CharSequence s) {
            if ((getCurrencyUnit() != null || getPrice() != null)) {
                if(isFristEditPrice){
                    setPrice(s.toString().replace(",", ""), true);
                }else{
                    Pair<Boolean, String> res = VerificationUtils.validateWholeSalePrice(
                            itemView.getContext(), getCurrencyUnit(), getPrice(),
                            s.toString(), dataBefore);
                    if (!res.getModel1()) {
                        wholesaleItemQtyPrice.setError(res.getModel2());
                        setPrice(null, false);
                        return false;
                    } else {
                        wholesaleItemQtyPrice.setError(null);
                        if (data != null) {
                            setPrice(s.toString().replace(",", ""), true);
                        }
                    }
                    Log.d(TAG, MESSAGE_TAG + "wholesaleItemQtyPrice" + res);
                }
                isFristEditPrice = false;
                return true;
            }
            return false;
        }

        private void formatPrice() {
            switch(currency){
                case "Rp":
                    CurrencyFormatHelper.SetToRupiah(wholesaleItemQtyPrice);
                    break;
                case "US$":
                    CurrencyFormatHelper.SetToDollar(wholesaleItemQtyPrice);
                    break;
            }
        }

        private void setPrice(String price, boolean isNoError) {
            data.setNoErrorPrice(isNoError);
            if(isNoError) {
                if (!wholesaleItemQtyPrice.getText().toString().isEmpty()) {
                    try {
                        Log.d(TAG, MESSAGE_TAG + " [before] success edit for qty price : " + data);
                        data.setWholeSalePrice(Double.parseDouble(price));
                        Log.d(TAG, MESSAGE_TAG + " [after] success edit for qty price : " + data);
                        notifyChanged();
                    } catch (NumberFormatException e) {
                        wholesaleItemQtyPrice.setError("Mohon masukan nominal angka dengan benar");
                    }
                }
            }
        }

        DefaultTextWatcher qtyTwoTextWatcher = new DefaultTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkSecondText(s);
            }
        };

        private boolean checkSecondText(CharSequence s) {
            if ((getCurrencyUnit() != null || getPrice() != null) ) { //
                if(isFristEditTwo || isFristEditOne){
                    setQtyTwo(wholesaleItemQtyTwo.getText().toString(), true);
                }else{
                    Pair<Boolean, String> res = VerificationUtils.validateWholeSaleItemQuantity(
                            WholesaleAdapter.QTY2,
                            itemView.getContext(),
                            wholesaleItemQtyOne.getText().toString(),
                            s.toString(), dataBefore);
                    if (!res.getModel1()) {
                        wholesaleItemQtyTwo.setError(res.getModel2());
                        setQtyTwo(null, false);
                        return false;
                    } else {
                        wholesaleItemQtyTwo.setError(null);
                        if (data != null) {
                            setQtyTwo(wholesaleItemQtyTwo.getText().toString(), true);
                        }
                    }
                    Log.d(TAG, MESSAGE_TAG + "wholesaleItemQtyTwo" + res);
                }
                isFristEditTwo = false;
                return true;
            }
            return false;
        }

        private void setQtyTwo(String value, boolean noError) {
            data.setNoErrorQTwo(noError);
            if(noError){
                if (value != null && !value.isEmpty()) {
                    Log.d(TAG, MESSAGE_TAG + " [before] success edit for qty two : " + data);
                    data.setQuantityTwo(Double.parseDouble(value));
                    Log.d(TAG, MESSAGE_TAG + " [after] success edit for qty two : " + data);
                    notifyChanged();
                }
            }
        }

        public ViewHolder(final View itemView) {
            super(itemView);
            wholesaleItemQtyOne = (EditText)itemView.findViewById(R.id.wholesale_item_qty_one);
            wholesaleItemQtyTwo= (EditText)itemView.findViewById(R.id.wholesale_item_qty_two);
            wholesaleItemQtyPrice= (EditText)itemView.findViewById(R.id.wholesale_item_qty_price);
            wholesaleItemQtyRemove=(ImageView)itemView.findViewById(R.id.wholesale_item_qty_remove);

//            wholesaleItemQtyOne.addTextChangedListener(qtyOneTextWatcher);
//            wholesaleItemQtyTwo.addTextChangedListener(qtyTwoTextWatcher);
//            wholesaleItemQtyPrice.addTextChangedListener(qtyPrice);
            wholesaleItemQtyRemove.setOnClickListener(this);
        }

        private void notifyChanged() {
            datas.set(getAdapterPosition(), data);
            //[START] Cannot call this in recyclerview.holder
//            WholesaleAdapter.this.notifyDataSetChanged();
            //[END] Cannot call this in recyclerview.holder
        }

        public void bindView(WholeSaleAdapterModel data, String currencyUnit){
            this.data = data;
            isFristEditOne = true;
            isFristEditTwo = true;
            isFristEditPrice = true;
            wholesaleItemQtyOne.setText((int)data.getQuantityOne()+"");
            wholesaleItemQtyTwo.setText((int)data.getQuantityTwo()+"");
            wholesaleItemQtyPrice.setText(String.format("%.0f",data.getWholeSalePrice()));

            wholesaleItemQtyOne.addTextChangedListener(qtyOneTextWatcher);
            wholesaleItemQtyTwo.addTextChangedListener(qtyTwoTextWatcher);
            wholesaleItemQtyPrice.addTextChangedListener(qtyPrice);
//            wholesaleItemQtyRemove.setOnClickListener(this);
            currency = currencyUnit;
            formatPrice();
        }


        @Override
        public void onClick(View v) {
            WholeSaleAdapterModel temp = datas.remove(getAdapterPosition());
            List<WholesalePriceDB> wholesalePriceDBs =
                    DbManagerImpl.getInstance().removeWholeSaleDb(temp.getbDid());
            Log.e(TAG, MESSAGE_TAG+"remove harga grosir : "+ wholesalePriceDBs);
            WholesaleAdapter.this.notifyItemRemoved(getAdapterPosition());
        }

        public ViewHolderListener getInterface() {
            return this;
        }

        @Override
        public boolean checkValue() {
            boolean res = true;
            // THIS IS NEED TO BE CALLED TWICE EACH BECAUSE OF FIRST CHECKING ISSUE
            res &= checkFirstText(wholesaleItemQtyOne.getText().toString());
            res &= checkFirstText(wholesaleItemQtyOne.getText().toString());
            res &= checkSecondText(wholesaleItemQtyTwo.getText().toString());
            res &= checkSecondText(wholesaleItemQtyTwo.getText().toString());
            res &= checkPrice(wholesaleItemQtyPrice.getText().toString());
            res &= checkPrice(wholesaleItemQtyPrice.getText().toString());
            return res;
        }
    }

    /**
     * this is just to shorten
     */
    private class DefaultTextWatcher implements TextWatcher{
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {Log.d(TAG, MESSAGE_TAG+s.toString()+" start : "+start+" count : "+count+" after "+after);}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { Log.d(TAG, MESSAGE_TAG+s.toString()); } }
}
