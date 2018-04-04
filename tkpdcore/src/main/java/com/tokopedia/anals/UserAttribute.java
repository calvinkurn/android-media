package com.tokopedia.anals;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import com.tokopedia.anals.type.CustomType;
import java.io.IOException;
import java.lang.Boolean;
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
public final class UserAttribute implements Query<UserAttribute.Data, UserAttribute.Data, UserAttribute.Variables> {
  public static final String OPERATION_DEFINITION = "query UserAttribute($userID: Int!) {\n"
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

  private final Variables variables;

  public UserAttribute(int userID) {
    variables = new Variables(userID);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public Data wrapData(Data data) {
    return data;
  }

  @Override
  public Variables variables() {
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

    public UserAttribute build() {
      return new UserAttribute(userID);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable ShopInfoMoengage shopInfoMoengage;

    private final @Nullable Profile profile;

    private final @Nullable Address address;

    private final @Nullable Wallet wallet;

    private final @Nullable Saldo saldo;

    private final @Nullable PaymentAdminProfile paymentAdminProfile;

    private final @Nullable TopadsDeposit topadsDeposit;

    public Data(@Nullable ShopInfoMoengage shopInfoMoengage, @Nullable Profile profile,
        @Nullable Address address, @Nullable Wallet wallet, @Nullable Saldo saldo,
        @Nullable PaymentAdminProfile paymentAdminProfile, @Nullable TopadsDeposit topadsDeposit) {
      this.shopInfoMoengage = shopInfoMoengage;
      this.profile = profile;
      this.address = address;
      this.wallet = wallet;
      this.saldo = saldo;
      this.paymentAdminProfile = paymentAdminProfile;
      this.topadsDeposit = topadsDeposit;
    }

    public @Nullable ShopInfoMoengage shopInfoMoengage() {
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

    public @Nullable Saldo saldo() {
      return this.saldo;
    }

    public @Nullable PaymentAdminProfile paymentAdminProfile() {
      return this.paymentAdminProfile;
    }

    public @Nullable TopadsDeposit topadsDeposit() {
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
        .build(), true, new Field.ObjectReader<ShopInfoMoengage>() {
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
        Field.forObject("saldo", "saldo", null, true, new Field.ObjectReader<Saldo>() {
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
        .build(), true, new Field.ObjectReader<TopadsDeposit>() {
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

      public Info(@Nonnull String date_shop_created, @Nonnull String shop_id,
          @Nonnull String shop_location, @Nonnull String shop_name, int shop_score,
          int total_active_product) {
        this.date_shop_created = date_shop_created;
        this.shop_id = shop_id;
        this.shop_location = shop_location;
        this.shop_name = shop_name;
        this.shop_score = shop_score;
        this.total_active_product = total_active_product;
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

      @Override
      public String toString() {
        return "Info{"
          + "date_shop_created=" + date_shop_created + ", "
          + "shop_id=" + shop_id + ", "
          + "shop_location=" + shop_location + ", "
          + "shop_name=" + shop_name + ", "
          + "shop_score=" + shop_score + ", "
          + "total_active_product=" + total_active_product
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
           && this.total_active_product == that.total_active_product;
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
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Info> {
        final Field[] fields = {
          Field.forString("date_shop_created", "date_shop_created", null, false),
          Field.forString("shop_id", "shop_id", null, false),
          Field.forString("shop_location", "shop_location", null, false),
          Field.forString("shop_name", "shop_name", null, false),
          Field.forInt("shop_score", "shop_score", null, false),
          Field.forInt("total_active_product", "total_active_product", null, false)
        };

        @Override
        public Info map(ResponseReader reader) throws IOException {
          final String date_shop_created = reader.read(fields[0]);
          final String shop_id = reader.read(fields[1]);
          final String shop_location = reader.read(fields[2]);
          final String shop_name = reader.read(fields[3]);
          final int shop_score = reader.read(fields[4]);
          final int total_active_product = reader.read(fields[5]);
          return new Info(date_shop_created, shop_id, shop_location, shop_name, shop_score, total_active_product);
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
      private final @Nullable Info info;

      private final @Nullable Owner owner;

      private final @Nullable Stats stats;

      public ShopInfoMoengage(@Nullable Info info, @Nullable Owner owner, @Nullable Stats stats) {
        this.info = info;
        this.owner = owner;
        this.stats = stats;
      }

      public @Nullable Info info() {
        return this.info;
      }

      public @Nullable Owner owner() {
        return this.owner;
      }

      public @Nullable Stats stats() {
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
          Field.forObject("info", "info", null, true, new Field.ObjectReader<Info>() {
            @Override public Info read(final ResponseReader reader) throws IOException {
              return infoFieldMapper.map(reader);
            }
          }),
          Field.forObject("owner", "owner", null, true, new Field.ObjectReader<Owner>() {
            @Override public Owner read(final ResponseReader reader) throws IOException {
              return ownerFieldMapper.map(reader);
            }
          }),
          Field.forObject("stats", "stats", null, true, new Field.ObjectReader<Stats>() {
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
      private final @Nullable String user_id;

      private final @Nullable String first_name;

      private final @Nullable String full_name;

      private final @Nullable Object email;

      private final @Nullable String gender;

      private final @Nullable String bday;

      private final @Nullable String age;

      private final @Nullable String phone;

      private final @Nullable Boolean phone_verified;

      private final @Nullable String register_date;

      public Profile(@Nullable String user_id, @Nullable String first_name,
          @Nullable String full_name, @Nullable Object email, @Nullable String gender,
          @Nullable String bday, @Nullable String age, @Nullable String phone,
          @Nullable Boolean phone_verified, @Nullable String register_date) {
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
      }

      public @Nullable String user_id() {
        return this.user_id;
      }

      public @Nullable String first_name() {
        return this.first_name;
      }

      public @Nullable String full_name() {
        return this.full_name;
      }

      public @Nullable Object email() {
        return this.email;
      }

      public @Nullable String gender() {
        return this.gender;
      }

      public @Nullable String bday() {
        return this.bday;
      }

      public @Nullable String age() {
        return this.age;
      }

      public @Nullable String phone() {
        return this.phone;
      }

      public @Nullable Boolean phone_verified() {
        return this.phone_verified;
      }

      public @Nullable String register_date() {
        return this.register_date;
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
          + "register_date=" + register_date
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
           && ((this.phone_verified == null) ? (that.phone_verified == null) : this.phone_verified.equals(that.phone_verified))
           && ((this.register_date == null) ? (that.register_date == null) : this.register_date.equals(that.register_date));
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
        h ^= (phone_verified == null) ? 0 : phone_verified.hashCode();
        h *= 1000003;
        h ^= (register_date == null) ? 0 : register_date.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Profile> {
        final Field[] fields = {
          Field.forString("user_id", "user_id", null, true),
          Field.forString("first_name", "first_name", null, true),
          Field.forString("full_name", "full_name", null, true),
          Field.forCustomType("email", "email", null, true, CustomType.EMAIL),
          Field.forString("gender", "gender", null, true),
          Field.forString("bday", "bday", null, true),
          Field.forString("age", "age", null, true),
          Field.forString("phone", "phone", null, true),
          Field.forBoolean("phone_verified", "phone_verified", null, true),
          Field.forString("register_date", "register_date", null, true)
        };

        @Override
        public Profile map(ResponseReader reader) throws IOException {
          final String user_id = reader.read(fields[0]);
          final String first_name = reader.read(fields[1]);
          final String full_name = reader.read(fields[2]);
          final Object email = reader.read(fields[3]);
          final String gender = reader.read(fields[4]);
          final String bday = reader.read(fields[5]);
          final String age = reader.read(fields[6]);
          final String phone = reader.read(fields[7]);
          final Boolean phone_verified = reader.read(fields[8]);
          final String register_date = reader.read(fields[9]);
          return new Profile(user_id, first_name, full_name, email, gender, bday, age, phone, phone_verified, register_date);
        }
      }
    }

    public static class Address1 {
      private final @Nullable String city_name;

      private final @Nullable String province_name;

      public Address1(@Nullable String city_name, @Nullable String province_name) {
        this.city_name = city_name;
        this.province_name = province_name;
      }

      public @Nullable String city_name() {
        return this.city_name;
      }

      public @Nullable String province_name() {
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
          Field.forString("city_name", "city_name", null, true),
          Field.forString("province_name", "province_name", null, true)
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

    public static class Wallet {
      private final boolean linked;

      private final @Nonnull String balance;

      private final int rawBalance;

      private final @Nullable List<Error> errors;

      public Wallet(boolean linked, @Nonnull String balance, int rawBalance,
          @Nullable List<Error> errors) {
        this.linked = linked;
        this.balance = balance;
        this.rawBalance = rawBalance;
        this.errors = errors;
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

      @Override
      public String toString() {
        return "Wallet{"
          + "linked=" + linked + ", "
          + "balance=" + balance + ", "
          + "rawBalance=" + rawBalance + ", "
          + "errors=" + errors
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
           && ((this.errors == null) ? (that.errors == null) : this.errors.equals(that.errors));
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
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Wallet> {
        final Error.Mapper errorFieldMapper = new Error.Mapper();

        final Field[] fields = {
          Field.forBoolean("linked", "linked", null, false),
          Field.forString("balance", "balance", null, false),
          Field.forInt("rawBalance", "rawBalance", null, false),
          Field.forList("errors", "errors", null, true, new Field.ObjectReader<Error>() {
            @Override public Error read(final ResponseReader reader) throws IOException {
              return errorFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Wallet map(ResponseReader reader) throws IOException {
          final boolean linked = reader.read(fields[0]);
          final String balance = reader.read(fields[1]);
          final int rawBalance = reader.read(fields[2]);
          final List<Error> errors = reader.read(fields[3]);
          return new Wallet(linked, balance, rawBalance, errors);
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
      private final @Nullable Integer topads_amount;

      private final @Nullable Boolean is_topads_user;

      public TopadsDeposit(@Nullable Integer topads_amount, @Nullable Boolean is_topads_user) {
        this.topads_amount = topads_amount;
        this.is_topads_user = is_topads_user;
      }

      public @Nullable Integer topads_amount() {
        return this.topads_amount;
      }

      public @Nullable Boolean is_topads_user() {
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
          return ((this.topads_amount == null) ? (that.topads_amount == null) : this.topads_amount.equals(that.topads_amount))
           && ((this.is_topads_user == null) ? (that.is_topads_user == null) : this.is_topads_user.equals(that.is_topads_user));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (topads_amount == null) ? 0 : topads_amount.hashCode();
        h *= 1000003;
        h ^= (is_topads_user == null) ? 0 : is_topads_user.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<TopadsDeposit> {
        final Field[] fields = {
          Field.forInt("topads_amount", "topads_amount", null, true),
          Field.forBoolean("is_topads_user", "is_topads_user", null, true)
        };

        @Override
        public TopadsDeposit map(ResponseReader reader) throws IOException {
          final Integer topads_amount = reader.read(fields[0]);
          final Boolean is_topads_user = reader.read(fields[1]);
          return new TopadsDeposit(topads_amount, is_topads_user);
        }
      }
    }
  }
}
