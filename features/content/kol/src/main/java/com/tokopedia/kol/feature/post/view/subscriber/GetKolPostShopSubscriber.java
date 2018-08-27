package com.tokopedia.kol.feature.post.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.common.network.GraphqlErrorException;
import com.tokopedia.kol.common.network.GraphqlErrorHandler;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.post.data.pojo.shop.Content;
import com.tokopedia.kol.feature.post.data.pojo.shop.ContentListData;
import com.tokopedia.kol.feature.post.data.pojo.shop.FeedContentPost;
import com.tokopedia.kol.feature.post.data.pojo.shop.Post;
import com.tokopedia.kol.feature.post.data.pojo.shop.Tag;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by milhamj on 23/08/18.
 */

public class GetKolPostShopSubscriber extends Subscriber<GraphqlResponse> {

    private static final String TYPE_IMAGE = "image";
    private static final String TYPE_YOUTUBE = "youtube";

    private final KolPostShopContract.View view;

    public GetKolPostShopSubscriber(KolPostShopContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
        view.onErrorGetProfileData(
                GraphqlErrorHandler.getErrorMessage(view.getContext(), e)
        );
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoading();
        ContentListData data = graphqlResponse.getData(ContentListData.class);
        FeedContentPost feedContentPost = getFeedContentPost(data);
        List<Visitable> visitables = convertToVisitableList(feedContentPost.posts);
        view.onSuccessGetKolPostShop(visitables, feedContentPost.lastCursor);
    }

    private FeedContentPost getFeedContentPost(ContentListData data) {
        if (data != null && data.feedContentPost != null) {
            if (TextUtils.isEmpty(data.feedContentPost.error)) {
                return data.feedContentPost;
            } else {
                throw new GraphqlErrorException(data.feedContentPost.error);
            }
        } else {
            throw new RuntimeException();
        }
    }

    private List<Visitable> convertToVisitableList(List<Post> postList) {
        List<Visitable> visitableList = new ArrayList<>();
        for (Post post : postList) {
            Visitable visitable;
            Content content = getContent(post);
            String type =  content != null && content.type != null ? content.type : "";
            switch (type) {
                case TYPE_IMAGE:
                    visitable = convertToKolPostViewModel(post);
                    visitableList.add(visitable);
                    break;
                case TYPE_YOUTUBE:
                default:
                    break;
            }
        }
        return visitableList;
    }

    private KolPostViewModel convertToKolPostViewModel(Post post) {
        Content content = getContent(post);
        Tag tag = getTag(content);
        return new KolPostViewModel(
                post.author != null ? post.author.id : 0,
                "",
                "",
                post.author != null && post.author.name != null ? post.author.name : "",
                post.author != null && post.author.thumbnail != null ? post.author.thumbnail : "",
                "",
                post.author != null && post.author.url != null ? post.author.url : "",
                true,
                post.description != null ? post.description : "",
                post.interaction != null && post.interaction.isLiked,
                post.interaction != null ? post.interaction.likeCount : 0,
                post.interaction != null ? post.interaction.commentCount : 0,
                0,
                post.id,
                post.createTime != null ?
                        TimeConverter.generateTime(view.getContext(), post.createTime): "",
                post.interaction != null && post.interaction.showComment,
                post.interaction != null && post.interaction.showLike,
                content != null && content.uRL != null ? content.uRL : "",
                tag != null ? tag.id : 0,
                "",
                tag != null && tag.type != null ? tag.type : "",
                tag != null && tag.captionInd != null ? tag.captionInd : "",
                tag != null && tag.link != null ? tag.link : ""
        );
    }

    private Content getContent(Post post) {
        if (post.content != null && !post.content.isEmpty()) {
            return post.content.get(0);
        } else {
            return null;
        }
    }

    private Tag getTag(Content content) {
        if (content.tags != null && !content.tags.isEmpty()) {
            return content.tags.get(0);
        } else {
            return null;
        }
    }
}
