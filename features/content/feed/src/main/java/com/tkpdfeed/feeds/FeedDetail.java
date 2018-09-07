package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import com.apollographql.apollo.api.internal.Utils;
import com.tkpdfeed.feeds.type.CustomType;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class FeedDetail implements Query<FeedDetail.Data, FeedDetail.Data, FeedDetail.Variables> {
  public static final String OPERATION_DEFINITION = "query FeedDetail($userID: Int!, $detailID: String!, $pageDetail: Int!, $limitDetail: Int!) {\n"
      + "  feed(userID: $userID, detailID: $detailID, pageDetail: $pageDetail, limitDetail: $limitDetail) {\n"
      + "    __typename\n"
      + "    data {\n"
      + "      __typename\n"
      + "      id\n"
      + "      create_time\n"
      + "      type\n"
      + "      cursor\n"
      + "      source {\n"
      + "        __typename\n"
      + "        type\n"
      + "        shop {\n"
      + "          __typename\n"
      + "          id\n"
      + "          name\n"
      + "          avatar\n"
      + "          isOfficial\n"
      + "          isGold\n"
      + "          url\n"
      + "          shopLink\n"
      + "          shareLinkDescription\n"
      + "          shareLinkURL\n"
      + "        }\n"
      + "      }\n"
      + "      content {\n"
      + "        __typename\n"
      + "        type\n"
      + "        total_product\n"
      + "        products {\n"
      + "          __typename\n"
      + "          id\n"
      + "          name\n"
      + "          price\n"
      + "          image\n"
      + "          wholesale {\n"
      + "            __typename\n"
      + "            qty_min_fmt\n"
      + "          }\n"
      + "          freereturns\n"
      + "          preorder\n"
      + "          cashback\n"
      + "          url\n"
      + "          productLink\n"
      + "          wishlist\n"
      + "          rating\n"
      + "        }\n"
      + "        status_activity\n"
      + "      }\n"
      + "      meta {\n"
      + "        __typename\n"
      + "        has_next_page\n"
      + "      }\n"
      + "    }\n"
      + "    token\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final FeedDetail.Variables variables;

  public FeedDetail(int userID, @Nonnull String detailID, int pageDetail, int limitDetail) {
    Utils.checkNotNull(detailID, "detailID == null");
    variables = new FeedDetail.Variables(userID, detailID, pageDetail, limitDetail);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public FeedDetail.Data wrapData(FeedDetail.Data data) {
    return data;
  }

  @Override
  public FeedDetail.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<FeedDetail.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int userID;

    private final @Nonnull String detailID;

    private final int pageDetail;

    private final int limitDetail;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int userID, @Nonnull String detailID, int pageDetail, int limitDetail) {
      this.userID = userID;
      this.detailID = detailID;
      this.pageDetail = pageDetail;
      this.limitDetail = limitDetail;
      this.valueMap.put("userID", userID);
      this.valueMap.put("detailID", detailID);
      this.valueMap.put("pageDetail", pageDetail);
      this.valueMap.put("limitDetail", limitDetail);
    }

    public int userID() {
      return userID;
    }

    public @Nonnull String detailID() {
      return detailID;
    }

    public int pageDetail() {
      return pageDetail;
    }

    public int limitDetail() {
      return limitDetail;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int userID;

    private @Nonnull String detailID;

    private int pageDetail;

    private int limitDetail;

    Builder() {
    }

    public Builder userID(int userID) {
      this.userID = userID;
      return this;
    }

    public Builder detailID(@Nonnull String detailID) {
      this.detailID = detailID;
      return this;
    }

    public Builder pageDetail(int pageDetail) {
      this.pageDetail = pageDetail;
      return this;
    }

    public Builder limitDetail(int limitDetail) {
      this.limitDetail = limitDetail;
      return this;
    }

    public FeedDetail build() {
      if (detailID == null) throw new IllegalStateException("detailID can't be null");
      return new FeedDetail(userID, detailID, pageDetail, limitDetail);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Feed feed;

    public Data(@Nullable Feed feed) {
      this.feed = feed;
    }

    public @Nullable Feed feed() {
      return this.feed;
    }

    @Override
    public String toString() {
      return "Data{"
        + "feed=" + feed
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.feed == null) ? (that.feed == null) : this.feed.equals(that.feed));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (feed == null) ? 0 : feed.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Feed.Mapper feedFieldMapper = new Feed.Mapper();

      final Field[] fields = {
        Field.forObject("feed", "feed", new UnmodifiableMapBuilder<String, Object>(4)
          .put("limitDetail", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "limitDetail")
          .build())
          .put("detailID", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "detailID")
          .build())
          .put("pageDetail", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "pageDetail")
          .build())
          .put("userID", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "userID")
          .build())
        .build(), true, new Field.ObjectReader<Feed>() {
          @Override public Feed read(final ResponseReader reader) throws IOException {
            return feedFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Feed feed = reader.read(fields[0]);
        return new Data(feed);
      }
    }

    public static class Shop {
      private final @Nullable Integer id;

      private final @Nullable String name;

      private final @Nullable String avatar;

      private final @Nullable Boolean isOfficial;

      private final @Nullable Boolean isGold;

      private final @Nullable Object url;

      private final @Nullable String shopLink;

      private final @Nullable String shareLinkDescription;

      private final @Nullable String shareLinkURL;

      public Shop(@Nullable Integer id, @Nullable String name, @Nullable String avatar,
          @Nullable Boolean isOfficial, @Nullable Boolean isGold, @Nullable Object url,
          @Nullable String shopLink, @Nullable String shareLinkDescription,
          @Nullable String shareLinkURL) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.isOfficial = isOfficial;
        this.isGold = isGold;
        this.url = url;
        this.shopLink = shopLink;
        this.shareLinkDescription = shareLinkDescription;
        this.shareLinkURL = shareLinkURL;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String avatar() {
        return this.avatar;
      }

      public @Nullable Boolean isOfficial() {
        return this.isOfficial;
      }

      public @Nullable Boolean isGold() {
        return this.isGold;
      }

      public @Nullable Object url() {
        return this.url;
      }

      public @Nullable String shopLink() {
        return this.shopLink;
      }

      public @Nullable String shareLinkDescription() {
        return this.shareLinkDescription;
      }

      public @Nullable String shareLinkURL() {
        return this.shareLinkURL;
      }

      @Override
      public String toString() {
        return "Shop{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "avatar=" + avatar + ", "
          + "isOfficial=" + isOfficial + ", "
          + "isGold=" + isGold + ", "
          + "url=" + url + ", "
          + "shopLink=" + shopLink + ", "
          + "shareLinkDescription=" + shareLinkDescription + ", "
          + "shareLinkURL=" + shareLinkURL
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Shop) {
          Shop that = (Shop) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.avatar == null) ? (that.avatar == null) : this.avatar.equals(that.avatar))
           && ((this.isOfficial == null) ? (that.isOfficial == null) : this.isOfficial.equals(that.isOfficial))
           && ((this.isGold == null) ? (that.isGold == null) : this.isGold.equals(that.isGold))
           && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
           && ((this.shopLink == null) ? (that.shopLink == null) : this.shopLink.equals(that.shopLink))
           && ((this.shareLinkDescription == null) ? (that.shareLinkDescription == null) : this.shareLinkDescription.equals(that.shareLinkDescription))
           && ((this.shareLinkURL == null) ? (that.shareLinkURL == null) : this.shareLinkURL.equals(that.shareLinkURL));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (avatar == null) ? 0 : avatar.hashCode();
        h *= 1000003;
        h ^= (isOfficial == null) ? 0 : isOfficial.hashCode();
        h *= 1000003;
        h ^= (isGold == null) ? 0 : isGold.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (shopLink == null) ? 0 : shopLink.hashCode();
        h *= 1000003;
        h ^= (shareLinkDescription == null) ? 0 : shareLinkDescription.hashCode();
        h *= 1000003;
        h ^= (shareLinkURL == null) ? 0 : shareLinkURL.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Shop> {
        final Field[] fields = {
          Field.forInt("id", "id", null, true),
          Field.forString("name", "name", null, true),
          Field.forString("avatar", "avatar", null, true),
          Field.forBoolean("isOfficial", "isOfficial", null, true),
          Field.forBoolean("isGold", "isGold", null, true),
          Field.forCustomType("url", "url", null, true, CustomType.URL),
          Field.forString("shopLink", "shopLink", null, true),
          Field.forString("shareLinkDescription", "shareLinkDescription", null, true),
          Field.forString("shareLinkURL", "shareLinkURL", null, true)
        };

        @Override
        public Shop map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String avatar = reader.read(fields[2]);
          final Boolean isOfficial = reader.read(fields[3]);
          final Boolean isGold = reader.read(fields[4]);
          final Object url = reader.read(fields[5]);
          final String shopLink = reader.read(fields[6]);
          final String shareLinkDescription = reader.read(fields[7]);
          final String shareLinkURL = reader.read(fields[8]);
          return new Shop(id, name, avatar, isOfficial, isGold, url, shopLink, shareLinkDescription, shareLinkURL);
        }
      }
    }

    public static class Source {
      private final @Nullable Integer type;

      private final @Nullable Shop shop;

      public Source(@Nullable Integer type, @Nullable Shop shop) {
        this.type = type;
        this.shop = shop;
      }

      public @Nullable Integer type() {
        return this.type;
      }

      public @Nullable Shop shop() {
        return this.shop;
      }

      @Override
      public String toString() {
        return "Source{"
          + "type=" + type + ", "
          + "shop=" + shop
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Source) {
          Source that = (Source) o;
          return ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
           && ((this.shop == null) ? (that.shop == null) : this.shop.equals(that.shop));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (shop == null) ? 0 : shop.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Source> {
        final Shop.Mapper shopFieldMapper = new Shop.Mapper();

        final Field[] fields = {
          Field.forInt("type", "type", null, true),
          Field.forObject("shop", "shop", null, true, new Field.ObjectReader<Shop>() {
            @Override public Shop read(final ResponseReader reader) throws IOException {
              return shopFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Source map(ResponseReader reader) throws IOException {
          final Integer type = reader.read(fields[0]);
          final Shop shop = reader.read(fields[1]);
          return new Source(type, shop);
        }
      }
    }

    public static class Wholesale {
      private final @Nullable String qty_min_fmt;

      public Wholesale(@Nullable String qty_min_fmt) {
        this.qty_min_fmt = qty_min_fmt;
      }

      public @Nullable String qty_min_fmt() {
        return this.qty_min_fmt;
      }

      @Override
      public String toString() {
        return "Wholesale{"
          + "qty_min_fmt=" + qty_min_fmt
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Wholesale) {
          Wholesale that = (Wholesale) o;
          return ((this.qty_min_fmt == null) ? (that.qty_min_fmt == null) : this.qty_min_fmt.equals(that.qty_min_fmt));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (qty_min_fmt == null) ? 0 : qty_min_fmt.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Wholesale> {
        final Field[] fields = {
          Field.forString("qty_min_fmt", "qty_min_fmt", null, true)
        };

        @Override
        public Wholesale map(ResponseReader reader) throws IOException {
          final String qty_min_fmt = reader.read(fields[0]);
          return new Wholesale(qty_min_fmt);
        }
      }
    }

    public static class Product {
      private final @Nullable Integer id;

      private final @Nullable String name;

      private final @Nullable String price;

      private final @Nullable String image;

      private final @Nullable List<Wholesale> wholesale;

      private final @Nullable Boolean freereturns;

      private final @Nullable Boolean preorder;

      private final @Nullable String cashback;

      private final @Nullable Object url;

      private final @Nullable String productLink;

      private final @Nullable Boolean wishlist;

      private final @Nullable Double rating;

      public Product(@Nullable Integer id, @Nullable String name, @Nullable String price,
          @Nullable String image, @Nullable List<Wholesale> wholesale,
          @Nullable Boolean freereturns, @Nullable Boolean preorder, @Nullable String cashback,
          @Nullable Object url, @Nullable String productLink, @Nullable Boolean wishlist,
          @Nullable Double rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.wholesale = wholesale;
        this.freereturns = freereturns;
        this.preorder = preorder;
        this.cashback = cashback;
        this.url = url;
        this.productLink = productLink;
        this.wishlist = wishlist;
        this.rating = rating;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String price() {
        return this.price;
      }

      public @Nullable String image() {
        return this.image;
      }

      public @Nullable List<Wholesale> wholesale() {
        return this.wholesale;
      }

      public @Nullable Boolean freereturns() {
        return this.freereturns;
      }

      public @Nullable Boolean preorder() {
        return this.preorder;
      }

      public @Nullable String cashback() {
        return this.cashback;
      }

      public @Nullable Object url() {
        return this.url;
      }

      public @Nullable String productLink() {
        return this.productLink;
      }

      public @Nullable Boolean wishlist() {
        return this.wishlist;
      }

      public @Nullable Double rating() {
        return this.rating;
      }

      @Override
      public String toString() {
        return "Product{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "price=" + price + ", "
          + "image=" + image + ", "
          + "wholesale=" + wholesale + ", "
          + "freereturns=" + freereturns + ", "
          + "preorder=" + preorder + ", "
          + "cashback=" + cashback + ", "
          + "url=" + url + ", "
          + "productLink=" + productLink + ", "
          + "wishlist=" + wishlist + ", "
          + "rating=" + rating
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Product) {
          Product that = (Product) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.price == null) ? (that.price == null) : this.price.equals(that.price))
           && ((this.image == null) ? (that.image == null) : this.image.equals(that.image))
           && ((this.wholesale == null) ? (that.wholesale == null) : this.wholesale.equals(that.wholesale))
           && ((this.freereturns == null) ? (that.freereturns == null) : this.freereturns.equals(that.freereturns))
           && ((this.preorder == null) ? (that.preorder == null) : this.preorder.equals(that.preorder))
           && ((this.cashback == null) ? (that.cashback == null) : this.cashback.equals(that.cashback))
           && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
           && ((this.productLink == null) ? (that.productLink == null) : this.productLink.equals(that.productLink))
           && ((this.wishlist == null) ? (that.wishlist == null) : this.wishlist.equals(that.wishlist))
           && ((this.rating == null) ? (that.rating == null) : this.rating.equals(that.rating));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (price == null) ? 0 : price.hashCode();
        h *= 1000003;
        h ^= (image == null) ? 0 : image.hashCode();
        h *= 1000003;
        h ^= (wholesale == null) ? 0 : wholesale.hashCode();
        h *= 1000003;
        h ^= (freereturns == null) ? 0 : freereturns.hashCode();
        h *= 1000003;
        h ^= (preorder == null) ? 0 : preorder.hashCode();
        h *= 1000003;
        h ^= (cashback == null) ? 0 : cashback.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (productLink == null) ? 0 : productLink.hashCode();
        h *= 1000003;
        h ^= (wishlist == null) ? 0 : wishlist.hashCode();
        h *= 1000003;
        h ^= (rating == null) ? 0 : rating.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Product> {
        final Wholesale.Mapper wholesaleFieldMapper = new Wholesale.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, true),
          Field.forString("name", "name", null, true),
          Field.forString("price", "price", null, true),
          Field.forString("image", "image", null, true),
          Field.forList("wholesale", "wholesale", null, true, new Field.ObjectReader<Wholesale>() {
            @Override public Wholesale read(final ResponseReader reader) throws IOException {
              return wholesaleFieldMapper.map(reader);
            }
          }),
          Field.forBoolean("freereturns", "freereturns", null, true),
          Field.forBoolean("preorder", "preorder", null, true),
          Field.forString("cashback", "cashback", null, true),
          Field.forCustomType("url", "url", null, true, CustomType.URL),
          Field.forString("productLink", "productLink", null, true),
          Field.forBoolean("wishlist", "wishlist", null, true),
          Field.forDouble("rating", "rating", null, true)
        };

        @Override
        public Product map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String price = reader.read(fields[2]);
          final String image = reader.read(fields[3]);
          final List<Wholesale> wholesale = reader.read(fields[4]);
          final Boolean freereturns = reader.read(fields[5]);
          final Boolean preorder = reader.read(fields[6]);
          final String cashback = reader.read(fields[7]);
          final Object url = reader.read(fields[8]);
          final String productLink = reader.read(fields[9]);
          final Boolean wishlist = reader.read(fields[10]);
          final Double rating = reader.read(fields[11]);
          return new Product(id, name, price, image, wholesale, freereturns, preorder, cashback, url, productLink, wishlist, rating);
        }
      }
    }

    public static class Content {
      private final @Nullable String type;

      private final @Nullable Integer total_product;

      private final @Nullable List<Product> products;

      private final @Nullable String status_activity;

      public Content(@Nullable String type, @Nullable Integer total_product,
          @Nullable List<Product> products, @Nullable String status_activity) {
        this.type = type;
        this.total_product = total_product;
        this.products = products;
        this.status_activity = status_activity;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable Integer total_product() {
        return this.total_product;
      }

      public @Nullable List<Product> products() {
        return this.products;
      }

      public @Nullable String status_activity() {
        return this.status_activity;
      }

      @Override
      public String toString() {
        return "Content{"
          + "type=" + type + ", "
          + "total_product=" + total_product + ", "
          + "products=" + products + ", "
          + "status_activity=" + status_activity
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Content) {
          Content that = (Content) o;
          return ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
           && ((this.total_product == null) ? (that.total_product == null) : this.total_product.equals(that.total_product))
           && ((this.products == null) ? (that.products == null) : this.products.equals(that.products))
           && ((this.status_activity == null) ? (that.status_activity == null) : this.status_activity.equals(that.status_activity));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (total_product == null) ? 0 : total_product.hashCode();
        h *= 1000003;
        h ^= (products == null) ? 0 : products.hashCode();
        h *= 1000003;
        h ^= (status_activity == null) ? 0 : status_activity.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Content> {
        final Product.Mapper productFieldMapper = new Product.Mapper();

        final Field[] fields = {
          Field.forString("type", "type", null, true),
          Field.forInt("total_product", "total_product", null, true),
          Field.forList("products", "products", null, true, new Field.ObjectReader<Product>() {
            @Override public Product read(final ResponseReader reader) throws IOException {
              return productFieldMapper.map(reader);
            }
          }),
          Field.forString("status_activity", "status_activity", null, true)
        };

        @Override
        public Content map(ResponseReader reader) throws IOException {
          final String type = reader.read(fields[0]);
          final Integer total_product = reader.read(fields[1]);
          final List<Product> products = reader.read(fields[2]);
          final String status_activity = reader.read(fields[3]);
          return new Content(type, total_product, products, status_activity);
        }
      }
    }

    public static class Meta {
      private final @Nullable Boolean has_next_page;

      public Meta(@Nullable Boolean has_next_page) {
        this.has_next_page = has_next_page;
      }

      public @Nullable Boolean has_next_page() {
        return this.has_next_page;
      }

      @Override
      public String toString() {
        return "Meta{"
          + "has_next_page=" + has_next_page
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Meta) {
          Meta that = (Meta) o;
          return ((this.has_next_page == null) ? (that.has_next_page == null) : this.has_next_page.equals(that.has_next_page));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (has_next_page == null) ? 0 : has_next_page.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Meta> {
        final Field[] fields = {
          Field.forBoolean("has_next_page", "has_next_page", null, true)
        };

        @Override
        public Meta map(ResponseReader reader) throws IOException {
          final Boolean has_next_page = reader.read(fields[0]);
          return new Meta(has_next_page);
        }
      }
    }

    public static class Datum {
      private final @Nullable String id;

      private final @Nullable String create_time;

      private final @Nullable String type;

      private final @Nullable String cursor;

      private final @Nullable Source source;

      private final @Nullable Content content;

      private final @Nullable Meta meta;

      public Datum(@Nullable String id, @Nullable String create_time, @Nullable String type,
          @Nullable String cursor, @Nullable Source source, @Nullable Content content,
          @Nullable Meta meta) {
        this.id = id;
        this.create_time = create_time;
        this.type = type;
        this.cursor = cursor;
        this.source = source;
        this.content = content;
        this.meta = meta;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String create_time() {
        return this.create_time;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable String cursor() {
        return this.cursor;
      }

      public @Nullable Source source() {
        return this.source;
      }

      public @Nullable Content content() {
        return this.content;
      }

      public @Nullable Meta meta() {
        return this.meta;
      }

      @Override
      public String toString() {
        return "Datum{"
          + "id=" + id + ", "
          + "create_time=" + create_time + ", "
          + "type=" + type + ", "
          + "cursor=" + cursor + ", "
          + "source=" + source + ", "
          + "content=" + content + ", "
          + "meta=" + meta
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Datum) {
          Datum that = (Datum) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.create_time == null) ? (that.create_time == null) : this.create_time.equals(that.create_time))
           && ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
           && ((this.cursor == null) ? (that.cursor == null) : this.cursor.equals(that.cursor))
           && ((this.source == null) ? (that.source == null) : this.source.equals(that.source))
           && ((this.content == null) ? (that.content == null) : this.content.equals(that.content))
           && ((this.meta == null) ? (that.meta == null) : this.meta.equals(that.meta));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (create_time == null) ? 0 : create_time.hashCode();
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (cursor == null) ? 0 : cursor.hashCode();
        h *= 1000003;
        h ^= (source == null) ? 0 : source.hashCode();
        h *= 1000003;
        h ^= (content == null) ? 0 : content.hashCode();
        h *= 1000003;
        h ^= (meta == null) ? 0 : meta.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Datum> {
        final Source.Mapper sourceFieldMapper = new Source.Mapper();

        final Content.Mapper contentFieldMapper = new Content.Mapper();

        final Meta.Mapper metaFieldMapper = new Meta.Mapper();

        final Field[] fields = {
          Field.forString("id", "id", null, true),
          Field.forString("create_time", "create_time", null, true),
          Field.forString("type", "type", null, true),
          Field.forString("cursor", "cursor", null, true),
          Field.forObject("source", "source", null, true, new Field.ObjectReader<Source>() {
            @Override public Source read(final ResponseReader reader) throws IOException {
              return sourceFieldMapper.map(reader);
            }
          }),
          Field.forObject("content", "content", null, true, new Field.ObjectReader<Content>() {
            @Override public Content read(final ResponseReader reader) throws IOException {
              return contentFieldMapper.map(reader);
            }
          }),
          Field.forObject("meta", "meta", null, true, new Field.ObjectReader<Meta>() {
            @Override public Meta read(final ResponseReader reader) throws IOException {
              return metaFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Datum map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String create_time = reader.read(fields[1]);
          final String type = reader.read(fields[2]);
          final String cursor = reader.read(fields[3]);
          final Source source = reader.read(fields[4]);
          final Content content = reader.read(fields[5]);
          final Meta meta = reader.read(fields[6]);
          return new Datum(id, create_time, type, cursor, source, content, meta);
        }
      }
    }

    public static class Feed {
      private final @Nullable List<Datum> data;

      private final @Nullable String token;

      public Feed(@Nullable List<Datum> data, @Nullable String token) {
        this.data = data;
        this.token = token;
      }

      public @Nullable List<Datum> data() {
        return this.data;
      }

      public @Nullable String token() {
        return this.token;
      }

      @Override
      public String toString() {
        return "Feed{"
          + "data=" + data + ", "
          + "token=" + token
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Feed) {
          Feed that = (Feed) o;
          return ((this.data == null) ? (that.data == null) : this.data.equals(that.data))
           && ((this.token == null) ? (that.token == null) : this.token.equals(that.token));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (data == null) ? 0 : data.hashCode();
        h *= 1000003;
        h ^= (token == null) ? 0 : token.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Feed> {
        final Datum.Mapper datumFieldMapper = new Datum.Mapper();

        final Field[] fields = {
          Field.forList("data", "data", null, true, new Field.ObjectReader<Datum>() {
            @Override public Datum read(final ResponseReader reader) throws IOException {
              return datumFieldMapper.map(reader);
            }
          }),
          Field.forString("token", "token", null, true)
        };

        @Override
        public Feed map(ResponseReader reader) throws IOException {
          final List<Datum> data = reader.read(fields[0]);
          final String token = reader.read(fields[1]);
          return new Feed(data, token);
        }
      }
    }
  }
}
