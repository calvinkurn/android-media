package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.graphics.PorterDuff;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;


/**
 * @author Kulomady on 1/25/17.
 */

public class LoadingMoreViewHolder extends AbstractViewHolder<LoadingMoreModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.loading_layout;

    private ProgressBar progressBar;

    public LoadingMoreViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }

    @Override
    public void bind(LoadingMoreModel element) {
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.getIndeterminateDrawable().setColorFilter(
                MethodChecker.getColor(progressBar.getContext(), R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
        );
    }
}
