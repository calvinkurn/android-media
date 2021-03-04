package com.tokopedia.attachproduct.view.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.attachproduct.R;
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;


/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductListItemViewHolder extends BaseCheckableViewHolder<AttachProductItemUiModel>
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_product_attach;
    private ImageView imageView;
    private TextView nameTextView;
    private CheckBox checkBox;
    private TextView priceTextView;

    private CheckableInteractionListenerWithPreCheckedAction checkableInteractionListener;
    private View itemView;
    public AttachProductListItemViewHolder(View itemView, CheckableInteractionListener checkableInteractionListener) {
        super(itemView,checkableInteractionListener);
        findAndAssignAllFields(itemView);
        checkBox.setClickable(false);
        itemView.setOnClickListener(this);
        this.itemView = itemView;
        this.checkableInteractionListener = (CheckableInteractionListenerWithPreCheckedAction)checkableInteractionListener;
    }

    private void findAndAssignAllFields(View itemView){
        this.imageView = itemView.findViewById(R.id.attach_product_item_image);
        this.nameTextView = itemView.findViewById(R.id.attach_product_item_name);
        this.checkBox = itemView.findViewById(R.id.attach_product_item_checkbox);
        this.priceTextView = itemView.findViewById(R.id.attach_product_item_price);
    }

    @Override
    public CompoundButton getCheckable() {
        return checkBox;
    }

    @Override
    public void bind(AttachProductItemUiModel element) {
        ImageHandler.loadImageRounded2(imageView.getContext(), imageView,element.getProductImage());
        nameTextView.setText(element.getProductName());
        priceTextView.setText(element.getProductPrice());
    }

    @Override
    public void setChecked(boolean checked) {
        if(checkableInteractionListener.shouldAllowCheckChange(getAdapterPosition(),checked)) {
            itemView.setSelected(checked);
            super.setChecked(checked);
        }
    }

    @Override
    public void onClick(View view) {
        toggle();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        super.onCheckedChanged(buttonView, isChecked);
    }
}
