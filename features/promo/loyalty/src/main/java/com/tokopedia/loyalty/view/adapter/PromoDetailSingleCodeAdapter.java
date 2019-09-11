package com.tokopedia.loyalty.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.loyalty.view.data.SingleCodeViewModel;
import com.tokopedia.loyalty.view.viewholder.PromoDetailSingleCodeViewHolder;

import java.util.List;

import static com.tokopedia.loyalty.view.viewholder.PromoDetailSingleCodeViewHolder.ITEM_VIEW_SINGLE_CODE;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailSingleCodeAdapter extends RecyclerView.Adapter<PromoDetailSingleCodeViewHolder> {

    private PromoDetailAdapter.OnAdapterActionListener adapterActionListener;
    private List<SingleCodeViewModel> singleCodeList;

    @Override
    public PromoDetailSingleCodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(ITEM_VIEW_SINGLE_CODE, viewGroup, false);
        return new PromoDetailSingleCodeViewHolder(view, adapterActionListener);
    }

    @Override
    public void onBindViewHolder(PromoDetailSingleCodeViewHolder holder, int position) {
        holder.bind(this.singleCodeList.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return this.singleCodeList.size();
    }

    public PromoDetailSingleCodeAdapter(List<SingleCodeViewModel> singleCodeList) {
        this.singleCodeList = singleCodeList;
    }

    public void setAdapterActionListener(PromoDetailAdapter.OnAdapterActionListener adapterActionListener) {
        this.adapterActionListener = adapterActionListener;
    }

}
