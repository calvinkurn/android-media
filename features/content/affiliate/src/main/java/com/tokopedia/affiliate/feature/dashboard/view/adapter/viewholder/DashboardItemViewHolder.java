package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;

/**
 * @author by yfsx on 18/09/18.
 */
public class DashboardItemViewHolder extends AbstractViewHolder<DashboardItemViewModel> {

    private DashboardContract.View mainView;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_af_dashboard;

    private ImageView ivItem;
    private FrameLayout layoutStatus;
    private TextView tvStatus, tvName, tvCommission, tvBuyCount, tvClickCount, tvProductCommission;
    private LinearLayout layoutActive, layoutInactive;

    public DashboardItemViewHolder(View itemView, DashboardContract.View mainView) {
        super(itemView);
        this.mainView = mainView;
        ivItem = (ImageView) itemView.findViewById(R.id.iv_item);
        layoutStatus = (FrameLayout) itemView.findViewById(R.id.layout_status);
        tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCommission = (TextView) itemView.findViewById(R.id.tv_commission);
        tvBuyCount = (TextView) itemView.findViewById(R.id.tv_buy_count);
        tvClickCount = (TextView) itemView.findViewById(R.id.tv_click_count);
        tvProductCommission = (TextView) itemView.findViewById(R.id.tv_product_commission);
        layoutActive = (LinearLayout) itemView.findViewById(R.id.layout_active);
        layoutInactive = (LinearLayout) itemView.findViewById(R.id.layout_inactive);
    }

    @Override
    public void bind(DashboardItemViewModel element) {
        initView(element);
    }

    private void initView(DashboardItemViewModel element) {
        ImageHandler.loadImageRounded2(ivItem.getContext(), ivItem, element.getImageUrl(), 6.0f);
        tvName.setText(MethodChecker.fromHtml(element.getTitle()));
        if (element.isActive() && !element.getProductCommission().isEmpty()) {
            layoutInactive.setVisibility(View.GONE);
            layoutActive.setVisibility(View.VISIBLE);
            tvProductCommission.setText(element.getProductCommission());
        } else {
            layoutInactive.setVisibility(View.VISIBLE);
            layoutActive.setVisibility(View.GONE);
            tvCommission.setText(element.getValue());
        }
        tvClickCount.setText(element.getItemClicked());
        tvBuyCount.setText(element.getItemSold());
        layoutStatus.setBackground(MethodChecker.getDrawable(
                layoutStatus.getContext(),
                element.isActive() ?
                        R.drawable.bg_af_ongoing :
                        R.drawable.bg_af_finished));
        tvStatus.setText(tvStatus.getContext().getString(
                element.isActive() ?
                        R.string.text_af_ongoing :
                        R.string.text_af_finished));
        tvStatus.setTextColor(MethodChecker.getColor(
                tvStatus.getContext(),
                element.isActive() ?
                        R.color.color_ongoing_text :
                        R.color.font_black_secondary_54
                )
        );
    }
}
