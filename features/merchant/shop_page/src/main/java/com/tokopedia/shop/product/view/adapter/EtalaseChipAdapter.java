package com.tokopedia.shop.product.view.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.shop.R;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class EtalaseChipAdapter extends RecyclerView.Adapter<EtalaseChipAdapter.ShopProductEtalaseChipViewHolder> {

    private List<ShopEtalaseViewModel> etalaseViewModelList;
    private String selectedEtalaseId;

    private OnEtalaseChipAdapterListener onEtalaseChipAdapterListener;

    public interface OnEtalaseChipAdapterListener {
        void onEtalaseChipClicked(ShopEtalaseViewModel shopEtalaseViewModel);
    }

    public EtalaseChipAdapter(List<ShopEtalaseViewModel> shopEtalaseViewModelList, String selectedEtalaseId,
                              OnEtalaseChipAdapterListener onEtalaseChipAdapterListener) {
        setEtalaseViewModelList(shopEtalaseViewModelList);
        setSelectedEtalaseId(selectedEtalaseId);
        this.onEtalaseChipAdapterListener = onEtalaseChipAdapterListener;
    }

    public void setEtalaseViewModelList(List<ShopEtalaseViewModel> etalaseViewModelList) {
        if (etalaseViewModelList == null) {
            this.etalaseViewModelList = new ArrayList<>();
        } else {
            this.etalaseViewModelList = etalaseViewModelList;
        }
    }

    public void setSelectedEtalaseId(String selectedEtalaseId) {
        if (selectedEtalaseId == null) {
            this.selectedEtalaseId = "";
        } else {
            this.selectedEtalaseId = selectedEtalaseId;
        }
    }

    public List<ShopEtalaseViewModel> getEtalaseViewModelList() {
        return etalaseViewModelList;
    }

    public int getSelectedPosition() {
        for (int i = 0, sizei = etalaseViewModelList.size(); i < sizei; i++) {
            ShopEtalaseViewModel shopEtalaseViewModel = etalaseViewModelList.get(i);
            if (shopEtalaseViewModel.getEtalaseId().equalsIgnoreCase(selectedEtalaseId)) {
                return i;
            }
        }
        return 0;
    }

    class ShopProductEtalaseChipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;

        public ShopProductEtalaseChipViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // fix java.lang.ArrayIndexOutOfBoundsException when click parallelly
            if (position < 0 || position >= etalaseViewModelList.size()) {
                return;
            }
            String clickedEtalaseId = etalaseViewModelList.get(position).getEtalaseId();
            if (!clickedEtalaseId.equalsIgnoreCase(selectedEtalaseId)) {
                selectedEtalaseId = clickedEtalaseId;
                notifyDataSetChanged();
                if (onEtalaseChipAdapterListener != null) {
                    onEtalaseChipAdapterListener.onEtalaseChipClicked(etalaseViewModelList.get(position));
                }
            }
        }
    }


    @Override
    public ShopProductEtalaseChipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop_product_etalase_chip, parent, false);
        return new ShopProductEtalaseChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopProductEtalaseChipViewHolder holder, int position) {
        ShopEtalaseViewModel shopEtalaseViewModel = etalaseViewModelList.get(position);
        holder.textView.setText(MethodChecker.fromHtml(shopEtalaseViewModel.getEtalaseName()));
        if (TextUtils.isEmpty(selectedEtalaseId)) {
            if (position == 0) {
                holder.textView.setSelected(true);
            } else {
                holder.textView.setSelected(false);
            }
        } else if (selectedEtalaseId.equalsIgnoreCase(shopEtalaseViewModel.getEtalaseId())) {
            holder.textView.setSelected(true);
        } else {
            holder.textView.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return etalaseViewModelList.size();
    }

}
