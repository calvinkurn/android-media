package com.tokopedia.tkpd.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.product.customview.BaseView;
import com.tokopedia.tkpd.rescenter.edit.customadapter.ProductAdapter;
import com.tokopedia.tkpd.rescenter.edit.listener.BuyerEditResCenterListener;
import com.tokopedia.tkpd.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.tkpd.rescenter.edit.model.responsedata.PassProductTrouble;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created on 8/24/16.
 */
public class EditProductTroubleView extends BaseView<EditResCenterFormData, BuyerEditResCenterListener> {

    @Bind(R.id.product_recyclerview)
    RecyclerView productRecyclerView;

    private ProductAdapter adapter;

    public EditProductTroubleView(Context context) {
        super(context);
    }

    public EditProductTroubleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(BuyerEditResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_rescenter_choose_product;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull EditResCenterFormData data) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProductAdapter(data);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(adapter);
    }

    public List<PassProductTrouble> getProductTrouble() {
        List<PassProductTrouble> passProductTroubleList = new ArrayList<>();

        if (getVisibility() == GONE) {
            return passProductTroubleList;
        }

        for (int i = 0; i < adapter.getItemCount(); i++) {
            PassProductTrouble passProductTrouble = new PassProductTrouble();
            ProductAdapter.SimpleViewHolder holder = (ProductAdapter.SimpleViewHolder) productRecyclerView.getChildViewHolder(productRecyclerView.getChildAt(i));
            if (holder.checkView.isChecked()) {
                passProductTrouble.setProductData(adapter.getAllProduct().get(i));
                passProductTroubleList.add(passProductTrouble);
            }
        }
        return passProductTroubleList;
    }
}
