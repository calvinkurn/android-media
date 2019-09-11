package com.tokopedia.contactus.home.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeepgoyal on 11/04/18.
 */

public class PurchaseListAdpater extends RecyclerView.Adapter<ContactUsPurchaseViewHolder> {
    List<BuyerPurchaseList> buyerPurchaseLists= new ArrayList<>();
    Context context;
    String type;

    public PurchaseListAdpater(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public ContactUsPurchaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactUsPurchaseViewHolder(LayoutInflater.from(context).inflate(R.layout.order_vertical_list_item, parent, false), type);
    }

    @Override
    public void onBindViewHolder(ContactUsPurchaseViewHolder holder, int position) {
        holder.bind(buyerPurchaseLists.get(position));
    }

    @Override
    public int getItemCount() {
        return buyerPurchaseLists.size();
    }

    public void setBuyerPurchaseLists(List<BuyerPurchaseList> buyerPurchaseLists) {
        this.buyerPurchaseLists = buyerPurchaseLists;
        notifyDataSetChanged();
    }
}
