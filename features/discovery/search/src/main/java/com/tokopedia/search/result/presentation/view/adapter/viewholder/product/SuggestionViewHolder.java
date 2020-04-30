package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;

public class SuggestionViewHolder extends AbstractViewHolder<SuggestionViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.suggestion_layout;
    private SuggestionListener suggestionListener;
    private TextView suggestionText;

    public SuggestionViewHolder(View itemView,
                                SuggestionListener suggestionListener) {
        super(itemView);
        this.suggestionListener = suggestionListener;
        suggestionText = itemView.findViewById(R.id.suggestion_text_view);
    }

    @Override
    public void bind(final SuggestionViewModel element) {
        bindSuggestionView(element);
    }

    private void bindSuggestionView(final SuggestionViewModel element) {
        if (!TextUtils.isEmpty(element.getSuggestionText())) {
            suggestionText.setText(Html.fromHtml(element.getSuggestionText()));
            suggestionText.setOnClickListener(v -> {
                if (suggestionListener != null && !TextUtils.isEmpty(element.getSuggestedQuery())) {
                    suggestionListener.onSuggestionClicked(element);
                }
            });
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
