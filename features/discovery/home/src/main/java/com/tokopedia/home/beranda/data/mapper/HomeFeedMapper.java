package com.tokopedia.home.beranda.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.Content;
import com.tokopedia.home.beranda.domain.gql.feed.Datum;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.Inspirasi;
import com.tokopedia.home.beranda.domain.gql.feed.Recommendation;
import com.tokopedia.home.beranda.domain.gql.feed.Source;
import com.tokopedia.home.beranda.domain.gql.feed.Topad;
import com.tokopedia.home.beranda.domain.model.feed.ContentFeedDomain;
import com.tokopedia.home.beranda.domain.model.feed.DataFeedDomain;
import com.tokopedia.home.beranda.domain.model.feed.FeedDomain;
import com.tokopedia.home.beranda.domain.model.feed.FeedResult;
import com.tokopedia.home.beranda.domain.model.feed.InspirationDomain;
import com.tokopedia.home.beranda.domain.model.feed.InspirationItemDomain;
import com.tokopedia.home.beranda.domain.model.feed.SourceFeedDomain;
import com.tokopedia.topads.sdk.domain.model.Data;

import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.ProductImage;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class HomeFeedMapper implements Func1<GraphqlResponse, FeedResult> {

    @Override
    public FeedResult call(GraphqlResponse graphqlResponse) {
        HomeFeedGqlResponse gqlResponse = graphqlResponse.getData(HomeFeedGqlResponse.class);
        return convertToFeedResult(gqlResponse);
    }

    private FeedResult convertToFeedResult(HomeFeedGqlResponse data) {
        FeedDomain feedDomain = convertToDataFeedDomain(data);
        return new FeedResult(feedDomain, FeedResult.SOURCE_CLOUD, feedDomain.isHasNext());
    }

    private FeedDomain convertToDataFeedDomain(HomeFeedGqlResponse data) {
        FeedDomain feedDomain = new FeedDomain(convertToFeedDomain(data),
                data.getFeed().getLinks().getPagination().isHasNextPage());
        feedDomain.setTitle(data.getFeed().getTitle());
        return feedDomain;
    }

    private List<DataFeedDomain> convertToFeedDomain(HomeFeedGqlResponse data) {
        List<Datum> datumList = data.getFeed().getData();
        List<DataFeedDomain> dataFeedDomains = new ArrayList<>();
        if (datumList != null) {
            for (int i = 0; i < datumList.size(); i++) {
                Datum datum = datumList.get(i);
                if (datum.getContent() != null) {

                    List<InspirationDomain> inspirationDomains = convertToInspirationDomain(datum
                            .getContent().getInspirasi());
                    List<Data> topAdsList = convertToTopadsDomain(datum.getContent().getTopads());
                    ContentFeedDomain contentFeedDomain = createContentFeedDomain(
                            datum.getContent(),
                            inspirationDomains,
                            topAdsList
                    );
                    SourceFeedDomain sourceFeedDomain =
                            createSourceFeedDomain(datum.getSource());
                    dataFeedDomains.add(createDataFeedDomain(datum,
                            contentFeedDomain, sourceFeedDomain));
                }
            }
        }
        return dataFeedDomains;
    }

    private List<Data> convertToTopadsDomain(List<Topad> topads) {
        List<Data> list = new ArrayList<>();
        if(topads !=null){
            for (Topad topad : topads){
                try {
                    Data data = new Data();
                    data.setProductClickUrl(topad.getProduct_click_url());
                    Product product = new Product();
                    product.setId(topad.getProduct().getId());
                    product.setName(topad.getProduct().getName());
                    product.setPriceFormat(topad.getProduct().getPrice_format());
                    ProductImage productImage = new ProductImage();
                    productImage.setM_ecs(topad.getProduct().getImage().getS_ecs());
                    productImage.setM_url(topad.getProduct().getImage().getS_url());
                    product.setImage(productImage);
                    data.setProduct(product);
                    list.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private List<InspirationDomain> convertToInspirationDomain(List<Inspirasi> inspirasi) {
        List<InspirationDomain> listInspiration = new ArrayList<>();
        if (inspirasi != null) {
            for (Inspirasi inspiration : inspirasi) {
                listInspiration.add(new InspirationDomain(
                        inspiration.getExperimentVersion(),
                        inspiration.getSource(),
                        inspiration.getTitle(),
                        inspiration.getForeignTitle(),
                        inspiration.getWidgetUrl(),
                        convertToInspirationItemDomainList(inspiration.getRecommendation())
                ));
            }
        }
        return listInspiration;
    }

    private List<InspirationItemDomain> convertToInspirationItemDomainList(List<Recommendation> recommendations) {
        List<InspirationItemDomain> listItemInspiration = new ArrayList<>();
        if (recommendations != null) {
            for (Recommendation recommendation : recommendations) {
                listItemInspiration.add(new InspirationItemDomain(
                        recommendation.getId(),
                        recommendation.getName(),
                        recommendation.getUrl(),
                        recommendation.getClickUrl(),
                        recommendation.getAppUrl(),
                        recommendation.getImageUrl(),
                        recommendation.getPrice(),
                        recommendation.getRecommendationType(),
                        recommendation.getPrice(),
                        recommendation.getCategoryBreadcrumbs()
                ));
            }
        }
        return listItemInspiration;
    }

    private ContentFeedDomain
    createContentFeedDomain(Content content,
                            List<InspirationDomain> inspirationDomains,
                            List<Data> topAdsList) {
        if (content == null) return null;
        return new ContentFeedDomain(content.getType(),
                inspirationDomains,
                topAdsList);
    }

    private SourceFeedDomain createSourceFeedDomain(Source source) {
        if (source == null) return null;
        return new SourceFeedDomain(source.getType());
    }

    private DataFeedDomain createDataFeedDomain(Datum datum,
                                                ContentFeedDomain contentFeedDomain,
                                                SourceFeedDomain sourceFeedDomain) {
        return new DataFeedDomain(datum.getId(), datum.getCreateTime(), datum.getType(), datum.getCursor(),
                sourceFeedDomain, contentFeedDomain);
    }
}