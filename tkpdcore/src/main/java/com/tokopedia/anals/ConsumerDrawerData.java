package com.tokopedia.anals;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class ConsumerDrawerData implements Query<ConsumerDrawerData.Data, ConsumerDrawerData.Data, ConsumerDrawerData.Variables> {
  public static final String OPERATION_DEFINITION = "query ConsumerDrawerData($userID: Int!) {\n"
      + "  shopInfoMoengage(userID: $userID) {\n"
      + "    __typename\n"
      + "    info {\n"
      + "      __typename\n"
      + "      date_shop_created\n"
      + "      shop_id\n"
      + "      shop_location\n"
      + "      shop_name\n"
      + "      shop_score\n"
      + "      total_active_product\n"
      + "      shop_avatar\n"
      + "      shop_cover\n"
      + "      shop_domain\n"
      + "    }\n"
      + "    owner {\n"
      + "      __typename\n"
      + "      is_gold_merchant\n"
      + "      is_seller\n"
      + "    }\n"
      + "    stats {\n"
      + "      __typename\n"
      + "      shop_total_transaction\n"
      + "      shop_item_sold\n"
      + "    }\n"
      + "  }\n"
      + "  profile {\n"
      + "    __typename\n"
      + "    user_id\n"
      + "    first_name\n"
      + "    full_name\n"
      + "    email\n"
      + "    gender\n"
      + "    bday\n"
      + "    age\n"
      + "    phone\n"
      + "    phone_verified\n"
      + "    register_date\n"
      + "    profile_picture\n"
      + "    completion\n"
      + "  }\n"
      + "  address {\n"
      + "    __typename\n"
      + "    addresses {\n"
      + "      __typename\n"
      + "      city_name\n"
      + "      province_name\n"
      + "    }\n"
      + "  }\n"
      + "  wallet {\n"
      + "    __typename\n"
      + "    linked\n"
      + "    balance\n"
      + "    rawBalance\n"
      + "    errors {\n"
      + "      __typename\n"
      + "      name\n"
      + "      message\n"
      + "    }\n"
      + "    text\n"
      + "    total_balance\n"
      + "    raw_total_balance\n"
      + "    hold_balance\n"
      + "    raw_hold_balance\n"
      + "    redirect_url\n"
      + "    applinks\n"
      + "    ab_tags {\n"
      + "      __typename\n"
      + "      tag\n"
      + "    }\n"
      + "    action {\n"
      + "      __typename\n"
      + "      text\n"
      + "      redirect_url\n"
      + "      applinks\n"
      + "      visibility\n"
      + "    }\n"
      + "  }\n"
      + "  saldo {\n"
      + "    __typename\n"
      + "    deposit_fmt\n"
      + "    deposit\n"
      + "  }\n"
      + "  paymentAdminProfile {\n"
      + "    __typename\n"
      + "    is_purchased_marketplace\n"
      + "    is_purchased_digital\n"
      + "    is_purchased_ticket\n"
      + "    last_purchase_date\n"
      + "  }\n"
      + "  topadsDeposit(userID: $userID) {\n"
      + "    __typename\n"
      + "    topads_amount\n"
      + "    is_topads_user\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final ConsumerDrawerData.Variables variables;

  public ConsumerDrawerData(int userID) {
    variables = new ConsumerDrawerData.Variables(userID);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public ConsumerDrawerData.Data wrapData(ConsumerDrawerData.Data data) {
    return data;
  }

  @Override
  public ConsumerDrawerData.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int userID;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int userID) {
      this.userID = userID;
      this.valueMap.put("userID", userID);
    }

    public int userID() {
      return userID;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int userID;

    Builder() {
    }

    public Builder userID(int userID) {
      this.userID = userID;
      return this;
    }

    public ConsumerDrawerData build() {
      return new ConsumerDrawerData(userID);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nonnull ShopInfoMoengage shopInfoMoengage;

    private final @Nullable Profile profile;

    private final @Nullable Address address;

    private final @Nullable Wallet wallet;

    private final @Nonnull Saldo saldo;

    private final @Nullable PaymentAdminProfile paymentAdminProfile;

    private final @Nonnull TopadsDeposit topadsDeposit;

    public Data(@Nonnull ShopInfoMoengage shopInfoMoengage, @Nullable Profile profile,
        @Nullable Address address, @Nullable Wallet wallet, @Nonnull Saldo saldo,
        @Nullable PaymentAdminProfile paymentAdminProfile, @Nonnull TopadsDeposit topadsDeposit) {
      this.shopInfoMoengage = shopInfoMoengage;
      this.profile = profile;
      this.address = address;
      this.wallet = wallet;
      this.saldo = saldo;
      this.paymentAdminProfile = paymentAdminProfile;
      this.topadsDeposit = topadsDeposit;
    }

    public @Nonnull ShopInfoMoengage shopInfoMoengage() {
      return this.shopInfoMoengage;
    }

    public @Nullable Profile profile() {
      return this.profile;
    }

    public @Nullable Address address() {
      return this.address;
    }

    public @Nullable Wallet wallet() {
      return this.wallet;
    }

    public @Nonnull Saldo saldo() {
      return this.saldo;
    }

    public @Nullable PaymentAdminProfile paymentAdminProfile() {
      return this.paymentAdminProfile;
    }

    public @Nonnull TopadsDeposit topadsDeposit() {
      return this.topadsDeposit;
    }

    @Override
    public String toString() {
      return "Data{"
        + "shopInfoMoengage=" + shopInfoMoengage + ", "
        + "profile=" + profile + ", "
        + "address=" + address + ", "
        + "wallet=" + wallet + ", "
        + "saldo=" + saldo + ", "
        + "paymentAdminProfile=" + paymentAdminProfile + ", "
        + "topadsDeposit=" + topadsDeposit
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.shopInfoMoengage == null) ? (that.shopInfoMoengage == null) : this.shopInfoMoengage.equals(that.shopInfoMoengage))
         && ((this.profile == null) ? (that.profile == null) : this.profile.equals(that.profile))
         && ((this.address == null) ? (that.address == null) : this.address.equals(that.address))
         && ((this.wallet == null) ? (that.wallet == null) : this.wallet.equals(that.wallet))
         && ((this.saldo == null) ? (that.saldo == null) : this.saldo.equals(that.saldo))
         && ((this.paymentAdminProfile == null) ? (that.paymentAdminProfile == null) : this.paymentAdminProfile.equals(that.paymentAdminProfile))
         && ((this.topadsDeposit == null) ? (that.topadsDeposit == null) : this.topadsDeposit.equals(that.topadsDeposit));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (shopInfoMoengage == null) ? 0 : shopInfoMoengage.hashCode();
      h *= 1000003;
      h ^= (profile == null) ? 0 : profile.hashCode();
      h *= 1000003;
      h ^= (address == null) ? 0 : address.hashCode();
      h *= 1000003;
      h ^= (wallet == null) ? 0 : wallet.hashCode();
      h *= 1000003;
      h ^= (saldo == null) ? 0 : saldo.hashCode();
      h *= 1000003;
      h ^= (paymentAdminProfile == null) ? 0 : paymentAdminProfile.hashCode();
      h *= 1000003;
      h ^= (topadsDeposit == null) ? 0 : topadsDeposit.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final ShopInfoMoengage.Mapper shopInfoMoengageFieldMapper = new ShopInfoMoengage.Mapper();

      final Profile.Mapper profileFieldMapper = new Profile.Mapper();

      final Address.Mapper addressFieldMapper = new Address.Mapper();

      final Wallet.Mapper walletFieldMapper = new Wallet.Mapper();

      final Saldo.Mapper saldoFieldMapper = new Saldo.Mapper();

      final PaymentAdminProfile.Mapper paymentAdminProfileFieldMapper = new PaymentAdminProfile.Mapper();

      final TopadsDeposit.Mapper topadsDepositFieldMapper = new TopadsDeposit.Mapper();

      final Field[] fields = {
        Field.forObject("shopInfoMoengage", "shopInfoMoengage", new UnmodifiableMapBuilder<String, Object>(1)
          .put("userID", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "userID")
          .build())
        .build(), false, new Field.ObjectReader<ShopInfoMoengage>() {
          @Override public ShopInfoMoengage read(final ResponseReader reader) throws IOException {
            return shopInfoMoengageFieldMapper.map(reader);
          }
        }),
        Field.forObject("profile", "profile", null, true, new Field.ObjectReader<Profile>() {
          @Override public Profile read(final ResponseReader reader) throws IOException {
            return profileFieldMapper.map(reader);
          }
        }),
        Field.forObject("address", "address", null, true, new Field.ObjectReader<Address>() {
          @Override public Address read(final ResponseReader reader) throws IOException {
            return addressFieldMapper.map(reader);
          }
        }),
        Field.forObject("wallet", "wallet", null, true, new Field.ObjectReader<Wallet>() {
          @Override public Wallet read(final ResponseReader reader) throws IOException {
            return walletFieldMapper.map(reader);
          }
        }),
        Field.forObject("saldo", "saldo", null, false, new Field.ObjectReader<Saldo>() {
          @Override public Saldo read(final ResponseReader reader) throws IOException {
            return saldoFieldMapper.map(reader);
          }
        }),
        Field.forObject("paymentAdminProfile", "paymentAdminProfile", null, true, new Field.ObjectReader<PaymentAdminProfile>() {
          @Override public PaymentAdminProfile read(final ResponseReader reader) throws IOException {
            return paymentAdminProfileFieldMapper.map(reader);
          }
        }),
        Field.forObject("topadsDeposit", "topadsDeposit", new UnmodifiableMapBuilder<String, Object>(1)
          .put("userID", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "userID")
          .build())
        .build(), false, new Field.ObjectReader<TopadsDeposit>() {
          @Override public TopadsDeposit read(final ResponseReader reader) throws IOException {
            return topadsDepositFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final ShopInfoMoengage shopInfoMoengage = reader.read(fields[0]);
        final Profile profile = reader.read(fields[1]);
        final Address address = reader.read(fields[2]);
        final Wallet wallet = reader.read(fields[3]);
        final Saldo saldo = reader.read(fields[4]);
        final PaymentAdminProfile paymentAdminProfile = reader.read(fields[5]);
        final TopadsDeposit topadsDeposit = reader.read(fields[6]);
        return new Data(shopInfoMoengage, profile, address, wallet, saldo, paymentAdminProfile, topadsDeposit);
      }
    }

    public static class Info {
      private final @Nonnull String date_shop_created;

      private final @Nonnull String shop_id;

      private final @Nonnull String shop_location;

      private final @Nonnull String shop_name;

      private final int shop_score;

      private final int total_active_product;

      private final @Nullable String shop_avatar;

      private final @Nullable String shop_cover;

      private final @Nullable String shop_domain;

      public Info(@Nonnull String date_shop_created, @Nonnull String shop_id,
          @Nonnull String shop_location, @Nonnull String shop_name, int shop_score,
          int total_active_product, @Nullable String shop_avatar, @Nullable String shop_cover,
          @Nullable String shop_domain) {
        this.date_shop_created = date_shop_created;
        this.shop_id = shop_id;
        this.shop_location = shop_location;
        this.shop_name = shop_name;
        this.shop_score = shop_score;
        this.total_active_product = total_active_product;
        this.shop_avatar = shop_avatar;
        this.shop_cover = shop_cover;
        this.shop_domain = shop_domain;
      }

      public @Nonnull String date_shop_created() {
        return this.date_shop_created;
      }

      public @Nonnull String shop_id() {
        return this.shop_id;
      }

      public @Nonnull String shop_location() {
        return this.shop_location;
      }

      public @Nonnull String shop_name() {
        return this.shop_name;
      }

      public int shop_score() {
        return this.shop_score;
      }

      public int total_active_product() {
        return this.total_active_product;
      }

      public @Nullable String shop_avatar() {
        return this.shop_avatar;
      }

      public @Nullable String shop_cover() {
        return this.shop_cover;
      }

      public @Nullable String shop_domain() {
        return this.shop_domain;
      }

      @Override
      public String toString() {
        return "Info{"
          + "date_shop_created=" + date_shop_created + ", "
          + "shop_id=" + shop_id + ", "
          + "shop_location=" + shop_location + ", "
          + "shop_name=" + shop_name + ", "
          + "shop_score=" + shop_score + ", "
          + "total_active_product=" + total_active_product + ", "
          + "shop_avatar=" + shop_avatar + ", "
          + "shop_cover=" + shop_cover + ", "
          + "shop_domain=" + shop_domain
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Info) {
          Info that = (Info) o;
          return ((this.date_shop_created == null) ? (that.date_shop_created == null) : this.date_shop_created.equals(that.date_shop_created))
           && ((this.shop_id == null) ? (that.shop_id == null) : this.shop_id.equals(that.shop_id))
           && ((this.shop_location == null) ? (that.shop_location == null) : this.shop_location.equals(that.shop_location))
           && ((this.shop_name == null) ? (that.shop_name == null) : this.shop_name.equals(that.shop_name))
           && this.shop_score == that.shop_score
           && this.total_active_product == that.total_active_product
           && ((this.shop_avatar == null) ? (that.shop_avatar == null) : this.shop_avatar.equals(that.shop_avatar))
           && ((this.shop_cover == null) ? (that.shop_cover == null) : this.shop_cover.equals(that.shop_cover))
           && ((this.shop_domain == null) ? (that.shop_domain == null) : this.shop_domain.equals(that.shop_domain));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (date_shop_created == null) ? 0 : date_shop_created.hashCode();
        h *= 1000003;
        h ^= (shop_id == null) ? 0 : shop_id.hashCode();
        h *= 1000003;
        h ^= (shop_location == null) ? 0 : shop_location.hashCode();
        h *= 1000003;
        h ^= (shop_name == null) ? 0 : shop_name.hashCode();
        h *= 1000003;
        h ^= shop_score;
        h *= 1000003;
        h ^= total_active_product;
        h *= 1000003;
        h ^= (shop_avatar == null) ? 0 : shop_avatar.hashCode();
        h *= 1000003;
        h ^= (shop_cover == null) ? 0 : shop_cover.hashCode();
        h *= 1000003;
        h ^= (shop_domain == null) ? 0 : shop_domain.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Info> {
        final Field[] fields = {
          Field.forString("date_shop_created", "date_shop_created", null, false),
          Field.forString("shop_id", "shop_id", null, false),
          Field.forString("shop_location", "shop_location", null, false),
          Field.forString("shop_name", "shop_name", null, false),
          Field.forInt("shop_score", "shop_score", null, false),
          Field.forInt("total_active_product", "total_active_product", null, false),
          Field.forString("shop_avatar", "shop_avatar", null, true),
          Field.forString("shop_cover", "shop_cover", null, true),
          Field.forString("shop_domain", "shop_domain", null, true)
        };

        @Override
        public Info map(ResponseReader reader) throws IOException {
          final String date_shop_created = reader.read(fields[0]);
          final String shop_id = reader.read(fields[1]);
          final String shop_location = reader.read(fields[2]);
          final String shop_name = reader.read(fields[3]);
          final int shop_score = reader.read(fields[4]);
          final int total_active_product = reader.read(fields[5]);
          final String shop_avatar = reader.read(fields[6]);
          final String shop_cover = reader.read(fields[7]);
          final String shop_domain = reader.read(fields[8]);
          return new Info(date_shop_created, shop_id, shop_location, shop_name, shop_score, total_active_product, shop_avatar, shop_cover, shop_domain);
        }
      }
    }

    public static class Owner {
      private final boolean is_gold_merchant;

      private final boolean is_seller;

      public Owner(boolean is_gold_merchant, boolean is_seller) {
        this.is_gold_merchant = is_gold_merchant;
        this.is_seller = is_seller;
      }

      public boolean is_gold_merchant() {
        return this.is_gold_merchant;
      }

      public boolean is_seller() {
        return this.is_seller;
      }

      @Override
      public String toString() {
        return "Owner{"
          + "is_gold_merchant=" + is_gold_merchant + ", "
          + "is_seller=" + is_seller
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Owner) {
          Owner that = (Owner) o;
          return this.is_gold_merchant == that.is_gold_merchant
           && this.is_seller == that.is_seller;
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= Boolean.valueOf(is_gold_merchant).hashCode();
        h *= 1000003;
        h ^= Boolean.valueOf(is_seller).hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Owner> {
        final Field[] fields = {
          Field.forBoolean("is_gold_merchant", "is_gold_merchant", null, false),
          Field.forBoolean("is_seller", "is_seller", null, false)
        };

        @Override
        public Owner map(ResponseReader reader) throws IOException {
          final boolean is_gold_merchant = reader.read(fields[0]);
          final boolean is_seller = reader.read(fields[1]);
          return new Owner(is_gold_merchant, is_seller);
        }
      }
    }

    public static class Stats {
      private final @Nonnull String shop_total_transaction;

      private final @Nonnull String shop_item_sold;

      public Stats(@Nonnull String shop_total_transaction, @Nonnull String shop_item_sold) {
        this.shop_total_transaction = shop_total_transaction;
        this.shop_item_sold = shop_item_sold;
      }

      public @Nonnull String shop_total_transaction() {
        return this.shop_total_transaction;
      }

      public @Nonnull String shop_item_sold() {
        return this.shop_item_sold;
      }

      @Override
      public String toString() {
        return "Stats{"
          + "shop_total_transaction=" + shop_total_transaction + ", "
          + "shop_item_sold=" + shop_item_sold
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Stats) {
          Stats that = (Stats) o;
          return ((this.shop_total_transaction == null) ? (that.shop_total_transaction == null) : this.shop_total_transaction.equals(that.shop_total_transaction))
           && ((this.shop_item_sold == null) ? (that.shop_item_sold == null) : this.shop_item_sold.equals(that.shop_item_sold));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (shop_total_transaction == null) ? 0 : shop_total_transaction.hashCode();
        h *= 1000003;
        h ^= (shop_item_sold == null) ? 0 : shop_item_sold.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Stats> {
        final Field[] fields = {
          Field.forString("shop_total_transaction", "shop_total_transaction", null, false),
          Field.forString("shop_item_sold", "shop_item_sold", null, false)
        };

        @Override
        public Stats map(ResponseReader reader) throws IOException {
          final String shop_total_transaction = reader.read(fields[0]);
          final String shop_item_sold = reader.read(fields[1]);
          return new Stats(shop_total_transaction, shop_item_sold);
        }
      }
    }

    public static class ShopInfoMoengage {
      private final @Nonnull Info info;

      private final @Nonnull Owner owner;

      private final @Nonnull Stats stats;

      public ShopInfoMoengage(@Nonnull Info info, @Nonnull Owner owner, @Nonnull Stats stats) {
        this.info = info;
        this.owner = owner;
        this.stats = stats;
      }

      public @Nonnull Info info() {
        return this.info;
      }

      public @Nonnull Owner owner() {
        return this.owner;
      }

      public @Nonnull Stats stats() {
        return this.stats;
      }

      @Override
      public String toString() {
        return "ShopInfoMoengage{"
          + "info=" + info + ", "
          + "owner=" + owner + ", "
          + "stats=" + stats
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof ShopInfoMoengage) {
          ShopInfoMoengage that = (ShopInfoMoengage) o;
          return ((this.info == null) ? (that.info == null) : this.info.equals(that.info))
           && ((this.owner == null) ? (that.owner == null) : this.owner.equals(that.owner))
           && ((this.stats == null) ? (that.stats == null) : this.stats.equals(that.stats));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (info == null) ? 0 : info.hashCode();
        h *= 1000003;
        h ^= (owner == null) ? 0 : owner.hashCode();
        h *= 1000003;
        h ^= (stats == null) ? 0 : stats.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<ShopInfoMoengage> {
        final Info.Mapper infoFieldMapper = new Info.Mapper();

        final Owner.Mapper ownerFieldMapper = new Owner.Mapper();

        final Stats.Mapper statsFieldMapper = new Stats.Mapper();

        final Field[] fields = {
          Field.forObject("info", "info", null, false, new Field.ObjectReader<Info>() {
            @Override public Info read(final ResponseReader reader) throws IOException {
              return infoFieldMapper.map(reader);
            }
          }),
          Field.forObject("owner", "owner", null, false, new Field.ObjectReader<Owner>() {
            @Override public Owner read(final ResponseReader reader) throws IOException {
              return ownerFieldMapper.map(reader);
            }
          }),
          Field.forObject("stats", "stats", null, false, new Field.ObjectReader<Stats>() {
            @Override public Stats read(final ResponseReader reader) throws IOException {
              return statsFieldMapper.map(reader);
            }
          })
        };

        @Override
        public ShopInfoMoengage map(ResponseReader reader) throws IOException {
          final Info info = reader.read(fields[0]);
          final Owner owner = reader.read(fields[1]);
          final Stats stats = reader.read(fields[2]);
          return new ShopInfoMoengage(info, owner, stats);
        }
      }
    }

    public static class Profile {
      private final @Nonnull String user_id;

      private final @Nonnull String first_name;

      private final @Nonnull String full_name;

      private final @Nonnull String email;

      private final @Nonnull String gender;

      private final @Nonnull String bday;

      private final @Nonnull String age;

      private final @Nonnull String phone;

      private final boolean phone_verified;

      private final @Nonnull String register_date;

      private final @Nonnull String profile_picture;

      private final int completion;

      public Profile(@Nonnull String user_id, @Nonnull String first_name, @Nonnull String full_name,
          @Nonnull String email, @Nonnull String gender, @Nonnull String bday, @Nonnull String age,
          @Nonnull String phone, boolean phone_verified, @Nonnull String register_date,
          @Nonnull String profile_picture, int completion) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.full_name = full_name;
        this.email = email;
        this.gender = gender;
        this.bday = bday;
        this.age = age;
        this.phone = phone;
        this.phone_verified = phone_verified;
        this.register_date = register_date;
        this.profile_picture = profile_picture;
        this.completion = completion;
      }

      public @Nonnull String user_id() {
        return this.user_id;
      }

      public @Nonnull String first_name() {
        return this.first_name;
      }

      public @Nonnull String full_name() {
        return this.full_name;
      }

      public @Nonnull String email() {
        return this.email;
      }

      public @Nonnull String gender() {
        return this.gender;
      }

      public @Nonnull String bday() {
        return this.bday;
      }

      public @Nonnull String age() {
        return this.age;
      }

      public @Nonnull String phone() {
        return this.phone;
      }

      public boolean phone_verified() {
        return this.phone_verified;
      }

      public @Nonnull String register_date() {
        return this.register_date;
      }

      public @Nonnull String profile_picture() {
        return this.profile_picture;
      }

      public int completion() {
        return this.completion;
      }

      @Override
      public String toString() {
        return "Profile{"
          + "user_id=" + user_id + ", "
          + "first_name=" + first_name + ", "
          + "full_name=" + full_name + ", "
          + "email=" + email + ", "
          + "gender=" + gender + ", "
          + "bday=" + bday + ", "
          + "age=" + age + ", "
          + "phone=" + phone + ", "
          + "phone_verified=" + phone_verified + ", "
          + "register_date=" + register_date + ", "
          + "profile_picture=" + profile_picture + ", "
          + "completion=" + completion
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Profile) {
          Profile that = (Profile) o;
          return ((this.user_id == null) ? (that.user_id == null) : this.user_id.equals(that.user_id))
           && ((this.first_name == null) ? (that.first_name == null) : this.first_name.equals(that.first_name))
           && ((this.full_name == null) ? (that.full_name == null) : this.full_name.equals(that.full_name))
           && ((this.email == null) ? (that.email == null) : this.email.equals(that.email))
           && ((this.gender == null) ? (that.gender == null) : this.gender.equals(that.gender))
           && ((this.bday == null) ? (that.bday == null) : this.bday.equals(that.bday))
           && ((this.age == null) ? (that.age == null) : this.age.equals(that.age))
           && ((this.phone == null) ? (that.phone == null) : this.phone.equals(that.phone))
           && this.phone_verified == that.phone_verified
           && ((this.register_date == null) ? (that.register_date == null) : this.register_date.equals(that.register_date))
           && ((this.profile_picture == null) ? (that.profile_picture == null) : this.profile_picture.equals(that.profile_picture))
           && this.completion == that.completion;
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (user_id == null) ? 0 : user_id.hashCode();
        h *= 1000003;
        h ^= (first_name == null) ? 0 : first_name.hashCode();
        h *= 1000003;
        h ^= (full_name == null) ? 0 : full_name.hashCode();
        h *= 1000003;
        h ^= (email == null) ? 0 : email.hashCode();
        h *= 1000003;
        h ^= (gender == null) ? 0 : gender.hashCode();
        h *= 1000003;
        h ^= (bday == null) ? 0 : bday.hashCode();
        h *= 1000003;
        h ^= (age == null) ? 0 : age.hashCode();
        h *= 1000003;
        h ^= (phone == null) ? 0 : phone.hashCode();
        h *= 1000003;
        h ^= Boolean.valueOf(phone_verified).hashCode();
        h *= 1000003;
        h ^= (register_date == null) ? 0 : register_date.hashCode();
        h *= 1000003;
        h ^= (profile_picture == null) ? 0 : profile_picture.hashCode();
        h *= 1000003;
        h ^= completion;
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Profile> {
        final Field[] fields = {
          Field.forString("user_id", "user_id", null, false),
          Field.forString("first_name", "first_name", null, false),
          Field.forString("full_name", "full_name", null, false),
          Field.forString("email", "email", null, false),
          Field.forString("gender", "gender", null, false),
          Field.forString("bday", "bday", null, false),
          Field.forString("age", "age", null, false),
          Field.forString("phone", "phone", null, false),
          Field.forBoolean("phone_verified", "phone_verified", null, false),
          Field.forString("register_date", "register_date", null, false),
          Field.forString("profile_picture", "profile_picture", null, false),
          Field.forInt("completion", "completion", null, false)
        };

        @Override
        public Profile map(ResponseReader reader) throws IOException {
          final String user_id = reader.read(fields[0]);
          final String first_name = reader.read(fields[1]);
          final String full_name = reader.read(fields[2]);
          final String email = reader.read(fields[3]);
          final String gender = reader.read(fields[4]);
          final String bday = reader.read(fields[5]);
          final String age = reader.read(fields[6]);
          final String phone = reader.read(fields[7]);
          final boolean phone_verified = reader.read(fields[8]);
          final String register_date = reader.read(fields[9]);
          final String profile_picture = reader.read(fields[10]);
          final int completion = reader.read(fields[11]);
          return new Profile(user_id, first_name, full_name, email, gender, bday, age, phone, phone_verified, register_date, profile_picture, completion);
        }
      }
    }

    public static class Address1 {
      private final @Nonnull String city_name;

      private final @Nonnull String province_name;

      public Address1(@Nonnull String city_name, @Nonnull String province_name) {
        this.city_name = city_name;
        this.province_name = province_name;
      }

      public @Nonnull String city_name() {
        return this.city_name;
      }

      public @Nonnull String province_name() {
        return this.province_name;
      }

      @Override
      public String toString() {
        return "Address1{"
          + "city_name=" + city_name + ", "
          + "province_name=" + province_name
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Address1) {
          Address1 that = (Address1) o;
          return ((this.city_name == null) ? (that.city_name == null) : this.city_name.equals(that.city_name))
           && ((this.province_name == null) ? (that.province_name == null) : this.province_name.equals(that.province_name));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (city_name == null) ? 0 : city_name.hashCode();
        h *= 1000003;
        h ^= (province_name == null) ? 0 : province_name.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Address1> {
        final Field[] fields = {
          Field.forString("city_name", "city_name", null, false),
          Field.forString("province_name", "province_name", null, false)
        };

        @Override
        public Address1 map(ResponseReader reader) throws IOException {
          final String city_name = reader.read(fields[0]);
          final String province_name = reader.read(fields[1]);
          return new Address1(city_name, province_name);
        }
      }
    }

    public static class Address {
      private final @Nullable List<Address1> addresses;

      public Address(@Nullable List<Address1> addresses) {
        this.addresses = addresses;
      }

      public @Nullable List<Address1> addresses() {
        return this.addresses;
      }

      @Override
      public String toString() {
        return "Address{"
          + "addresses=" + addresses
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Address) {
          Address that = (Address) o;
          return ((this.addresses == null) ? (that.addresses == null) : this.addresses.equals(that.addresses));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (addresses == null) ? 0 : addresses.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Address> {
        final Address1.Mapper address1FieldMapper = new Address1.Mapper();

        final Field[] fields = {
          Field.forList("addresses", "addresses", null, true, new Field.ObjectReader<Address1>() {
            @Override public Address1 read(final ResponseReader reader) throws IOException {
              return address1FieldMapper.map(reader);
            }
          })
        };

        @Override
        public Address map(ResponseReader reader) throws IOException {
          final List<Address1> addresses = reader.read(fields[0]);
          return new Address(addresses);
        }
      }
    }

    public static class Error {
      private final @Nullable String name;

      private final @Nullable String message;

      public Error(@Nullable String name, @Nullable String message) {
        this.name = name;
        this.message = message;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String message() {
        return this.message;
      }

      @Override
      public String toString() {
        return "Error{"
          + "name=" + name + ", "
          + "message=" + message
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Error) {
          Error that = (Error) o;
          return ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.message == null) ? (that.message == null) : this.message.equals(that.message));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (message == null) ? 0 : message.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Error> {
        final Field[] fields = {
          Field.forString("name", "name", null, true),
          Field.forString("message", "message", null, true)
        };

        @Override
        public Error map(ResponseReader reader) throws IOException {
          final String name = reader.read(fields[0]);
          final String message = reader.read(fields[1]);
          return new Error(name, message);
        }
      }
    }

    public static class Ab_tag {
      private final @Nonnull String tag;

      public Ab_tag(@Nonnull String tag) {
        this.tag = tag;
      }

      public @Nonnull String tag() {
        return this.tag;
      }

      @Override
      public String toString() {
        return "Ab_tag{"
          + "tag=" + tag
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Ab_tag) {
          Ab_tag that = (Ab_tag) o;
          return ((this.tag == null) ? (that.tag == null) : this.tag.equals(that.tag));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (tag == null) ? 0 : tag.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Ab_tag> {
        final Field[] fields = {
          Field.forString("tag", "tag", null, false)
        };

        @Override
        public Ab_tag map(ResponseReader reader) throws IOException {
          final String tag = reader.read(fields[0]);
          return new Ab_tag(tag);
        }
      }
    }

    public static class Action {
      private final @Nonnull String text;

      private final @Nonnull String redirect_url;

      private final @Nonnull String applinks;

      private final @Nonnull String visibility;

      public Action(@Nonnull String text, @Nonnull String redirect_url, @Nonnull String applinks,
          @Nonnull String visibility) {
        this.text = text;
        this.redirect_url = redirect_url;
        this.applinks = applinks;
        this.visibility = visibility;
      }

      public @Nonnull String text() {
        return this.text;
      }

      public @Nonnull String redirect_url() {
        return this.redirect_url;
      }

      public @Nonnull String applinks() {
        return this.applinks;
      }

      public @Nonnull String visibility() {
        return this.visibility;
      }

      @Override
      public String toString() {
        return "Action{"
          + "text=" + text + ", "
          + "redirect_url=" + redirect_url + ", "
          + "applinks=" + applinks + ", "
          + "visibility=" + visibility
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Action) {
          Action that = (Action) o;
          return ((this.text == null) ? (that.text == null) : this.text.equals(that.text))
           && ((this.redirect_url == null) ? (that.redirect_url == null) : this.redirect_url.equals(that.redirect_url))
           && ((this.applinks == null) ? (that.applinks == null) : this.applinks.equals(that.applinks))
           && ((this.visibility == null) ? (that.visibility == null) : this.visibility.equals(that.visibility));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (text == null) ? 0 : text.hashCode();
        h *= 1000003;
        h ^= (redirect_url == null) ? 0 : redirect_url.hashCode();
        h *= 1000003;
        h ^= (applinks == null) ? 0 : applinks.hashCode();
        h *= 1000003;
        h ^= (visibility == null) ? 0 : visibility.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Action> {
        final Field[] fields = {
          Field.forString("text", "text", null, false),
          Field.forString("redirect_url", "redirect_url", null, false),
          Field.forString("applinks", "applinks", null, false),
          Field.forString("visibility", "visibility", null, false)
        };

        @Override
        public Action map(ResponseReader reader) throws IOException {
          final String text = reader.read(fields[0]);
          final String redirect_url = reader.read(fields[1]);
          final String applinks = reader.read(fields[2]);
          final String visibility = reader.read(fields[3]);
          return new Action(text, redirect_url, applinks, visibility);
        }
      }
    }

    public static class Wallet {
      private final boolean linked;

      private final @Nonnull String balance;

      private final int rawBalance;

      private final @Nullable List<Error> errors;

      private final @Nonnull String text;

      private final @Nonnull String total_balance;

      private final int raw_total_balance;

      private final @Nonnull String hold_balance;

      private final int raw_hold_balance;

      private final @Nonnull String redirect_url;

      private final @Nonnull String applinks;

      private final @Nullable List<Ab_tag> ab_tags;

      private final @Nonnull Action action;

      public Wallet(boolean linked, @Nonnull String balance, int rawBalance,
          @Nullable List<Error> errors, @Nonnull String text, @Nonnull String total_balance,
          int raw_total_balance, @Nonnull String hold_balance, int raw_hold_balance,
          @Nonnull String redirect_url, @Nonnull String applinks, @Nullable List<Ab_tag> ab_tags,
          @Nonnull Action action) {
        this.linked = linked;
        this.balance = balance;
        this.rawBalance = rawBalance;
        this.errors = errors;
        this.text = text;
        this.total_balance = total_balance;
        this.raw_total_balance = raw_total_balance;
        this.hold_balance = hold_balance;
        this.raw_hold_balance = raw_hold_balance;
        this.redirect_url = redirect_url;
        this.applinks = applinks;
        this.ab_tags = ab_tags;
        this.action = action;
      }

      public boolean linked() {
        return this.linked;
      }

      public @Nonnull String balance() {
        return this.balance;
      }

      public int rawBalance() {
        return this.rawBalance;
      }

      public @Nullable List<Error> errors() {
        return this.errors;
      }

      public @Nonnull String text() {
        return this.text;
      }

      public @Nonnull String total_balance() {
        return this.total_balance;
      }

      public int raw_total_balance() {
        return this.raw_total_balance;
      }

      public @Nonnull String hold_balance() {
        return this.hold_balance;
      }

      public int raw_hold_balance() {
        return this.raw_hold_balance;
      }

      public @Nonnull String redirect_url() {
        return this.redirect_url;
      }

      public @Nonnull String applinks() {
        return this.applinks;
      }

      public @Nullable List<Ab_tag> ab_tags() {
        return this.ab_tags;
      }

      public @Nonnull Action action() {
        return this.action;
      }

      @Override
      public String toString() {
        return "Wallet{"
          + "linked=" + linked + ", "
          + "balance=" + balance + ", "
          + "rawBalance=" + rawBalance + ", "
          + "errors=" + errors + ", "
          + "text=" + text + ", "
          + "total_balance=" + total_balance + ", "
          + "raw_total_balance=" + raw_total_balance + ", "
          + "hold_balance=" + hold_balance + ", "
          + "raw_hold_balance=" + raw_hold_balance + ", "
          + "redirect_url=" + redirect_url + ", "
          + "applinks=" + applinks + ", "
          + "ab_tags=" + ab_tags + ", "
          + "action=" + action
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Wallet) {
          Wallet that = (Wallet) o;
          return this.linked == that.linked
           && ((this.balance == null) ? (that.balance == null) : this.balance.equals(that.balance))
           && this.rawBalance == that.rawBalance
           && ((this.errors == null) ? (that.errors == null) : this.errors.equals(that.errors))
           && ((this.text == null) ? (that.text == null) : this.text.equals(that.text))
           && ((this.total_balance == null) ? (that.total_balance == null) : this.total_balance.equals(that.total_balance))
           && this.raw_total_balance == that.raw_total_balance
           && ((this.hold_balance == null) ? (that.hold_balance == null) : this.hold_balance.equals(that.hold_balance))
           && this.raw_hold_balance == that.raw_hold_balance
           && ((this.redirect_url == null) ? (that.redirect_url == null) : this.redirect_url.equals(that.redirect_url))
           && ((this.applinks == null) ? (that.applinks == null) : this.applinks.equals(that.applinks))
           && ((this.ab_tags == null) ? (that.ab_tags == null) : this.ab_tags.equals(that.ab_tags))
           && ((this.action == null) ? (that.action == null) : this.action.equals(that.action));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= Boolean.valueOf(linked).hashCode();
        h *= 1000003;
        h ^= (balance == null) ? 0 : balance.hashCode();
        h *= 1000003;
        h ^= rawBalance;
        h *= 1000003;
        h ^= (errors == null) ? 0 : errors.hashCode();
        h *= 1000003;
        h ^= (text == null) ? 0 : text.hashCode();
        h *= 1000003;
        h ^= (total_balance == null) ? 0 : total_balance.hashCode();
        h *= 1000003;
        h ^= raw_total_balance;
        h *= 1000003;
        h ^= (hold_balance == null) ? 0 : hold_balance.hashCode();
        h *= 1000003;
        h ^= raw_hold_balance;
        h *= 1000003;
        h ^= (redirect_url == null) ? 0 : redirect_url.hashCode();
        h *= 1000003;
        h ^= (applinks == null) ? 0 : applinks.hashCode();
        h *= 1000003;
        h ^= (ab_tags == null) ? 0 : ab_tags.hashCode();
        h *= 1000003;
        h ^= (action == null) ? 0 : action.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Wallet> {
        final Error.Mapper errorFieldMapper = new Error.Mapper();

        final Ab_tag.Mapper ab_tagFieldMapper = new Ab_tag.Mapper();

        final Action.Mapper actionFieldMapper = new Action.Mapper();

        final Field[] fields = {
          Field.forBoolean("linked", "linked", null, false),
          Field.forString("balance", "balance", null, false),
          Field.forInt("rawBalance", "rawBalance", null, false),
          Field.forList("errors", "errors", null, true, new Field.ObjectReader<Error>() {
            @Override public Error read(final ResponseReader reader) throws IOException {
              return errorFieldMapper.map(reader);
            }
          }),
          Field.forString("text", "text", null, false),
          Field.forString("total_balance", "total_balance", null, false),
          Field.forInt("raw_total_balance", "raw_total_balance", null, false),
          Field.forString("hold_balance", "hold_balance", null, false),
          Field.forInt("raw_hold_balance", "raw_hold_balance", null, false),
          Field.forString("redirect_url", "redirect_url", null, false),
          Field.forString("applinks", "applinks", null, false),
          Field.forList("ab_tags", "ab_tags", null, true, new Field.ObjectReader<Ab_tag>() {
            @Override public Ab_tag read(final ResponseReader reader) throws IOException {
              return ab_tagFieldMapper.map(reader);
            }
          }),
          Field.forObject("action", "action", null, false, new Field.ObjectReader<Action>() {
            @Override public Action read(final ResponseReader reader) throws IOException {
              return actionFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Wallet map(ResponseReader reader) throws IOException {
          final boolean linked = reader.read(fields[0]);
          final String balance = reader.read(fields[1]);
          final int rawBalance = reader.read(fields[2]);
          final List<Error> errors = reader.read(fields[3]);
          final String text = reader.read(fields[4]);
          final String total_balance = reader.read(fields[5]);
          final int raw_total_balance = reader.read(fields[6]);
          final String hold_balance = reader.read(fields[7]);
          final int raw_hold_balance = reader.read(fields[8]);
          final String redirect_url = reader.read(fields[9]);
          final String applinks = reader.read(fields[10]);
          final List<Ab_tag> ab_tags = reader.read(fields[11]);
          final Action action = reader.read(fields[12]);
          return new Wallet(linked, balance, rawBalance, errors, text, total_balance, raw_total_balance, hold_balance, raw_hold_balance, redirect_url, applinks, ab_tags, action);
        }
      }
    }

    public static class Saldo {
      private final @Nonnull String deposit_fmt;

      private final int deposit;

      public Saldo(@Nonnull String deposit_fmt, int deposit) {
        this.deposit_fmt = deposit_fmt;
        this.deposit = deposit;
      }

      public @Nonnull String deposit_fmt() {
        return this.deposit_fmt;
      }

      public int deposit() {
        return this.deposit;
      }

      @Override
      public String toString() {
        return "Saldo{"
          + "deposit_fmt=" + deposit_fmt + ", "
          + "deposit=" + deposit
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Saldo) {
          Saldo that = (Saldo) o;
          return ((this.deposit_fmt == null) ? (that.deposit_fmt == null) : this.deposit_fmt.equals(that.deposit_fmt))
           && this.deposit == that.deposit;
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (deposit_fmt == null) ? 0 : deposit_fmt.hashCode();
        h *= 1000003;
        h ^= deposit;
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Saldo> {
        final Field[] fields = {
          Field.forString("deposit_fmt", "deposit_fmt", null, false),
          Field.forInt("deposit", "deposit", null, false)
        };

        @Override
        public Saldo map(ResponseReader reader) throws IOException {
          final String deposit_fmt = reader.read(fields[0]);
          final int deposit = reader.read(fields[1]);
          return new Saldo(deposit_fmt, deposit);
        }
      }
    }

    public static class PaymentAdminProfile {
      private final @Nullable Boolean is_purchased_marketplace;

      private final @Nullable Boolean is_purchased_digital;

      private final @Nullable Boolean is_purchased_ticket;

      private final @Nullable String last_purchase_date;

      public PaymentAdminProfile(@Nullable Boolean is_purchased_marketplace,
          @Nullable Boolean is_purchased_digital, @Nullable Boolean is_purchased_ticket,
          @Nullable String last_purchase_date) {
        this.is_purchased_marketplace = is_purchased_marketplace;
        this.is_purchased_digital = is_purchased_digital;
        this.is_purchased_ticket = is_purchased_ticket;
        this.last_purchase_date = last_purchase_date;
      }

      public @Nullable Boolean is_purchased_marketplace() {
        return this.is_purchased_marketplace;
      }

      public @Nullable Boolean is_purchased_digital() {
        return this.is_purchased_digital;
      }

      public @Nullable Boolean is_purchased_ticket() {
        return this.is_purchased_ticket;
      }

      public @Nullable String last_purchase_date() {
        return this.last_purchase_date;
      }

      @Override
      public String toString() {
        return "PaymentAdminProfile{"
          + "is_purchased_marketplace=" + is_purchased_marketplace + ", "
          + "is_purchased_digital=" + is_purchased_digital + ", "
          + "is_purchased_ticket=" + is_purchased_ticket + ", "
          + "last_purchase_date=" + last_purchase_date
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof PaymentAdminProfile) {
          PaymentAdminProfile that = (PaymentAdminProfile) o;
          return ((this.is_purchased_marketplace == null) ? (that.is_purchased_marketplace == null) : this.is_purchased_marketplace.equals(that.is_purchased_marketplace))
           && ((this.is_purchased_digital == null) ? (that.is_purchased_digital == null) : this.is_purchased_digital.equals(that.is_purchased_digital))
           && ((this.is_purchased_ticket == null) ? (that.is_purchased_ticket == null) : this.is_purchased_ticket.equals(that.is_purchased_ticket))
           && ((this.last_purchase_date == null) ? (that.last_purchase_date == null) : this.last_purchase_date.equals(that.last_purchase_date));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (is_purchased_marketplace == null) ? 0 : is_purchased_marketplace.hashCode();
        h *= 1000003;
        h ^= (is_purchased_digital == null) ? 0 : is_purchased_digital.hashCode();
        h *= 1000003;
        h ^= (is_purchased_ticket == null) ? 0 : is_purchased_ticket.hashCode();
        h *= 1000003;
        h ^= (last_purchase_date == null) ? 0 : last_purchase_date.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<PaymentAdminProfile> {
        final Field[] fields = {
          Field.forBoolean("is_purchased_marketplace", "is_purchased_marketplace", null, true),
          Field.forBoolean("is_purchased_digital", "is_purchased_digital", null, true),
          Field.forBoolean("is_purchased_ticket", "is_purchased_ticket", null, true),
          Field.forString("last_purchase_date", "last_purchase_date", null, true)
        };

        @Override
        public PaymentAdminProfile map(ResponseReader reader) throws IOException {
          final Boolean is_purchased_marketplace = reader.read(fields[0]);
          final Boolean is_purchased_digital = reader.read(fields[1]);
          final Boolean is_purchased_ticket = reader.read(fields[2]);
          final String last_purchase_date = reader.read(fields[3]);
          return new PaymentAdminProfile(is_purchased_marketplace, is_purchased_digital, is_purchased_ticket, last_purchase_date);
        }
      }
    }

    public static class TopadsDeposit {
      private final int topads_amount;

      private final boolean is_topads_user;

      public TopadsDeposit(int topads_amount, boolean is_topads_user) {
        this.topads_amount = topads_amount;
        this.is_topads_user = is_topads_user;
      }

      public int topads_amount() {
        return this.topads_amount;
      }

      public boolean is_topads_user() {
        return this.is_topads_user;
      }

      @Override
      public String toString() {
        return "TopadsDeposit{"
          + "topads_amount=" + topads_amount + ", "
          + "is_topads_user=" + is_topads_user
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof TopadsDeposit) {
          TopadsDeposit that = (TopadsDeposit) o;
          return this.topads_amount == that.topads_amount
           && this.is_topads_user == that.is_topads_user;
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= topads_amount;
        h *= 1000003;
        h ^= Boolean.valueOf(is_topads_user).hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<TopadsDeposit> {
        final Field[] fields = {
          Field.forInt("topads_amount", "topads_amount", null, false),
          Field.forBoolean("is_topads_user", "is_topads_user", null, false)
        };

        @Override
        public TopadsDeposit map(ResponseReader reader) throws IOException {
          final int topads_amount = reader.read(fields[0]);
          final boolean is_topads_user = reader.read(fields[1]);
          return new TopadsDeposit(topads_amount, is_topads_user);
        }
      }
    }
  }
}
