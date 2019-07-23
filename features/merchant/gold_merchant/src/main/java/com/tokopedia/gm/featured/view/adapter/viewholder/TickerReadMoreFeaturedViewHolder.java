package com.tokopedia.gm.featured.view.adapter.viewholder;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.gm.R;
import com.tokopedia.gm.featured.view.adapter.model.TickerReadMoreFeaturedModel;

import org.jetbrains.annotations.NotNull;

/**
 * Created by normansyahputa on 9/8/17.
 */

public class TickerReadMoreFeaturedViewHolder extends BaseViewHolder<TickerReadMoreFeaturedModel> {
    private final TextView textViewTitle;
    private final TextView textViewDescription;
    private TickerViewHolderViewHolderListener tickerViewHolderViewHolderListener;

    public TickerReadMoreFeaturedViewHolder(
            View itemView,
            TickerViewHolderViewHolderListener tickerViewHolderViewHolderListener
    ) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.txt_ticker_title);
        textViewDescription = itemView.findViewById(R.id.txt_ticker_description);
        this.tickerViewHolderViewHolderListener = tickerViewHolderViewHolderListener;
    }

    @Override
    public void bindObject(TickerReadMoreFeaturedModel gmFeaturedProductModel) {
        bindData(gmFeaturedProductModel);
    }

    public void bindData(final TickerReadMoreFeaturedModel tickerReadMoreFeaturedModel) {
        textViewTitle.setText(tickerReadMoreFeaturedModel.getTitle());
        textViewDescription.setText(
                createDescriptionWithSpannable(
                        tickerReadMoreFeaturedModel.getDescription(),
                        tickerReadMoreFeaturedModel.getReadMoreString()
                )
        );
    }

    private SpannableStringBuilder createDescriptionWithSpannable(
            String originalText,
            String readMoreText
    ) {
        SpannableString spannableText = new SpannableString(readMoreText);
        int startIndex = 0;
        int endIndex = spannableText.length();
        int color = Color.parseColor("#03ac0e");
        spannableText.setSpan(color, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View view) {
                tickerViewHolderViewHolderListener.onTickerReadMoreClicked();
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
            }
        };
        spannableText.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return new SpannableStringBuilder(originalText).append(" ").append(spannableText);
    }

    public interface TickerViewHolderViewHolderListener {

        void onTickerReadMoreClicked();
    }
}
