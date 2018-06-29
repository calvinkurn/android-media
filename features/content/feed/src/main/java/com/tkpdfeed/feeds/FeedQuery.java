package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import com.tkpdfeed.feeds.type.CustomType;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class FeedQuery implements Query<FeedQuery.Data, FeedQuery.Data, FeedQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query FeedQuery($userID: Int!, $limit: Int!, $cursor: String, $source: String) {\n"
          + "  feed(limit: $limit, cursor: $cursor, userID: $userID, source: $source) {\n"
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
          + "        display\n"
          + "        total_product\n"
          + "        products {\n"
          + "          __typename\n"
          + "          id\n"
          + "          name\n"
          + "          price\n"
          + "          image\n"
          + "          image_single\n"
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
          + "          price_int\n"
          + "        }\n"
          + "        promotions {\n"
          + "          __typename\n"
          + "          id\n"
          + "          name\n"
          + "          type\n"
          + "          thumbnail\n"
          + "          feature_image\n"
          + "          description\n"
          + "          periode\n"
          + "          code\n"
          + "          url\n"
          + "          min_transcation\n"
          + "        }\n"
          + "        official_store {\n"
          + "          __typename\n"
          + "          shop_id\n"
          + "          shop_defaultv3_url\n"
          + "          shop_mobile_url\n"
          + "          shop_apps_url\n"
          + "          shop_name\n"
          + "          logo_url\n"
          + "          microsite_url\n"
          + "          brand_img_url\n"
          + "          is_owner\n"
          + "          shop_tagline\n"
          + "          is_new\n"
          + "          title\n"
          + "          mobile_img_url\n"
          + "          feed_hexa_color\n"
          + "          products {\n"
          + "            __typename\n"
          + "            brand_id\n"
          + "            brand_logo\n"
          + "            data {\n"
          + "              __typename\n"
          + "              id\n"
          + "              name\n"
          + "              url\n"
          + "              url_app\n"
          + "              image_url\n"
          + "              image_url_700\n"
          + "              price\n"
          + "              original_price\n"
          + "              discount_percentage\n"
          + "              discount_expired\n"
          + "              badges {\n"
          + "                __typename\n"
          + "                title\n"
          + "                image_url\n"
          + "              }\n"
          + "              labels {\n"
          + "                __typename\n"
          + "                title\n"
          + "                color\n"
          + "              }\n"
          + "              shop {\n"
          + "                __typename\n"
          + "                name\n"
          + "                url\n"
          + "                url_app\n"
          + "                location\n"
          + "              }\n"
          + "            }\n"
          + "          }\n"
          + "          redirect_url_mobile\n"
          + "          redirect_url_desktop\n"
          + "          redirect_url_app\n"
          + "        }\n"
          + "        redirect_url_app\n"
          + "        seller_story {\n"
          + "          __typename\n"
          + "          id\n"
          + "          title\n"
          + "          date\n"
          + "          link\n"
          + "          image\n"
          + "          youtube\n"
          + "        }\n"
          + "        top_picks {\n"
          + "          __typename\n"
          + "          name\n"
          + "          url\n"
          + "          image_url\n"
          + "          image_landscape_url\n"
          + "          is_parent\n"
          + "        }\n"
          + "        status_activity\n"
          + "        new_status_activity {\n"
          + "          __typename\n"
          + "          source\n"
          + "          activity\n"
          + "          amount\n"
          + "        }\n"
          + "        inspirasi {\n"
          + "          __typename\n"
          + "          experiment_version\n"
          + "          source\n"
          + "          title\n"
          + "          foreign_title\n"
          + "          widget_url\n"
          + "          pagination {\n"
          + "            __typename\n"
          + "            current_page\n"
          + "            next_page\n"
          + "            prev_page\n"
          + "          }\n"
          + "          recommendation {\n"
          + "            __typename\n"
          + "            id\n"
          + "            name\n"
          + "            url\n"
          + "            click_url\n"
          + "            app_url\n"
          + "            image_url\n"
          + "            price\n"
          + "            price_int\n"
          + "            recommendation_type\n"
          + "          }\n"
          + "        }\n"
          + "        kolpost {\n"
          + "          __typename\n"
          + "          id\n"
          + "          description\n"
          + "          commentCount\n"
          + "          likeCount\n"
          + "          isLiked\n"
          + "          isFollowed\n"
          + "          createTime\n"
          + "          showComment\n"
          + "          userName\n"
          + "          userPhoto\n"
          + "          userInfo\n"
          + "          userUrl\n"
          + "          userId\n"
          + "          headerTitle\n"
          + "          content {\n"
          + "            __typename\n"
          + "            imageurl\n"
          + "            tags {\n"
          + "              __typename\n"
          + "              id\n"
          + "              type\n"
          + "              url\n"
          + "              link\n"
          + "              price\n"
          + "              caption\n"
          + "            }\n"
          + "          }\n"
          + "        }\n"
          + "        followedkolpost {\n"
          + "          __typename\n"
          + "          id\n"
          + "          description\n"
          + "          commentCount\n"
          + "          likeCount\n"
          + "          isLiked\n"
          + "          isFollowed\n"
          + "          createTime\n"
          + "          showComment\n"
          + "          userName\n"
          + "          userPhoto\n"
          + "          userInfo\n"
          + "          userUrl\n"
          + "          userId\n"
          + "          content {\n"
          + "            __typename\n"
          + "            imageurl\n"
          + "            tags {\n"
          + "              __typename\n"
          + "              id\n"
          + "              type\n"
          + "              url\n"
          + "              link\n"
          + "              price\n"
          + "              caption\n"
          + "            }\n"
          + "          }\n"
          + "        }\n"
          + "        kolrecommendation {\n"
          + "          __typename\n"
          + "          headerTitle\n"
          + "          exploreLink\n"
          + "          exploreText\n"
          + "          kols {\n"
          + "            __typename\n"
          + "            userName\n"
          + "            userId\n"
          + "            userPhoto\n"
          + "            isFollowed\n"
          + "            info\n"
          + "            url\n"
          + "          }\n"
          + "        }\n"
          + "        favorite_cta {\n"
          + "          __typename\n"
          + "          title_en\n"
          + "          title_id\n"
          + "          subtitle_en\n"
          + "          subtitle_id\n"
          + "        }\n"
          + "        kol_cta {\n"
          + "          __typename\n"
          + "          img_header\n"
          + "          title\n"
          + "          subtitle\n"
          + "          button_text\n"
          + "          click_url\n"
          + "          click_applink\n"
          + "        }\n"
          + "        topads {\n"
          + "          __typename\n"
          + "          id\n"
          + "          ad_ref_key\n"
          + "          redirect\n"
          + "          sticker_id\n"
          + "          sticker_image\n"
          + "          product_click_url\n"
          + "          shop_click_url\n"
          + "          product {\n"
          + "            __typename\n"
          + "            id\n"
          + "            name\n"
          + "            image {\n"
          + "              __typename\n"
          + "              m_url\n"
          + "              s_url\n"
          + "              xs_url\n"
          + "              m_ecs\n"
          + "              s_ecs\n"
          + "              xs_ecs\n"
          + "            }\n"
          + "            uri\n"
          + "            relative_uri\n"
          + "            price_format\n"
          + "            count_talk_format\n"
          + "            count_review_format\n"
          + "            category {\n"
          + "              __typename\n"
          + "              id\n"
          + "            }\n"
          + "            product_preorder\n"
          + "            product_wholesale\n"
          + "            free_return\n"
          + "            product_cashback\n"
          + "            product_cashback_rate\n"
          + "            product_rating\n"
          + "          }\n"
          + "          shop {\n"
          + "            __typename\n"
          + "            id\n"
          + "            name\n"
          + "            domain\n"
          + "            tagline\n"
          + "            location\n"
          + "            city\n"
          + "            image_product {\n"
          + "              __typename\n"
          + "              product_id\n"
          + "              product_name\n"
          + "              image_url\n"
          + "            }\n"
          + "            image_shop {\n"
          + "              __typename\n"
          + "              cover_ecs\n"
          + "              s_ecs\n"
          + "              xs_ecs\n"
          + "              s_url\n"
          + "              xs_url\n"
          + "            }\n"
          + "            gold_shop\n"
          + "            lucky_shop\n"
          + "            shop_is_official\n"
          + "            owner_id\n"
          + "            is_owner\n"
          + "            badges {\n"
          + "              __typename\n"
          + "              title\n"
          + "              image_url\n"
          + "            }\n"
          + "            uri\n"
          + "            gold_shop_badge\n"
          + "          }\n"
          + "          applinks\n"
          + "        }\n"
          + "      }\n"
          + "    }\n"
          + "    links {\n"
          + "      __typename\n"
          + "      self\n"
          + "      pagination {\n"
          + "        __typename\n"
          + "        has_next_page\n"
          + "      }\n"
          + "    }\n"
          + "    meta {\n"
          + "      __typename\n"
          + "      total_data\n"
          + "    }\n"
          + "  }\n"
          + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final FeedQuery.Variables variables;

  public FeedQuery(int userID, int limit, @Nullable String cursor, @Nullable String source) {
    variables = new FeedQuery.Variables(userID, limit, cursor, source);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public FeedQuery.Data wrapData(FeedQuery.Data data) {
    return data;
  }

  @Override
  public FeedQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<FeedQuery.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int userID;

    private final int limit;

    private final @Nullable String cursor;

    private final @Nullable String source;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int userID, int limit, @Nullable String cursor, @Nullable String source) {
      this.userID = userID;
      this.limit = limit;
      this.cursor = cursor;
      this.source = source;
      this.valueMap.put("userID", userID);
      this.valueMap.put("limit", limit);
      this.valueMap.put("cursor", cursor);
      this.valueMap.put("source", source);
    }

    public int userID() {
      return userID;
    }

    public int limit() {
      return limit;
    }

    public @Nullable String cursor() {
      return cursor;
    }

    public @Nullable String source() {
      return source;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int userID;

    private int limit;

    private @Nullable String cursor;

    private @Nullable String source;

    Builder() {
    }

    public Builder userID(int userID) {
      this.userID = userID;
      return this;
    }

    public Builder limit(int limit) {
      this.limit = limit;
      return this;
    }

    public Builder cursor(@Nullable String cursor) {
      this.cursor = cursor;
      return this;
    }

    public Builder source(@Nullable String source) {
      this.source = source;
      return this;
    }

    public FeedQuery build() {
      return new FeedQuery(userID, limit, cursor, source);
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
                      .put("cursor", new UnmodifiableMapBuilder<String, Object>(2)
                              .put("kind", "Variable")
                              .put("variableName", "cursor")
                              .build())
                      .put("limit", new UnmodifiableMapBuilder<String, Object>(2)
                              .put("kind", "Variable")
                              .put("variableName", "limit")
                              .build())
                      .put("source", new UnmodifiableMapBuilder<String, Object>(2)
                              .put("kind", "Variable")
                              .put("variableName", "source")
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

      private final @Nullable String image_single;

      private final @Nullable List<Wholesale> wholesale;

      private final @Nullable Boolean freereturns;

      private final @Nullable Boolean preorder;

      private final @Nullable String cashback;

      private final @Nullable Object url;

      private final @Nullable String productLink;

      private final @Nullable Boolean wishlist;

      private final @Nullable Double rating;

      private final @Nullable Integer price_int;

      public Product(@Nullable Integer id, @Nullable String name, @Nullable String price,
                     @Nullable String image, @Nullable String image_single,
                     @Nullable List<Wholesale> wholesale, @Nullable Boolean freereturns,
                     @Nullable Boolean preorder, @Nullable String cashback, @Nullable Object url,
                     @Nullable String productLink, @Nullable Boolean wishlist, @Nullable Double rating,
                     @Nullable Integer price_int) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.image_single = image_single;
        this.wholesale = wholesale;
        this.freereturns = freereturns;
        this.preorder = preorder;
        this.cashback = cashback;
        this.url = url;
        this.productLink = productLink;
        this.wishlist = wishlist;
        this.rating = rating;
        this.price_int = price_int;
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

      public @Nullable String image_single() {
        return this.image_single;
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

      public @Nullable Integer price_int() {
        return this.price_int;
      }

      @Override
      public String toString() {
        return "Product{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "price=" + price + ", "
                + "image=" + image + ", "
                + "image_single=" + image_single + ", "
                + "wholesale=" + wholesale + ", "
                + "freereturns=" + freereturns + ", "
                + "preorder=" + preorder + ", "
                + "cashback=" + cashback + ", "
                + "url=" + url + ", "
                + "productLink=" + productLink + ", "
                + "wishlist=" + wishlist + ", "
                + "rating=" + rating + ", "
                + "price_int=" + price_int
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
                  && ((this.image_single == null) ? (that.image_single == null) : this.image_single.equals(that.image_single))
                  && ((this.wholesale == null) ? (that.wholesale == null) : this.wholesale.equals(that.wholesale))
                  && ((this.freereturns == null) ? (that.freereturns == null) : this.freereturns.equals(that.freereturns))
                  && ((this.preorder == null) ? (that.preorder == null) : this.preorder.equals(that.preorder))
                  && ((this.cashback == null) ? (that.cashback == null) : this.cashback.equals(that.cashback))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.productLink == null) ? (that.productLink == null) : this.productLink.equals(that.productLink))
                  && ((this.wishlist == null) ? (that.wishlist == null) : this.wishlist.equals(that.wishlist))
                  && ((this.rating == null) ? (that.rating == null) : this.rating.equals(that.rating))
                  && ((this.price_int == null) ? (that.price_int == null) : this.price_int.equals(that.price_int));
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
        h ^= (image_single == null) ? 0 : image_single.hashCode();
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
        h *= 1000003;
        h ^= (price_int == null) ? 0 : price_int.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Product> {
        final Wholesale.Mapper wholesaleFieldMapper = new Wholesale.Mapper();

        final Field[] fields = {
                Field.forInt("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forString("price", "price", null, true),
                Field.forString("image", "image", null, true),
                Field.forString("image_single", "image_single", null, true),
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
                Field.forDouble("rating", "rating", null, true),
                Field.forInt("price_int", "price_int", null, true)
        };

        @Override
        public Product map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String price = reader.read(fields[2]);
          final String image = reader.read(fields[3]);
          final String image_single = reader.read(fields[4]);
          final List<Wholesale> wholesale = reader.read(fields[5]);
          final Boolean freereturns = reader.read(fields[6]);
          final Boolean preorder = reader.read(fields[7]);
          final String cashback = reader.read(fields[8]);
          final Object url = reader.read(fields[9]);
          final String productLink = reader.read(fields[10]);
          final Boolean wishlist = reader.read(fields[11]);
          final Double rating = reader.read(fields[12]);
          final Integer price_int = reader.read(fields[13]);
          return new Product(id, name, price, image, image_single, wholesale, freereturns, preorder, cashback, url, productLink, wishlist, rating, price_int);
        }
      }
    }

    public static class Promotion {
      private final @Nonnull String id;

      private final @Nonnull String name;

      private final @Nonnull String type;

      private final @Nonnull String thumbnail;

      private final @Nonnull String feature_image;

      private final @Nonnull String description;

      private final @Nonnull String periode;

      private final @Nonnull String code;

      private final @Nonnull Object url;

      private final @Nonnull String min_transcation;

      public Promotion(@Nonnull String id, @Nonnull String name, @Nonnull String type,
                       @Nonnull String thumbnail, @Nonnull String feature_image, @Nonnull String description,
                       @Nonnull String periode, @Nonnull String code, @Nonnull Object url,
                       @Nonnull String min_transcation) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.thumbnail = thumbnail;
        this.feature_image = feature_image;
        this.description = description;
        this.periode = periode;
        this.code = code;
        this.url = url;
        this.min_transcation = min_transcation;
      }

      public @Nonnull String id() {
        return this.id;
      }

      public @Nonnull String name() {
        return this.name;
      }

      public @Nonnull String type() {
        return this.type;
      }

      public @Nonnull String thumbnail() {
        return this.thumbnail;
      }

      public @Nonnull String feature_image() {
        return this.feature_image;
      }

      public @Nonnull String description() {
        return this.description;
      }

      public @Nonnull String periode() {
        return this.periode;
      }

      public @Nonnull String code() {
        return this.code;
      }

      public @Nonnull Object url() {
        return this.url;
      }

      public @Nonnull String min_transcation() {
        return this.min_transcation;
      }

      @Override
      public String toString() {
        return "Promotion{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "type=" + type + ", "
                + "thumbnail=" + thumbnail + ", "
                + "feature_image=" + feature_image + ", "
                + "description=" + description + ", "
                + "periode=" + periode + ", "
                + "code=" + code + ", "
                + "url=" + url + ", "
                + "min_transcation=" + min_transcation
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Promotion) {
          Promotion that = (Promotion) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
                  && ((this.thumbnail == null) ? (that.thumbnail == null) : this.thumbnail.equals(that.thumbnail))
                  && ((this.feature_image == null) ? (that.feature_image == null) : this.feature_image.equals(that.feature_image))
                  && ((this.description == null) ? (that.description == null) : this.description.equals(that.description))
                  && ((this.periode == null) ? (that.periode == null) : this.periode.equals(that.periode))
                  && ((this.code == null) ? (that.code == null) : this.code.equals(that.code))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.min_transcation == null) ? (that.min_transcation == null) : this.min_transcation.equals(that.min_transcation));
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
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (thumbnail == null) ? 0 : thumbnail.hashCode();
        h *= 1000003;
        h ^= (feature_image == null) ? 0 : feature_image.hashCode();
        h *= 1000003;
        h ^= (description == null) ? 0 : description.hashCode();
        h *= 1000003;
        h ^= (periode == null) ? 0 : periode.hashCode();
        h *= 1000003;
        h ^= (code == null) ? 0 : code.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (min_transcation == null) ? 0 : min_transcation.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Promotion> {
        final Field[] fields = {
                Field.forString("id", "id", null, false),
                Field.forString("name", "name", null, false),
                Field.forString("type", "type", null, false),
                Field.forString("thumbnail", "thumbnail", null, false),
                Field.forString("feature_image", "feature_image", null, false),
                Field.forString("description", "description", null, false),
                Field.forString("periode", "periode", null, false),
                Field.forString("code", "code", null, false),
                Field.forCustomType("url", "url", null, false, CustomType.URL),
                Field.forString("min_transcation", "min_transcation", null, false)
        };

        @Override
        public Promotion map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String type = reader.read(fields[2]);
          final String thumbnail = reader.read(fields[3]);
          final String feature_image = reader.read(fields[4]);
          final String description = reader.read(fields[5]);
          final String periode = reader.read(fields[6]);
          final String code = reader.read(fields[7]);
          final Object url = reader.read(fields[8]);
          final String min_transcation = reader.read(fields[9]);
          return new Promotion(id, name, type, thumbnail, feature_image, description, periode, code, url, min_transcation);
        }
      }
    }

    public static class Badge {
      private final @Nonnull String title;

      private final @Nonnull Object image_url;

      public Badge(@Nonnull String title, @Nonnull Object image_url) {
        this.title = title;
        this.image_url = image_url;
      }

      public @Nonnull String title() {
        return this.title;
      }

      public @Nonnull Object image_url() {
        return this.image_url;
      }

      @Override
      public String toString() {
        return "Badge{"
                + "title=" + title + ", "
                + "image_url=" + image_url
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Badge) {
          Badge that = (Badge) o;
          return ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.image_url == null) ? (that.image_url == null) : this.image_url.equals(that.image_url));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (image_url == null) ? 0 : image_url.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Badge> {
        final Field[] fields = {
                Field.forString("title", "title", null, false),
                Field.forCustomType("image_url", "image_url", null, false, CustomType.URL)
        };

        @Override
        public Badge map(ResponseReader reader) throws IOException {
          final String title = reader.read(fields[0]);
          final Object image_url = reader.read(fields[1]);
          return new Badge(title, image_url);
        }
      }
    }

    public static class Label {
      private final @Nonnull String title;

      private final @Nonnull String color;

      public Label(@Nonnull String title, @Nonnull String color) {
        this.title = title;
        this.color = color;
      }

      public @Nonnull String title() {
        return this.title;
      }

      public @Nonnull String color() {
        return this.color;
      }

      @Override
      public String toString() {
        return "Label{"
                + "title=" + title + ", "
                + "color=" + color
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Label) {
          Label that = (Label) o;
          return ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.color == null) ? (that.color == null) : this.color.equals(that.color));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (color == null) ? 0 : color.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Label> {
        final Field[] fields = {
                Field.forString("title", "title", null, false),
                Field.forString("color", "color", null, false)
        };

        @Override
        public Label map(ResponseReader reader) throws IOException {
          final String title = reader.read(fields[0]);
          final String color = reader.read(fields[1]);
          return new Label(title, color);
        }
      }
    }

    public static class Shop1 {
      private final @Nullable String name;

      private final @Nullable String url;

      private final @Nullable String url_app;

      private final @Nullable String location;

      public Shop1(@Nullable String name, @Nullable String url, @Nullable String url_app,
                   @Nullable String location) {
        this.name = name;
        this.url = url;
        this.url_app = url_app;
        this.location = location;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String url() {
        return this.url;
      }

      public @Nullable String url_app() {
        return this.url_app;
      }

      public @Nullable String location() {
        return this.location;
      }

      @Override
      public String toString() {
        return "Shop1{"
                + "name=" + name + ", "
                + "url=" + url + ", "
                + "url_app=" + url_app + ", "
                + "location=" + location
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Shop1) {
          Shop1 that = (Shop1) o;
          return ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.url_app == null) ? (that.url_app == null) : this.url_app.equals(that.url_app))
                  && ((this.location == null) ? (that.location == null) : this.location.equals(that.location));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (url_app == null) ? 0 : url_app.hashCode();
        h *= 1000003;
        h ^= (location == null) ? 0 : location.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Shop1> {
        final Field[] fields = {
                Field.forString("name", "name", null, true),
                Field.forString("url", "url", null, true),
                Field.forString("url_app", "url_app", null, true),
                Field.forString("location", "location", null, true)
        };

        @Override
        public Shop1 map(ResponseReader reader) throws IOException {
          final String name = reader.read(fields[0]);
          final String url = reader.read(fields[1]);
          final String url_app = reader.read(fields[2]);
          final String location = reader.read(fields[3]);
          return new Shop1(name, url, url_app, location);
        }
      }
    }

    public static class Data1 {
      private final @Nullable Integer id;

      private final @Nullable String name;

      private final @Nullable String url;

      private final @Nullable String url_app;

      private final @Nullable String image_url;

      private final @Nullable String image_url_700;

      private final @Nullable String price;

      private final @Nullable String original_price;

      private final @Nullable Integer discount_percentage;

      private final @Nullable String discount_expired;

      private final @Nullable List<Badge> badges;

      private final @Nullable List<Label> labels;

      private final @Nullable Shop1 shop;

      public Data1(@Nullable Integer id, @Nullable String name, @Nullable String url,
                   @Nullable String url_app, @Nullable String image_url, @Nullable String image_url_700,
                   @Nullable String price, @Nullable String original_price,
                   @Nullable Integer discount_percentage, @Nullable String discount_expired,
                   @Nullable List<Badge> badges, @Nullable List<Label> labels, @Nullable Shop1 shop) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.url_app = url_app;
        this.image_url = image_url;
        this.image_url_700 = image_url_700;
        this.price = price;
        this.original_price = original_price;
        this.discount_percentage = discount_percentage;
        this.discount_expired = discount_expired;
        this.badges = badges;
        this.labels = labels;
        this.shop = shop;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String url() {
        return this.url;
      }

      public @Nullable String url_app() {
        return this.url_app;
      }

      public @Nullable String image_url() {
        return this.image_url;
      }

      public @Nullable String image_url_700() {
        return this.image_url_700;
      }

      public @Nullable String price() {
        return this.price;
      }

      public @Nullable String original_price() {
        return this.original_price;
      }

      public @Nullable Integer discount_percentage() {
        return this.discount_percentage;
      }

      public @Nullable String discount_expired() {
        return this.discount_expired;
      }

      public @Nullable List<Badge> badges() {
        return this.badges;
      }

      public @Nullable List<Label> labels() {
        return this.labels;
      }

      public @Nullable Shop1 shop() {
        return this.shop;
      }

      @Override
      public String toString() {
        return "Data1{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "url=" + url + ", "
                + "url_app=" + url_app + ", "
                + "image_url=" + image_url + ", "
                + "image_url_700=" + image_url_700 + ", "
                + "price=" + price + ", "
                + "original_price=" + original_price + ", "
                + "discount_percentage=" + discount_percentage + ", "
                + "discount_expired=" + discount_expired + ", "
                + "badges=" + badges + ", "
                + "labels=" + labels + ", "
                + "shop=" + shop
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Data1) {
          Data1 that = (Data1) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.url_app == null) ? (that.url_app == null) : this.url_app.equals(that.url_app))
                  && ((this.image_url == null) ? (that.image_url == null) : this.image_url.equals(that.image_url))
                  && ((this.image_url_700 == null) ? (that.image_url_700 == null) : this.image_url_700.equals(that.image_url_700))
                  && ((this.price == null) ? (that.price == null) : this.price.equals(that.price))
                  && ((this.original_price == null) ? (that.original_price == null) : this.original_price.equals(that.original_price))
                  && ((this.discount_percentage == null) ? (that.discount_percentage == null) : this.discount_percentage.equals(that.discount_percentage))
                  && ((this.discount_expired == null) ? (that.discount_expired == null) : this.discount_expired.equals(that.discount_expired))
                  && ((this.badges == null) ? (that.badges == null) : this.badges.equals(that.badges))
                  && ((this.labels == null) ? (that.labels == null) : this.labels.equals(that.labels))
                  && ((this.shop == null) ? (that.shop == null) : this.shop.equals(that.shop));
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
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (url_app == null) ? 0 : url_app.hashCode();
        h *= 1000003;
        h ^= (image_url == null) ? 0 : image_url.hashCode();
        h *= 1000003;
        h ^= (image_url_700 == null) ? 0 : image_url_700.hashCode();
        h *= 1000003;
        h ^= (price == null) ? 0 : price.hashCode();
        h *= 1000003;
        h ^= (original_price == null) ? 0 : original_price.hashCode();
        h *= 1000003;
        h ^= (discount_percentage == null) ? 0 : discount_percentage.hashCode();
        h *= 1000003;
        h ^= (discount_expired == null) ? 0 : discount_expired.hashCode();
        h *= 1000003;
        h ^= (badges == null) ? 0 : badges.hashCode();
        h *= 1000003;
        h ^= (labels == null) ? 0 : labels.hashCode();
        h *= 1000003;
        h ^= (shop == null) ? 0 : shop.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Data1> {
        final Badge.Mapper badgeFieldMapper = new Badge.Mapper();

        final Label.Mapper labelFieldMapper = new Label.Mapper();

        final Shop1.Mapper shop1FieldMapper = new Shop1.Mapper();

        final Field[] fields = {
                Field.forInt("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forString("url", "url", null, true),
                Field.forString("url_app", "url_app", null, true),
                Field.forString("image_url", "image_url", null, true),
                Field.forString("image_url_700", "image_url_700", null, true),
                Field.forString("price", "price", null, true),
                Field.forString("original_price", "original_price", null, true),
                Field.forInt("discount_percentage", "discount_percentage", null, true),
                Field.forString("discount_expired", "discount_expired", null, true),
                Field.forList("badges", "badges", null, true, new Field.ObjectReader<Badge>() {
                  @Override public Badge read(final ResponseReader reader) throws IOException {
                    return badgeFieldMapper.map(reader);
                  }
                }),
                Field.forList("labels", "labels", null, true, new Field.ObjectReader<Label>() {
                  @Override public Label read(final ResponseReader reader) throws IOException {
                    return labelFieldMapper.map(reader);
                  }
                }),
                Field.forObject("shop", "shop", null, true, new Field.ObjectReader<Shop1>() {
                  @Override public Shop1 read(final ResponseReader reader) throws IOException {
                    return shop1FieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Data1 map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String url = reader.read(fields[2]);
          final String url_app = reader.read(fields[3]);
          final String image_url = reader.read(fields[4]);
          final String image_url_700 = reader.read(fields[5]);
          final String price = reader.read(fields[6]);
          final String original_price = reader.read(fields[7]);
          final Integer discount_percentage = reader.read(fields[8]);
          final String discount_expired = reader.read(fields[9]);
          final List<Badge> badges = reader.read(fields[10]);
          final List<Label> labels = reader.read(fields[11]);
          final Shop1 shop = reader.read(fields[12]);
          return new Data1(id, name, url, url_app, image_url, image_url_700, price, original_price, discount_percentage, discount_expired, badges, labels, shop);
        }
      }
    }

    public static class Product1 {
      private final @Nullable Integer brand_id;

      private final @Nullable String brand_logo;

      private final @Nullable Data1 data;

      public Product1(@Nullable Integer brand_id, @Nullable String brand_logo,
                      @Nullable Data1 data) {
        this.brand_id = brand_id;
        this.brand_logo = brand_logo;
        this.data = data;
      }

      public @Nullable Integer brand_id() {
        return this.brand_id;
      }

      public @Nullable String brand_logo() {
        return this.brand_logo;
      }

      public @Nullable Data1 data() {
        return this.data;
      }

      @Override
      public String toString() {
        return "Product1{"
                + "brand_id=" + brand_id + ", "
                + "brand_logo=" + brand_logo + ", "
                + "data=" + data
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Product1) {
          Product1 that = (Product1) o;
          return ((this.brand_id == null) ? (that.brand_id == null) : this.brand_id.equals(that.brand_id))
                  && ((this.brand_logo == null) ? (that.brand_logo == null) : this.brand_logo.equals(that.brand_logo))
                  && ((this.data == null) ? (that.data == null) : this.data.equals(that.data));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (brand_id == null) ? 0 : brand_id.hashCode();
        h *= 1000003;
        h ^= (brand_logo == null) ? 0 : brand_logo.hashCode();
        h *= 1000003;
        h ^= (data == null) ? 0 : data.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Product1> {
        final Data1.Mapper data1FieldMapper = new Data1.Mapper();

        final Field[] fields = {
                Field.forInt("brand_id", "brand_id", null, true),
                Field.forString("brand_logo", "brand_logo", null, true),
                Field.forObject("data", "data", null, true, new Field.ObjectReader<Data1>() {
                  @Override public Data1 read(final ResponseReader reader) throws IOException {
                    return data1FieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Product1 map(ResponseReader reader) throws IOException {
          final Integer brand_id = reader.read(fields[0]);
          final String brand_logo = reader.read(fields[1]);
          final Data1 data = reader.read(fields[2]);
          return new Product1(brand_id, brand_logo, data);
        }
      }
    }

    public static class Official_store {
      private final @Nullable Integer shop_id;

      private final @Nullable String shop_defaultv3_url;

      private final @Nullable String shop_mobile_url;

      private final @Nullable String shop_apps_url;

      private final @Nullable String shop_name;

      private final @Nullable String logo_url;

      private final @Nullable String microsite_url;

      private final @Nullable String brand_img_url;

      private final @Nullable Boolean is_owner;

      private final @Nullable String shop_tagline;

      private final @Nullable Boolean is_new;

      private final @Nullable String title;

      private final @Nullable String mobile_img_url;

      private final @Nullable String feed_hexa_color;

      private final @Nullable List<Product1> products;

      private final @Nullable String redirect_url_mobile;

      private final @Nullable String redirect_url_desktop;

      private final @Nullable String redirect_url_app;

      public Official_store(@Nullable Integer shop_id, @Nullable String shop_defaultv3_url,
                            @Nullable String shop_mobile_url, @Nullable String shop_apps_url,
                            @Nullable String shop_name, @Nullable String logo_url, @Nullable String microsite_url,
                            @Nullable String brand_img_url, @Nullable Boolean is_owner, @Nullable String shop_tagline,
                            @Nullable Boolean is_new, @Nullable String title, @Nullable String mobile_img_url,
                            @Nullable String feed_hexa_color, @Nullable List<Product1> products,
                            @Nullable String redirect_url_mobile, @Nullable String redirect_url_desktop,
                            @Nullable String redirect_url_app) {
        this.shop_id = shop_id;
        this.shop_defaultv3_url = shop_defaultv3_url;
        this.shop_mobile_url = shop_mobile_url;
        this.shop_apps_url = shop_apps_url;
        this.shop_name = shop_name;
        this.logo_url = logo_url;
        this.microsite_url = microsite_url;
        this.brand_img_url = brand_img_url;
        this.is_owner = is_owner;
        this.shop_tagline = shop_tagline;
        this.is_new = is_new;
        this.title = title;
        this.mobile_img_url = mobile_img_url;
        this.feed_hexa_color = feed_hexa_color;
        this.products = products;
        this.redirect_url_mobile = redirect_url_mobile;
        this.redirect_url_desktop = redirect_url_desktop;
        this.redirect_url_app = redirect_url_app;
      }

      public @Nullable Integer shop_id() {
        return this.shop_id;
      }

      public @Nullable String shop_defaultv3_url() {
        return this.shop_defaultv3_url;
      }

      public @Nullable String shop_mobile_url() {
        return this.shop_mobile_url;
      }

      public @Nullable String shop_apps_url() {
        return this.shop_apps_url;
      }

      public @Nullable String shop_name() {
        return this.shop_name;
      }

      public @Nullable String logo_url() {
        return this.logo_url;
      }

      public @Nullable String microsite_url() {
        return this.microsite_url;
      }

      public @Nullable String brand_img_url() {
        return this.brand_img_url;
      }

      public @Nullable Boolean is_owner() {
        return this.is_owner;
      }

      public @Nullable String shop_tagline() {
        return this.shop_tagline;
      }

      public @Nullable Boolean is_new() {
        return this.is_new;
      }

      public @Nullable String title() {
        return this.title;
      }

      public @Nullable String mobile_img_url() {
        return this.mobile_img_url;
      }

      public @Nullable String feed_hexa_color() {
        return this.feed_hexa_color;
      }

      public @Nullable List<Product1> products() {
        return this.products;
      }

      public @Nullable String redirect_url_mobile() {
        return this.redirect_url_mobile;
      }

      public @Nullable String redirect_url_desktop() {
        return this.redirect_url_desktop;
      }

      public @Nullable String redirect_url_app() {
        return this.redirect_url_app;
      }

      @Override
      public String toString() {
        return "Official_store{"
                + "shop_id=" + shop_id + ", "
                + "shop_defaultv3_url=" + shop_defaultv3_url + ", "
                + "shop_mobile_url=" + shop_mobile_url + ", "
                + "shop_apps_url=" + shop_apps_url + ", "
                + "shop_name=" + shop_name + ", "
                + "logo_url=" + logo_url + ", "
                + "microsite_url=" + microsite_url + ", "
                + "brand_img_url=" + brand_img_url + ", "
                + "is_owner=" + is_owner + ", "
                + "shop_tagline=" + shop_tagline + ", "
                + "is_new=" + is_new + ", "
                + "title=" + title + ", "
                + "mobile_img_url=" + mobile_img_url + ", "
                + "feed_hexa_color=" + feed_hexa_color + ", "
                + "products=" + products + ", "
                + "redirect_url_mobile=" + redirect_url_mobile + ", "
                + "redirect_url_desktop=" + redirect_url_desktop + ", "
                + "redirect_url_app=" + redirect_url_app
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Official_store) {
          Official_store that = (Official_store) o;
          return ((this.shop_id == null) ? (that.shop_id == null) : this.shop_id.equals(that.shop_id))
                  && ((this.shop_defaultv3_url == null) ? (that.shop_defaultv3_url == null) : this.shop_defaultv3_url.equals(that.shop_defaultv3_url))
                  && ((this.shop_mobile_url == null) ? (that.shop_mobile_url == null) : this.shop_mobile_url.equals(that.shop_mobile_url))
                  && ((this.shop_apps_url == null) ? (that.shop_apps_url == null) : this.shop_apps_url.equals(that.shop_apps_url))
                  && ((this.shop_name == null) ? (that.shop_name == null) : this.shop_name.equals(that.shop_name))
                  && ((this.logo_url == null) ? (that.logo_url == null) : this.logo_url.equals(that.logo_url))
                  && ((this.microsite_url == null) ? (that.microsite_url == null) : this.microsite_url.equals(that.microsite_url))
                  && ((this.brand_img_url == null) ? (that.brand_img_url == null) : this.brand_img_url.equals(that.brand_img_url))
                  && ((this.is_owner == null) ? (that.is_owner == null) : this.is_owner.equals(that.is_owner))
                  && ((this.shop_tagline == null) ? (that.shop_tagline == null) : this.shop_tagline.equals(that.shop_tagline))
                  && ((this.is_new == null) ? (that.is_new == null) : this.is_new.equals(that.is_new))
                  && ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.mobile_img_url == null) ? (that.mobile_img_url == null) : this.mobile_img_url.equals(that.mobile_img_url))
                  && ((this.feed_hexa_color == null) ? (that.feed_hexa_color == null) : this.feed_hexa_color.equals(that.feed_hexa_color))
                  && ((this.products == null) ? (that.products == null) : this.products.equals(that.products))
                  && ((this.redirect_url_mobile == null) ? (that.redirect_url_mobile == null) : this.redirect_url_mobile.equals(that.redirect_url_mobile))
                  && ((this.redirect_url_desktop == null) ? (that.redirect_url_desktop == null) : this.redirect_url_desktop.equals(that.redirect_url_desktop))
                  && ((this.redirect_url_app == null) ? (that.redirect_url_app == null) : this.redirect_url_app.equals(that.redirect_url_app));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (shop_id == null) ? 0 : shop_id.hashCode();
        h *= 1000003;
        h ^= (shop_defaultv3_url == null) ? 0 : shop_defaultv3_url.hashCode();
        h *= 1000003;
        h ^= (shop_mobile_url == null) ? 0 : shop_mobile_url.hashCode();
        h *= 1000003;
        h ^= (shop_apps_url == null) ? 0 : shop_apps_url.hashCode();
        h *= 1000003;
        h ^= (shop_name == null) ? 0 : shop_name.hashCode();
        h *= 1000003;
        h ^= (logo_url == null) ? 0 : logo_url.hashCode();
        h *= 1000003;
        h ^= (microsite_url == null) ? 0 : microsite_url.hashCode();
        h *= 1000003;
        h ^= (brand_img_url == null) ? 0 : brand_img_url.hashCode();
        h *= 1000003;
        h ^= (is_owner == null) ? 0 : is_owner.hashCode();
        h *= 1000003;
        h ^= (shop_tagline == null) ? 0 : shop_tagline.hashCode();
        h *= 1000003;
        h ^= (is_new == null) ? 0 : is_new.hashCode();
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (mobile_img_url == null) ? 0 : mobile_img_url.hashCode();
        h *= 1000003;
        h ^= (feed_hexa_color == null) ? 0 : feed_hexa_color.hashCode();
        h *= 1000003;
        h ^= (products == null) ? 0 : products.hashCode();
        h *= 1000003;
        h ^= (redirect_url_mobile == null) ? 0 : redirect_url_mobile.hashCode();
        h *= 1000003;
        h ^= (redirect_url_desktop == null) ? 0 : redirect_url_desktop.hashCode();
        h *= 1000003;
        h ^= (redirect_url_app == null) ? 0 : redirect_url_app.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Official_store> {
        final Product1.Mapper product1FieldMapper = new Product1.Mapper();

        final Field[] fields = {
                Field.forInt("shop_id", "shop_id", null, true),
                Field.forString("shop_defaultv3_url", "shop_defaultv3_url", null, true),
                Field.forString("shop_mobile_url", "shop_mobile_url", null, true),
                Field.forString("shop_apps_url", "shop_apps_url", null, true),
                Field.forString("shop_name", "shop_name", null, true),
                Field.forString("logo_url", "logo_url", null, true),
                Field.forString("microsite_url", "microsite_url", null, true),
                Field.forString("brand_img_url", "brand_img_url", null, true),
                Field.forBoolean("is_owner", "is_owner", null, true),
                Field.forString("shop_tagline", "shop_tagline", null, true),
                Field.forBoolean("is_new", "is_new", null, true),
                Field.forString("title", "title", null, true),
                Field.forString("mobile_img_url", "mobile_img_url", null, true),
                Field.forString("feed_hexa_color", "feed_hexa_color", null, true),
                Field.forList("products", "products", null, true, new Field.ObjectReader<Product1>() {
                  @Override public Product1 read(final ResponseReader reader) throws IOException {
                    return product1FieldMapper.map(reader);
                  }
                }),
                Field.forString("redirect_url_mobile", "redirect_url_mobile", null, true),
                Field.forString("redirect_url_desktop", "redirect_url_desktop", null, true),
                Field.forString("redirect_url_app", "redirect_url_app", null, true)
        };

        @Override
        public Official_store map(ResponseReader reader) throws IOException {
          final Integer shop_id = reader.read(fields[0]);
          final String shop_defaultv3_url = reader.read(fields[1]);
          final String shop_mobile_url = reader.read(fields[2]);
          final String shop_apps_url = reader.read(fields[3]);
          final String shop_name = reader.read(fields[4]);
          final String logo_url = reader.read(fields[5]);
          final String microsite_url = reader.read(fields[6]);
          final String brand_img_url = reader.read(fields[7]);
          final Boolean is_owner = reader.read(fields[8]);
          final String shop_tagline = reader.read(fields[9]);
          final Boolean is_new = reader.read(fields[10]);
          final String title = reader.read(fields[11]);
          final String mobile_img_url = reader.read(fields[12]);
          final String feed_hexa_color = reader.read(fields[13]);
          final List<Product1> products = reader.read(fields[14]);
          final String redirect_url_mobile = reader.read(fields[15]);
          final String redirect_url_desktop = reader.read(fields[16]);
          final String redirect_url_app = reader.read(fields[17]);
          return new Official_store(shop_id, shop_defaultv3_url, shop_mobile_url, shop_apps_url, shop_name, logo_url, microsite_url, brand_img_url, is_owner, shop_tagline, is_new, title, mobile_img_url, feed_hexa_color, products, redirect_url_mobile, redirect_url_desktop, redirect_url_app);
        }
      }
    }

    public static class Seller_story {
      private final @Nullable String id;

      private final @Nullable String title;

      private final @Nullable String date;

      private final @Nullable String link;

      private final @Nullable String image;

      private final @Nullable String youtube;

      public Seller_story(@Nullable String id, @Nullable String title, @Nullable String date,
                          @Nullable String link, @Nullable String image, @Nullable String youtube) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.link = link;
        this.image = image;
        this.youtube = youtube;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String title() {
        return this.title;
      }

      public @Nullable String date() {
        return this.date;
      }

      public @Nullable String link() {
        return this.link;
      }

      public @Nullable String image() {
        return this.image;
      }

      public @Nullable String youtube() {
        return this.youtube;
      }

      @Override
      public String toString() {
        return "Seller_story{"
                + "id=" + id + ", "
                + "title=" + title + ", "
                + "date=" + date + ", "
                + "link=" + link + ", "
                + "image=" + image + ", "
                + "youtube=" + youtube
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Seller_story) {
          Seller_story that = (Seller_story) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.date == null) ? (that.date == null) : this.date.equals(that.date))
                  && ((this.link == null) ? (that.link == null) : this.link.equals(that.link))
                  && ((this.image == null) ? (that.image == null) : this.image.equals(that.image))
                  && ((this.youtube == null) ? (that.youtube == null) : this.youtube.equals(that.youtube));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (date == null) ? 0 : date.hashCode();
        h *= 1000003;
        h ^= (link == null) ? 0 : link.hashCode();
        h *= 1000003;
        h ^= (image == null) ? 0 : image.hashCode();
        h *= 1000003;
        h ^= (youtube == null) ? 0 : youtube.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Seller_story> {
        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("title", "title", null, true),
                Field.forString("date", "date", null, true),
                Field.forString("link", "link", null, true),
                Field.forString("image", "image", null, true),
                Field.forString("youtube", "youtube", null, true)
        };

        @Override
        public Seller_story map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String title = reader.read(fields[1]);
          final String date = reader.read(fields[2]);
          final String link = reader.read(fields[3]);
          final String image = reader.read(fields[4]);
          final String youtube = reader.read(fields[5]);
          return new Seller_story(id, title, date, link, image, youtube);
        }
      }
    }

    public static class Top_pick {
      private final @Nullable String name;

      private final @Nullable String url;

      private final @Nullable String image_url;

      private final @Nullable String image_landscape_url;

      private final @Nullable Boolean is_parent;

      public Top_pick(@Nullable String name, @Nullable String url, @Nullable String image_url,
                      @Nullable String image_landscape_url, @Nullable Boolean is_parent) {
        this.name = name;
        this.url = url;
        this.image_url = image_url;
        this.image_landscape_url = image_landscape_url;
        this.is_parent = is_parent;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String url() {
        return this.url;
      }

      public @Nullable String image_url() {
        return this.image_url;
      }

      public @Nullable String image_landscape_url() {
        return this.image_landscape_url;
      }

      public @Nullable Boolean is_parent() {
        return this.is_parent;
      }

      @Override
      public String toString() {
        return "Top_pick{"
                + "name=" + name + ", "
                + "url=" + url + ", "
                + "image_url=" + image_url + ", "
                + "image_landscape_url=" + image_landscape_url + ", "
                + "is_parent=" + is_parent
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Top_pick) {
          Top_pick that = (Top_pick) o;
          return ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.image_url == null) ? (that.image_url == null) : this.image_url.equals(that.image_url))
                  && ((this.image_landscape_url == null) ? (that.image_landscape_url == null) : this.image_landscape_url.equals(that.image_landscape_url))
                  && ((this.is_parent == null) ? (that.is_parent == null) : this.is_parent.equals(that.is_parent));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (image_url == null) ? 0 : image_url.hashCode();
        h *= 1000003;
        h ^= (image_landscape_url == null) ? 0 : image_landscape_url.hashCode();
        h *= 1000003;
        h ^= (is_parent == null) ? 0 : is_parent.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Top_pick> {
        final Field[] fields = {
                Field.forString("name", "name", null, true),
                Field.forString("url", "url", null, true),
                Field.forString("image_url", "image_url", null, true),
                Field.forString("image_landscape_url", "image_landscape_url", null, true),
                Field.forBoolean("is_parent", "is_parent", null, true)
        };

        @Override
        public Top_pick map(ResponseReader reader) throws IOException {
          final String name = reader.read(fields[0]);
          final String url = reader.read(fields[1]);
          final String image_url = reader.read(fields[2]);
          final String image_landscape_url = reader.read(fields[3]);
          final Boolean is_parent = reader.read(fields[4]);
          return new Top_pick(name, url, image_url, image_landscape_url, is_parent);
        }
      }
    }

    public static class New_status_activity {
      private final @Nullable String source;

      private final @Nullable String activity;

      private final @Nullable Integer amount;

      public New_status_activity(@Nullable String source, @Nullable String activity,
                                 @Nullable Integer amount) {
        this.source = source;
        this.activity = activity;
        this.amount = amount;
      }

      public @Nullable String source() {
        return this.source;
      }

      public @Nullable String activity() {
        return this.activity;
      }

      public @Nullable Integer amount() {
        return this.amount;
      }

      @Override
      public String toString() {
        return "New_status_activity{"
                + "source=" + source + ", "
                + "activity=" + activity + ", "
                + "amount=" + amount
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof New_status_activity) {
          New_status_activity that = (New_status_activity) o;
          return ((this.source == null) ? (that.source == null) : this.source.equals(that.source))
                  && ((this.activity == null) ? (that.activity == null) : this.activity.equals(that.activity))
                  && ((this.amount == null) ? (that.amount == null) : this.amount.equals(that.amount));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (source == null) ? 0 : source.hashCode();
        h *= 1000003;
        h ^= (activity == null) ? 0 : activity.hashCode();
        h *= 1000003;
        h ^= (amount == null) ? 0 : amount.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<New_status_activity> {
        final Field[] fields = {
                Field.forString("source", "source", null, true),
                Field.forString("activity", "activity", null, true),
                Field.forInt("amount", "amount", null, true)
        };

        @Override
        public New_status_activity map(ResponseReader reader) throws IOException {
          final String source = reader.read(fields[0]);
          final String activity = reader.read(fields[1]);
          final Integer amount = reader.read(fields[2]);
          return new New_status_activity(source, activity, amount);
        }
      }
    }

    public static class Pagination {
      private final @Nullable Integer current_page;

      private final @Nullable Integer next_page;

      private final @Nullable Integer prev_page;

      public Pagination(@Nullable Integer current_page, @Nullable Integer next_page,
                        @Nullable Integer prev_page) {
        this.current_page = current_page;
        this.next_page = next_page;
        this.prev_page = prev_page;
      }

      public @Nullable Integer current_page() {
        return this.current_page;
      }

      public @Nullable Integer next_page() {
        return this.next_page;
      }

      public @Nullable Integer prev_page() {
        return this.prev_page;
      }

      @Override
      public String toString() {
        return "Pagination{"
                + "current_page=" + current_page + ", "
                + "next_page=" + next_page + ", "
                + "prev_page=" + prev_page
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Pagination) {
          Pagination that = (Pagination) o;
          return ((this.current_page == null) ? (that.current_page == null) : this.current_page.equals(that.current_page))
                  && ((this.next_page == null) ? (that.next_page == null) : this.next_page.equals(that.next_page))
                  && ((this.prev_page == null) ? (that.prev_page == null) : this.prev_page.equals(that.prev_page));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (current_page == null) ? 0 : current_page.hashCode();
        h *= 1000003;
        h ^= (next_page == null) ? 0 : next_page.hashCode();
        h *= 1000003;
        h ^= (prev_page == null) ? 0 : prev_page.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Pagination> {
        final Field[] fields = {
                Field.forInt("current_page", "current_page", null, true),
                Field.forInt("next_page", "next_page", null, true),
                Field.forInt("prev_page", "prev_page", null, true)
        };

        @Override
        public Pagination map(ResponseReader reader) throws IOException {
          final Integer current_page = reader.read(fields[0]);
          final Integer next_page = reader.read(fields[1]);
          final Integer prev_page = reader.read(fields[2]);
          return new Pagination(current_page, next_page, prev_page);
        }
      }
    }

    public static class Recommendation {
      private final @Nullable String id;

      private final @Nullable String name;

      private final @Nullable Object url;

      private final @Nullable String click_url;

      private final @Nullable String app_url;

      private final @Nullable Object image_url;

      private final @Nullable String price;

      private final @Nullable Integer price_int;

      private final @Nullable String recommendation_type;

      public Recommendation(@Nullable String id, @Nullable String name, @Nullable Object url,
                            @Nullable String click_url, @Nullable String app_url, @Nullable Object image_url,
                            @Nullable String price, @Nullable Integer price_int,
                            @Nullable String recommendation_type) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.click_url = click_url;
        this.app_url = app_url;
        this.image_url = image_url;
        this.price = price;
        this.price_int = price_int;
        this.recommendation_type = recommendation_type;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable Object url() {
        return this.url;
      }

      public @Nullable String click_url() {
        return this.click_url;
      }

      public @Nullable String app_url() {
        return this.app_url;
      }

      public @Nullable Object image_url() {
        return this.image_url;
      }

      public @Nullable String price() {
        return this.price;
      }

      public @Nullable Integer price_int() {
        return this.price_int;
      }

      public @Nullable String recommendation_type() {
        return this.recommendation_type;
      }

      @Override
      public String toString() {
        return "Recommendation{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "url=" + url + ", "
                + "click_url=" + click_url + ", "
                + "app_url=" + app_url + ", "
                + "image_url=" + image_url + ", "
                + "price=" + price + ", "
                + "price_int=" + price_int + ", "
                + "recommendation_type=" + recommendation_type
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Recommendation) {
          Recommendation that = (Recommendation) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.click_url == null) ? (that.click_url == null) : this.click_url.equals(that.click_url))
                  && ((this.app_url == null) ? (that.app_url == null) : this.app_url.equals(that.app_url))
                  && ((this.image_url == null) ? (that.image_url == null) : this.image_url.equals(that.image_url))
                  && ((this.price == null) ? (that.price == null) : this.price.equals(that.price))
                  && ((this.price_int == null) ? (that.price_int == null) : this.price_int.equals(that.price_int))
                  && ((this.recommendation_type == null) ? (that.recommendation_type == null) : this.recommendation_type.equals(that.recommendation_type));
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
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (click_url == null) ? 0 : click_url.hashCode();
        h *= 1000003;
        h ^= (app_url == null) ? 0 : app_url.hashCode();
        h *= 1000003;
        h ^= (image_url == null) ? 0 : image_url.hashCode();
        h *= 1000003;
        h ^= (price == null) ? 0 : price.hashCode();
        h *= 1000003;
        h ^= (price_int == null) ? 0 : price_int.hashCode();
        h *= 1000003;
        h ^= (recommendation_type == null) ? 0 : recommendation_type.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Recommendation> {
        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forCustomType("url", "url", null, true, CustomType.URL),
                Field.forString("click_url", "click_url", null, true),
                Field.forString("app_url", "app_url", null, true),
                Field.forCustomType("image_url", "image_url", null, true, CustomType.URL),
                Field.forString("price", "price", null, true),
                Field.forInt("price_int", "price_int", null, true),
                Field.forString("recommendation_type", "recommendation_type", null, true)
        };

        @Override
        public Recommendation map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final Object url = reader.read(fields[2]);
          final String click_url = reader.read(fields[3]);
          final String app_url = reader.read(fields[4]);
          final Object image_url = reader.read(fields[5]);
          final String price = reader.read(fields[6]);
          final Integer price_int = reader.read(fields[7]);
          final String recommendation_type = reader.read(fields[8]);
          return new Recommendation(id, name, url, click_url, app_url, image_url, price, price_int, recommendation_type);
        }
      }
    }

    public static class Inspirasi {
      private final @Nullable String experiment_version;

      private final @Nullable String source;

      private final @Nullable String title;

      private final @Nullable String foreign_title;

      private final @Nullable String widget_url;

      private final @Nullable Pagination pagination;

      private final @Nullable List<Recommendation> recommendation;

      public Inspirasi(@Nullable String experiment_version, @Nullable String source,
                       @Nullable String title, @Nullable String foreign_title, @Nullable String widget_url,
                       @Nullable Pagination pagination, @Nullable List<Recommendation> recommendation) {
        this.experiment_version = experiment_version;
        this.source = source;
        this.title = title;
        this.foreign_title = foreign_title;
        this.widget_url = widget_url;
        this.pagination = pagination;
        this.recommendation = recommendation;
      }

      public @Nullable String experiment_version() {
        return this.experiment_version;
      }

      public @Nullable String source() {
        return this.source;
      }

      public @Nullable String title() {
        return this.title;
      }

      public @Nullable String foreign_title() {
        return this.foreign_title;
      }

      public @Nullable String widget_url() {
        return this.widget_url;
      }

      public @Nullable Pagination pagination() {
        return this.pagination;
      }

      public @Nullable List<Recommendation> recommendation() {
        return this.recommendation;
      }

      @Override
      public String toString() {
        return "Inspirasi{"
                + "experiment_version=" + experiment_version + ", "
                + "source=" + source + ", "
                + "title=" + title + ", "
                + "foreign_title=" + foreign_title + ", "
                + "widget_url=" + widget_url + ", "
                + "pagination=" + pagination + ", "
                + "recommendation=" + recommendation
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Inspirasi) {
          Inspirasi that = (Inspirasi) o;
          return ((this.experiment_version == null) ? (that.experiment_version == null) : this.experiment_version.equals(that.experiment_version))
                  && ((this.source == null) ? (that.source == null) : this.source.equals(that.source))
                  && ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.foreign_title == null) ? (that.foreign_title == null) : this.foreign_title.equals(that.foreign_title))
                  && ((this.widget_url == null) ? (that.widget_url == null) : this.widget_url.equals(that.widget_url))
                  && ((this.pagination == null) ? (that.pagination == null) : this.pagination.equals(that.pagination))
                  && ((this.recommendation == null) ? (that.recommendation == null) : this.recommendation.equals(that.recommendation));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (experiment_version == null) ? 0 : experiment_version.hashCode();
        h *= 1000003;
        h ^= (source == null) ? 0 : source.hashCode();
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (foreign_title == null) ? 0 : foreign_title.hashCode();
        h *= 1000003;
        h ^= (widget_url == null) ? 0 : widget_url.hashCode();
        h *= 1000003;
        h ^= (pagination == null) ? 0 : pagination.hashCode();
        h *= 1000003;
        h ^= (recommendation == null) ? 0 : recommendation.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Inspirasi> {
        final Pagination.Mapper paginationFieldMapper = new Pagination.Mapper();

        final Recommendation.Mapper recommendationFieldMapper = new Recommendation.Mapper();

        final Field[] fields = {
                Field.forString("experiment_version", "experiment_version", null, true),
                Field.forString("source", "source", null, true),
                Field.forString("title", "title", null, true),
                Field.forString("foreign_title", "foreign_title", null, true),
                Field.forString("widget_url", "widget_url", null, true),
                Field.forObject("pagination", "pagination", null, true, new Field.ObjectReader<Pagination>() {
                  @Override public Pagination read(final ResponseReader reader) throws IOException {
                    return paginationFieldMapper.map(reader);
                  }
                }),
                Field.forList("recommendation", "recommendation", null, true, new Field.ObjectReader<Recommendation>() {
                  @Override public Recommendation read(final ResponseReader reader) throws IOException {
                    return recommendationFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Inspirasi map(ResponseReader reader) throws IOException {
          final String experiment_version = reader.read(fields[0]);
          final String source = reader.read(fields[1]);
          final String title = reader.read(fields[2]);
          final String foreign_title = reader.read(fields[3]);
          final String widget_url = reader.read(fields[4]);
          final Pagination pagination = reader.read(fields[5]);
          final List<Recommendation> recommendation = reader.read(fields[6]);
          return new Inspirasi(experiment_version, source, title, foreign_title, widget_url, pagination, recommendation);
        }
      }
    }

    public static class Tag {
      private final @Nullable Integer id;

      private final @Nullable String type;

      private final @Nullable String url;

      private final @Nullable String link;

      private final @Nullable String price;

      private final @Nullable String caption;

      public Tag(@Nullable Integer id, @Nullable String type, @Nullable String url,
                 @Nullable String link, @Nullable String price, @Nullable String caption) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.link = link;
        this.price = price;
        this.caption = caption;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable String url() {
        return this.url;
      }

      public @Nullable String link() {
        return this.link;
      }

      public @Nullable String price() {
        return this.price;
      }

      public @Nullable String caption() {
        return this.caption;
      }

      @Override
      public String toString() {
        return "Tag{"
                + "id=" + id + ", "
                + "type=" + type + ", "
                + "url=" + url + ", "
                + "link=" + link + ", "
                + "price=" + price + ", "
                + "caption=" + caption
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Tag) {
          Tag that = (Tag) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.link == null) ? (that.link == null) : this.link.equals(that.link))
                  && ((this.price == null) ? (that.price == null) : this.price.equals(that.price))
                  && ((this.caption == null) ? (that.caption == null) : this.caption.equals(that.caption));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (link == null) ? 0 : link.hashCode();
        h *= 1000003;
        h ^= (price == null) ? 0 : price.hashCode();
        h *= 1000003;
        h ^= (caption == null) ? 0 : caption.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Tag> {
        final Field[] fields = {
                Field.forInt("id", "id", null, true),
                Field.forString("type", "type", null, true),
                Field.forString("url", "url", null, true),
                Field.forString("link", "link", null, true),
                Field.forString("price", "price", null, true),
                Field.forString("caption", "caption", null, true)
        };

        @Override
        public Tag map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String type = reader.read(fields[1]);
          final String url = reader.read(fields[2]);
          final String link = reader.read(fields[3]);
          final String price = reader.read(fields[4]);
          final String caption = reader.read(fields[5]);
          return new Tag(id, type, url, link, price, caption);
        }
      }
    }

    public static class Content1 {
      private final @Nullable String imageurl;

      private final @Nullable List<Tag> tags;

      public Content1(@Nullable String imageurl, @Nullable List<Tag> tags) {
        this.imageurl = imageurl;
        this.tags = tags;
      }

      public @Nullable String imageurl() {
        return this.imageurl;
      }

      public @Nullable List<Tag> tags() {
        return this.tags;
      }

      @Override
      public String toString() {
        return "Content1{"
                + "imageurl=" + imageurl + ", "
                + "tags=" + tags
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Content1) {
          Content1 that = (Content1) o;
          return ((this.imageurl == null) ? (that.imageurl == null) : this.imageurl.equals(that.imageurl))
                  && ((this.tags == null) ? (that.tags == null) : this.tags.equals(that.tags));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (imageurl == null) ? 0 : imageurl.hashCode();
        h *= 1000003;
        h ^= (tags == null) ? 0 : tags.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Content1> {
        final Tag.Mapper tagFieldMapper = new Tag.Mapper();

        final Field[] fields = {
                Field.forString("imageurl", "imageurl", null, true),
                Field.forList("tags", "tags", null, true, new Field.ObjectReader<Tag>() {
                  @Override public Tag read(final ResponseReader reader) throws IOException {
                    return tagFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Content1 map(ResponseReader reader) throws IOException {
          final String imageurl = reader.read(fields[0]);
          final List<Tag> tags = reader.read(fields[1]);
          return new Content1(imageurl, tags);
        }
      }
    }

    public static class Kolpost {
      private final @Nullable Integer id;

      private final @Nullable String description;

      private final @Nullable Integer commentCount;

      private final @Nullable Integer likeCount;

      private final @Nullable Boolean isLiked;

      private final @Nullable Boolean isFollowed;

      private final @Nullable String createTime;

      private final @Nullable Boolean showComment;

      private final @Nullable String userName;

      private final @Nullable String userPhoto;

      private final @Nullable String userInfo;

      private final @Nullable String userUrl;

      private final @Nullable Integer userId;

      private final @Nullable String headerTitle;

      private final @Nullable List<Content1> content;

      public Kolpost(@Nullable Integer id, @Nullable String description,
                     @Nullable Integer commentCount, @Nullable Integer likeCount, @Nullable Boolean isLiked,
                     @Nullable Boolean isFollowed, @Nullable String createTime, @Nullable Boolean showComment,
                     @Nullable String userName, @Nullable String userPhoto, @Nullable String userInfo,
                     @Nullable String userUrl, @Nullable Integer userId, @Nullable String headerTitle,
                     @Nullable List<Content1> content) {
        this.id = id;
        this.description = description;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.createTime = createTime;
        this.showComment = showComment;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userInfo = userInfo;
        this.userUrl = userUrl;
        this.userId = userId;
        this.headerTitle = headerTitle;
        this.content = content;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String description() {
        return this.description;
      }

      public @Nullable Integer commentCount() {
        return this.commentCount;
      }

      public @Nullable Integer likeCount() {
        return this.likeCount;
      }

      public @Nullable Boolean isLiked() {
        return this.isLiked;
      }

      public @Nullable Boolean isFollowed() {
        return this.isFollowed;
      }

      public @Nullable String createTime() {
        return this.createTime;
      }

      public @Nullable Boolean showComment() {
        return this.showComment;
      }

      public @Nullable String userName() {
        return this.userName;
      }

      public @Nullable String userPhoto() {
        return this.userPhoto;
      }

      public @Nullable String userInfo() {
        return this.userInfo;
      }

      public @Nullable String userUrl() {
        return this.userUrl;
      }

      public @Nullable Integer userId() {
        return this.userId;
      }

      public @Nullable String headerTitle() {
        return this.headerTitle;
      }

      public @Nullable List<Content1> content() {
        return this.content;
      }

      @Override
      public String toString() {
        return "Kolpost{"
                + "id=" + id + ", "
                + "description=" + description + ", "
                + "commentCount=" + commentCount + ", "
                + "likeCount=" + likeCount + ", "
                + "isLiked=" + isLiked + ", "
                + "isFollowed=" + isFollowed + ", "
                + "createTime=" + createTime + ", "
                + "showComment=" + showComment + ", "
                + "userName=" + userName + ", "
                + "userPhoto=" + userPhoto + ", "
                + "userInfo=" + userInfo + ", "
                + "userUrl=" + userUrl + ", "
                + "userId=" + userId + ", "
                + "headerTitle=" + headerTitle + ", "
                + "content=" + content
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Kolpost) {
          Kolpost that = (Kolpost) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.description == null) ? (that.description == null) : this.description.equals(that.description))
                  && ((this.commentCount == null) ? (that.commentCount == null) : this.commentCount.equals(that.commentCount))
                  && ((this.likeCount == null) ? (that.likeCount == null) : this.likeCount.equals(that.likeCount))
                  && ((this.isLiked == null) ? (that.isLiked == null) : this.isLiked.equals(that.isLiked))
                  && ((this.isFollowed == null) ? (that.isFollowed == null) : this.isFollowed.equals(that.isFollowed))
                  && ((this.createTime == null) ? (that.createTime == null) : this.createTime.equals(that.createTime))
                  && ((this.showComment == null) ? (that.showComment == null) : this.showComment.equals(that.showComment))
                  && ((this.userName == null) ? (that.userName == null) : this.userName.equals(that.userName))
                  && ((this.userPhoto == null) ? (that.userPhoto == null) : this.userPhoto.equals(that.userPhoto))
                  && ((this.userInfo == null) ? (that.userInfo == null) : this.userInfo.equals(that.userInfo))
                  && ((this.userUrl == null) ? (that.userUrl == null) : this.userUrl.equals(that.userUrl))
                  && ((this.userId == null) ? (that.userId == null) : this.userId.equals(that.userId))
                  && ((this.headerTitle == null) ? (that.headerTitle == null) : this.headerTitle.equals(that.headerTitle))
                  && ((this.content == null) ? (that.content == null) : this.content.equals(that.content));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (description == null) ? 0 : description.hashCode();
        h *= 1000003;
        h ^= (commentCount == null) ? 0 : commentCount.hashCode();
        h *= 1000003;
        h ^= (likeCount == null) ? 0 : likeCount.hashCode();
        h *= 1000003;
        h ^= (isLiked == null) ? 0 : isLiked.hashCode();
        h *= 1000003;
        h ^= (isFollowed == null) ? 0 : isFollowed.hashCode();
        h *= 1000003;
        h ^= (createTime == null) ? 0 : createTime.hashCode();
        h *= 1000003;
        h ^= (showComment == null) ? 0 : showComment.hashCode();
        h *= 1000003;
        h ^= (userName == null) ? 0 : userName.hashCode();
        h *= 1000003;
        h ^= (userPhoto == null) ? 0 : userPhoto.hashCode();
        h *= 1000003;
        h ^= (userInfo == null) ? 0 : userInfo.hashCode();
        h *= 1000003;
        h ^= (userUrl == null) ? 0 : userUrl.hashCode();
        h *= 1000003;
        h ^= (userId == null) ? 0 : userId.hashCode();
        h *= 1000003;
        h ^= (headerTitle == null) ? 0 : headerTitle.hashCode();
        h *= 1000003;
        h ^= (content == null) ? 0 : content.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Kolpost> {
        final Content1.Mapper content1FieldMapper = new Content1.Mapper();

        final Field[] fields = {
                Field.forInt("id", "id", null, true),
                Field.forString("description", "description", null, true),
                Field.forInt("commentCount", "commentCount", null, true),
                Field.forInt("likeCount", "likeCount", null, true),
                Field.forBoolean("isLiked", "isLiked", null, true),
                Field.forBoolean("isFollowed", "isFollowed", null, true),
                Field.forString("createTime", "createTime", null, true),
                Field.forBoolean("showComment", "showComment", null, true),
                Field.forString("userName", "userName", null, true),
                Field.forString("userPhoto", "userPhoto", null, true),
                Field.forString("userInfo", "userInfo", null, true),
                Field.forString("userUrl", "userUrl", null, true),
                Field.forInt("userId", "userId", null, true),
                Field.forString("headerTitle", "headerTitle", null, true),
                Field.forList("content", "content", null, true, new Field.ObjectReader<Content1>() {
                  @Override public Content1 read(final ResponseReader reader) throws IOException {
                    return content1FieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Kolpost map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String description = reader.read(fields[1]);
          final Integer commentCount = reader.read(fields[2]);
          final Integer likeCount = reader.read(fields[3]);
          final Boolean isLiked = reader.read(fields[4]);
          final Boolean isFollowed = reader.read(fields[5]);
          final String createTime = reader.read(fields[6]);
          final Boolean showComment = reader.read(fields[7]);
          final String userName = reader.read(fields[8]);
          final String userPhoto = reader.read(fields[9]);
          final String userInfo = reader.read(fields[10]);
          final String userUrl = reader.read(fields[11]);
          final Integer userId = reader.read(fields[12]);
          final String headerTitle = reader.read(fields[13]);
          final List<Content1> content = reader.read(fields[14]);
          return new Kolpost(id, description, commentCount, likeCount, isLiked, isFollowed, createTime, showComment, userName, userPhoto, userInfo, userUrl, userId, headerTitle, content);
        }
      }
    }

    public static class Tag1 {
      private final @Nullable Integer id;

      private final @Nullable String type;

      private final @Nullable String url;

      private final @Nullable String link;

      private final @Nullable String price;

      private final @Nullable String caption;

      public Tag1(@Nullable Integer id, @Nullable String type, @Nullable String url,
                  @Nullable String link, @Nullable String price, @Nullable String caption) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.link = link;
        this.price = price;
        this.caption = caption;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable String url() {
        return this.url;
      }

      public @Nullable String link() {
        return this.link;
      }

      public @Nullable String price() {
        return this.price;
      }

      public @Nullable String caption() {
        return this.caption;
      }

      @Override
      public String toString() {
        return "Tag1{"
                + "id=" + id + ", "
                + "type=" + type + ", "
                + "url=" + url + ", "
                + "link=" + link + ", "
                + "price=" + price + ", "
                + "caption=" + caption
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Tag1) {
          Tag1 that = (Tag1) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.link == null) ? (that.link == null) : this.link.equals(that.link))
                  && ((this.price == null) ? (that.price == null) : this.price.equals(that.price))
                  && ((this.caption == null) ? (that.caption == null) : this.caption.equals(that.caption));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (link == null) ? 0 : link.hashCode();
        h *= 1000003;
        h ^= (price == null) ? 0 : price.hashCode();
        h *= 1000003;
        h ^= (caption == null) ? 0 : caption.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Tag1> {
        final Field[] fields = {
                Field.forInt("id", "id", null, true),
                Field.forString("type", "type", null, true),
                Field.forString("url", "url", null, true),
                Field.forString("link", "link", null, true),
                Field.forString("price", "price", null, true),
                Field.forString("caption", "caption", null, true)
        };

        @Override
        public Tag1 map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String type = reader.read(fields[1]);
          final String url = reader.read(fields[2]);
          final String link = reader.read(fields[3]);
          final String price = reader.read(fields[4]);
          final String caption = reader.read(fields[5]);
          return new Tag1(id, type, url, link, price, caption);
        }
      }
    }

    public static class Content2 {
      private final @Nullable String imageurl;

      private final @Nullable List<Tag1> tags;

      public Content2(@Nullable String imageurl, @Nullable List<Tag1> tags) {
        this.imageurl = imageurl;
        this.tags = tags;
      }

      public @Nullable String imageurl() {
        return this.imageurl;
      }

      public @Nullable List<Tag1> tags() {
        return this.tags;
      }

      @Override
      public String toString() {
        return "Content2{"
                + "imageurl=" + imageurl + ", "
                + "tags=" + tags
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Content2) {
          Content2 that = (Content2) o;
          return ((this.imageurl == null) ? (that.imageurl == null) : this.imageurl.equals(that.imageurl))
                  && ((this.tags == null) ? (that.tags == null) : this.tags.equals(that.tags));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (imageurl == null) ? 0 : imageurl.hashCode();
        h *= 1000003;
        h ^= (tags == null) ? 0 : tags.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Content2> {
        final Tag1.Mapper tag1FieldMapper = new Tag1.Mapper();

        final Field[] fields = {
                Field.forString("imageurl", "imageurl", null, true),
                Field.forList("tags", "tags", null, true, new Field.ObjectReader<Tag1>() {
                  @Override public Tag1 read(final ResponseReader reader) throws IOException {
                    return tag1FieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Content2 map(ResponseReader reader) throws IOException {
          final String imageurl = reader.read(fields[0]);
          final List<Tag1> tags = reader.read(fields[1]);
          return new Content2(imageurl, tags);
        }
      }
    }

    public static class Followedkolpost {
      private final @Nullable Integer id;

      private final @Nullable String description;

      private final @Nullable Integer commentCount;

      private final @Nullable Integer likeCount;

      private final @Nullable Boolean isLiked;

      private final @Nullable Boolean isFollowed;

      private final @Nullable String createTime;

      private final @Nullable Boolean showComment;

      private final @Nullable String userName;

      private final @Nullable String userPhoto;

      private final @Nullable String userInfo;

      private final @Nullable String userUrl;

      private final @Nullable Integer userId;

      private final @Nullable List<Content2> content;

      public Followedkolpost(@Nullable Integer id, @Nullable String description,
                             @Nullable Integer commentCount, @Nullable Integer likeCount, @Nullable Boolean isLiked,
                             @Nullable Boolean isFollowed, @Nullable String createTime, @Nullable Boolean showComment,
                             @Nullable String userName, @Nullable String userPhoto, @Nullable String userInfo,
                             @Nullable String userUrl, @Nullable Integer userId, @Nullable List<Content2> content) {
        this.id = id;
        this.description = description;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.createTime = createTime;
        this.showComment = showComment;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userInfo = userInfo;
        this.userUrl = userUrl;
        this.userId = userId;
        this.content = content;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String description() {
        return this.description;
      }

      public @Nullable Integer commentCount() {
        return this.commentCount;
      }

      public @Nullable Integer likeCount() {
        return this.likeCount;
      }

      public @Nullable Boolean isLiked() {
        return this.isLiked;
      }

      public @Nullable Boolean isFollowed() {
        return this.isFollowed;
      }

      public @Nullable String createTime() {
        return this.createTime;
      }

      public @Nullable Boolean showComment() {
        return this.showComment;
      }

      public @Nullable String userName() {
        return this.userName;
      }

      public @Nullable String userPhoto() {
        return this.userPhoto;
      }

      public @Nullable String userInfo() {
        return this.userInfo;
      }

      public @Nullable String userUrl() {
        return this.userUrl;
      }

      public @Nullable Integer userId() {
        return this.userId;
      }

      public @Nullable List<Content2> content() {
        return this.content;
      }

      @Override
      public String toString() {
        return "Followedkolpost{"
                + "id=" + id + ", "
                + "description=" + description + ", "
                + "commentCount=" + commentCount + ", "
                + "likeCount=" + likeCount + ", "
                + "isLiked=" + isLiked + ", "
                + "isFollowed=" + isFollowed + ", "
                + "createTime=" + createTime + ", "
                + "showComment=" + showComment + ", "
                + "userName=" + userName + ", "
                + "userPhoto=" + userPhoto + ", "
                + "userInfo=" + userInfo + ", "
                + "userUrl=" + userUrl + ", "
                + "userId=" + userId + ", "
                + "content=" + content
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Followedkolpost) {
          Followedkolpost that = (Followedkolpost) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.description == null) ? (that.description == null) : this.description.equals(that.description))
                  && ((this.commentCount == null) ? (that.commentCount == null) : this.commentCount.equals(that.commentCount))
                  && ((this.likeCount == null) ? (that.likeCount == null) : this.likeCount.equals(that.likeCount))
                  && ((this.isLiked == null) ? (that.isLiked == null) : this.isLiked.equals(that.isLiked))
                  && ((this.isFollowed == null) ? (that.isFollowed == null) : this.isFollowed.equals(that.isFollowed))
                  && ((this.createTime == null) ? (that.createTime == null) : this.createTime.equals(that.createTime))
                  && ((this.showComment == null) ? (that.showComment == null) : this.showComment.equals(that.showComment))
                  && ((this.userName == null) ? (that.userName == null) : this.userName.equals(that.userName))
                  && ((this.userPhoto == null) ? (that.userPhoto == null) : this.userPhoto.equals(that.userPhoto))
                  && ((this.userInfo == null) ? (that.userInfo == null) : this.userInfo.equals(that.userInfo))
                  && ((this.userUrl == null) ? (that.userUrl == null) : this.userUrl.equals(that.userUrl))
                  && ((this.userId == null) ? (that.userId == null) : this.userId.equals(that.userId))
                  && ((this.content == null) ? (that.content == null) : this.content.equals(that.content));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (description == null) ? 0 : description.hashCode();
        h *= 1000003;
        h ^= (commentCount == null) ? 0 : commentCount.hashCode();
        h *= 1000003;
        h ^= (likeCount == null) ? 0 : likeCount.hashCode();
        h *= 1000003;
        h ^= (isLiked == null) ? 0 : isLiked.hashCode();
        h *= 1000003;
        h ^= (isFollowed == null) ? 0 : isFollowed.hashCode();
        h *= 1000003;
        h ^= (createTime == null) ? 0 : createTime.hashCode();
        h *= 1000003;
        h ^= (showComment == null) ? 0 : showComment.hashCode();
        h *= 1000003;
        h ^= (userName == null) ? 0 : userName.hashCode();
        h *= 1000003;
        h ^= (userPhoto == null) ? 0 : userPhoto.hashCode();
        h *= 1000003;
        h ^= (userInfo == null) ? 0 : userInfo.hashCode();
        h *= 1000003;
        h ^= (userUrl == null) ? 0 : userUrl.hashCode();
        h *= 1000003;
        h ^= (userId == null) ? 0 : userId.hashCode();
        h *= 1000003;
        h ^= (content == null) ? 0 : content.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Followedkolpost> {
        final Content2.Mapper content2FieldMapper = new Content2.Mapper();

        final Field[] fields = {
                Field.forInt("id", "id", null, true),
                Field.forString("description", "description", null, true),
                Field.forInt("commentCount", "commentCount", null, true),
                Field.forInt("likeCount", "likeCount", null, true),
                Field.forBoolean("isLiked", "isLiked", null, true),
                Field.forBoolean("isFollowed", "isFollowed", null, true),
                Field.forString("createTime", "createTime", null, true),
                Field.forBoolean("showComment", "showComment", null, true),
                Field.forString("userName", "userName", null, true),
                Field.forString("userPhoto", "userPhoto", null, true),
                Field.forString("userInfo", "userInfo", null, true),
                Field.forString("userUrl", "userUrl", null, true),
                Field.forInt("userId", "userId", null, true),
                Field.forList("content", "content", null, true, new Field.ObjectReader<Content2>() {
                  @Override public Content2 read(final ResponseReader reader) throws IOException {
                    return content2FieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Followedkolpost map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String description = reader.read(fields[1]);
          final Integer commentCount = reader.read(fields[2]);
          final Integer likeCount = reader.read(fields[3]);
          final Boolean isLiked = reader.read(fields[4]);
          final Boolean isFollowed = reader.read(fields[5]);
          final String createTime = reader.read(fields[6]);
          final Boolean showComment = reader.read(fields[7]);
          final String userName = reader.read(fields[8]);
          final String userPhoto = reader.read(fields[9]);
          final String userInfo = reader.read(fields[10]);
          final String userUrl = reader.read(fields[11]);
          final Integer userId = reader.read(fields[12]);
          final List<Content2> content = reader.read(fields[13]);
          return new Followedkolpost(id, description, commentCount, likeCount, isLiked, isFollowed, createTime, showComment, userName, userPhoto, userInfo, userUrl, userId, content);
        }
      }
    }

    public static class Kol {
      private final @Nullable String userName;

      private final @Nullable Integer userId;

      private final @Nullable String userPhoto;

      private final @Nullable Boolean isFollowed;

      private final @Nullable String info;

      private final @Nullable String url;

      public Kol(@Nullable String userName, @Nullable Integer userId, @Nullable String userPhoto,
                 @Nullable Boolean isFollowed, @Nullable String info, @Nullable String url) {
        this.userName = userName;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.isFollowed = isFollowed;
        this.info = info;
        this.url = url;
      }

      public @Nullable String userName() {
        return this.userName;
      }

      public @Nullable Integer userId() {
        return this.userId;
      }

      public @Nullable String userPhoto() {
        return this.userPhoto;
      }

      public @Nullable Boolean isFollowed() {
        return this.isFollowed;
      }

      public @Nullable String info() {
        return this.info;
      }

      public @Nullable String url() {
        return this.url;
      }

      @Override
      public String toString() {
        return "Kol{"
                + "userName=" + userName + ", "
                + "userId=" + userId + ", "
                + "userPhoto=" + userPhoto + ", "
                + "isFollowed=" + isFollowed + ", "
                + "info=" + info + ", "
                + "url=" + url
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Kol) {
          Kol that = (Kol) o;
          return ((this.userName == null) ? (that.userName == null) : this.userName.equals(that.userName))
                  && ((this.userId == null) ? (that.userId == null) : this.userId.equals(that.userId))
                  && ((this.userPhoto == null) ? (that.userPhoto == null) : this.userPhoto.equals(that.userPhoto))
                  && ((this.isFollowed == null) ? (that.isFollowed == null) : this.isFollowed.equals(that.isFollowed))
                  && ((this.info == null) ? (that.info == null) : this.info.equals(that.info))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (userName == null) ? 0 : userName.hashCode();
        h *= 1000003;
        h ^= (userId == null) ? 0 : userId.hashCode();
        h *= 1000003;
        h ^= (userPhoto == null) ? 0 : userPhoto.hashCode();
        h *= 1000003;
        h ^= (isFollowed == null) ? 0 : isFollowed.hashCode();
        h *= 1000003;
        h ^= (info == null) ? 0 : info.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Kol> {
        final Field[] fields = {
                Field.forString("userName", "userName", null, true),
                Field.forInt("userId", "userId", null, true),
                Field.forString("userPhoto", "userPhoto", null, true),
                Field.forBoolean("isFollowed", "isFollowed", null, true),
                Field.forString("info", "info", null, true),
                Field.forString("url", "url", null, true)
        };

        @Override
        public Kol map(ResponseReader reader) throws IOException {
          final String userName = reader.read(fields[0]);
          final Integer userId = reader.read(fields[1]);
          final String userPhoto = reader.read(fields[2]);
          final Boolean isFollowed = reader.read(fields[3]);
          final String info = reader.read(fields[4]);
          final String url = reader.read(fields[5]);
          return new Kol(userName, userId, userPhoto, isFollowed, info, url);
        }
      }
    }

    public static class Kolrecommendation {
      private final @Nullable String headerTitle;

      private final @Nullable String exploreLink;

      private final @Nullable String exploreText;

      private final @Nullable List<Kol> kols;

      public Kolrecommendation(@Nullable String headerTitle, @Nullable String exploreLink,
                               @Nullable String exploreText, @Nullable List<Kol> kols) {
        this.headerTitle = headerTitle;
        this.exploreLink = exploreLink;
        this.exploreText = exploreText;
        this.kols = kols;
      }

      public @Nullable String headerTitle() {
        return this.headerTitle;
      }

      public @Nullable String exploreLink() {
        return this.exploreLink;
      }

      public @Nullable String exploreText() {
        return this.exploreText;
      }

      public @Nullable List<Kol> kols() {
        return this.kols;
      }

      @Override
      public String toString() {
        return "Kolrecommendation{"
                + "headerTitle=" + headerTitle + ", "
                + "exploreLink=" + exploreLink + ", "
                + "exploreText=" + exploreText + ", "
                + "kols=" + kols
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Kolrecommendation) {
          Kolrecommendation that = (Kolrecommendation) o;
          return ((this.headerTitle == null) ? (that.headerTitle == null) : this.headerTitle.equals(that.headerTitle))
                  && ((this.exploreLink == null) ? (that.exploreLink == null) : this.exploreLink.equals(that.exploreLink))
                  && ((this.exploreText == null) ? (that.exploreText == null) : this.exploreText.equals(that.exploreText))
                  && ((this.kols == null) ? (that.kols == null) : this.kols.equals(that.kols));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (headerTitle == null) ? 0 : headerTitle.hashCode();
        h *= 1000003;
        h ^= (exploreLink == null) ? 0 : exploreLink.hashCode();
        h *= 1000003;
        h ^= (exploreText == null) ? 0 : exploreText.hashCode();
        h *= 1000003;
        h ^= (kols == null) ? 0 : kols.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Kolrecommendation> {
        final Kol.Mapper kolFieldMapper = new Kol.Mapper();

        final Field[] fields = {
                Field.forString("headerTitle", "headerTitle", null, true),
                Field.forString("exploreLink", "exploreLink", null, true),
                Field.forString("exploreText", "exploreText", null, true),
                Field.forList("kols", "kols", null, true, new Field.ObjectReader<Kol>() {
                  @Override public Kol read(final ResponseReader reader) throws IOException {
                    return kolFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Kolrecommendation map(ResponseReader reader) throws IOException {
          final String headerTitle = reader.read(fields[0]);
          final String exploreLink = reader.read(fields[1]);
          final String exploreText = reader.read(fields[2]);
          final List<Kol> kols = reader.read(fields[3]);
          return new Kolrecommendation(headerTitle, exploreLink, exploreText, kols);
        }
      }
    }

    public static class Favorite_cta {
      private final @Nullable String title_en;

      private final @Nullable String title_id;

      private final @Nullable String subtitle_en;

      private final @Nullable String subtitle_id;

      public Favorite_cta(@Nullable String title_en, @Nullable String title_id,
                          @Nullable String subtitle_en, @Nullable String subtitle_id) {
        this.title_en = title_en;
        this.title_id = title_id;
        this.subtitle_en = subtitle_en;
        this.subtitle_id = subtitle_id;
      }

      public @Nullable String title_en() {
        return this.title_en;
      }

      public @Nullable String title_id() {
        return this.title_id;
      }

      public @Nullable String subtitle_en() {
        return this.subtitle_en;
      }

      public @Nullable String subtitle_id() {
        return this.subtitle_id;
      }

      @Override
      public String toString() {
        return "Favorite_cta{"
                + "title_en=" + title_en + ", "
                + "title_id=" + title_id + ", "
                + "subtitle_en=" + subtitle_en + ", "
                + "subtitle_id=" + subtitle_id
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Favorite_cta) {
          Favorite_cta that = (Favorite_cta) o;
          return ((this.title_en == null) ? (that.title_en == null) : this.title_en.equals(that.title_en))
                  && ((this.title_id == null) ? (that.title_id == null) : this.title_id.equals(that.title_id))
                  && ((this.subtitle_en == null) ? (that.subtitle_en == null) : this.subtitle_en.equals(that.subtitle_en))
                  && ((this.subtitle_id == null) ? (that.subtitle_id == null) : this.subtitle_id.equals(that.subtitle_id));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (title_en == null) ? 0 : title_en.hashCode();
        h *= 1000003;
        h ^= (title_id == null) ? 0 : title_id.hashCode();
        h *= 1000003;
        h ^= (subtitle_en == null) ? 0 : subtitle_en.hashCode();
        h *= 1000003;
        h ^= (subtitle_id == null) ? 0 : subtitle_id.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Favorite_cta> {
        final Field[] fields = {
                Field.forString("title_en", "title_en", null, true),
                Field.forString("title_id", "title_id", null, true),
                Field.forString("subtitle_en", "subtitle_en", null, true),
                Field.forString("subtitle_id", "subtitle_id", null, true)
        };

        @Override
        public Favorite_cta map(ResponseReader reader) throws IOException {
          final String title_en = reader.read(fields[0]);
          final String title_id = reader.read(fields[1]);
          final String subtitle_en = reader.read(fields[2]);
          final String subtitle_id = reader.read(fields[3]);
          return new Favorite_cta(title_en, title_id, subtitle_en, subtitle_id);
        }
      }
    }

    public static class Kol_cta {
      private final @Nullable String img_header;

      private final @Nullable String title;

      private final @Nullable String subtitle;

      private final @Nullable String button_text;

      private final @Nullable String click_url;

      private final @Nullable String click_applink;

      public Kol_cta(@Nullable String img_header, @Nullable String title, @Nullable String subtitle,
                     @Nullable String button_text, @Nullable String click_url,
                     @Nullable String click_applink) {
        this.img_header = img_header;
        this.title = title;
        this.subtitle = subtitle;
        this.button_text = button_text;
        this.click_url = click_url;
        this.click_applink = click_applink;
      }

      public @Nullable String img_header() {
        return this.img_header;
      }

      public @Nullable String title() {
        return this.title;
      }

      public @Nullable String subtitle() {
        return this.subtitle;
      }

      public @Nullable String button_text() {
        return this.button_text;
      }

      public @Nullable String click_url() {
        return this.click_url;
      }

      public @Nullable String click_applink() {
        return this.click_applink;
      }

      @Override
      public String toString() {
        return "Kol_cta{"
                + "img_header=" + img_header + ", "
                + "title=" + title + ", "
                + "subtitle=" + subtitle + ", "
                + "button_text=" + button_text + ", "
                + "click_url=" + click_url + ", "
                + "click_applink=" + click_applink
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Kol_cta) {
          Kol_cta that = (Kol_cta) o;
          return ((this.img_header == null) ? (that.img_header == null) : this.img_header.equals(that.img_header))
                  && ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.subtitle == null) ? (that.subtitle == null) : this.subtitle.equals(that.subtitle))
                  && ((this.button_text == null) ? (that.button_text == null) : this.button_text.equals(that.button_text))
                  && ((this.click_url == null) ? (that.click_url == null) : this.click_url.equals(that.click_url))
                  && ((this.click_applink == null) ? (that.click_applink == null) : this.click_applink.equals(that.click_applink));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (img_header == null) ? 0 : img_header.hashCode();
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (subtitle == null) ? 0 : subtitle.hashCode();
        h *= 1000003;
        h ^= (button_text == null) ? 0 : button_text.hashCode();
        h *= 1000003;
        h ^= (click_url == null) ? 0 : click_url.hashCode();
        h *= 1000003;
        h ^= (click_applink == null) ? 0 : click_applink.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Kol_cta> {
        final Field[] fields = {
                Field.forString("img_header", "img_header", null, true),
                Field.forString("title", "title", null, true),
                Field.forString("subtitle", "subtitle", null, true),
                Field.forString("button_text", "button_text", null, true),
                Field.forString("click_url", "click_url", null, true),
                Field.forString("click_applink", "click_applink", null, true)
        };

        @Override
        public Kol_cta map(ResponseReader reader) throws IOException {
          final String img_header = reader.read(fields[0]);
          final String title = reader.read(fields[1]);
          final String subtitle = reader.read(fields[2]);
          final String button_text = reader.read(fields[3]);
          final String click_url = reader.read(fields[4]);
          final String click_applink = reader.read(fields[5]);
          return new Kol_cta(img_header, title, subtitle, button_text, click_url, click_applink);
        }
      }
    }

    public static class Image {
      private final @Nonnull Object m_url;

      private final @Nonnull Object s_url;

      private final @Nonnull Object xs_url;

      private final @Nonnull Object m_ecs;

      private final @Nonnull Object s_ecs;

      private final @Nonnull Object xs_ecs;

      public Image(@Nonnull Object m_url, @Nonnull Object s_url, @Nonnull Object xs_url,
                   @Nonnull Object m_ecs, @Nonnull Object s_ecs, @Nonnull Object xs_ecs) {
        this.m_url = m_url;
        this.s_url = s_url;
        this.xs_url = xs_url;
        this.m_ecs = m_ecs;
        this.s_ecs = s_ecs;
        this.xs_ecs = xs_ecs;
      }

      public @Nonnull Object m_url() {
        return this.m_url;
      }

      public @Nonnull Object s_url() {
        return this.s_url;
      }

      public @Nonnull Object xs_url() {
        return this.xs_url;
      }

      public @Nonnull Object m_ecs() {
        return this.m_ecs;
      }

      public @Nonnull Object s_ecs() {
        return this.s_ecs;
      }

      public @Nonnull Object xs_ecs() {
        return this.xs_ecs;
      }

      @Override
      public String toString() {
        return "Image{"
                + "m_url=" + m_url + ", "
                + "s_url=" + s_url + ", "
                + "xs_url=" + xs_url + ", "
                + "m_ecs=" + m_ecs + ", "
                + "s_ecs=" + s_ecs + ", "
                + "xs_ecs=" + xs_ecs
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Image) {
          Image that = (Image) o;
          return ((this.m_url == null) ? (that.m_url == null) : this.m_url.equals(that.m_url))
                  && ((this.s_url == null) ? (that.s_url == null) : this.s_url.equals(that.s_url))
                  && ((this.xs_url == null) ? (that.xs_url == null) : this.xs_url.equals(that.xs_url))
                  && ((this.m_ecs == null) ? (that.m_ecs == null) : this.m_ecs.equals(that.m_ecs))
                  && ((this.s_ecs == null) ? (that.s_ecs == null) : this.s_ecs.equals(that.s_ecs))
                  && ((this.xs_ecs == null) ? (that.xs_ecs == null) : this.xs_ecs.equals(that.xs_ecs));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (m_url == null) ? 0 : m_url.hashCode();
        h *= 1000003;
        h ^= (s_url == null) ? 0 : s_url.hashCode();
        h *= 1000003;
        h ^= (xs_url == null) ? 0 : xs_url.hashCode();
        h *= 1000003;
        h ^= (m_ecs == null) ? 0 : m_ecs.hashCode();
        h *= 1000003;
        h ^= (s_ecs == null) ? 0 : s_ecs.hashCode();
        h *= 1000003;
        h ^= (xs_ecs == null) ? 0 : xs_ecs.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Image> {
        final Field[] fields = {
                Field.forCustomType("m_url", "m_url", null, false, CustomType.URL),
                Field.forCustomType("s_url", "s_url", null, false, CustomType.URL),
                Field.forCustomType("xs_url", "xs_url", null, false, CustomType.URL),
                Field.forCustomType("m_ecs", "m_ecs", null, false, CustomType.URL),
                Field.forCustomType("s_ecs", "s_ecs", null, false, CustomType.URL),
                Field.forCustomType("xs_ecs", "xs_ecs", null, false, CustomType.URL)
        };

        @Override
        public Image map(ResponseReader reader) throws IOException {
          final Object m_url = reader.read(fields[0]);
          final Object s_url = reader.read(fields[1]);
          final Object xs_url = reader.read(fields[2]);
          final Object m_ecs = reader.read(fields[3]);
          final Object s_ecs = reader.read(fields[4]);
          final Object xs_ecs = reader.read(fields[5]);
          return new Image(m_url, s_url, xs_url, m_ecs, s_ecs, xs_ecs);
        }
      }
    }

    public static class Category {
      private final @Nullable String id;

      public Category(@Nullable String id) {
        this.id = id;
      }

      public @Nullable String id() {
        return this.id;
      }

      @Override
      public String toString() {
        return "Category{"
                + "id=" + id
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Category) {
          Category that = (Category) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Category> {
        final Field[] fields = {
                Field.forString("id", "id", null, true)
        };

        @Override
        public Category map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          return new Category(id);
        }
      }
    }

    public static class Product2 {
      private final @Nullable String id;

      private final @Nullable String name;

      private final @Nullable Image image;

      private final @Nullable Object uri;

      private final @Nullable String relative_uri;

      private final @Nullable String price_format;

      private final @Nullable String count_talk_format;

      private final @Nullable String count_review_format;

      private final @Nullable Category category;

      private final @Nullable Boolean product_preorder;

      private final @Nullable Boolean product_wholesale;

      private final @Nullable Object free_return;

      private final @Nullable Boolean product_cashback;

      private final @Nullable String product_cashback_rate;

      private final @Nullable Integer product_rating;

      public Product2(@Nullable String id, @Nullable String name, @Nullable Image image,
                      @Nullable Object uri, @Nullable String relative_uri, @Nullable String price_format,
                      @Nullable String count_talk_format, @Nullable String count_review_format,
                      @Nullable Category category, @Nullable Boolean product_preorder,
                      @Nullable Boolean product_wholesale, @Nullable Object free_return,
                      @Nullable Boolean product_cashback, @Nullable String product_cashback_rate,
                      @Nullable Integer product_rating) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.uri = uri;
        this.relative_uri = relative_uri;
        this.price_format = price_format;
        this.count_talk_format = count_talk_format;
        this.count_review_format = count_review_format;
        this.category = category;
        this.product_preorder = product_preorder;
        this.product_wholesale = product_wholesale;
        this.free_return = free_return;
        this.product_cashback = product_cashback;
        this.product_cashback_rate = product_cashback_rate;
        this.product_rating = product_rating;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable Image image() {
        return this.image;
      }

      public @Nullable Object uri() {
        return this.uri;
      }

      public @Nullable String relative_uri() {
        return this.relative_uri;
      }

      public @Nullable String price_format() {
        return this.price_format;
      }

      public @Nullable String count_talk_format() {
        return this.count_talk_format;
      }

      public @Nullable String count_review_format() {
        return this.count_review_format;
      }

      public @Nullable Category category() {
        return this.category;
      }

      public @Nullable Boolean product_preorder() {
        return this.product_preorder;
      }

      public @Nullable Boolean product_wholesale() {
        return this.product_wholesale;
      }

      public @Nullable Object free_return() {
        return this.free_return;
      }

      public @Nullable Boolean product_cashback() {
        return this.product_cashback;
      }

      public @Nullable String product_cashback_rate() {
        return this.product_cashback_rate;
      }

      public @Nullable Integer product_rating() {
        return this.product_rating;
      }

      @Override
      public String toString() {
        return "Product2{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "image=" + image + ", "
                + "uri=" + uri + ", "
                + "relative_uri=" + relative_uri + ", "
                + "price_format=" + price_format + ", "
                + "count_talk_format=" + count_talk_format + ", "
                + "count_review_format=" + count_review_format + ", "
                + "category=" + category + ", "
                + "product_preorder=" + product_preorder + ", "
                + "product_wholesale=" + product_wholesale + ", "
                + "free_return=" + free_return + ", "
                + "product_cashback=" + product_cashback + ", "
                + "product_cashback_rate=" + product_cashback_rate + ", "
                + "product_rating=" + product_rating
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Product2) {
          Product2 that = (Product2) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.image == null) ? (that.image == null) : this.image.equals(that.image))
                  && ((this.uri == null) ? (that.uri == null) : this.uri.equals(that.uri))
                  && ((this.relative_uri == null) ? (that.relative_uri == null) : this.relative_uri.equals(that.relative_uri))
                  && ((this.price_format == null) ? (that.price_format == null) : this.price_format.equals(that.price_format))
                  && ((this.count_talk_format == null) ? (that.count_talk_format == null) : this.count_talk_format.equals(that.count_talk_format))
                  && ((this.count_review_format == null) ? (that.count_review_format == null) : this.count_review_format.equals(that.count_review_format))
                  && ((this.category == null) ? (that.category == null) : this.category.equals(that.category))
                  && ((this.product_preorder == null) ? (that.product_preorder == null) : this.product_preorder.equals(that.product_preorder))
                  && ((this.product_wholesale == null) ? (that.product_wholesale == null) : this.product_wholesale.equals(that.product_wholesale))
                  && ((this.free_return == null) ? (that.free_return == null) : this.free_return.equals(that.free_return))
                  && ((this.product_cashback == null) ? (that.product_cashback == null) : this.product_cashback.equals(that.product_cashback))
                  && ((this.product_cashback_rate == null) ? (that.product_cashback_rate == null) : this.product_cashback_rate.equals(that.product_cashback_rate))
                  && ((this.product_rating == null) ? (that.product_rating == null) : this.product_rating.equals(that.product_rating));
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
        h ^= (image == null) ? 0 : image.hashCode();
        h *= 1000003;
        h ^= (uri == null) ? 0 : uri.hashCode();
        h *= 1000003;
        h ^= (relative_uri == null) ? 0 : relative_uri.hashCode();
        h *= 1000003;
        h ^= (price_format == null) ? 0 : price_format.hashCode();
        h *= 1000003;
        h ^= (count_talk_format == null) ? 0 : count_talk_format.hashCode();
        h *= 1000003;
        h ^= (count_review_format == null) ? 0 : count_review_format.hashCode();
        h *= 1000003;
        h ^= (category == null) ? 0 : category.hashCode();
        h *= 1000003;
        h ^= (product_preorder == null) ? 0 : product_preorder.hashCode();
        h *= 1000003;
        h ^= (product_wholesale == null) ? 0 : product_wholesale.hashCode();
        h *= 1000003;
        h ^= (free_return == null) ? 0 : free_return.hashCode();
        h *= 1000003;
        h ^= (product_cashback == null) ? 0 : product_cashback.hashCode();
        h *= 1000003;
        h ^= (product_cashback_rate == null) ? 0 : product_cashback_rate.hashCode();
        h *= 1000003;
        h ^= (product_rating == null) ? 0 : product_rating.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Product2> {
        final Image.Mapper imageFieldMapper = new Image.Mapper();

        final Category.Mapper categoryFieldMapper = new Category.Mapper();

        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forObject("image", "image", null, true, new Field.ObjectReader<Image>() {
                  @Override public Image read(final ResponseReader reader) throws IOException {
                    return imageFieldMapper.map(reader);
                  }
                }),
                Field.forCustomType("uri", "uri", null, true, CustomType.URL),
                Field.forString("relative_uri", "relative_uri", null, true),
                Field.forString("price_format", "price_format", null, true),
                Field.forString("count_talk_format", "count_talk_format", null, true),
                Field.forString("count_review_format", "count_review_format", null, true),
                Field.forObject("category", "category", null, true, new Field.ObjectReader<Category>() {
                  @Override public Category read(final ResponseReader reader) throws IOException {
                    return categoryFieldMapper.map(reader);
                  }
                }),
                Field.forBoolean("product_preorder", "product_preorder", null, true),
                Field.forBoolean("product_wholesale", "product_wholesale", null, true),
                Field.forCustomType("free_return", "free_return", null, true, CustomType.URL),
                Field.forBoolean("product_cashback", "product_cashback", null, true),
                Field.forString("product_cashback_rate", "product_cashback_rate", null, true),
                Field.forInt("product_rating", "product_rating", null, true)
        };

        @Override
        public Product2 map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final Image image = reader.read(fields[2]);
          final Object uri = reader.read(fields[3]);
          final String relative_uri = reader.read(fields[4]);
          final String price_format = reader.read(fields[5]);
          final String count_talk_format = reader.read(fields[6]);
          final String count_review_format = reader.read(fields[7]);
          final Category category = reader.read(fields[8]);
          final Boolean product_preorder = reader.read(fields[9]);
          final Boolean product_wholesale = reader.read(fields[10]);
          final Object free_return = reader.read(fields[11]);
          final Boolean product_cashback = reader.read(fields[12]);
          final String product_cashback_rate = reader.read(fields[13]);
          final Integer product_rating = reader.read(fields[14]);
          return new Product2(id, name, image, uri, relative_uri, price_format, count_talk_format, count_review_format, category, product_preorder, product_wholesale, free_return, product_cashback, product_cashback_rate, product_rating);
        }
      }
    }

    public static class Image_product {
      private final @Nonnull String product_id;

      private final @Nonnull String product_name;

      private final @Nonnull Object image_url;

      public Image_product(@Nonnull String product_id, @Nonnull String product_name,
                           @Nonnull Object image_url) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
      }

      public @Nonnull String product_id() {
        return this.product_id;
      }

      public @Nonnull String product_name() {
        return this.product_name;
      }

      public @Nonnull Object image_url() {
        return this.image_url;
      }

      @Override
      public String toString() {
        return "Image_product{"
                + "product_id=" + product_id + ", "
                + "product_name=" + product_name + ", "
                + "image_url=" + image_url
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Image_product) {
          Image_product that = (Image_product) o;
          return ((this.product_id == null) ? (that.product_id == null) : this.product_id.equals(that.product_id))
                  && ((this.product_name == null) ? (that.product_name == null) : this.product_name.equals(that.product_name))
                  && ((this.image_url == null) ? (that.image_url == null) : this.image_url.equals(that.image_url));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (product_id == null) ? 0 : product_id.hashCode();
        h *= 1000003;
        h ^= (product_name == null) ? 0 : product_name.hashCode();
        h *= 1000003;
        h ^= (image_url == null) ? 0 : image_url.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Image_product> {
        final Field[] fields = {
                Field.forString("product_id", "product_id", null, false),
                Field.forString("product_name", "product_name", null, false),
                Field.forCustomType("image_url", "image_url", null, false, CustomType.URL)
        };

        @Override
        public Image_product map(ResponseReader reader) throws IOException {
          final String product_id = reader.read(fields[0]);
          final String product_name = reader.read(fields[1]);
          final Object image_url = reader.read(fields[2]);
          return new Image_product(product_id, product_name, image_url);
        }
      }
    }

    public static class Image_shop {
      private final @Nonnull Object cover_ecs;

      private final @Nonnull Object s_ecs;

      private final @Nonnull Object xs_ecs;

      private final @Nonnull Object s_url;

      private final @Nonnull Object xs_url;

      public Image_shop(@Nonnull Object cover_ecs, @Nonnull Object s_ecs, @Nonnull Object xs_ecs,
                        @Nonnull Object s_url, @Nonnull Object xs_url) {
        this.cover_ecs = cover_ecs;
        this.s_ecs = s_ecs;
        this.xs_ecs = xs_ecs;
        this.s_url = s_url;
        this.xs_url = xs_url;
      }

      public @Nonnull Object cover_ecs() {
        return this.cover_ecs;
      }

      public @Nonnull Object s_ecs() {
        return this.s_ecs;
      }

      public @Nonnull Object xs_ecs() {
        return this.xs_ecs;
      }

      public @Nonnull Object s_url() {
        return this.s_url;
      }

      public @Nonnull Object xs_url() {
        return this.xs_url;
      }

      @Override
      public String toString() {
        return "Image_shop{"
                + "cover_ecs=" + cover_ecs + ", "
                + "s_ecs=" + s_ecs + ", "
                + "xs_ecs=" + xs_ecs + ", "
                + "s_url=" + s_url + ", "
                + "xs_url=" + xs_url
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Image_shop) {
          Image_shop that = (Image_shop) o;
          return ((this.cover_ecs == null) ? (that.cover_ecs == null) : this.cover_ecs.equals(that.cover_ecs))
                  && ((this.s_ecs == null) ? (that.s_ecs == null) : this.s_ecs.equals(that.s_ecs))
                  && ((this.xs_ecs == null) ? (that.xs_ecs == null) : this.xs_ecs.equals(that.xs_ecs))
                  && ((this.s_url == null) ? (that.s_url == null) : this.s_url.equals(that.s_url))
                  && ((this.xs_url == null) ? (that.xs_url == null) : this.xs_url.equals(that.xs_url));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (cover_ecs == null) ? 0 : cover_ecs.hashCode();
        h *= 1000003;
        h ^= (s_ecs == null) ? 0 : s_ecs.hashCode();
        h *= 1000003;
        h ^= (xs_ecs == null) ? 0 : xs_ecs.hashCode();
        h *= 1000003;
        h ^= (s_url == null) ? 0 : s_url.hashCode();
        h *= 1000003;
        h ^= (xs_url == null) ? 0 : xs_url.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Image_shop> {
        final Field[] fields = {
                Field.forCustomType("cover_ecs", "cover_ecs", null, false, CustomType.URL),
                Field.forCustomType("s_ecs", "s_ecs", null, false, CustomType.URL),
                Field.forCustomType("xs_ecs", "xs_ecs", null, false, CustomType.URL),
                Field.forCustomType("s_url", "s_url", null, false, CustomType.URL),
                Field.forCustomType("xs_url", "xs_url", null, false, CustomType.URL)
        };

        @Override
        public Image_shop map(ResponseReader reader) throws IOException {
          final Object cover_ecs = reader.read(fields[0]);
          final Object s_ecs = reader.read(fields[1]);
          final Object xs_ecs = reader.read(fields[2]);
          final Object s_url = reader.read(fields[3]);
          final Object xs_url = reader.read(fields[4]);
          return new Image_shop(cover_ecs, s_ecs, xs_ecs, s_url, xs_url);
        }
      }
    }

    public static class Badge1 {
      private final @Nullable String title;

      private final @Nullable Object image_url;

      public Badge1(@Nullable String title, @Nullable Object image_url) {
        this.title = title;
        this.image_url = image_url;
      }

      public @Nullable String title() {
        return this.title;
      }

      public @Nullable Object image_url() {
        return this.image_url;
      }

      @Override
      public String toString() {
        return "Badge1{"
                + "title=" + title + ", "
                + "image_url=" + image_url
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Badge1) {
          Badge1 that = (Badge1) o;
          return ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.image_url == null) ? (that.image_url == null) : this.image_url.equals(that.image_url));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (image_url == null) ? 0 : image_url.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Badge1> {
        final Field[] fields = {
                Field.forString("title", "title", null, true),
                Field.forCustomType("image_url", "image_url", null, true, CustomType.URL)
        };

        @Override
        public Badge1 map(ResponseReader reader) throws IOException {
          final String title = reader.read(fields[0]);
          final Object image_url = reader.read(fields[1]);
          return new Badge1(title, image_url);
        }
      }
    }

    public static class Shop2 {
      private final @Nullable String id;

      private final @Nullable String name;

      private final @Nullable String domain;

      private final @Nullable String tagline;

      private final @Nullable String location;

      private final @Nullable String city;

      private final @Nullable List<Image_product> image_product;

      private final @Nullable Image_shop image_shop;

      private final @Nullable Boolean gold_shop;

      private final @Nullable Object lucky_shop;

      private final @Nullable Boolean shop_is_official;

      private final @Nullable String owner_id;

      private final @Nullable Boolean is_owner;

      private final @Nullable List<Badge1> badges;

      private final @Nullable Object uri;

      private final @Nullable Boolean gold_shop_badge;

      public Shop2(@Nullable String id, @Nullable String name, @Nullable String domain,
                   @Nullable String tagline, @Nullable String location, @Nullable String city,
                   @Nullable List<Image_product> image_product, @Nullable Image_shop image_shop,
                   @Nullable Boolean gold_shop, @Nullable Object lucky_shop,
                   @Nullable Boolean shop_is_official, @Nullable String owner_id, @Nullable Boolean is_owner,
                   @Nullable List<Badge1> badges, @Nullable Object uri, @Nullable Boolean gold_shop_badge) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.tagline = tagline;
        this.location = location;
        this.city = city;
        this.image_product = image_product;
        this.image_shop = image_shop;
        this.gold_shop = gold_shop;
        this.lucky_shop = lucky_shop;
        this.shop_is_official = shop_is_official;
        this.owner_id = owner_id;
        this.is_owner = is_owner;
        this.badges = badges;
        this.uri = uri;
        this.gold_shop_badge = gold_shop_badge;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String domain() {
        return this.domain;
      }

      public @Nullable String tagline() {
        return this.tagline;
      }

      public @Nullable String location() {
        return this.location;
      }

      public @Nullable String city() {
        return this.city;
      }

      public @Nullable List<Image_product> image_product() {
        return this.image_product;
      }

      public @Nullable Image_shop image_shop() {
        return this.image_shop;
      }

      public @Nullable Boolean gold_shop() {
        return this.gold_shop;
      }

      public @Nullable Object lucky_shop() {
        return this.lucky_shop;
      }

      public @Nullable Boolean shop_is_official() {
        return this.shop_is_official;
      }

      public @Nullable String owner_id() {
        return this.owner_id;
      }

      public @Nullable Boolean is_owner() {
        return this.is_owner;
      }

      public @Nullable List<Badge1> badges() {
        return this.badges;
      }

      public @Nullable Object uri() {
        return this.uri;
      }

      public @Nullable Boolean gold_shop_badge() {
        return this.gold_shop_badge;
      }

      @Override
      public String toString() {
        return "Shop2{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "domain=" + domain + ", "
                + "tagline=" + tagline + ", "
                + "location=" + location + ", "
                + "city=" + city + ", "
                + "image_product=" + image_product + ", "
                + "image_shop=" + image_shop + ", "
                + "gold_shop=" + gold_shop + ", "
                + "lucky_shop=" + lucky_shop + ", "
                + "shop_is_official=" + shop_is_official + ", "
                + "owner_id=" + owner_id + ", "
                + "is_owner=" + is_owner + ", "
                + "badges=" + badges + ", "
                + "uri=" + uri + ", "
                + "gold_shop_badge=" + gold_shop_badge
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Shop2) {
          Shop2 that = (Shop2) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.domain == null) ? (that.domain == null) : this.domain.equals(that.domain))
                  && ((this.tagline == null) ? (that.tagline == null) : this.tagline.equals(that.tagline))
                  && ((this.location == null) ? (that.location == null) : this.location.equals(that.location))
                  && ((this.city == null) ? (that.city == null) : this.city.equals(that.city))
                  && ((this.image_product == null) ? (that.image_product == null) : this.image_product.equals(that.image_product))
                  && ((this.image_shop == null) ? (that.image_shop == null) : this.image_shop.equals(that.image_shop))
                  && ((this.gold_shop == null) ? (that.gold_shop == null) : this.gold_shop.equals(that.gold_shop))
                  && ((this.lucky_shop == null) ? (that.lucky_shop == null) : this.lucky_shop.equals(that.lucky_shop))
                  && ((this.shop_is_official == null) ? (that.shop_is_official == null) : this.shop_is_official.equals(that.shop_is_official))
                  && ((this.owner_id == null) ? (that.owner_id == null) : this.owner_id.equals(that.owner_id))
                  && ((this.is_owner == null) ? (that.is_owner == null) : this.is_owner.equals(that.is_owner))
                  && ((this.badges == null) ? (that.badges == null) : this.badges.equals(that.badges))
                  && ((this.uri == null) ? (that.uri == null) : this.uri.equals(that.uri))
                  && ((this.gold_shop_badge == null) ? (that.gold_shop_badge == null) : this.gold_shop_badge.equals(that.gold_shop_badge));
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
        h ^= (domain == null) ? 0 : domain.hashCode();
        h *= 1000003;
        h ^= (tagline == null) ? 0 : tagline.hashCode();
        h *= 1000003;
        h ^= (location == null) ? 0 : location.hashCode();
        h *= 1000003;
        h ^= (city == null) ? 0 : city.hashCode();
        h *= 1000003;
        h ^= (image_product == null) ? 0 : image_product.hashCode();
        h *= 1000003;
        h ^= (image_shop == null) ? 0 : image_shop.hashCode();
        h *= 1000003;
        h ^= (gold_shop == null) ? 0 : gold_shop.hashCode();
        h *= 1000003;
        h ^= (lucky_shop == null) ? 0 : lucky_shop.hashCode();
        h *= 1000003;
        h ^= (shop_is_official == null) ? 0 : shop_is_official.hashCode();
        h *= 1000003;
        h ^= (owner_id == null) ? 0 : owner_id.hashCode();
        h *= 1000003;
        h ^= (is_owner == null) ? 0 : is_owner.hashCode();
        h *= 1000003;
        h ^= (badges == null) ? 0 : badges.hashCode();
        h *= 1000003;
        h ^= (uri == null) ? 0 : uri.hashCode();
        h *= 1000003;
        h ^= (gold_shop_badge == null) ? 0 : gold_shop_badge.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Shop2> {
        final Image_product.Mapper image_productFieldMapper = new Image_product.Mapper();

        final Image_shop.Mapper image_shopFieldMapper = new Image_shop.Mapper();

        final Badge1.Mapper badge1FieldMapper = new Badge1.Mapper();

        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forString("domain", "domain", null, true),
                Field.forString("tagline", "tagline", null, true),
                Field.forString("location", "location", null, true),
                Field.forString("city", "city", null, true),
                Field.forList("image_product", "image_product", null, true, new Field.ObjectReader<Image_product>() {
                  @Override public Image_product read(final ResponseReader reader) throws IOException {
                    return image_productFieldMapper.map(reader);
                  }
                }),
                Field.forObject("image_shop", "image_shop", null, true, new Field.ObjectReader<Image_shop>() {
                  @Override public Image_shop read(final ResponseReader reader) throws IOException {
                    return image_shopFieldMapper.map(reader);
                  }
                }),
                Field.forBoolean("gold_shop", "gold_shop", null, true),
                Field.forCustomType("lucky_shop", "lucky_shop", null, true, CustomType.URL),
                Field.forBoolean("shop_is_official", "shop_is_official", null, true),
                Field.forString("owner_id", "owner_id", null, true),
                Field.forBoolean("is_owner", "is_owner", null, true),
                Field.forList("badges", "badges", null, true, new Field.ObjectReader<Badge1>() {
                  @Override public Badge1 read(final ResponseReader reader) throws IOException {
                    return badge1FieldMapper.map(reader);
                  }
                }),
                Field.forCustomType("uri", "uri", null, true, CustomType.URL),
                Field.forBoolean("gold_shop_badge", "gold_shop_badge", null, true)
        };

        @Override
        public Shop2 map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String domain = reader.read(fields[2]);
          final String tagline = reader.read(fields[3]);
          final String location = reader.read(fields[4]);
          final String city = reader.read(fields[5]);
          final List<Image_product> image_product = reader.read(fields[6]);
          final Image_shop image_shop = reader.read(fields[7]);
          final Boolean gold_shop = reader.read(fields[8]);
          final Object lucky_shop = reader.read(fields[9]);
          final Boolean shop_is_official = reader.read(fields[10]);
          final String owner_id = reader.read(fields[11]);
          final Boolean is_owner = reader.read(fields[12]);
          final List<Badge1> badges = reader.read(fields[13]);
          final Object uri = reader.read(fields[14]);
          final Boolean gold_shop_badge = reader.read(fields[15]);
          return new Shop2(id, name, domain, tagline, location, city, image_product, image_shop, gold_shop, lucky_shop, shop_is_official, owner_id, is_owner, badges, uri, gold_shop_badge);
        }
      }
    }

    public static class Topad {
      private final @Nullable String id;

      private final @Nullable String ad_ref_key;

      private final @Nullable Object redirect;

      private final @Nullable String sticker_id;

      private final @Nullable Object sticker_image;

      private final @Nullable Object product_click_url;

      private final @Nullable Object shop_click_url;

      private final @Nullable Product2 product;

      private final @Nullable Shop2 shop;

      private final @Nullable String applinks;

      public Topad(@Nullable String id, @Nullable String ad_ref_key, @Nullable Object redirect,
                   @Nullable String sticker_id, @Nullable Object sticker_image,
                   @Nullable Object product_click_url, @Nullable Object shop_click_url,
                   @Nullable Product2 product, @Nullable Shop2 shop, @Nullable String applinks) {
        this.id = id;
        this.ad_ref_key = ad_ref_key;
        this.redirect = redirect;
        this.sticker_id = sticker_id;
        this.sticker_image = sticker_image;
        this.product_click_url = product_click_url;
        this.shop_click_url = shop_click_url;
        this.product = product;
        this.shop = shop;
        this.applinks = applinks;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String ad_ref_key() {
        return this.ad_ref_key;
      }

      public @Nullable Object redirect() {
        return this.redirect;
      }

      public @Nullable String sticker_id() {
        return this.sticker_id;
      }

      public @Nullable Object sticker_image() {
        return this.sticker_image;
      }

      public @Nullable Object product_click_url() {
        return this.product_click_url;
      }

      public @Nullable Object shop_click_url() {
        return this.shop_click_url;
      }

      public @Nullable Product2 product() {
        return this.product;
      }

      public @Nullable Shop2 shop() {
        return this.shop;
      }

      public @Nullable String applinks() {
        return this.applinks;
      }

      @Override
      public String toString() {
        return "Topad{"
                + "id=" + id + ", "
                + "ad_ref_key=" + ad_ref_key + ", "
                + "redirect=" + redirect + ", "
                + "sticker_id=" + sticker_id + ", "
                + "sticker_image=" + sticker_image + ", "
                + "product_click_url=" + product_click_url + ", "
                + "shop_click_url=" + shop_click_url + ", "
                + "product=" + product + ", "
                + "shop=" + shop + ", "
                + "applinks=" + applinks
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Topad) {
          Topad that = (Topad) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.ad_ref_key == null) ? (that.ad_ref_key == null) : this.ad_ref_key.equals(that.ad_ref_key))
                  && ((this.redirect == null) ? (that.redirect == null) : this.redirect.equals(that.redirect))
                  && ((this.sticker_id == null) ? (that.sticker_id == null) : this.sticker_id.equals(that.sticker_id))
                  && ((this.sticker_image == null) ? (that.sticker_image == null) : this.sticker_image.equals(that.sticker_image))
                  && ((this.product_click_url == null) ? (that.product_click_url == null) : this.product_click_url.equals(that.product_click_url))
                  && ((this.shop_click_url == null) ? (that.shop_click_url == null) : this.shop_click_url.equals(that.shop_click_url))
                  && ((this.product == null) ? (that.product == null) : this.product.equals(that.product))
                  && ((this.shop == null) ? (that.shop == null) : this.shop.equals(that.shop))
                  && ((this.applinks == null) ? (that.applinks == null) : this.applinks.equals(that.applinks));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (ad_ref_key == null) ? 0 : ad_ref_key.hashCode();
        h *= 1000003;
        h ^= (redirect == null) ? 0 : redirect.hashCode();
        h *= 1000003;
        h ^= (sticker_id == null) ? 0 : sticker_id.hashCode();
        h *= 1000003;
        h ^= (sticker_image == null) ? 0 : sticker_image.hashCode();
        h *= 1000003;
        h ^= (product_click_url == null) ? 0 : product_click_url.hashCode();
        h *= 1000003;
        h ^= (shop_click_url == null) ? 0 : shop_click_url.hashCode();
        h *= 1000003;
        h ^= (product == null) ? 0 : product.hashCode();
        h *= 1000003;
        h ^= (shop == null) ? 0 : shop.hashCode();
        h *= 1000003;
        h ^= (applinks == null) ? 0 : applinks.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Topad> {
        final Product2.Mapper product2FieldMapper = new Product2.Mapper();

        final Shop2.Mapper shop2FieldMapper = new Shop2.Mapper();

        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("ad_ref_key", "ad_ref_key", null, true),
                Field.forCustomType("redirect", "redirect", null, true, CustomType.URL),
                Field.forString("sticker_id", "sticker_id", null, true),
                Field.forCustomType("sticker_image", "sticker_image", null, true, CustomType.URL),
                Field.forCustomType("product_click_url", "product_click_url", null, true, CustomType.URL),
                Field.forCustomType("shop_click_url", "shop_click_url", null, true, CustomType.URL),
                Field.forObject("product", "product", null, true, new Field.ObjectReader<Product2>() {
                  @Override public Product2 read(final ResponseReader reader) throws IOException {
                    return product2FieldMapper.map(reader);
                  }
                }),
                Field.forObject("shop", "shop", null, true, new Field.ObjectReader<Shop2>() {
                  @Override public Shop2 read(final ResponseReader reader) throws IOException {
                    return shop2FieldMapper.map(reader);
                  }
                }),
                Field.forString("applinks", "applinks", null, true)
        };

        @Override
        public Topad map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String ad_ref_key = reader.read(fields[1]);
          final Object redirect = reader.read(fields[2]);
          final String sticker_id = reader.read(fields[3]);
          final Object sticker_image = reader.read(fields[4]);
          final Object product_click_url = reader.read(fields[5]);
          final Object shop_click_url = reader.read(fields[6]);
          final Product2 product = reader.read(fields[7]);
          final Shop2 shop = reader.read(fields[8]);
          final String applinks = reader.read(fields[9]);
          return new Topad(id, ad_ref_key, redirect, sticker_id, sticker_image, product_click_url, shop_click_url, product, shop, applinks);
        }
      }
    }

    public static class Content {
      private final @Nullable String type;

      private final @Nullable String display;

      private final @Nullable Integer total_product;

      private final @Nullable List<Product> products;

      private final @Nullable List<Promotion> promotions;

      private final @Nullable List<Official_store> official_store;

      private final @Nullable String redirect_url_app;

      private final @Nullable Seller_story seller_story;

      private final @Nullable List<Top_pick> top_picks;

      private final @Nullable String status_activity;

      private final @Nullable New_status_activity new_status_activity;

      private final @Nullable List<Inspirasi> inspirasi;

      private final @Nullable Kolpost kolpost;

      private final @Nullable Followedkolpost followedkolpost;

      private final @Nullable Kolrecommendation kolrecommendation;

      private final @Nullable Favorite_cta favorite_cta;

      private final @Nullable Kol_cta kol_cta;

      private final @Nullable List<Topad> topads;

      public Content(@Nullable String type, @Nullable String display,
                     @Nullable Integer total_product, @Nullable List<Product> products,
                     @Nullable List<Promotion> promotions, @Nullable List<Official_store> official_store,
                     @Nullable String redirect_url_app, @Nullable Seller_story seller_story,
                     @Nullable List<Top_pick> top_picks, @Nullable String status_activity,
                     @Nullable New_status_activity new_status_activity, @Nullable List<Inspirasi> inspirasi,
                     @Nullable Kolpost kolpost, @Nullable Followedkolpost followedkolpost,
                     @Nullable Kolrecommendation kolrecommendation, @Nullable Favorite_cta favorite_cta,
                     @Nullable Kol_cta kol_cta, @Nullable List<Topad> topads) {
        this.type = type;
        this.display = display;
        this.total_product = total_product;
        this.products = products;
        this.promotions = promotions;
        this.official_store = official_store;
        this.redirect_url_app = redirect_url_app;
        this.seller_story = seller_story;
        this.top_picks = top_picks;
        this.status_activity = status_activity;
        this.new_status_activity = new_status_activity;
        this.inspirasi = inspirasi;
        this.kolpost = kolpost;
        this.followedkolpost = followedkolpost;
        this.kolrecommendation = kolrecommendation;
        this.favorite_cta = favorite_cta;
        this.kol_cta = kol_cta;
        this.topads = topads;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable String display() {
        return this.display;
      }

      public @Nullable Integer total_product() {
        return this.total_product;
      }

      public @Nullable List<Product> products() {
        return this.products;
      }

      public @Nullable List<Promotion> promotions() {
        return this.promotions;
      }

      public @Nullable List<Official_store> official_store() {
        return this.official_store;
      }

      public @Nullable String redirect_url_app() {
        return this.redirect_url_app;
      }

      public @Nullable Seller_story seller_story() {
        return this.seller_story;
      }

      public @Nullable List<Top_pick> top_picks() {
        return this.top_picks;
      }

      public @Nullable String status_activity() {
        return this.status_activity;
      }

      public @Nullable New_status_activity new_status_activity() {
        return this.new_status_activity;
      }

      public @Nullable List<Inspirasi> inspirasi() {
        return this.inspirasi;
      }

      public @Nullable Kolpost kolpost() {
        return this.kolpost;
      }

      public @Nullable Followedkolpost followedkolpost() {
        return this.followedkolpost;
      }

      public @Nullable Kolrecommendation kolrecommendation() {
        return this.kolrecommendation;
      }

      public @Nullable Favorite_cta favorite_cta() {
        return this.favorite_cta;
      }

      public @Nullable Kol_cta kol_cta() {
        return this.kol_cta;
      }

      public @Nullable List<Topad> topads() {
        return this.topads;
      }

      @Override
      public String toString() {
        return "Content{"
                + "type=" + type + ", "
                + "display=" + display + ", "
                + "total_product=" + total_product + ", "
                + "products=" + products + ", "
                + "promotions=" + promotions + ", "
                + "official_store=" + official_store + ", "
                + "redirect_url_app=" + redirect_url_app + ", "
                + "seller_story=" + seller_story + ", "
                + "top_picks=" + top_picks + ", "
                + "status_activity=" + status_activity + ", "
                + "new_status_activity=" + new_status_activity + ", "
                + "inspirasi=" + inspirasi + ", "
                + "kolpost=" + kolpost + ", "
                + "followedkolpost=" + followedkolpost + ", "
                + "kolrecommendation=" + kolrecommendation + ", "
                + "favorite_cta=" + favorite_cta + ", "
                + "kol_cta=" + kol_cta + ", "
                + "topads=" + topads
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
                  && ((this.display == null) ? (that.display == null) : this.display.equals(that.display))
                  && ((this.total_product == null) ? (that.total_product == null) : this.total_product.equals(that.total_product))
                  && ((this.products == null) ? (that.products == null) : this.products.equals(that.products))
                  && ((this.promotions == null) ? (that.promotions == null) : this.promotions.equals(that.promotions))
                  && ((this.official_store == null) ? (that.official_store == null) : this.official_store.equals(that.official_store))
                  && ((this.redirect_url_app == null) ? (that.redirect_url_app == null) : this.redirect_url_app.equals(that.redirect_url_app))
                  && ((this.seller_story == null) ? (that.seller_story == null) : this.seller_story.equals(that.seller_story))
                  && ((this.top_picks == null) ? (that.top_picks == null) : this.top_picks.equals(that.top_picks))
                  && ((this.status_activity == null) ? (that.status_activity == null) : this.status_activity.equals(that.status_activity))
                  && ((this.new_status_activity == null) ? (that.new_status_activity == null) : this.new_status_activity.equals(that.new_status_activity))
                  && ((this.inspirasi == null) ? (that.inspirasi == null) : this.inspirasi.equals(that.inspirasi))
                  && ((this.kolpost == null) ? (that.kolpost == null) : this.kolpost.equals(that.kolpost))
                  && ((this.followedkolpost == null) ? (that.followedkolpost == null) : this.followedkolpost.equals(that.followedkolpost))
                  && ((this.kolrecommendation == null) ? (that.kolrecommendation == null) : this.kolrecommendation.equals(that.kolrecommendation))
                  && ((this.favorite_cta == null) ? (that.favorite_cta == null) : this.favorite_cta.equals(that.favorite_cta))
                  && ((this.kol_cta == null) ? (that.kol_cta == null) : this.kol_cta.equals(that.kol_cta))
                  && ((this.topads == null) ? (that.topads == null) : this.topads.equals(that.topads));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (display == null) ? 0 : display.hashCode();
        h *= 1000003;
        h ^= (total_product == null) ? 0 : total_product.hashCode();
        h *= 1000003;
        h ^= (products == null) ? 0 : products.hashCode();
        h *= 1000003;
        h ^= (promotions == null) ? 0 : promotions.hashCode();
        h *= 1000003;
        h ^= (official_store == null) ? 0 : official_store.hashCode();
        h *= 1000003;
        h ^= (redirect_url_app == null) ? 0 : redirect_url_app.hashCode();
        h *= 1000003;
        h ^= (seller_story == null) ? 0 : seller_story.hashCode();
        h *= 1000003;
        h ^= (top_picks == null) ? 0 : top_picks.hashCode();
        h *= 1000003;
        h ^= (status_activity == null) ? 0 : status_activity.hashCode();
        h *= 1000003;
        h ^= (new_status_activity == null) ? 0 : new_status_activity.hashCode();
        h *= 1000003;
        h ^= (inspirasi == null) ? 0 : inspirasi.hashCode();
        h *= 1000003;
        h ^= (kolpost == null) ? 0 : kolpost.hashCode();
        h *= 1000003;
        h ^= (followedkolpost == null) ? 0 : followedkolpost.hashCode();
        h *= 1000003;
        h ^= (kolrecommendation == null) ? 0 : kolrecommendation.hashCode();
        h *= 1000003;
        h ^= (favorite_cta == null) ? 0 : favorite_cta.hashCode();
        h *= 1000003;
        h ^= (kol_cta == null) ? 0 : kol_cta.hashCode();
        h *= 1000003;
        h ^= (topads == null) ? 0 : topads.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Content> {
        final Product.Mapper productFieldMapper = new Product.Mapper();

        final Promotion.Mapper promotionFieldMapper = new Promotion.Mapper();

        final Official_store.Mapper official_storeFieldMapper = new Official_store.Mapper();

        final Seller_story.Mapper seller_storyFieldMapper = new Seller_story.Mapper();

        final Top_pick.Mapper top_pickFieldMapper = new Top_pick.Mapper();

        final New_status_activity.Mapper new_status_activityFieldMapper = new New_status_activity.Mapper();

        final Inspirasi.Mapper inspirasiFieldMapper = new Inspirasi.Mapper();

        final Kolpost.Mapper kolpostFieldMapper = new Kolpost.Mapper();

        final Followedkolpost.Mapper followedkolpostFieldMapper = new Followedkolpost.Mapper();

        final Kolrecommendation.Mapper kolrecommendationFieldMapper = new Kolrecommendation.Mapper();

        final Favorite_cta.Mapper favorite_ctaFieldMapper = new Favorite_cta.Mapper();

        final Kol_cta.Mapper kol_ctaFieldMapper = new Kol_cta.Mapper();

        final Topad.Mapper topadFieldMapper = new Topad.Mapper();

        final Field[] fields = {
                Field.forString("type", "type", null, true),
                Field.forString("display", "display", null, true),
                Field.forInt("total_product", "total_product", null, true),
                Field.forList("products", "products", null, true, new Field.ObjectReader<Product>() {
                  @Override public Product read(final ResponseReader reader) throws IOException {
                    return productFieldMapper.map(reader);
                  }
                }),
                Field.forList("promotions", "promotions", null, true, new Field.ObjectReader<Promotion>() {
                  @Override public Promotion read(final ResponseReader reader) throws IOException {
                    return promotionFieldMapper.map(reader);
                  }
                }),
                Field.forList("official_store", "official_store", null, true, new Field.ObjectReader<Official_store>() {
                  @Override public Official_store read(final ResponseReader reader) throws IOException {
                    return official_storeFieldMapper.map(reader);
                  }
                }),
                Field.forString("redirect_url_app", "redirect_url_app", null, true),
                Field.forObject("seller_story", "seller_story", null, true, new Field.ObjectReader<Seller_story>() {
                  @Override public Seller_story read(final ResponseReader reader) throws IOException {
                    return seller_storyFieldMapper.map(reader);
                  }
                }),
                Field.forList("top_picks", "top_picks", null, true, new Field.ObjectReader<Top_pick>() {
                  @Override public Top_pick read(final ResponseReader reader) throws IOException {
                    return top_pickFieldMapper.map(reader);
                  }
                }),
                Field.forString("status_activity", "status_activity", null, true),
                Field.forObject("new_status_activity", "new_status_activity", null, true, new Field.ObjectReader<New_status_activity>() {
                  @Override public New_status_activity read(final ResponseReader reader) throws IOException {
                    return new_status_activityFieldMapper.map(reader);
                  }
                }),
                Field.forList("inspirasi", "inspirasi", null, true, new Field.ObjectReader<Inspirasi>() {
                  @Override public Inspirasi read(final ResponseReader reader) throws IOException {
                    return inspirasiFieldMapper.map(reader);
                  }
                }),
                Field.forObject("kolpost", "kolpost", null, true, new Field.ObjectReader<Kolpost>() {
                  @Override public Kolpost read(final ResponseReader reader) throws IOException {
                    return kolpostFieldMapper.map(reader);
                  }
                }),
                Field.forObject("followedkolpost", "followedkolpost", null, true, new Field.ObjectReader<Followedkolpost>() {
                  @Override public Followedkolpost read(final ResponseReader reader) throws IOException {
                    return followedkolpostFieldMapper.map(reader);
                  }
                }),
                Field.forObject("kolrecommendation", "kolrecommendation", null, true, new Field.ObjectReader<Kolrecommendation>() {
                  @Override public Kolrecommendation read(final ResponseReader reader) throws IOException {
                    return kolrecommendationFieldMapper.map(reader);
                  }
                }),
                Field.forObject("favorite_cta", "favorite_cta", null, true, new Field.ObjectReader<Favorite_cta>() {
                  @Override public Favorite_cta read(final ResponseReader reader) throws IOException {
                    return favorite_ctaFieldMapper.map(reader);
                  }
                }),
                Field.forObject("kol_cta", "kol_cta", null, true, new Field.ObjectReader<Kol_cta>() {
                  @Override public Kol_cta read(final ResponseReader reader) throws IOException {
                    return kol_ctaFieldMapper.map(reader);
                  }
                }),
                Field.forList("topads", "topads", null, true, new Field.ObjectReader<Topad>() {
                  @Override public Topad read(final ResponseReader reader) throws IOException {
                    return topadFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Content map(ResponseReader reader) throws IOException {
          final String type = reader.read(fields[0]);
          final String display = reader.read(fields[1]);
          final Integer total_product = reader.read(fields[2]);
          final List<Product> products = reader.read(fields[3]);
          final List<Promotion> promotions = reader.read(fields[4]);
          final List<Official_store> official_store = reader.read(fields[5]);
          final String redirect_url_app = reader.read(fields[6]);
          final Seller_story seller_story = reader.read(fields[7]);
          final List<Top_pick> top_picks = reader.read(fields[8]);
          final String status_activity = reader.read(fields[9]);
          final New_status_activity new_status_activity = reader.read(fields[10]);
          final List<Inspirasi> inspirasi = reader.read(fields[11]);
          final Kolpost kolpost = reader.read(fields[12]);
          final Followedkolpost followedkolpost = reader.read(fields[13]);
          final Kolrecommendation kolrecommendation = reader.read(fields[14]);
          final Favorite_cta favorite_cta = reader.read(fields[15]);
          final Kol_cta kol_cta = reader.read(fields[16]);
          final List<Topad> topads = reader.read(fields[17]);
          return new Content(type, display, total_product, products, promotions, official_store, redirect_url_app, seller_story, top_picks, status_activity, new_status_activity, inspirasi, kolpost, followedkolpost, kolrecommendation, favorite_cta, kol_cta, topads);
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

      public Datum(@Nullable String id, @Nullable String create_time, @Nullable String type,
                   @Nullable String cursor, @Nullable Source source, @Nullable Content content) {
        this.id = id;
        this.create_time = create_time;
        this.type = type;
        this.cursor = cursor;
        this.source = source;
        this.content = content;
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

      @Override
      public String toString() {
        return "Datum{"
                + "id=" + id + ", "
                + "create_time=" + create_time + ", "
                + "type=" + type + ", "
                + "cursor=" + cursor + ", "
                + "source=" + source + ", "
                + "content=" + content
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
                  && ((this.content == null) ? (that.content == null) : this.content.equals(that.content));
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
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Datum> {
        final Source.Mapper sourceFieldMapper = new Source.Mapper();

        final Content.Mapper contentFieldMapper = new Content.Mapper();

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
          return new Datum(id, create_time, type, cursor, source, content);
        }
      }
    }

    public static class Pagination1 {
      private final @Nullable Boolean has_next_page;

      public Pagination1(@Nullable Boolean has_next_page) {
        this.has_next_page = has_next_page;
      }

      public @Nullable Boolean has_next_page() {
        return this.has_next_page;
      }

      @Override
      public String toString() {
        return "Pagination1{"
                + "has_next_page=" + has_next_page
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Pagination1) {
          Pagination1 that = (Pagination1) o;
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

      public static final class Mapper implements ResponseFieldMapper<Pagination1> {
        final Field[] fields = {
                Field.forBoolean("has_next_page", "has_next_page", null, true)
        };

        @Override
        public Pagination1 map(ResponseReader reader) throws IOException {
          final Boolean has_next_page = reader.read(fields[0]);
          return new Pagination1(has_next_page);
        }
      }
    }

    public static class Links {
      private final @Nullable String self;

      private final @Nullable Pagination1 pagination;

      public Links(@Nullable String self, @Nullable Pagination1 pagination) {
        this.self = self;
        this.pagination = pagination;
      }

      public @Nullable String self() {
        return this.self;
      }

      public @Nullable Pagination1 pagination() {
        return this.pagination;
      }

      @Override
      public String toString() {
        return "Links{"
                + "self=" + self + ", "
                + "pagination=" + pagination
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Links) {
          Links that = (Links) o;
          return ((this.self == null) ? (that.self == null) : this.self.equals(that.self))
                  && ((this.pagination == null) ? (that.pagination == null) : this.pagination.equals(that.pagination));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (self == null) ? 0 : self.hashCode();
        h *= 1000003;
        h ^= (pagination == null) ? 0 : pagination.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Links> {
        final Pagination1.Mapper pagination1FieldMapper = new Pagination1.Mapper();

        final Field[] fields = {
                Field.forString("self", "self", null, true),
                Field.forObject("pagination", "pagination", null, true, new Field.ObjectReader<Pagination1>() {
                  @Override public Pagination1 read(final ResponseReader reader) throws IOException {
                    return pagination1FieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Links map(ResponseReader reader) throws IOException {
          final String self = reader.read(fields[0]);
          final Pagination1 pagination = reader.read(fields[1]);
          return new Links(self, pagination);
        }
      }
    }

    public static class Meta {
      private final @Nullable Integer total_data;

      public Meta(@Nullable Integer total_data) {
        this.total_data = total_data;
      }

      public @Nullable Integer total_data() {
        return this.total_data;
      }

      @Override
      public String toString() {
        return "Meta{"
                + "total_data=" + total_data
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Meta) {
          Meta that = (Meta) o;
          return ((this.total_data == null) ? (that.total_data == null) : this.total_data.equals(that.total_data));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (total_data == null) ? 0 : total_data.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Meta> {
        final Field[] fields = {
                Field.forInt("total_data", "total_data", null, true)
        };

        @Override
        public Meta map(ResponseReader reader) throws IOException {
          final Integer total_data = reader.read(fields[0]);
          return new Meta(total_data);
        }
      }
    }

    public static class Feed {
      private final @Nullable List<Datum> data;

      private final @Nullable Links links;

      private final @Nullable Meta meta;

      public Feed(@Nullable List<Datum> data, @Nullable Links links, @Nullable Meta meta) {
        this.data = data;
        this.links = links;
        this.meta = meta;
      }

      public @Nullable List<Datum> data() {
        return this.data;
      }

      public @Nullable Links links() {
        return this.links;
      }

      public @Nullable Meta meta() {
        return this.meta;
      }

      @Override
      public String toString() {
        return "Feed{"
                + "data=" + data + ", "
                + "links=" + links + ", "
                + "meta=" + meta
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
                  && ((this.links == null) ? (that.links == null) : this.links.equals(that.links))
                  && ((this.meta == null) ? (that.meta == null) : this.meta.equals(that.meta));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (data == null) ? 0 : data.hashCode();
        h *= 1000003;
        h ^= (links == null) ? 0 : links.hashCode();
        h *= 1000003;
        h ^= (meta == null) ? 0 : meta.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Feed> {
        final Datum.Mapper datumFieldMapper = new Datum.Mapper();

        final Links.Mapper linksFieldMapper = new Links.Mapper();

        final Meta.Mapper metaFieldMapper = new Meta.Mapper();

        final Field[] fields = {
                Field.forList("data", "data", null, true, new Field.ObjectReader<Datum>() {
                  @Override public Datum read(final ResponseReader reader) throws IOException {
                    return datumFieldMapper.map(reader);
                  }
                }),
                Field.forObject("links", "links", null, true, new Field.ObjectReader<Links>() {
                  @Override public Links read(final ResponseReader reader) throws IOException {
                    return linksFieldMapper.map(reader);
                  }
                }),
                Field.forObject("meta", "meta", null, true, new Field.ObjectReader<Meta>() {
                  @Override public Meta read(final ResponseReader reader) throws IOException {
                    return metaFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Feed map(ResponseReader reader) throws IOException {
          final List<Datum> data = reader.read(fields[0]);
          final Links links = reader.read(fields[1]);
          final Meta meta = reader.read(fields[2]);
          return new Feed(data, links, meta);
        }
      }
    }
  }
}
