package com.tokopedia.posapp.product.management.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.product.common.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementAdapterTypeFactory;

public class ProductManagementViewHolder extends AbstractViewHolder<ProductViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_manage_product;

    private ImageView imageThumbnail;
    private TextView textProductName;
    private EditText editOnlinePrice;
    private EditText editOutletPrice;
    private ImageButton buttonEdit;
    private SwitchCompat switchStatus;
    private ProductManagementAdapterTypeFactory.Listener listener;

    public ProductManagementViewHolder(View view, ProductManagementAdapterTypeFactory.Listener listener) {
        super(view);
        imageThumbnail = view.findViewById(R.id.image_thumbnail);
        textProductName = view.findViewById(R.id.text_product_name);
        editOnlinePrice = view.findViewById(R.id.edit_online_price);
        editOutletPrice = view.findViewById(R.id.edit_outlet_price);
        buttonEdit = view.findViewById(R.id.button_edit);
        switchStatus = view.findViewById(R.id.switch_status);
        this.listener = listener;
    }

    @Override
    public void bind(final ProductViewModel element) {
        textProductName.setText(element.getName());
        ImageHandler.loadImage(itemView.getContext(), imageThumbnail, element.getImageUrl(), com.tokopedia.abstraction.R.drawable.loading_page);
        editOnlinePrice.setText(Double.toString(element.getOnlinePrice()));
        editOutletPrice.setText(Double.toString(element.getOutletPrice()));

        switchStatus.setOnCheckedChangeListener(null);
        switchStatus.setChecked(element.isShown());
        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onShowProductCheckedChange(element, isChecked);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickEditProduct(v, element);
            }
        });

    }
}