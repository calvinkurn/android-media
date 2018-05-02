package com.tokopedia.discovery.autocomplete.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.DigitalSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

public class DigitalViewHolder extends AbstractViewHolder<DigitalSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_digital_auto_complete;

    private final TextView titleTextView;
    private final ImageView iconImageView;
    private final ItemClickListener listener;

    public DigitalViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.listener = clickListener;
        titleTextView = itemView.findViewById(R.id.titleTextView);
        iconImageView = itemView.findViewById(R.id.iconImageView);
    }

    @Override
    public void bind(DigitalSearch element) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        titleTextView.setText(element.getRecom());
        ImageHandler.loadImageThumbs(itemView.getContext(), iconImageView, element.getImageUrl());
    }
}
