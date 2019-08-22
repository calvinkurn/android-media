package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.hotel.R;

public class HotelEmptyViewHolder extends EmptyViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_hotel_room_empty_list;

    public HotelEmptyViewHolder(View itemView, Callback callback) {
        super(itemView, callback);
    }

    @Override
    protected void findView(View itemView) {
        emptyTitleTextView = itemView.findViewById(R.id.text_view_empty_title_text);
        emptyContentTextView = itemView.findViewById(R.id.text_view_empty_content_text);
        emptyContentItemTextView = itemView.findViewById(R.id.text_view_empty_content_item_text);
        emptyButtonItemButton = itemView.findViewById(R.id.button_add_promo);
        emptyIconImageView = itemView.findViewById(R.id.no_result_image);
    }

}
