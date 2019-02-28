package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.viewmodel.DummyModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DummyViewHolder extends AbstractViewHolder<DummyModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.dummy_ovo;


    public DummyViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
    }

    @Override
    public void bind(DummyModel element) {

    }
}
