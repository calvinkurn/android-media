package com.tokopedia.shop.settings.notes.view.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.touchhelper.OnStartDragListener;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.common.util.ShopDateUtil;
import com.tokopedia.shop.settings.common.util.SpanTextUtil;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopNoteReorderViewHolder extends AbstractViewHolder<ShopNoteViewModel> {

    public static final int LAYOUT = R.layout.item_shop_note_reorder;

    private TextView tvNoteName;
    private TextView tvLastUpdate;

    private OnStartDragListener onStartDragListener;

    public ShopNoteReorderViewHolder(View itemView,
                                     OnStartDragListener onStartDragListener) {
        super(itemView);
        this.onStartDragListener = onStartDragListener;
        tvNoteName = itemView.findViewById(R.id.tvNoteName);
        tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate);
    }

    @Override
    public void bind(ShopNoteViewModel shopNoteViewModel) {
        tvNoteName.setText(shopNoteViewModel.getTitle());
        tvLastUpdate.setText(ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DATE_TIME, shopNoteViewModel.getUpdateTime()));

        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(ShopNoteReorderViewHolder.this);
                }
                return false;
            }
        });
    }

}
