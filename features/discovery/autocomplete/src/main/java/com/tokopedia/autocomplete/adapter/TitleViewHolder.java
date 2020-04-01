package com.tokopedia.autocomplete.adapter;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.viewmodel.TitleSearch;

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