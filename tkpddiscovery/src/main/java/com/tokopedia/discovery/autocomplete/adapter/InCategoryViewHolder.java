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
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.InCategorySearch;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.util.AutoCompleteTracking;

import java.util.HashMap;
import java.util.Locale;

public class InCategoryViewHolder extends AbstractViewHolder<InCategorySearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_in_category_auto_complete;

    private final TextView titleTextView;
    private final TextView subTitleTextView;
    private final ItemClickListener listener;
    private final ImageView iconCopyTextView;
    private final String tabName;

    public InCategoryViewHolder(View itemView, ItemClickListener clickListener, String tabName) {
        super(itemView);
        this.listener = clickListener;
        this.tabName = tabName;
        titleTextView = itemView.findViewById(R.id.titleTextView);
        subTitleTextView = itemView.findViewById(R.id.subTitleTextView);
        iconCopyTextView = itemView.findViewById(R.id.icon);
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
                AutoCompleteTracking.eventClickInCategory(
                        itemView.getContext(),
                        String.format(
                                "keyword: %s - value: %s - cat: %s - cat id: %s - po: %s - applink: %s",
                                element.getSearchTerm(),
                                element.getKeyword(),
                                element.getRecom(),
                                element.getCategoryId(),
                                String.valueOf(getAdapterPosition() + 1),
                                element.getApplink()
                        ),
                        tabName
                );
                listener.onItemSearchClicked(element.getApplink());
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

    private boolean getAutoCompleteItemIsOfficial(BaseItemAutoCompleteSearch autoCompleteSearch) {
        boolean isOfficial = false;

        HashMap<String, String> applinkParameterHashMap = autoCompleteSearch.getApplinkParameterHashmap();

        if(applinkParameterHashMap.containsKey(SearchApiConst.OFFICIAL)) {
            isOfficial = Boolean.parseBoolean(applinkParameterHashMap.get(SearchApiConst.OFFICIAL));
        }

        return isOfficial;
    }
}
