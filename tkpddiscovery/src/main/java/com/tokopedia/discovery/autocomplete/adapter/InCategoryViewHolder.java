package com.tokopedia.discovery.autocomplete.adapter;

import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.InCategorySearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

import java.util.Locale;

public class InCategoryViewHolder extends AbstractViewHolder<InCategorySearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_in_category_auto_complete;

    private final TextView titleTextView;
    private final TextView subTitleTextView;
    private final ItemClickListener listener;

    public InCategoryViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.listener = clickListener;
        titleTextView = itemView.findViewById(R.id.titleTextView);
        subTitleTextView = itemView.findViewById(R.id.subTitleTextView);
    }

    @Override
    public void bind(final InCategorySearch element) {
        int startIndex = indexOfSearchQuery(element.getKeyword(), element.getSearchTerm());
        if (startIndex == -1) {
            titleTextView.setText(element.getKeyword().toLowerCase());
        } else {
            SpannableString highlightedTitle = new SpannableString(element.getKeyword());
            highlightedTitle.setSpan(new TextAppearanceSpan(itemView.getContext(), R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlightedTitle.setSpan(new TextAppearanceSpan(itemView.getContext(), R.style.searchTextHiglight),
                    startIndex + element.getSearchTerm().length(),
                    element.getKeyword().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleTextView.setText(highlightedTitle);
        }

        subTitleTextView.setText(
                String.format(itemView.getContext().getString(R.string.formated_in_category), element.getRecom())
        );

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickAutoCompleteCategory(
                        element.getRecom(),
                        element.getCategoryId(),
                        element.getKeyword()
                );
                listener.onItemClicked(element.getApplink(), element.getUrl());
            }
        });

    }

    private int indexOfSearchQuery(String displayName, String searchTerm) {
        if (!TextUtils.isEmpty(searchTerm)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }
}
