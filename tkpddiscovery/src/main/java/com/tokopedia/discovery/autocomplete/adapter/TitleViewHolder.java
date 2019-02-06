package com.tokopedia.discovery.autocomplete.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.TitleSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

public class TitleViewHolder extends AbstractViewHolder<TitleSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_title_auto_complete;

    private final TextView titleTextView;
    private View btnDelete;

    public TitleViewHolder(View itemView, final ItemClickListener clickListener) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.titleTextView);
        btnDelete = itemView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onDeleteAllRecentSearch();
            }
        });
    }

    @Override
    public void bind(TitleSearch element) {
        titleTextView.setText(element.getTitle());
        btnDelete.setVisibility(element.isVisible() ? View.VISIBLE : View.GONE);
    }
}
