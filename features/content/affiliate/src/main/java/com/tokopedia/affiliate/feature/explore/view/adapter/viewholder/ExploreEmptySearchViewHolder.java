package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;

/**
 * @author by yfsx on 12/10/18.
 */
public class ExploreEmptySearchViewHolder extends AbstractViewHolder<ExploreEmptySearchViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_af_explore_empty_search;

    private ExploreContract.View mainView;

    private Button searchButton;

    public ExploreEmptySearchViewHolder(View itemView, ExploreContract.View mainView) {
        super(itemView);
        this.mainView = mainView;
        searchButton = (Button) itemView.findViewById(R.id.btn_search);
    }

    @Override
    public void bind(ExploreEmptySearchViewModel element) {
        initView(element);
        initViewListener(element);
    }

    private void initView(ExploreEmptySearchViewModel element) {

    }

    private void initViewListener(ExploreEmptySearchViewModel element) {
        searchButton.setOnClickListener(view -> {
            mainView.onButtonEmptySearchClicked();
        });
    }
}
