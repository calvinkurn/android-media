package com.tokopedia.digital.product.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
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
    private ActionListener actionListener;

    public BannerAdapter(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HOLDER_TITLE) {
            return new BannerTitleHolder(LayoutInflater.from(
                    parent.getContext()).inflate(viewType, parent, false
            ));
        } else if (viewType == TYPE_HOLDER_BANNER_ITEM) {
            return new BannerItemHolder(LayoutInflater.from(
                    parent.getContext()).inflate(viewType, parent, false
            ));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerItemHolder) {
            BannerData bannerData = (BannerData) dataList.get(position);
            BannerItemHolder bannerItemHolder = (BannerItemHolder) holder;
            bannerItemHolder.tvDescBanner.setText(MethodChecker.fromHtml(bannerData.getSubtitle()));
            bannerItemHolder.tvVoucherCode.setText("Belom ada param");
            bannerItemHolder.btnCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.onButtonCopyClicked("hahahaha");
                }
            });
        } else if (holder instanceof BannerTitleHolder) {
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
        dataList.clear();
        notifyDataSetChanged();
        dataList.add(new BannerTitle(title));
        notifyItemInserted(0);
        for (int i = 0; i < bannerDataList.size(); i++) {
            BannerData bannerData = bannerDataList.get(i);
            dataList.add(bannerData);
            notifyItemInserted(i + 1);
        }
    }

    static class BannerItemHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_desc_banner)
        TextView tvDescBanner;
        @BindView(R2.id.tv_voucher_code)
        TextView tvVoucherCode;
        @BindView(R2.id.btn_copy)
        TextView btnCopy;

        public BannerItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class BannerTitleHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_title)
        TextView tvTitle;

        public BannerTitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onButtonCopyClicked(String voucherCode);
    }
}
