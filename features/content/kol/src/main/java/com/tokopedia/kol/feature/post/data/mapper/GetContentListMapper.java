package com.tokopedia.kol.feature.post.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.post.data.pojo.shop.Content;
import com.tokopedia.kol.feature.post.data.pojo.shop.ContentListData;
import com.tokopedia.kol.feature.post.data.pojo.shop.FeedContentPost;
import com.tokopedia.kol.feature.post.data.pojo.shop.Post;
import com.tokopedia.kol.feature.post.data.pojo.shop.Tag;
import com.tokopedia.kol.feature.post.domain.model.ContentListDomain;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by milhamj on 10/10/18.
 */
public class GetContentListMapper implements Func1<GraphqlResponse, Observable<ContentListDomain>> {

    private static final String TYPE_IMAGE = "image";
    private static final String TYPE_YOUTUBE = "youtube";

    private final Context context;

    @Inject
    GetContentListMapper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<ContentListDomain> call(GraphqlResponse graphqlResponse) {
        ContentListData data = graphqlResponse.getData(ContentListData.class);
        FeedContentPost feedContentPost;
        if (data != null && data.getFeedContentPost() != null) {
            if (TextUtils.isEmpty(data.getFeedContentPost().getError())) {
                feedContentPost = data.getFeedContentPost();
            } else {
                return Observable.error(
                        new MessageErrorException(data.getFeedContentPost().getError())
                );
            }
        } else {
            return Observable.error(
                    new MessageErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
            );
        }

        return Observable.just(
                new ContentListDomain(
                        convertToVisitableList(feedContentPost.getPosts()),
                        feedContentPost.getLastCursor()
                )
        );
    }

    private List<Visitable> convertToVisitableList(List<Post> postList) {
        List<Visitable> visitableList = new ArrayList<>();
        for (Post post : postList) {
            Visitable visitable;
            switch (getType(post)) {
                case TYPE_YOUTUBE:
                    break;
                case TYPE_IMAGE:
                default:
                    visitable = convertToKolPostViewModel(post);
                    visitableList.add(visitable);
                    break;
            }
        }
        return visitableList;
    }

    private String getType(Post post) {
        if (post.getContent() != null && !post.getContent().isEmpty()) {
            return post.getContent().get(0).getType();
        }
        return "";
    }

    private KolPostViewModel convertToKolPostViewModel(Post post) {
        List<Content> contentList = getContentList(post);
        List<String> imageList = getImageList(contentList);
        Tag tag = getTag(contentList);

        return new KolPostViewModel(
                post.getAuthor() != null ? post.getAuthor().getId() : 0,
                "",
                "",
                post.getAuthor() != null && post.getAuthor().getName() != null ? post.getAuthor()
                        .getName() : "",
                post.getAuthor() != null && post.getAuthor().getThumbnail() != null ? post
                        .getAuthor().getThumbnail() : "",
                "",
                post.getAuthor() != null && post.getAuthor().getUrl() != null ? post.getAuthor()
                        .getUrl() : "",
                true,
                post.getDescription() != null ? post.getDescription() : "",
                post.getInteraction() != null && post.getInteraction().isLiked(),
                post.getInteraction() != null ? post.getInteraction().getLikeCount() : 0,
                post.getInteraction() != null ? post.getInteraction().getCommentCount() : 0,
                0,
                post.getId(),
                post.getCreateTime() != null ?
                        TimeConverter.generateTime(context, post.getCreateTime()) : "",
                post.getInteraction() != null && post.getInteraction().isShowComment(),
                post.getInteraction() != null && post.getInteraction().isShowLike(),
                post.getInteraction() != null && post.getInteraction().isEditable(),
                post.getInteraction() != null && post.getInteraction().isDeletable(),
                imageList,
                tag != null ? tag.getId() : 0,
                "",
                tag != null && tag.getType() != null ? tag.getType() : "",
                tag != null && tag.getCaptionInd() != null ? tag.getCaptionInd() : "",
                tag != null && tag.getLink() != null ? tag.getLink() : "",
                tag != null && tag.getAffiliateTrackId() != null ? tag.getAffiliateTrackId() : "",
                post.getStatus() != null ? post.getStatus() : ""
        );
    }

    private List<Content> getContentList(Post post) {
        if (post.getContent() != null) {
            return post.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    private List<String> getImageList(List<Content> contentList) {
        List<String> imageList = new ArrayList<>();
        for (Content content : contentList) {
            imageList.add(content.getUrl());
        }
        return imageList;
    }

    private Tag getTag(List<Content> contentList) {
        if (!contentList.isEmpty()
                && contentList.get(0).getTags() != null
                && !contentList.get(0).getTags().isEmpty()) {
            return contentList.get(0).getTags().get(0);
        } else {
            return null;
        }
    }
}
