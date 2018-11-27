package com.tokopedia.gallery.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.gallery.R;
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.gallery.viewmodel.ImageReviewListModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class GetImageReviewUseCase extends UseCase<ImageReviewListModel> {

    public static final String KEY_PRODUCT_ID = "productID";
    public static final String KEY_PAGE = "page";
    public static final String KEY_TOTAL = "total";

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    public GetImageReviewUseCase(Context context,
                                 GraphqlUseCase graphqlUseCase) {

        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<ImageReviewListModel> createObservable(RequestParams requestParams) {

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_image_review), ImageReviewGqlResponse.class, requestParams.getParameters());

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map(new Func1<GraphqlResponse, ImageReviewListModel>() {
                    @Override
                    public ImageReviewListModel call(GraphqlResponse graphqlResponse) {
                        ImageReviewGqlResponse imageReviewGqlResponse
                                = graphqlResponse.getData(ImageReviewGqlResponse.class);
                        return new ImageReviewListModel(
                                convertToImageReviewItemList(imageReviewGqlResponse),
                                imageReviewGqlResponse.getProductReviewImageListQuery().isHasNext()
                        );
                    }
        });
    }

    private List<ImageReviewItem> convertToImageReviewItemList(ImageReviewGqlResponse gqlResponse) {
        Map<Integer, ImageReviewGqlResponse.Review> reviewMap = new HashMap<>();
        Map<Integer, ImageReviewGqlResponse.Image> imageMap = new HashMap<>();

        ImageReviewGqlResponse.ProductReviewImageListQuery reviewImageListQueryResponse
                = gqlResponse.getProductReviewImageListQuery();

        for (ImageReviewGqlResponse.Image image : reviewImageListQueryResponse.getDetail().getImages()) {
            imageMap.put(image.getImageAttachmentID(), image);
        }

        for (ImageReviewGqlResponse.Review review : reviewImageListQueryResponse.getDetail().getReviews()) {
            reviewMap.put(review.getReviewId(), review);
        }

        List<ImageReviewItem> imageReviewItems = new ArrayList<>();
        for (ImageReviewGqlResponse.Item item : reviewImageListQueryResponse.getList()) {
            ImageReviewGqlResponse.Image image = imageMap.get(item.getImageID());
            ImageReviewGqlResponse.Review review = reviewMap.get(item.getReviewID());

            ImageReviewItem imageReviewItem = new ImageReviewItem();
            imageReviewItem.setReviewId(String.valueOf(item.getReviewID()));
            imageReviewItem.setImageUrlLarge(image.getUriLarge());
            imageReviewItem.setImageUrlThumbnail(image.getUriThumbnail());
            imageReviewItem.setFormattedDate(review.getTimeFormat().getDateTimeFmt1());
            imageReviewItem.setRating(review.getRating());
            imageReviewItem.setReviewerName(review.getReviewer().getFullName());
            imageReviewItems.add(imageReviewItem);
        }

        return imageReviewItems;
    }

    public static RequestParams createRequestParams(int page,
                                                    int total,
                                                    int productId) {

        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(KEY_PRODUCT_ID, productId);
        requestParams.putInt(KEY_PAGE, page);
        requestParams.putInt(KEY_TOTAL, total);
        return requestParams;
    }
}
