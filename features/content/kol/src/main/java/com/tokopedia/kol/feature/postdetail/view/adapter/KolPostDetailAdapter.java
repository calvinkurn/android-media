package com.tokopedia.kol.feature.postdetail.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private KolPostDetailTypeFactory typeFactory;

    @Inject
    KolPostDetailAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(list.get(position), payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    public void setTypeFactory(KolPostDetailTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public List<Visitable> getList() {
        return list;
    }

    public void setList(List<Visitable> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        int itemCount = getItemCount();
        this.list.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    static class Callback extends DiffUtil.Callback {
        private final List<Visitable> oldList;
        private final List<Visitable> newList;

        public Callback(List<Visitable> oldList, List<Visitable> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Visitable oldItem = oldList.get(oldItemPosition);
            Visitable newItem = newList.get(newItemPosition);

            if (oldItem instanceof KolPostViewModel) {
                return newItem instanceof KolPostViewModel
                        && ((KolPostViewModel) oldItem).getContentId()
                        == ((KolPostViewModel) oldItem).getContentId();
            } else if (oldItem instanceof KolCommentViewModel) {
                return newItem instanceof KolCommentViewModel
                        && ((KolCommentViewModel) oldItem).getId()
                        .equals(((KolCommentViewModel) newItem).getId());
            }
            return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Visitable oldItem = oldList.get(oldItemPosition);
            Visitable newItem = newList.get(newItemPosition);

            if (oldItem instanceof KolPostViewModel && newItem instanceof KolPostViewModel) {
                KolPostViewModel oldPost = ((KolPostViewModel) oldItem);
                KolPostViewModel newPost = ((KolPostViewModel) newItem);

                return oldPost.getTotalLike() == newPost.getTotalLike()
                        && oldPost.getTotalComment() == newPost.getTotalComment()
                        && oldPost.isLiked() == newPost.isLiked()
                        && oldPost.isFollowed() == newPost.isFollowed()
                        && oldPost.isTemporarilyFollowed() == newPost.isTemporarilyFollowed();

            } else if (oldItem instanceof KolCommentViewModel
                    && newItem instanceof KolCommentViewModel) {
                KolCommentViewModel oldComment = (KolCommentViewModel) oldItem;
                KolCommentViewModel newComment = (KolCommentViewModel) newItem;

                return oldComment.getReview().equals(newComment.getReview())
                        && oldComment.getName().equals(newComment.getName())
                        && oldComment.getTime().equals(newComment.getTime());
            }
            return oldItem.equals(newItem);
        }
    }
}
