package com.tokopedia.kol.feature.postdetail.view.adapter;

import android.support.v7.util.DiffUtil;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.EmptyDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailAdapter extends BaseAdapter<KolPostDetailTypeFactory> {

    private EmptyModel emptyModel;

    public KolPostDetailAdapter(KolPostDetailTypeFactory typeFactory) {
        super(typeFactory, new ArrayList<>());
        this.emptyModel = new EmptyModel();
    }

    public void setList(List<Visitable> list) {
        this.visitables.clear();
        this.visitables.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        int itemCount = getItemCount();
        this.visitables.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    public void showEmpty() {
        this.visitables.clear();
        this.visitables.add(new EmptyDetailViewModel());
        notifyDataSetChanged();
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
