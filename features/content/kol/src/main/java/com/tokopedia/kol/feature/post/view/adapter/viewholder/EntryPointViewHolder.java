package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel;

/**
 * @author by milhamj on 10/3/18.
 */
public class EntryPointViewHolder extends AbstractViewHolder<EntryPointViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.entry_point_layout;

    private ButtonCompat createPostBtn;

    public EntryPointViewHolder(View itemView) {
        super(itemView);
        createPostBtn = itemView.findViewById(R.id.createPostBtn);
    }

    @Override
    public void bind(EntryPointViewModel element) {
        createPostBtn.setOnClickListener(element.getOnClickListener());
    }
}
