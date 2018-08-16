package com.tokopedia.shop.settings.notes.view.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopNoteViewHolder extends AbstractViewHolder<ShopNoteViewModel> {

    public static final int LAYOUT = R.layout.item_shop_note;

    private View ivMenuMore;
    private TextView tvNoteName;
    private TextView tvLastUpdate;

    public ShopNoteViewHolder(View itemView,
                              OnShopNoteViewHolderListener onOnShopNoteViewHolderListener) {
        super(itemView);
        this.onOnShopNoteViewHolderListener = onOnShopNoteViewHolderListener;
        ivMenuMore = itemView.findViewById(R.id.ivMenuMore);
        tvNoteName = itemView.findViewById(R.id.tvNoteName);
        tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate);
    }

    private OnShopNoteViewHolderListener onOnShopNoteViewHolderListener;
    public interface OnShopNoteViewHolderListener {
        void onIconMoreClicked(ShopNoteViewModel shopNoteViewModel);
        String getKeyword();
    }

    @Override
    public void bind(ShopNoteViewModel shopNoteViewModel) {
        if (onOnShopNoteViewHolderListener!= null) {
            String keyword = onOnShopNoteViewHolderListener.getKeyword();
            if (!TextUtils.isEmpty(keyword)) {

            }
        }
        tvNoteName.setText(shopNoteViewModel.getTitle());
        tvLastUpdate.setText(shopNoteViewModel.getUpdateTime());
        ivMenuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOnShopNoteViewHolderListener!= null) {
                    onOnShopNoteViewHolderListener.onIconMoreClicked(shopNoteViewModel);
                }
            }
        });
    }

}
