package com.tokopedia.feedplus.data.mapper;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tkpdfeed.feeds.FeedDetail;
import com.tokopedia.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.feedplus.domain.model.feeddetail.FeedDetailContentDomain;
import com.tokopedia.feedplus.domain.model.feeddetail.FeedDetailMetaDomain;
import com.tokopedia.feedplus.domain.model.feeddetail.FeedDetailProductDomain;
import com.tokopedia.feedplus.domain.model.feeddetail.FeedDetailShopDomain;
import com.tokopedia.feedplus.domain.model.feeddetail.FeedDetailSourceDomain;
import com.tokopedia.feedplus.domain.model.feeddetail.FeedDetailWholesaleDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailListMapper implements Func1<FeedDetail.Data, List<DataFeedDetailDomain>> {

    @Inject
    public FeedDetailListMapper() {
    }

    @Override
    public List<DataFeedDetailDomain> call(FeedDetail.Data data) {
        return convertToDataFeedDomain(data.feed().data());
    }

    private List<DataFeedDetailDomain> convertToDataFeedDomain(List<FeedDetail.Data.Datum> data) {
        List<DataFeedDetailDomain> list = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (FeedDetail.Data.Datum datum : data) {
                DataFeedDetailDomain domainData =
                        new DataFeedDetailDomain(
                                datum.id(),
                                datum.create_time(),
                                datum.type(),
                                datum.cursor(),
                                getFeedDetailSourceDomain(datum.source()),
                                getFeedDetailContentDomain(datum.content()),
                                getFeedDetailMetaDomain(datum.meta())
                        );
                list.add(domainData);
            }
        }
        return list;
    }

    private FeedDetailMetaDomain getFeedDetailMetaDomain(FeedDetail.Data.Meta meta) {
        return new FeedDetailMetaDomain(meta.has_next_page());
    }

    private FeedDetailContentDomain getFeedDetailContentDomain(FeedDetail.Data.Content content) {
        return new FeedDetailContentDomain(
                content.type(),
                content.total_product(),
                getFeedDetailProductDomainList(content.products()),
                content.status_activity());
    }

    private List<FeedDetailProductDomain> getFeedDetailProductDomainList(List<FeedDetail.Data.Product> products) {
        List<FeedDetailProductDomain> listProduct = new ArrayList<>();
        for (FeedDetail.Data.Product product : products) {
            if(productIsNotNull(product)) {
                FeedDetailProductDomain productDomain =
                        new FeedDetailProductDomain(
                                product.id(),
                                product.name(),
                                product.price(),
                                product.image(),
                                getFeedDetailWholesaleDomainList(product.wholesale()),
                                product.freereturns(),
                                product.preorder(),
                                product.cashback(),
                                product.url(),
                                product.productLink(),
                                product.wishlist(),
                                product.rating());
                listProduct.add(productDomain);
            }
        }
        return listProduct;
    }

    private boolean productIsNotNull(FeedDetail.Data.Product product) {
        return product.id() != null
                && !TextUtils.isEmpty(product.name())
                && !TextUtils.isEmpty(product.price())
                && !TextUtils.isEmpty(product.image())
                && product.wholesale() != null
                && product.freereturns() != null
                && product.preorder() != null
                && product.cashback() != null
                && product.url() != null
                && !TextUtils.isEmpty(product.productLink())
                && product.wishlist() != null
                && product.rating() != null;
    }

    private List<FeedDetailWholesaleDomain> getFeedDetailWholesaleDomainList(@Nullable List<FeedDetail
            .Data.Wholesale> wholesales) {
        List<FeedDetailWholesaleDomain> listWholesale = new ArrayList<>();

        if (wholesales != null) {
            for (FeedDetail.Data.Wholesale wholesale : wholesales) {
                FeedDetailWholesaleDomain wholesaleDomain =
                        new FeedDetailWholesaleDomain(
                                wholesale.qty_min_fmt());
                listWholesale.add(wholesaleDomain);
            }
        }

        return listWholesale;
    }

    private FeedDetailSourceDomain getFeedDetailSourceDomain(FeedDetail.Data.Source source) {
        return new FeedDetailSourceDomain(
                source.type(),
                getFeedDetailShopDomain(source.shop()));
    }

    private FeedDetailShopDomain getFeedDetailShopDomain(FeedDetail.Data.Shop shop) {
        return new FeedDetailShopDomain(
                shop.id(),
                shop.name(),
                shop.avatar(),
                shop.isOfficial(),
                shop.isGold(),
                (String) shop.url(),
                shop.shopLink(),
                shop.shareLinkDescription(),
                shop.shareLinkURL()
        );
    }
}
