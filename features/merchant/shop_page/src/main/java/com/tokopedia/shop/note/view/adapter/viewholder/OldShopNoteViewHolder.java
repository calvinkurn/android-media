package com.tokopedia.shop.note.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class OldShopNoteViewHolder extends AbstractViewHolder<ShopNoteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_info_note;

    private LabelView shopNoteLabelView;

    public OldShopNoteViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        shopNoteLabelView = view.findViewById(R.id.label_view);
    }

    @Override
    public void bind(ShopNoteViewModel element) {
        shopNoteLabelView.setTitle(element.getTitle());
        shopNoteLabelView.setSubTitle(element.getLastUpdate());
    }
}