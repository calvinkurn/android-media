package com.tokopedia.feedplus.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedplus.data.pojo.ContentFeedKol;
import com.tokopedia.feedplus.data.pojo.Feed;
import com.tokopedia.feedplus.data.pojo.FeedBanner;
import com.tokopedia.feedplus.data.pojo.FeedContent;
import com.tokopedia.feedplus.data.pojo.FeedKolRecommendedType;
import com.tokopedia.feedplus.data.pojo.FeedKolType;
import com.tokopedia.feedplus.data.pojo.FeedPolling;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.feedplus.data.pojo.FeedSource;
import com.tokopedia.feedplus.data.pojo.Feeds;
import com.tokopedia.feedplus.data.pojo.FeedsFavoriteCta;
import com.tokopedia.feedplus.data.pojo.FeedsKolCta;
import com.tokopedia.feedplus.data.pojo.KolRecommendedDataType;
import com.tokopedia.feedplus.data.pojo.PollingOption;
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
import com.tokopedia.feedplus.domain.model.feed.ProductFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.ShopFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.SourceFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.WholesaleDomain;
import com.tokopedia.feedplus.view.viewmodel.kol.PollOptionViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationItemViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationViewModel;
import com.tokopedia.kol.common.util.TimeConverter;
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
    private final Context context;

    @Inject
    public FeedListMapper(@ApplicationContext Context context) {
        this.context = context;
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
                product.getImageSingle(), wholesaleDomains, product.getFreereturns(),
                product.getPreorder(), product.getCashback(), product.getUrl(),
                product.getProductLink(), product.getWishlist(), product.getRating(),
                String.valueOf(product.getPriceInt()), cursor
        );
    }

    private WholesaleDomain createWholesaleDomain(Wholesale wholesale) {
        return new WholesaleDomain(wholesale.getQtyMinFmt());
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
                                                      ProductCommunicationViewModel
                                                              productCommunicationViewModel,
                                                      PollViewModel pollViewModel) {

        if (content == null) {
            return null;
        }

        return new ContentFeedDomain(
                content.getType(),
                content.getTotalProduct() != null ? content.getTotalProduct() : 0,
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
                productCommunicationViewModel,
                pollViewModel,
                content.getStatusActivity()
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
        return new FeedDomain(
                convertToFeedDomain(data),
                data.getLinks().getPagination().getHasNextPage()
        );
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
                        = convertToFavoriteCtaDomain(datum.getContent().getFavoriteCta());

                KolCtaDomain kolCtaDomain = convertToKolCtaDomain(datum.getContent().getKolCta());

                List<Data> topadsData = convertToTopadsDomain(datum.getContent().getTopads());

                ProductCommunicationViewModel productCommunicationViewModel
                        = convertToProductCommunicationViewModel(
                        datum.getId(),
                        datum.getContent().getBanner()
                );

                PollViewModel pollViewModel
                        = convertToPollViewModel(
                                datum.getId(),
                                datum.getContent().getType(),
                                datum.getContent().getPolling()
                        );

                ContentFeedDomain contentFeedDomain = createContentFeedDomain(
                        datum.getContent(),
                        productFeedDomains,
                        kolPostDomain,
                        kolRecommendations,
                        favoriteCta,
                        kolCtaDomain,
                        topadsData,
                        productCommunicationViewModel,
                        pollViewModel
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
                    favoriteCta.getTitleId() == null ? "" : favoriteCta.getTitleId(),
                    favoriteCta.getSubtitleId() == null ? "" : favoriteCta.getSubtitleId()
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
                    content.getVideo() == null ? "" : content.getVideo(),
                    content.getYoutube() == null ? "" : content.getYoutube(),
                    content.getType() == null ? "" : content.getType(),
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
                    kolpost.getShowComment() == null ? true : kolpost.getShowComment(),
                    kolpost.getShowLike() == null ? true : kolpost.getShowLike(),
                    datum.isAllowReport(),
                    datum.getContent().getType() == null ? "" : datum.getContent().getType()
            );

        } else if (datum.getContent().getFollowedkolpost() != null) {
            FeedKolType kolpost = datum.getContent().getFollowedkolpost();

            ContentFeedKol content = kolpost.getContent().get(0);

            TagsFeedKol tags = content.getTags().get(0);
            return new KolPostDomain(
                    kolpost.getId() == null ? 0 : kolpost.getId(),
                    content.getImageurl() == null ? "" : content.getImageurl(),
                    content.getVideo() == null ? "" : content.getVideo(),
                    content.getYoutube() == null ? "" : content.getYoutube(),
                    content.getType() == null ? "" : content.getType(),
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
                    kolpost.getShowComment() == null ? true : kolpost.getShowComment(),
                    kolpost.getShowLike() == null ? true : kolpost.getShowLike(),
                    datum.isAllowReport(),
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
                kolCta.getImgHeader(),
                kolCta.getClickApplink(),
                kolCta.getButtonText(),
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

    private ProductCommunicationViewModel convertToProductCommunicationViewModel(
            String activityId,
            List<FeedBanner> bannerList) {
        if (bannerList == null || bannerList.isEmpty()) {
            return null;
        }

        List<ProductCommunicationItemViewModel> productCommunicationItems = new ArrayList<>();
        for (FeedBanner banner : bannerList) {
            String redirectUrl = "";

            if (!TextUtils.isEmpty(banner.getClickApplink())) {
                redirectUrl = banner.getClickApplink();
            } else if (!TextUtils.isEmpty(banner.getClickUrl())) {
                redirectUrl = banner.getClickUrl();
            }

            productCommunicationItems.add(
                    new ProductCommunicationItemViewModel(
                            Integer.valueOf(activityId),
                            banner.getImgUrl() == null ? "" : banner.getImgUrl(),
                            redirectUrl
                    )
            );
        }

        return new ProductCommunicationViewModel(productCommunicationItems);
    }

    private PollViewModel convertToPollViewModel(String activityId, String cardType,
                                                 FeedPolling polling) {
        if (polling == null) {
            return null;
        }

        boolean voted = polling.getIsAnswered() == null ? false : polling.getIsAnswered();

        List<PollOptionViewModel> optionViewModels = new ArrayList<>();
        for (PollingOption option: polling.getOptions()) {
            String weblink = option.getWeblink() == null ? "" : option.getWeblink();
            String redirectLink = !TextUtils.isEmpty(option.getApplink()) ?
                    option.getApplink() : weblink;

            optionViewModels.add(
                    new PollOptionViewModel(
                            String.valueOf(
                                    option.getOptionId() == null ? "" : option.getOptionId()
                            ),
                            option.getOption() == null ? "" : option.getOption(),
                            option.getImageOption() == null ? "" : option.getImageOption(),
                            redirectLink,
                            String.valueOf(
                                    option.getPercentage() == null ? 0 : option.getPercentage()
                            ),
                            checkIfSelected(
                                    voted,
                                    option.getIsSelected() == null ? false : option.getIsSelected()
                            )
                    )
            );
        }

        boolean isLiked = polling.getRelation() != null
                && polling.getRelation().getIsLiked() != null
                && polling.getRelation().getIsLiked();

        return new PollViewModel(
                polling.getUserId() == null ? 0 : polling.getUserId(),
                cardType,
                polling.getTitle() == null ? "" : polling.getTitle(),
                polling.getUserName() == null ? "" : polling.getUserName(),
                polling.getUserPhoto() == null ? "" : polling.getUserPhoto(),
                polling.getUserInfo() == null ? "" : polling.getUserInfo(),
                polling.getUserUrl() == null ? "" : polling.getUserUrl(),
                true,
                polling.getQuestion() == null ? "" : polling.getQuestion(),
                isLiked,
                polling.getLikeCount() == null ? 0 : polling.getLikeCount(),
                polling.getCommentCount() == null ? 0 : polling.getCommentCount(),
                0,
                TextUtils.isEmpty(activityId) ? 0 : Integer.valueOf(activityId),
                TimeConverter.generateTime(context,
                        polling.getCreateTime() == null ? "" : polling.getCreateTime()
                ),
                polling.getShowComment() == null ? true : polling.getShowComment(),
                polling.getShowLike() == null ? true : polling.getShowLike(),
                String.valueOf(polling.getPollId() == null ? 0 : polling.getPollId()),
                String.valueOf(polling.getTotalVoter() == null ? 0 : polling.getTotalVoter()),
                voted,
                optionViewModels
        );
    }

    private int checkIfSelected(boolean isAnswered, boolean isSelected) {
        if (isAnswered && isSelected) {
            return PollOptionViewModel.SELECTED;
        } else if (isAnswered) {
            return PollOptionViewModel.UNSELECTED;
        } else {
            return PollOptionViewModel.DEFAULT;
        }
    }

    private DataFeedDomain createDataFeedDomain(Feed datum,
                                                ContentFeedDomain contentFeedDomain,
                                                SourceFeedDomain sourceFeedDomain) {
        return new DataFeedDomain(
                datum.getId(),
                datum.getCreateTime(),
                datum.getType(),
                datum.getCursor(),
                sourceFeedDomain,
                contentFeedDomain
        );
    }


}
