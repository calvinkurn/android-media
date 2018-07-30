package com.tokopedia.shop.product.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.EmptyKeywordModel;

public class EmptyProductViewHolder extends BaseEmptyViewHolder<EmptyKeywordModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_view_product_no_result;

    public EmptyProductViewHolder(View itemView) {
        super(itemView);
    }

    public EmptyProductViewHolder(View itemView, Callback callback) {
        super(itemView, callback);
    }

    @Override
    public void bind(EmptyKeywordModel element) {
        super.bind(element);
        String keyword = element.getKeyword();
        if (TextUtils.isEmpty(keyword)) {
            emptyContentItemTextView.setVisibility(View.GONE);
        } else {
            Context context = emptyContentItemTextView.getContext();
            String stringToShow = emptyContentItemTextView.getContext().getString(
                    R.string.search_keyword_at_tokopedia, keyword);
            Spannable spannable = new SpannableString(stringToShow);
            int indexStart = stringToShow.indexOf(keyword);
            int indexEnd = indexStart + keyword.length();

            spannable.setSpan(new ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.tkpd_main_green)
            ), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    //TODO go to tokopedia search
                    Toast.makeText(context, "TEest", Toast.LENGTH_LONG).show();
                }
            };
            spannable.setSpan(clickableSpan, indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            emptyContentItemTextView.setMovementMethod(LinkMovementMethod.getInstance());
            emptyContentItemTextView.setText(spannable);
            emptyContentItemTextView.setVisibility(View.VISIBLE);
        }
    }
}
