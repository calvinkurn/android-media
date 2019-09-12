package com.tokopedia.groupchat.vote.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.groupchat.vote.view.adapter.typefactory.VoteTypeFactory;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private VoteTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;


    public static VoteAdapter createInstance(VoteTypeFactory voteTypeFactory) {
        return new VoteAdapter(voteTypeFactory);
    }

    public VoteAdapter(VoteTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(List<Visitable> listChat) {
        if (listChat != null) {
            this.list.clear();
            this.list.addAll(listChat);
            notifyDataSetChanged();
        }
    }

    public void change(GroupChatViewModel viewModel, VoteViewModel element,
                       VoteStatisticDomainModel voteStatisticViewModel) {
        change(viewModel.getChannelInfoViewModel().getVoteInfoViewModel(), element, voteStatisticViewModel);
    }

    public void change(VoteInfoViewModel viewModel, VoteViewModel element,
                       VoteStatisticDomainModel voteStatisticViewModel) {
        int index = list.indexOf(element);
        for (int i = 0; i < list.size(); i++) {
            VoteViewModel temp = (VoteViewModel) list.get(i);
            if (index == i) {
                temp.setSelected(VoteViewModel.SELECTED);
                ((VoteViewModel) viewModel.getListOption().get(i)).setSelected(VoteViewModel.SELECTED);

            } else {
                temp.setSelected(VoteViewModel.UNSELECTED);
                ((VoteViewModel) viewModel.getListOption().get(i)).setSelected(VoteViewModel.UNSELECTED);

            }
            ((VoteViewModel) viewModel.getListOption().get(i)).setPercentage(
                    voteStatisticViewModel.getListOptions().get(i).getPercentage());
        }
        notifyItemRangeChanged(0, list.size());
    }

    public void updateStatistic() {
        for (int i = 0; i < list.size(); i++) {
            VoteViewModel temp = (VoteViewModel) list.get(i);
            if (temp.getSelected() == VoteViewModel.DEFAULT) {
                temp.setSelected(VoteViewModel.UNSELECTED);
            }
        }
        notifyItemRangeChanged(0, list.size());
    }
}
