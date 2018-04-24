package com.tokopedia.feedplus.view.adapter.viewholder.inspiration;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.inspiration.InspirationViewModel;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class InspirationViewHolder extends AbstractViewHolder<InspirationViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.inspiration_layout;
    private final FeedPlus.View viewListener;

    RecyclerView recyclerView;
    TextView textView;

    private InspirationAdapter adapter;

    public InspirationViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        textView = (TextView) itemView.findViewById(R.id.title);
        this.viewListener = viewListener;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                6,
                LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getList().size() == 1) {
                    return 6;
                } else if (adapter.getList().size() % 3 == 0 || adapter.getList().size() > 6) {
                    return 2;
                } else if (adapter.getList().size() % 2 == 0) {
                    return 3;
                } else {
                    return 0;
                }
            }
        });
        adapter = new InspirationAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(InspirationViewModel inspirationViewModel) {
        inspirationViewModel.setRowNumber(getAdapterPosition());
        adapter.setData(inspirationViewModel);
        if (!TextUtils.isEmpty(inspirationViewModel.getTitle())) {
            textView.setText(inspirationViewModel.getTitle());
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

}
