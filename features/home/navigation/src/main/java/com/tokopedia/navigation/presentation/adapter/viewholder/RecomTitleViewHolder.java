package com.tokopedia.navigation.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.RecomTitle;

/**
 * Author errysuprayogi on 15,March,2019
 */
public class RecomTitleViewHolder extends AbstractViewHolder<RecomTitle> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_title;

    private TextViewCompat textView;

    public RecomTitleViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.title);
    }

    @Override
    public void bind(RecomTitle element) {
        textView.setText(element.getTitle());
    }
}
