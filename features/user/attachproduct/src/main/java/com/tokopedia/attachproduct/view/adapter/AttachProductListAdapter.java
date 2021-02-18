package com.tokopedia.attachproduct.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hendri on 15/02/18.
 */

public class AttachProductListAdapter extends BaseListAdapter<AttachProductItemUiModel,AttachProductListAdapterTypeFactory>
{
    private HashSet<String> productIds;
    private ArrayList<AttachProductItemUiModel> checkedList;

    public AttachProductListAdapter(AttachProductListAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
        productIds = new HashSet<>();
        checkedList = new ArrayList<>();
    }

    public AttachProductListAdapter(AttachProductListAdapterTypeFactory baseListAdapterTypeFactory,
                                    HashSet<String> productIds, ArrayList<AttachProductItemUiModel> checkedList) {
        super(baseListAdapterTypeFactory);
        this.productIds = productIds;
        this.checkedList = checkedList;
    }

    @Override
    public List<AttachProductItemUiModel> getData() {
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

    private AttachProductItemUiModel getDataRow(int position){
        if(position < 0 || position >= visitables.size()) {
            return null;
        }
        else {
            AttachProductItemUiModel item = (AttachProductItemUiModel) visitables.get(position);
            return item;
        }
    }

    public void itemChecked(boolean isChecked,int position){
        AttachProductItemUiModel product = getDataRow(position);
        if(product != null) {
            String productId = product.getProductId();
            if (isChecked) {
                productIds.add(productId);
                addToCheckedDataList(product);
            } else {
                productIds.remove(productId);
                removeFromCheckedDataList(productId);
            }
        }
    }

    private void addToCheckedDataList(AttachProductItemUiModel productItemViewModel){
        checkedList.add(productItemViewModel);
    }

    private void removeFromCheckedDataList(String productId){
        Iterator<AttachProductItemUiModel> iterator = checkedList.iterator();
        while(iterator.hasNext()){
            AttachProductItemUiModel itemViewModel = iterator.next();
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
        String productId = "0";
        AttachProductItemUiModel attachProductItemUiModel = getDataRow(position);
        if(attachProductItemUiModel != null) {
            productId = attachProductItemUiModel.getProductId();
        }
        return productIds.contains(productId);
    }


    public List<AttachProductItemUiModel> getCheckedDataList(){
        return checkedList;
    }
}
