package com.tokopedia.digital.product.adapter;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.fragment.DigitalProductFragment;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.BannerTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/2/17.
 */

public class BannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HOLDER_BANNER_ITEM =
            R.layout.view_holder_banner_item_digital_module;
    private static final int TYPE_HOLDER_TITLE =
            R.layout.view_holder_banner_title_digital_module;

    private List<Object> dataList = new ArrayList<>();
    private Fragment hostFragment;
    private ActionListener actionListener;

    public BannerAdapter(DigitalProductFragment digitalProductFragment) {
        this.actionListener = digitalProductFragment;
        this.hostFragment = digitalProductFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HOLDER_TITLE) {
            return new BannerTitleHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        } else if (viewType == TYPE_HOLDER_BANNER_ITEM) {
            return new BannerItemHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
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
                        hostFragment.getResources().getDrawable(
                                R.drawable.digital_bg_banner_selected
                        )
                );
                bannerItemHolder.tvVoucherCode.setBackgroundColor(hostFragment
                        .getResources()
                        .getColor(R.color.digital_voucher_copied_color));
                bannerItemHolder.tvVoucherCode.setTextColor(hostFragment
                        .getResources()
                        .getColor(android.R.color.white));
            } else {
                bannerItemHolder.mainContainer.setBackgroundDrawable(
                        hostFragment.getResources().getDrawable(
                                R.drawable.digital_bg_banner_item
                        )
                );
                bannerItemHolder.tvVoucherCode.setTextColor(hostFragment
                        .getResources()
                        .getColor(R.color.digital_voucher_copied_color));
                bannerItemHolder.tvVoucherCode.setBackgroundColor(hostFragment
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
        if (bannerDataList.isEmpty()) return;
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

        @BindView(R2.id.main_container)
        LinearLayout mainContainer;
        @BindView(R2.id.tv_desc_banner)
        TextView tvDescBanner;
        @BindView(R2.id.tv_voucher_code)
        TextView tvVoucherCode;
        @BindView(R2.id.btn_copy)
        TextView btnCopy;
        @BindView(R2.id.holder_voucher)
        LinearLayout holderVoucherCode;

        BannerItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class BannerTitleHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_title)
        TextView tvTitle;

        BannerTitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onButtonCopyBannerVoucherCodeClicked(String voucherCode);

        void onBannerItemClicked(BannerData bannerData);
    }
}
