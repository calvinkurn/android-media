package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.base.list.seller.view.old.BaseLinearRecyclerViewAdapter;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsAddProductListViewHolder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyRowViewHolder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsNonPromotedViewHolder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsPromotedViewHolder;
import com.tokopedia.topads.dashboard.view.listener.AdapterSelectionListener;
import com.tokopedia.topads.dashboard.view.model.BaseTopAdsProductModel;
import com.tokopedia.topads.dashboard.view.model.EmptyTypeBasedModel;
import com.tokopedia.topads.dashboard.view.model.NonPromotedTopAdsAddProductModel;
import com.tokopedia.topads.dashboard.view.model.PromotedTopAdsAddProductModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddProductModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/13/17.
 *
 * @author normansyahputa
 *         <p>
 *         13-02-2017 - create first time and layout
 *         06-03-2017, separate promoted and non promoted.
 *         <p>
 */
public class TopAdsAddProductListAdapter extends BaseLinearRecyclerViewAdapter{
    List<ItemType> datas;
    AdapterSelectionListener<TopAdsProductViewModel> listener;
    private ImageHandler imageHandler;
    private boolean isEmptyShown;

    public TopAdsAddProductListAdapter(ImageHandler imageHandler,
                                       AdapterSelectionListener<TopAdsProductViewModel> listener) {
        this.imageHandler = imageHandler;
        this.listener = listener;
        datas = new ArrayList<>();
    }

    public TopAdsAddProductListAdapter() {
        datas = new ArrayList<>();
    }

    public void setImageHandler(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    public void setListener(AdapterSelectionListener<TopAdsProductViewModel> adapterSelectionListener) {
        this.listener = adapterSelectionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.
                from(parent.getContext());
        switch (viewType) {
            case PromotedTopAdsAddProductModel.TYPE:
                View view = from.inflate(R.layout.item_top_ads_add_product_list_promoted,
                        parent, false);
                return new TopAdsPromotedViewHolder(view);
            case NonPromotedTopAdsAddProductModel.TYPE:
                view = from.inflate(R.layout.item_top_ads_add_product_list_non_promoted,
                        parent, false);
                return new TopAdsNonPromotedViewHolder(view, listener);
            case TopAdsAddProductModel.TYPE:
                view = from.inflate(R.layout.item_top_ads_add_product_list, parent, false);
                return new TopAdsAddProductListViewHolder(view, imageHandler, listener);
            case EmptyTypeBasedModel.TYPE:
                view = from.inflate(R.layout.item_top_ads_empty_list, parent, false);
                return new TopAdsEmptyRowViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case PromotedTopAdsAddProductModel.TYPE:
                ((TopAdsPromotedViewHolder) holder).bind(
                        ((PromotedTopAdsAddProductModel) datas.get(position))
                );
                break;
            case NonPromotedTopAdsAddProductModel.TYPE:
                ((TopAdsNonPromotedViewHolder) holder).bind(
                        ((NonPromotedTopAdsAddProductModel) datas.get(position))
                );
                break;
            case TopAdsAddProductModel.TYPE:
                TopAdsAddProductModel topAdsAddProductModel
                        = (TopAdsAddProductModel) datas.get(position);
                ((TopAdsAddProductListViewHolder) holder).
                        bind(topAdsAddProductModel);
                break;
            case EmptyTypeBasedModel.TYPE:
                break; // prevent from entering super.
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (datas.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return datas.get(position).getType();
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == datas.size();
    }

    public void clear() {
        showEmptyFull(false);
        showEmpty(false);
        showLoadingFull(false);
        showLoading(false);
        showRetry(false);
        showRetryFull(false);
        datas.clear();
    }

    public void addAll(List<ItemType> itemTypes) {
        datas.addAll(itemTypes);
        notifyDataSetChanged();
    }

    public void addAllWithoutNotify(List<ItemType> itemTypes) {
        datas.addAll(itemTypes);
    }

    @Override
    public int getItemCount() {
        return datas.size() + super.getItemCount();
    }

    public void notifyUnCheck(TopAdsProductViewModel topAdsProductViewModel) {
        // search for position;
        List<TopAdsProductViewModel> topAdsProductViewModels = convertTo();

        int position = topAdsProductViewModels.indexOf(topAdsProductViewModel);

        if (position >= 0) {
            removedItem(position);
        }
    }

    private List<TopAdsProductViewModel> convertTo() {
        List<TopAdsProductViewModel> topAdsProductViewModels =
                new ArrayList<>();
        for (ItemType data : datas) {
            if (data != null && data instanceof BaseTopAdsProductModel) {
                BaseTopAdsProductModel baseTopAdsProductModel = ((BaseTopAdsProductModel) data);
                if (baseTopAdsProductModel.getTopAdsProductViewModel() != null) {
                    topAdsProductViewModels.add(
                            baseTopAdsProductModel.getTopAdsProductViewModel());
                }
            } else {
                throw new RuntimeException("all model in this adapter must implement " +
                        "BaseTopAdsProductModel");
            }
        }
        return topAdsProductViewModels;
    }

    public void removedItem(int position) {
        ItemType itemType = datas.get(position);
        if (itemType != null && itemType instanceof TopAdsAddProductModel) {
            TopAdsAddProductModel topAdsAddProductModel = (TopAdsAddProductModel) itemType;
            topAdsAddProductModel.setSelected(false);

            datas.set(position, topAdsAddProductModel);


            notifyItemChanged(position);
        } else {
            notifyItemChanged(position);
        }
    }

    public int getDataSize() {
        return datas.size();
    }

    public TopAdsProductViewModel getItem(int position) {
        if (position >= 0 && position < datas.size()) {
            ItemType itemType = datas.get(position);
            if (itemType instanceof TopAdsAddProductModel) {
                return ((TopAdsAddProductModel) itemType).productDomain;
            }
        }

        return null;
    }

    public void insertEmptyFooter() {
        if (!isEmptyShown) {
            isEmptyShown = datas.add(new EmptyTypeBasedModel());
            notifyItemInserted(datas.size() - 1);
        }
    }

    public void removeEmptyFooter() {
        if (isEmptyShown) {
            int index = datas.size() - 1;
            datas.remove(index);
            notifyItemRemoved(index);

            isEmptyShown = false;
        }
    }

    public void resetEmptyShown() {
        isEmptyShown = false;
    }
}
