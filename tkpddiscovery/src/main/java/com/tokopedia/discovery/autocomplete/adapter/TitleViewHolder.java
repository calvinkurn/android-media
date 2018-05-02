package com.tokopedia.discovery.autocomplete.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.TitleSearch;

public class TitleViewHolder extends AbstractViewHolder<TitleSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_title_auto_complete;

    private final TextView titleTextView;

    public TitleViewHolder(View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.titleTextView);
    }

    @Override
    public void bind(TitleSearch element) {
        titleTextView.setText(element.getTitle());
    }
}
