package com.tokopedia.attachproduct.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hendri on 15/02/18.
 */

public class AttachProductListAdapter extends BaseListAdapter<AttachProductItemViewModel,AttachProductListAdapterTypeFactory>
{
    private HashSet<Integer> productIds;
    private ArrayList<AttachProductItemViewModel> checkedList;

    public AttachProductListAdapter(AttachProductListAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
        productIds = new HashSet<>();
        checkedList = new ArrayList<>();
    }

    public AttachProductListAdapter(AttachProductListAdapterTypeFactory baseListAdapterTypeFactory,
                                    HashSet<Integer> productIds, ArrayList<AttachProductItemViewModel> checkedList) {
        super(baseListAdapterTypeFactory);
        this.productIds = productIds;
        this.checkedList = checkedList;
    }

    @Override
    public List<AttachProductItemViewModel> getData() {
        return super.getData();
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(holder instanceof BaseCheckableViewHolder){
            ((BaseCheckableViewHolder) holder).getCheckable()
                    .setChecked(isChecked(position));
            ((BaseCheckableViewHolder) holder).itemView.setSelected(isChecked(position));
        }
    }

    private AttachProductItemViewModel getDataRow(int position){
        if(position < 0 && position >= visitables.size()) {
            return null;
        }
        else {
            AttachProductItemViewModel item = (AttachProductItemViewModel) visitables.get(position);
            return item;
        }
    }

    public void itemChecked(boolean isChecked,int position){
        AttachProductItemViewModel product = getDataRow(position);
        if(product != null) {
            int productId = product.getProductId();
            if (isChecked) {
                productIds.add(productId);
                addToCheckedDataList(product);
            } else {
                productIds.remove(productId);
                removeFromCheckedDataList(productId);
            }
        }
    }

    private void addToCheckedDataList(AttachProductItemViewModel productItemViewModel){
        checkedList.add(productItemViewModel);
    }

    private void removeFromCheckedDataList(int productId){
        Iterator<AttachProductItemViewModel> iterator = checkedList.iterator();
        while(iterator.hasNext()){
            AttachProductItemViewModel itemViewModel = iterator.next();
            if(itemViewModel.getProductId() == productId){
                iterator.remove();
                return;
            }
        }
    }

    public int getCheckedCount(){
        return productIds.size();
    }

    public boolean isChecked(int position){
        int productId = 0;
        AttachProductItemViewModel attachProductItemViewModel = getDataRow(position);
        if(attachProductItemViewModel != null) {
            productId = attachProductItemViewModel.getProductId();
        }
        return productIds.contains(productId);
    }


    public List<AttachProductItemViewModel> getCheckedDataList(){
        return checkedList;
    }
}
