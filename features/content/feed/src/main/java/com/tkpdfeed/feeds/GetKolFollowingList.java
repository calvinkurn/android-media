package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
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
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class GetKolFollowingList implements Query<GetKolFollowingList.Data, GetKolFollowingList.Data, GetKolFollowingList.Variables> {
  public static final String OPERATION_DEFINITION = "query GetKolFollowingList($userID: Int!, $cursor: String, $limit: Int!) {\n"
      + "  get_user_kol_following(userID: $userID, cursor: $cursor, limit: $limit) {\n"
      + "    __typename\n"
      + "    profileKol {\n"
      + "      __typename\n"
      + "      following\n"
      + "      followers\n"
      + "      followed\n"
      + "      iskol\n"
      + "      id\n"
      + "      info\n"
      + "      bio\n"
      + "      name\n"
      + "      photo\n"
      + "    }\n"
      + "    users {\n"
      + "      __typename\n"
      + "      id\n"
      + "      followed\n"
      + "      name\n"
      + "      info\n"
      + "      photo\n"
      + "      userUrl\n"
      + "      userApplink\n"
      + "      isInfluencer\n"
      + "    }\n"
      + "    error\n"
      + "    lastCursor\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final GetKolFollowingList.Variables variables;

  public GetKolFollowingList(int userID, @Nullable String cursor, int limit) {
    variables = new GetKolFollowingList.Variables(userID, cursor, limit);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public GetKolFollowingList.Data wrapData(GetKolFollowingList.Data data) {
    return data;
  }

  @Override
  public GetKolFollowingList.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<GetKolFollowingList.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int userID;

    private final @Nullable String cursor;

    private final int limit;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int userID, @Nullable String cursor, int limit) {
      this.userID = userID;
      this.cursor = cursor;
      this.limit = limit;
      this.valueMap.put("userID", userID);
      this.valueMap.put("cursor", cursor);
      this.valueMap.put("limit", limit);
    }

    public int userID() {
      return userID;
    }

    public @Nullable String cursor() {
      return cursor;
    }

    public int limit() {
      return limit;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int userID;

    private @Nullable String cursor;

    private int limit;

    Builder() {
    }

    public Builder userID(int userID) {
      this.userID = userID;
      return this;
    }

    public Builder cursor(@Nullable String cursor) {
      this.cursor = cursor;
      return this;
    }

    public Builder limit(int limit) {
      this.limit = limit;
      return this;
    }

    public GetKolFollowingList build() {
      return new GetKolFollowingList(userID, cursor, limit);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Get_user_kol_following get_user_kol_following;

    public Data(@Nullable Get_user_kol_following get_user_kol_following) {
      this.get_user_kol_following = get_user_kol_following;
    }

    public @Nullable Get_user_kol_following get_user_kol_following() {
      return this.get_user_kol_following;
    }

    @Override
    public String toString() {
      return "Data{"
        + "get_user_kol_following=" + get_user_kol_following
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.get_user_kol_following == null) ? (that.get_user_kol_following == null) : this.get_user_kol_following.equals(that.get_user_kol_following));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (get_user_kol_following == null) ? 0 : get_user_kol_following.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Get_user_kol_following.Mapper get_user_kol_followingFieldMapper = new Get_user_kol_following.Mapper();

      final Field[] fields = {
        Field.forObject("get_user_kol_following", "get_user_kol_following", new UnmodifiableMapBuilder<String, Object>(3)
          .put("cursor", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "cursor")
          .build())
          .put("limit", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "limit")
          .build())
          .put("userID", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "userID")
          .build())
        .build(), true, new Field.ObjectReader<Get_user_kol_following>() {
          @Override public Get_user_kol_following read(final ResponseReader reader) throws IOException {
            return get_user_kol_followingFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Get_user_kol_following get_user_kol_following = reader.read(fields[0]);
        return new Data(get_user_kol_following);
      }
    }

    public static class ProfileKol {
      private final @Nullable Integer following;

      private final @Nullable Integer followers;

      private final @Nullable Boolean followed;

      private final @Nullable Boolean iskol;

      private final @Nullable Integer id;

      private final @Nullable String info;

      private final @Nullable String bio;

      private final @Nullable String name;

      private final @Nullable String photo;

      public ProfileKol(@Nullable Integer following, @Nullable Integer followers,
          @Nullable Boolean followed, @Nullable Boolean iskol, @Nullable Integer id,
          @Nullable String info, @Nullable String bio, @Nullable String name,
          @Nullable String photo) {
        this.following = following;
        this.followers = followers;
        this.followed = followed;
        this.iskol = iskol;
        this.id = id;
        this.info = info;
        this.bio = bio;
        this.name = name;
        this.photo = photo;
      }

      public @Nullable Integer following() {
        return this.following;
      }

      public @Nullable Integer followers() {
        return this.followers;
      }

      public @Nullable Boolean followed() {
        return this.followed;
      }

      public @Nullable Boolean iskol() {
        return this.iskol;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String info() {
        return this.info;
      }

      public @Nullable String bio() {
        return this.bio;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String photo() {
        return this.photo;
      }

      @Override
      public String toString() {
        return "ProfileKol{"
          + "following=" + following + ", "
          + "followers=" + followers + ", "
          + "followed=" + followed + ", "
          + "iskol=" + iskol + ", "
          + "id=" + id + ", "
          + "info=" + info + ", "
          + "bio=" + bio + ", "
          + "name=" + name + ", "
          + "photo=" + photo
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof ProfileKol) {
          ProfileKol that = (ProfileKol) o;
          return ((this.following == null) ? (that.following == null) : this.following.equals(that.following))
           && ((this.followers == null) ? (that.followers == null) : this.followers.equals(that.followers))
           && ((this.followed == null) ? (that.followed == null) : this.followed.equals(that.followed))
           && ((this.iskol == null) ? (that.iskol == null) : this.iskol.equals(that.iskol))
           && ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.info == null) ? (that.info == null) : this.info.equals(that.info))
           && ((this.bio == null) ? (that.bio == null) : this.bio.equals(that.bio))
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.photo == null) ? (that.photo == null) : this.photo.equals(that.photo));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (following == null) ? 0 : following.hashCode();
        h *= 1000003;
        h ^= (followers == null) ? 0 : followers.hashCode();
        h *= 1000003;
        h ^= (followed == null) ? 0 : followed.hashCode();
        h *= 1000003;
        h ^= (iskol == null) ? 0 : iskol.hashCode();
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (info == null) ? 0 : info.hashCode();
        h *= 1000003;
        h ^= (bio == null) ? 0 : bio.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (photo == null) ? 0 : photo.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<ProfileKol> {
        final Field[] fields = {
          Field.forInt("following", "following", null, true),
          Field.forInt("followers", "followers", null, true),
          Field.forBoolean("followed", "followed", null, true),
          Field.forBoolean("iskol", "iskol", null, true),
          Field.forInt("id", "id", null, true),
          Field.forString("info", "info", null, true),
          Field.forString("bio", "bio", null, true),
          Field.forString("name", "name", null, true),
          Field.forString("photo", "photo", null, true)
        };

        @Override
        public ProfileKol map(ResponseReader reader) throws IOException {
          final Integer following = reader.read(fields[0]);
          final Integer followers = reader.read(fields[1]);
          final Boolean followed = reader.read(fields[2]);
          final Boolean iskol = reader.read(fields[3]);
          final Integer id = reader.read(fields[4]);
          final String info = reader.read(fields[5]);
          final String bio = reader.read(fields[6]);
          final String name = reader.read(fields[7]);
          final String photo = reader.read(fields[8]);
          return new ProfileKol(following, followers, followed, iskol, id, info, bio, name, photo);
        }
      }
    }

    public static class User {
      private final @Nullable Integer id;

      private final @Nullable Boolean followed;

      private final @Nullable String name;

      private final @Nullable String info;

      private final @Nullable String photo;

      private final @Nullable String userUrl;

      private final @Nullable String userApplink;

      private final @Nullable Boolean isInfluencer;

      public User(@Nullable Integer id, @Nullable Boolean followed, @Nullable String name,
          @Nullable String info, @Nullable String photo, @Nullable String userUrl,
          @Nullable String userApplink, @Nullable Boolean isInfluencer) {
        this.id = id;
        this.followed = followed;
        this.name = name;
        this.info = info;
        this.photo = photo;
        this.userUrl = userUrl;
        this.userApplink = userApplink;
        this.isInfluencer = isInfluencer;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable Boolean followed() {
        return this.followed;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String info() {
        return this.info;
      }

      public @Nullable String photo() {
        return this.photo;
      }

      public @Nullable String userUrl() {
        return this.userUrl;
      }

      public @Nullable String userApplink() {
        return this.userApplink;
      }

      public @Nullable Boolean isInfluencer() {
        return this.isInfluencer;
      }

      @Override
      public String toString() {
        return "User{"
          + "id=" + id + ", "
          + "followed=" + followed + ", "
          + "name=" + name + ", "
          + "info=" + info + ", "
          + "photo=" + photo + ", "
          + "userUrl=" + userUrl + ", "
          + "userApplink=" + userApplink + ", "
          + "isInfluencer=" + isInfluencer
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof User) {
          User that = (User) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.followed == null) ? (that.followed == null) : this.followed.equals(that.followed))
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.info == null) ? (that.info == null) : this.info.equals(that.info))
           && ((this.photo == null) ? (that.photo == null) : this.photo.equals(that.photo))
           && ((this.userUrl == null) ? (that.userUrl == null) : this.userUrl.equals(that.userUrl))
           && ((this.userApplink == null) ? (that.userApplink == null) : this.userApplink.equals(that.userApplink))
           && ((this.isInfluencer == null) ? (that.isInfluencer == null) : this.isInfluencer.equals(that.isInfluencer));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (followed == null) ? 0 : followed.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (info == null) ? 0 : info.hashCode();
        h *= 1000003;
        h ^= (photo == null) ? 0 : photo.hashCode();
        h *= 1000003;
        h ^= (userUrl == null) ? 0 : userUrl.hashCode();
        h *= 1000003;
        h ^= (userApplink == null) ? 0 : userApplink.hashCode();
        h *= 1000003;
        h ^= (isInfluencer == null) ? 0 : isInfluencer.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<User> {
        final Field[] fields = {
          Field.forInt("id", "id", null, true),
          Field.forBoolean("followed", "followed", null, true),
          Field.forString("name", "name", null, true),
          Field.forString("info", "info", null, true),
          Field.forString("photo", "photo", null, true),
          Field.forString("userUrl", "userUrl", null, true),
          Field.forString("userApplink", "userApplink", null, true),
          Field.forBoolean("isInfluencer", "isInfluencer", null, true)
        };

        @Override
        public User map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final Boolean followed = reader.read(fields[1]);
          final String name = reader.read(fields[2]);
          final String info = reader.read(fields[3]);
          final String photo = reader.read(fields[4]);
          final String userUrl = reader.read(fields[5]);
          final String userApplink = reader.read(fields[6]);
          final Boolean isInfluencer = reader.read(fields[7]);
          return new User(id, followed, name, info, photo, userUrl, userApplink, isInfluencer);
        }
      }
    }

    public static class Get_user_kol_following {
      private final @Nullable ProfileKol profileKol;

      private final @Nullable List<User> users;

      private final @Nullable String error;

      private final @Nullable String lastCursor;

      public Get_user_kol_following(@Nullable ProfileKol profileKol, @Nullable List<User> users,
          @Nullable String error, @Nullable String lastCursor) {
        this.profileKol = profileKol;
        this.users = users;
        this.error = error;
        this.lastCursor = lastCursor;
      }

      public @Nullable ProfileKol profileKol() {
        return this.profileKol;
      }

      public @Nullable List<User> users() {
        return this.users;
      }

      public @Nullable String error() {
        return this.error;
      }

      public @Nullable String lastCursor() {
        return this.lastCursor;
      }

      @Override
      public String toString() {
        return "Get_user_kol_following{"
          + "profileKol=" + profileKol + ", "
          + "users=" + users + ", "
          + "error=" + error + ", "
          + "lastCursor=" + lastCursor
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Get_user_kol_following) {
          Get_user_kol_following that = (Get_user_kol_following) o;
          return ((this.profileKol == null) ? (that.profileKol == null) : this.profileKol.equals(that.profileKol))
           && ((this.users == null) ? (that.users == null) : this.users.equals(that.users))
           && ((this.error == null) ? (that.error == null) : this.error.equals(that.error))
           && ((this.lastCursor == null) ? (that.lastCursor == null) : this.lastCursor.equals(that.lastCursor));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (profileKol == null) ? 0 : profileKol.hashCode();
        h *= 1000003;
        h ^= (users == null) ? 0 : users.hashCode();
        h *= 1000003;
        h ^= (error == null) ? 0 : error.hashCode();
        h *= 1000003;
        h ^= (lastCursor == null) ? 0 : lastCursor.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Get_user_kol_following> {
        final ProfileKol.Mapper profileKolFieldMapper = new ProfileKol.Mapper();

        final User.Mapper userFieldMapper = new User.Mapper();

        final Field[] fields = {
          Field.forObject("profileKol", "profileKol", null, true, new Field.ObjectReader<ProfileKol>() {
            @Override public ProfileKol read(final ResponseReader reader) throws IOException {
              return profileKolFieldMapper.map(reader);
            }
          }),
          Field.forList("users", "users", null, true, new Field.ObjectReader<User>() {
            @Override public User read(final ResponseReader reader) throws IOException {
              return userFieldMapper.map(reader);
            }
          }),
          Field.forString("error", "error", null, true),
          Field.forString("lastCursor", "lastCursor", null, true)
        };

        @Override
        public Get_user_kol_following map(ResponseReader reader) throws IOException {
          final ProfileKol profileKol = reader.read(fields[0]);
          final List<User> users = reader.read(fields[1]);
          final String error = reader.read(fields[2]);
          final String lastCursor = reader.read(fields[3]);
          return new Get_user_kol_following(profileKol, users, error, lastCursor);
        }
      }
    }
  }
}
