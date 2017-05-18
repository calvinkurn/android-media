package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedProductAdapter;
import com.tokopedia.tkpd.feedplus.view.adapter.OfficialStoreAdapter;
import com.tokopedia.tkpd.feedplus.view.adapter.PromoAdapter;
import com.tokopedia.tkpd.feedplus.view.viewmodel.OfficialStoreViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromoViewModel;

import butterknife.BindView;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class OfficialStoreViewHolder extends AbstractViewHolder<OfficialStoreViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.official_store_layout;
    private final FeedPlus.View viewListener;

    @BindView(R.id.product_list)
    RecyclerView recyclerView;

    @BindView(R.id.official_store_image)
    ImageView imageView;

    private OfficialStoreAdapter adapter;

    private OfficialStoreViewModel officialStoreViewModel;

    public OfficialStoreViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
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
        adapter = new OfficialStoreAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(OfficialStoreViewModel officialStoreViewModel) {
        this.officialStoreViewModel = officialStoreViewModel;
        adapter.setList(officialStoreViewModel.getListProduct());
        ImageHandler.LoadImage(imageView, officialStoreViewModel.getOfficialStoreHeaderImageUrl());
    }

}
