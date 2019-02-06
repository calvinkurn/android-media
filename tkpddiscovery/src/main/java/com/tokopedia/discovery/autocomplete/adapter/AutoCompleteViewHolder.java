package com.tokopedia.discovery.autocomplete.adapter;

import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.AutoCompleteSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.util.AutoCompleteTracking;

import java.util.Locale;

public class AutoCompleteViewHolder extends AbstractViewHolder<AutoCompleteSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_default_auto_complete;
    private final String tabName;

    private TextView titleTextView;
    private ImageView iconCopyTextView;

    private final ItemClickListener listener;

    public AutoCompleteViewHolder(View itemView, ItemClickListener clickListener, String tabName) {
        super(itemView);
        this.listener = clickListener;
        this.tabName = tabName;
        titleTextView = itemView.findViewById(R.id.titleTextView);
        iconCopyTextView = itemView.findViewById(R.id.icon);
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
                AutoCompleteTracking.eventClickAutoCompleteSearch(
                        itemView.getContext(),
                        String.format(
                                "keyword: %s - value: %s - po: %s - applink: %s",
                                element.getKeyword(),
                                element.getSearchTerm(),
                                String.valueOf(getAdapterPosition() + 1),
                                element.getApplink()
                        ),
                        tabName
                );
                listener.onItemSearchClicked(
                        element.getKeyword(),
                        element.getCategoryId()
                );
            }
        });

        iconCopyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.copyTextToSearchView(element.getKeyword());
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
