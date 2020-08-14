package com.tokopedia.navigation.presentation.adapter;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.InboxTopAdsBannerUiModel;
import com.tokopedia.navigation.presentation.adapter.viewholder.InboxTopAdsBannerViewHolder;
import com.tokopedia.navigation.presentation.adapter.viewholder.RecommendationViewHolder;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel;

import java.util.List;


/**
 * Created by meta on 15/07/18.
 */
public class InboxAdapter extends BaseAdapter<InboxAdapterTypeFactory> {

    private InboxAdapterTypeFactory typeFactory;

    public InboxAdapter(InboxAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        this.typeFactory = adapterTypeFactory;
    }

    public void updateValue(NotificationsModel entity) {
        if(visitables !=null && visitables.size() > 0){
            if(visitables.get(InboxFragment.CHAT_MENU) instanceof Inbox){
                ((Inbox) visitables.get(InboxFragment.CHAT_MENU))
                        .setTotalBadge(entity.getChat().getUnreads());
            }
            if(visitables.get(InboxFragment.DISCUSSION_MENU) instanceof Inbox){
                ((Inbox) visitables.get(InboxFragment.DISCUSSION_MENU))
                        .setTotalBadge(entity.getInbox().getTalk());
            }
            if(visitables.get(InboxFragment.REVIEW_MENU) instanceof Inbox){
                ((Inbox) visitables.get(InboxFragment.REVIEW_MENU))
                        .setTotalBadge(entity.getInbox().getReview());
            }
            if(visitables.get(InboxFragment.HELP_MENU) instanceof Inbox){
                ((Inbox) visitables.get(InboxFragment.HELP_MENU))
                        .setTotalBadge(entity.getInbox().getTicket());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layout = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        if(getItemViewType(position) == RecommendationViewHolder.Companion.getLAYOUT()){
            layout.setFullSpan(false);
        } else {
            layout.setFullSpan(true);
        }
        holder.bind(visitables.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(typeFactory);
    }

    public void updateTopAdsBanner(TopAdsImageViewModel topAdsImageViewModel) {
        int bannerPosition = 4;
        if (bannerPosition >= visitables.size()) return;
        Visitable item = visitables.get(bannerPosition);
        if (item instanceof InboxTopAdsBannerUiModel) {
            InboxTopAdsBannerUiModel banner = (InboxTopAdsBannerUiModel) item;
            banner.setAd(topAdsImageViewModel);
            notifyItemChanged(bannerPosition, InboxTopAdsBannerViewHolder.getPAYLOAD_UPDATE_AD());
        }
    }
}
