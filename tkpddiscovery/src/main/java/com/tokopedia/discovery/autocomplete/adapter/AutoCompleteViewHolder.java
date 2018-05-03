package com.tokopedia.discovery.autocomplete.adapter;

import android.content.Context;
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
import com.tokopedia.discovery.autocomplete.viewmodel.AutoCompleteSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

import java.util.Locale;

public class AutoCompleteViewHolder extends AbstractViewHolder<AutoCompleteSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_default_auto_complete;
    private final Context context;

    private TextView titleTextView;

    private final ItemClickListener listener;

    public AutoCompleteViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = clickListener;
        titleTextView = itemView.findViewById(R.id.titleTextView);
    }

    @Override
    public void bind(final AutoCompleteSearch element) {
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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickAutoCompleteSearch(element.getKeyword());
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
