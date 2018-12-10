package com.tokopedia.loyalty.view.viewholder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter.OnAdapterActionListener;
import com.tokopedia.loyalty.view.adapter.PromoDetailSingleCodeAdapter;
import com.tokopedia.loyalty.view.data.PromoCodeViewModel;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;

import java.util.List;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailGroupCodeViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_GROUP_CODE = R.layout.item_view_promo_group_code_layout;

    private OnAdapterActionListener adapterActionListener;

    private ImageView ivChevronIcon;
    private ImageView ivTooltipInfo;
    private TextView tvGroupCodeTitle;
    private TextView tvGroupCodeDescription;
    private RecyclerView rvSingleCodeView;
    private RelativeLayout rlDetailGroupCodeLayout;

    private Context context;

    private boolean isDetailOpen;

    public PromoDetailGroupCodeViewHolder(View itemView, Context context,
                                          OnAdapterActionListener adapterActionListener) {
        super(itemView);

        this.context = context;
        this.adapterActionListener = adapterActionListener;

        this.ivChevronIcon = itemView.findViewById(R.id.iv_chevron_icon);
        this.ivTooltipInfo = itemView.findViewById(R.id.iv_tooltip_info);
        this.tvGroupCodeTitle = itemView.findViewById(R.id.tv_group_code_title);
        this.tvGroupCodeDescription = itemView.findViewById(R.id.tv_group_code_description);
        this.rvSingleCodeView = itemView.findViewById(R.id.rv_single_code_view);
        this.rlDetailGroupCodeLayout = itemView.findViewById(R.id.rl_detail_group_code_layout);

        this.isDetailOpen = false;
    }

    public void bind(PromoCodeViewModel viewModel) {

        this.ivTooltipInfo.setOnClickListener(tooltipInfoListener());
        this.rlDetailGroupCodeLayout.setVisibility(isDetailOpen ? View.VISIBLE : View.GONE);

        this.ivChevronIcon.setOnClickListener(chevronListener());
        this.ivChevronIcon.setImageResource(isDetailOpen ?
            R.drawable.ic_arrow_up_grey : R.drawable.ic_arrow_down_grey);

        this.tvGroupCodeTitle.setVisibility(TextUtils.isEmpty(viewModel.getGroupCodeTitle()) ?
                View.GONE : View.VISIBLE);
        this.tvGroupCodeTitle.setText(viewModel.getGroupCodeTitle());

        this.tvGroupCodeDescription.setVisibility(TextUtils.isEmpty(viewModel.getGroupCodeTitle()) ?
                View.GONE : View.VISIBLE);
        this.tvGroupCodeDescription.setText(viewModel.getGroupCodeDescription());

        this.rvSingleCodeView.setVisibility(View.VISIBLE);
        this.rvSingleCodeView.setHasFixedSize(true);
        this.rvSingleCodeView.setLayoutManager(getLayoutManager());
        this.rvSingleCodeView.setAdapter(getRecyclerViewAdapter(viewModel.getGroupCode()));
    }

    private GridLayoutManager getLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(this.context, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }

    private PromoDetailSingleCodeAdapter getRecyclerViewAdapter(List<SingleCodeViewModel> groupCode) {
        PromoDetailSingleCodeAdapter adapter = new PromoDetailSingleCodeAdapter(groupCode);
        adapter.setAdapterActionListener(adapterActionListener);
        return adapter;
    }

    private View.OnClickListener chevronListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleShowLayout();
            }
        };
    }

    private View.OnClickListener tooltipInfoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterActionListener.onItemPromoCodeTooltipClicked();
            }
        };
    }

    private void toggleShowLayout() {
        isDetailOpen = !isDetailOpen;
        this.rlDetailGroupCodeLayout.setVisibility(isDetailOpen ? View.VISIBLE : View.GONE);
        this.ivChevronIcon.setImageResource(isDetailOpen ?
                R.drawable.ic_arrow_up_grey : R.drawable.ic_arrow_down_grey);
    }
}
