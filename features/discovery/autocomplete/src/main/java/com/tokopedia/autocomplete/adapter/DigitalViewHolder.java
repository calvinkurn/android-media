package com.tokopedia.autocomplete.adapter;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.analytics.AutocompleteTracking;
import com.tokopedia.autocomplete.viewmodel.DigitalSearch;

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
    public void bind(final DigitalSearch element) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutocompleteTracking.eventClickDigital(
                        itemView.getContext(),
                        String.format(
                                "keyword: %s - product: %s - po: %s - page: %s",
                                element.getSearchTerm(),
                                element.getKeyword(),
                                String.valueOf(getAdapterPosition() + 1),
                                element.getApplink()
                        )
                );
                listener.onItemClicked(element.getApplink(), element.getUrl());
            }
        });
        titleTextView.setText(element.getRecom());
        ImageHandler.loadImageThumbs(itemView.getContext(), iconImageView, element.getImageUrl());
    }
}
