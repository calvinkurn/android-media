package com.tokopedia.shop.settings.etalase.view.viewholder;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.touchhelper.OnStartDragListener;
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopEtalaseReorderViewHolder extends AbstractViewHolder<ShopEtalaseViewModel> {

    public static final int LAYOUT = R.layout.item_shop_etalase_reorder;

    private TextView tvEtalaseName;
    private TextView tvEtalaseCount;
    private View handler;

    private OnStartDragListener onStartDragListener;

    public ShopEtalaseReorderViewHolder(View itemView,
                                        OnStartDragListener onStartDragListener) {
        super(itemView);
        this.onStartDragListener = onStartDragListener;
        tvEtalaseName = itemView.findViewById(R.id.tvEtalaseName);
        tvEtalaseCount = itemView.findViewById(R.id.tvEtalaseCount);
        handler = itemView.findViewById(R.id.ivReorder);
    }

    @Override
    public void bind(ShopEtalaseViewModel shopEtalaseViewModel) {
        tvEtalaseName.setText(shopEtalaseViewModel.getName());
        tvEtalaseCount.setText(tvEtalaseCount.getContext().getString(R.string.x_products, shopEtalaseViewModel.getCount()));
        if (shopEtalaseViewModel.getType() == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
            handler.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(ShopEtalaseReorderViewHolder.this);
                    }
                    return false;
                }
            });
            handler.setVisibility(View.VISIBLE);
        } else {
            handler.setVisibility(View.GONE);
        }
    }

}
