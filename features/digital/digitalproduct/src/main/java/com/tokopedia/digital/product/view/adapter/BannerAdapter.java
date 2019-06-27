package com.tokopedia.digital.product.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.BannerTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/2/17.
 */

public class BannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOLDER_BANNER_ITEM =
            R.layout.view_holder_banner_item_digital_module;
    private static final int TYPE_HOLDER_TITLE =
            R.layout.view_holder_banner_title_digital_module;

    private List<Object> dataList = new ArrayList<>();
    private Context context;
    private ActionListener actionListener;

    public BannerAdapter(Context context, ActionListener actionListener) {
        this.actionListener = actionListener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HOLDER_TITLE) {
            return new BannerTitleHolder(LayoutInflater.from(
                    context).inflate(viewType, parent, false
            ));
        } else if (viewType == TYPE_HOLDER_BANNER_ITEM) {
            return new BannerItemHolder(LayoutInflater.from(
                    context).inflate(viewType, parent, false
            ));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_HOLDER_BANNER_ITEM) {
            final BannerData bannerData = (BannerData) dataList.get(position);

            BannerItemHolder bannerItemHolder = (BannerItemHolder) holder;
            if (bannerData.isVoucherCodeCopied()) {
                bannerItemHolder.mainContainer.setBackgroundDrawable(
                        context.getResources().getDrawable(
                                R.drawable.digital_bg_banner_selected
                        )
                );
                bannerItemHolder.tvVoucherCode.setBackgroundColor(context
                        .getResources()
                        .getColor(R.color.digital_voucher_copied_color));
                bannerItemHolder.tvVoucherCode.setTextColor(context
                        .getResources()
                        .getColor(android.R.color.white));
            } else {
                bannerItemHolder.mainContainer.setBackgroundDrawable(
                        context.getResources().getDrawable(
                                R.drawable.digital_bg_banner_item
                        )
                );
                bannerItemHolder.tvVoucherCode.setTextColor(context
                        .getResources()
                        .getColor(R.color.digital_voucher_copied_color));
                bannerItemHolder.tvVoucherCode.setBackgroundColor(context
                        .getResources()
                        .getColor(android.R.color.transparent));
            }

            bannerItemHolder.tvDescBanner.setText(MethodChecker.fromHtml(bannerData.getTitle()));
            bannerItemHolder.holderVoucherCode.setVisibility(
                    TextUtils.isEmpty(bannerData.getPromocode()) ? View.GONE : View.VISIBLE
            );
            bannerItemHolder.tvVoucherCode.setText(bannerData.getPromocode());
            bannerItemHolder.btnCopy.setOnClickListener(getButtonCopyClickedListener(position, bannerData));
            bannerItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.onBannerItemClicked(bannerData);
                }
            });
        } else if (type == TYPE_HOLDER_TITLE) {
            BannerTitle bannerTitle = (BannerTitle) dataList.get(position);
            BannerTitleHolder bannerTitleHolder = (BannerTitleHolder) holder;
            bannerTitleHolder.tvTitle.setText(bannerTitle.getTitle());
        }
    }

    @NonNull
    private View.OnClickListener getButtonCopyClickedListener(
            final int position, final BannerData bannerData
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0, dataListSize = dataList.size(); i < dataListSize; i++) {
                    Object object = dataList.get(i);
                    if (object instanceof BannerData) {
                        if (((BannerData) object).isVoucherCodeCopied()) {
                            ((BannerData) dataList.get(i)).setVoucherCodeCopied(false);
                            notifyItemChanged(i);
                            break;
                        }
                    }
                }
                ((BannerData) dataList.get(position)).setVoucherCodeCopied(true);
                notifyItemChanged(position);
                actionListener.onButtonCopyBannerVoucherCodeClicked(bannerData.getPromocode());
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof BannerTitle) {
            return TYPE_HOLDER_TITLE;
        } else if (dataList.get(position) instanceof BannerData) {
            return TYPE_HOLDER_BANNER_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addBannerDataListAndTitle(List<BannerData> bannerDataList, String title) {
        if (bannerDataList == null || bannerDataList.isEmpty()) return;
        dataList.add(new BannerTitle(title));
        for (int i = 0; i < bannerDataList.size(); i++) {
            BannerData bannerData = bannerDataList.get(i);
            dataList.add(bannerData);
        }
        notifyDataSetChanged();
    }

    public void clearDataList() {
        dataList.clear();
        notifyDataSetChanged();
    }

    static class BannerItemHolder extends RecyclerView.ViewHolder {
        private LinearLayout mainContainer;
        private TextView tvDescBanner;
        private TextView tvVoucherCode;
        private TextView btnCopy;
        private LinearLayout holderVoucherCode;

        BannerItemHolder(View itemView) {
            super(itemView);

            mainContainer = itemView.findViewById(R.id.main_container);
            tvDescBanner = itemView.findViewById(R.id.tv_desc_banner);
            tvVoucherCode = itemView.findViewById(R.id.tv_voucher_code);
            btnCopy = itemView.findViewById(R.id.btn_copy);
            holderVoucherCode = itemView.findViewById(R.id.holder_voucher);
        }
    }

    static class BannerTitleHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        BannerTitleHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public interface ActionListener {
        void onButtonCopyBannerVoucherCodeClicked(String voucherCode);

        void onBannerItemClicked(BannerData bannerData);
    }

}
