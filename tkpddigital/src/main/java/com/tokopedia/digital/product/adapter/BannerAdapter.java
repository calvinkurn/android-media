package com.tokopedia.digital.product.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_HOLDER_BANNER_ITEM) {
            final BannerData bannerData = (BannerData) dataList.get(position);
            BannerItemHolder bannerItemHolder = (BannerItemHolder) holder;
            bannerItemHolder.tvDescBanner.setText(MethodChecker.fromHtml(bannerData.getTitle()));
            bannerItemHolder.tvVoucherCode.setText(bannerData.getPromocode());
            bannerItemHolder.btnCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.onButtonCopyBannerVoucherCodeClicked(bannerData.getPromocode());
                }
            });
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

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof BannerTitle) return TYPE_HOLDER_TITLE;
        else if (dataList.get(position) instanceof BannerData) return TYPE_HOLDER_BANNER_ITEM;
        else return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addBannerDataListAndTitle(List<BannerData> bannerDataList, String title) {
        dataList.add(new BannerTitle(title));
        for (int i = 0; i < bannerDataList.size(); i++) {
            BannerData bannerData = bannerDataList.get(i);
            dataList.add(bannerData);
        }
        notifyDataSetChanged();
    }

    static class BannerItemHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_desc_banner)
        TextView tvDescBanner;
        @BindView(R2.id.tv_voucher_code)
        TextView tvVoucherCode;
        @BindView(R2.id.btn_copy)
        TextView btnCopy;

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
