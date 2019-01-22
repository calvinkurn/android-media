package com.tokopedia.digital.newcart.presentation.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.presentation.compoundview.adapter.DigitalCartDetailAdapter;

import java.util.ArrayList;
import java.util.List;

public class DigitalCartDetailHolderView extends LinearLayout {
    private RecyclerView mainInfoRecyclerView;
    private AppCompatTextView detailToggleAppCompatTextView;

    private List<CartItemDigital> mainInfos;
    private List<CartAdditionalInfo> additionalInfos;

    private DigitalCartDetailAdapter adapter;

    public DigitalCartDetailHolderView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public DigitalCartDetailHolderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DigitalCartDetailHolderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_digital_cart_detail_holder, this, true
        );
        mainInfoRecyclerView = view.findViewById(R.id.rv_details);
        detailToggleAppCompatTextView = view.findViewById(R.id.tv_see_detail_toggle);

    }

    public void setMainInfo(List<CartItemDigital> mainInfos) {
        this.mainInfos = mainInfos;
        adapter.setInfos(new ArrayList<>(mainInfos));
        adapter.notifyDataSetChanged();
    }

    public void setAdditionalInfos(List<CartAdditionalInfo> additionalInfos) {
        this.additionalInfos = additionalInfos;
        if (additionalInfos != null && additionalInfos.size() > 0) {
            detailToggleAppCompatTextView.setVisibility(VISIBLE);
            detailToggleAppCompatTextView.setText(R.string.digital_cart_detail_see_detail_label);
        } else {
            detailToggleAppCompatTextView.setVisibility(GONE);
        }
        if (adapter.getItemCount() != mainInfos.size() && additionalInfos != null) {
            detailToggleAppCompatTextView.setText(R.string.digital_cart_detail_close_label);
            List<Visitable> newLists = new ArrayList<>(mainInfos);
            newLists.addAll(constructAdditionalInfo(additionalInfos));
            adapter.setInfos(newLists);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        adapter = new DigitalCartDetailAdapter();
        mainInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainInfoRecyclerView.setNestedScrollingEnabled(false);
        mainInfoRecyclerView.setAdapter(adapter);

        detailToggleAppCompatTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() == mainInfos.size()) {
                    expandAdditional();
                } else {
                    collapseAdditional();
                }
            }
        });
    }

    private List<Visitable> constructAdditionalInfo(List<CartAdditionalInfo> additionalInfos) {
        List<Visitable> visitables = new ArrayList<>();
        for (CartAdditionalInfo cartAdditionalInfo : additionalInfos) {
            CartAdditionalInfo info = new CartAdditionalInfo(cartAdditionalInfo.getTitle(), new ArrayList<>());
            visitables.add(info);
            List<Visitable> infos = new ArrayList<>(cartAdditionalInfo.getCartItemDigitalList());
            visitables.addAll(infos);
        }
        return visitables;
    }

    public void expandAdditional() {
        if (adapter.getItemCount() == mainInfos.size()) {
            detailToggleAppCompatTextView.setText(R.string.digital_cart_detail_close_label);
            adapter.addInfos(constructAdditionalInfo(additionalInfos));
            adapter.notifyDataSetChanged();
        }
    }


    public void collapseAdditional() {
        if (adapter.getItemCount() != mainInfos.size()) {
            detailToggleAppCompatTextView.setText(R.string.digital_cart_detail_see_detail_label);
            adapter.setInfos(new ArrayList<>(mainInfos));
            adapter.notifyDataSetChanged();
        }
    }


}
