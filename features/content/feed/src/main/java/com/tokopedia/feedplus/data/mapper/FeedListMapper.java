package com.tokopedia.feedplus.data.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.feedplus.data.pojo.ContentFeedKol;
import com.tokopedia.feedplus.data.pojo.Feed;
import com.tokopedia.feedplus.data.pojo.FeedBanner;
import com.tokopedia.feedplus.data.pojo.FeedContent;
import com.tokopedia.feedplus.data.pojo.FeedKolRecommendedType;
import com.tokopedia.feedplus.data.pojo.FeedKolType;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.feedplus.data.pojo.FeedSource;
import com.tokopedia.feedplus.data.pojo.Feeds;
import com.tokopedia.feedplus.data.pojo.FeedsFavoriteCta;
import com.tokopedia.feedplus.data.pojo.FeedsKolCta;
import com.tokopedia.feedplus.data.pojo.KolRecommendedDataType;
import com.tokopedia.feedplus.data.pojo.ProductFeedType;
import com.tokopedia.feedplus.data.pojo.ShopDetail;
import com.tokopedia.feedplus.data.pojo.TagsFeedKol;
import com.tokopedia.feedplus.data.pojo.TopAd;
import com.tokopedia.feedplus.data.pojo.Wholesale;
import com.tokopedia.feedplus.domain.model.feed.ContentFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FavoriteCtaDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.KolCtaDomain;
import com.tokopedia.feedplus.domain.model.feed.KolPostDomain;
import com.tokopedia.feedplus.domain.model.feed.KolRecommendationDomain;
import com.tokopedia.feedplus.domain.model.feed.KolRecommendationItemDomain;
import com.tokopedia.feedplus.domain.model.feed.ProductCommunicationDomain;
import com.tokopedia.feedplus.domain.model.feed.ProductFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.ShopFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.SourceFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.WholesaleDomain;
import com.tokopedia.topads.sdk.domain.model.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class FeedListMapper implements Func1<Response<GraphqlResponse<FeedQuery>>, FeedDomain> {

    private static final String ERROR_SERVER = "ERROR_SERVER";
    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    @Inject
    public FeedListMapper() {
    }

    @Override
    public FeedDomain call(Response<GraphqlResponse<FeedQuery>> feedQueryResponse) {
        Feeds data = getDataOrError(feedQueryResponse);
        return convertToDataFeedDomain(data);
    }

    private Feeds getDataOrError(Response<GraphqlResponse<FeedQuery>> feedQueryResponse) {
        if (feedQueryResponse != null
                && feedQueryResponse.body() != null
                && feedQueryResponse.body().getData() != null) {
            if (feedQueryResponse.isSuccessful()) {
                FeedQuery feedQuery = feedQueryResponse.body().getData();
                if (feedQuery.getFeed() != null) {
                    return feedQuery.getFeed();
                } else {
                    throw new RuntimeException(ERROR_SERVER);
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }

    private ProductFeedDomain createProductFeedDomain(String cursor,
                                                      ProductFeedType product,
                                                      List<WholesaleDomain> wholesaleDomains) {
        if (product == null) return null;
        return new ProductFeedDomain(
                product.getId(), product.getName(), product.getPrice(), product.getImage(),
                product.getImage_single(), wholesaleDomains, product.getFreereturns(),
                product.getPreorder(), product.getCashback(), product.getUrl(),
                product.getProductLink(), product.getWishlist(), product.getRating(),
                String.valueOf(product.getPrice_int()), cursor
        );
    }

    private WholesaleDomain createWholesaleDomain(Wholesale wholesale) {
        return new WholesaleDomain(wholesale.getQty_min_fmt());
    }

    private List<WholesaleDomain> convertToWholesaleDomain(List<Wholesale> wholesales) {
        List<WholesaleDomain> wholesaleDomains = new ArrayList<>();
        if (wholesales != null) {
            for (int i = 0; i < wholesales.size(); i++) {
                wholesaleDomains.add(createWholesaleDomain(wholesales.get(i)));
            }
        }

        return wholesaleDomains;
    }

    private List<ProductFeedDomain>
    convertToProductFeedDomain(String cursor,
                               List<ProductFeedType> products) {
        List<ProductFeedDomain> productFeedDomains = new ArrayList<>();
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {

                ProductFeedType product = products.get(i);

                List<WholesaleDomain> wholesaleDomains =
                        convertToWholesaleDomain(product.getWholesale());

                productFeedDomains.add(createProductFeedDomain(cursor, product, wholesaleDomains));
            }
        }

        return productFeedDomains;
    }

    private ShopFeedDomain createShopFeedDomain(ShopDetail shop) {
        if (shop == null) return null;
        return new ShopFeedDomain(
                shop.getId(), shop.getName(), shop.getAvatar(), shop.getIsOfficial(),
                shop.getIsGold(), shop.getUrl(), shop.getShopLink(), shop.getShareLinkDescription(),
                shop.getShareLinkURL()
        );
    }

    private ContentFeedDomain createContentFeedDomain(FeedContent content,
                                                      List<ProductFeedDomain> productFeedDomains,
                                                      KolPostDomain kolPostDomain,
                                                      KolRecommendationDomain kolRecommendations,
                                                      FavoriteCtaDomain favoriteCtaDomain,
                                                      KolCtaDomain kolCtaDomain,
                                                      List<Data> topadsData,
                                                      List<ProductCommunicationDomain>
                                                              productCommunications) {

        if (content == null) {
            return null;
        }

        return new ContentFeedDomain(
                content.getType(),
                content.getTotal_product() != null ? content.getTotal_product() : 0,
                productFeedDomains,
                null,
                null,
                null,
                null,
                topadsData,
                kolPostDomain,
                kolRecommendations,
                favoriteCtaDomain,
                kolCtaDomain,
                productCommunications,
                content.getStatus_activity()
        );
    }

    private SourceFeedDomain createSourceFeedDomain(FeedSource source,
                                                    ShopFeedDomain shopFeedDomain) {
        if (source == null) {
            return null;
        }

        return new SourceFeedDomain(source.getType(), shopFeedDomain);
    }

    private FeedDomain convertToDataFeedDomain(Feeds data) {

        return new FeedDomain(convertToFeedDomain(data),
                data.getLinks().getPagination().getHas_next_page());
    }

    private List<DataFeedDomain> convertToFeedDomain(Feeds data) {
        List<Feed> datumList = data.getData();
        List<DataFeedDomain> dataFeedDomains = new ArrayList<>();
        if (datumList != null) {
            for (int i = 0; i < datumList.size(); i++) {
                Feed datum = datumList.get(i);

                List<ProductFeedDomain> productFeedDomains =
                        convertToProductFeedDomain(
                                datum.getCursor(),
                                datum.getContent().getProducts()
                        );

                ShopFeedDomain shopFeedDomain = createShopFeedDomain(datum.getSource().getShop());

                KolPostDomain kolPostDomain = createKolPostDomain(datum);

                KolRecommendationDomain kolRecommendations
                        = convertToKolRecommendationDomain(datum.getContent().getKolrecommendation());

                FavoriteCtaDomain favoriteCta
                        = convertToFavoriteCtaDomain(datum.getContent().getFavorite_cta());

                KolCtaDomain kolCtaDomain = convertToKolCtaDomain(datum.getContent().getKol_cta());

                List<Data> topadsData = convertToTopadsDomain(datum.getContent().getTopads());

                List<ProductCommunicationDomain> productCommunications
                        = convertToProductCommunicationDomain(datum.getContent().getBanner());

                ContentFeedDomain contentFeedDomain = createContentFeedDomain(
                        datum.getContent(),
                        productFeedDomains,
                        kolPostDomain,
                        kolRecommendations,
                        favoriteCta,
                        kolCtaDomain,
                        topadsData,
                        productCommunications
                );

                SourceFeedDomain sourceFeedDomain =
                        createSourceFeedDomain(datum.getSource(), shopFeedDomain);

                dataFeedDomains.add(
                        createDataFeedDomain(datum, contentFeedDomain, sourceFeedDomain)
                );
            }
        }
        return dataFeedDomains;
    }

    private KolRecommendationDomain convertToKolRecommendationDomain(KolRecommendedDataType
                                                                             kolrecommendation) {
        if (kolrecommendation == null) {
            return null;
        }

        return new KolRecommendationDomain(
                kolrecommendation.getHeaderTitle() == null ?
                        "" : kolrecommendation.getHeaderTitle(),
                kolrecommendation.getExploreLink() == null ?
                        "" : kolrecommendation.getExploreLink(),
                kolrecommendation.getExploreText() == null ?
                        "" : kolrecommendation.getExploreText(),
                convertToListKolRecommendation(kolrecommendation.getKols())
        );

    }

    private FavoriteCtaDomain convertToFavoriteCtaDomain(FeedsFavoriteCta favoriteCta) {
        if (favoriteCta == null) {
            return null;
        }

        return new FavoriteCtaDomain(
                    favoriteCta.getTitle_id() == null ? "" : favoriteCta.getTitle_id(),
                    favoriteCta.getSubtitle_id() == null ? "" : favoriteCta.getSubtitle_id()
        );
    }

    private List<KolRecommendationItemDomain> convertToListKolRecommendation(
            List<FeedKolRecommendedType> kolrecommendation) {

        List<KolRecommendationItemDomain> list = new ArrayList<>();
        if (kolrecommendation != null) {
            for (FeedKolRecommendedType recommendation : kolrecommendation) {
                list.add(new KolRecommendationItemDomain(
                        recommendation.getUserName() == null ? "" : recommendation.getUserName(),
                        recommendation.getUserId() == null ? 0 : recommendation.getUserId(),
                        recommendation.getUserPhoto() == null ? "" : recommendation.getUserPhoto(),
                        recommendation.getIsFollowed() == null ?
                                false : recommendation.getIsFollowed(),
                        recommendation.getInfo() == null ? "" : recommendation.getInfo(),
                        recommendation.getUrl() == null ? "" : recommendation.getUrl()
                ));
            }
        }
        return list;
    }

    private KolPostDomain createKolPostDomain(Feed datum) {
        if (datum.getContent().getKolpost() != null) {
            FeedKolType kolpost = datum.getContent().getKolpost();

            ContentFeedKol content = kolpost.getContent().get(0);

            TagsFeedKol tags = content.getTags().get(0);

            return new KolPostDomain(
                    kolpost.getId() == null ? 0 : kolpost.getId(),
                    content.getImageurl() == null ? "" : content.getImageurl(),
                    kolpost.getDescription() == null ? "" : kolpost.getDescription(),
                    kolpost.getCommentCount() == null ? 0 : kolpost.getCommentCount(),
                    kolpost.getLikeCount() == null ? 0 : kolpost.getLikeCount(),
                    kolpost.getIsLiked() == null ? false : kolpost.getIsLiked(),
                    kolpost.getIsFollowed() == null ? false : kolpost.getIsFollowed(),
                    kolpost.getCreateTime() == null ? "" : kolpost.getCreateTime(),
                    tags.getPrice() == null ? "" : tags.getPrice(),
                    tags.getLink() == null ? "" : tags.getLink(),
                    tags.getUrl() == null ? "" : tags.getUrl(),
                    kolpost.getUserName() == null ? "" : kolpost.getUserName(),
                    kolpost.getUserPhoto() == null ? "" : kolpost.getUserPhoto(),
                    tags.getType() == null ? "" : tags.getType(),
                    tags.getCaption() == null ? "" : tags.getCaption(),
                    tags.getId() == null ? 0 : tags.getId(),
                    kolpost.getUserInfo() == null ? "" : kolpost.getUserInfo(),
                    kolpost.getHeaderTitle() == null ? "" : kolpost.getHeaderTitle(),
                    kolpost.getUserUrl() == null ? "" : kolpost.getUserUrl(),
                    kolpost.getUserId() == null ? 0 : kolpost.getUserId(),
                    kolpost.getShowComment(),
                    datum.getContent().getType() == null ? "" : datum.getContent().getType()
            );

        } else if (datum.getContent().getFollowedkolpost() != null) {
            FeedKolType kolpost = datum.getContent().getFollowedkolpost();

            ContentFeedKol content = kolpost.getContent().get(0);

            TagsFeedKol tags = content.getTags().get(0);

            return new KolPostDomain(
                    kolpost.getId() == null ? 0 : kolpost.getId(),
                    content.getImageurl() == null ? "" : content.getImageurl(),
                    kolpost.getDescription() == null ? "" : kolpost.getDescription(),
                    kolpost.getCommentCount() == null ? 0 : kolpost.getCommentCount(),
                    kolpost.getLikeCount() == null ? 0 : kolpost.getLikeCount(),
                    kolpost.getIsLiked() == null ? false : kolpost.getIsLiked(),
                    kolpost.getIsFollowed() == null ? false : kolpost.getIsFollowed(),
                    kolpost.getCreateTime() == null ? "" : kolpost.getCreateTime(),
                    tags.getPrice() == null ? "" : tags.getPrice(),
                    tags.getLink() == null ? "" : tags.getLink(),
                    tags.getUrl() == null ? "" : tags.getUrl(),
                    kolpost.getUserName() == null ? "" : kolpost.getUserName(),
                    kolpost.getUserPhoto() == null ? "" : kolpost.getUserPhoto(),
                    tags.getType() == null ? "" : tags.getType(),
                    tags.getCaption() == null ? "" : tags.getCaption(),
                    tags.getId() == null ? 0 : tags.getId(),
                    kolpost.getUserInfo() == null ? "" : kolpost.getUserInfo(),
                    "",
                    kolpost.getUserUrl() == null ? "" : kolpost.getUserUrl(),
                    kolpost.getUserId() == null ? 0 : kolpost.getUserId(),
                    kolpost.getShowComment(),
                    datum.getContent().getType() == null ? "" : datum.getContent().getType());
        } else {
            return null;
        }
    }

    private KolCtaDomain convertToKolCtaDomain(FeedsKolCta kolCta) {
        if (kolCta == null) {
            return null;
        }

        return new KolCtaDomain(
                kolCta.getImg_header(),
                kolCta.getClick_applink(),
                kolCta.getButton_text(),
                kolCta.getTitle(),
                kolCta.getSubtitle()
        );
    }

    private List<Data> convertToTopadsDomain(List<TopAd> topads) {
        if (topads == null) {
            return null;
        }

        List<Data> list = new ArrayList<>();
        for (TopAd topad : topads) {
            try {
                list.add(new Data(new JSONObject(new Gson().toJson(topad, TopAd.class))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private List<ProductCommunicationDomain> convertToProductCommunicationDomain(
            List<FeedBanner> bannerList) {
        if (bannerList == null || bannerList.isEmpty()) {
            return null;
        }

        List<ProductCommunicationDomain> productCommunicationDomains = new ArrayList<>();
        for (FeedBanner banner : bannerList) {
            String redirectUrl = "";

            if (!TextUtils.isEmpty(banner.getClick_applink())) {
                redirectUrl = banner.getClick_applink();
            } else if (!TextUtils.isEmpty(banner.getClick_url())) {
                redirectUrl = banner.getClick_url();
            }

            productCommunicationDomains.add(
                    new ProductCommunicationDomain(
                            banner.getImg_url() == null ? "" : banner.getImg_url(),
                            redirectUrl
                    )
            );
        }
        return productCommunicationDomains;
    }

    private DataFeedDomain createDataFeedDomain(Feed datum,
                                                ContentFeedDomain contentFeedDomain,
                                                SourceFeedDomain sourceFeedDomain) {
        return new DataFeedDomain(
                datum.getId(),
                datum.getCreate_time(),
                datum.getType(),
                datum.getCursor(),
                sourceFeedDomain,
                contentFeedDomain
        );
    }


}
