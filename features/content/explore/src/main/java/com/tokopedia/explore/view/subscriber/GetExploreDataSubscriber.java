package com.tokopedia.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.explore.domain.entity.Category;
import com.tokopedia.explore.domain.entity.Content;
import com.tokopedia.explore.domain.entity.GetDiscoveryKolData;
import com.tokopedia.explore.domain.entity.GetExploreData;
import com.tokopedia.explore.domain.entity.PostKol;
import com.tokopedia.explore.domain.entity.Tag;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreCategoryViewModel;
import com.tokopedia.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by milhamj on 23/07/18.
 */

public class GetExploreDataSubscriber extends Subscriber<GraphqlResponse> {

    private final ContentExploreContract.View view;

    public GetExploreDataSubscriber(ContentExploreContract.View view) {
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
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        GetExploreData exploreData = graphqlResponse.getData(GetExploreData.class);
        GetDiscoveryKolData discoveryKolData = exploreData.getGetDiscoveryKolData();

        view.updateCursor(discoveryKolData.getLastCursor());
        List<KolPostViewModel> kolPostViewModelList
                = convertToKolPostViewModelList(discoveryKolData.getPostKol());
        List<ExploreCategoryViewModel> categoryViewModelList
                = convertToCategoryViewModelList(discoveryKolData.getCategories());
        view.onSuccessGetExploreData(
                new ExploreViewModel(kolPostViewModelList, categoryViewModelList)
        );
    }

    private List<KolPostViewModel> convertToKolPostViewModelList(List<PostKol> postKolList) {
        List<KolPostViewModel> kolPostViewModelList = new ArrayList<>();
        for (PostKol postKol : postKolList) {
            kolPostViewModelList.add(convertToKolPostViewModel(postKol));
        }
        return kolPostViewModelList;
    }

    private KolPostViewModel convertToKolPostViewModel(PostKol postKol) {
        Content content = getContent(postKol);
        Tag tag = getKolTag(content);

        return new KolPostViewModel(
                postKol.getUserId(),
                "",
                "",
                postKol.getUserName() == null ? "" : postKol.getUserName(),
                postKol.getUserPhoto() == null ? "" : postKol.getUserPhoto(),
                postKol.getUserInfo() == null ? "" : postKol.getUserInfo(),
                postKol.getUserUrl() == null ? "" : postKol.getUserUrl(),
                true,
                postKol.getDescription() == null ? "" : postKol.getDescription(),
                postKol.isIsLiked(),
                postKol.getLikeCount(),
                postKol.getCommentCount(),
                0,
                postKol.getId(),
                postKol.getCreateTime() == null ? "" : generateTime(postKol.getCreateTime()),
                true,
                true,
                getImageUrl(content),
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

    private List<ExploreCategoryViewModel> convertToCategoryViewModelList(
            List<Category> categoryList) {
        List<ExploreCategoryViewModel> categoryViewModelList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryViewModelList.add(convertToCategoryViewModel(category));
        }
        return categoryViewModelList;
    }

    private ExploreCategoryViewModel convertToCategoryViewModel(Category category) {
        return new ExploreCategoryViewModel(
                category.getId(),
                category.getName() == null ? "" : category.getName()
        );
    }
}
