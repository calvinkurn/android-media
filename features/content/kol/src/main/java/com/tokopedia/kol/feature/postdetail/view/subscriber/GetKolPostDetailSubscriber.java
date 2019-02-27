package com.tokopedia.kol.feature.postdetail.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.comment.data.pojo.get.Comment;
import com.tokopedia.kol.feature.comment.data.pojo.get.Content;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetUserPostComment;
import com.tokopedia.kol.feature.comment.data.pojo.get.PostKol;
import com.tokopedia.kol.feature.comment.data.pojo.get.Tag;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;

/**
 * @author by milhamj on 27/07/18.
 */

public class GetKolPostDetailSubscriber extends Subscriber<GraphqlResponse> {

    private final KolPostDetailContract.View view;

    public GetKolPostDetailSubscriber(KolPostDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        view.dismissLoading();
        view.onErrorGetKolPostDetail(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        GetKolCommentData data = graphqlResponse.getData(GetKolCommentData.class);
        GetUserPostComment postComment = data.getGetUserPostComment();

        view.dismissLoading();

        if (!TextUtils.isEmpty(postComment.getError())) {
            view.onErrorGetKolPostDetail(postComment.getError());
            return;
        }

        List<Visitable> list = new ArrayList<>();
        list.add(convertToKolPostViewModel(postComment.getPostKol()));

        List<KolCommentViewModel> kolCommentViewModels
                = converToKolCommentViewModelList(postComment.getComments());
        if (postComment.getPostKol().getCommentCount() > GetKolPostDetailUseCase.DEFAULT_LIMIT) {
            list.add(addSeeAll(postComment.getPostKol()));
        }
        Collections.reverse(kolCommentViewModels);
        list.addAll(kolCommentViewModels);

        view.onSuccessGetKolPostDetail(list);
        view.stopTrace();
    }

    private KolPostViewModel convertToKolPostViewModel(PostKol postKol) {
        Content content = getContent(postKol);
        Tag tag = getKolTag(content);
        List<String> imageList = new ArrayList<>();
        imageList.add(getImageUrl(content));

        return new KolPostViewModel(
                postKol.getUserId(),
                "",
                "",
                postKol.getUserName() == null ? "" : postKol.getUserName(),
                postKol.getUserPhoto() == null ? "" : postKol.getUserPhoto(),
                postKol.getUserInfo() == null ? "" : postKol.getUserInfo(),
                postKol.getUserUrl() == null ? "" : postKol.getUserUrl(),
                postKol.isFollowed(),
                postKol.getDescription() == null ? "" : postKol.getDescription(),
                postKol.isLiked(),
                postKol.getLikeCount(),
                postKol.getCommentCount(),
                0,
                postKol.getId(),
                postKol.getCreateTime() == null ? "" : generateTime(postKol.getCreateTime()),
                true,
                true,
                imageList,
                getTagId(tag),
                "",
                getTagType(tag),
                getTagCaption(tag),
                getTagLink(tag)
        );
    }

    private String generateTime(String rawTime) {
        return TimeConverter.generateTime(view.getContext(), rawTime);
    }

    private Content getContent(PostKol postKol) {
        try {
            return postKol.getContent().get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private Tag getKolTag(Content content) {
        try {
            return content.getTags().get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private String getImageUrl(Content content) {
        if (content != null && content.getImageurl() != null) {
            return content.getImageurl();
        } else {
            return "";
        }
    }

    private String getTagCaption(Tag tag) {
        if (tag != null && tag.getCaption() != null) {
            return tag.getCaption();
        } else {
            return "";
        }
    }

    private int getTagId(Tag tag) {
        if (tag != null) {
            return tag.getId();
        } else {
            return 0;
        }
    }

    private String getTagType(Tag tag) {
        if (tag != null && tag.getType() != null) {
            return tag.getType();
        } else {
            return "";
        }
    }

    private String getTagLink(Tag tag) {
        if (tag != null && tag.getLink() != null) {
            return tag.getLink();
        } else {
            return "";
        }
    }

    private List<KolCommentViewModel> converToKolCommentViewModelList(List<Comment> commentList) {
        List<KolCommentViewModel> kolPostDetailViewModelList = new ArrayList<>();
        for (Comment comment : commentList) {
            kolPostDetailViewModelList.add(converToKolCommentViewModel(comment));
        }
        return kolPostDetailViewModelList;
    }

    private KolCommentViewModel converToKolCommentViewModel(Comment comment) {
        return new KolCommentViewModel(
                String.valueOf(comment.getId()),
                String.valueOf(comment.getUserID()),
                comment.getUserPhoto(),
                comment.getUserName(),
                comment.getComment(),
                generateTime(comment.getCreateTime()),
                comment.isKol(),
                comment.isCommentOwner()
        );
    }

    private SeeAllCommentsViewModel addSeeAll(PostKol postKol) {
        return new SeeAllCommentsViewModel(
                postKol.getId(),
                postKol.getCommentCount()
        );
    }
}
