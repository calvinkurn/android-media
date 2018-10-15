package com.tokopedia.kol.feature.post.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.analytics.KolEventTracking;
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
        List<Visitable> visitables = convertToVisitableList(feedContentPost.getPosts());
        view.onSuccessGetKolPostShop(visitables, feedContentPost.getLastCursor());
    }

    private FeedContentPost getFeedContentPost(ContentListData data) {
        if (data != null && data.getFeedContentPost() != null) {
            if (TextUtils.isEmpty(data.getFeedContentPost().getError())) {
                return data.getFeedContentPost();
            } else {
                throw new GraphqlErrorException(data.getFeedContentPost().getError());
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
            String type =  content != null && content.getType() != null ? content.getType() : "";
            view.getAbstractionRouter().getAnalyticTracker().sendEventTracking(
                    KolEventTracking.Event.EVENT_SHOP_PAGE,
                    KolEventTracking.Category.SHOP_PAGE_FEED,
                    KolEventTracking.Action.SHOP_ITEM_IMPRESSION,
                    String.valueOf(post.getId()));
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
                post.getAuthor() != null ? post.getAuthor().getId() : 0,
                "",
                "",
                post.getAuthor() != null && post.getAuthor().getName() != null ? post.getAuthor().getName() : "",
                post.getAuthor() != null && post.getAuthor().getThumbnail() != null ? post.getAuthor().getThumbnail() : "",
                "",
                post.getAuthor() != null && post.getAuthor().getUrl() != null ? post.getAuthor().getUrl() : "",
                true,
                post.getDescription() != null ? post.getDescription() : "",
                post.getInteraction() != null && post.getInteraction().isLiked(),
                post.getInteraction() != null ? post.getInteraction().getLikeCount() : 0,
                post.getInteraction() != null ? post.getInteraction().getCommentCount() : 0,
                0,
                post.getId(),
                post.getCreateTime() != null ?
                        TimeConverter.generateTime(view.getContext(), post.getCreateTime()): "",
                post.getInteraction() != null && post.getInteraction().isShowComment(),
                post.getInteraction() != null && post.getInteraction().isShowLike(),
                content != null && content.getUrl() != null ? content.getUrl() : "",
                tag != null ? tag.getId() : 0,
                "",
                tag != null && tag.getType() != null ? tag.getType() : "",
                tag != null && tag.getCaptionInd() != null ? tag.getCaptionInd() : "",
                tag != null && tag.getLink() != null ? tag.getLink() : ""
        );
    }

    private Content getContent(Post post) {
        if (post.getContent() != null && !post.getContent().isEmpty()) {
            return post.getContent().get(0);
        } else {
            return null;
        }
    }

    private Tag getTag(Content content) {
        if (content.getTags() != null && !content.getTags().isEmpty()) {
            return content.getTags().get(0);
        } else {
            return null;
        }
    }
}
