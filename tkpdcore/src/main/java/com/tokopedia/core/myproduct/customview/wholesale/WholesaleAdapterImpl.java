package com.tokopedia.core.myproduct.customview.wholesale;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R;
import com.tokopedia.core.myproduct.utils.PriceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 12/2/16.
 */
public class WholesaleAdapterImpl extends RecyclerView.Adapter<WholesaleViewHolderImpl> implements WholesaleAdapter {

    private static final int MAX_WHOLESALE_PRICE = 5;
    private static final String TAG = "WholesaleAdapter";
    public static final int QTY_ONE = 1;
    public static final int QTY_TWO = 2;
    public static final int QTY_PRICE = 3;

    private List<WholesaleModel> data;
    private final WholesaleAdapterListener listener;
    private double mainPrice;
    private int currency;

    public WholesaleAdapterImpl(WholesaleAdapterListener listener, double mainPrice, int currency){
        this.data = new ArrayList<>();
        this.listener = listener;
        this.mainPrice = mainPrice;
        this.currency = currency;
    }

    @Override
    public WholesaleViewHolderImpl onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_wholesale, parent, false);
        return new WholesaleViewHolderImpl(view, currency);
    }

    @Override
    public void onBindViewHolder(WholesaleViewHolderImpl holder, int position) {
        data.get(position).setViewHolder(holder);
        holder.bindView(this, position, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public boolean addItem(){
        if(getItemCount() == MAX_WHOLESALE_PRICE){
            listener.throwShomething("Jumlah maksimum harga grosir sudah tercapai");
            return false;
        }
        checkError();
        if(!checkIfErrorExist()){
            data.add(new WholesaleModel());
            notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public void onUpdateData(int type, int position, String value, boolean isShouldCheckError) {

        WholesaleModel model = data.get(position);
        if(value.equals("")) value = "0";

        switch (type){
            case QTY_ONE :
                model.setQtyOne(Integer.valueOf(value));
                break;
            case QTY_TWO :
                model.setQtyTwo(Integer.valueOf(value));
                break;
            case QTY_PRICE :
                model.setQtyPrice(Double.valueOf(value));
                break;
        }

        Log.d(TAG, "print data position : " + position + "\n Datas : " + data.get(position).toString());

        if(isShouldCheckError) {
            checkError();
        }
    }

    private void checkError() {

        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getViewHolder() != null) {

                if (data.get(i).getQtyOne() >= data.get(i).getQtyTwo()) {
                    data.get(i).getViewHolder().onQtyTwoError("Jumlah barang harus lebih besar dari jumlah barang pertama");
                } else {
                    data.get(i).getViewHolder().onQtyTwoError(null);
                }

                double comparingPrice;
                int qtyBefore = 0;
                if (i == 0) {
                    comparingPrice = mainPrice;
                } else {
                    comparingPrice = data.get(i - 1).getQtyPrice();
                    qtyBefore = data.get(i - 1).getQtyTwo();
                }
                Pair<Boolean, String> pair = PriceUtils.validatePrice(currency, data.get(i).getQtyPrice());
                if (!pair.first) {
                    data.get(i).getViewHolder().onQtyPriceError(pair.second);
                } else if (data.get(i).getQtyPrice() >= comparingPrice) {
                    data.get(i).getViewHolder().onQtyPriceError("Harga harus lebih rendah dari harga sebelumnya");
                } else {
                    data.get(i).getViewHolder().onQtyPriceError(null);
                }

                if (data.get(i).getQtyOne() <= qtyBefore) {
                    data.get(i).getViewHolder().onQtyOneError("Jumlah barang harus lebih besar dari jumlah barang sebelumnya");
                } else {
                    data.get(i).getViewHolder().onQtyOneError(null);
                }
            }

        }

    }

    @Override
    public void removeAllWholesaleItem() {
        for(int i = 0; i < data.size(); i ++){
            removeWholesaleItem(i);
        }
    }

    @Override
    public void removeWholesaleItem(int position){
        /** remove the text watcher listener */
        data.get(position).getViewHolder().removeQtyOneTextWatcher();
        data.get(position).getViewHolder().removeQtyTwoTextWatcher();
        data.get(position).getViewHolder().removeQtyPriceTextWatcher();

        data.remove(position);
        notifyDataSetChanged();

    }

    @Override
    public boolean checkIfErrorExist() {
        checkError();
        boolean bool = false;
        for(WholesaleModel model : data){
            if(model.getViewHolder().getQtyOneError() != null){
                bool = true;
                break;
            }
            if(model.getViewHolder().getQtyTwoError() != null){
                bool = true;
                break;
            }
            if(model.getViewHolder().getQtyPriceError() != null){
                bool = true;
                break;
            }
        }
        return bool;
    }

    @Override
    public List<WholesaleModel> getDatas() {
        return data;
    }

    @Override
    public void setData(List<WholesaleModel> datas) {
        data = datas;
        notifyDataSetChanged();
    }

    public void setMainPrice(double mainPrice) {
        this.mainPrice = mainPrice;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }


    public interface WholesaleAdapterListener{
        void throwShomething(String error);
    }

}
